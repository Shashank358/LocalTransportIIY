package com.iiysoftware.localtransport;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.iiysoftware.localtransport.AttendanceFrag.AttendancePager;
import com.iiysoftware.localtransport.AttendanceFrag.StudentPager;


/**
 * A simple {@link Fragment} subclass.
 */
public class StudAttMainFrag extends Fragment {

    private ViewPager viewPager;
    private StudentPager adapter;
    private TabLayout attTabs;
    public StudAttMainFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_stud_att_main, container, false);

        attTabs = view.findViewById(R.id.student_tabs);
        viewPager = view.findViewById(R.id.student_viewpager);
        attTabs.setupWithViewPager(viewPager);
        adapter = new StudentPager(getFragmentManager());
        viewPager.setAdapter(adapter);

        return view;
    }

}
