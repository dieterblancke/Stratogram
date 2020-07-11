package com.dbsoftwares.stratogram.api;

import com.dbsoftwares.stratogram.api.line.HologramLine;
import com.dbsoftwares.stratogram.api.util.Players;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

public interface Hologram
{

    /**
     * Adds a text line to the end of this hologram.
     *
     * @param text the text that should be added on the end of the hologram.
     * @return the textline that got added
     */
    HologramLine addTextLine( final String text );

    /**
     * Adds a item line to the end of this hologram.
     *
     * @param item the item that should be added on the end of the hologram.
     * @return the itemline that got added
     */
    HologramLine addItemLine( final ItemStack item );

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
     * Updates all the hologram lines. If the location which is stored in a ({@link java.lang.ref.SoftReference<Location>})
     * is removed by GC, this method will automatically delete the hologram as a result.
     */
    void update();

    /**
     * The default visibility of the hologram. By setting this to false, the hologram will disappear for all players.
     * Opposite happens when setting this to true.
     *
     * @param visible default visibility of the hologram.
     */
    void setVisibleByDefault(boolean visible);

    /**
     * Hides the hologram from the given players.
     * Setting this to {@link Players#none()} will call {@link #showTo(Players)} with {@link Players#all()} (aka shows it to everyone).
     * Setting this to {@link Players#all()} will call {@link #setVisibleByDefault(boolean)} with a {@link Boolean#FALSE} value.
     * Setting this to {@link Players#none()} will set {@link #setVisibleByDefault(boolean)} with a {@link Boolean#TRUE} value.
     *
     * @param players the players to hide the hologram from.
     */
    void hideTo( Players players );

    /**
     * Shows the hologram to the given players.
     * Setting this to {@link Players#none()} will call {@link #hideTo(Players)} with {@link Players#all()} (aka hides it to everyone).
     * Setting this to {@link Players#all()} will call {@link #setVisibleByDefault(boolean)} with a {@link Boolean#TRUE} value.
     * Setting this to {@link Players#none()} will set {@link #setVisibleByDefault(boolean)} with a {@link Boolean#FALSE} value.
     *
     * @param players the players to show the hologram to.
     */
    void showTo( Players players );
}
