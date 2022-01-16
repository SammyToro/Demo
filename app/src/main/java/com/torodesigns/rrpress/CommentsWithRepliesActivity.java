package com.torodesigns.rrpress;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class CommentsWithRepliesActivity extends AppCompatActivity {
    private EditText commentText;
    private Comment comment;
    private boolean hasNoReplies = true;
    private RecyclerView commentsRecyclerView;
    private CommentsListAdapter commentsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comments_with_replies);

        //set the toolbar as the activities app bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //enable back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        //Get comment from intent
        comment = (Comment) getIntent().getSerializableExtra("comment");

        boolean isCommentButtonClicked = (Boolean) getIntent().getExtras().get("isCommentButtonClicked");
        if(isCommentButtonClicked){
            commentText = (EditText)findViewById(R.id.replyText);
            commentText.requestFocus();// Request Focus
            //Shows the keyboard
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.showSoftInput(commentText,InputMethodManager.SHOW_IMPLICIT);
        }

        TextView nameOfCommentator = (TextView) findViewById(R.id.nameOfCommentator);
        nameOfCommentator.setText(comment.getName());

        TextView timeElasped = (TextView) findViewById(R.id.timeElapsed);
        timeElasped.setText(comment.getTimeElapsed());

        TextView commentMessage = (TextView) findViewById(R.id.comments);
        commentMessage.setText(comment.getMessage());

        TextView numOfLikes = (TextView) findViewById(R.id.commentNumOfLikes);
        numOfLikes.setText(String.valueOf(comment.getNumOfLikes()));

        TextView numOfComments = (TextView) findViewById(R.id.numOfReplies);
        numOfComments.setText(String.valueOf(comment.getNumberOfReplies()));

        //If it has been liked
        if(comment.isLiked()){
            Button likeButton = (Button) findViewById(R.id.commentLikeButton);
            Typeface typeface = ResourcesCompat.getFont(this,R.font.fa_solid);
            likeButton.setTypeface(typeface);
            likeButton.setTextColor(Color.rgb(255,64,129));
        }

        //RecyclerView to hold comments
        commentsRecyclerView = (RecyclerView) findViewById(R.id.commentRecyclerView);
        if(comment.getNumberOfReplies() > 0){
            commentsListAdapter = new CommentsListAdapter(comment.getReplies());
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

    public void onCommentLikeButtonCliked(View view){
        Button likedButton = (Button) findViewById(R.id.commentLikeButton);
        TextView numOfLikes = (TextView) findViewById(R.id.commentNumOfLikes);
        int likes = comment.getNumOfLikes();

        if(!comment.isLiked()){//If it has not been liked, like
            Typeface typeface = ResourcesCompat.getFont(this,R.font.fa_solid);
            likedButton.setTypeface(typeface);
            likedButton.setTextColor(Color.rgb(255,64,129));

            comment.setNumOfLikes(++likes);
            numOfLikes.setText(String.valueOf(likes));

            comment.setLiked(true);
        }else{//if liked, unlike
            Typeface typeface = ResourcesCompat.getFont(this,R.font.fa_regular);
            likedButton.setTypeface(typeface);
            likedButton.setTextColor(Color.rgb(170,170,170));

            comment.setNumOfLikes(--likes);
            numOfLikes.setText(String.valueOf(likes));

            comment.setLiked(false);
        }
    }

    public void onCommentReplyButtonClicked(View view){
        commentText = (EditText)findViewById(R.id.replyText);
        commentText.requestFocus();// Request Focus
        //Shows the keyboard
        InputMethodManager inputMethodManager =
                (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.showSoftInput(commentText,InputMethodManager.SHOW_IMPLICIT);
    }

    public void onCommentSendButton(View view){
        commentText = (EditText) findViewById(R.id.replyText);
        if(commentText.getText().length() > 0){
            //create comment
            Comment reply = new Comment("Sammy","1 sec",3,commentText.getText().toString());
            if(hasNoReplies){
                commentsListAdapter.setComments(comment.getReplies());
            }
            comment.getReplies().add(reply);
            //Make way for new comment to be entered
            commentText.getText().clear();
            commentText.clearFocus();
            commentsListAdapter.notifyDataSetChanged(); //update view when comment is added
            //Removes the keyboard
            InputMethodManager inputMethodManager =
                    (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            inputMethodManager.hideSoftInputFromWindow(commentText.getWindowToken(),0);

            TextView numOfComments = (TextView) findViewById(R.id.numOfReplies);
            numOfComments.setText(String.valueOf(comment.getNumberOfReplies()));
        }else{
            Toast.makeText(this,"No comment entered. Please enter your comment",Toast.LENGTH_SHORT).show();
        }
    }
}
