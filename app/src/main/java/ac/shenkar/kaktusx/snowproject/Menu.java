package ac.shenkar.kaktusx.snowproject;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;

public class Menu extends AppCompatActivity {
    private ViewGroup contentView;
    private Button playButton, scoreButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_menu);

        getWindow().setBackgroundDrawableResource(R.drawable.winter_landscape_menu);
        contentView = (ViewGroup) findViewById(R.id.activity_menu);

        playButton = (Button) findViewById(R.id.play_button);
        scoreButton = (Button) findViewById(R.id.score_button);

        playGameButton();
        heighScoreButton();


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
    protected void playGameButton(){
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(contentView.getContext(), MainActivity.class);
                contentView.getContext().startActivity(myIntent);
            }
        });
    }
    //Go to Highscore Activity
    protected void heighScoreButton(){
        scoreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(contentView.getContext(), Highscore.class);
                contentView.getContext().startActivity(myIntent);
            }
        });
    }
}
