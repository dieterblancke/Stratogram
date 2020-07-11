package com.dbsoftwares.stratogram.api.nms;

import com.dbsoftwares.stratogram.api.Hologram;
import com.dbsoftwares.stratogram.api.line.ItemLine;
import com.dbsoftwares.stratogram.api.line.TextLine;
import com.dbsoftwares.stratogram.api.nms.hologram.HologramArmorStand;
import com.dbsoftwares.stratogram.api.nms.hologram.HologramItem;
import org.bukkit.Location;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;

import java.util.Collection;

public interface NMSHologramManager
{

    HologramItem spawnHologramItem( Location location, ItemLine line );

    HologramArmorStand spawnHologramArmorStand( Location location, TextLine line, boolean broadcast );

    boolean isHologramEntity( Entity entity );

    default void sendSpawnPacket( final Hologram hologram, final Collection<? extends Player> players )
    {
        this.sendSpawnPacket( hologram, players.toArray( new Player[0] ) );
    }

    void sendSpawnPacket( final Hologram hologram, final Player... players );

    default void sendDestroyPacket( final Hologram hologram, final Collection<? extends Player> players )
    {
        this.sendDestroyPacket( hologram, players.toArray( new Player[0] ) );
    }

    void sendDestroyPacket( final Hologram hologram, final Player... players );
}
