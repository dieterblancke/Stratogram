package com.dbsoftwares.stratogram.listeners;

import com.dbsoftwares.stratogram.book.BookEditor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerQuitListener implements Listener
{

    @EventHandler
    public void onQuitBookEditorUnload( final PlayerQuitEvent event )
    {
        final BookEditor editor = BookEditor.findEditor( event.getPlayer() );

        if ( editor != null )
        {
            editor.exit();
        }
    }
}
