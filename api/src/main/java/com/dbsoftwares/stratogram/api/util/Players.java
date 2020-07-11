package com.dbsoftwares.stratogram.api.util;

import lombok.Data;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Data
public class Players
{

    private final List<String> playerNames;
    private boolean all;

    private Players( final boolean isAll, final List<String> playerNames )
    {
        this.all = isAll;
        this.playerNames = playerNames;
    }

    public static Players all()
    {
        final Players players = of( Bukkit.getOnlinePlayers() );
        players.setAll( true );
        return players;
    }

    public static Players none()
    {
        return new Players( false, null );
    }

    public static Players of( final Collection<? extends Player> players )
    {
        return of( players.toArray( new Player[0] ) );
    }

    public static Players of( final Player... players )
    {
        final List<String> playerNames = new ArrayList<>();

        for ( Player player : players )
        {
            if ( player != null && player.isOnline() )
            {
                playerNames.add( player.getName() );
            }
        }
        return of( playerNames );
    }

    public static Players of( final List<String> players )
    {
        return of( players.toArray( new String[0] ) );
    }

    public static Players of( final String... playerNames )
    {
        return new Players( false, new ArrayList<>( Arrays.asList( playerNames ) ) );
    }

    public Collection<? extends Player> getPlayerList()
    {
        final List<Player> players = new ArrayList<>();

        for ( String playerName : this.playerNames )
        {
            final Player player = Bukkit.getPlayer( playerName );

            if ( player != null && player.isOnline() )
            {
                players.add( player );
            }
        }
        return players;
    }
}
