package com.dbsoftwares.stratogram.nms.v1_8_R3.hologram;

import com.dbsoftwares.stratogram.api.line.ItemLine;
import com.dbsoftwares.stratogram.api.nms.hologram.HologramEntity;
import com.dbsoftwares.stratogram.api.nms.hologram.HologramItem;
import com.dbsoftwares.stratogram.nms.v1_8_R3.hologram.craft.CraftStratoItem;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_8_R3.inventory.CraftItemStack;

import java.lang.reflect.Field;

@EqualsAndHashCode(callSuper = true)
public class StratogramItem extends EntityItem implements HologramItem
{

    private static Field riderPitchDelta;
    private static Field riderYawDelta;

    static
    {
        try
        {
            riderPitchDelta = Entity.class.getDeclaredField( "ar" );
            riderYawDelta = Entity.class.getDeclaredField( "as" );
        }
        catch ( NoSuchFieldException e )
        {
            e.printStackTrace();
        }
    }

    private final ItemLine itemLine;

    public StratogramItem( final World world, final ItemLine itemLine )
    {
        super( world );
        super.pickupDelay = Integer.MAX_VALUE;
        this.itemLine = itemLine;
        this.noclip = true;
    }

    @Override
    public void t_()
    {
        // Disable normal ticking for this entity.

        // So it won't get removed.
        ticksLived = 0;
    }

    @Override
    public void inactiveTick()
    {
        // Disable normal ticking for this entity.

        // So it won't get removed.
        ticksLived = 0;
    }

    @Override
    protected void burn( float i )
    {
        // do nothing
    }

    // Method called when a player is near.
    @Override
    public void d( EntityHuman human )
    {
    }

    @Override
    public void b( NBTTagCompound nbttagcompound )
    {
        // Do not save NBT.
    }

    @Override
    public boolean c( NBTTagCompound nbttagcompound )
    {
        // Do not save NBT.
        return false;
    }

    @Override
    public boolean d( NBTTagCompound nbttagcompound )
    {
        // Do not save NBT.
        return false;
    }

    @Override
    public void e( NBTTagCompound nbttagcompound )
    {
        // Do not save NBT.
    }

    @Override
    public boolean isInvulnerable( DamageSource source )
    {
        /*
         * The field Entity.invulnerable is private.
         * It's only used while saving NBTTags, but since the entity would be killed
         * on chunk unload, we prefer to override isInvulnerable().
         */
        return true;
    }

    @Override
    public void die()
    {
        // Prevent being killed.
    }

    @Override
    public boolean isAlive()
    {
        // This override prevents items from being picked up by hoppers.
        // Should have no side effects.
        return false;
    }

    @Override
    public CraftEntity getBukkitEntity()
    {
        if ( super.bukkitEntity == null )
        {
            this.bukkitEntity = new CraftStratoItem( this.world.getServer(), this );
        }
        return this.bukkitEntity;
    }

    @Override
    public ItemLine getHologramLine()
    {
        return itemLine;
    }

    @Override
    public boolean isEntityDead()
    {
        return this.dead;
    }

    @Override
    public void destroyHologramEntity()
    {
        this.dead = true;
    }

    @Override
    public void setHologramLocation( double x, double y, double z )
    {
        super.setPosition( x, y, z );
    }

    @Override
    public Object getHologramItemStack()
    {
        return super.getItemStack();
    }

    @Override
    public void setHologramItemStack( org.bukkit.inventory.ItemStack stack )
    {
        ItemStack newItem = CraftItemStack.asNMSCopy( stack );

        if ( newItem == null )
        {
            newItem = new ItemStack( Blocks.BARRIER );
        }

        if ( newItem.getTag() == null )
        {
            newItem.setTag( new NBTTagCompound() );
        }
        NBTTagCompound display = newItem.getTag().getCompound( "display" );

        if ( !newItem.getTag().hasKey( "display" ) )
        {
            newItem.getTag().set( "display", display );
        }

        NBTTagList tagList = new NBTTagList();
        tagList.add( new NBTTagString( ChatColor.BLACK.toString() + Math.random() ) ); // Antistack lore
        display.set( "Lore", tagList );

        setItemStack( newItem );
    }

    @Override
    public void setPassengerOf( final HologramEntity hologramEntity )
    {
        if ( !(hologramEntity instanceof Entity) )
        {
            return;
        }

        final Entity entity = (Entity) hologramEntity;
        this.mount( entity );
    }
}