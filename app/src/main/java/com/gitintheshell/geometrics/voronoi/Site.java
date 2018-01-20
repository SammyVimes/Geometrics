package com.gitintheshell.geometrics.voronoi;

import com.gitintheshell.geometrics.base.Vector2D;

// used both for sites and for vertices
public class Site
{
    Vector2D coord;
    int sitenbr;

    public Site()
    {
        coord = new Vector2D(0,0);
    }
}
