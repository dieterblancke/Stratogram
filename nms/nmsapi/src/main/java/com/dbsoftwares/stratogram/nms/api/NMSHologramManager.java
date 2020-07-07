package com.dbsoftwares.stratogram.nms.api;

import com.dbsoftwares.stratogram.api.line.ItemLine;
import com.dbsoftwares.stratogram.api.line.TextLine;
import com.dbsoftwares.stratogram.nms.api.hologram.HologramArmorStand;
import com.dbsoftwares.stratogram.nms.api.hologram.HologramItem;
import org.bukkit.Location;
import org.bukkit.entity.Entity;

public interface NMSHologramManager
{

    void registerCustomEntities() throws Exception;

    HologramItem spawnHologramItem( Location location, ItemLine line );

    HologramArmorStand spawnHologramArmorStand( Location location, TextLine line, boolean broadcast );

    boolean isHologramEntity( Entity entity );
}
