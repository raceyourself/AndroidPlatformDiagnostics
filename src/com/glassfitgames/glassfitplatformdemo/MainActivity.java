
package com.glassfitgames.glassfitplatformdemo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

import com.glassfitgames.glassfitplatform.auth.AuthenticationActivity;
import com.glassfitgames.glassfitplatform.models.UserDetail;
import com.glassfitgames.glassfitplatform.utils.Utils;
import com.roscopeco.ormdroid.ORMDroidApplication;

/**
 * Default activity for the Platform Demo.
 * <p>
 * Shows the user buttons to start each of the demos, for example the GPS and Authentication demos.
 * Will be expanded with more demos as the platform develops.
 */
public class MainActivity extends Activity  {

    static final int API_ACCESS_TOKEN_REQUEST_ID = 0;
    
    private Button testAuthenticationButton;
    private Button testGpsButton;
    private Button testSensorButton;
    private Button resetDbButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        testAuthenticationButton = (Button)findViewById(R.id.testAuthenticationButton);
        testGpsButton = (Button)findViewById(R.id.testGpsButton);
        testSensorButton = (Button)findViewById(R.id.testSensorButton);
        resetDbButton = (Button)findViewById(R.id.resetDbButton);

        testAuthenticationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                intent.putExtra("provider", "any");
                intent.putExtra("permissions", "read,write");
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
                new Thread(new Runnable() {
                    public void run(){
                            long time = System.currentTimeMillis();
                            Encoder enc = new Encoder();
                            enc.testEncodeVideoToMp4();
                            Log.w("HelperDebug", "It took " + (System.currentTimeMillis()-time)/1000 + " seconds to test encode");
                            
                    }
                }).start();
            }
        });

        resetDbButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	ORMDroidApplication.initialize(getApplicationContext());
                ORMDroidApplication.getSingleton().resetDatabase();
        		Editor editor = getApplicationContext().getSharedPreferences(Utils.SYNC_PREFERENCES,
        				Context.MODE_PRIVATE).edit();
        		editor.clear();
        		editor.commit();
           }
        });

    }
    
    @Override
    public void onBackPressed() {
        System.exit(0);
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
            String text;
            Context context = getApplicationContext();
            int duration = Toast.LENGTH_SHORT;
            if (resultCode == Activity.RESULT_OK) { 
	            String apiAccessToken = data.getStringExtra(AuthenticationActivity.API_ACCESS_TOKEN);
	            Log.d("GlassFitPlatformDemo","AuthenticationActivity returned with token: " + apiAccessToken);
	            // display apiAccessToken to user
	
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
	            
            } else {
            	text = "Authentication interrupted: " + resultCode;
            }
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            break; 
          } 
        } 
      }
    

}
