package com.example.kaktusx.snowproject;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;


public class MainActivity extends AppCompatActivity implements SnowFlake.SnowFlakeListener{
    private static final int MIN_ANIM_DEALY = 500;
    private static final int MAX_ANIM_DEALY = 1500;
    private static final int MIN_ANIM_DURATION = 1000;
    private static final int MAX_ANIM_DURATION = 8000;
    private static final int NUM_OF_LIFE = 5;
    private ViewGroup contentView;
    private Button playButton;
    private  int[] flakeColors = new int[8];
    private  int nextColor, screenWidth, screenHeight;
    private int level, score, lifeUsed, flakesCatched;
    private boolean stopGame = true;
    private boolean inGame;
    private TextView scoreTextView, levelTextView;
    private List<ImageView> lifeImages = new ArrayList<ImageView>();
    private List<SnowFlake> snowFlakes = new ArrayList<SnowFlake>();
    private int flakesPerLevel = 3;
    private GameAudio music;
    private DatabaseHelper controller;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //setting the snow flake colors
        flakeColors[0] = Color.argb(255, 0, 255, 255);
        flakeColors[1] = Color.argb(255, 255, 255, 102);
        flakeColors[2] = Color.argb(255, 96, 96, 96);
        flakeColors[3] = Color.argb(255, 255, 102, 102);
        flakeColors[4] = Color.argb(255, 255, 50, 150);
        flakeColors[5] = Color.argb(255, 15, 80, 140);
        flakeColors[6] = Color.argb(255, 50, 255, 50);
        flakeColors[7] = Color.argb(255, 230, 120, 50);

        getWindow().setBackgroundDrawableResource(R.drawable.winterlandscape);
        contentView = (ViewGroup) findViewById(R.id.activity_main);
        setToFullScreen();

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        screenHeight = displaymetrics.heightPixels;
        screenWidth = displaymetrics.widthPixels;

//        contentView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                setToFullScreen();
//            }
//        });

        scoreTextView = (TextView) findViewById(R.id.score_display);
        levelTextView = (TextView) findViewById(R.id.level_display);
        playButton = (Button)findViewById(R.id.go_button);
        lifeImages.add((ImageView) findViewById(R.id.life5));
        lifeImages.add((ImageView) findViewById(R.id.life4));
        lifeImages.add((ImageView) findViewById(R.id.life3));
        lifeImages.add((ImageView) findViewById(R.id.life2));
        lifeImages.add((ImageView) findViewById(R.id.life1));

        updateView();

        music = new GameAudio();
        music.startMusicPlayer(this);

        controller = new DatabaseHelper(this, "", null, 1);
    }

    private void createFlake(int x) {
        SnowFlake flake = new SnowFlake(this, flakeColors[nextColor], 40);
        snowFlakes.add(flake);
        //changing the color to the next color order in array
        if (nextColor + 1 == flakeColors.length) {
            nextColor = 0;
        } else {
            nextColor++;
        }

        //Set snow flake vertical position
        flake.setX(x);
        flake.setY(screenHeight + flake.getHeight());
        contentView.addView(flake);

        //We set the duration of the fall according to the level and drop the flake
        int duration = Math.max(MIN_ANIM_DURATION, MAX_ANIM_DURATION - (level * 1000));
        flake.dropFlake(screenHeight, duration);

    }

    //make the game set to fullscreen

    private void setToFullScreen(){
        ViewGroup viewLayout = (ViewGroup) findViewById(R.id.activity_main);
        viewLayout.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN
                                | View.SYSTEM_UI_FLAG_LOW_PROFILE
                                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION);

    }

    protected void onResume() {
        super.onResume();
        setToFullScreen();
    }

    private void startGame(){
        setToFullScreen();
        score = 0;
        level = 0;
        lifeUsed = 0;
        //flakesPerLevel = 2;
        for (ImageView life : lifeImages) {
            life.setVisibility(View.VISIBLE);
        }
        stopGame = false;
        startLevel();
        music.playMusic();
    }
    private void startLevel(){
        //flakesPerLevel++;
        level++;
        updateView();
        snowFlakeDropper start = new snowFlakeDropper();
        start.execute(level);
        inGame = true;
        flakesCatched = 0;
        playButton.setText("Stop game");
    }

    private void finishLevel(){
        Toast.makeText(this, "You finished level: " + level, Toast.LENGTH_SHORT).show();
        inGame = false;
        playButton.setText("Start level " + (level+1));
    }

    protected void playButtonListener(View view) {
        if (inGame){
            gameOver(false);
        }
        else if (stopGame) {
            startGame();
            }
            else {
                startLevel();
            }
    }

    @Override
    public void removeSnowFlake(SnowFlake flake, boolean userTouch) {

        flakesCatched++;

        contentView.removeView(flake);
        snowFlakes.remove(flake);
        if (userTouch) {
            score++;
        }
        else{
            lifeUsed++;
            if (lifeUsed<= lifeImages.size()) {
                lifeImages.get(lifeUsed -1 ).setVisibility(View.INVISIBLE);
            }
            if (lifeUsed == NUM_OF_LIFE){
                gameOver(true);
                return;
            }
            else {
                //Toast.makeText(this, "Missed a Snow Flake!", Toast.LENGTH_SHORT).show();
            }
        }
        updateView();

        if (flakesCatched == flakesPerLevel)
            finishLevel();
    }

    //our gameover method, will change the go_button text accordingly and take care of removing flakes
    private void gameOver(boolean allLifeGone) {
        music.pauseMusic();
        Toast.makeText(this, "Game Over!", Toast.LENGTH_SHORT).show();
        for (SnowFlake flake: snowFlakes) {
            contentView.removeView(flake);
            flake.setDropped(true);
        }
        snowFlakes.clear();
        flakesCatched = 0;
        inGame = false;
        stopGame = true;
        playButton.setText("Start a new game");
        
        if (allLifeGone) {
            //setToFullScreen();
            AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
            dialog.setMessage("Your score is " + score);

            dialog.setTitle("Enter Your Name: ");
            final EditText name = new EditText(this);
            dialog.setView(name);
            dialog.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    controller.insertScore(name.getText().toString(), score);
                }
            });
            dialog.show();

        }
        else{
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

    }
    //updates the view
    private void updateView() {
        scoreTextView.setText(String.valueOf(score));
        levelTextView.setText(String.valueOf(level));
    }

    private class snowFlakeDropper extends AsyncTask<Integer, Integer, Void> {

        @Override
        protected Void doInBackground(Integer... params) {

            if (params.length != 1) {
                throw new AssertionError("Expected 1 param for current level");
            }

            int level = params[0];
            int maxDelay = Math.max(MIN_ANIM_DEALY,
                    (MAX_ANIM_DEALY - ((level - 1) * 500)));
            int minDelay = maxDelay / 2;

            int flakeDropped = 0;
            while (inGame && flakeDropped < flakesPerLevel) {

                //Get a random horizontal position for the next snow flake
                Random random = new Random(new Date().getTime());
                int x = random.nextInt(screenWidth - 200);
                publishProgress(x);
                flakeDropped++;
                //Wait a random number of milliseconds before continuing the loop
                int delay = random.nextInt(minDelay) + minDelay;
                try {
                    Thread.sleep(delay);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }
        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            int x = values[0];
            createFlake(x);
        }
    }
}