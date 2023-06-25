package com.example.myapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Path;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

import edu.washington.cs.touchfreelibrary.sensors.CameraGestureSensor;
import edu.washington.cs.touchfreelibrary.sensors.ClickSensor;
import edu.washington.cs.touchfreelibrary.utilities.LocalOpenCV;
import edu.washington.cs.touchfreelibrary.utilities.PermissionUtility;

public class PracticeGesturesActivity extends AppCompatActivity implements CameraGestureSensor.Listener, ClickSensor.Listener {

    Random mRandom;
    int mCurrentRound;
    long mStartTime;
    /** The total number of rounds played. */
    private static final int NUMBER_OF_ROUNDS = 25;
    private static final String TAG = "PracticeGesturesActivity";

    LocalOpenCV loader;

    int mCurrentDirection;
    final int DirectionLeft = 0;
    final int DirectionDown = 1;
    final int DirectionRight = 2;
    final int DirectionUp = 3;
    final int DirectionNone = 4;
    final int DirectionClick = 5;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_start);

        if (PermissionUtility.checkCameraPermission(this)) {
            //The third passing in represents a separate click sensor which is not required if you just want the hand motions
            loader = new LocalOpenCV(PracticeGesturesActivity.this, PracticeGesturesActivity.this, PracticeGesturesActivity.this);
        }

        mRandom = new Random();
        mCurrentRound = 0;
        mStartTime = System.currentTimeMillis();
        setRandomDirection();
    }

    /** Moves onto the next screen. If all of the rounds are over,
     *  opens the final screen. */
    protected void setRandomDirection() {
        if(mCurrentRound >= NUMBER_OF_ROUNDS) {
            Intent intent = new Intent(this, FinishGame.class);
            Log.e(TAG, "" + System.currentTimeMillis() );
            Log.e(TAG, "" + mStartTime );

            long totalTime = System.currentTimeMillis() - mStartTime;
            intent.putExtra("time", totalTime);
            startActivity(intent);

            // Game is over
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // mGestureSensor.stop();
                }
            });
        } else {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    TextView mCurrentScore = (TextView) findViewById(R.id.textView3);
                    TextView mDirection = (TextView) findViewById(R.id.textView4);
                    ImageView imageView1 = (ImageView) findViewById(R.id.imageView1);
                    mCurrentRound++;
                    mCurrentScore.setText("" + (mCurrentRound) + "/" + NUMBER_OF_ROUNDS);

                    int randomNumb = mRandom.nextInt(4);
                    switch(Integer.valueOf(randomNumb)) {
                        case 0:
                            mCurrentDirection = DirectionUp;
                            imageView1.setImageResource(R.drawable.arrow_up);
                            break;
                        case 1:
                            mCurrentDirection = DirectionDown;
                            imageView1.setImageResource(R.drawable.arrow_down);
                            break;
                        case 2:
                            mCurrentDirection = DirectionLeft;
                            imageView1.setImageResource(R.drawable.arrow_left);
                            break;
                        case 3:
                            mCurrentDirection = DirectionRight;
                            imageView1.setImageResource(R.drawable.arrow_right);
                            break;
                        case 4:
                            mCurrentDirection = DirectionClick;
                            imageView1.setImageResource(R.drawable.click);
                            break;
                    }

                    // Makes the background color look quite nice
                    int red = mRandom.nextInt(256-150) + 150;
                    int green = mRandom.nextInt(256-150) + 150;
                    int blue = mRandom.nextInt(256-150) + 150;

                    RelativeLayout wholeScreen = (RelativeLayout) findViewById(R.id.setArrows);
                    wholeScreen.setBackgroundColor(Color.rgb(red,green,blue));
                    imageView1.setBackgroundColor(Color.rgb(red,green,blue));
                    mDirection.setText("" + directionToString(mCurrentDirection));
                }
            });
        }
    }

    /** Given a direction, returns the direction as a string. */
    private String directionToString(int d) {
        switch(d) {
            case DirectionUp:
                return "Up";
            case DirectionDown:
                return "Down";
            case DirectionLeft:
                return "Left";
            case DirectionClick:
                return "Click";
            default:
                return "Right";
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (PermissionUtility.checkCameraPermission(this)) {
            loader = new LocalOpenCV(PracticeGesturesActivity.this, PracticeGesturesActivity.this, PracticeGesturesActivity.this);
        }
    }

    /** Given a Direction, checks whether that direction is the
     *  same as the currently anticipated gesture. Moves onto the next
     *  screen if it is. */
    private void checkGestureAccuracy(int direction) {
        final int userDirection = direction;
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if(mCurrentDirection == userDirection) {
                    setRandomDirection();
                }
            }
        });

    }

    @Override
    public void onGestureUp(CameraGestureSensor caller, long gestureLength) {
        Log.e(TAG, "up gesture detected");
        checkGestureAccuracy(DirectionUp);
    }

    @Override
    public void onGestureDown(CameraGestureSensor caller, long gestureLength) {
        Log.e(TAG, "down gesture detected");
        checkGestureAccuracy(DirectionDown);
    }

    @Override
    public void onGestureLeft(CameraGestureSensor caller, long gestureLength) {
        Log.e(TAG, "Left gesture detected");
        checkGestureAccuracy(DirectionLeft);
    }

    @Override
    public void onGestureRight(CameraGestureSensor caller, long gestureLength) {
        Log.e(TAG, "right gesture detected");
        checkGestureAccuracy(DirectionRight);
    }

    @Override
    public void onSensorClick(ClickSensor caller) {
        Log.e(TAG, "click gesture detected");
        checkGestureAccuracy(DirectionClick);
    }
}
