package com.dbsoftwares.stratogram.holograms.line;

import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.stratogram.Stratogram;
import com.dbsoftwares.stratogram.api.line.ItemLine;
import com.dbsoftwares.stratogram.nms.api.hologram.HologramItem;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.lang.ref.WeakReference;

public class StratoItemLine extends StratoLine implements ItemLine
{

    private ItemStack item;

    public StratoItemLine( final Location previousLine, final ItemStack item )
    {
        super( previousLine.clone().add( 0, Stratogram.getInstance().getConfiguration().getDouble( "spacing.item" ), 0 ) );
        this.setItem( item );
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
    }

    @Override
    public void remove()
    {
        this.attemptDeletion();
    }

    @Override
    public void update()
    {
        if ( this.location == null || this.location.get() == null || (this.nmsEntity != null && this.nmsEntity.get() == null) )
        {
            this.attemptDeletion();
            Stratogram.getInstance().debug( "A hologram line got deleted because the entity got removed by garbage collection." );
            return;
        }
        super.update();

        // This method returns false if the ArmorStand was already spawned
        if ( !this.spawnIfDead() )
        {
            final HologramItem item = (HologramItem) this.nmsEntity.get();

            if ( item != null )
            {
                item.setHologramItemStack( this.getItem() );
            }
        }
    }

    private boolean spawnIfDead()
    {
        if ( this.nmsEntity == null )
        {
            final HologramItem entity = Stratogram.getInstance().getHologramManager().spawnHologramItem( this.location.get(), this );
            this.nmsEntity = new WeakReference<>( entity );
            return true;
        }
        return false;
    }

    @Override
    public ISection asSection()
    {
        final ISection section = super.asSection();

        section.set( "type", "item" );
        section.set( "data", this.item );

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
        this.item = null;
    }
}
