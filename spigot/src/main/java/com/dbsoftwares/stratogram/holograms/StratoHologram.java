package com.dbsoftwares.stratogram.holograms;

import com.dbsoftwares.stratogram.Stratogram;
import com.dbsoftwares.stratogram.api.Hologram;
import com.dbsoftwares.stratogram.api.line.HologramLine;
import org.bukkit.Location;

import java.lang.ref.WeakReference;

public class StratoHologram implements Hologram
{

    private WeakReference<Location> location;

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
        return 0;
    }

    @Override
    public void teleport( final Location location )
    {

    }

    @Override
    public Location getLocation()
    {
        return null;
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
