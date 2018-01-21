package com.gitintheshell.geometrics;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.gitintheshell.geometrics.clipping.PolygonClippingView;

public class PolygonClipping extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_polygon_clipping);

        PolygonClippingView pCV = (PolygonClippingView) findViewById(R.id.frame);

        CheckBox showFirst = (CheckBox) findViewById(R.id.show_first_poly);

        showFirst.setOnCheckedChangeListener((buttonView, isChecked) -> pCV.setShowFirstPoly(isChecked));

        CheckBox showSecond = (CheckBox) findViewById(R.id.show_second_poly);
        showSecond.setOnCheckedChangeListener((buttonView, isChecked) -> pCV.setShowSecondPoly(isChecked));
        CheckBox showIntersection = (CheckBox) findViewById(R.id.show_intersection);
        showIntersection.setOnCheckedChangeListener((buttonView, isChecked) -> pCV.setShowIntersection(isChecked));

    }
}
