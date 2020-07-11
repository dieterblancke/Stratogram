package com.dbsoftwares.stratogram.holograms.line;

import com.dbsoftwares.stratogram.api.line.HologramLine;
import com.dbsoftwares.stratogram.api.nms.hologram.HologramEntity;
import org.bukkit.Location;

public abstract class StratoLine implements HologramLine
{

    private final long updateDelay;
    protected Location location;
    protected HologramEntity nmsEntity;
    private long lastUpdate;

    public StratoLine( final Location location )
    {
        this( location, -1 );
    }

    public StratoLine( final Location location, final int updateDelay )
    {
        this.location = location;
        this.updateDelay = updateDelay;
    }

    @Override
    public Location getLocation()
    {
        return location;
    }

    @Override
    public void teleport( final Location location )
    {
        this.location = location;
    }

    @Override
    public void update()
    {
        lastUpdate = System.currentTimeMillis();

        if ( nmsEntity != null )
        {
            nmsEntity.setHologramLocation( this.location.getX(), this.location.getY(), this.getLocation().getZ() );
        }
    }

    @Override
    public boolean shouldUpdate()
    {
        return updateDelay == -1 || System.currentTimeMillis() > lastUpdate + updateDelay;
    }

    protected void attemptDeletion()
    {
        // Destroying ArmorStand entity if it exists
        if ( this.nmsEntity != null )
        {
            this.nmsEntity.destroyHologramEntity();
        }
        // Setting all variables to null
        this.nmsEntity = null;
        this.location = null;
    }

    @Override
    public HologramEntity getEntity()
    {
        return nmsEntity;
    }
}
