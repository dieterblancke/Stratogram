package com.dbsoftwares.stratogram.example;

import com.dbsoftwares.stratogram.api.Hologram;
import com.dbsoftwares.stratogram.api.util.Players;
import com.dbsoftwares.stratogram.holograms.StratoHologram;
import com.dbsoftwares.stratogram.utils.XMaterial;
import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;

import java.util.List;

public class HologramApiExample
{

    private final List<Hologram> holograms = Lists.newArrayList();

    public void setupHolograms( final Player player )
    {
        // Top Kills
        Hologram hologram = new StratoHologram(
                new Location( Bukkit.getWorld( "world" ), 0, 50, 0 )
        );

        hologram.addItemLine( XMaterial.DIAMOND_SWORD.parseItem() );
        hologram.addTextLine( "&e&lTop Killers" );

        for ( int i = 0; i < 5; i++ )
        {
            // Using random "dummy" data (RandomUserName[IDX]), kills: [IDX * 5])
            hologram.addTextLine( "&b#" + ( i + 1 ) + " &eRandomUserName" + i + " &a" + ( i * 5 ) );
        }
        this.holograms.add( hologram );

        // Player Stats
        final ItemStack skullStack = buildSkull( player );
        hologram = new StratoHologram(
                new Location( Bukkit.getWorld( "world" ), 5, 50, 5 ),
                new PlayerStatReplacer( player.getName() )
        );
        hologram.addItemLine( skullStack );
        hologram.addTextLine( "&eKills: &b{kills}" );
        hologram.addTextLine( "&eDeaths: &b{deaths}" );
        hologram.setVisibleByDefault( false );
        hologram.showTo( Players.of( player ) );

        this.holograms.add( hologram );
    }

    private ItemStack buildSkull( final Player player )
    {
        final ItemStack skullStack = new ItemStack( Material.SKULL_ITEM, 1, (byte) 3 );
        final SkullMeta meta = (SkullMeta) skullStack.getItemMeta();

        meta.setOwner( player.getName() );
        skullStack.setItemMeta( meta );

        return skullStack;
    }

    public void shutdown()
    {
        for ( Hologram hologram : this.holograms )
        {
            hologram.delete();
        }
        holograms.clear();
    }
}
