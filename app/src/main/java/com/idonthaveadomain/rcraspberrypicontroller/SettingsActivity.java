package com.idonthaveadomain.rcraspberrypicontroller;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class SettingsActivity extends AppCompatActivity {
    Button okButton;
    FileIOHandler fileIOHandler;
    EditText ipAddressTextbox;
    SettingsConfig settingsConfig;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        fileIOHandler = new FileIOHandler();
        ipAddressTextbox = (EditText) findViewById(R.id.ipAddressValue);
        setUpSettingsConfig();
        ipAddressTextbox.setText(settingsConfig.getIpAddress(), TextView.BufferType.EDITABLE);

        okButton = (Button) findViewById(R.id.okButton);
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO: Find a way to persist the IP address so I don't need to enter it each time. Maybe read it from a file?
                settingsConfig.setIpAddress(ipAddressTextbox.getText().toString());
                RCClient rcClient = RCClient.getInstance(getApplicationContext());
                rcClient.setUpConnectionToIP(ipAddressTextbox.getText().toString());
                fileIOHandler.writeToFile(settingsConfig, getApplicationContext());
                openMainActivity();
            }
        });

    }

    private void setUpSettingsConfig() {
        try {
            settingsConfig = fileIOHandler.readFromFile(getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
            settingsConfig = new SettingsConfig();
            settingsConfig.setIpAddress("123.456.789");
        }
    }

    public void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
