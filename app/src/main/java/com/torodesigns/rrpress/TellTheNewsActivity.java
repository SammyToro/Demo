package com.torodesigns.rrpress;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.ClipData;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.media.MediaRecorder;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class TellTheNewsActivity extends AppCompatActivity {

    private  static final int PERMISSION_CODE = 1000;
    private int IMAGE_CAPTURE_CODE = 1002;
    private int VIDEO_CAPTURE_CODE = 1003;
    private int MEDIA_CAPTURE_CODE = 1004;
    private Uri imageUri,videoUri,audioUri;
    private  RecyclerView imagesRecyclerView,videosRecyclerView,audiosRecyclerView;
    private ReportingImagesAdapter reportingImagesAdapter;
    private ReportingVideosAdapter reportingVideosAdapter;
    private ReportingAudiosAdapter reportingAudiosAdapter;
    private ArrayList<String> imagesPath,videosPath,audiosPath;
    private MediaRecorder mediaRecorder;
    private TextView recordingTimeTextView,microphone,locationTextView;
    private boolean showMic = true;
    private final Handler handler = new Handler();
    private BottomSheetDialog dialog;
    private int seconds = 0,button;
   private FusedLocationProviderClient fusedLocationProviderClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tell_the_news);

        //set the toolbar as the activities app bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //enable back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);


        //Initialize and use image reclcyer view ti display images
        imagesRecyclerView = (RecyclerView) findViewById(R.id.imagesRecyclerView);
        reportingImagesAdapter = new ReportingImagesAdapter();
        imagesRecyclerView.setAdapter(reportingImagesAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,false);
        imagesRecyclerView.setLayoutManager(linearLayoutManager);
        imagesRecyclerView.setVisibility(View.GONE);



        //When close button is clicked, display the remaining images
        reportingImagesAdapter.setListener(new ReportingImagesAdapter.Listener() {
            @Override
            public void onClick(int position) {
                reportingImagesAdapter.getImagesUri().remove(position);
                reportingImagesAdapter.notifyDataSetChanged();
                if(reportingImagesAdapter.getItemCount() == 0){
                    imagesRecyclerView.setVisibility(View.GONE);
                }
            }
        });

        //Hold the videos captured or selected by the user
        videosRecyclerView = (RecyclerView) findViewById(R.id.videosRecyclerView);
        reportingVideosAdapter = new ReportingVideosAdapter();
        videosRecyclerView.setAdapter(reportingVideosAdapter);
        linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,false); //Specifies the layout for the view
        videosRecyclerView.setLayoutManager(linearLayoutManager);
        videosRecyclerView.setVisibility(View.GONE);

        reportingVideosAdapter.setListener(new ReportingVideosAdapter.Listener() {
            @Override
            public void onClick(int position) {
                reportingVideosAdapter.getVideosUri().remove(position);
                reportingVideosAdapter.notifyDataSetChanged();
                if(reportingVideosAdapter.getItemCount() == 0){
                    videosRecyclerView.setVisibility(View.GONE);
                }
            }
        });

        //Hold the audio recoreded by the user
        audiosRecyclerView = (RecyclerView) findViewById(R.id.audiosRecyclerView);
        reportingAudiosAdapter = new ReportingAudiosAdapter();
        audiosRecyclerView.setAdapter(reportingAudiosAdapter);
        linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.VERTICAL,false);//Specifies the layout for the view
        audiosRecyclerView.setLayoutManager(linearLayoutManager);
        audiosRecyclerView.setVisibility(View.GONE);

        //When the play button of the view is clicked
        reportingAudiosAdapter.setListener(new ReportingAudiosAdapter.Listener() {
            @Override
            public void onClick(int position) {//Starts a music player intent
                Intent intent  = new Intent(Intent.ACTION_VIEW);
                intent.setData(reportingAudiosAdapter.getAudios().get(position).getAudioUri());
                if(intent.resolveActivity(getPackageManager()) != null){
                    startActivity(intent);
                }
            }
        });

        //Initialise bottom sheet dialog fragment
        View view1  = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_recording,null);
        recordingTimeTextView = (TextView) view1.findViewById(R.id.timeTextView);
        microphone = (TextView) view1.findViewById(R.id.mic);
        dialog = new BottomSheetDialog(this);
        dialog.setContentView(view1);
        dialog.setCanceledOnTouchOutside(false);

        locationTextView = (TextView) findViewById(R.id.location);
        locationTextView.setVisibility(View.GONE);

        //Use Google play services location's fusedLocationProviderClient
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

    }

    @Override
    protected void onStart() {
        if(reportingImagesAdapter.getItemCount() > 0){
            imagesRecyclerView.setVisibility(View.VISIBLE);
        }
        if(reportingVideosAdapter.getItemCount() > 0){
            videosRecyclerView.setVisibility(View.VISIBLE);
        }
        if(reportingAudiosAdapter.getItemCount() > 0){
          audiosRecyclerView.setVisibility(View.VISIBLE);
        }
        super.onStart();
    }

    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    //When the gallery button is clicked
    public void onGalleryButtonClicked(View view){
        if(reportingImagesAdapter.getItemCount() < 3 || reportingVideosAdapter.getItemCount() < 2){
            Intent mediaChooser = new Intent(Intent.ACTION_GET_CONTENT);
            //comma-seperated MIME types
            mediaChooser.setType("*/*");
            mediaChooser.putExtra(Intent.EXTRA_ALLOW_MULTIPLE,true);
            mediaChooser.putExtra(Intent.EXTRA_MIME_TYPES,new String[] {"image/*","video/*"});
            startActivityForResult(mediaChooser,MEDIA_CAPTURE_CODE);
        }else{
            String text = " Maximmum number of images and videos selected";
            Snackbar.make(findViewById(R.id.tellTheNews),text,Snackbar.LENGTH_SHORT).show();
        }
    }

    //When the camera button is clicked
    public void onCameraButtonClicked(View view){
        button = 1;
        //if permission to use camera has not been granted
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this,permission,PERMISSION_CODE); //request permission
        }else{
            openCamera();
        }

    }

    //Creates an image or video file using MediaStore
    private Uri createFile(String fileDirectory,String identifier,String fileExtension){
        Uri uri = null;
        final String relativeLocation = fileDirectory + File.separator + "Envoy";
        //Create image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String fileName = identifier + timeStamp;
        ContentValues values = new ContentValues();
        values.put(MediaStore.MediaColumns.DISPLAY_NAME,fileName);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            values.put(MediaStore.MediaColumns.RELATIVE_PATH, relativeLocation);
        }else{
            String root = Environment.getExternalStoragePublicDirectory(fileDirectory).toString();
            File directory = new File(root+File.separator+"Envoy");
            if(!directory.exists()){
                directory.mkdirs();
            }
            File file = new File(directory,fileName + fileExtension);
            if(fileName.contains("JPEG")){
                values.put(MediaStore.Images.Media.DATA,file.getAbsolutePath());
            }else if(fileName.contains("MP4")){
                values.put(MediaStore.Video.Media.DATA,file.getAbsolutePath());
            }
        }
        if(fileName.contains("JPEG")){
            uri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        }else if(fileName.contains("MP4")){
            uri = getContentResolver().insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI,values);
        }

        return uri;
    }

    private void openCamera(){
        if(reportingImagesAdapter.getItemCount() < 3){ //A maximum of four images can be uploaded
            imageUri = createFile(Environment.DIRECTORY_PICTURES,"JPEG",".jpg");
            //Start the camera intent
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
            if(cameraIntent.resolveActivity(getPackageManager())!= null){ //Prevent app from crashing if intent cannot be handled
                startActivityForResult(cameraIntent,IMAGE_CAPTURE_CODE);
            }

        }else{
            CharSequence text = "Maximum number of images selected, delete some of the images";
            int duration = Snackbar.LENGTH_SHORT;
            Snackbar snackbar = Snackbar.make(findViewById(R.id.tellTheNews),text,duration);
            snackbar.show();
        }

    }

    //When the video button is clicked
    public void onVideoButtonClicked(View view){
        button = 2;
        //if permission to use camera has not been granted
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED){
            String[] permission = {Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this,permission,PERMISSION_CODE); //request permission
        }else{
            openVideo();
        }

    }

    private void openVideo(){
        if(reportingVideosAdapter.getItemCount() < 2){
            videoUri = createFile(Environment.DIRECTORY_MOVIES,"MP4",".mp4");
            //Start video recorder intent
            Intent videoIntent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);
            videoIntent.putExtra(MediaStore.EXTRA_OUTPUT,videoUri);
            if(videoIntent.resolveActivity(getPackageManager())!= null){ //Prevent app from crashing if intent cannot be handled
                startActivityForResult(videoIntent,VIDEO_CAPTURE_CODE);
            }
        }else{
            CharSequence text = "Maximum number of videos selected, delete some of the videos";
            int duration = Snackbar.LENGTH_SHORT;
            Snackbar snackbar = Snackbar.make(findViewById(R.id.tellTheNews),text,duration);
            snackbar.show();

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
                            openVideo();
                            break;
                        case 3:
                            recordAudio();
                            break;
                        case 4:
                            getLocation();
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
    protected void onActivityResult(int requestCode,int resultCode,Intent data){
        if(requestCode == IMAGE_CAPTURE_CODE){ //Results returned from the Camera Intent
            if(resultCode == RESULT_OK ){
                reportingImagesAdapter.getImagesUri().add(imageUri);
                reportingImagesAdapter.notifyDataSetChanged();
            }else if(resultCode == RESULT_CANCELED){
                //delete the file created
                getContentResolver().delete(imageUri,null,null);
            }
        }else if(requestCode == VIDEO_CAPTURE_CODE){ //Results returned from the Video Intent
            if(resultCode == RESULT_OK ){
                if(Build.VERSION.SDK_INT < Build.VERSION_CODES.N ) {
                    videoUri = data.getData();
                }
                reportingVideosAdapter.getVideosUri().add(videoUri);
                reportingVideosAdapter.notifyDataSetChanged();
            }else if(resultCode == RESULT_CANCELED){
                //delete the file created
                getContentResolver().delete(videoUri,null,null);
            }
        }else if(requestCode == MEDIA_CAPTURE_CODE){ //Results returned from Multimedia(Videos and Images)
            if(resultCode == RESULT_OK){
                if( data.getData() != null ){ //Handle a single image or video selected
                    Uri selectedMediaUri = data.getData();
                    String text = "";
                    boolean showToast = false;
                    if(selectedMediaUri.toString().contains("image")){
                        //handle image
                        if(reportingImagesAdapter.getItemCount() < 3){
                            reportingImagesAdapter.getImagesUri().add(selectedMediaUri);
                            reportingImagesAdapter.notifyDataSetChanged();
                        }else{
                            text = "Maximum number of Images selected. Try deleting some Images";
                            showToast = true;
                        }

                    }else if(selectedMediaUri.toString().contains("video")){
                        //handle video
                        if(reportingVideosAdapter.getItemCount() < 2){
                            reportingVideosAdapter.getVideosUri().add(selectedMediaUri);
                            reportingVideosAdapter.notifyDataSetChanged();
                        }else{
                            text = "Maximum number of Videos selected. Try deleting some Videos";
                            showToast = true;
                        }
                    }else{
                        text = "Choose an Image or a Video";
                        showToast = true;
                    }
                    if(showToast){
                        Snackbar.make(findViewById(R.id.tellTheNews),text,Snackbar.LENGTH_SHORT).show();
                    }
                }else{ //Handle multiple images and video selected
                    if( data.getClipData() != null){
                        ClipData clipData = data.getClipData();
                        int imageCount = 0, videoCount = 0;
                        int currentImagesCount = reportingImagesAdapter.getItemCount();
                        int currentVideoCount = reportingVideosAdapter.getItemCount();
                        String text = "";
                        boolean showToast = true;
                        boolean showImageText = false;
                        boolean showVideoText = false;
                        String imageText = "",videoText = "";
                        ArrayList<Uri> mediaUri = new ArrayList<>();
                        for(int i = 0; i < clipData.getItemCount(); i++){
                            ClipData.Item item = clipData.getItemAt(i);
                            mediaUri.add(item.getUri());
                            if(item.getUri().toString().contains("image")){
                                imageCount++;
                            }else if(item.getUri().toString().contains("video")){
                                videoCount++;
                            }
                        }
                        for(Uri uri : mediaUri){
                            if(uri.toString().contains("image")){
                                //handle image
                                int totalImages = currentImagesCount + imageCount;
                                if(totalImages < 4){
                                    reportingImagesAdapter.getImagesUri().add(uri);
                                }else{
                                    showImageText = true;
                                    imageText =  "Maxumum number of Images that can be selected is 3. Try again\n";
                                }

                            }else if(uri.toString().contains("video")){
                                //handle video
                                int totalVideos = currentVideoCount + videoCount;
                                if(totalVideos < 3){
                                    reportingVideosAdapter.getVideosUri().add(uri);
                                }else{
                                    showVideoText = true;
                                    videoText =  "Maximum number of Videos that can be selected is 2. Try again\n";
                                }
                            }
                        }
                        if(showImageText && showVideoText){
                            text = text + imageText + videoText;
                        }else if(showImageText){
                            text = text + imageText;
                        }else if(showVideoText){
                           text = text + videoText;
                        }else{
                            showToast = false;
                        }
                        if(showToast){
                            Toast.makeText(this,text,Toast.LENGTH_SHORT).show();
                        }
                        reportingImagesAdapter.notifyDataSetChanged();
                        reportingVideosAdapter.notifyDataSetChanged();
                    }
                }
            }
        }
        if(reportingImagesAdapter.getItemCount() > 0){
            imagesRecyclerView.setVisibility(View.VISIBLE);
        }
        if(reportingVideosAdapter.getItemCount() > 0){
            videosRecyclerView.setVisibility(View.VISIBLE);
        }
        super.onActivityResult(requestCode,resultCode,data);

    }

    //when the voice note button is clicked
    public void onVoiceNoteButtonClicked(View view){
        button  = 3;
        //If permission to record audio has not been granted
        if(ContextCompat.checkSelfPermission(this,Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED){
            String[] permission = {Manifest.permission.RECORD_AUDIO,Manifest.permission.WRITE_EXTERNAL_STORAGE};
            ActivityCompat.requestPermissions(this,permission,PERMISSION_CODE); //request permission
        }else{
            recordAudio();
        }

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

    private void recordAudio(){
        if(reportingAudiosAdapter.getItemCount() < 3){
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
                dialog.show();
                handler.postDelayed(runTimer,1000);//Stops the animation
            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }else{
            CharSequence text = "The maximum number of audio have been selected";
            int duration = Snackbar.LENGTH_SHORT;
            Snackbar snackbar = Snackbar.make(findViewById(R.id.tellTheNews),text,duration);
            snackbar.show();
        }
    }

    public void onCancelRecordingButtonClicked(View view){
        //delete the created audio file
        getContentResolver().delete(audioUri,null,null);
        //release the mediaRecorder resources and close the recording UI
        mediaRecorder.reset();
        mediaRecorder.release();
        mediaRecorder = null;
        seconds = 0;
        dialog.cancel();
        handler.removeCallbacks(runTimer);
    }

    public void onDoneRecordingButtonClicked(View view){
        //release the mediaRecorder resources and close the recording UI
        mediaRecorder.stop();
        mediaRecorder.release();
        mediaRecorder = null;
        seconds = 0;
        dialog.cancel();
        handler.removeCallbacks(runTimer);

        reportingAudiosAdapter.getAudios().add(new MyAudio(audioUri,this));
        reportingAudiosAdapter.notifyDataSetChanged();

        if(reportingAudiosAdapter.getItemCount() > 0){
            audiosRecyclerView.setVisibility(View.VISIBLE);
        }

    }

    //Show time recorded updates
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

    //When the location button is clicked
    public void onLocationButtonClicked(View view){
        button = 4;
        locationTextView.setVisibility(View.VISIBLE);
        //If permission to use location is not granted
        if(ActivityCompat.checkSelfPermission(this,Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            String[] permission = {Manifest.permission.ACCESS_FINE_LOCATION};
            ActivityCompat.requestPermissions(this,permission,PERMISSION_CODE); //request permission
        }else{
           getLocation();
        }
    }

    private void getLocation(){
        locationTextView = (TextView) findViewById(R.id.location);
        //Get the current location
        fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this,
                new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {//When the location is successful
                if(location != null){//if the location is available
                    String coordinates = null;
                    Geocoder geocoder = new Geocoder(getApplicationContext(),Locale.getDefault());
                    try{
                        //Get address of latitude and longitude coordinates using Geocoder
                        List<Address> addresses =
                                geocoder.getFromLocation(location.getLatitude(),location.getLongitude(),1);
                        if(addresses != null && addresses.size() > 0){
                            coordinates = "Current Location: "+addresses.get(0).getLocality() + " "+
                                    " "+addresses.get(0).getCountryName();
                        }else{
                            coordinates = "Unknown";
                        }
                    }catch (IOException e){
                        e.printStackTrace();
                    }
                    locationTextView.setText(coordinates);
                }else{//if location is not available
                    String message = "Your location might be turned off. Please check and turn it on";
                    Toast.makeText(getApplicationContext(),message,Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                e.printStackTrace();
            }
        });
    }



    //Save values to be restored during screen rotation
    @Override
    public void onSaveInstanceState(@Nullable Bundle outState) {
        //Save imagePath states
        imagesPath = new ArrayList<>();
        for (Uri uri : reportingImagesAdapter.getImagesUri()) {
            imagesPath.add(uri.toString());
        }
        outState.putStringArrayList("imagesPath", imagesPath);
        //Save videoPath states
        videosPath = new ArrayList<>();
        for (Uri uri : reportingVideosAdapter.getVideosUri()) {
            videosPath.add(uri.toString());
        }
        outState.putStringArrayList("videosPath", videosPath);
        //Save audioPath states
        audiosPath = new ArrayList<>();
        for(int i =0 ; i < reportingAudiosAdapter.getItemCount(); i++){
            audiosPath.add(reportingAudiosAdapter.getAudios().get(i).getAudioUri().toString());
        }
        outState.putStringArrayList("audiosPath",audiosPath);
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onRestoreInstanceState(@Nullable Bundle savedInstanceState) {
        imagesPath = savedInstanceState.getStringArrayList("imagesPath");
        for(String imagePath : imagesPath){
            reportingImagesAdapter.getImagesUri().add(Uri.parse(imagePath));
        }
        videosPath = savedInstanceState.getStringArrayList("videosPath");
        for(String videoPath : videosPath){
            reportingVideosAdapter.getVideosUri().add(Uri.parse(videoPath));
        }
        audiosPath = savedInstanceState.getStringArrayList("audiosPath");
        for(String audioPath : audiosPath){
            reportingAudiosAdapter.getAudios().add(new MyAudio(Uri.parse(audioPath),this));
        }
        super.onRestoreInstanceState(savedInstanceState);
    }
}
