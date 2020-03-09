package com.idonthaveadomain.rcraspberrypicontroller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import io.github.controlwear.virtual.joystick.android.JoystickView;

public class MainActivity extends AppCompatActivity {

    Button buttonStart, buttonStop, settingsButton, rotateHeadLeft, rotateHeadRight;
    SeekBar seekBar;
    JoystickView bodyMovementJoystickView, headMovementJoystickView;

    Button rotateBodyLeft, rotateBodyRight;

    RCClient rcClient;
    int pulseWidth = 1400;


    double bodyMovementAngle;
    double bodyMovementSpeed;
    double bodyRotation;
    double headMovementAngle;
    double headMovementSpeed;
    double headRotationAngle;

    double oldBodyMovementAngle;
    double oldBodyMovementSpeed;
    double oldBodyRotation;
    double oldHeadMovementAngle;
    double oldHeadMovementSpeed;
    double oldHeadRotationAngle;

    boolean isServerCommunicating = false;

    Thread thread = new Thread(new Runnable() {
       @Override
       public void run() {
           try {
               rcClient = RCClient.getInstance(getApplicationContext());
           } catch (Exception e) {
               e.printStackTrace();
           }
               int oldPW = 0;

               while(isServerCommunicating) {
                   try {
                       //rcClient = RCClient.getInstance(getApplicationContext());

                       if (pulseWidth == 1100) {
                           break;
                       }

                       //TODO: Delete this
                       if (oldPW != pulseWidth) {
                           rcClient.turn(pulseWidth);
                           oldPW = pulseWidth;
                       }

                       if (hasNewBodyMovementAngle()) {
                           rcClient.emitBodyMovementAngle(bodyMovementAngle);
                           oldBodyMovementAngle = bodyMovementAngle;
                       }

                       if (hasNewBodyMovementSpeed()) {
                           rcClient.emitBodyMovementSpeed(bodyMovementSpeed);
                           oldBodyMovementSpeed = bodyMovementSpeed;
                       }

                       if (hasNewBodyRotation()) {
                           rcClient.emitBodyRotation(bodyRotation);
                           oldBodyRotation = bodyRotation;
                       }

                       if (hasNewHeadMovementAngle()) {
                           rcClient.emitHeadMovementAngle(headMovementAngle);
                           oldHeadMovementAngle = headMovementAngle;
                       }

                       if (hasNewHeadMovementSpeed()) {
                           rcClient.emitHeadMovementSpeed(headMovementSpeed);
                           oldHeadMovementSpeed = headMovementSpeed;
                       }

                       if (hasNewHeadRotationAngle()) {
                           rcClient.emitHeadRotationAngle(headRotationAngle);
                           oldHeadRotationAngle = headRotationAngle;
                       }
                   } catch (Exception e) {
                       e.printStackTrace();
                   }
               }
               try {
                   rcClient.closeUp();
               } catch (Exception e) {
                   e.printStackTrace();
               }
       }
    });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Starting thread...");
        isServerCommunicating = true;
        thread.start();

        //TODO: Delete this:
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                pulseWidth = 1130+i;
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

        //TODO: Delete this:
        buttonStart = (Button) findViewById(R.id.buttonStart);
        buttonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulseWidth = 1400;
                thread.start();
                buttonStart.setEnabled(false);
            }
        });

        //TODO: Delete this:
        buttonStop = (Button) findViewById(R.id.buttonStop);
        buttonStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pulseWidth = 1100;
                buttonStop.setEnabled(false);
            }
        });

        settingsButton = (Button) findViewById(R.id.settingsButton);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isServerCommunicating = false;
                openSettingsActivity();
            }
        });

        rotateBodyLeft = (Button) findViewById(R.id.rotateBodyLeft);
        rotateBodyLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent mEvent) {
                System.out.println("Rotating body left while button is pressed");
                bodyRotation = -1.0;

                if(mEvent.getAction() == MotionEvent.ACTION_UP) {
                    bodyRotation = 0.0;
                    System.out.println("Stop rotating left because stopped pressing button");
                }

                return false;
            }
        });

        rotateBodyRight = (Button) findViewById(R.id.rotateBodyRight);
        rotateBodyRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent mEvent) {
                System.out.println("Rotating body right while button is pressed");
                bodyRotation = 1.0;

                if(mEvent.getAction() == MotionEvent.ACTION_UP) {
                    bodyRotation = 0.0;
                    System.out.println("Stop rotating right because stopped pressing button");
                }

                return false;
            }
        });

        rotateHeadLeft = (Button) findViewById(R.id.rotateHeadLeft);
        rotateHeadLeft.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent mEvent) {
                System.out.println("Rotating head left while button is pressed");
                headRotationAngle -= 1.0;
                return false;
            }
        });

        rotateHeadRight = (Button) findViewById(R.id.rotateHeadRight);
        rotateHeadRight.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent mEvent) {
                System.out.println("Rotating head right while button is pressed");
                headRotationAngle += 1.0;
                return false;
            }
        });

        bodyMovementJoystickView = (JoystickView) findViewById(R.id.bodyMovementJoystickView);
        bodyMovementJoystickView.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                System.out.println("Body Motion: angle:"+angle+", strength: "+strength);
                bodyMovementAngle = angle;
                bodyMovementSpeed = strength;
            }
        });

        headMovementJoystickView = (JoystickView) findViewById(R.id.headMovementJoystickView);
        headMovementJoystickView.setOnMoveListener(new JoystickView.OnMoveListener() {
            @Override
            public void onMove(int angle, int strength) {
                System.out.println("Head Motion: angle:"+angle+", strength: "+strength);
                headMovementAngle = angle;
                headMovementSpeed = strength;
            }
        });
    }

    private boolean hasNewBodyMovementAngle() {
        return bodyMovementAngle != oldBodyMovementAngle;
    }

    private boolean hasNewBodyMovementSpeed() {
        return bodyMovementSpeed != oldBodyMovementSpeed;
    }

    private boolean hasNewBodyRotation() {
        return bodyRotation != 0.0;
    }

    private boolean hasNewHeadMovementAngle() {
        return headMovementAngle != oldHeadMovementAngle;
    }

    private boolean hasNewHeadMovementSpeed() {
        return headMovementSpeed != oldHeadMovementSpeed;
    }
    private boolean hasNewHeadRotationAngle() {
        return headRotationAngle != oldHeadRotationAngle;
    }

    @Override
    protected void onDestroy() {
        pulseWidth = 1100;
        super.onDestroy();
    }

    public void openSettingsActivity() {
        Intent intent = new Intent(this, SettingsActivity.class);
        startActivity(intent);
    }
}
