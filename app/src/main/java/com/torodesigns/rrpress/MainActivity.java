package com.torodesigns.rrpress;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.material.tabs.TabLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set the toolbar
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Add a toggle drawer
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,
                drawerLayout,toolbar,R.string.nav_open_drawer,R.string.nav_close_drawer);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        //Attach the SectionsPageAdapter to the ViewPager
        SectionsPageAdapter pageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        pager.setAdapter(pageAdapter);

        int index = getIntent().getIntExtra("fragment_index_key",0);
        pager.setCurrentItem(index);

        //Attach the ViewPager to the TabLayout
        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(pager);
    }

    //Show the menu options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    //This methods gets called when an action on the app bar is clicked
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_sign_in:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //Action when the user presses the back button
    @Override
    public void onBackPressed() {
        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        if(drawerLayout.isDrawerOpen(GravityCompat.START)){
            drawerLayout.closeDrawer(GravityCompat.START);
        }else{
            super.onBackPressed();
        }
    }

    //This class passes information to the ViewPager
    private class SectionsPageAdapter extends FragmentPagerAdapter{
        //default constructor
        public SectionsPageAdapter(FragmentManager fm){
            super(fm);
        }


        //Specify which fragment should appear on each page
        @Override
        public Fragment getItem(int position) {
            switch (position){
                case 0:
                    return new TopStoriesFragment();
                case 1:
                    return new CartoonaFragment();
                case 2:
                    return new RRFragment();
                case 3:
                    return new VideoFragment();
            }
            return null;
        }

        //number of pages the view should contain
        @Override
        public int getCount() {
            return 4;
        }

        //Add text to the tabs
        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position){
                case 0:
                    return getResources().getText(R.string.topstories_tab);
                case 1:
                    return getResources().getText(R.string.cartoona_tab);
                case 2:
                    return getResources().getText(R.string.rr_tab);
                case 3:
                    return getResources().getText(R.string.videos_tab);
            }
            return null;
        }
    }

    public void onTellTheNewsClicked(View view){
        Intent intent = new Intent(this, TellTheNewsActivity.class);
        startActivity(intent);
    }

    public void onReportMissingItemClicked(View view){
        Intent intent = new Intent(this,ReportMissingItemActivity.class);
        startActivity(intent);
    }
}
