package com.dbsoftwares.stratogram.holograms;

import com.dbsoftwares.configuration.api.ISection;

public class StoredHologram extends StratoHologram
{

    public StoredHologram( final ISection section )
    {
        super( section.spigot().getLocation( "location" ) );
    }
}
