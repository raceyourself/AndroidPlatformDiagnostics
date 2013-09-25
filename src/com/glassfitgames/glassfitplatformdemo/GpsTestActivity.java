
package com.glassfitgames.glassfitplatformdemo;

import java.text.DecimalFormat;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.glassfitgames.glassfitplatform.gpstracker.GPSTracker;
import com.glassfitgames.glassfitplatform.gpstracker.TargetTracker;
import com.glassfitgames.glassfitplatform.gpstracker.Helper;
import com.glassfitgames.glassfitplatform.gpstracker.TargetTracker.TargetSpeed;

public class GpsTestActivity extends Activity {

    private GPSTracker gpsTracker;

    private TargetTracker targetTracker;

    private TextView testLocationText;

    private Button initGpsButton;

    private Button initFakeGpsButton;

    private Button initTargetButton;

    private Button startTrackingButton;

    private Button stopTrackingButton;

    private Button distanceButton;

    private Button syncButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testgps);
        testLocationText = (TextView)findViewById(R.id.testLocationText);
        initGpsButton = (Button)findViewById(R.id.initGpsButton);
        initFakeGpsButton = (Button)findViewById(R.id.initFakeGpsButton);
        initTargetButton = (Button)findViewById(R.id.initTargetButton);
        startTrackingButton = (Button)findViewById(R.id.startTrackingButton);
        stopTrackingButton = (Button)findViewById(R.id.stopTrackingButton);
        distanceButton = (Button)findViewById(R.id.DistanceButton);
        syncButton = (Button)findViewById(R.id.SyncButton);

        initGpsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsTracker = Helper.getGPSTracker(getApplicationContext());
            }
        });

        initFakeGpsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsTracker = Helper.getGPSTracker(getApplicationContext());
                gpsTracker.setIndoorMode(true);
                gpsTracker.setIndoorSpeed(TargetSpeed.WALKING);
            }
        });

        initTargetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    targetTracker = Helper.getTargetTracker("WALKING");
                } catch (Exception e) {
                    Log.e("GlassFitPlatform", e.getMessage());
                }
            }
        });

        startTrackingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gpsTracker == null) {
                    gpsTracker = Helper.getGPSTracker(getApplicationContext());

                } else {

                }
                gpsTracker.startTracking();
            }
        });

        stopTrackingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                gpsTracker.stopTracking();
            }
        });

        distanceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!gpsTracker.hasPosition()) {
                    testLocationText.setText("GPS position not yet accurate enough, please wait");
                    return;
                }

                DecimalFormat zeroDp = new DecimalFormat("#");
                DecimalFormat twoDp = new DecimalFormat("#.##");
                double gpsDistance = gpsTracker.getElapsedDistance();
                long gpsTime = gpsTracker.getElapsedTime();
                double targetDistance = targetTracker.getCumulativeDistanceAtTime(gpsTime);
                String bearing = gpsTracker.hasBearing() ? zeroDp.format(gpsTracker.getCurrentBearing()) + " degrees" : "unknown, please move in a straight line";

                String text = "Target elapsed distance = " + twoDp.format(targetDistance) + "m.\n"
                        + "GPS elapsed distance = " + twoDp.format(gpsDistance) + "m (\u00b1" + zeroDp.format(gpsTracker.getCurrentPosition().getEpe()) + "m.)\n"
                        + "Distance to avatar = " + twoDp.format(targetDistance - gpsDistance) + "m.\n\n"
                        + "Target current speed = " + twoDp.format(targetTracker.getCurrentSpeed(gpsTracker.getElapsedTime())) + "m/s."
                        + "GPS current speed = " + twoDp.format(gpsTracker.getCurrentSpeed()) + "m/s.\n"        
                        + "Smoothed bearing to target = " + bearing + ".\n";
                        
                        
                testLocationText.setText(text);
            }
        });

        syncButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Helper.syncToServer(getApplicationContext());
            }
        });

    }
}
