package com.dbsoftwares.stratogram.commands;

import com.dbsoftwares.commands.CommandBuilder;
import com.dbsoftwares.commands.CommandCall;
import com.dbsoftwares.commands.ParentCommand;
import com.dbsoftwares.stratogram.commands.subcommands.StratogramCreateSubCommandCall;
import com.dbsoftwares.stratogram.commands.subcommands.StratogramTestSubCommandCall;

public class StratogramCommandCall extends ParentCommand implements CommandCall
{

    public StratogramCommandCall()
    {
        super( sender ->
        {
            // TODO: send help message
        } );

        this.registerSubCommand( CommandBuilder.builder()
                .name( "test" )
                .permission( "stratogram.admin.test" )
                .executable( new StratogramTestSubCommandCall() )
                .build()
        );
        this.registerSubCommand( CommandBuilder.builder()
                .name( "create" )
                .permission( "stratogram.admin.create" )
                .executable( new StratogramCreateSubCommandCall() )
                .build()
        );
    }
}
