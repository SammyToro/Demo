package com.torodesigns.rrpress;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class TopStoriesFragment extends Fragment {



    public TopStoriesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Set the layout
        View view = inflater
                .inflate(R.layout.fragment_top_stories,container,false);

        RecyclerView topStoriesRecyclerView = (RecyclerView) view.findViewById(R.id.news_recycler);

        //The data items
        String[] newHeadlines = {getContext().getString(R.string.news_headline),
                getContext().getString(R.string.news_headline),
                getContext().getString(R.string.news_headline)};
        int[] newImageIds = {R.drawable.buhari,R.drawable.obama,R.drawable.sleeping_man};

        //
        CaptionedImagesAdapter adapter = new CaptionedImagesAdapter(newHeadlines,newImageIds);
        topStoriesRecyclerView.setAdapter(adapter);
        GridLayoutManager gridLayoutManager =
                new GridLayoutManager(getActivity(),1,GridLayoutManager.HORIZONTAL,false);
        topStoriesRecyclerView.setLayoutManager(gridLayoutManager);
        //Implement the listener interface for topstories adapater
        adapter.setListener(new CaptionedImagesAdapter.Listener() {
            @Override
            public void onClick(int postion) {
                Toast toast = Toast.makeText(getContext(),"Clicked",Toast.LENGTH_SHORT);
                toast.show();
            }
        });


        RecyclerView newsListRecyclerView = (RecyclerView) view.findViewById(R.id.news_list_recycler);

        //The news data
        News news1 = new News("Enoy App Soon To Be Released",
                R.drawable.buhari,"Politics",3,6,"4 days ago");
        News news2 = new News("Man Sleeps Till Death",
                R.drawable.sleeping_man,"Entertainment",10,15,"3 hours ago");
        News news3 = new News("Obama Resigns as US President",R.drawable.obama,
                "Trivial",30,100,"2 mins ago");


        News[] newsList = {news1,news2,news3,news2,news3,news1,news2};
        //Populate the newslist recylcerview with linear layout
        NewsListAdapter newsListAdapter = new NewsListAdapter(newsList);
        newsListRecyclerView.setAdapter(newsListAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        newsListRecyclerView.setLayoutManager(linearLayoutManager);

        News.newsList = new ArrayList<>(Arrays.asList(newsList));
        

        //Implement the listener interface in the NewsListAdapter
        newsListAdapter.setListener(new NewsListAdapter.Listener() {
            @Override
            public void onClick(int position) {
                Intent intent = new Intent(getActivity(),NewsDetailActivity.class);
                intent.putExtra(NewsDetailActivity.EXTRA_NEWS_ID,position);
                getActivity().startActivity(intent);
            }
        });


        return view;
    }


}
