package com.mdelbel.android.bottomsheettest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.mdelbel.android.bottomsheettest.map.MapView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MapView mapView = findViewById(R.id.map);
        mapView.initMap(getSupportFragmentManager());
    }
}