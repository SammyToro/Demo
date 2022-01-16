package com.torodesigns.rrpress;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;

import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class CartoonaFragment extends Fragment {


    public CartoonaFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View view =  inflater.inflate(R.layout.fragment_cartoona, container, false);

        final RecyclerView cartoonaRecyclerView = (RecyclerView) view.findViewById(R.id.cartoonaRecyclerView);

        Cartoona cartoona1 = new Cartoona("Tom and Jerry: My Childhood SuperHeroes",
                "2 days ago",R.drawable.tom_jerry,2);
        Cartoona cartoona2 = new Cartoona("Hawa Koomson: The Female James Bond",
                "2 mins ago",R.drawable.maxres,12);
        Cartoona cartoona3 = new Cartoona("Black Lives Matter: I can't breathe",
                "2 hours ago",R.drawable.scobby_doo,7);
        Cartoona cartoona4 = new Cartoona("Mahama choose a female",
                "30 mins ago",R.drawable.sleeping_man,0);

        Comment comment1 = new Comment("Sammy","2 days ago",3,"Good job");
        Comment comment2 = new Comment("Toro","5 hours ago",2,"Congrats bro");
        Comment comment3 = new Comment("Slykid","2 mins ago",0,"No comments");

        Comment comment4 = new Comment("Jerry","1 hour ago,",4,"You got nerves");
        Comment comment5 = new Comment("Puss","45 mins ago",2,"Well done bro");
        Comment reply1 = new Comment("Tom","3O mins ago",0,"Y3nk) heblews");

        cartoona1.getComments().add(comment1);
        cartoona1.getComments().add(comment2);
        cartoona1.getComments().add(comment3);

        cartoona2.getComments().add(comment4);
        cartoona2.getComments().add(comment5);
        cartoona2.getComments().get(0).getReplies().add(reply1);
        cartoona2.getComments().get(0).getReplies().add(comment3);
        cartoona2.getComments().get(1).getReplies().add(reply1);
        //cartoona2.getComments().get(1).getReplies().add(comment3);



        final Cartoona[] cartoonas = {cartoona1,cartoona2,cartoona3,cartoona4};

        Cartoona.cartoonas = new ArrayList<>(Arrays.asList(cartoonas));

        CartoonaAdapter cartoonaAdapter = new CartoonaAdapter(cartoonas);
        cartoonaRecyclerView.setAdapter(cartoonaAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),
                LinearLayoutManager.VERTICAL,false);
        cartoonaRecyclerView.setLayoutManager(linearLayoutManager);

        cartoonaAdapter.setListener(new CartoonaAdapter.Listener() {

            @Override
            public void onCommentButtonClicked(int position) {
                Intent intent = new Intent(getActivity(),CartoonaWithCommentsActivity.class);
                intent.putExtra(CartoonaWithCommentsActivity.EXTRA_CARTOONA_ID,position);
                intent.putExtra("isCommentButtonClicked",true);
                getActivity().startActivity(intent);
            }

            @Override
            public void onShareButtonClicked(int position) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("text/plain");
                intent.putExtra(Intent.EXTRA_TEXT,"Link will be available soon");
                Intent chooserIntent = Intent.createChooser(intent,"Share news");
                startActivity(chooserIntent);
            }

            @Override
            public void onCartoonaItemClicked(int position) {
                Intent intent = new Intent(getActivity(),CartoonaWithCommentsActivity.class);
                intent.putExtra(CartoonaWithCommentsActivity.EXTRA_CARTOONA_ID,position);
                intent.putExtra("isCommentButtonClicked",false);
                getActivity().startActivity(intent);
            }
        });

        return view;
    }
}
