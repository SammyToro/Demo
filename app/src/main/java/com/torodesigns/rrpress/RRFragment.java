package com.torodesigns.rrpress;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;

import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class RRFragment extends Fragment {


    public RRFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View  view = inflater.inflate(R.layout.fragment_rr, container, false);

        RecyclerView rrRecyclerView = (RecyclerView) view.findViewById(R.id.rrRecyclerView);

        //Create obejcts
        RR rr = new RR("Retrieved Mobile Phone","1 day ago",R.drawable.iphone_6);
        RR rr1 = new RR("Retrieved Ladies Bag","8 hours ago",R.drawable.ladies_bag);
        RR rr2 = new RR("Retrieved Mens Wallet","30 min ago",R.drawable.mens_wallet);
        RR rr3 = new RR("Retrieved Laptop Bag","20 secs ago",R.drawable.laptop_bag);

        RR[] rrList = {rr,rr1,rr2,rr3,rr,rr1,rr2,rr3};
        RR.rrList = rrList;

        RRListAdapter rrListAdapter = new RRListAdapter(Arrays.asList(rrList));
        rrRecyclerView.setAdapter(rrListAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(),2);
        rrRecyclerView.setLayoutManager(gridLayoutManager);

        return view;
    }

}
