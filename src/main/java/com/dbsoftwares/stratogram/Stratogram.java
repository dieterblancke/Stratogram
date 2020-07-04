package com.dbsoftwares.stratogram;

import com.google.common.reflect.ClassPath;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Set;

public class Stratogram extends JavaPlugin
{

    @Override
    public void onEnable()
    {
        this.getLogger().info( "Registering listeners ..." );
        this.registerListeners();
    }

    @Override
    public void onDisable()
    {

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
}
