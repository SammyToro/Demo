package com.torodesigns.rrpress;

import android.view.LayoutInflater;

import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;


public class NewsListAdapter extends RelatedNewsListAdapter {

    //constructor
    public NewsListAdapter(News[] newList){
        super(newList);
    }

    @NonNull
    @Override
    public NewsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Specify the layout to use
        CardView cardView = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_of_news,parent,false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull RelatedNewsListAdapter.ViewHolder holder,final int position) {
        super.onBindViewHolder(holder,position);
        CardView cardView = holder.getCardView();
        //Set the new category
        TextView categoryTextView = (TextView) cardView.findViewById(R.id.news_category);
        categoryTextView.setText(super.getNewsList()[position].getNewsCategory());
        //Set the number of likes
        TextView likesTextView = (TextView) cardView.findViewById(R.id.likes);
        likesTextView.setText(super.getNewsList()[position].getLikes());
        //Set the number of view
        TextView viewsTextView = (TextView) cardView.findViewById(R.id.views);
        viewsTextView.setText(super.getNewsList()[position].getViews());
    }
}
