package com.dbsoftwares.stratogram.nms.v1_8_R3;

import com.dbsoftwares.stratogram.api.Hologram;
import com.dbsoftwares.stratogram.api.line.HologramLine;
import com.dbsoftwares.stratogram.api.line.ItemLine;
import com.dbsoftwares.stratogram.api.line.TextLine;
import com.dbsoftwares.stratogram.api.nms.NMSHologramManager;
import com.dbsoftwares.stratogram.api.nms.hologram.HologramArmorStand;
import com.dbsoftwares.stratogram.api.nms.hologram.HologramEntity;
import com.dbsoftwares.stratogram.api.nms.hologram.HologramItem;
import com.dbsoftwares.stratogram.nms.v1_8_R3.hologram.StratogramArmorStand;
import com.dbsoftwares.stratogram.nms.v1_8_R3.hologram.StratogramItem;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.util.ArrayList;
import java.util.List;

public class NMSStratogramManager implements NMSHologramManager
{

    @Override
    public boolean isHologramEntity( final Entity entity )
    {
        return ( (CraftEntity) entity ).getHandle() instanceof HologramEntity;
    }

    @Override
    public HologramArmorStand spawnHologramArmorStand( final Location location, final TextLine line, final boolean broadcast )
    {
        final WorldServer nmsWorld = ( (CraftWorld) location.getWorld() ).getHandle();
        final StratogramArmorStand invisibleArmorStand = new StratogramArmorStand( nmsWorld, line );
        invisibleArmorStand.setHologramLocation( location.getX(), location.getY(), location.getZ(), broadcast );

        if ( !addEntityToWorld( nmsWorld, invisibleArmorStand ) )
        {
            System.out.println( "Could not spawn text line on " + location );
        }
        return invisibleArmorStand;
    }

    @Override
    public HologramItem spawnHologramItem( final Location location, final ItemLine line )
    {
        final WorldServer nmsWorld = ( (CraftWorld) location.getWorld() ).getHandle();
        final StratogramItem customItem = new StratogramItem( nmsWorld, line );
        customItem.setHologramLocation( location.getX(), location.getY(), location.getZ() );
        customItem.setHologramItemStack( line.getItem() );

        if ( !addEntityToWorld( nmsWorld, customItem ) )
        {
            System.out.println( "Could not spawn item line on " + location );
        }
        return customItem;
    }

    @Override
    public void sendSpawnPacket( final Hologram hologram, final Player... players )
    {
        for ( int i = 0; i < hologram.size(); i++ )
        {
            final HologramLine line = hologram.getLine( i );

            this.sendSpawnPacket( line.getEntity(), players );
            if ( line instanceof ItemLine )
            {
                this.sendSpawnPacket(
                        (HologramEntity) ( (net.minecraft.server.v1_8_R3.Entity) line.getEntity() ).vehicle,
                        players
                );
            }
        }
    }

    private void sendSpawnPacket( final HologramEntity entity, final Player[] players )
    {
        if ( entity instanceof HologramArmorStand )
        {
            final PacketPlayOutSpawnEntityLiving packet = new PacketPlayOutSpawnEntityLiving(
                    (EntityArmorStand) entity
            );
            this.sendPacket( players, packet );
        }
        else
        {
            final EntityItem item = (EntityItem) entity;
            final PacketPlayOutSpawnEntity packet = new PacketPlayOutSpawnEntity( item, 2, 100 );
            this.sendPacket( players, packet );

            final PacketPlayOutEntityMetadata data = new PacketPlayOutEntityMetadata( item.getId(), item.getDataWatcher(), true );
            this.sendPacket( players, data );
        }
    }

    @Override
    public void sendDestroyPacket( final Hologram hologram, final Player... players )
    {
        final List<Integer> allIds = new ArrayList<>();

        for ( int i = 0; i < hologram.size(); i++ )
        {
            final HologramLine line = hologram.getLine( i );

            allIds.add( line.getEntity().getBukkitEntity().getEntityId() );

            if ( line instanceof ItemLine )
            {
                allIds.add( ( (net.minecraft.server.v1_8_R3.Entity) line.getEntity() ).vehicle.getId() );
            }
        }

        sendDestroyPacket( allIds, players );
    }

    private void sendDestroyPacket( final List<Integer> ids, final Player[] players )
    {
        final PacketPlayOutEntityDestroy destroyPacket = new PacketPlayOutEntityDestroy( this.toIntArray( ids ) );
        this.sendPacket( players, destroyPacket );
    }

    private int[] toIntArray( List<Integer> list )
    {
        int[] ret = new int[list.size()];
        for ( int i = 0; i < ret.length; i++ )
        {
            ret[i] = list.get( i );
        }
        return ret;
    }

    private void sendPacket( final Player[] players, final Packet packet )
    {
        for ( Player player : players )
        {
            if ( player != null && player.isOnline() )
            {
                ( (CraftPlayer) player ).getHandle().playerConnection.sendPacket( packet );
            }
        }
    }

    private boolean addEntityToWorld( WorldServer nmsWorld, net.minecraft.server.v1_8_R3.Entity nmsEntity )
    {
        final int chunkX = MathHelper.floor( nmsEntity.locX / 16.0 );
        final int chunkZ = MathHelper.floor( nmsEntity.locZ / 16.0 );

        if ( !nmsWorld.chunkProviderServer.isChunkLoaded( chunkX, chunkZ ) )
        {
            nmsEntity.dead = true;
            return false;
        }

        return nmsWorld.addEntity( nmsEntity, CreatureSpawnEvent.SpawnReason.CUSTOM );
    }
}
