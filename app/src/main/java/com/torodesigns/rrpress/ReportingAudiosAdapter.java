package com.torodesigns.rrpress;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ReportingAudiosAdapter extends RecyclerView.Adapter<ReportingAudiosAdapter.ViewHolder> {
    private ArrayList<MyAudio> audios; //Arraylist to hold audios
    private Listener listener;

    interface Listener{
        void onClick(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    //default constructor
    public ReportingAudiosAdapter(){
        this.audios = new ArrayList<>();
    }

    //constructor
    public ReportingAudiosAdapter(ArrayList<MyAudio> audios){
        this.audios = audios;
    }

    public void setAudios(ArrayList<MyAudio> audios) {
        this.audios = audios;
    }

    public ArrayList<MyAudio> getAudios() {
        return audios;
    }

    //Specify the view for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout linearLayout;

        //constructor
        public ViewHolder(LinearLayout linearLayout){
            super(linearLayout);
            this.linearLayout = linearLayout;
        }
    }

    @NonNull
    @Override
    public ReportingAudiosAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Specify the layout to use
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reporting_audios,parent,false);
        return new ViewHolder(linearLayout);
    }

    //Binds data from the audio class to the view
    @Override
    public void onBindViewHolder(@NonNull ReportingAudiosAdapter.ViewHolder holder,final int position) {
        final LinearLayout linearLayout = holder.linearLayout;


        Button audioPlayButton = (Button) linearLayout.findViewById(R.id.audioPlayButton);


        TextView audioNameTextView = (TextView) linearLayout.findViewById(R.id.audioName);
        audioNameTextView.setText(audios.get(position).getAudioName());
        TextView songTimeTextView = (TextView) linearLayout.findViewById(R.id.songTime);
        songTimeTextView.setText(audios.get(position).getSongDuration());

        audioPlayButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(position);
                }

            }
        });
    }

    @Override
    public int getItemCount() {
        return audios.size();
    }
}

