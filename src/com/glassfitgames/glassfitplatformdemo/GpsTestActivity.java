package com.glassfitgames.glassfitplatformdemo;

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

public class GpsTestActivity extends Activity {

	private GPSTracker gpsTracker;

	private TargetTracker targetTracker;

	private TextView testLocationText;

	private Button initGpsButton;

	private Button initTargetButton;

	private Button startTrackingButton;

	private Button stopTrackingButton;

	private Button distanceButton;

	private Button syncButton;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testgps);
		testLocationText = (TextView) findViewById(R.id.testLocationText);
		initGpsButton = (Button) findViewById(R.id.initGpsButton);
		initTargetButton = (Button) findViewById(R.id.initTargetButton);
		startTrackingButton = (Button) findViewById(R.id.startTrackingButton);
		stopTrackingButton = (Button) findViewById(R.id.stopTrackingButton);
		distanceButton = (Button) findViewById(R.id.DistanceButton);
		syncButton = (Button) findViewById(R.id.SyncButton);

		initGpsButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				gpsTracker = Helper.getGPSTracker(getApplicationContext());
			}
		});

		initTargetButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				try {
					targetTracker = Helper.getTargetTracker("pb");
				} catch (Exception e) {
					Log.e("GlassFitPlatform", e.getMessage());
				}
			}
		});

		startTrackingButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
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
				long gpsDistance = gpsTracker.getElapsedDistance();
				long gpsTime = gpsTracker.getElapsedTime();
				long targetDistance = targetTracker
						.getCumulativeDistanceAtTime(gpsTime);

				String text = "Elapsed distance: " + gpsDistance
						+ ". Target distance = " + targetDistance;
				testLocationText.setText(text);
			}
		});

		syncButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				Helper.syncToServer(getApplicationContext());
			}
		});

	}

}
