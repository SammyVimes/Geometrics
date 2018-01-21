package com.gitintheshell.geometrics;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.AttrRes;
import android.support.annotation.CallSuper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.FrameLayout;

import com.gitintheshell.geometrics.base.NotEnoughPointsException;
import com.gitintheshell.geometrics.base.Triangle2D;
import com.gitintheshell.geometrics.base.Vector2D;
import com.gitintheshell.geometrics.delaunay.DelaunayTriangulator;
import com.gitintheshell.geometrics.voronoi.GraphEdge;
import com.gitintheshell.geometrics.voronoi.Voronoi;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 * Created by Semyon on 21.01.2018.
 */

public class PointsView extends FrameLayout implements GestureDetector.OnGestureListener {

    private List<Vector2D> pointSet = new ArrayList<>();
    private Paint pointPaint = new Paint();


    public PointsView(@NonNull final Context context) {
        super(context);
        init();
    }

    public PointsView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PointsView(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PointsView(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        this.setWillNotDraw(false);

        final GestureDetector gestureDetector = new GestureDetector(this.getContext(), this);
        setOnTouchListener((v, event) -> gestureDetector.onTouchEvent(event));

        pointPaint = new Paint();
        pointPaint.setColor(Color.MAGENTA);
        pointPaint.setStrokeWidth(30.0f);
    }

    @Override
    @CallSuper
    protected void onDraw(final Canvas canvas) {
        canvas.drawColor(Color.BLACK);

        for (Vector2D a : pointSet) {
            canvas.drawPoint((float) a.x, (float) a.y, pointPaint);
        }
    }


    protected void onNewPoint(final Vector2D newPoint) {

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
        final Vector2D newPoint = new Vector2D(x, y);
        pointSet.add(newPoint);
        invalidate();
        onNewPoint(newPoint);
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
