package com.dbsoftwares.stratogram.holograms.line;

import com.dbsoftwares.stratogram.Stratogram;
import com.dbsoftwares.stratogram.api.line.TextLine;
import com.dbsoftwares.stratogram.nms.api.hologram.HologramArmorStand;
import com.dbsoftwares.stratogram.pluginhooks.PluginHooks;
import org.bukkit.Location;

import java.lang.ref.WeakReference;

public class StratoTextLine extends StratoLine implements TextLine
{

    private boolean placeHoldersFound = false;
    private String text;

    public StratoTextLine( final Location location, final String text )
    {
        super( location );
        this.setText( text );
    }

    @Override
    public String getText()
    {
        return placeHoldersFound ? PluginHooks.PLACEHOLDERAPI.setPlaceHolders( text ) : text;
    }

    @Override
    public void setText( String text )
    {
        this.placeHoldersFound = hasPlaceHolders( text );
        this.text = text;

        this.update(); // Force calling update for if shouldUpdate returns false we still want to update the line
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
            final HologramArmorStand armorStand = (HologramArmorStand) this.nmsEntity.get();

            if ( armorStand != null )
            {
                armorStand.setHologramCustomName( this.getText() );
            }
        }
    }

    private boolean spawnIfDead()
    {
        if ( this.nmsEntity == null )
        {
            final HologramArmorStand entity = Stratogram.getInstance().getHologramManager().spawnHologramArmorStand( this.location.get(), this, true );
            this.nmsEntity = new WeakReference<>( entity );
            return true;
        }
        return false;
    }

    @Override
    public boolean shouldUpdate()
    {
        // If update delay hasn't passed yet, skip updating.
        if ( !super.shouldUpdate() )
        {
            return false;
        }

        // If no place holders are found, the line shouldn't update
        return placeHoldersFound;
    }

    private boolean hasPlaceHolders( final String text )
    {
        return PluginHooks.PLACEHOLDERAPI.isPresent() && PluginHooks.PLACEHOLDERAPI.hasPlaceHolders( text );
    }


    @Override
    protected void attemptDeletion()
    {
        super.attemptDeletion();
        this.text = null;
    }
}
