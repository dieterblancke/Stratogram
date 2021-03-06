package com.dbsoftwares.stratogram.commands.subcommands;

import com.dbsoftwares.commands.CommandCall;
import com.dbsoftwares.stratogram.Stratogram;
import com.dbsoftwares.stratogram.book.BookEditor;
import com.dbsoftwares.stratogram.holograms.StoredHologram;
import com.dbsoftwares.stratogram.utils.XMaterial;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;
import java.util.Optional;

public class StratogramCreateSubCommandCall implements CommandCall
{

    @Override
    public void onExecute( final CommandSender sender, final List<String> args, final List<String> params )
    {
        if ( !(sender instanceof Player) )
        {
            sender.sendMessage( ChatColor.YELLOW + "The console cannot execute this command!" );
            return;
        }
        if ( args.isEmpty() )
        {
            sender.sendMessage( ChatColor.YELLOW + "Please use " + ChatColor.AQUA + "/sg bcreate (name)" + ChatColor.YELLOW + "!" );
            return;
        }
        final Player player = (Player) sender;
        final Location location = player.getLocation();
        final String name = args.get( 0 );

        if ( Stratogram.getInstance().searchHologram( name ) != null )
        {
            sender.sendMessage( ChatColor.YELLOW + "Please use " + ChatColor.AQUA + "unique names" + ChatColor.YELLOW + " for holograms!" );
            return;
        }

        BookEditor.newEditor()
                .onSubmit( ( pages, text ) ->
                {
                    final StoredHologram hologram = new StoredHologram( name, pages, location );

                    Stratogram.getInstance().getStoredHolograms().add( hologram );
                    Stratogram.getInstance().saveHolograms();
                    player.sendMessage( ChatColor.YELLOW + "A hologram was created at the location you executed this command!" );
                } )
                .open( player );

        player.sendMessage( ChatColor.YELLOW + "Please open this writable book and write the hologram text!" );
    }
}
