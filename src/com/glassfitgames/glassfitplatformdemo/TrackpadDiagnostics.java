
package com.glassfitgames.glassfitplatformdemo;

import android.R;
import android.os.Bundle;
import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.TextView;

public class TrackpadDiagnostics extends Activity implements GestureDetector.OnGestureListener {

    private TextView tv;
    GestureDetector gestureDetector;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_trackpad_diagnostics);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        //tv = (TextView)findViewById(R.id.trackpadTextview);
        gestureDetector = new GestureDetector(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.trackpad_diagnostics, menu);
        return true;
    }

    @Override
    public boolean onDown(MotionEvent arg0) {
        tv.setText("Pressed down");
        return true;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        tv.setText("Fling, velocity " + velocityX + "," + velocityY + "pixels/sec");
        return true;
    }

    @Override
    public void onLongPress(MotionEvent e) {
        tv.setText("Long press");
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        tv.setText("Scroll: " + distanceX + "," + distanceY);
        return true;
    }

    @Override
    public void onShowPress(MotionEvent e) {
        tv.setText("Press and hold");
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        // TODO Auto-generated method stub
        return false;
    }

}
