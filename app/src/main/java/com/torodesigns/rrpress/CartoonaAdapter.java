package com.torodesigns.rrpress;

import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

public class CartoonaAdapter extends RecyclerView.Adapter<CartoonaAdapter.ViewHolder> {

    private Cartoona[] cartoonas;
    private Listener listener;



    interface Listener{
        void onCommentButtonClicked(int position);
        void onShareButtonClicked(int position);
        void onCartoonaItemClicked(int position);
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    //constructor
     public CartoonaAdapter(Cartoona[] cartoonas){
         this.cartoonas = cartoonas;
     }


    public static class ViewHolder extends RecyclerView.ViewHolder{
        private LinearLayout linearLayout; //Display LinearLayout

        //constructor
        public ViewHolder(LinearLayout linearLayout){
            super(linearLayout);
            this.linearLayout = linearLayout;
        }
    }

    @NonNull
    @Override
    public CartoonaAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //Specify the layout to use
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.cartoona_list,parent,false);
        return new ViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull CartoonaAdapter.ViewHolder viewHolder, final int position) {
         final LinearLayout linearLayout = viewHolder.linearLayout;

         //Bind the headline data
        TextView headline = (TextView) linearLayout.findViewById(R.id.cartoonaHeadline);
        headline.setText(cartoonas[position].getHeadline());

        //Bind the date created data
        TextView dateCreated = (TextView) linearLayout.findViewById(R.id.cartoonaTimeCreated);
        dateCreated.setText(cartoonas[position].getDateCreated());

        //Bind the cartoon image data
        ImageView cartoonaImage = (ImageView) linearLayout.findViewById(R.id.cartoona_image);
        Drawable drawable = ContextCompat.getDrawable(linearLayout.getContext(),cartoonas[position].getCartoonaImageId());
        cartoonaImage.setImageDrawable(drawable);

        //Bind the number of likes data
        TextView numOfLikes = (TextView) linearLayout.findViewById(R.id.cartoonaNumOfLikes);
        numOfLikes.setText(String.valueOf(cartoonas[position].getNumOfLikes()));

        //Bind the number of comments data
        TextView numOfComments = (TextView) linearLayout.findViewById(R.id.cartoonaNumofComments);
        numOfComments.setText(String.valueOf(cartoonas[position].getNumOfComments()));

        //When the like button is clicked
        final Button likeButton = (Button) linearLayout.findViewById(R.id.cartoonaLikeButton);
        likeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button likedButton = (Button) linearLayout.findViewById(R.id.cartoonaLikeButton);
                TextView numOfLikes = (TextView) linearLayout.findViewById(R.id.cartoonaNumOfLikes);
                int likes = cartoonas[position].getNumOfLikes();

                if(!cartoonas[position].isLiked()){//If it has not been liked, like
                    Typeface typeface = ResourcesCompat.getFont(linearLayout.getContext(),R.font.fa_solid);
                    likedButton.setTypeface(typeface);
                    likedButton.setTextColor(Color.rgb(255,64,129));

                    cartoonas[position].setNumOfLikes(++likes);
                    numOfLikes.setText(String.valueOf(likes));

                    cartoonas[position].setLiked(true);
                }else{//if liked, unlike
                    Typeface typeface = ResourcesCompat.getFont(linearLayout.getContext(),R.font.fa_regular);
                    likedButton.setTypeface(typeface);
                    likedButton.setTextColor(Color.rgb(170,170,170));

                    cartoonas[position].setNumOfLikes(--likes);
                    numOfLikes.setText(String.valueOf(likes));

                    cartoonas[position].setLiked(false);
                }
            }
        });

        Button commentButton = (Button) linearLayout.findViewById(R.id.cartoonaCommentButton);
        commentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onCommentButtonClicked(position);
                }
            }
        });

        Button shareButton = (Button) linearLayout.findViewById(R.id.cartoonaShareButton);
        shareButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onShareButtonClicked(position);
                }
            }
        });

        LinearLayout layout = (LinearLayout) linearLayout.findViewById(R.id.cartoonaItem);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(listener != null){
                    listener.onCartoonaItemClicked(position);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return cartoonas.length;
    }
}
