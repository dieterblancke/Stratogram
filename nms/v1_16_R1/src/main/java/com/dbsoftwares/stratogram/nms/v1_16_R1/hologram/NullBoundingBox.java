package com.dbsoftwares.stratogram.nms.v1_16_R1.hologram;

import net.minecraft.server.v1_16_R1.AxisAlignedBB;
import net.minecraft.server.v1_16_R1.BlockPosition;
import net.minecraft.server.v1_16_R1.EnumDirection;
import net.minecraft.server.v1_16_R1.Vec3D;

public class NullBoundingBox extends AxisAlignedBB
{

    public NullBoundingBox()
    {
        super( 0, 0, 0, 0, 0, 0 );
    }

    @Override
    public double a()
    {
        return 0.0;
    }

    @Override
    public AxisAlignedBB a( AxisAlignedBB arg0 )
    {
        return this;
    }

    @Override
    public AxisAlignedBB a( double arg0, double arg1, double arg2 )
    {
        return this;
    }

    @Override
    public AxisAlignedBB grow( double arg0, double arg1, double arg2 )
    {
        return this;
    }

    @Override
    public AxisAlignedBB shrink( double arg0 )
    {
        return this;
    }

    @Override
    public AxisAlignedBB a( BlockPosition arg0 )
    {
        return this;
    }

    @Override
    public boolean a( double arg0, double arg1, double arg2, double arg3, double arg4, double arg5 )
    {
        return false;
    }

    @Override
    public AxisAlignedBB g( double arg0 )
    {
        return this;
    }

    @Override
    public AxisAlignedBB b( Vec3D arg0 )
    {
        return this;
    }

    @Override
    public AxisAlignedBB b( AxisAlignedBB arg0 )
    {
        return this;
    }

    @Override
    public AxisAlignedBB b( double arg0, double arg1, double arg2 )
    {
        return this;
    }

    @Override
    public boolean c( AxisAlignedBB arg0 )
    {
        return false;
    }

    @Override
    public AxisAlignedBB d( double arg0, double arg1, double arg2 )
    {
        return this;
    }

    @Override
    public double a( EnumDirection.EnumAxis arg0 )
    {
        return 0.0;
    }

    @Override
    public double b( EnumDirection.EnumAxis arg0 )
    {
        return 0.0;
    }

    @Override
    public boolean e( double arg0, double arg1, double arg2 )
    {
        return false;
    }

    @Override
    public double b()
    {
        return 0.0;
    }

    @Override
    public double c()
    {
        return 0.0;
    }

    @Override
    public double d()
    {
        return 0.0;
    }

    @Override
    public boolean d( Vec3D var0 )
    {
        return false;
    }

    @Override
    public Vec3D f()
    {
        return Vec3D.a;
    }
}