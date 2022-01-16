package com.torodesigns.rrpress;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class CartoonaWithCommentsActivity extends AppCompatActivity {
    public static final String EXTRA_CARTOONA_ID = "cartoonaId"; //Pass the cartoona_Id to intent
    private Cartoona cartoona = null;
    private EditText commentText;
    private boolean hasNoReplies = true;
    private RecyclerView commentsRecyclerView;
    private CommentsListAdapter commentsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cartoona_with_comments);

        //set the toolbar as the activities app bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //enable back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {//When the back icon is clicked
            @Override
            public void onClick(View v) {
                commentText = (EditText)findViewById(R.id.cartoonaComment);
                if(commentText.hasFocus()){//If the editText has focus
                    commentText.clearFocus();// Remove focus
                    commentText.getText().clear(); //Remove any text if any

                    //Hide the keyboard
                    InputMethodManager inputMethodManager =
                            (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(commentText.getWindowToken(),0);
                }else{
                    Intent intent = new Intent(CartoonaWithCommentsActivity.this,MainActivity.class);
                    intent.putExtra("fragment_index_key",1);
                    startActivity(intent);
                }
            }
        });

        //Get the Cartoona position in the list
        int position = (Integer) getIntent().getExtras().get(EXTRA_CARTOONA_ID);
        cartoona = Cartoona.cartoonas.get(position);

        boolean isCommentButtonClicked = (Boolean) getIntent().getExtras().get("isCommentButtonClicked");
        if(isCommentButtonClicked){
            commentText = (EditText)findViewById(R.id.cartoonaComment);
            commentText.requestFocus();// Request Focus
            //Shows the keyboard
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(commentText,InputMethodManager.SHOW_IMPLICIT);
        }

        //Set the headline data
        TextView headline = (TextView) findViewById(R.id.cartoonaHeadline);
        headline.setText(cartoona.getHeadline());

        //Set the date created data
        TextView dateCreated = (TextView) findViewById(R.id.cartoonaTimeCreated);
        dateCreated.setText(cartoona.getDateCreated());

        //Set the cartoon image data
        ImageView cartoonaImage = (ImageView) findViewById(R.id.cartoona_image);
        Drawable drawable = ContextCompat.getDrawable(this,cartoona.getCartoonaImageId());
        cartoonaImage.setImageDrawable(drawable);

        //Set the number of likes data
        TextView numOfLikes = (TextView) findViewById(R.id.cartoonaNumOfLikes);
        numOfLikes.setText(String.valueOf(cartoona.getNumOfLikes()));

        //Set the number of comments data
        TextView numOfComments = (TextView) findViewById(R.id.cartoonaNumofComments);
        numOfComments.setText(String.valueOf(cartoona.getNumOfComments()));

        //If it has been liked
        if(cartoona.isLiked()){
            Button likeButton = (Button) findViewById(R.id.cartoonaLikeButton);
            Typeface typeface = ResourcesCompat.getFont(this,R.font.fa_solid);
            likeButton.setTypeface(typeface);
            likeButton.setTextColor(Color.rgb(255,64,129));
        }

        //RecyclerView to hold comments
        commentsRecyclerView = (RecyclerView) findViewById(R.id.commentRecyclerView);
        if(cartoona.getNumOfComments() > 0){
            commentsListAdapter = new CommentsListAdapter(cartoona.getComments());
            hasNoReplies = false;
        }else{
            commentsListAdapter = new CommentsListAdapter();
        }
        commentsRecyclerView.setAdapter(commentsListAdapter);
        //Set the layout for the recyclerView
        LinearLayoutManager linearLayoutManager = new
                LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        commentsRecyclerView.setLayoutManager(linearLayoutManager);


    }

    public void onCartoonaLikeButtonClicked(View view){
        Button likedButton = (Button) findViewById(R.id.cartoonaLikeButton);
        TextView numOfLikes = (TextView) findViewById(R.id.cartoonaNumOfLikes);
        int likes = cartoona.getNumOfLikes();

        if(!cartoona.isLiked()){//If it has not been liked, like
            Typeface typeface = ResourcesCompat.getFont(this,R.font.fa_solid);
            likedButton.setTypeface(typeface);
            likedButton.setTextColor(Color.rgb(255,64,129));

            cartoona.setNumOfLikes(++likes);
            numOfLikes.setText(String.valueOf(likes));

            cartoona.setLiked(true);
        }else{//if liked, unlike
            Typeface typeface = ResourcesCompat.getFont(this,R.font.fa_regular);
            likedButton.setTypeface(typeface);
            likedButton.setTextColor(Color.rgb(170,170,170));

            cartoona.setNumOfLikes(--likes);
            numOfLikes.setText(String.valueOf(likes));

            cartoona.setLiked(false);
        }
    }

    public void onCartoonaCommentButtonClicked(View view){
        commentText = (EditText)findViewById(R.id.cartoonaComment);
        commentText.requestFocus();// Request Focus
        //Shows the keyboard
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(commentText,InputMethodManager.SHOW_IMPLICIT);

    }

    public void onCartoonaShareButtonClicked(View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Link will be available soon");
        Intent chooserIntent = Intent.createChooser(intent,"Share news");
        startActivity(chooserIntent);
    }

    public void onCartoonaCommentReplyButtonClicked(View view){
        commentText = (EditText) findViewById(R.id.cartoonaComment);
        if(commentText.getText().length() > 0){
            //create comment
            Comment comment = new Comment("Sammy","1 sec",3,commentText.getText().toString());
            if(hasNoReplies){
                commentsListAdapter.setComments(cartoona.getComments());
            }
            cartoona.getComments().add(comment); //add comment to cartoona item
            //Make way for new comment to be entered
            commentText.getText().clear();
            commentText.clearFocus();
            commentsListAdapter.notifyDataSetChanged(); //update view when comment is added
            //Removes the keyboard
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(commentText.getWindowToken(),0);

            TextView numOfComments = (TextView) findViewById(R.id.cartoonaNumofComments);
            numOfComments.setText(String.valueOf(cartoona.getNumOfComments()));
        }else{
            Toast.makeText(this,"No comment entered. Please enter your comment",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        commentText = (EditText)findViewById(R.id.cartoonaComment);
        if(commentText.hasFocus()){//If the editText has focus
            commentText.clearFocus();// Remove focus

        }else{
            super.onBackPressed();
        }
    }
}
