package com.dbsoftwares.stratogram;

import com.dbsoftwares.commands.Command;
import com.dbsoftwares.commands.CommandBuilder;
import com.dbsoftwares.configuration.api.IConfiguration;
import com.dbsoftwares.stratogram.commands.StratogramCommandCall;
import com.dbsoftwares.stratogram.nms.api.NMSHologramManager;
import com.dbsoftwares.stratogram.utils.ReflectionUtils;
import com.google.common.reflect.ClassPath;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;
import java.util.logging.Level;

@Getter
public class Stratogram extends JavaPlugin
{

    @Getter
    private static Stratogram instance;
    private IConfiguration configuration;
    private NMSHologramManager hologramManager;

    @Override
    public void onEnable()
    {
        instance = this;

        this.getLogger().info( "Loading configuration data ..." );
        final File configFile = new File( this.getDataFolder(), "config.yml" );
        if ( !configFile.exists() )
        {
            IConfiguration.createDefaultFile( getResource( "config.yml" ), configFile );
        }
        this.configuration = IConfiguration.loadYamlConfiguration( configFile );

        this.getLogger().info( "Searching NMS handler for " + ReflectionUtils.getServerVersion() + " ..." );
        this.registerHologramManager();

        this.getLogger().info( "Registering commands ..." );
        this.registerCommands();

        this.getLogger().info( "Registering listeners ..." );
        this.registerListeners();
    }

    private void registerHologramManager()
    {
        try
        {
            final Class<?> clazz = Class.forName( "com.dbsoftwares.stratogram.nms." + ReflectionUtils.getServerVersion() + ".NMSStratogramManager" );

            this.hologramManager = (NMSHologramManager) clazz.getConstructor().newInstance();
            this.hologramManager.registerCustomEntities();
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

    @Override
    public void onDisable()
    {

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

    @SuppressWarnings("all")
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

    public void debug( final String text )
    {
        if ( this.configuration.getBoolean( "debug" ) )
        {
            this.getLogger().info( text );
        }
    }
}
