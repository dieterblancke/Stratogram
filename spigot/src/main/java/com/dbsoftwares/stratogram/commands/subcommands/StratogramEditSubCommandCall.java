package com.dbsoftwares.stratogram.commands.subcommands;

import com.dbsoftwares.commands.CommandCall;
import com.dbsoftwares.stratogram.Stratogram;
import com.dbsoftwares.stratogram.book.BookEditor;
import com.dbsoftwares.stratogram.holograms.StoredHologram;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class StratogramEditSubCommandCall implements CommandCall
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
            sender.sendMessage( ChatColor.YELLOW + "Please use " + ChatColor.AQUA + "/sg bedit (name)" + ChatColor.YELLOW + "!" );
            return;
        }
        final Player player = (Player) sender;
        final String name = args.get( 0 );
        final StoredHologram hologram = Stratogram.getInstance().searchHologram( name );

        if ( hologram == null )
        {
            sender.sendMessage( ChatColor.YELLOW + "Could not find a hologram with the name " + ChatColor.AQUA + name + ChatColor.YELLOW + "!" );
            return;
        }

        BookEditor.newEditor()
                .onSubmit( ( pages, text ) ->
                {
                    hologram.setPages( pages );
                    hologram.update();
                    Stratogram.getInstance().saveHolograms();

                    player.sendMessage( ChatColor.YELLOW + "The hologram " + ChatColor.AQUA + name + ChatColor.YELLOW + " was edited successfully!" );
                } )
                .withPages( hologram.getPages() )
                .open( player );

        player.sendMessage( ChatColor.YELLOW + "Please open this writable book and edit the hologram text!" );
    }
}
