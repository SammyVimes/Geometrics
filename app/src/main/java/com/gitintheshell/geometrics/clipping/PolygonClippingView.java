package com.gitintheshell.geometrics.clipping;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by Semyon on 21.01.2018.
 */

public class PolygonClippingView extends FrameLayout {

    List<double[]> subject, clipper, result;
    private Paint firstPolyPointPaint = new Paint();
    private Paint secondPolyPaint = new Paint();

    private Paint secondPolyPointPaint = new Paint();
    private Paint firstPolyPaint = new Paint();

    private Paint intersectionPolyPointPaint = new Paint();
    private Paint intersectionPolyPaint = new Paint();

    private boolean showFirstPoly = true;
    private boolean showSecondPoly = true;
    private boolean showIntersection = true;

    public void setShowFirstPoly(final boolean showFirstPoly) {
        this.showFirstPoly = showFirstPoly;
        postInvalidateDelayed(10);
    }

    public void setShowSecondPoly(final boolean showSecondPoly) {
        this.showSecondPoly = showSecondPoly;
        postInvalidateDelayed(10);
    }

    public void setShowIntersection(final boolean showIntersection) {
        this.showIntersection = showIntersection;
        postInvalidateDelayed(10);
    }

    public PolygonClippingView(@NonNull final Context context) {
        super(context);
        init();
    }

    public PolygonClippingView(@NonNull final Context context, @Nullable final AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public PolygonClippingView(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public PolygonClippingView(@NonNull final Context context, @Nullable final AttributeSet attrs, @AttrRes final int defStyleAttr, @StyleRes final int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    private void init() {
        setWillNotDraw(false);


        firstPolyPointPaint.setColor(Color.RED);
        firstPolyPointPaint.setStrokeWidth(30.0f);

        firstPolyPaint.setStyle(Paint.Style.STROKE);
        firstPolyPaint.setColor(Color.RED);
        firstPolyPaint.setStrokeWidth(10.0f);

        secondPolyPointPaint.setColor(Color.GREEN);
        secondPolyPointPaint.setStrokeWidth(30.0f);

        secondPolyPaint.setStyle(Paint.Style.STROKE);
        secondPolyPaint.setColor(Color.GREEN);
        secondPolyPaint.setStrokeWidth(10.0f);

        intersectionPolyPointPaint.setColor(Color.BLUE);
        intersectionPolyPointPaint.setStrokeWidth(30.0f);

        intersectionPolyPaint.setStyle(Paint.Style.STROKE);
        intersectionPolyPaint.setColor(Color.BLUE);
        intersectionPolyPaint.setStrokeWidth(10.0f);


        // these subject and clip points are assumed to be valid
        double[][] subjPoints = {{50, 150}, {200, 50}, {350, 150}, {350, 300},
                {250, 300}, {200, 250}, {150, 350}, {100, 250}, {100, 200}};

        double[][] clipPoints = {{100, 100}, {300, 100}, {300, 300}, {200, 400}, {100, 300}};

        subject = new ArrayList<>(Arrays.asList(subjPoints));
        result = new ArrayList<>(subject);
        clipper = new ArrayList<>(Arrays.asList(clipPoints));

        clipPolygon();
    }

    private void clipPolygon() {
        int len = clipper.size();
        for (int i = 0; i < len; i++) {

            int len2 = result.size();
            List<double[]> input = result;
            result = new ArrayList<>(len2);

            double[] A = clipper.get((i + len - 1) % len);
            double[] B = clipper.get(i);

            for (int j = 0; j < len2; j++) {

                double[] P = input.get((j + len2 - 1) % len2);
                double[] Q = input.get(j);

                if (isInside(A, B, Q)) {
                    if (!isInside(A, B, P)) {
                        result.add(intersection(A, B, P, Q));
                    }
                    result.add(Q);
                } else if (isInside(A, B, P)) {
                    result.add(intersection(A, B, P, Q));
                }
            }
        }
    }

    private boolean isInside(double[] a, double[] b, double[] c) {
        return (a[0] - c[0]) * (b[1] - c[1]) > (a[1] - c[1]) * (b[0] - c[0]);
    }

    private double[] intersection(double[] a, double[] b, double[] p, double[] q) {
        double A1 = b[1] - a[1];
        double B1 = a[0] - b[0];
        double C1 = A1 * a[0] + B1 * a[1];

        double A2 = q[1] - p[1];
        double B2 = p[0] - q[0];
        double C2 = A2 * p[0] + B2 * p[1];

        double det = A1 * B2 - A2 * B1;
        double x = (B2 * C1 - B1 * C2) / det;
        double y = (A1 * C2 - A2 * C1) / det;

        return new double[]{x, y};
    }

    @Override
    protected void onDraw(final Canvas canvas) {
        super.onDraw(canvas);

        if (showFirstPoly) {
            drawPolygon(canvas, subject, firstPolyPaint, firstPolyPointPaint);
        }
        if (showSecondPoly) {
            drawPolygon(canvas, clipper, secondPolyPaint, secondPolyPointPaint);
        }
        if (showIntersection) {
            drawPolygon(canvas, result, intersectionPolyPaint, intersectionPolyPointPaint);
        }
    }

    private void drawPolygon(final Canvas canvas, List<double[]> points, Paint paint, final Paint pointPaint) {
        int len = points.size();
        for (int i = 0; i < len; i++) {
            double[] p1 = points.get(i);
            double[] p2 = points.get((i + 1) % len);
            canvas.drawLine((float) p1[0], (float) p1[1], (float) p2[0], (float) p2[1], paint);
            canvas.drawPoint((float) p1[0], (float) p1[1], pointPaint);
            canvas.drawPoint((float) p2[0], (float) p2[1], pointPaint);
        }
    }

}
