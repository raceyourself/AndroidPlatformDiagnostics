
package com.glassfitgames.glassfitplatformdemo;

import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.TextView;

public class TrackpadDiagnostics extends Activity implements GestureDetector.OnGestureListener {

    //private TextView tv = new TextView(getApplicationContext());
    GestureDetector gestureDetector;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orientation_diagnostics);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //tv = (TextView)findViewById(R.id.trackpadTextview);
        gestureDetector = new GestureDetector(this, this);
//
//        Log.i("Social", "Creating intent");
//        Intent sendIntent = new Intent();
//        sendIntent.setAction(Intent.ACTION_SEND);
//        sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
//        sendIntent.setType("text/plain");
//        Log.i("Social", "Starting intent");
//        startActivity(sendIntent);
//        Log.i("Social", "Success!");
        
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.trackpad_diagnostics, menu);
        return true;
    }
    
    @Override
    public boolean onGenericMotionEvent(MotionEvent event) {
        Log.i("TrackpadDiagnostics","Generic Event");
        gestureDetector.onTouchEvent(event);
       return true;
   }

    @Override
    public boolean onDown(MotionEvent arg0) {
        Log.i("TrackpadDiagnostics","Pressed down");
        //tv.setText("Pressed down");
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        Log.i("TrackpadDiagnostics","Fling, velocity " + velocityX + "," + velocityY + "pixels/sec");
        //tv.setText("Fling, velocity " + velocityX + "," + velocityY + "pixels/sec");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        Log.i("TrackpadDiagnostics","Long press");
        //tv.setText("Long press");
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        Log.i("TrackpadDiagnostics","Scroll: " + distanceX + "," + distanceY);
        //tv.setText("Scroll: " + distanceX + "," + distanceY);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        Log.i("TrackpadDiagnostics","Press and hold");
//        tv.setText("Press and hold");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        Log.i("TrackpadDiagnostics","Single tap up");
//        tv.setText("Single tap up");
        return false;
    }

}
