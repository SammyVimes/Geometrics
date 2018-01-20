package com.gitintheshell.geometrics.delaunay;

import com.gitintheshell.geometrics.base.Graph;
import com.gitintheshell.geometrics.base.Polygon2D;
import com.gitintheshell.geometrics.base.Triangle2D;
import com.gitintheshell.geometrics.base.Vector2D;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Semyon on 20.01.2018.
 */

public class VoronoiGenerator {

    private List<Polygon2D> polygons = new ArrayList<>();

    public void generate(final List<Triangle2D> triangles) {
        // Keep track of sites done; no drawing for initial triangles sites
        polygons = new ArrayList<>();
        Set<Vector2D> done = new HashSet<>();
        for (Triangle2D triangle : triangles) {
            for (Vector2D site : triangle) {
                if (done.contains(site)) continue;
                done.add(site);
                List<Triangle2D> list = surroundingTriangles(triangles, site, triangle);

                List<Vector2D> vertices = new ArrayList<>(list.size());
                int i = 0;
                for (Triangle2D tri : list) {
                    vertices.add(tri.getCircumcenter());
                }
                polygons.add(new Polygon2D(vertices));
            }
        }
    }

    public List<Triangle2D> surroundingTriangles(final List<Triangle2D> triangles, Vector2D site, Triangle2D triangle) {
        List<Triangle2D> list = new ArrayList<>();
        Triangle2D start = triangle;
        Vector2D guide = triangle.getVertexButNot(site);        // Affects cw or ccw
        while (true) {
            list.add(triangle);
            Triangle2D previous = triangle;
            triangle = this.neighborOpposite(triangles, guide, triangle); // Next triangle
            if (triangle == null) {
                return list;
            }
            guide = previous.getVertexButNot(site, guide);     // Update guide
            if (triangle == start) {
                break;
            }
        }
        return list;
    }

    public Triangle2D neighborOpposite(final List<Triangle2D> triangles, Vector2D site, Triangle2D triangle) {
        Graph<Triangle2D> triGraph = new Graph<>();

        for (Triangle2D tri : triangles) {
            triGraph.add(tri);
        }

        for (Triangle2D triangle1 : triangles) {
            for (Triangle2D other : triangles) {
                if (triangle1.isNeighbor(other)) {
                    triGraph.add(triangle1, other);
                }
            }
        }


        if (triGraph.nodeSet().size() < 2) {
            return null;
        }

        for (Triangle2D neighbor : triGraph.neighbors(triangle)) {
            if (!neighbor.containsInc(site)) {
                return neighbor;
            }
        }

        return null;
    }

    public List<Polygon2D> getPolygons() {
        return polygons;
    }
}
