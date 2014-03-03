package com.glassfitgames.glassfitplatformdemo;

import java.util.Timer;
import java.util.TimerTask;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;
import android.widget.TextView;
import com.glassfitgames.glassfitplatform.gpstracker.LifeFitnessTracker;

/**
 * Class to connect to Life Fitness consoles and log diagnostic information
 * The Life Fitness API docs are here: https://software.lfconnect.com/lfopen/1.1.1/androidapidocs/index.html
 * @author benlister
 *
 */
public class LifeFitnessDiagnostics extends Activity {
    
    private TextView textView;
    private Timer screenTimer;
    private ScreenTask screenTask;
    
    private LifeFitnessTracker lfTracker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        Log.d(this.getClass().getSimpleName(), "onCreate() called");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_life_fitness_diagnostics);
        textView = (TextView) findViewById(R.id.lifeFitnessDiagnosticsTextView);

        lfTracker = LifeFitnessTracker.getInstance();
        lfTracker.init(getApplicationContext());
        //lfTracker.connect();
        lfTracker.startNewTrack();
        lfTracker.startTracking();

        // start updating the real-time workout info on screen
        screenTimer = new Timer();
        screenTask = new ScreenTask();
        screenTimer.scheduleAtFixedRate(screenTask, 0, 100);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.life_fitness_diagnostics, menu);
        return true;
    }
    
    public void onPause() {
        Log.d(this.getClass().getSimpleName(), "onPause() called");
        super.onPause();
        //lfTracker.disconnect();
        
    }
    
    public void onResume() {
        Log.d(this.getClass().getSimpleName(), "onResume() called");
        super.onResume();
        lfTracker.bind();
    }
    
    private class ScreenTask extends TimerTask {
        public void run() {
            runOnUiThread(new Runnable() {
                public void run() {
                    textView.setText(((LifeFitnessTracker)lfTracker).getScreenLog());
                }
            });
        }
    }
}
