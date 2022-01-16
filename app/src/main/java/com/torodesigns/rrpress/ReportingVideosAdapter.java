package com.torodesigns.rrpress;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.MediaController;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ReportingVideosAdapter extends RecyclerView.Adapter<ReportingVideosAdapter.ViewHolder> {
    private ArrayList<Uri> videosUri; //Arraylist to hold videosUri
    private Listener listener;

    interface Listener{
        void onClick(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    //default constructor
    public ReportingVideosAdapter(){
        this.videosUri = new ArrayList<>();
    }

    //constructor
    public ReportingVideosAdapter(ArrayList videosUri){
        this.videosUri = videosUri;
    }

    public void setVideosUri(ArrayList<Uri> videosUri) {
        this.videosUri = videosUri;
    }

    public ArrayList<Uri> getVideosUri() {
        return videosUri;
    }

    //Specify the view for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private FrameLayout frameLayout;

        //constructor
        public ViewHolder(FrameLayout frameLayout){
            super(frameLayout);
            this.frameLayout = frameLayout;
        }
    }

    @NonNull
    @Override
    public ReportingVideosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Specify the layout to use
        FrameLayout frameLayout = (FrameLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reporting_videos,parent,false);
        return new ViewHolder(frameLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportingVideosAdapter.ViewHolder holder,final int position) {
        final FrameLayout frameLayout = holder.frameLayout;
        //Set the videoUri
        VideoView capturedVideo = (VideoView) frameLayout.findViewById(R.id.capturedVideo);
        //Creating MediaController and setting MediaController
        MediaController mediaController = new MediaController(frameLayout.getContext());
        mediaController.setAnchorView(capturedVideo);
        capturedVideo.setVideoURI(videosUri.get(position));
        capturedVideo.setMediaController(mediaController);

        Button closeButton = (Button) frameLayout.findViewById(R.id.videoCloseButton);
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(listener != null){
                   listener.onClick(position);
               }
            }
        });

    }

    @Override
    public int getItemCount() {
        return videosUri.size();
    }
}

