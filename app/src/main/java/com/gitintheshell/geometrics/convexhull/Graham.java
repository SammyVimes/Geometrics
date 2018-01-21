package com.gitintheshell.geometrics.convexhull;

import android.support.annotation.Nullable;

import com.annimon.stream.Stream;
import com.gitintheshell.geometrics.base.Vector2D;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Semyon on 21.01.2018.
 */

public class Graham {

    private boolean reverse = false;

    private List<Vector2D> points = new ArrayList<>();

    private Vector2D anchorPoint = null;

    private double findPolarAngle(@Nullable Vector2D a, @Nullable Vector2D b) {
        double ONE_RADIAN = 57.295779513082;
        double deltaX, deltaY;

        //if the points are undefined, return a zero difference angle.
        if (a == null || b == null) return 0;

        deltaX = (b.x - a.x);
        deltaY = (b.y - a.y);

        if (deltaX == 0 && deltaY == 0) {
            return 0;
        }

        double angle = Math.atan2(deltaY, deltaX) * ONE_RADIAN;

        if (this.reverse) {
            if (angle <= 0) {
                angle += 360;
            }
        } else {
            if (angle >= 0) {
                angle += 360;
            }
        }

        return angle;
    }

    public void addPoint(double x, double y) {
        //Check for a new anchor
        boolean newAnchor =
                (this.anchorPoint == null) ||
                        (this.anchorPoint.y > y) ||
                        (this.anchorPoint.y == y && this.anchorPoint.x > x);

        if (newAnchor) {
            if (this.anchorPoint != null) {
                this.points.add(new Vector2D(this.anchorPoint.x, this.anchorPoint.y));
            }
            this.anchorPoint = new Vector2D(x, y);
        } else {
            this.points.add(new Vector2D(x, y));
        }
    }

    private List<Vector2D> sortPoints() {
        final ArrayList<Vector2D> newList = new ArrayList<>(this.points);
        newList.sort((a, b) -> {

            double polarA = findPolarAngle(anchorPoint, a);
            double polarB = findPolarAngle(anchorPoint, b);

            if (polarA < polarB) {
                return -1;
            }
            if (polarA > polarB) {
                return 1;
            }

            return 0;
        });
        return newList;
    }

    private boolean checkPoints(Vector2D p0, Vector2D p1, Vector2D p2) {
        double difAngle;
        double cwAngle = findPolarAngle(p0, p1);
        double ccwAngle = findPolarAngle(p0, p2);

        if (cwAngle > ccwAngle) {

            difAngle = cwAngle - ccwAngle;

            return !(difAngle > 180);

        } else if (cwAngle < ccwAngle) {

            difAngle = ccwAngle - cwAngle;

            return (difAngle > 180);

        }

        return true;
    }

    public List<Vector2D> getHull() {
        List<Vector2D> hullPoints = new ArrayList<>();
        List<Vector2D> points;
        int pointsLength;

        this.reverse = Stream.of(this.points).allMatch(point -> (point.x < 0 && point.y < 0));

        points = sortPoints();
        pointsLength = points.size();

        //If there are less than 3 points, joining these points creates a correct hull.
        if (pointsLength < 3) {
            points.add(0, this.anchorPoint);
            return points;
        }

        //move first two points to output array
        hullPoints.add(points.remove(0));
        hullPoints.add(points.remove(0));

        //scan is repeated until no concave points are present.
        while (true) {
            Vector2D p0, p1, p2;

            hullPoints.add(points.remove(0));
            int sz = hullPoints.size();
            p0 = hullPoints.get(sz - 3);
            p1 = hullPoints.get(sz - 2);
            p2 = hullPoints.get(sz - 1);

            if (checkPoints(p0, p1, p2)) {
                hullPoints.remove(hullPoints.size() - 2);
            }

            if (points.size() == 0) {
                if (pointsLength == hullPoints.size()) {
                    //check for duplicate anchorPoint edge-case, if not found, add the anchorpoint as the first item.
                    Vector2D ap = this.anchorPoint;
                    //remove any udefined elements in the hullPoints array.
                    hullPoints = Stream.of(hullPoints).filter(v -> v != null).toList();

                    boolean hasA = Stream.of(hullPoints).anyMatch(p -> (p.x == ap.x && p.y == ap.y));

                    if (!hasA) {
                        hullPoints.add(0, this.anchorPoint);
                    }
                    return hullPoints;
                }
                points = hullPoints;
                pointsLength = points.size();
                hullPoints = new ArrayList<>();
                hullPoints.add(points.remove(0));
                hullPoints.add(points.remove(0));
            }
        }
    }


}
