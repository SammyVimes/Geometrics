package com.gitintheshell.geometrics.convexhull;

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
import com.gitintheshell.geometrics.base.Triangle2D;
import com.gitintheshell.geometrics.base.Vector2D;

import java.util.Collections;
import java.util.List;

/**
 * Created by Semyon on 21.01.2018.
 */

public class ConvexHullView extends PointsView {

    Graham graham = new Graham();
    private Paint paint = new Paint();

    private List<Vector2D> curHull = Collections.emptyList();

    public ConvexHullView(@NonNull final Context context) {
        super(context);
        init();
    }

    public ConvexHullView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ConvexHullView(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        paint.setColor(Color.RED);
        paint.setStrokeWidth(3.0f);
    }

    public ConvexHullView(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onNewPoint(final Vector2D newPoint) {
        graham.addPoint(newPoint.x, newPoint.y);
        curHull = graham.getHull();
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        final int size = curHull.size();
        for (int i = 0; i < size; i++) {
            Vector2D point = curHull.get(i);
            Vector2D next = i == (size - 1) ? curHull.get(0) : curHull.get(i + 1);
            canvas.drawLine((float) point.x, (float) point.y, (float) next.x, (float) next.y, paint);
        }

    }
}
