package com.iiysoftware.localtransport.AttendanceFrag;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.iiysoftware.localtransport.AllDriversFrag3;

public class StudentPager extends FragmentStatePagerAdapter {


    public StudentPager(@NonNull FragmentManager fm) {
        super(fm);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {

        switch (position){
            case 0:
                return new AllDriversFrag3();

            case 1:
                return new AssignedStudFrag();
        }
        return null;
    }

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
                return "Assigned Students";

                default:
                    return super.getPageTitle(position);
        }
    }
}
