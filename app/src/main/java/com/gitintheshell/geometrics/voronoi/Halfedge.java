package com.gitintheshell.geometrics.voronoi;

public class Halfedge
{
    Halfedge ELleft, ELright;
    Edge ELedge;
    boolean deleted;
    int ELpm;
    Site vertex;
    double ystar;
    Halfedge PQnext;

    public Halfedge()
    {
        PQnext = null;
    }
}
