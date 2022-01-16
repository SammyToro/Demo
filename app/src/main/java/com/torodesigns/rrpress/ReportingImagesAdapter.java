package com.torodesigns.rrpress;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ReportingImagesAdapter extends RecyclerView.Adapter<ReportingImagesAdapter.ViewHolder> {
    private ArrayList<Uri> imagesUri; //Arraylist to hold imagesUri
    private Listener listener;

    interface Listener{
        void onClick(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    //default constructor
    public ReportingImagesAdapter(){
        this.imagesUri = new ArrayList<>();
    }

    //constructor
    public ReportingImagesAdapter(ArrayList imagesUri){
        this.imagesUri = imagesUri;
    }

    public void setImagesUri(ArrayList<Uri> imagesUri) {
        this.imagesUri = imagesUri;
    }

    public ArrayList<Uri> getImagesUri() {
        return imagesUri;
    }

    //Specify the view for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView;

        //constructor
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
        }
    }

    @NonNull
    @Override
    public ReportingImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Specify the layout to use
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.reporting_images,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportingImagesAdapter.ViewHolder holder,final int position) {
        final CardView cardView = holder.cardView;
        //Set the videoUri
        ImageView capturedImage = (ImageView) cardView.findViewById(R.id.capturedImage);
        capturedImage.setImageURI(imagesUri.get(position));

        Button closeButton = (Button) cardView.findViewById(R.id.imageCloseButton);
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
        return imagesUri.size();
    }
}
