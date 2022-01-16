package com.torodesigns.rrpress;

import android.content.Context;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.net.Uri;
import android.provider.OpenableColumns;

import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MyAudio {
    private Uri audioUri;
    private String audioName;
    private int songDuration;
    private MediaPlayer mediaPlayer;

    public MyAudio(Uri audioUri, Context context){
        this.audioUri = audioUri;
        mediaPlayer = MediaPlayer.create(context,audioUri);
        songDuration = mediaPlayer.getDuration();
        setAudioName(context);
    }

    public String getSongDuration() {
        return String.format(Locale.getDefault(), "%02d:%02d",
                minutesElapsed(songDuration), secondsElapsed(songDuration));
    }

    public Uri getAudioUri() {
        return audioUri;
    }

    public MediaPlayer getMediaPlayer() {
        return mediaPlayer;
    }

    public void setAudioName(Context context) {
        Cursor returnCursor = context.getContentResolver()
                .query(audioUri,null,null,null,null);
        int nameIndex = returnCursor.getColumnIndex(OpenableColumns.DISPLAY_NAME);
        returnCursor.moveToFirst();
        audioName = returnCursor.getString(nameIndex);
        returnCursor.close();
    }

    public String getAudioName() {
        return audioName;
    }

    //converts milliseconds to minutes
    private long minutesElapsed(int time){
        return TimeUnit.MILLISECONDS.toMinutes(time);
    }

    //converts milliseconds to seconds
    private long secondsElapsed(int time){
        return TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds
                (TimeUnit.MILLISECONDS.toMinutes(time));
    }
}
