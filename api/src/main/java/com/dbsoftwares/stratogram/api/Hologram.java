package com.dbsoftwares.stratogram.api;

import com.dbsoftwares.stratogram.api.line.HologramLine;
import org.bukkit.Location;

public interface Hologram
{

    /**
     * Adds a line to the end of this hologram.
     *
     * @param line the line that should be added on the end of the hologram.
     * @return the line that got added
     */
    HologramLine addLine( HologramLine line );

    /**
     * Adds a line to the hologram on a given index.
     *
     * @param index the index on which the line should be added.
     * @param line the line that should be added to the hologram.
     * @return the line that got added
     */
    HologramLine addLine( int index, HologramLine line );

    /**
     * Gets the line at the given index in the hologram.
     *
     * @param index the index to search for.
     * @return the line that is on the given index.
     * @throws IndexOutOfBoundsException if the index is lower then 0 or higher then {@link #size()}.
     */
    HologramLine getLine( int index );

    /**
     * Removes the line on the given index from the hologram.
     *
     * @param index the index to search for.
     * @return the line that got removed from the given index.
     * @throws IndexOutOfBoundsException if the index is lower then 0 or higher then {@link #size()}.
     */
    HologramLine removeLine( int index );

    /**
     * Clears all lines from the hologram.
     */
    void clear();

    /**
     * Gets the amount of lines that are present in the hologram.
     *
     * @return the amount of lines.
     */
    int size();

    /**
     * Teleports the hologram to a new location.
     *
     * @param location the location that the hologram should teleport to.
     */
    void teleport( Location location );

    /**
     * Gets the current location of the hologram.
     *
     * @return the current location of the hologram.
     */
    Location getLocation();

    /**
     * Gets the time at which the hologram was created. This time is set by using ({@link System#currentTimeMillis()} value on creation.
     *
     * @return the time of creation.
     */
    long getCreatedAt();

    /**
     * Deletes the hologram.
     */
    void delete();

    /**
     * Updates all the hologram lines. If the location which is stored in a ({@link java.lang.ref.WeakReference<Location>})
     * is removed by GC, this method will automatically delete the hologram as a result.
     */
    void update();

}
