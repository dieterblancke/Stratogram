package com.dbsoftwares.stratogram.commands;

import com.dbsoftwares.commands.CommandBuilder;
import com.dbsoftwares.commands.CommandCall;
import com.dbsoftwares.commands.ParentCommand;
import com.dbsoftwares.stratogram.commands.subcommands.StratogramCreateSubCommandCall;
import com.dbsoftwares.stratogram.commands.subcommands.StratogramDeleteSubCommand;
import com.dbsoftwares.stratogram.commands.subcommands.StratogramEditSubCommandCall;
import com.dbsoftwares.stratogram.commands.subcommands.StratogramReloadSubCommandCall;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class StratogramCommandCall extends ParentCommand implements CommandCall
{

    public StratogramCommandCall()
    {
        super( sender ->
        {
            sender.sendMessage( ChatColor.YELLOW.toString() + ChatColor.BOLD.toString() + "Stratogram " + ChatColor.AQUA.toString() + "Commands Help:" );
            sender.sendMessage( ChatColor.AQUA.toString() + "- " + ChatColor.GRAY.toString() + "/stratogram bcreate - Hologram creator using a written book" );
        } );

        this.registerSubCommand( CommandBuilder.builder()
                .enabled( true )
                .name( "bcreate" )
                .permission( "stratogram.admin.create" )
                .executable( new StratogramCreateSubCommandCall() )
                .build()
        );
        this.registerSubCommand( CommandBuilder.builder()
                .enabled( true )
                .name( "delete" )
                .aliases( "rm", "del", "remove" )
                .permission( "stratogram.admin.delete" )
                .executable( new StratogramDeleteSubCommand() )
                .build()
        );
        this.registerSubCommand( CommandBuilder.builder()
                .enabled( true )
                .name( "edit" )
                .aliases( "change", "ch", "mod", "modify" )
                .permission( "stratogram.admin.edit" )
                .executable( new StratogramEditSubCommandCall() )
                .build()
        );
        this.registerSubCommand( CommandBuilder.builder()
                .enabled( true )
                .name( "reload" )
                .aliases( "rl", "rel" )
                .permission( "stratogram.admin.reload" )
                .executable( new StratogramReloadSubCommandCall() )
                .build()
        );
    }

    @Override
    public void onExecute( CommandSender sender, List<String> args, List<String> parameters )
    {
        super.onExecute( sender, args, parameters );
    }
}
