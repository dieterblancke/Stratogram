package com.dbsoftwares.stratogram.nms.api.hologram;

public interface HologramArmorStand extends HologramEntity
{
    @Override
    default void setHologramLocation( double x, double y, double z )
    {
        this.setHologramLocation( x, y, z, true );
    }

    void setHologramLocation( double x, double y, double z, boolean broadcastLocationPacket );

    String getHologramCustomName();

    void setHologramCustomName( String name );

    Object getHologramCustomNameObject();
}
