
package com.glassfitgames.glassfitplatformdemo;

import com.glassfitgames.glassfitplatform.auth.AuthenticationActivity;
import com.glassfitgames.glassfitplatform.auth.Helper;
import com.glassfitgames.glassfitplatform.models.UserDetail;

import android.os.Bundle;
import android.accounts.NetworkErrorException;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {

    private Button testAuthenticationButton;

    private Button testGpsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main1);

        testAuthenticationButton = (Button)findViewById(R.id.testAuthenticationButton);
        testGpsButton = (Button)findViewById(R.id.testGpsButton);

        testAuthenticationButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), AuthenticationActivity.class);
                startActivity(intent);
            }
        });

        testGpsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), GpsTestActivity.class);
                startActivity(intent);
            }
        });

    }

    private void authenticate() {

        String apiAccessToken = null;

        try {
            Helper authHelper = new Helper();
            authHelper.authenticate(this);
            apiAccessToken = UserDetail.get().getApiAccessToken();
        } catch (NetworkErrorException e) {

        }

        // display success/failure message to user
        CharSequence text;
        Context context = getApplicationContext();
        int duration = Toast.LENGTH_SHORT;

        if (apiAccessToken != null) {
            text = "Success! API access token: " + apiAccessToken;
        } else {
            text = "Failure! Couldn't authenticate";
        }

        Toast toast = Toast.makeText(context, text, duration);
        toast.show();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

}
