package com.gitintheshell.geometrics.voronoi;

import com.gitintheshell.geometrics.base.Triangle2D;
import com.gitintheshell.geometrics.base.Vector2D;

/**
 * Created by Semyon on 20.01.2018.
 */

public class VTriangle2D extends Triangle2D {

    private boolean marked;

    /**
     * Constructor of the 2D triangle class used to create a new triangle
     * instance from three 2D vectors describing the triangle's vertices.
     *
     * @param a The first vertex of the triangle
     * @param b The second vertex of the triangle
     * @param c The third vertex of the triangle
     */
    public VTriangle2D(final Vector2D a, final Vector2D b, final Vector2D c) {
        super(a, b, c);
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(final boolean marked) {
        this.marked = marked;
    }
}
