package com.dbsoftwares.stratogram.holograms;

import com.dbsoftwares.stratogram.Stratogram;
import com.dbsoftwares.stratogram.api.Hologram;
import com.dbsoftwares.stratogram.api.line.HologramLine;
import org.bukkit.Location;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StratoHologram implements Hologram
{

    private WeakReference<Location> location;
    private List<HologramLine> lines = Collections.synchronizedList( new ArrayList<>() );

    public StratoHologram( final Location location )
    {
        this.location = new WeakReference<>( location );
    }

    @Override
    public HologramLine addLine( final HologramLine line )
    {
        return null;
    }

    @Override
    public HologramLine addLine( final int index, final HologramLine line )
    {
        return null;
    }

    @Override
    public HologramLine getLine( final int index )
    {
        return null;
    }

    @Override
    public HologramLine removeLine( final int index )
    {
        return null;
    }

    @Override
    public void clear()
    {

    }

    @Override
    public int size()
    {
        return lines.size();
    }

    @Override
    public void teleport( final Location location )
    {

    }

    @Override
    public Location getLocation()
    {
        return this.location == null ? null : this.location.get();
    }

    @Override
    public long getCreatedAt()
    {
        return 0;
    }

    @Override
    public void delete()
    {

    }

    @Override
    public void update()
    {
        if ( this.location == null || this.location.get() == null )
        {
            this.attemptDeletion();
            Stratogram.getInstance().debug( "A hologram got deleted because the location got removed by garbage collection." );
            return;
        }

//        for ( HologramLine line : lines ) {
//
//        }
    }

    private void attemptDeletion()
    {
        // TODO
    }
}
