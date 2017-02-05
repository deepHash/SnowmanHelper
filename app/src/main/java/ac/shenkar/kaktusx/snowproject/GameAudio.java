package ac.shenkar.kaktusx.snowproject;

import android.content.Context;
import android.media.MediaPlayer;


public class GameAudio {
    //
    private MediaPlayer musicPlayer;
    public GameAudio() {

    }
    //init the music player
    protected void startMusicPlayer(Context context){
        //song was taken from www.freexmasmp3.com
        musicPlayer = musicPlayer.create(context, R.raw.carol_of_the_bells);
        musicPlayer.setVolume(0.5f,0.5f);
        musicPlayer.setLooping(true);
    }

    //play music
    protected void playMusic(){
        if (musicPlayer != null){
            musicPlayer.start();
        }
    }
    //pause the music
    protected void pauseMusic(){
        if (musicPlayer != null && musicPlayer.isPlaying()) {
            musicPlayer.pause();
        }
    }
    //set from settings activity only!
    // TODO: 22/01/2017 change according to settings activity 
    protected void changeVolume(float vol) {
        musicPlayer.setVolume(vol, vol);
    }

}
