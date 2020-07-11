package com.dbsoftwares.stratogram.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.util.EulerAngle;

public class Locations
{

    public static Location toLocation( final String s )
    {
        if ( s == null || s.equals( "NONE" ) )
        {
            return null;
        }
        final World world = Bukkit.getWorld( s.split( ";" )[0] );
        final double x = Double.parseDouble( s.split( ";" )[1] );
        final double y = Double.parseDouble( s.split( ";" )[2] );
        final double z = Double.parseDouble( s.split( ";" )[3] );

        final Location loc = new Location( world, x, y, z );

        if ( s.split( ";" ).length > 4 )
        {
            final float yaw = Float.parseFloat( s.split( ";" )[4] );
            final float pitch = Float.parseFloat( s.split( ";" )[5] );

            loc.setYaw( yaw );
            loc.setPitch( pitch );
        }
        return loc;
    }

    public static Location toLocation( final World world, final String s )
    {
        if ( s == null || s.equals( "NONE" ) )
        {
            return null;
        }
        final double x = Double.parseDouble( s.split( ";" )[1] );
        final double y = Double.parseDouble( s.split( ";" )[2] );
        final double z = Double.parseDouble( s.split( ";" )[3] );

        final Location loc = new Location( world, x, y, z );

        if ( s.split( ";" ).length > 4 )
        {
            final float yaw = Float.parseFloat( s.split( ";" )[4] );
            final float pitch = Float.parseFloat( s.split( ";" )[5] );

            loc.setYaw( yaw );
            loc.setPitch( pitch );
        }

        return loc;
    }

    public static String toString( final Location loc )
    {
        if ( loc == null )
        {
            return "NONE";
        }
        return loc.getWorld().getName() +
                ";" + loc.getX() +
                ";" + loc.getY() +
                ";" + loc.getZ() +
                ";" + loc.getYaw() +
                ";" + loc.getPitch();
    }

    public static String toStringRound( final Location loc )
    {
        if ( loc == null )
        {
            return "NONE";
        }
        return loc.getWorld().getName() +
                ";" + loc.getBlockX() +
                ";" + loc.getBlockY() +
                ";" + loc.getBlockZ();
    }

    public static EulerAngle toEulerAngle( final String angle )
    {
        if ( angle == null || angle.equals( "NONE" ) )
        {
            return null;
        }
        return new EulerAngle(
                Double.parseDouble( angle.split( ";" )[0] ),
                Double.parseDouble( angle.split( ";" )[1] ),
                Double.parseDouble( angle.split( ";" )[2] )
        );
    }

    public static String toString( final EulerAngle angle )
    {
        if ( angle == null )
        {
            return "NONE";
        }
        return angle.getX() + ";" + angle.getY() + ";" + angle.getZ();
    }

    public static boolean compare( final Location l1, final Location l2 )
    {
        return l1.getWorld() == l2.getWorld()
                && l1.getBlockX() == l2.getBlockX()
                && l1.getBlockY() == l2.getBlockY()
                && l1.getBlockZ() == l2.getBlockZ();
    }
}