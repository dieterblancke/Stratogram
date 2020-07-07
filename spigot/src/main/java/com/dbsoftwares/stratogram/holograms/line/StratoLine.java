package com.dbsoftwares.stratogram.holograms.line;

import com.dbsoftwares.stratogram.holograms.api.line.HologramLine;

public abstract class StratoLine implements HologramLine
{

    private final long updateDelay;
    private long lastUpdate;

    public StratoLine()
    {
        this( -1 );
    }

    public StratoLine( final int updateDelay )
    {
        this.updateDelay = updateDelay;
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
}
