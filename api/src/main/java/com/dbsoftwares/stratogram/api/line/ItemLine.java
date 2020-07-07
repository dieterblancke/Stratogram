package com.dbsoftwares.stratogram.api.line;

import org.bukkit.inventory.ItemStack;

public interface ItemLine extends HologramLine
{

    /**
     * Gets the current item content of this line.
     *
     * @return the current item content.
     */
    ItemStack getItem();

    /**
     * Updates the item content of this line.
     *
     * @param item the new content for this line.
     */
    void setItem( final ItemStack item );
}
