package com.dbsoftwares.stratogram.nms.api.hologram;

import org.bukkit.inventory.ItemStack;

public interface HologramItem extends HologramEntity
{

    Object getHologramItemStack();

    void setHologramItemStack( ItemStack stack );

    void setPassengerOf( HologramEntity entity );
}
