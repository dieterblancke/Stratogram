package com.dbsoftwares.stratogram.pluginhooks.hooks;

import com.dbsoftwares.stratogram.pluginhooks.PluginHook;
import org.bukkit.Bukkit;

public class PlaceHolderAPIHook implements PluginHook
{

    @Override
    public boolean isPresent()
    {
        return Bukkit.getPluginManager().isPluginEnabled( "PlaceHolderAPI" );
    }

    public boolean hasPlaceHolders( final String text )
    {
        return me.clip.placeholderapi.PlaceholderAPI.containsPlaceholders( text )
                || me.clip.placeholderapi.PlaceholderAPI.containsBracketPlaceholders( text );
    }

    public String setPlaceHolders( final String text )
    {
        return me.clip.placeholderapi.PlaceholderAPI.setPlaceholders( null, text );
    }
}
