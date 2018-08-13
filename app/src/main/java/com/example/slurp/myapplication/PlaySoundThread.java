package com.example.slurp.myapplication;

import android.content.Context;
import android.media.MediaPlayer;
import android.widget.Toast;


public class PlaySoundThread implements Runnable {

    // looking into playing the sound ascyncrhonously if it doesn't already because it's in this thread,
    // then test the performance
    private MediaPlayer powerUpSound;

    public PlaySoundThread(final Context context, int resId, float volume){
        this.powerUpSound = MediaPlayer.create(
                context, resId);

        this.powerUpSound.setVolume(volume, volume);

        powerUpSound.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override

            public void onPrepared(MediaPlayer mediaPlayer) {
//                Toast.makeText(context, "media player setup",
//                        Toast.LENGTH_SHORT).show();
            }
        });

        powerUpSound.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.release();
                powerUpSound = null; // mark null for gb collection?
            }
        });
    }

    @Override
    public void run() {
        this.powerUpSound.start();
    }
}
