package com.dbsoftwares.stratogram.holograms;

import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.configuration.json.JsonSection;
import com.dbsoftwares.stratogram.api.line.HologramLine;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class StoredHologram extends StratoHologram
{

    public StoredHologram( final Location location )
    {
        super( location );
    }

    public StoredHologram( final ISection section )
    {
        this( section.spigot().getLocation( "location" ) );

        final List<ISection> sections = section.getSectionList( "lines" );
        for ( ISection lineSection : sections )
        {
            final String type = lineSection.getString( "type" );

            if ( type.equalsIgnoreCase( "text" ) )
            {
                super.addTextLine( lineSection.getString( "data" ) );
            }
            else if ( type.equalsIgnoreCase( "item" ) )
            {
                super.addItemLine( lineSection.spigot().getItemStack( "data" ) );
            }
        }
    }

    public ISection saveHologram()
    {
        final JsonSection section = new JsonSection();
        final Location location = super.getLocation();
        final List<ISection> lines = this.getLinesAsSectionList();

        if ( location == null || lines.isEmpty() )
        {
            return null;
        }

        section.set( "location", location );
        section.set( "lines", lines );

        return section;
    }

    private List<ISection> getLinesAsSectionList() {
        final List<ISection> lines = new ArrayList<>();

        for ( HologramLine line : super.lines )
        {
            lines.add( line.asSection() );
        }
        return lines;
    }
}
