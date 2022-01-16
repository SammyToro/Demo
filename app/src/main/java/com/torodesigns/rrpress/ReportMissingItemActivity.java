package com.torodesigns.rrpress;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ReportMissingItemActivity extends AppCompatActivity {
    private  static final int PERMISSION_CODE = 1000;
    private int IMAGE_CAPTURE_CODE = 1002;
    private int MEDIA_CAPTURE_CODE = 1003;
    private BottomSheetDialog imageDialog,recordingDialog;
    private Uri imageUri,audioUri;
    private MediaRecorder mediaRecorder;
    private TextView recordingTimeTextView,microphone,locationTextView;
    private boolean showMic = true;
    private final Handler handler = new Handler();
    private int button,seconds = 0;
    ViewGroup parentLayout;
    View rrImageView,rrAudioView;
    private Button galleryButton,voiceNoteButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_missing_item);

        //set the toolbar as the activities app bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //enable back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        setTitle("Report A Retrieved Item");

        //Initialize bottom sheet image option
        View imageOptionsView = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_images,null);
        imageDialog = new BottomSheetDialog(this);
        imageDialog.setContentView(imageOptionsView);

        //Initialize bottom sheet recording option
        View recordingView = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_recording,null);
        recordingTimeTextView = (TextView) recordingView.findViewById(R.id.timeTextView);
        microphone = (TextView) recordingView.findViewById(R.id.mic);
        recordingDialog = new BottomSheetDialog(this);
        recordingDialog.setContentView(recordingView);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);

        rrImageView = inflater.inflate(R.layout.rr_image,
                (ViewGroup) findViewById(R.id.retrieved_image),false);

        //Set the layout width and layout height
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        rrImageView.setLayoutParams(params);

        //Set the layout margin top
        ViewGroup.MarginLayoutParams layoutParams = new ViewGroup.MarginLayoutParams(params);
        layoutParams.topMargin = 15;
        rrImageView.setLayoutParams(layoutParams);

        rrAudioView = inflater.inflate(R.layout.rr_audio,
                (ViewGroup) findViewById(R.id.rr_recorded_audio),false);

        //Set the layout width and layout height
        ViewGroup.LayoutParams params1 = new ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        rrAudioView.setLayoutParams(params1);

        //Set the layout margin top
        ViewGroup.MarginLayoutParams layoutParams1 = new ViewGroup.MarginLayoutParams(params1);
        layoutParams1.topMargin = 15;
        rrAudioView.setLayoutParams(layoutParams1);

        parentLayout = (LinearLayout) findViewById(R.id.parentLayout);

        galleryButton = (Button) findViewById(R.id.galleryButton);
        voiceNoteButton = (Button) findViewById(R.id.voiceNoteButton);


    }

    public void onRRImageButtonClicked(View view){
        imageDialog.show();
    }

    //Creates an image or video file using MediaStore
    private Uri createImageFile(){
        Uri uri;
        String fileDirectory = Environment.DIRECTORY_PICTURES;
        final String relativeLocation = fileDirectory + File.separator + "Envoy";
        //Create image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "JPEG" + timeStamp;
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME,fileName);

        //Stores the file in Envoy Folder located in the Pictures Directory
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation);
        }else{
            String root = Environment.getExternalStoragePublicDirectory(fileDirectory).toString();
            File directory = new File(root+File.separator+"Envoy");
            if(!directory.exists()){
                directory.mkdirs();
            }
            File file = new File(directory,fileName + ".jpg");
            values.put(MediaStore.Images.Media.DATA,file.getAbsolutePath());
        }
        uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        return uri;
    }

    //When the gallery button is clicked
    public void onRRGalleryButtonClicked(View view){
        imageDialog.cancel();// closes the image options dialog
        //Configure and start the media chooser intent
        Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
        //comma-seperated MIME types
        mediaChooser.setType("*/*");
        mediaChooser.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,false);
        mediaChooser.putExtra(Intent.EXTRA_MIME_TYPES,new String[] {"image/*"});
        startActivityForResult(mediaChooser,MEDIA_CAPTURE_CODE);
    }

    public void openCamera(){
        imageUri = createImageFile();
        //Start the camera intent
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        if(cameraIntent.resolveActivity(getPackageManager())!= null){ //Prevent app from crashing if intent cannot be handled
            startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);
        }
    }

    //When the camera button is clicked
    public void onRRCameraButtonClicked(View view){
        imageDialog.cancel();
        button = 1;
        //if permission to use camera has not been granted
        if(ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this,permission,PERMISSION_CODE); //request permission
        }else{
            openCamera();
        }
    }

    public void onRemoveRRImageClicked(View view){
        parentLayout.removeView(rrImageView);
        galleryButton.setEnabled(true);
    }

    //Creates an audio file using MediaStore
    private Uri createAudioFile() throws FileNotFoundException {
        final String relativeLocation = Environment.DIRECTORY_MUSIC + File.separator + "Envoy";
        //Create image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = "AUD_" + timeStamp + ".mp3";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Media.DISPLAY_NAME,fileName);
        values.put(MediaStore.Audio.Media.MIME_TYPE,"audio/mp3");
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation);
        }else {
            String root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC).toString();
            File directory = new File(root + File.separator + "Envoy");
            if (!directory.exists()) {
                directory.mkdirs();
            }
            File file = new File(directory, fileName);
            values.put(MediaStore.Audio.Media.DATA,file.getAbsolutePath());
        }
        return   getContentResolver().insert(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,values);
    }

    //Show time recorded updates with mic on and off animation
    private Runnable runTimer = new Runnable() {
        @Override
        public void run() {
            int minutes = (seconds%3600)/60;
            int secs = seconds%60;
            String time = String.format(Locale.getDefault(),"%d:%02d",minutes,secs);
            recordingTimeTextView.setText(time);
            if(showMic){
                microphone.setVisibility(View.VISIBLE);
                showMic = false;
            }else{
                microphone.setVisibility(View.INVISIBLE);
                showMic = true;
            }
            seconds++;
            handler.postDelayed(this,1000);
        }
    };

    public void recordAudio(){
        try{
            audioUri = createAudioFile();
            ParcelFileDescriptor file = getContentResolver().openFileDescriptor(audioUri,"w");
            //Create MediaRecorder object with the necessary configuration
            mediaRecorder = new MediaRecorder();
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC);
            mediaRecorder.setOutputFile(file.getFileDescriptor());
            mediaRecorder.prepare();
            mediaRecorder.start();
            //Show the recorder UI
            recordingTimeTextView.setText(R.string.initialTime);
            recordingDialog.show();
            handler.postDelayed(runTimer,1000);//Stops the animation
        }catch (FileNotFoundException e){
            e.printStackTrace();
        }catch (IOException e){
            e.printStackTrace();
        }
    }

    //When the recording is cancelled
    public void onCancelRecordingButtonClicked(View view){
        //delete the created audio file
        getContentResolver().delete(audioUri,null,null);
        //release the mediaRecorder resources and close the recording UI
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
        seconds = 0;
        recordingDialog.cancel();
        handler.removeCallbacks(runTimer);
    }

    //When the recording is completed
    public void onDoneRecordingButtonClicked(View view){
        //release the mediaRecorder resources and close the recording UI
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        seconds = 0;
        recordingDialog.cancel();
        handler.removeCallbacks(runTimer);

        MyAudio audio = new MyAudio(audioUri,this);

        TextView audioName = (TextView) rrAudioView.findViewById(R.id.audioName);
        audioName.setText(audio.getAudioName());

        TextView songTime = (TextView) rrAudioView.findViewById(R.id.songTime);
        songTime.setText(audio.getSongDuration());

//        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams)  rrAudioView.getLayoutParams();
//        params.topMargin = 10;
//        rrAudioView.setLayoutParams(params);

        parentLayout.addView(rrAudioView);
        voiceNoteButton.setEnabled(false);



    }
    public void onRRVoiceNoteButtonClicked(View view){
        button  = 2;
        //If permission to record audio has not been granted
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            String[] permission = {Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this,permission,PERMISSION_CODE); //request permission
        }else{
            recordAudio();
        }
    }

    public  void onRemoveRRAudioClicked(View view){
        parentLayout.removeView(rrAudioView);
        voiceNoteButton.setEnabled(true);
    }

    public void onRRAudioPlayButtonClicked(View view){//Starts a music player intent
        Intent intent  = new Intent(Intent.ACTION_VIEW);
        intent.setData(audioUri);
        if(intent.resolveActivity(getPackageManager()) != null){
            startActivity(intent);
        }
    }

    //When permission is requested
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults){
        switch (requestCode){
            case PERMISSION_CODE: {
                //if permission has been granted, camera
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    switch (button) {
                        case 1:
                            openCamera();
                            break;
                        case 2:
                            recordAudio();
                            break;
                    }
                } else { //display message if permission is denied
                    Toast.makeText(this, "Permission Denied .....", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if(requestCode == IMAGE_CAPTURE_CODE){
            if(resultCode == RESULT_OK){//If image was captured
                //Add captured image to view
                ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) rrImageView.getLayoutParams();
                params.topMargin = 10;
                parentLayout.addView(rrImageView);
                ImageView capturedImage = (ImageView) findViewById(R.id.capturedImage);
                capturedImage.setImageURI(imageUri);
                galleryButton.setEnabled(false);
            }else if(resultCode == RESULT_CANCELED){//if image was not captured
                //delete the file created
                getContentResolver().delete(imageUri,null,null);
            }
        }else if(requestCode == MEDIA_CAPTURE_CODE){
            if(resultCode == RESULT_OK){//if image was selected
                if(data.getData() != null){
                    imageUri = data.getData();
                    //Add captured image to view
                    parentLayout.addView(rrImageView);
                    ImageView capturedImage = (ImageView) findViewById(R.id.capturedImage);
                    capturedImage.setImageURI(imageUri);
                    galleryButton.setEnabled(false);
                }
            }

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
