package com.gitintheshell.geometrics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.start_triangulation).setOnClickListener(v -> Delaunay.start(this));
        findViewById(R.id.start_convex).setOnClickListener(v -> ConvexHullActivity.start(this));
        findViewById(R.id.start_polygon).setOnClickListener(v -> PolygonClipping.start(this));

    }
}
