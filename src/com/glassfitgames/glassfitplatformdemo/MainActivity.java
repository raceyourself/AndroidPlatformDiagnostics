
package com.glassfitgames.glassfitplatformdemo;

import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.location.Location;
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
import com.glassfitgames.glassfitplatform.gpstracker.Helper;
import com.glassfitgames.glassfitplatform.gpstracker.SyncHelper;
import com.glassfitgames.glassfitplatform.models.Device;
import com.glassfitgames.glassfitplatform.models.Friend;
import com.glassfitgames.glassfitplatform.models.Game;
import com.glassfitgames.glassfitplatform.models.Position;
import com.glassfitgames.glassfitplatform.models.Track;
import com.glassfitgames.glassfitplatform.models.Transaction;
import com.glassfitgames.glassfitplatform.models.Transaction.InsufficientFundsException;
import com.glassfitgames.glassfitplatform.models.UserDetail;
import com.glassfitgames.glassfitplatform.points.PointsHelper;
import com.glassfitgames.glassfitplatform.sensors.GestureHelper;
import com.roscopeco.ormdroid.Entity;
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
    private Button testSyncButton;
    private Button lifeFitnessButton;
    
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
        testSyncButton = (Button)findViewById(R.id.testSyncButton);
        lifeFitnessButton = (Button)findViewById(R.id.lifeFitnessDiagnosticsButton);
        mainTextView = (TextView)findViewById(R.id.mainTextView);

        testAuthenticationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                //startActivityForResult(intent, API_ACCESS_TOKEN_REQUEST_ID);
                //Helper.getInstance(getApplicationContext()).authorize(MainActivity.this, "any", "login");
                
                // test setup
                Track track = new Track(UserDetail.get().getGuid(), "Profile");
                Location location = new Location("");
                Position ps = new Position(track, location);
                Device d = Device.self();
                Friend f = new Friend();
                
                ORMDroidApplication.getInstance().beginTransaction();
                ORMDroidApplication.getInstance().beginTransaction();
                d.save();
                f.save();
                ps.save();
                ps.flush();
                track.save();
                track.flush();
                ORMDroidApplication.getInstance().setTransactionSuccessful();
                ORMDroidApplication.getInstance().endTransaction();
                ORMDroidApplication.getInstance().setTransactionSuccessful();
                ORMDroidApplication.getInstance().endTransaction();
                
                
                // test individual inserts
                long starttime = System.currentTimeMillis();
                for (int i=0; i<1000; i++) {
                    Position p = new Position(track, location);
                    p.save();
                }
                Log.i("ORM","Time for 1000 individual position inserts is " + (System.currentTimeMillis()-starttime)/1000.0f + " seconds.");
                
                // test query
                //ORMDroidApplication.getInstance().clearCache();
                starttime = System.currentTimeMillis();
                List<Position> positions = track.getTrackPositions();
                Log.i("ORM","Time to query " + positions.size() + " inserted positions is " + (System.currentTimeMillis()-starttime)/1000.0f + " seconds.");
                
                // test individual deletes
                starttime = System.currentTimeMillis();
                for(Position p : positions) {
                    p.delete();
                    p.flush();
                }
                Log.i("ORM","Time for 1000 individual position deletes is " + (System.currentTimeMillis()-starttime)/1000.0f + " seconds.");
                
                // test inserts in transaction
                starttime = System.currentTimeMillis();
                ORMDroidApplication.getInstance().beginTransaction();
                for (int i=0; i<1000; i++) {
                    Position p = new Position(track, location);
                    p.save();
                }
                ORMDroidApplication.getInstance().setTransactionSuccessful();
                ORMDroidApplication.getInstance().endTransaction();
                Log.i("ORM","Time for 1000 position inserts in a single transaction is " + (System.currentTimeMillis()-starttime)/1000.0f + " seconds.");
                
                // test query
                //ORMDroidApplication.getInstance().clearCache();
                starttime = System.currentTimeMillis();
                positions = track.getTrackPositions();
                Log.i("ORM","Time to query " + positions.size() + " inserted positions is " + (System.currentTimeMillis()-starttime)/1000.0f + " seconds.");
                
                // test deletes in transaction
                starttime = System.currentTimeMillis();
                ORMDroidApplication.getInstance().beginTransaction();
                for(Position p : positions) {
                    p.delete();
                    p.flush();
                }
                ORMDroidApplication.getInstance().setTransactionSuccessful();
                ORMDroidApplication.getInstance().endTransaction();
                Log.i("ORM","Time for 1000 position deletes in a single transaction is " + (System.currentTimeMillis()-starttime)/1000.0f + " seconds.");

            }
        });

        testGpsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, GpsTestActivity.class);
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
                Intent intent = new Intent(getApplicationContext(), GestureHelper.class);
                startActivity(intent);
            }
        });
        
        testSyncButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Helper.getInstance(getApplicationContext()).authorize(MainActivity.this, "any", "login");
                Helper.syncToServer(MainActivity.this);
                Helper.getFriends();
            }
        });
        
        lifeFitnessButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LifeFitnessDiagnostics.class);
                startActivity(intent);
            }
        });
        
    }
    
    @Override
    public void onResume() {    
        
        super.onResume();
        
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
        
        // test database
        ORMDroidApplication.initialize(getApplicationContext());
//        deviceText += "Tracks on device: " + Entity.query(Track.class).executeMulti().size() + "\n";
//        deviceText += "Positions on device: " + Entity.query(Position.class).executeMulti().size() + "\n";
//        deviceText += "Transactions on device: " + Entity.query(Transaction.class).executeMulti().size() + "\n";
        
        // try 10 concurrent writes to check database locking
//        final Track t = new Track(1, "thread-test");
//        t.save();
//        for (int i=0; i<100; i++) {
//            new Thread() {
//                public void run() {
//                    (new Position(t, new Location(""))).save();
//                }
//            }.run();
//        }
//        
        // test game loading
        //List<Game> games = Game.getGames(getApplicationContext());
        //deviceText += "Games loaded: " + games.size() + "\n";
        
        // test points system:
//        PointsHelper p = PointsHelper.getInstance(getApplicationContext());
//        try {
//            Log.i("PlatformDemo.MainActivity", "Trying to award points, gems and metabolism..");
//            p.awardPoints("test", "hard-coded", "PlatformDemo.MainActivity", 0);
//            p.awardGems("test", "hard-coded", "PlatformDemo.MainActivity", 0);
//            p.awardMetabolism("test", "hard-coded", "PlatformDemo.MainActivity", 0.0f);
//            Log.i("PlatformDemo.MainActivity", "..funds awarded successfully.");
//        } catch (InsufficientFundsException e) {
//            Log.e("PlatformDemo.MainActivity", "InsufficientFunds");
//        }
        
//        Log.i("PlatformDemo.MainActivity", "Opening points: " + p.getOpeningPointsBalance());
//        Log.i("PlatformDemo.MainActivity", "Current-game points: " + p.getCurrentActivityPoints());
//        Log.i("PlatformDemo.MainActivity", "Total gems: " + p.getCurrentGemBalance());
//        Log.i("PlatformDemo.MainActivity", "Current metabolism: " + p.getCurrentMetabolism());
//        
//        deviceText += "Points: " + (p.getOpeningPointsBalance() + p.getCurrentActivityPoints()) + "\n";
//        deviceText += "Gems: " + p.getCurrentGemBalance() + "\n";
//        deviceText += "Metabolism: " + p.getCurrentMetabolism() + "\n";
        mainTextView.setText(deviceText);

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