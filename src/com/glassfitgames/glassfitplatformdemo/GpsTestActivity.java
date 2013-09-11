package com.glassfitgames.glassfitplatformdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

import com.glassfitgames.glassfitplatform.gpstracker.Helper;
import com.glassfitgames.glassfitplatform.models.Position;

public class GpsTestActivity extends Activity {

	private TextView testLocationText;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.testgps);
		testLocationText = (TextView) findViewById(R.id.testLocationText);
		Helper gpsHelper = new Helper();
		Position pos = new Position();
		pos = gpsHelper.getCurrentPosition(GpsTestActivity.this);
		String lat = Float.toString(pos.latx);
		String lon = Float.toString(pos.lngx);
		String text = "Current Latitude is : " + lat
				+ " and Current Longtitude is: " + lon;
		testLocationText.setText(text);

	}
}
