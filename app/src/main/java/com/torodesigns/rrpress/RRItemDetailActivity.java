package com.torodesigns.rrpress;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Arrays;

public class RRItemDetailActivity extends AppCompatActivity {
    public static final String EXTRA_RRITEM_ID = "rrItemId"; //Pass the rrItem Id to intent

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rritem_detail);

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


        //Display the details of the news
        int rrItemId = (Integer) getIntent().getExtras().get(EXTRA_RRITEM_ID);
        RR rrItem = RR.rrList[rrItemId];

        TextView rrHeadline = (TextView) findViewById(R.id.rrHeadline);
        rrHeadline.setText(rrItem.getItemName());

        TextView rrTimeElapsed = (TextView) findViewById(R.id.rrTimeElapsed);
        rrTimeElapsed.setText(rrItem.getTimeElapsed());

        ImageView rrImage  = (ImageView) findViewById(R.id.rrImage);
        Drawable drawable = ContextCompat.getDrawable(this,rrItem.getImageResourceId());
        rrImage.setImageDrawable(drawable);

        RecyclerView relatedRRRecycler = findViewById(R.id.related_rr_recycler);

        //Related RR Data
        RR rr1 = new RR("Retrieved Ladies Bag","8 hours ago",R.drawable.ladies_bag);
        RR rr2 = new RR("Retrieved Mens Wallet","30 min ago",R.drawable.mens_wallet);
        RR rr3 = new RR("Retrieved Laptop Bag","20 secs ago",R.drawable.laptop_bag);

        RR[] relatedRRList = {rr1,rr2,rr3}; //Create array

        RRListAdapter relatedRRListAdapter = new RRListAdapter(Arrays.asList(relatedRRList),true);
        relatedRRRecycler.setAdapter(relatedRRListAdapter);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,3);
        relatedRRRecycler.setLayoutManager(gridLayoutManager);

    }

    public void onShareRRItemClicked(View view){
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setType("text/plain");
        intent.putExtra(Intent.EXTRA_TEXT,"Link will be available soon");
        Intent chooserIntent = Intent.createChooser(intent,"Share news");
        startActivity(chooserIntent);
    }
}
