package com.torodesigns.rrpress;

import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

public class RRListAdapter extends RecyclerView.Adapter<RRListAdapter.ViewHolder> {

    private List<RR> rrList;
    private boolean isRelatedRR;

    private Listener listener;

    interface Listener{
        void onClick(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    //default constructor
    public RRListAdapter(){
        this.rrList = new ArrayList<>();
        this.isRelatedRR = false;
    }

    //Overloaded Constructors
    public RRListAdapter(List<RR> rrList){
        this.rrList = rrList;
    }

    public RRListAdapter(boolean isRelatedRR){
        this.isRelatedRR = isRelatedRR;
    }

    public RRListAdapter(List<RR> rrList, boolean isRelatedRR){
        this.rrList = rrList;
        this.isRelatedRR = isRelatedRR;
    }


    //Create setters anf getters

    public void setRrList(List<RR> rrList) {
        this.rrList = rrList;
    }

    public List<RR> getRrList() {
        return rrList;
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
    public RRListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //Specify the layout to use
        CardView cardView;
        if(!isRelatedRR){
            cardView = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.rr_list,parent,false);
        }else{
            cardView = (CardView) LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.related_rr_list,parent,false);
        }
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull RRListAdapter.ViewHolder holder, final int position) {
        final CardView cardView = holder.cardView;
        RR rr = rrList.get(position);

        ImageView imageView = (ImageView) cardView.findViewById(R.id.retrieved_image);
        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(),rr.getImageResourceId());
        //Set the rr image
        imageView.setImageDrawable(drawable);
        //Set the rr name
        TextView imageNameTextView = (TextView) cardView.findViewById(R.id.image_name);
        imageNameTextView.setText(rr.getItemName());
        //Set the rr timeElapsed
        TextView timeElapsedTextView = (TextView) cardView.findViewById(R.id.image_timeElapsed);
        timeElapsedTextView.setText(rr.getTimeElapsed());

        //Add listener to cardview
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                if(listener != null){
//                    listener.onClick(position);
//                }
                Intent intent = new Intent(cardView.getContext(),RRItemDetailActivity.class);
                intent.putExtra(RRItemDetailActivity.EXTRA_RRITEM_ID,position);
                cardView.getContext().startActivity(intent);
            }
        });

    }

    //Number of data items
    @Override
    public int getItemCount() {
        return rrList.size();
    }
}
