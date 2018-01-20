package com.gitintheshell.geometrics.voronoi;

import com.gitintheshell.geometrics.base.Polygon2D;
import com.gitintheshell.geometrics.base.Triangle2D;
import com.gitintheshell.geometrics.base.Vector2D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Semyon on 20.01.2018.
 */

public class VoronoiGenerator {

    private List<Polygon2D> polygons = Collections.emptyList();

    public void generate(final List<Triangle2D> delaunay) {
        polygons = createPolygonList(delaunay);
        if (polygons == null) {
            polygons = Collections.emptyList();
        }
    }

    private List<Polygon2D> createPolygonList(final List<Triangle2D> delaunay) {
        List<Polygon2D> polygons = new ArrayList<>();

        for (Triangle2D t : delaunay) {
            for (Vector2D p : t) {
                Polygon2D polygon = constructPolygon(p, t, delaunay);
                polygons.add(polygon);
            }
        }
        return polygons;
    }

    private Polygon2D triangleListToPolygon(List<Triangle2D> triangles) {
        List<Vector2D> vertices = new ArrayList<>();
        for (Triangle2D triangle : triangles) {
            vertices.add(calcIntersection(triangle));
        }
        return new Polygon2D(vertices);
    }


    private Vector2D[] calcPerpendicularBisector(Vector2D p1, Vector2D p2) {
        Vector2D diff = new Vector2D(p2.x - p1.x, p2.y - p1.y);
        double[] v = new double[]{diff.x, diff.y, 0};
        double[] z = new double[]{0, 0, 1};

        double u1 = v[0];
        double u2 = v[1];
        double u3 = v[2];

        double v1 = z[0];
        double v2 = z[1];
        double v3 = z[2];

        double[] cross = new double[]{u2 * v3 - u3 * v2, u3 * v1 - u1 * v3, u1 * v2 - u2 * v1};

        Vector2D middle = new Vector2D(p1.x + diff.x / 2.0, p1.y + diff.y / 2.0);
        Vector2D newPoint = new Vector2D(middle.x + cross[0], middle.y + cross[1]);
        return new Vector2D[]{middle, newPoint};
    }

    private double det(Vector2D a, Vector2D b) {
        return a.x * b.y - a.y * b.x;
    }

    private Vector2D line_intersection(Vector2D[] line1, Vector2D[] line2) {
        Vector2D xdiff = new Vector2D(line1[0].x - line1[1].x, line2[0].x - line2[1].x);
        Vector2D ydiff = new Vector2D(line1[0].y - line1[1].y, line2[0].y - line2[1].y);

        double div = det(xdiff, ydiff);
        if (div == 0) {
            throw new RuntimeException("lines do not intersect");
        }
        Vector2D d = new Vector2D(det(line1[0], line1[1]), det(line2[0], line2[1]));
        double x = det(d, xdiff) / div;
        double y = det(d, ydiff) / div;
        return new Vector2D(x, y);
    }

    private Vector2D calcIntersection(final Triangle2D triangle) {
        Vector2D[] pb1 = calcPerpendicularBisector(triangle.a, triangle.b);
        Vector2D[] pb2 = calcPerpendicularBisector(triangle.b, triangle.c);
        return line_intersection(pb1, pb2);
    }

    private List<Triangle2D> getTrianglesAroundPoint(final Vector2D p, final Triangle2D t, final List<Triangle2D> all) {
        Triangle2D thisT = t;
        List<Triangle2D> triangles = new ArrayList<>();
        while (true) {
            triangles.add(thisT);
            Triangle2D nextT = getNextTriangle(p, all, triangles);

            if (nextT == null) {
                break;
            }

            thisT = nextT;
        }
        return triangles;
    }

    private boolean valid(Triangle2D t1, List<Triangle2D> triangles) {
        if (t1 == null) {
            return false;
        }
        for (Triangle2D t : triangles) {
            if (equalT(t, t1)) {
                return false;
            }
        }
        return true;
    }

    private boolean equalT(Triangle2D t1, Triangle2D t2) {
        return t1 != null && t2 != null && t1.a.equals(t2.a) && t1.b.equals(t2.b) && t1.c.equals(t2.c);
    }

    private Triangle2D getNextTriangle(final Vector2D p, final List<Triangle2D> t, final List<Triangle2D> lastT) {
        for (Triangle2D ti : t) {
            if (valid(ti, lastT)) {
                if (ti.a.equals(p) || ti.b.equals(p) || ti.c.equals(p)){
                    return ti;
                }
            }
        }

        return null;
    }

    private Polygon2D constructPolygon(final Vector2D p, final Triangle2D t, final List<Triangle2D> all) {
        return triangleListToPolygon(getTrianglesAroundPoint(p, t, all));
    }

    public List<Polygon2D> getPolygons() {
        return polygons;
    }

}
