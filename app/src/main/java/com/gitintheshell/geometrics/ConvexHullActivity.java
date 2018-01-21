package com.gitintheshell.geometrics;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.gitintheshell.geometrics.convexhull.ConvexHullView;
import com.gitintheshell.geometrics.delaunay.DelaunayView;

public class ConvexHullActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_convex_hull);

        final ConvexHullView cV = (ConvexHullView) findViewById(R.id.frame);
    }

    public static void start(Context context) {
        Intent starter = new Intent(context, ConvexHullActivity.class);
        context.startActivity(starter);
    }

}
