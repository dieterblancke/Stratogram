package com.dbsoftwares.stratogram.commands.subcommands;

import com.dbsoftwares.commands.CommandCall;
import com.dbsoftwares.stratogram.Stratogram;
import com.dbsoftwares.stratogram.holograms.StoredHologram;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class StratogramDeleteSubCommand implements CommandCall
{

    @Override
    public void onExecute( final CommandSender sender, final List<String> args, final List<String> params )
    {
        if ( args.isEmpty() )
        {
            sender.sendMessage( ChatColor.YELLOW + "Please use " + ChatColor.AQUA + "/sg delete (name)" + ChatColor.YELLOW + "!" );
            return;
        }
        final String name = args.get( 0 );
        final StoredHologram hologram = Stratogram.getInstance().searchHologram( name );

        if ( hologram == null )
        {
            sender.sendMessage( ChatColor.YELLOW + "Could not find a hologram with the name " + ChatColor.AQUA + name + ChatColor.YELLOW + "!" );
            return;
        }

        hologram.delete();
        Stratogram.getInstance().getStoredHolograms().remove( hologram );
        Stratogram.getInstance().saveHolograms();
        sender.sendMessage( ChatColor.YELLOW + "Successfully removed the hologram named " + ChatColor.AQUA + name + ChatColor.YELLOW + "!" );
    }
}
