package com.torodesigns.rrpress;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class NewsDetailActivity extends AppCompatActivity {
    public static final String EXTRA_NEWS_ID = "newsId"; //Pass the news Id to intent
    private boolean isLiked = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news_detail);

        //set the toolbar as the activities app bar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //enable back button
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);



        //Display the details of the news
        int newsId = (Integer) getIntent().getExtras().get(EXTRA_NEWS_ID);
        Log.v("News Id",String.valueOf(newsId));
        //Set the headline
        TextView textViewNewsHeadline = (TextView) findViewById(R.id.news_headline);
        textViewNewsHeadline.setText(News.newsList.get(newsId).getNewsHeadLine());
        //Set the category
        TextView textViewNewsCategory = (TextView) findViewById(R.id.news_category);
        textViewNewsCategory.setText(News.newsList.get(newsId).getNewsCategory());
        //Set the time elapsed
        TextView textViewTimeElapsed = (TextView) findViewById(R.id.timeElapsed);
        textViewTimeElapsed.setText(News.newsList.get(newsId).getTimeElapsed());
        //Set the news image
        ImageView imageNews = (ImageView) findViewById(R.id.news_image);
        Drawable drawable = ContextCompat.getDrawable(this,News.newsList.get(newsId).getNewsPicId());
        imageNews.setImageDrawable(drawable);
        //Set the author image
        //Set the author name
//        //Set the news detail
//        TextView textViewNewsDetail = (TextView) findViewById(R.id.news_details);
//        textViewNewsDetail.setText(News.newsList.get(newsId).getNewsDetails());
        //Set the number of likes
        TextView textViewLikes = (TextView) findViewById(R.id.likes);
        textViewLikes.setText(News.newsList.get(newsId).getLikes());

        //The data for related new list
        //The news data
        News news1 = new News("Enoy App Soon To Be Released",
                R.drawable.buhari,"Politics",3,6,"4 days ago");
        News news2 = new News("Man Sleeps Till Death",
                R.drawable.sleeping_man,"Entertainment",10,15,"3 hours ago");
        News news3 = new News("Obama Resigns as US President",R.drawable.obama,
                "Trivial",30,100,"2 mins ago");

        News[] relatedNewsList = {news2,news3,news1};

        //Populate the recyclerview
        RecyclerView relatedNewsListRecyclerView = (RecyclerView) findViewById(R.id.related_news_recycler);
        RelatedNewsListAdapter relatedNewsListAdapter = new RelatedNewsListAdapter(relatedNewsList);
        relatedNewsListRecyclerView.setAdapter(relatedNewsListAdapter);
        //Set the recyclerview layout
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        relatedNewsListRecyclerView.setLayoutManager(linearLayoutManager);

        //Implement the listener interface in the NewsListAdapter
        relatedNewsListAdapter.setListener(new RelatedNewsListAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Intent intent = getIntent();
                intent.putExtra(NewsDetailActivity.EXTRA_NEWS_ID,position);
                startActivity(intent);
            }
        });

    }

    //if like button is clicked
    public void onLikeCliked(View view){
        int newsId = (Integer) getIntent().getExtras().get(EXTRA_NEWS_ID);
        Button likedButton = (Button) findViewById(R.id.likeButton);
        TextView textViewLikes = (TextView) findViewById(R.id.likes);
        int numOfLikes = Integer.parseInt(News.newsList.get(newsId).getLikes());
        if(!isLiked){ //change like colour to blue and increase the number of likes
            isLiked = true;
            likedButton.setTextColor(Color.BLUE);
            numOfLikes++;
            textViewLikes.setText(String.valueOf(numOfLikes));
        }else{ //change the like color to default and decrease the number of likes
            isLiked = false;
            likedButton.setTextColor(Color.BLACK);
            numOfLikes--;
            textViewLikes.setText(String.valueOf(numOfLikes));
        }
        News.newsList.get(newsId).setLikes(numOfLikes);
    }

    public void onShareClicked(View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Link will be available soon");
        Intent chooserIntent = Intent.createChooser(intent,"Share news");
        startActivity(chooserIntent);
    }
}
