package com.dennis_brink.android.myweatherapp;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.dennis_brink.android.myweatherapp.fragments.FragmentCity;
import com.dennis_brink.android.myweatherapp.fragments.FragmentForecast;
import com.dennis_brink.android.myweatherapp.fragments.FragmentMain;

public class ViewPagerMainAdapter extends FragmentStateAdapter {

    public ViewPagerMainAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        Fragment fragment = null;
        switch(position){
            case 0:
                fragment = FragmentMain.newInstance();
                break;
            case 1:
                fragment = FragmentForecast.newInstance();
                break;
            case 2:
                fragment = FragmentCity.newInstance();
                break;
        }
        return fragment;
    }

    @Override
    public int getItemCount() {
        return 3;
    }
}
