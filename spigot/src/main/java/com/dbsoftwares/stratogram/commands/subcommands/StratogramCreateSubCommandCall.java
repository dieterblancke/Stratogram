package com.dbsoftwares.stratogram.commands.subcommands;

import com.dbsoftwares.commands.CommandCall;
import com.dbsoftwares.stratogram.book.BookEditor;
import com.dbsoftwares.stratogram.holograms.StoredHologram;
import com.dbsoftwares.stratogram.holograms.line.StratoTextLine;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class StratogramCreateSubCommandCall implements CommandCall
{
    @Override
    public void onExecute( final CommandSender sender, final List<String> args, final List<String> params )
    {
        if ( !( sender instanceof Player ) )
        {
            sender.sendMessage( ChatColor.RED + "The console cannot execute this command!" );
            return;
        }
        final Player player = (Player) sender;
        final Location location = player.getLocation();

        BookEditor.newEditor()
                .onSubmit( text ->
                {
                    final StoredHologram hologram = new StoredHologram( location );


                    for ( String line : text )
                    {
                        hologram.addTextLine( line );
                        player.sendMessage( ChatColor.translateAlternateColorCodes( '&', line ) );
                    }
                } )
                .open( player );

        player.sendMessage( ChatColor.YELLOW + "&ePlease open this writable book and write the hologram text!" );
    }
}
