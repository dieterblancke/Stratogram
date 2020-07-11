package com.dbsoftwares.stratogram.holograms;

import com.dbsoftwares.stratogram.Stratogram;
import com.dbsoftwares.stratogram.api.Hologram;
import com.dbsoftwares.stratogram.api.line.HologramLine;
import com.dbsoftwares.stratogram.api.line.TextLine;
import com.dbsoftwares.stratogram.api.util.Players;
import com.dbsoftwares.stratogram.holograms.line.StratoItemLine;
import com.dbsoftwares.stratogram.holograms.line.StratoTextLine;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.UnaryOperator;

public class StratoHologram implements Hologram
{

    protected final List<HologramLine> lines = Collections.synchronizedList( new ArrayList<>() );
    private final long createdAt;
    private Location location;

    private UnaryOperator<String> placeholderFormatFunction;
    private boolean visibleByDefault = true;

    public StratoHologram( final Location location )
    {
        this.location = location;
        this.createdAt = System.currentTimeMillis();

        Stratogram.getInstance().getAllHolograms().add( this );
    }

    public StratoHologram( final Location location, UnaryOperator<String> placeholderFormatFunction )
    {
        this( location );
        this.placeholderFormatFunction = placeholderFormatFunction;
    }

    @Override
    public HologramLine addTextLine( final String text )
    {
        return this.addLine( new StratoTextLine( this.getNextLineLocation(), text, placeholderFormatFunction ) );
    }

    @Override
    public HologramLine addItemLine( final ItemStack item )
    {
        return this.addLine( new StratoItemLine( this.getNextLineLocation(), item ) );
    }

    private Location getNextLineLocation()
    {
        if ( lines.isEmpty() )
        {
            return this.location;
        }
        final HologramLine line = lines.get( lines.size() - 1 );
        final double margin = Stratogram.getInstance().getConfiguration().getDouble(
                "spacing." + ( line instanceof TextLine ? "text" : "item" ) + ".bottom"
        );

        return line.getLocation().clone().subtract( 0, margin, 0 );
    }

    private HologramLine addLine( final HologramLine line )
    {
        this.lines.add( line );

        return line;
    }

    @Override
    public HologramLine getLine( final int index )
    {
        return this.lines.get( index );
    }

    @Override
    public HologramLine removeLine( final int index )
    {
        final HologramLine line = this.getLine( index );

        line.remove();
        return this.lines.remove( index );
    }

    @Override
    public void clear()
    {
        for ( HologramLine line : this.lines )
        {
            line.remove();
        }
        this.lines.clear();
    }

    @Override
    public int size()
    {
        return this.lines.size();
    }

    @Override
    public void teleport( final Location location )
    {
        final List<HologramLine> tempLines = new ArrayList<>( this.lines );
        this.clear();

        this.location = location;
        for ( HologramLine line : tempLines )
        {
            line.teleport( this.getNextLineLocation() );
            this.addLine( line );
        }
    }

    @Override
    public Location getLocation()
    {
        return this.location;
    }

    @Override
    public long getCreatedAt()
    {
        return this.createdAt;
    }

    @Override
    public void delete()
    {
        this.attemptDeletion();
    }

    @Override
    public void update()
    {
        for ( HologramLine line : lines )
        {
            if ( line.shouldUpdate() )
            {
                line.update();
            }
        }
    }

    @Override
    public void setVisibleByDefault( final boolean visible )
    {
        if ( this.visibleByDefault != visible )
        {
            if ( visible )
            {
                Stratogram.getInstance().getHologramManager().sendSpawnPacket( this, Bukkit.getOnlinePlayers() );
            }
            else
            {
                Stratogram.getInstance().getHologramManager().sendDestroyPacket( this, Bukkit.getOnlinePlayers() );
            }
            this.visibleByDefault = visible;
        }
    }

    @Override
    public void hideTo( final Players players )
    {
        if ( players.getPlayerNames() == null )
        {
            this.showTo( Players.all() );
        }
        else
        {
            if ( players.isAll() )
            {
                this.setVisibleByDefault( false );
            }
            else
            {
                Stratogram.getInstance().getHologramManager().sendDestroyPacket( this, players.getPlayerList() );
            }
        }
    }

    @Override
    public void showTo( final Players players )
    {
        if ( players.getPlayerNames() == null )
        {
            this.hideTo( Players.all() );
        }
        else
        {
            if ( players.isAll() )
            {
                this.setVisibleByDefault( true );
            }
            else
            {
                Stratogram.getInstance().getHologramManager().sendSpawnPacket( this, players.getPlayerList() );
            }
        }
    }

    private void attemptDeletion()
    {
        this.location = null;
        this.clear();
    }
}
