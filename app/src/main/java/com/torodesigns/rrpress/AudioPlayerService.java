package com.torodesigns.rrpress;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class AudioPlayerService extends Service {

    private final IBinder binder = new AudioPlayerBinder(); //IBinder Object
    MediaPlayer mediaPlayer;
    private int resumePosition;
    public static final String RESID = "resId";
    int resId;

    public AudioPlayerService() {
    }

    //IBinder implementation
    public class AudioPlayerBinder extends Binder {
        AudioPlayerService getAudioPlayerService(){
            return AudioPlayerService.this;
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return binder;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Toast.makeText(this,"Start",Toast.LENGTH_SHORT).show();
        try {
            resId = intent.getExtras().getInt(RESID);
        }catch (NullPointerException e){
            Log.v("Null","NullPointer");
            e.printStackTrace();
        }
        initializeAudioPlayer();
        return super.onStartCommand(intent, flags, startId);
    }

    private void initializeAudioPlayer(){
        mediaPlayer = MediaPlayer.create(this,resId);
    }

    public void playAudio(){
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }
    }

    public void pauseAudio(){
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            resumePosition = mediaPlayer.getCurrentPosition();
        }
    }

    public void resumeMedia(){
        if(!mediaPlayer.isPlaying()){
            mediaPlayer.seekTo(resumePosition);
            mediaPlayer.start();
        }
    }

    private void stopMedia(){
        if(mediaPlayer == null){
            return;
        }
        if(mediaPlayer.isPlaying()){
            mediaPlayer.stop();
        }
    }

    public int getCurrentPosition(){
        return mediaPlayer.getCurrentPosition();
    }

    public int getSongDuration(){
        return  mediaPlayer.getDuration();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mediaPlayer != null){
            stopMedia();
            mediaPlayer.release();
        }

    }
}
