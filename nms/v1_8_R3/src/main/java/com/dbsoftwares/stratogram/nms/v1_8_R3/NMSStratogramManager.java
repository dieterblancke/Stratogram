package com.dbsoftwares.stratogram.nms.v1_8_R3;

import com.dbsoftwares.stratogram.api.line.ItemLine;
import com.dbsoftwares.stratogram.api.line.TextLine;
import com.dbsoftwares.stratogram.nms.api.NMSHologramManager;
import com.dbsoftwares.stratogram.nms.api.hologram.HologramArmorStand;
import com.dbsoftwares.stratogram.nms.api.hologram.HologramEntity;
import com.dbsoftwares.stratogram.nms.api.hologram.HologramItem;
import com.dbsoftwares.stratogram.nms.v1_8_R3.hologram.StratogramArmorStand;
import com.dbsoftwares.stratogram.nms.v1_8_R3.hologram.StratogramItem;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class NMSStratogramManager implements NMSHologramManager
{

    @Override
    public boolean isHologramEntity( final Entity entity )
    {
        return ((CraftEntity) entity).getHandle() instanceof HologramEntity;
    }

    @Override
    public HologramArmorStand spawnHologramArmorStand( final Location location, final TextLine line, final boolean broadcast )
    {
        final WorldServer nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
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
        final WorldServer nmsWorld = ((CraftWorld) location.getWorld()).getHandle();
        final StratogramItem customItem = new StratogramItem( nmsWorld, line );
        customItem.setHologramLocation( location.getX(), location.getY(), location.getZ() );
        customItem.setHologramItemStack( line.getItem() );

        if ( !addEntityToWorld( nmsWorld, customItem ) )
        {
            System.out.println( "Could not spawn item line on " + location );
        }
        return customItem;
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
