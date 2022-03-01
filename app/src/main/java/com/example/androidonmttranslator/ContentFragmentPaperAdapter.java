package com.example.androidonmttranslator;

import android.util.Pair;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class ContentFragmentPaperAdapter extends FragmentPagerAdapter {

    FragmentManager fm;
    List<Pair<String, Fragment>> fragments;

    public ContentFragmentPaperAdapter(@NonNull FragmentManager fm, int behavior, List<Pair<String, Fragment>> fragments) {
        super(fm, behavior);
        this.fm = fm;
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position).second;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return fragments.get(position).first;
    }
}
