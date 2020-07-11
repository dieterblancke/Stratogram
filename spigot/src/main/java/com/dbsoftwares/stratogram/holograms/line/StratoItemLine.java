package com.dbsoftwares.stratogram.holograms.line;

import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.configuration.json.JsonSection;
import com.dbsoftwares.stratogram.Stratogram;
import com.dbsoftwares.stratogram.api.line.ItemLine;
import com.dbsoftwares.stratogram.nms.api.hologram.HologramArmorStand;
import com.dbsoftwares.stratogram.nms.api.hologram.HologramItem;
import com.dbsoftwares.stratogram.utils.Locations;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

@Data
@EqualsAndHashCode(callSuper = true)
public class StratoItemLine extends StratoLine implements ItemLine
{

    private ItemStack item;
    private HologramArmorStand nmsVehicle;

    public StratoItemLine( final Location previousLine, final ItemStack item )
    {
        super( previousLine.clone().subtract( 0, Stratogram.getInstance().getConfiguration().getDouble( "spacing.item.top" ), 0 ) );
        this.setItem( item );
        Stratogram.getInstance().debug( "Spawning an item line at " + Locations.toString( super.getLocation() ) + "." );
    }

    @Override
    public ItemStack getItem()
    {
        return item;
    }

    @Override
    public void setItem( final ItemStack item )
    {
        this.item = item;
        this.update();
    }

    @Override
    public void remove()
    {
        this.attemptDeletion();
    }

    @Override
    public void update()
    {
        super.update();

        if ( !this.spawnIfDead() )
        {
            final HologramItem item = (HologramItem) this.nmsEntity;

            item.setHologramItemStack( this.getItem() );
        }
    }

    private boolean spawnIfDead()
    {
        if ( this.nmsEntity == null )
        {
            this.nmsEntity = Stratogram.getInstance().getHologramManager().spawnHologramItem( this.location, this );
            this.nmsVehicle = Stratogram.getInstance().getHologramManager().spawnHologramArmorStand( location, null, true );

            ((HologramItem) this.nmsEntity).setPassengerOf( this.nmsVehicle );
            return true;
        }
        return false;
    }

    @Override
    public ISection asSection()
    {
        final ISection section = new JsonSection();

        section.set( "type", "item" );
        section.set( "data", this.item.getType().toString() );

        return section;
    }

    @Override
    public boolean shouldUpdate()
    {
        // Item lines never get updated through the update task. It only updates if ItemLine#setItem is called
        return false;
    }

    @Override
    protected void attemptDeletion()
    {
        super.attemptDeletion();

        if ( this.nmsVehicle != null )
        {
            this.nmsVehicle.destroyHologramEntity();
        }

        this.item = null;
        this.nmsVehicle = null;
    }
}
