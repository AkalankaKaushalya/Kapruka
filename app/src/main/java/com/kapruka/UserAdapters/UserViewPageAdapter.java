package com.kapruka.UserAdapters;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.kapruka.UserFragments.HomeFragment;
import com.kapruka.UserFragments.ProfileFragment;
import com.kapruka.UserFragments.QuestionsFragment;
import com.kapruka.UserFragments.TreesFragment;


public class UserViewPageAdapter extends FragmentStatePagerAdapter {
    public UserViewPageAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm, behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new HomeFragment();

            case 1:
                return new QuestionsFragment();

            case 2:
                return new ProfileFragment();

            case 3:
                return new TreesFragment();


            default:
                return new HomeFragment();
        }

    }

    @Override
    public int getCount() {
        return 5;
    }
}
