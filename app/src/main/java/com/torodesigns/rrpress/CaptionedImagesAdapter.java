package com.torodesigns.rrpress;

import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

public class CaptionedImagesAdapter extends RecyclerView.Adapter<CaptionedImagesAdapter.ViewHolder> {

    private String[] newsHeadlines;//Contain the news headlines
    private int[] imageIds;//Contain the news images
    private Listener listener;

    interface Listener{
        void onClick(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    //constructor
    public CaptionedImagesAdapter(String[] newsHeadlines,int[] imageIds){
        this.newsHeadlines = newsHeadlines;
        this.imageIds = imageIds;
    }
    //Specifies the view for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView; //Display CardView

        //constructor
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
        }

    }
    @NonNull
    @Override
    public CaptionedImagesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Specify the layout to use
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_captioned_image,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull CaptionedImagesAdapter.ViewHolder holder, final int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = (ImageView) cardView.findViewById(R.id.news_image);
        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(),imageIds[position]);
        //Set the news image
        imageView.setImageDrawable(drawable);
        //Set the news headline
        TextView textView = (TextView) cardView.findViewById(R.id.news_headline);
        textView.setText(newsHeadlines[position]);
        //Add listener to cardview
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onClick(position);
                }
            }
        });

    }

    //Number of data items
    @Override
    public int getItemCount() {
        return newsHeadlines.length;
    }
}
