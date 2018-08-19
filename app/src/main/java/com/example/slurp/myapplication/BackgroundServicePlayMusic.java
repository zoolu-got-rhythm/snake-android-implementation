package com.example.slurp.myapplication;

import android.app.Service;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.IBinder;
import android.util.Log;

public class BackgroundServicePlayMusic extends Service {

    private static final String TAG = "BackgroundSoundService";
    MediaPlayer player;

    SoundPool soundPool;
    int soundId;



    public IBinder onBind(Intent arg0) {
        Log.i(TAG, "onBind()" );
        return null;
    }

    @Override
    public void onCreate() {
//        super.onCreate();
//        player = MediaPlayer.create(this, R.raw.menu_music);
//        player.setLooping(true); // Set looping
//
//        player.setVolume(50,50);

        soundPool = new SoundPool(10, AudioManager.STREAM_MUSIC, 0);
        soundId = soundPool.load(this, R.raw.menu_music, 1);

        soundPool.setOnLoadCompleteListener(new SoundPool.OnLoadCompleteListener() {
            @Override
            public void onLoadComplete(SoundPool soundPool, int i, int i1) {
                soundPool.play(soundId, 50, 50, 1, 1, 0.5f);
            }
        });



        Log.i(TAG, "onCreate() , service started...");

    }
    public int onStartCommand(Intent intent, int flags, int startId) {
//        player.start();
        return Service.START_STICKY;
    }

    public IBinder onUnBind(Intent arg0) {
        Log.i(TAG, "onUnBind()");
        return null;
    }
    public void onStop() {
        Log.i(TAG, "onStop()");
    }
    public void onPause() {
        if(player!=null && player.isPlaying()){
            player.pause();
        }
        Log.i(TAG, "onPause()");
    }
    @Override
    public void onDestroy() {
        player.stop();
        player.release();
        Log.i(TAG, "onCreate() , service stopped...");
    }

    @Override
    public void onLowMemory() {
        Log.i(TAG, "onLowMemory()");
    }
}
