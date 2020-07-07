package com.dbsoftwares.stratogram.nms.api.hologram;

import org.bukkit.inventory.ItemStack;

public interface HologramItem extends HologramEntity
{

    void setHologramLocation( double x, double y, double z );

    Object getHologramItemStack();

    void setHologramItemStack( ItemStack stack );
}
