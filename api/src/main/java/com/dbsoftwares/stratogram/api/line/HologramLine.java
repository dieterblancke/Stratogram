package com.dbsoftwares.stratogram.api.line;

import com.dbsoftwares.configuration.api.ISection;
import org.bukkit.Location;

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

    /**
     * Returns a section containing the necessary data to store the line.
     *
     * @return a section with the line data.
     */
    ISection asSection();

    /**
     * Returns the location of the hologram line.
     *
     * @return the location of the hologram line.
     */
    Location getLocation();

    /**
     * Teleports the hologram to a specific location.
     *
     * @param location the location to teleport to.
     */
    void teleport( Location location );
}
