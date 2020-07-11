package com.dbsoftwares.stratogram.nms.v1_16_R1.hologram;

import com.dbsoftwares.stratogram.api.util.Utils;
import com.dbsoftwares.stratogram.api.line.HologramLine;
import com.dbsoftwares.stratogram.api.line.TextLine;
import com.dbsoftwares.stratogram.api.nms.hologram.HologramArmorStand;
import com.dbsoftwares.stratogram.nms.v1_16_R1.hologram.craft.CraftStratoArmorStand;
import lombok.EqualsAndHashCode;
import net.minecraft.server.v1_16_R1.*;
import org.bukkit.craftbukkit.v1_16_R1.entity.CraftEntity;
import org.bukkit.craftbukkit.v1_16_R1.util.CraftChatMessage;

@EqualsAndHashCode(callSuper = true)
public class StratogramArmorStand extends EntityArmorStand implements HologramArmorStand
{

    private final TextLine textLine;
    private String customName;
    private CraftEntity customBukkitEntity;

    public StratogramArmorStand( final World world, final TextLine textLine )
    {
        super( EntityTypes.ARMOR_STAND, world );
        super.setInvisible( true );
        super.setSmall( true );
        super.setArms( false );
        super.setNoGravity( true );
        super.setBasePlate( true );
        super.setMarker( true );
        super.collides = false;
        this.textLine = textLine;
        forceSetBoundingBox( new NullBoundingBox() );

        this.onGround = true; // Workaround to force EntityTrackerEntry to send a teleport packet.
    }

    @Override
    public void tick()
    {
        // Disable normal ticking for this entity.

        // Workaround to force EntityTrackerEntry to send a teleport packet immediately after spawning this entity.
        if ( this.onGround )
        {
            this.onGround = false;
        }
    }

    @Override
    public void inactiveTick()
    {
        // Disable normal ticking for this entity.

        // Workaround to force EntityTrackerEntry to send a teleport packet immediately after spawning this entity.
        if ( this.onGround )
        {
            this.onGround = false;
        }
    }

    @Override
    public void saveData( NBTTagCompound nbttagcompound )
    {
        // Do not save NBT.
    }

    @Override
    public boolean a_( NBTTagCompound nbttagcompound )
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
    public NBTTagCompound save( NBTTagCompound nbttagcompound )
    {
        // Do not save NBT.
        return nbttagcompound;
    }

    @Override
    public void load( NBTTagCompound nbttagcompound )
    {
        // Do not load NBT.
    }

    @Override
    public void loadData( NBTTagCompound nbttagcompound )
    {
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
    public boolean isCollidable()
    {
        return false;
    }

    @Override
    public void setCustomName( IChatBaseComponent ichatbasecomponent )
    {
        // Locks the custom name.
    }

    @Override
    public void setCustomNameVisible( boolean visible )
    {
        // Locks the custom name.
    }

    @Override
    public EnumInteractionResult a( EntityHuman human, Vec3D vec3d, EnumHand enumhand )
    {
        // Prevent stand being equipped
        return EnumInteractionResult.PASS;
    }

    @Override
    public boolean a_( int i, ItemStack item )
    {
        // Prevent stand being equipped
        return false;
    }

    @Override
    public void setSlot( EnumItemSlot enumitemslot, ItemStack itemstack )
    {
        // Prevent stand being equipped
    }

    @Override
    public void a( AxisAlignedBB boundingBox )
    {
        // Do not change it!
    }

    public void forceSetBoundingBox( AxisAlignedBB boundingBox )
    {
        super.a( boundingBox );
    }

    @Override
    public void playSound( SoundEffect soundeffect, float f, float f1 )
    {
        // Remove sounds.
    }

    @Override
    public void die()
    {
        // Prevent being killed.
    }

    @Override
    public CraftEntity getBukkitEntity()
    {
        if ( customBukkitEntity == null )
        {
            customBukkitEntity = new CraftStratoArmorStand( this.world.getServer(), this );
        }
        return customBukkitEntity;
    }

    @Override
    public HologramLine getHologramLine()
    {
        return textLine;
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

    private void broadcastLocPacket()
    {
        final PacketPlayOutEntityTeleport teleportPacket = new PacketPlayOutEntityTeleport( this );

        for ( EntityHuman obj : super.world.getPlayers() )
        {
            if ( obj instanceof EntityPlayer )
            {
                EntityPlayer nmsPlayer = (EntityPlayer) obj;

                double distanceSquared = Utils.square( nmsPlayer.locX() - super.locX() ) + Utils.square( nmsPlayer.locZ() - super.locZ() );
                if ( distanceSquared < 8192 && nmsPlayer.playerConnection != null )
                {
                    nmsPlayer.playerConnection.sendPacket( teleportPacket );
                }
            }
        }
    }

    @Override
    public void setHologramLocation( final double x, final double y, final double z, final boolean broadcast )
    {
        super.setPosition( x, y, z );

        if ( broadcast )
        {
            broadcastLocPacket();
        }
    }

    @Override
    public String getHologramCustomName()
    {
        return this.customName;
    }

    @Override
    public void setHologramCustomName( String name )
    {
        this.customName = Utils.limitLength( name, 300 );
        super.setCustomName( CraftChatMessage.fromStringOrNull( customName ) );
        super.setCustomNameVisible( customName != null && !customName.isEmpty() );
    }

    @Override
    public Object getHologramCustomNameObject()
    {
        return super.getCustomName();
    }
}
