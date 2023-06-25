package com.example.myapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class FinishGame extends AppCompatActivity {
    /** Starts camera gesture activity. */
    private Button mReturnHome;

    private TextView mSpeed;

    /** Called when the activity is first created. */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.end);
        mReturnHome = (Button) findViewById(R.id.home);
        mSpeed = (TextView) findViewById(R.id.status);

        mReturnHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg) {
                Intent intent = new Intent(FinishGame.this, MainActivity.class);
                startActivity(intent);
            }
        });

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        long time = extras.getLong("time");

        String timeMinSecs = getMinutes(time);
        String timeSeconds = String.valueOf(time);

        //Get their fastest time
        String savedText = getPreferences(MODE_PRIVATE).getString("time", null);

        if(savedText == null) {
            Log.e("", "saved text is null");
            String message = "Great job! \n 你的分数是: \n " + timeMinSecs;
            mSpeed.setText(message);
            SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
            editor.putString("time", timeSeconds);
            editor.apply();
        } else {
            if(Long.parseLong(savedText) >= time) {
                Log.e("", "new high score");
                String message = " Great job! \n 你打破了你的手势记录. "
                        + "你新的分数是: \n " + timeMinSecs;
                mSpeed.setText(message);
                SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
                editor.putString("time", timeSeconds);
                editor.apply();
            } else {
                Log.e("", "not a new high score");
                String message = " 很遗憾，你比你的最高分数低!";
                message += "\n 你的最高分是: \n" + getMinutes(Long.parseLong(savedText));
                message += "\n 当前分数是: \n" + timeMinSecs;
                mSpeed.setText(message);
            }
        }
    }

    private String getMinutes(long time) {
        int seconds = (int) (time / 1000) % 60 ;
        int minutes = (int) ((time / (1000*60)) % 60);
        String text = String.format("%2d 分钟 and %02d 秒", minutes, seconds);
        return text;
    }
}
