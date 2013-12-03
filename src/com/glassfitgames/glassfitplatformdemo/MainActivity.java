
package com.glassfitgames.glassfitplatformdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.glassfitgames.glassfitplatform.auth.AuthenticationActivity;
import com.glassfitgames.glassfitplatform.models.Game;
import com.glassfitgames.glassfitplatform.models.UserDetail;
import com.roscopeco.ormdroid.ORMDroidApplication;

/**
 * Default activity for the Platform Demo.
 * <p>
 * Shows the user buttons to start each of the demos, for example the GPS and Authentication demos.
 * Will be expanded with more demos as the platform develops.
 */
public class MainActivity extends Activity {

    static final int API_ACCESS_TOKEN_REQUEST_ID = 0;
    
    private Button testAuthenticationButton;

    private Button testGpsButton;
    
    private Button testSensorButton;
    
    private Button trackpadDiagnosticsButton;
    
    private TextView mainTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        // SQLiteDatabase.deleteDatabase(new
        // File(ORMDroidApplication.getDefaultDatabase().getPath()));

        testAuthenticationButton = (Button)findViewById(R.id.testAuthenticationButton);
        testGpsButton = (Button)findViewById(R.id.testGpsButton);
        testSensorButton = (Button)findViewById(R.id.testSensorButton);
        trackpadDiagnosticsButton = (Button)findViewById(R.id.trackpadDiagnosticsButton);
        mainTextView = (TextView)findViewById(R.id.mainTextView);

        testAuthenticationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                startActivityForResult(intent, API_ACCESS_TOKEN_REQUEST_ID);
            }
        });

        testGpsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GpsTestActivity.class);
                startActivity(intent);
            }
        });
        
        testSensorButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), OrientationDiagnostics.class);
                startActivity(intent);
            }
        });
        
        trackpadDiagnosticsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), TrackpadDiagnostics.class);
                startActivity(intent);
            }
        });
        
        String deviceText = "";
        deviceText += "Manufacturer: " + android.os.Build.MANUFACTURER + "\n";
        deviceText += "Product: " + android.os.Build.PRODUCT + "\n";
        deviceText += "Model: " + android.os.Build.MODEL + "\n";
        
        int rotation = ((WindowManager)getSystemService(WINDOW_SERVICE)).getDefaultDisplay().getRotation();
        switch (rotation) {
            case Surface.ROTATION_0: deviceText += "Orientation: 0 degrees\n"; break;
            case Surface.ROTATION_90: deviceText += "Orientation: +90 degrees\n"; break;
            case Surface.ROTATION_180: deviceText += "Orientation: +180 degrees\n"; break;
            case Surface.ROTATION_270: deviceText += "Orientation: +270 degrees\n"; break;
            default: deviceText += "Orientation: unknown!\n"; break;
        }
        
        ORMDroidApplication.initialize(getApplicationContext());
        //deviceText += "Tracks on device: " + Entity.query(Track.class).count("*").executeAggregate() + "\n";
        //deviceText += "Positions on device: " + Entity.query(Position.class).count("*").executeAggregate() + "\n";
        //deviceText += "Transactions on device: " + Entity.query(Transaction.class).count("*").executeAggregate() + "\n";
        
        mainTextView.setText(deviceText);
        
        Game.getGames(getApplicationContext());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) { 
        // This method doesn't seem to get called after the authentication activity
        // Ideally it would!
        Log.d("GlassFitPlatformDemo","Activity returned with result");
        super.onActivityResult(requestCode, resultCode, data); 
        switch(requestCode) { 
          case (API_ACCESS_TOKEN_REQUEST_ID) : { 
            Log.d("GlassFitPlatformDemo","AuthenticationActivity returned with result");
            if (resultCode == Activity.RESULT_OK) { 
            String apiAccessToken = data.getStringExtra(AuthenticationActivity.API_ACCESS_TOKEN);
            Log.d("GlassFitPlatformDemo","AuthenticationActivity returned with token: " + apiAccessToken);
            // display apiAccessToken to user
            String text;
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;

            // token direct from authenticationActivity
            if (apiAccessToken != null) {
                text = "Success! API access token: " + apiAccessToken;
            } else {
                text = "Failure! Couldn't authenticate. ";
            }
            
            // token from database
            String dbApiAccessToken = UserDetail.get().getApiAccessToken();
            if (dbApiAccessToken != null) {
                text += "\nToken from DB: " + dbApiAccessToken;
                Log.d("GlassFitPlatformDemo","Database returned auth token: " + dbApiAccessToken);
            } else {
                text += "\nCouldn't retrieve token from DB.";
            }
            
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            } 
            break; 
          } 
        } 
      }
    

}