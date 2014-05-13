package com.glassfitgames.glassfitplatformdemo;

import com.glassfitgames.glassfitplatform.BLE.BluetoothLeHelper;
import com.glassfitgames.glassfitplatform.BLE.BluetoothLeListener;
import com.glassfitgames.glassfitplatform.BLE.SampleGattAttributes;

import android.app.Activity;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

/**
 * Searches for BLE fitness devices and displays data from them
 */
public class FitnessSensorActivity extends Activity implements BluetoothLeListener {
    
    private TextView textview;
    private BluetoothLeHelper helper;
    
    private String heartrateText = "Heartrate: unknown";
    private String cadenceText = "Cadence: unknown";
    private String wheelSpeedText = "Wheel speed: unknown";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        setContentView(R.layout.activity_fitness_sensor);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        
        textview = (TextView)findViewById(R.id.fitness_text_view);
        helper = new BluetoothLeHelper(this);
    }
    
    @Override
    public void onResume() {
        super.onResume();
        helper.registerListener(this);
        helper.startListening();
        setText("Scanning for BLE devices...");
    }
    
    @Override
    public void onPause() {
        super.onPause();
        helper.stopListening();
        helper.unregisterListener(this);
        setText("");
    }
    
    private void update() {
        setText(heartrateText + "\n" + cadenceText + "\n" + wheelSpeedText);
    }
    
    private void setText(String text) {
        final String text1 = text;
        this.runOnUiThread(new Runnable() {
            public void run() {
                textview.setText(text1);
            }
        });
    }
    
    @Override
    public void characteristicDetected(BluetoothGattCharacteristic characteristic) {
        setText("Characteristic detected: " + SampleGattAttributes.lookup(characteristic.getUuid(), "unknown"));
    }

    @Override
    public void onNewHeartrateData(int heartRateBpm) {
        heartrateText = "Heartrate: " + heartRateBpm + "bpm";
        update();
    }

    @Override
    public void onNewCadenceData(float cadenceRpm) {
        cadenceText = "Cadence: " + (int)cadenceRpm + "rpm";
        update();
    }

    @Override
    public void onNewWheelSpeedData(float wheelSpeedRpm) {
        wheelSpeedText = "Wheel Speed: " + (int)wheelSpeedRpm + "rpm";
        update();
    }
    
}
