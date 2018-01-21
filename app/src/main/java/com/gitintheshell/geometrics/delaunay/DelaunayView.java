package com.gitintheshell.geometrics.delaunay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;

import com.gitintheshell.geometrics.PointsView;
import com.gitintheshell.geometrics.base.NotEnoughPointsException;
import com.gitintheshell.geometrics.base.Triangle2D;
import com.gitintheshell.geometrics.base.Vector2D;
import com.gitintheshell.geometrics.voronoi.GraphEdge;
import com.gitintheshell.geometrics.voronoi.Voronoi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Semyon on 20.01.2018.
 */

public class DelaunayView extends PointsView {

    //    VoronoiGeneratorOld voronoiGeneratorOld = new VoronoiGeneratorOld();
    List<GraphEdge> graphEdges = new ArrayList<>();
    Voronoi voronoi = new Voronoi(1.0d);
    volatile boolean ready = true;
    private DelaunayTriangulator delaunayTriangulator;
    private Paint paint = new Paint();
    private Paint circlePaint = new Paint();
    private Paint voronoiPaint = new Paint();
    private boolean delaunayMode = true;
    private Executor executor = Executors.newSingleThreadExecutor();

    public DelaunayView(@NonNull final Context context) {
        super(context);
        init();
    }

    public DelaunayView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public DelaunayView(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public DelaunayView(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    public void setDelaunayMode(final boolean delaunayMode) {
        this.delaunayMode = delaunayMode;
        invalidate();
    }

    private void init() {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3.0f);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStrokeWidth(5.0f);
        delaunayTriangulator = new DelaunayTriangulator(pointSet);

        voronoiPaint.setStyle(Paint.Style.STROKE);
        voronoiPaint.setColor(Color.GREEN);
        voronoiPaint.setStrokeWidth(5.0f);

    }

    private void retriangulate() {

        executor.execute(() -> {
            try {
                delaunayTriangulator.triangulate();

                double[] xs = new double[pointSet.size()];
                double[] ys = new double[pointSet.size()];
                for (int i = 0; i < pointSet.size(); i++) {
                    Vector2D vector2D = pointSet.get(i);
                    xs[i] = vector2D.x;
                    ys[i] = vector2D.y;
                }
                graphEdges = voronoi.generateVoronoi(xs, ys, -10_000, 10_000, -10_000, 10_000);
                if (graphEdges == null) {
                    graphEdges = Collections.emptyList();
                }
//                voronoiGeneratorOld.generate(delaunayTriangulator.getTriangles());
                ready = true;
                postInvalidate();
            } catch (NotEnoughPointsException e1) {
            }
        });
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        if (!ready) {
            return;
        }

        if (delaunayMode) {

            for (int i = 0; i < delaunayTriangulator.getTriangles().size(); i++) {
                Triangle2D triangle = delaunayTriangulator.getTriangles().get(i);
                Vector2D a = triangle.a;
                Vector2D b = triangle.b;
                Vector2D c = triangle.c;

                canvas.drawLine((float) a.x, (float) a.y, (float) b.x, (float) b.y, paint);
                canvas.drawLine((float) b.x, (float) b.y, (float) c.x, (float) c.y, paint);
                canvas.drawLine((float) c.x, (float) c.y, (float) a.x, (float) a.y, paint);
            }

            for (int i = 0; i < delaunayTriangulator.getTriangles().size(); i++) {
                Triangle2D triangle = delaunayTriangulator.getTriangles().get(i);
                Vector2D a = triangle.a;

                final Vector2D circumcenter = triangle.getCircumcenter();
                final double distance = circumcenter.distance(a);
                canvas.drawCircle((float) circumcenter.x, (float) circumcenter.y, (float) distance, circlePaint);

            }
        } else {

//            final List<Polygon2D> polygons = voronoiGeneratorOld.getPolygons();
//            for (int i = 0; i < polygons.size(); i++) {
//                final Polygon2D polygon = polygons.get(i);
//                final List<Vector2D> vertices = polygon.getVertices();
//
//                Path path = new Path();
//                path.reset();
//                path.moveTo((float) vertices.get(0).x, (float) vertices.get(0).y);
//                final List<Vector2D> vector2Ds = vertices.subList(1, vertices.size());
//                for (Vector2D vertice : vector2Ds) {
//                    path.lineTo((float) vertice.x, (float) vertice.y);
//                }
//                canvas.drawPath(path, voronoiPaint);
//            }
            for (GraphEdge graphEdge : graphEdges) {
                canvas.drawLine((float) graphEdge.x1, (float) graphEdge.y1, (float) graphEdge.x2, (float) graphEdge.y2, voronoiPaint);
            }
//            final List<Polygon2D> polygons = voronoiGeneratorOld.getPolygons();
//            for (int i = 0; i < polygons.size(); i++) {
//                final Polygon2D polygon = polygons.get(i);
//                final List<Vector2D> vertices = polygon.getVertices();
//
//                Path path = new Path();
//                path.reset();
//                path.moveTo((float) vertices.get(0).x, (float) vertices.get(0).y);
//                final List<Vector2D> vector2Ds = vertices.subList(1, vertices.size());
//                for (Vector2D vertice : vector2Ds) {
//                    path.lineTo((float) vertice.x, (float) vertice.y);
//                }
//                canvas.drawPath(path, voronoiPaint);
//            }

        }
    }

    @Override
    protected void onNewPoint(final Vector2D newPoint) {
        ready = false;
        retriangulate();
    }

}
