package com.iiysoftware.localtransport;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.iiysoftware.localtransport.AttendanceFrag.AttendancePager;


/**
 * A simple {@link Fragment} subclass.
 */
public class AttendanceFragment extends Fragment {

    private ViewPager viewPager;
    private AttendancePager adapter;
    private TabLayout attTabs;

    public AttendanceFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_attendance, container, false);

        attTabs = view.findViewById(R.id.att_tabs);
        viewPager = view.findViewById(R.id.att_viewpager);
        attTabs.setupWithViewPager(viewPager);
        adapter = new AttendancePager(getFragmentManager());
        viewPager.setAdapter(adapter);

        return view;
    }

}
