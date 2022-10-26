package com.dennis_brink.android.myweatherapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

public class MainActivity extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager2 viewPager2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tabLayout = findViewById(R.id.tabLayout);
        viewPager2 = findViewById(R.id.viewPagerMain);

        ViewPagerMainAdapter adapter = new ViewPagerMainAdapter(getSupportFragmentManager(), getLifecycle());
        viewPager2.setAdapter(adapter);

        TabLayoutMediator tabLayoutMediator = new TabLayoutMediator(tabLayout, viewPager2, true, true, (tab, position) -> {
            switch(position) {
                case 0:
                    tab.setCustomView(R.layout.tab0);
                    break;
                case 1:
                    tab.setCustomView(R.layout.tab1);
                    break;
            }

        });
        tabLayoutMediator.attach();
    }

}