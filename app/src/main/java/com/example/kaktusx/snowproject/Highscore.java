package com.example.kaktusx.snowproject;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class Highscore extends AppCompatActivity {
    private ViewGroup contentView;
    private DatabaseHelper controller;
    private TextView viewScore;
    //private ListView scoreView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_highscore);

        getWindow().setBackgroundDrawableResource(R.drawable.winterlandscape2);
        contentView = (ViewGroup) findViewById(R.id.activity_highscore);
        viewScore = (TextView) findViewById(R.id.viewScore);
        controller = new DatabaseHelper(this, "", null, 1);
        showScores();


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

    protected void showScores() {
        controller.showScore(viewScore);
    }
}
