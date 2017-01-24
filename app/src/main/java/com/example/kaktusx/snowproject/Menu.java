package com.example.kaktusx.snowproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class Menu extends AppCompatActivity {
    private ViewGroup contentView;
    private Button playButton, scoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        getWindow().setBackgroundDrawableResource(R.drawable.winter_landscape_menu);
        contentView = (ViewGroup) findViewById(R.id.activity_menu);

        playButton = (Button) findViewById(R.id.play_button);
        scoreButton = (Button) findViewById(R.id.score_button);

    }

    //make the game set to fullscreen
    private void setToFullScreen(){
        contentView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);
    }

    @Override
    protected void onResume() {
        super.onResume();
        setToFullScreen();
    }
    //Go to Main Activity
    protected void playGameButtonListener(View view){
        Intent myIntent = new Intent(this, MainActivity.class);
        startActivity(myIntent);
    }
    //Go to Highscore Activity
    protected void scoreButtonListener(View view){
        Intent myIntent = new Intent(this, Highscore.class);
        startActivity(myIntent);
    }
}
