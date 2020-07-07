package com.dbsoftwares.stratogram.commands.subcommands;

import com.dbsoftwares.commands.CommandCall;
import com.dbsoftwares.stratogram.book.BookEditor;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class StratogramTestSubCommandCall implements CommandCall
{
    @Override
    public void onExecute( final CommandSender sender, final List<String> args, final List<String> params )
    {
        if ( sender instanceof Player )
        {
            final Player player = (Player) sender;

            BookEditor.newEditor()
                    .onSubmit( text ->
                    {
                        for ( String line : text )
                        {
                            player.sendMessage( ChatColor.translateAlternateColorCodes( '&', line ) );
                        }
                    } )
                    .open( player );

            player.sendMessage( ChatColor.YELLOW + "&ePlease open this writable book and write the hologram text!" );
        }
    }
}
