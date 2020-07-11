package com.dbsoftwares.stratogram.commands.subcommands;

import com.dbsoftwares.commands.CommandCall;
import com.dbsoftwares.stratogram.Stratogram;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.command.CommandSender;

import java.util.List;

public class StratogramReloadSubCommandCall implements CommandCall
{

    @Override
    public void onExecute( final CommandSender sender, final List<String> args, final List<String> params )
    {
        Stratogram.getInstance().reload();
        sender.sendMessage( ChatColor.GREEN + "Stratogram and it's configuration files have been reloaded!" );
    }
}
