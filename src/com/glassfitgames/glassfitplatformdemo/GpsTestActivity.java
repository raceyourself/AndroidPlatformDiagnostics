package com.glassfitgames.glassfitplatformdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.glassfitgames.glassfitplatform.auth.AuthenticationActivity;
import com.glassfitgames.glassfitplatform.gpstracker.Helper;
import com.glassfitgames.glassfitplatform.models.Position;

public class GpsTestActivity extends Activity {

    private Helper gpsHelper;
    private TextView testLocationText;
    private Button initGpsButton;
    private Button startTrackingButton;
    private Button stopTrackingButton;
    private Button getCurrentPositionButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testgps);
        testLocationText = (TextView) findViewById(R.id.testLocationText);
        initGpsButton = (Button) findViewById(R.id.initGpsButton);
        startTrackingButton = (Button) findViewById(R.id.startTrackingButton);
        stopTrackingButton = (Button) findViewById(R.id.stopTrackingButton);
        getCurrentPositionButton = (Button) findViewById(R.id.getCurrentPositionButton);

        gpsHelper = new Helper();
        
        initGpsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsHelper.initGps();
            }
        });
        
        startTrackingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsHelper.startTracking();
            }
        });

        stopTrackingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsHelper.stopTracking();
            }
        });

        getCurrentPositionButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Position pos = gpsHelper.getCurrentPosition();
                String lat = Float.toString(pos.latx);
                String lon = Float.toString(pos.lngx);
                String text = "Current Latitude is : " + lat + " and Current Longtitude is: " + lon;
                testLocationText.setText(text);
            }
        });

    }

}
