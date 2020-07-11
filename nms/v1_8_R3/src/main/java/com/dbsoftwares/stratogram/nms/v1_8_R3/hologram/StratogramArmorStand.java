package com.dbsoftwares.stratogram.nms.v1_8_R3.hologram;

import com.dbsoftwares.stratogram.api.Utils;
import com.dbsoftwares.stratogram.api.line.HologramLine;
import com.dbsoftwares.stratogram.api.line.TextLine;
import com.dbsoftwares.stratogram.nms.api.hologram.HologramArmorStand;
import com.dbsoftwares.stratogram.nms.v1_8_R3.hologram.craft.CraftStratoArmorStand;
import lombok.EqualsAndHashCode;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftEntity;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

@EqualsAndHashCode(callSuper = true)
public class StratogramArmorStand extends EntityArmorStand implements HologramArmorStand
{

    private static Field disabledSlotsField;
    private static Method setMarkerMethod;

    static
    {
        try
        {
            disabledSlotsField = EntityArmorStand.class.getDeclaredField( "bi" );
            setMarkerMethod = EntityArmorStand.class.getDeclaredMethod( "n", boolean.class );

            disabledSlotsField.setAccessible( true );
            setMarkerMethod.setAccessible( true );
        }
        catch ( NoSuchMethodException | NoSuchFieldException e )
        {
            e.printStackTrace();
        }
    }

    private final TextLine textLine;
    private String customName;

    public StratogramArmorStand( final World world, final TextLine textLine )
    {
        super( world );
        setInvisible( true );
        setSmall( true );
        setArms( false );
        setGravity( true );
        setBasePlate( true );

        try
        {
            setMarkerMethod.invoke( this, true );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        this.textLine = textLine;
        try
        {
            disabledSlotsField.set( this, Integer.MAX_VALUE );
        }
        catch ( Exception e )
        {
            e.printStackTrace();
        }
        forceSetBoundingBox( new NullBoundingBox() );

        this.onGround = true; // Workaround to force EntityTrackerEntry to send a teleport packet.
    }

    @Override
    public void t_()
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
    public void setCustomName( String customName )
    {
        // Locks the custom name.
    }

    @Override
    public void setCustomNameVisible( boolean visible )
    {
        // Locks the custom name.
    }

    @Override
    public boolean a( EntityHuman human, Vec3D vec3d )
    {
        // Prevent stand being equipped
        return true;
    }

    @Override
    public boolean d( int i, ItemStack item )
    {
        // Prevent stand being equipped
        return false;
    }

    @Override
    public void setEquipment( int i, ItemStack item )
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
    public void makeSound( String sound, float f1, float f2 )
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
        if ( super.bukkitEntity == null )
        {
            this.bukkitEntity = new CraftStratoArmorStand( this.world.getServer(), this );
        }
        return this.bukkitEntity;
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

        for ( EntityHuman obj : this.world.players )
        {
            if ( obj instanceof EntityPlayer )
            {
                EntityPlayer nmsPlayer = (EntityPlayer) obj;

                final double distanceSquared = Utils.square( nmsPlayer.locX - this.locX ) + Utils.square( nmsPlayer.locZ - this.locZ );
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
        super.setCustomName( customName );
        super.setCustomNameVisible( customName != null && !customName.isEmpty() );
    }

    @Override
    public Object getHologramCustomNameObject()
    {
        return super.getCustomName();
    }
}
