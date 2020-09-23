package com.example.healthcoach01;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

public class WorkoutViewPageAdapter extends FragmentStateAdapter {

    public int mCount;
    public WorkoutViewPageAdapter(FragmentActivity fa, int count) {
        super(fa);
        mCount = count;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position)
    {
        int index = getRealPosition(position);
        if(index==0)
            return new Fragment3();
        else
            return new Fragment4();
    }

    @Override
    public int getItemCount() {
        return 2;
    }

    public int getRealPosition(int position) {
        return position % mCount;
    }
}
