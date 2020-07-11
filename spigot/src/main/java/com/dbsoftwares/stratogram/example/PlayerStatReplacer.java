package com.dbsoftwares.stratogram.example;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.function.UnaryOperator;

public class PlayerStatReplacer implements UnaryOperator<String>
{

    private String playerName;

    public PlayerStatReplacer( final String playerName )
    {
        this.playerName = playerName;
    }

    @Override
    public String apply( final String line )
    {
        final Player player = Bukkit.getPlayer( this.playerName );

        // Get data from this player object :)

        return line
                .replace( "{name}", player == null ? this.playerName : player.getName() )
                .replace( "{kills}", "123" )
                .replace( "{deaths}", "456" );
    }
}
