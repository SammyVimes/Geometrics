package com.gitintheshell.geometrics.delaunay;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.gitintheshell.geometrics.base.NotEnoughPointsException;
import com.gitintheshell.geometrics.base.Polygon2D;
import com.gitintheshell.geometrics.base.Triangle2D;
import com.gitintheshell.geometrics.base.Vector2D;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Semyon on 20.01.2018.
 */

public class DelaunayView extends FrameLayout implements GestureDetector.OnGestureListener {

    private DelaunayTriangulator delaunayTriangulator;
    private List<Vector2D> pointSet = new ArrayList<>();
    private Paint paint = new Paint();
    private Paint circlePaint = new Paint();
    private Paint voronoiPaint = new Paint();
    private Paint pointPaint = new Paint();

    private boolean delaunayMode = true;

    VoronoiGenerator voronoiGenerator = new VoronoiGenerator();

    public void setDelaunayMode(final boolean delaunayMode) {
        this.delaunayMode = delaunayMode;
        invalidate();
    }

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

    private void init() {
        this.setWillNotDraw(false);

        final GestureDetector gestureDetector = new GestureDetector(this.getContext(), this);
        setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

        paint.setColor(Color.RED);
        paint.setStrokeWidth(3.0f);
        circlePaint.setStyle(Paint.Style.STROKE);
        circlePaint.setColor(Color.BLUE);
        circlePaint.setStrokeWidth(5.0f);
        delaunayTriangulator = new DelaunayTriangulator(pointSet);

        pointPaint = new Paint();
        pointPaint.setColor(Color.MAGENTA);
        pointPaint.setStrokeWidth(30.0f);

        voronoiPaint.setStyle(Paint.Style.STROKE);
        voronoiPaint.setColor(Color.GREEN);
        voronoiPaint.setStrokeWidth(5.0f);

    }

    private Executor executor = Executors.newSingleThreadExecutor();

    private void retriangulate() {

        executor.execute(() -> {
            try {
                delaunayTriangulator.triangulate();
                voronoiGenerator.generate(delaunayTriangulator.getTriangles());
                ready = true;
                postInvalidate();
            } catch (NotEnoughPointsException e1) {
            }
        });
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        for (Vector2D a : pointSet) {
            canvas.drawPoint((float) a.x, (float) a.y, pointPaint);
        }

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

            final List<Polygon2D> polygons = voronoiGenerator.getPolygons();
            for (int i = 0; i < polygons.size(); i++) {
                final Polygon2D polygon = polygons.get(i);
                final List<Vector2D> vertices = polygon.getVertices();

                Path path = new Path();
                path.reset();
                path.moveTo((float) vertices.get(0).x, (float) vertices.get(0).y);
                final List<Vector2D> vector2Ds = vertices.subList(1, vertices.size());
                for (Vector2D vertice : vector2Ds) {
                    path.lineTo((float) vertice.x, (float) vertice.y);
                }
                canvas.drawPath(path, voronoiPaint);
            }

        }

        super.onDraw(canvas);
    }

    @Override
    public boolean onDown(final MotionEvent e) {
        return true;
    }

    @Override
    public void onShowPress(final MotionEvent e) {

    }

    volatile boolean ready = true;

    @Override
    public boolean onSingleTapUp(final MotionEvent e) {
        final float x = e.getX();
        final float y = e.getY();
        pointSet.add(new Vector2D(x, y));
        invalidate();
        ready = false;
        retriangulate();
        return true;
    }

    @Override
    public boolean onScroll(final MotionEvent e1, final MotionEvent e2, final float distanceX, final float distanceY) {
        return false;
    }

    @Override
    public void onLongPress(final MotionEvent e) {

    }

    @Override
    public boolean onFling(final MotionEvent e1, final MotionEvent e2, final float velocityX, final float velocityY) {
        return false;
    }

}
