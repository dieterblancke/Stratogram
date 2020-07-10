package com.dbsoftwares.stratogram.holograms;

import com.dbsoftwares.stratogram.Stratogram;
import com.dbsoftwares.stratogram.api.Hologram;
import com.dbsoftwares.stratogram.api.line.HologramLine;
import com.dbsoftwares.stratogram.holograms.line.StratoItemLine;
import com.dbsoftwares.stratogram.holograms.line.StratoTextLine;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StratoHologram implements Hologram
{

    protected final List<HologramLine> lines = Collections.synchronizedList( new ArrayList<>() );
    private final long createdAt;
    private WeakReference<Location> location;

    public StratoHologram( final Location location )
    {
        this.location = new WeakReference<>( location );
        this.createdAt = System.currentTimeMillis();
    }

    @Override
    public HologramLine addTextLine( final String text )
    {
        return this.addLine( new StratoTextLine( this.getNextLineLocation(), text ) );
    }

    @Override
    public HologramLine addItemLine( final ItemStack item )
    {
        return this.addLine( new StratoItemLine( this.getNextLineLocation(), item ) );
    }

    private Location getNextLineLocation()
    {
        return lines.isEmpty() ? location.get() : lines.get( lines.size() - 1 ).getLocation();
    }

    private HologramLine addLine( final HologramLine line )
    {
        this.lines.add( line );

        return line;
    }

    @Override
    public HologramLine getLine( final int index )
    {
        return this.lines.get( index );
    }

    @Override
    public HologramLine removeLine( final int index )
    {
        final HologramLine line = this.getLine( index );

        line.remove();
        return this.lines.remove( index );
    }

    @Override
    public void clear()
    {
        for ( HologramLine line : this.lines )
        {
            line.remove();
        }
        this.lines.clear();
    }

    @Override
    public int size()
    {
        return this.lines.size();
    }

    @Override
    public void teleport( final Location location )
    {
        final List<HologramLine> tempLines = new ArrayList<>( this.lines );
        this.clear();

        if ( this.location != null )
        {
            this.location.clear();
        }

        this.location = new WeakReference<>( location );
        for ( HologramLine line : tempLines )
        {
            line.teleport( this.getNextLineLocation() );
            this.addLine( line );
        }
    }

    @Override
    public Location getLocation()
    {
        return this.location == null ? null : this.location.get();
    }

    @Override
    public long getCreatedAt()
    {
        return this.createdAt;
    }

    @Override
    public void delete()
    {
        this.attemptDeletion();
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

        for ( HologramLine line : lines )
        {
            if ( line.shouldUpdate() )
            {
                line.update();
            }
        }
    }

    private void attemptDeletion()
    {
        if ( this.location != null )
        {
            this.location.clear();
        }
        this.clear();
    }
}
