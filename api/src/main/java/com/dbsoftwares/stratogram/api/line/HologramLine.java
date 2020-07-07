package com.dbsoftwares.stratogram.api.line;

public interface HologramLine
{

    /**
     * Removes this line from the hologram (this does not alter configuration if this is called on a db hologram).
     */
    void remove();

    /**
     * Updates the contents of this line.
     */
    void update();

    /**
     * Checks whether or not the line should be updated at this point in time.
     *
     * @return true if should be updated, false if not.
     */
    boolean shouldUpdate();

}
