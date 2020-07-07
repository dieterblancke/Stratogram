package com.dbsoftwares.stratogram.nms.api.hologram;

import com.dbsoftwares.stratogram.api.line.HologramLine;
import org.bukkit.entity.Entity;

public interface HologramEntity
{
    HologramLine getHologramLine();

    boolean isEntityDead();

    void destroyHologramEntity();

    Entity getBukkitEntity();
}
