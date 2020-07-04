package com.dbsoftwares.stratogram.listeners;

import com.dbsoftwares.stratogram.book.BookEditor;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class TestListener implements Listener
{
    @EventHandler
    public void onChat( final AsyncPlayerChatEvent event )
    {
        if ( event.getMessage().contains( "test" ) )
        {
            BookEditor.newEditor()
                    .onSubmit( text ->
                    {
                        for ( String line : text )
                        {
                            event.getPlayer().sendMessage( ChatColor.translateAlternateColorCodes( '&', line ) );
                        }
                    } )
                    .open( event.getPlayer() );

            event.getPlayer().sendMessage( ChatColor.translateAlternateColorCodes( '&', "&ePlease open this writable book and write the hologram text!" ) );
        }
    }
}
