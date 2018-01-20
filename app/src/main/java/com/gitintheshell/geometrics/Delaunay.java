package com.gitintheshell.geometrics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

import com.gitintheshell.geometrics.delaunay.DelaunayView;

public class Delaunay extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delaunay);
        final DelaunayView dV = (DelaunayView) findViewById(R.id.frame);
        Button setDelaunay = (Button) findViewById(R.id.delaunay);
        setDelaunay.setOnClickListener(v -> dV.setDelaunayMode(true));
        Button setVoronoi = (Button) findViewById(R.id.voronoi);
        setVoronoi.setOnClickListener(v -> dV.setDelaunayMode(false));
    }



}
