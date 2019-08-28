package com.iiysoftware.localtransport.AttendanceFrag;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.iiysoftware.localtransport.R;

public class AttendancePager extends FragmentStatePagerAdapter {


    public AttendancePager(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TakeAttFrag();
            case 1:
                return new AllDatesFrag();
        }
        return null;    }

    @Override
    public int getCount() {
        return 2;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position){
            case 0:
                return "All Drivers";
            case 1:
                return "Present";
        }
        return super.getPageTitle(position);
    }
}
