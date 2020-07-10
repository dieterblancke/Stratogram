package com.dbsoftwares.stratogram.nms.v1_16_R1.hologram;

import com.dbsoftwares.stratogram.api.line.ItemLine;
import com.dbsoftwares.stratogram.nms.api.hologram.HologramItem;
import com.dbsoftwares.stratogram.nms.v1_16_R1.hologram.craft.CraftStratoItem;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.ChatColor;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;

@EqualsAndHashCode( callSuper = true )
public class StratogramItem extends EntityItem implements HologramItem
{

    private final ItemLine itemLine;
    private CraftEntity customBukkitEntity;

    public StratogramItem( final World world, final ItemLine itemLine )
    {
        super( EntityTypes.ITEM, world );
        super.pickupDelay = 32767;
        this.itemLine = itemLine;
    }

    @Override
    public void tick()
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

    // Method called when a player is near.
    @Override
    public void pickup( EntityHuman human )
    {
        // do nothing
    }

    @Override
    public void saveData(NBTTagCompound nbttagcompound) {
        // Do not save NBT.
    }

    @Override
    public boolean a_(NBTTagCompound nbttagcompound) {
        // Do not save NBT.
        return false;
    }

    @Override
    public boolean d(NBTTagCompound nbttagcompound) {
        // Do not save NBT.
        return false;
    }

    @Override
    public NBTTagCompound save(NBTTagCompound nbttagcompound) {
        // Do not save NBT.
        return nbttagcompound;
    }

    @Override
    public void load(NBTTagCompound nbttagcompound) {
        // Do not load NBT.
    }

    @Override
    public void loadData(NBTTagCompound nbttagcompound) {
        // Do not load NBT.
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
    public boolean isCollidable() {
        return false;
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
        if ( customBukkitEntity  == null )
        {
            customBukkitEntity  = new CraftStratoItem( this.world.getServer(), this );
        }
        return customBukkitEntity ;
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
        super.dead = true;
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
            newItem = new ItemStack( Blocks.BEDROCK );
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
        tagList.add( NBTTagString.a( ChatColor.BLACK.toString() + Math.random() ) ); // Antistack lore
        display.set( "Lore", tagList );

        setItemStack( newItem );
    }
}