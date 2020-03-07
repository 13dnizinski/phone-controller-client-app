package com.idonthaveadomain.rcraspberrypicontroller;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    Button buttonStart, buttonStop;
    SeekBar seekBar;
    TextView textViewProgress;

    RCClient rcClient;
    int pulseWidth = 1400;

    Thread thread = new Thread(new Runnable() {
       @Override
       public void run() {
           try {
               rcClient = new RCClient();
               int oldPW = 0;
               while(true) {
                   if(pulseWidth == 1100) {
                       break;
                   }
                   if(oldPW != pulseWidth) {
                       rcClient.turn(pulseWidth);
                       oldPW = pulseWidth;
                   }
               }
               rcClient.closeUp();
           } catch(Exception e) {
               e.printStackTrace();
           }
       }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulseWidth = 1400;
                thread.start();
                buttonStart.setEnabled(false);
            }
        });

        buttonStop = (Button) findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulseWidth = 1100;
                buttonStop.setEnabled(false);
            }
        });

        textViewProgress = (TextView) findViewById(R.id.textView3);

        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                pulseWidth = 1130+i;
                textViewProgress.setText(""+pulseWidth);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                System.out.println("Start tracking touch");
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                System.out.println("Stop tracking touch");
            }
        });
    }

    @Override
    protected void onDestroy() {
        pulseWidth = 1100;
        super.onDestroy();
    }
}
