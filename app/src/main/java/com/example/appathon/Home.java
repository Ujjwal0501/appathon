package com.example.appathon;

import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class Home extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        // reference to the TabLayout object added to the activity_home.xml
        TabLayout tabLayout =
                (TabLayout) findViewById(R.id.tablayout);

        // creating two tabs, assigning the text to appear on each
        tabLayout.addTab(tabLayout.newTab().setText("Account"));
        tabLayout.addTab(tabLayout.newTab().setText("History"));

        // reference to the ViewPager instance in the activity_home.xml
        final ViewPager viewPager =
                (ViewPager) findViewById(R.id.viewpager);

        // instance of the TabPagerAdapter class created
        final PagerAdapter adapter = new TabPagerAdapter
                (getSupportFragmentManager(),
                        tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        // listen for tab changes and inflate the fragment
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }
}
