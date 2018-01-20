package com.gitintheshell.geometrics.base;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.Spliterator;
import java.util.function.Consumer;

/**
 * 2D triangle class implementation.
 *
 * @author Semyon Danilov
 */
public class Triangle2D implements Iterable<Vector2D> {

    public Vector2D a;
    public Vector2D b;
    public Vector2D c;

    /**
     * Constructor of the 2D triangle class used to create a new triangle
     * instance from three 2D vectors describing the triangle's vertices.
     *
     * @param a The first vertex of the triangle
     * @param b The second vertex of the triangle
     * @param c The third vertex of the triangle
     */
    public Triangle2D(Vector2D a, Vector2D b, Vector2D c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    /**
     * Tests if a 2D point lies inside this 2D triangle. See Real-Time Collision
     * Detection, chap. 5, p. 206.
     *
     * @param point The point to be tested
     * @return Returns true iff the point lies inside this 2D triangle
     */
    public boolean contains(Vector2D point) {
        double pab = point.sub(a).cross(b.sub(a));
        double pbc = point.sub(b).cross(c.sub(b));

        if (!hasSameSign(pab, pbc)) {
            return false;
        }

        double pca = point.sub(c).cross(a.sub(c));

        if (!hasSameSign(pab, pca)) {
            return false;
        }

        return true;
    }

    public boolean containsInc(Vector2D point) {
        return a.equals(point) || b.equals(point) || c.equals(point);
    }

    /**
     * Tests if a given point lies in the circumcircle of this triangle. Let the
     * triangle ABC appear in counterclockwise (CCW) order. Then when det &gt;
     * 0, the point lies inside the circumcircle through the three points a, b
     * and c. If instead det &lt; 0, the point lies outside the circumcircle.
     * When det = 0, the four points are cocircular. If the triangle is oriented
     * clockwise (CW) the result is reversed. See Real-Time Collision Detection,
     * chap. 3, p. 34.
     *
     * @param point The point to be tested
     * @return Returns true iff the point lies inside the circumcircle through
     * the three points a, b, and c of the triangle
     */
    public boolean isPointInCircumcircle(Vector2D point) {
        double a11 = a.x - point.x;
        double a21 = b.x - point.x;
        double a31 = c.x - point.x;

        double a12 = a.y - point.y;
        double a22 = b.y - point.y;
        double a32 = c.y - point.y;

        double a13 = (a.x - point.x) * (a.x - point.x) + (a.y - point.y) * (a.y - point.y);
        double a23 = (b.x - point.x) * (b.x - point.x) + (b.y - point.y) * (b.y - point.y);
        double a33 = (c.x - point.x) * (c.x - point.x) + (c.y - point.y) * (c.y - point.y);

        double det = a11 * a22 * a33 + a12 * a23 * a31 + a13 * a21 * a32 - a13 * a22 * a31 - a12 * a21 * a33
                - a11 * a23 * a32;

        if (isOrientedCCW()) {
            return det > 0.0d;
        }

        return det < 0.0d;
    }

    /**
     * Test if this triangle is oriented counterclockwise (CCW). Let A, B and C
     * be three 2D points. If det &gt; 0, C lies to the left of the directed
     * line AB. Equivalently the triangle ABC is oriented counterclockwise. When
     * det &lt; 0, C lies to the right of the directed line AB, and the triangle
     * ABC is oriented clockwise. When det = 0, the three points are colinear.
     * See Real-Time Collision Detection, chap. 3, p. 32
     *
     * @return Returns true iff the triangle ABC is oriented counterclockwise
     * (CCW)
     */
    public boolean isOrientedCCW() {
        double a11 = a.x - c.x;
        double a21 = b.x - c.x;

        double a12 = a.y - c.y;
        double a22 = b.y - c.y;

        double det = a11 * a22 - a12 * a21;

        return det > 0.0d;
    }

    /**
     * Returns true if this triangle contains the given edge.
     *
     * @param edge The edge to be tested
     * @return Returns true if this triangle contains the edge
     */
    public boolean isNeighbour(Edge2D edge) {
        return (a == edge.a || b == edge.a || c == edge.a) && (a == edge.b || b == edge.b || c == edge.b);
    }

    /**
     * Returns the vertex of this triangle that is not part of the given edge.
     *
     * @param edge The edge
     * @return The vertex of this triangle that is not part of the edge
     */
    public Vector2D getNoneEdgeVertex(Edge2D edge) {
        if (a != edge.a && a != edge.b) {
            return a;
        } else if (b != edge.a && b != edge.b) {
            return b;
        } else if (c != edge.a && c != edge.b) {
            return c;
        }

        return null;
    }

    /**
     * Returns true if the given vertex is one of the vertices describing this
     * triangle.
     *
     * @param vertex The vertex to be tested
     * @return Returns true if the Vertex is one of the vertices describing this
     * triangle
     */
    public boolean hasVertex(Vector2D vertex) {
        if (a == vertex || b == vertex || c == vertex) {
            return true;
        }

        return false;
    }

    /**
     * Returns an EdgeDistancePack containing the edge and its distance nearest
     * to the specified point.
     *
     * @param point The point the nearest edge is queried for
     * @return The edge of this triangle that is nearest to the specified point
     */
    public EdgeDistancePack findNearestEdge(Vector2D point) {
        EdgeDistancePack[] edges = new EdgeDistancePack[3];

        edges[0] = new EdgeDistancePack(new Edge2D(a, b),
                computeClosestPoint(new Edge2D(a, b), point).sub(point).mag());
        edges[1] = new EdgeDistancePack(new Edge2D(b, c),
                computeClosestPoint(new Edge2D(b, c), point).sub(point).mag());
        edges[2] = new EdgeDistancePack(new Edge2D(c, a),
                computeClosestPoint(new Edge2D(c, a), point).sub(point).mag());

        Arrays.sort(edges);
        return edges[0];
    }

    /**
     * Computes the closest point on the given edge to the specified point.
     *
     * @param edge  The edge on which we search the closest point to the specified
     *              point
     * @param point The point to which we search the closest point on the edge
     * @return The closest point on the given edge to the specified point
     */
    private Vector2D computeClosestPoint(Edge2D edge, Vector2D point) {
        Vector2D ab = edge.b.sub(edge.a);
        double t = point.sub(edge.a).dot(ab) / ab.dot(ab);

        if (t < 0.0d) {
            t = 0.0d;
        } else if (t > 1.0d) {
            t = 1.0d;
        }

        return edge.a.add(ab.mult(t));
    }

    /**
     * Tests if the two arguments have the same sign.
     *
     * @param a The first floating point argument
     * @param b The second floating point argument
     * @return Returns true iff both arguments have the same sign
     */
    private boolean hasSameSign(double a, double b) {
        return Math.signum(a) == Math.signum(b);
    }

    public Vector2D getCircumcenter() {
        return getCircumcenter(a, b, c);
    }

    public Vector2D getCircumcenter(Vector2D a, Vector2D b, Vector2D c) {
        // determine midpoints (average of x & y coordinates)
        Vector2D midAB = midpoint(a, b);
        Vector2D midBC = midpoint(b, c);

        // determine slope
        // we need the negative reciprocal of the slope to get the slope of the perpendicular bisector
        double slopeAB = -1 / slope(a, b);
        double slopeBC = -1 / slope(b, c);

        // y = mx + b
        // solve for b
        double bAB = midAB.y - slopeAB * midAB.x;
        double bBC = midBC.y - slopeBC * midBC.x;

        // solve for x & y
        // x = (b1 - b2) / (m2 - m1)
        double x = (bAB - bBC) / (slopeBC - slopeAB);
        Vector2D circumcenter = new Vector2D(x, (slopeAB * x) + bAB);

        return circumcenter;
    }

    public double slope(Vector2D from, Vector2D to) {
        return (to.y - from.y) / (to.x - from.x);
    }

    public Vector2D midpoint(Vector2D a, Vector2D b) {
        return new Vector2D((a.x + b.x) / 2, (a.y + b.y) / 2);
    }

    @Override
    public String toString() {
        return "Triangle2D[" + a + ", " + b + ", " + c + "]";
    }

    @Override
    public Iterator<Vector2D> iterator() {
        return new Iterator<Vector2D>() {

            int cur = 0;

            @Override
            public boolean hasNext() {
                return cur < 3;
            }

            @Override
            public Vector2D next() {
                try {
                    switch (cur) {
                        case 0:
                            return a;
                        case 1:
                            return b;
                        case 2:
                            return c;
                        default:
                            throw new RuntimeException("You stupid motherfuuu");
                    }
                } finally {
                    cur++;
                }
            }
        };
    }

    @Override
    public void forEach(final Consumer<? super Vector2D> action) {
        action.accept(a);
        action.accept(b);
        action.accept(c);
    }

    @Override
    public Spliterator<Vector2D> spliterator() {
        return null;
    }


    /**
     * Get arbitrary vertex of this triangle, but not any of the bad vertices.
     *
     * @param badVertices one or more bad vertices
     * @return a vertex of this triangle, but not one of the bad vertices
     * @throws NoSuchElementException if no vertex found
     */
    public Vector2D getVertexButNot(Vector2D... badVertices) {
        Collection<Vector2D> bad = Arrays.asList(badVertices);
        for (Vector2D v : this) {
            if (!bad.contains(v)) {
                return v;
            }
        }
        throw new NoSuchElementException("No vertex found");
    }

    /**
     * True iff triangles are neighbors. Two triangles are neighbors if they
     * share a facet.
     *
     * @param triangle the other Triangle
     * @return true iff this Triangle is a neighbor of triangle
     */
    public boolean isNeighbor(Triangle2D triangle) {
        int count = 0;
        for (Vector2D vertex : this) {

            if (!triangle.containsInc(vertex)) {
                count++;
            }
        }
        return count == 1;
    }

    /**
     * Report the facet opposite vertex.
     *
     * @param vertex a vertex of this Triangle
     * @return the facet opposite vertex
     * @throws IllegalArgumentException if the vertex is not in triangle
     */
    public Set<Vector2D> facetOpposite(Vector2D vertex) {
        Set<Vector2D> facet = new HashSet<Vector2D>();
        facet.add(a);
        facet.add(b);
        facet.add(c);
        if (!facet.remove(vertex))
            throw new IllegalArgumentException("Vertex not in triangle");
        return facet;
    }

}