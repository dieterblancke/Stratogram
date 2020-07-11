package com.dbsoftwares.stratogram.book;

import com.dbsoftwares.stratogram.utils.XMaterial;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.BiConsumer;

public class BookEditor
{

    private static final List<BookEditor> editors = Collections.synchronizedList( new ArrayList<>() );

    private String currentlyOpenedBy;
    private BiConsumer<List<String>, String[]> hologramSubmitConsumer;
    private List<String> pages;

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

        final ItemStack item = XMaterial.WRITABLE_BOOK.parseItem();

        if ( this.pages != null && item != null )
        {
            final BookMeta meta = (BookMeta) item.getItemMeta();

            meta.setPages( this.pages );

            item.setItemMeta( meta );
        }

        player.getInventory().setItem( oldSlot, item );
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

    public void submit( List<String> pages )
    {
        final List<String> tempPages = new ArrayList<>();
        for ( String page : pages )
        {
            tempPages.add( ChatColor.stripColor( page ) );
        }
        pages = tempPages;

        String text = String.join( "\n", pages );
        if ( this.hologramSubmitConsumer == null )
        {
            return;
        }

        this.hologramSubmitConsumer.accept( pages, text.split( "\n" ) );
        this.exit();
    }

    public BookEditor onSubmit( final BiConsumer<List<String>, String[]> hologramSubmitConsumer )
    {
        this.hologramSubmitConsumer = hologramSubmitConsumer;
        return this;
    }

    public BookEditor withPages( final List<String> lines )
    {
        this.pages = lines;
        return this;
    }
}
