package com.glassfitgames.glassfitplatformdemo;

import java.io.File;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

import com.glassfitgames.glassfitplatform.gpstracker.Helper;
import com.glassfitgames.glassfitplatform.models.Position;
import com.roscopeco.ormdroid.ORMDroidApplication;

public class GpsTestActivity extends Activity {

	private Helper gpsHelper;
	private TextView testLocationText;
	private Button initGpsButton;
	private Button startTrackingButton;
	private Button stopTrackingButton;
	private Button getCurrentPositionButton;
	private Button distanceButton;
	private boolean targetset = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testgps);
		testLocationText = (TextView) findViewById(R.id.testLocationText);
		initGpsButton = (Button) findViewById(R.id.initGpsButton);
		startTrackingButton = (Button) findViewById(R.id.startTrackingButton);
		stopTrackingButton = (Button) findViewById(R.id.stopTrackingButton);
		getCurrentPositionButton = (Button) findViewById(R.id.getCurrentPositionButton);
		distanceButton = (Button) findViewById(R.id.DistanceButton);

		gpsHelper = new Helper(getApplicationContext());

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
				Position pos;
				try {
					pos = gpsHelper.getCurrentPosition();
					String lat = Double.toString(pos.latx);
					String lon = Double.toString(pos.lngx);
					String text = "Current Latitude is : " + lat
							+ " and Current Longtitude is: " + lon;
					testLocationText.setText(text);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});

		distanceButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (!targetset) {
					gpsHelper.setTargetTrack(0);
					targetset = true;
				}
				long distance = gpsHelper.getTargetElapsedDistance();
				String text = "Elapsed distance: " + distance;
				testLocationText.setText(text);
			}
		});

	}

}
