package com.dbsoftwares.stratogram.holograms;

import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.configuration.json.JsonSection;
import com.dbsoftwares.stratogram.api.line.HologramLine;
import com.dbsoftwares.stratogram.utils.Locations;
import com.dbsoftwares.stratogram.utils.XMaterial;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Data
@EqualsAndHashCode(callSuper = true)
public class StoredHologram extends StratoHologram
{

    private String name;
    private List<String> pages;

    public StoredHologram( final String name, final List<String> pages, final Location location )
    {
        super( location );
        this.name = name;
        this.setPages( pages );
    }

    public StoredHologram( final ISection section )
    {
        this( section.getString( "name" ), section.getStringList( "pages" ), Locations.toLocation( section.getString( "location" ) ) );
    }

    public void setPages( final List<String> pages )
    {
        this.clear();
        this.pages = pages;

        for ( String page : pages )
        {
            for ( String line : page.split( "\n" ) )
            {
                if ( line.startsWith( "item:" ) )
                {
                    final Optional<XMaterial> optionalXMaterial = XMaterial.matchXMaterial( line.replace( "item:", "" ) );

                    optionalXMaterial.ifPresent( xMaterial -> this.addItemLine( xMaterial.parseItem() ) );
                }
                else
                {
                    this.addTextLine( line );
                }
            }
        }
    }

    public ISection asSection()
    {
        final JsonSection section = new JsonSection();
        final Location location = super.getLocation();
        final List<ISection> lines = this.getLinesAsSectionList();

        if ( location == null )
        {
            throw new RuntimeException( "Location is null" );
        }
        if ( lines.isEmpty() )
        {
            throw new RuntimeException( "Lines are empty" );
        }

        section.set( "name", this.name );
        section.set( "pages", this.pages );
        section.set( "location", Locations.toString( location ) );

        return section;
    }

    private List<ISection> getLinesAsSectionList()
    {
        final List<ISection> lines = new ArrayList<>();

        for ( HologramLine line : super.lines )
        {
            lines.add( line.asSection() );
        }
        return lines;
    }
}
