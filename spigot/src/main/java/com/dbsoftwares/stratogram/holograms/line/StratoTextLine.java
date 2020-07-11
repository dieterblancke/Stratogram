package com.dbsoftwares.stratogram.holograms.line;

import com.dbsoftwares.configuration.api.ISection;
import com.dbsoftwares.configuration.json.JsonSection;
import com.dbsoftwares.stratogram.Stratogram;
import com.dbsoftwares.stratogram.api.line.TextLine;
import com.dbsoftwares.stratogram.nms.api.hologram.HologramArmorStand;
import com.dbsoftwares.stratogram.pluginhooks.PluginHooks;
import com.dbsoftwares.stratogram.utils.Locations;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Location;

@Data
@EqualsAndHashCode(callSuper = true)
public class StratoTextLine extends StratoLine implements TextLine
{

    private boolean placeHoldersFound = false;
    private String text;

    public StratoTextLine( final Location previousLine, final String text )
    {
        super( previousLine.clone().subtract( 0, Stratogram.getInstance().getConfiguration().getDouble( "spacing.text.top" ), 0 ) );
        this.setText( text );
        Stratogram.getInstance().debug( "Spawning a text line at " + Locations.toString( super.getLocation() ) + "." );
    }

    @Override
    public String getText()
    {
        if ( this.text == null )
        {
            return null;
        }
        final String text = ChatColor.translateAlternateColorCodes( '&', this.text );

        return placeHoldersFound ? PluginHooks.PLACEHOLDERAPI.setPlaceHolders( text ) : text;
    }

    @Override
    public void setText( final String text )
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
        super.update();
        this.spawnIfDead();

        if ( this.nmsEntity != null )
        {
            final HologramArmorStand armorStand = (HologramArmorStand) this.nmsEntity;

            armorStand.setHologramCustomName( this.getText() );
        }
    }

    private boolean spawnIfDead()
    {
        if ( this.nmsEntity == null )
        {
            this.nmsEntity = Stratogram.getInstance().getHologramManager().spawnHologramArmorStand( this.location, this, true );
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
    public ISection asSection()
    {
        final ISection section = new JsonSection();

        section.set( "type", "text" );
        section.set( "data", this.text );

        return section;
    }

    @Override
    protected void attemptDeletion()
    {
        super.attemptDeletion();
        this.text = null;
    }
}
