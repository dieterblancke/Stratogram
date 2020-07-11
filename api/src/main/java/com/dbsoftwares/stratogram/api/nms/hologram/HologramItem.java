package com.dbsoftwares.stratogram.api.nms.hologram;

import org.bukkit.inventory.ItemStack;

public interface HologramItem extends HologramEntity
{

    Object getHologramItemStack();

    void setHologramItemStack( ItemStack stack );

    void setPassengerOf( HologramEntity entity );
}
