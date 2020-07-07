package com.dbsoftwares.stratogram.api;

public class Utils
{

    public static double square( final double number )
    {
        return number * number;
    }

    public static String limitLength( String str, final int maxLength )
    {
        if ( str != null && str.length() > maxLength )
        {
            str = str.substring( 0, maxLength );
        }
        return str;
    }
}
