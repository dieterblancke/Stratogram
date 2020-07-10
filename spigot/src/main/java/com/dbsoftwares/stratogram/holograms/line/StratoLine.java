package com.dbsoftwares.stratogram.holograms.line;

import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.configuration.json.JsonSection;
import com.dbsoftwares.stratogram.api.line.HologramLine;
import com.dbsoftwares.stratogram.api.line.ItemLine;
import com.dbsoftwares.stratogram.nms.api.hologram.HologramEntity;
import org.bukkit.Location;

import java.lang.ref.WeakReference;

public abstract class StratoLine implements HologramLine
{

    private final long updateDelay;
    protected WeakReference<Location> location;
    protected WeakReference<HologramEntity> nmsEntity;
    private long lastUpdate;

    public StratoLine( final Location location )
    {
        this( location, -1 );
    }

    public StratoLine( final Location location, final int updateDelay )
    {
        this.location = new WeakReference<>( location );
        this.updateDelay = updateDelay;
    }

    @Override
    public Location getLocation()
    {
        return location == null ? null : location.get();
    }

    @Override
    public void teleport( final Location location )
    {
        this.location = new WeakReference<>( location );
    }

    @Override
    public void update()
    {
        lastUpdate = System.currentTimeMillis();
    }

    @Override
    public boolean shouldUpdate()
    {
        return updateDelay == -1 || System.currentTimeMillis() > lastUpdate + updateDelay;
    }

    @Override
    public ISection asSection()
    {
        return new JsonSection();
    }

    protected void attemptDeletion()
    {
        // Destroying ArmorStand entity if it exists
        if ( this.nmsEntity != null )
        {
            final HologramEntity item = this.nmsEntity.get();

            if ( item != null )
            {
                item.destroyHologramEntity();
            }
        }
        // Clearing contents of WeakReferences
        if ( this.nmsEntity != null )
        {
            this.nmsEntity.clear();
        }
        if ( this.location != null )
        {
            this.location.clear();
        }
        // Setting all variables to null
        this.nmsEntity = null;
        this.location = null;
    }
}
