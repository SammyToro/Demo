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

import java.util.Arrays;

public class RelatedNewsListAdapter extends RecyclerView.Adapter<RelatedNewsListAdapter.ViewHolder> {

    private News[] newsList; //Contains list of news
    private Listener listener;

    interface Listener{ //Interface to listen for click events
        void onClick(int position);
    }

    public News[] getNewsList() {
        return newsList;
    }

    //Set the listener
    public void setListener(Listener listener) {
        this.listener = listener;
    }

    //constructor
    public RelatedNewsListAdapter(News[] newList){
        this.newsList = Arrays.copyOf(newList,newList.length);
    }
    //Specifies the view for each data item
    public static class ViewHolder extends RecyclerView.ViewHolder{
        private CardView cardView; //Display CardView

        //constructor
        public ViewHolder(CardView cardView){
            super(cardView);
            this.cardView = cardView;
        }

        public CardView getCardView() {
            return cardView;
        }
    }
    @NonNull
    @Override
    public RelatedNewsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Specify the layout to use
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.related_list_of_news,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedNewsListAdapter.ViewHolder holder,final int position) {
        CardView cardView = holder.cardView;
        ImageView imageView = (ImageView) cardView.findViewById(R.id.newListImage);
        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(),newsList[position].getNewsPicId());
        //Set the news image
        imageView.setImageDrawable(drawable);
        //Set the news headline
        TextView textView = (TextView) cardView.findViewById(R.id.news_headline);
        textView.setText(newsList[position].getNewsHeadLine());
        //Set the time elapsed
        TextView timeTextView = (TextView) cardView.findViewById(R.id.timeElapsed);
        timeTextView.setText(newsList[position].getTimeElapsed());
        //Add listener to the cardview
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
        return newsList.length;
    }
}
