package com.torodesigns.rrpress;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class CommentsListAdapter extends RecyclerView.Adapter<CommentsListAdapter.ViewHolder> {

    private List<Comment> comments;

    //default constructor
    public CommentsListAdapter(){
        this.comments = new ArrayList<>();
    }

    //constructor
    public CommentsListAdapter(List<Comment> comments){
        this.comments = comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Comment> getComments() {
        return comments;
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
    public CommentsListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //Specify the layout to use
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.comments_list,parent,false);
        return new CommentsListAdapter.ViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsListAdapter.ViewHolder holder, final int position) {
        final LinearLayout linearLayout = holder.linearLayout;

        final Comment comment = comments.get(position);

        TextView nameTextView = (TextView) linearLayout.findViewById(R.id.nameOfCommentator);
        nameTextView.setText(comment.getName());

        TextView dateTextView = (TextView) linearLayout.findViewById(R.id.timeElapsed);
        dateTextView.setText(comment.getTimeElapsed());

        TextView commentTextView = (TextView) linearLayout.findViewById(R.id.comments);
        commentTextView.setText(comment.getMessage());

        final TextView likesTextView = (TextView) linearLayout.findViewById(R.id.commentNumOfLikes);
        likesTextView.setText(String.valueOf(comment.getNumOfLikes()));

        TextView repliesTextView = (TextView) linearLayout.findViewById(R.id.numOfReplies);
        repliesTextView.setText(String.valueOf(comment.getNumberOfReplies()));

        //If comment has been liked, needed for the Comment with replies activity
        if(comment.isLiked()){
            Button likeButton = (Button) linearLayout.findViewById(R.id.commentLikeButton);
            Typeface typeface = ResourcesCompat.getFont(linearLayout.getContext(),R.font.fa_solid);
            likeButton.setTypeface(typeface);
            likeButton.setTextColor(Color.rgb(255,64,129));
        }

        if(comment.getNumberOfReplies() > 0){
            int index = comment.getNumberOfReplies() - 1;
            Comment reply = comment.getReplies().get(index);

            TextView replyNameTextView = (TextView) linearLayout.findViewById(R.id.nameOfReplier);
            replyNameTextView.setText(reply.getName());

            TextView replyTimeElapsedTextView = (TextView) linearLayout.findViewById(R.id.replyTimeElapsed);
            replyTimeElapsedTextView.setText(reply.getTimeElapsed());

            TextView replyTextView = (TextView) linearLayout.findViewById(R.id.reply);
            replyTextView.setText(reply.getMessage());

            TextView replyLikesTextView = (TextView) linearLayout.findViewById(R.id.replyNumOfLikes);
            replyLikesTextView.setText(String.valueOf(reply.getNumOfLikes()));

            TextView repliedTextView = (TextView) linearLayout.findViewById(R.id.replyNumOfReplies);
            repliedTextView.setText(String.valueOf(reply.getNumberOfReplies()));

            //If last reply has been liked, needed for the Comment with replies activity
            if(reply.isLiked()){
                Button likeButton = (Button) linearLayout.findViewById(R.id.replyLikeButton);
                Typeface typeface = ResourcesCompat.getFont(linearLayout.getContext(),R.font.fa_solid);
                likeButton.setTypeface(typeface);
                likeButton.setTextColor(Color.rgb(255,64,129));
            }



        }

        //if replies to comment are more than one, use this view to navigate to them else hide view
        TextView showMoreRepliesButton = (TextView) linearLayout.findViewById(R.id.showMoreReplies);
        if(comment.getNumberOfReplies() < 2 ){
            showMoreRepliesButton.setVisibility(View.GONE);
        }

        //if no replies, hide the lastReplyView
        LinearLayout lastCommentView = (LinearLayout) linearLayout.findViewById(R.id.lastCommentView);
        if(comment.getNumberOfReplies() < 1){
            lastCommentView.setVisibility(View.GONE);
        }

        //When the comment like button is clicked
        final Button commentLikeButton = (Button) linearLayout.findViewById(R.id.commentLikeButton);
        commentLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button commentLikeButton = (Button) linearLayout.findViewById(R.id.commentLikeButton);
                TextView numOfLikes = (TextView) linearLayout.findViewById(R.id.commentNumOfLikes);
                int likes = comment.getNumOfLikes();

                if(!comment.isLiked()){//If it has not been liked, like
                    Typeface typeface = ResourcesCompat.getFont(linearLayout.getContext(),R.font.fa_solid);
                    commentLikeButton.setTypeface(typeface);
                    commentLikeButton.setTextColor(Color.rgb(255,64,129));//Change color to deep pink

                    comment.setNumOfLikes(++likes);
                    numOfLikes.setText(String.valueOf(likes));

                    comment.setLiked(true);
                }else{//if liked, unlike
                    Typeface typeface = ResourcesCompat.getFont(linearLayout.getContext(),R.font.fa_regular);
                    commentLikeButton.setTypeface(typeface);
                    commentLikeButton.setTextColor(Color.rgb(170,170,170));//Change color to grey

                    comment.setNumOfLikes(--likes);
                    numOfLikes.setText(String.valueOf(likes));

                    comment.setLiked(false);
                }
            }
        });

        //When the comment comment button is clicked
        Button commentReplyButton = (Button) linearLayout.findViewById(R.id.commentReplyButton);
        commentReplyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(linearLayout.getContext(),CommentsWithRepliesActivity.class);
                intent.putExtra("comment",comment);
                intent.putExtra("isCommentButtonClicked",true);
                linearLayout.getContext().startActivity(intent);
            }
        });

        //When the reply like button is clicked
        final Button replyLikeButton = (Button) linearLayout.findViewById(R.id.replyLikeButton);
        replyLikeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Button replyLikeButton = (Button) linearLayout.findViewById(R.id.replyLikeButton);
                TextView numOfLikes = (TextView) linearLayout.findViewById(R.id.replyNumOfLikes);
                int index = comment.getNumberOfReplies() - 1;
                Comment reply = comment.getReplies().get(index);
                int likes = reply.getNumOfLikes();

                if(!reply.isLiked()){//If it has not been liked, like
                    Typeface typeface = ResourcesCompat.getFont(linearLayout.getContext(),R.font.fa_solid);
                    replyLikeButton.setTypeface(typeface);
                    replyLikeButton.setTextColor(Color.rgb(255,64,129));//Change color to deep pink

                    reply.setNumOfLikes(++likes);
                    numOfLikes.setText(String.valueOf(likes));

                    reply.setLiked(true);
                }else{//if liked, unlike
                    Typeface typeface = ResourcesCompat.getFont(linearLayout.getContext(),R.font.fa_regular);
                    replyLikeButton.setTypeface(typeface);
                    replyLikeButton.setTextColor(Color.rgb(170,170,170));//Change color to grey

                    reply.setNumOfLikes(--likes);
                    numOfLikes.setText(String.valueOf(likes));

                    reply.setLiked(false);
                }
            }
        });

        //When a comment is clicked
        final LinearLayout commentView = (LinearLayout) linearLayout.findViewById(R.id.commentView);
        commentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(linearLayout.getContext(),CommentsWithRepliesActivity.class);
                intent.putExtra("comment",comment);
                intent.putExtra("isCommentButtonClicked",false);
                linearLayout.getContext().startActivity(intent);
            }
        });

        //When show previous replies is clicked
       showMoreRepliesButton.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               Intent intent = new Intent(linearLayout.getContext(),CommentsWithRepliesActivity.class);
               intent.putExtra("comment",comment);
               intent.putExtra("isCommentButtonClicked",false);
               linearLayout.getContext().startActivity(intent);
           }
       });

        //When lastReply is clicked
        LinearLayout lastReplyView = (LinearLayout) linearLayout.findViewById(R.id.lastCommentView);
        lastCommentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = comment.getNumberOfReplies() - 1;
                Comment reply = comment.getReplies().get(index);
                Intent intent = new Intent(linearLayout.getContext(),CommentsWithRepliesActivity.class);
                intent.putExtra("comment",reply);
                intent.putExtra("isCommentButtonClicked",false);
                linearLayout.getContext().startActivity(intent);
            }
        });

        //When lastReply reply button is clicked
        Button lastReplyCommentButton = (Button) linearLayout.findViewById(R.id.replyButton);
        lastReplyCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int index = comment.getNumberOfReplies() - 1;
                Comment reply = comment.getReplies().get(index);
                Intent intent = new Intent(linearLayout.getContext(),CommentsWithRepliesActivity.class);
                intent.putExtra("comment",reply);
                intent.putExtra("isCommentButtonClicked",true);
                linearLayout.getContext().startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return comments.size();
    }
}
