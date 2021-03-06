package com.dbsoftwares.stratogram;

import com.dbsoftwares.commands.Command;
import com.dbsoftwares.commands.CommandBuilder;
import com.dbsoftwares.configuration.api.FileStorageType;
import com.dbsoftwares.configuration.api.IConfiguration;
import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.stratogram.api.Hologram;
import com.dbsoftwares.stratogram.commands.StratogramCommandCall;
import com.dbsoftwares.stratogram.holograms.StoredHologram;
import com.dbsoftwares.stratogram.api.nms.NMSHologramManager;
import com.dbsoftwares.stratogram.utils.ReflectionUtils;
import com.google.common.reflect.ClassPath;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.logging.Level;

@Getter
public class Stratogram extends JavaPlugin
{

    @Getter
    private static Stratogram instance;
    private final List<StoredHologram> storedHolograms = Collections.synchronizedList( new ArrayList<>() );
    private final List<Hologram> allHolograms = Collections.synchronizedList( new ArrayList<>() );
    private IConfiguration configuration;
    private IConfiguration holograms;
    private NMSHologramManager hologramManager;
    private BukkitTask storedHologramUpdateTask;

    @Override
    public void onEnable()
    {
        instance = this;

        this.getLogger().info( "Searching NMS handler for " + ReflectionUtils.getServerVersion() + " ..." );
        this.registerHologramManager();

        this.getLogger().info( "Loading configuration data ..." );
        this.loadConfigurationFiles();

        this.getLogger().info( "Registering commands ..." );
        this.registerCommands();

        this.getLogger().info( "Registering listeners ..." );
        this.registerListeners();

        this.getLogger().info( "Registering tasks ..." );
        this.registerTasks();
    }

    @Override
    public void onDisable()
    {
        for ( StoredHologram hologram : this.storedHolograms )
        {
            hologram.delete();
        }
    }

    public void reload()
    {
        for ( StoredHologram hologram : this.storedHolograms )
        {
            hologram.delete();
        }
        this.storedHolograms.clear();
        this.loadConfigurationFiles();
        this.registerTasks();
    }

    private void loadConfigurationFiles()
    {
        this.configuration = loadConfigurationFile( FileStorageType.YAML, "config.yml", true );
        this.holograms = loadConfigurationFile( FileStorageType.JSON, "holograms.json", false );

        if ( this.holograms != null )
        {
            final List<ISection> hologramSections = this.holograms.getSectionList( "holograms" );

            if ( hologramSections != null && !hologramSections.isEmpty() )
            {
                for ( ISection section : hologramSections )
                {
                    this.storedHolograms.add( new StoredHologram( section ) );
                }
            }
        }
    }

    public void saveHolograms()
    {
        final List<ISection> sections = new ArrayList<>();
        for ( StoredHologram hologram : this.storedHolograms )
        {
            try
            {
                sections.add( hologram.asSection() );
            }
            catch ( RuntimeException e )
            {
                e.printStackTrace();
            }
        }
        holograms.set( "holograms", sections );
        try
        {
            holograms.save();
        }
        catch ( IOException e )
        {
            e.printStackTrace();
        }
    }

    private IConfiguration loadConfigurationFile( final FileStorageType type, final String fileName, final boolean loadFromResources )
    {
        final File file = new File( this.getDataFolder(), fileName );

        if ( !file.exists() )
        {
            if ( loadFromResources )
            {
                IConfiguration.createDefaultFile( getResource( fileName ), file );
            }
            else
            {
                try
                {
                    file.createNewFile();
                }
                catch ( IOException e )
                {
                    e.printStackTrace();
                    return null;
                }
            }
        }
        return IConfiguration.loadConfiguration( type, file );
    }

    private void registerHologramManager()
    {
        try
        {
            final Class<?> clazz = Class.forName( "com.dbsoftwares.stratogram.nms." + ReflectionUtils.getServerVersion() + ".NMSStratogramManager" );

            this.hologramManager = (NMSHologramManager) clazz.getConstructor().newInstance();
        }
        catch ( ClassNotFoundException e )
        {
            this.getLogger().log( Level.SEVERE, "Could not find a NMS handler for " + ReflectionUtils.getServerVersion() + "! Stratogram will shut down now.", e );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
    }

    private void registerCommands()
    {
        // Unregistering all existing commands (only used on reload)
        Command.unregisterAll( this );

        // Registering main command
        CommandBuilder.builder()
                .name( "stratograms" )
                .aliases( "sg", "stratogram", "strato", "holo" )
                .enabled( true )
                .permission( "stratogram.admin" )
                .executable( new StratogramCommandCall() )
                .build()
                .register( this );
    }

    @SuppressWarnings( "all" )
    private void registerListeners()
    {
        try
        {
            final Set<ClassPath.ClassInfo> classes = ClassPath
                    .from( this.getClassLoader() )
                    .getTopLevelClassesRecursive( "com.dbsoftwares.stratogram.listeners" );

            for ( ClassPath.ClassInfo clazz : classes )
            {
                final Class<?> loadedClazz = clazz.load();

                if ( Listener.class.isAssignableFrom( loadedClazz ) )
                {
                    final Listener listener = (Listener) loadedClazz.getConstructor().newInstance();

                    System.out.println( "Registering listener " + loadedClazz.getName() + " ..." );
                    Bukkit.getPluginManager().registerEvents( listener, this );
                }
            }
        }
        catch ( IOException | NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e )
        {
            e.printStackTrace();
        }
    }

    private void registerTasks()
    {
        if ( this.storedHologramUpdateTask != null )
        {
            this.storedHologramUpdateTask.cancel();
        }
        final int delay = this.configuration.getInteger( "update-task" ) * 20;

        this.storedHologramUpdateTask = new BukkitRunnable()
        {
            @Override
            public void run()
            {
                for ( StoredHologram hologram : storedHolograms )
                {
                    hologram.update();
                }
            }
        }.runTaskTimer( this, delay, delay );
    }

    public void debug( final String text )
    {
        if ( this.configuration.getBoolean( "debug" ) )
        {
            this.getLogger().info( text );
        }
    }

    public StoredHologram searchHologram( final String name )
    {
        for ( StoredHologram hologram : this.storedHolograms )
        {
            if ( hologram.getName().equalsIgnoreCase( name ) )
            {
                return hologram;
            }
        }
        return null;
    }
}
