package com.idonthaveadomain.rcraspberrypicontroller;

import android.content.Context;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class FileIOHandler {

    public static final String SETTINGS_CONFIG="config.txt";

    public static void writeToFile(SettingsConfig settingsConfig, Context context) {
        Gson gson = new Gson();
        String data = gson.toJson(settingsConfig);
        try {
            OutputStreamWriter outputStreamWriter = new OutputStreamWriter(context.openFileOutput(SETTINGS_CONFIG, Context.MODE_PRIVATE));
            outputStreamWriter.write(data);
            outputStreamWriter.close();
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static SettingsConfig readFromFile(Context context) {

        String ret = "";
        SettingsConfig settingsConfig = new SettingsConfig();
        settingsConfig.setIpAddress("");
        try {
            InputStream inputStream = context.openFileInput(SETTINGS_CONFIG);

            if ( inputStream != null ) {
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
                String receiveString = "";
                StringBuilder stringBuilder = new StringBuilder();

                while ( (receiveString = bufferedReader.readLine()) != null ) {
                    stringBuilder.append("\n").append(receiveString);
                }

                inputStream.close();
                ret = stringBuilder.toString();

                Gson gson = new Gson();
                settingsConfig = gson.fromJson(ret, SettingsConfig.class);
            }
        }
        catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return settingsConfig;
    }
}
