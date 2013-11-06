
package com.glassfitgames.glassfitplatformdemo;

import java.text.DecimalFormat;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.glassfitgames.glassfitplatform.gpstracker.GPSTracker;
import com.glassfitgames.glassfitplatform.gpstracker.Helper;
import com.glassfitgames.glassfitplatform.gpstracker.TargetTracker;
import com.glassfitgames.glassfitplatform.gpstracker.TargetTracker.TargetSpeed;


/**
 * Demo use-case for the GPSTracker and TargetTracker classes in GlassFitPlatform. Understanding
 * this is a good place to start if you want to build games that rely on the GPS/target
 * functionality of the platform.
 * <p>
 * Displays simple buttons to initialize GPS/target trackers, start/stop tracking and display
 * current speed/distance/time metric of the device vs. the target.
 */
public class GpsTestActivity extends Activity {

    private GPSTracker gpsTracker;

    private TargetTracker targetTracker;

    private TextView testLocationText;

    private Button initGpsButton;

    private Button initFakeGpsButton;

    private Button startTrackingButton;

    private Button stopTrackingButton;

    private Button distanceButton;

    private Button resetButton;
    
    private Button syncButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.testgps);
        testLocationText = (TextView)findViewById(R.id.testLocationText);
        initGpsButton = (Button)findViewById(R.id.initGpsButton);
        initFakeGpsButton = (Button)findViewById(R.id.initFakeGpsButton);
        startTrackingButton = (Button)findViewById(R.id.startTrackingButton);
        stopTrackingButton = (Button)findViewById(R.id.stopTrackingButton);
        distanceButton = (Button)findViewById(R.id.DistanceButton);
        resetButton = (Button)findViewById(R.id.resetButton);
        syncButton = (Button)findViewById(R.id.SyncButton);

        initGpsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper helper = Helper.getInstance(getApplicationContext());
                gpsTracker = helper.getGPSTracker();
                targetTracker = helper.getTargetTracker();
                targetTracker.setSpeed(TargetSpeed.JOGGING);
            }
        });

        initFakeGpsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gpsTracker == null) {
                    testLocationText.setText("No GPS tracker object, please press init GPS");
                    return;
                }
                gpsTracker.setIndoorMode(!gpsTracker.isIndoorMode());
                gpsTracker.setIndoorSpeed(TargetSpeed.WALKING);
            }
        });

        startTrackingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gpsTracker == null) {
                    testLocationText.setText("No GPS tracker object, please press init GPS");
                    return;
                }
                
                if (!gpsTracker.hasPosition()) {
                    testLocationText.setText("GPS position not yet accurate enough, please wait");
                    return;
                }
                
                gpsTracker.startTracking();
            }
        });

        distanceButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (gpsTracker == null) {
                    testLocationText.setText("GPSTracker is null, please press init");
                    return;
                }

                if (!gpsTracker.hasPosition()) {
                    testLocationText.setText("GPS position not yet accurate enough to start tracking, please wait");
                    return;
                }
                
                if (targetTracker == null) {
                    testLocationText.setText("TargetTracker is null, please press init");
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
                        + "Target current speed = " + twoDp.format(targetTracker.getCurrentSpeed(gpsTracker.getElapsedTime())) + "m/s.\n"
                        + "GPS current speed = " + twoDp.format(gpsTracker.getCurrentSpeed()) + "m/s.\n\n"        
                        + "Smoothed bearing to target = " + bearing + ".\n"
                        + "GPS elapsed time = " + twoDp.format((double)gpsTracker.getElapsedTime()/1000.0) + "s.)\n";
                        
                        
                testLocationText.setText(text);
            }
        });

        stopTrackingButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                
                if (gpsTracker == null) {
                    testLocationText.setText("No GPS tracker object, please press init GPS");
                    return;
                }
                
                gpsTracker.stopTracking();
            }
        });

        resetButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (gpsTracker == null) {
                    testLocationText.setText("No GPS tracker object, please press init GPS");
                    return;
                }
                
                gpsTracker.reset();
            }
        });
        
        syncButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {                                
                Helper.syncToServer(getApplicationContext());
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send: http://www.glassfitgames.com/");
                sendIntent.setType("text/plain");
                startActivity(sendIntent);

            }
        });

    }
}
