package com.dbsoftwares.stratogram.holograms.line;

import com.dbsoftwares.stratogram.pluginhooks.PluginHooks;
import com.dbsoftwares.stratogram.Stratogram;
import com.dbsoftwares.stratogram.api.line.TextLine;
import org.bukkit.entity.Entity;

import java.lang.ref.WeakReference;

public class StratoTextLine extends StratoLine implements TextLine
{

    private boolean placeHoldersFound = false;
    private String text;
    private WeakReference<Entity> nmsEntity;

    public StratoTextLine( final String text )
    {
        this.setText( text );
    }

    @Override
    public String getText()
    {
        return text;
    }

    @Override
    public void setText( String text )
    {
        this.placeHoldersFound = hasPlaceHolders( text );

        if ( placeHoldersFound )
        {
            text = PluginHooks.PLACEHOLDERAPI.setPlaceHolders( text );
        }
        this.text = text;

        this.update(); // Force calling update for if shouldUpdate returns false we still want to update the line
    }

    @Override
    public void remove()
    {

    }

    @Override
    public void update()
    {
        if ( this.nmsEntity == null || this.nmsEntity.get() == null )
        {
            this.attemptDeletion();
            Stratogram.getInstance().debug( "A hologram line got deleted because the entity got removed by garbage collection." );
            return;
        }
        super.update();

        // TODO
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

    private void attemptDeletion()
    {
        // TODO
    }

    private boolean hasPlaceHolders( final String text )
    {
        return PluginHooks.PLACEHOLDERAPI.isPresent() && PluginHooks.PLACEHOLDERAPI.hasPlaceHolders( text );
    }
}
