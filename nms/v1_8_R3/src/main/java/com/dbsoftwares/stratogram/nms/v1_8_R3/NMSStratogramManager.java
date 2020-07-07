package com.dbsoftwares.stratogram.nms.v1_8_R3;

import com.dbsoftwares.stratogram.api.line.ItemLine;
import com.dbsoftwares.stratogram.api.line.TextLine;
import com.dbsoftwares.stratogram.nms.api.NMSHologramManager;
import com.dbsoftwares.stratogram.nms.api.hologram.HologramArmorStand;
import com.dbsoftwares.stratogram.nms.api.hologram.HologramItem;
import com.dbsoftwares.stratogram.nms.v1_8_R3.hologram.StratogramArmorStand;
import com.dbsoftwares.stratogram.nms.v1_8_R3.hologram.StratogramItem;
import net.minecraft.server.v1_8_R3.EntityTypes;
import net.minecraft.server.v1_8_R3.MathHelper;
import net.minecraft.server.v1_8_R3.World;
import net.minecraft.server.v1_8_R3.WorldServer;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.entity.Entity;
import org.bukkit.event.entity.CreatureSpawnEvent;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class NMSStratogramManager implements NMSHologramManager
{

    private static Field entityNamesByClassField;
    private static Field entityIdsByClassField;

    static
    {
        try
        {
            entityNamesByClassField = EntityTypes.class.getDeclaredField( "d" );
            entityIdsByClassField = EntityTypes.class.getDeclaredField( "f" );
        }
        catch ( NoSuchFieldException e )
        {
            e.printStackTrace();
        }
    }

    private Method validateEntityMethod;

    @SuppressWarnings( "unchecked" )
    public void registerCustomEntity( final Class<?> entityClass, final String name, final int id ) throws Exception
    {
        ( (Map<Class<?>, String>) entityNamesByClassField.get( null ) ).put( entityClass, name );
        ( (Map<Class<?>, Integer>) entityIdsByClassField.get( null ) ).put( entityClass, id );
    }

    @Override
    public void registerCustomEntities() throws Exception
    {
        registerCustomEntity( StratogramArmorStand.class, "ArmorStand", 30 );
        registerCustomEntity( StratogramItem.class, "Item", 1 );

        validateEntityMethod = World.class.getDeclaredMethod( "a", Entity.class );
        validateEntityMethod.setAccessible( true );
    }

    @Override
    public boolean isHologramEntity( final Entity entity )
    {
        return false;
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

    private boolean addEntityToWorld( WorldServer nmsWorld, net.minecraft.server.v1_8_R3.Entity nmsEntity )
    {
        if ( Bukkit.isPrimaryThread() )
        {
            throw new IllegalStateException( "Async entity add" );
        }

        if ( validateEntityMethod == null )
        {
            return nmsWorld.addEntity( nmsEntity, CreatureSpawnEvent.SpawnReason.CUSTOM );
        }

        final int chunkX = MathHelper.floor( nmsEntity.locX / 16.0 );
        final int chunkZ = MathHelper.floor( nmsEntity.locZ / 16.0 );

        if ( !nmsWorld.chunkProviderServer.isChunkLoaded( chunkX, chunkZ ) )
        {
            // This should never happen
            nmsEntity.dead = true;
            return false;
        }

        nmsWorld.getChunkAt( chunkX, chunkZ ).a( nmsEntity );
        nmsWorld.entityList.add( nmsEntity );

        try
        {
            validateEntityMethod.invoke( nmsWorld, nmsEntity );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}
