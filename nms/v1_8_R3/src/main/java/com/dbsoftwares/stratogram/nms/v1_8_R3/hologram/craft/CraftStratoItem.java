package com.dbsoftwares.stratogram.nms.v1_8_R3.hologram.craft;

import com.dbsoftwares.stratogram.nms.v1_8_R3.hologram.StratogramItem;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftItem;
import org.bukkit.entity.Entity;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

public class CraftStratoItem extends CraftItem
{

    public CraftStratoItem( CraftServer server, StratogramItem entity )
    {
        super( server, entity );
    }

    // Disallow all the bukkit methods.

    @Override
    public void remove()
    {
        // Cannot be removed, this is the most important to override.
    }

    // Methods from Entity
    @Override
    public void setVelocity( Vector vel )
    {
    }

    @Override
    public boolean teleport( Location loc )
    {
        return false;
    }

    @Override
    public boolean teleport( Entity entity )
    {
        return false;
    }

    @Override
    public boolean teleport( Location loc, PlayerTeleportEvent.TeleportCause cause )
    {
        return false;
    }

    @Override
    public boolean teleport( Entity entity, PlayerTeleportEvent.TeleportCause cause )
    {
        return false;
    }

    @Override
    public void setFireTicks( int ticks )
    {
    }

    @Override
    public boolean setPassenger( Entity entity )
    {
        return false;
    }

    @Override
    public boolean eject()
    {
        return false;
    }

    @Override
    public boolean leaveVehicle()
    {
        return false;
    }

    @Override
    public void playEffect( EntityEffect effect )
    {
    }

    @Override
    public void setCustomName( String name )
    {
    }

    @Override
    public void setCustomNameVisible( boolean flag )
    {
    }

    // Methods from Item
    @Override
    public void setItemStack( ItemStack stack )
    {
    }

    @Override
    public void setPickupDelay( int delay )
    {
    }
}