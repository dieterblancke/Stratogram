package com.dbsoftwares.stratogram.listeners;

import com.dbsoftwares.stratogram.book.BookEditor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.inventory.meta.BookMeta;

public class BookEditListener implements Listener
{

    @EventHandler
    public void onBookEdit( final PlayerEditBookEvent event )
    {
        final BookEditor editor = BookEditor.findEditor( event.getPlayer() );
        final BookMeta meta = event.getNewBookMeta();

        if ( editor == null )
        {
            return;
        }

        editor.submit( String.join( "\n", meta.getPages() ) );
    }
}
