package com.dbsoftwares.stratogram.book;

import com.dbsoftwares.stratogram.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

public class BookEditor
{

    private static final List<BookEditor> editors = Collections.synchronizedList( new ArrayList<>() );
    private static final ItemStack BOOK_AND_QUILL = XMaterial.WRITABLE_BOOK.parseItem();

    private String currentlyOpenedBy;
    private Consumer<String[]> hologramSubmitConsumer;

    private int oldSlot;
    private ItemStack oldItem;

    private BookEditor()
    {
        editors.add( this );
    }

    public static BookEditor newEditor()
    {
        return new BookEditor();
    }

    public static BookEditor findEditor( final Player player )
    {
        for ( BookEditor editor : editors )
        {
            if ( editor.currentlyOpenedBy.equalsIgnoreCase( player.getName() ) )
            {
                return editor;
            }
        }
        return null;
    }

    public void open( final Player player )
    {
        if ( currentlyOpenedBy != null )
        {
            return;
        }
        this.currentlyOpenedBy = player.getName();

        oldSlot = player.getInventory().getHeldItemSlot();
        oldItem = player.getInventory().getItem( oldSlot );

        player.getInventory().setItem( oldSlot, BOOK_AND_QUILL );
    }

    public void exit()
    {
        if ( currentlyOpenedBy != null )
        {
            final Player player = Bukkit.getPlayer( currentlyOpenedBy );
            player.getInventory().setItem( oldSlot, oldItem );
            this.currentlyOpenedBy = null;
        }
        editors.remove( this );
    }

    public void submit( String text )
    {
        text = ChatColor.stripColor( text );
        if ( this.hologramSubmitConsumer == null )
        {
            return;
        }
        this.hologramSubmitConsumer.accept( text.split( "\n" ) );
        this.exit();
    }

    public BookEditor onSubmit( final Consumer<String[]> hologramSubmitConsumer )
    {
        this.hologramSubmitConsumer = hologramSubmitConsumer;
        return this;
    }
}
