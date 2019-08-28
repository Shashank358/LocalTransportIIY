package com.iiysoftware.localtransport.AttendanceFrag;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.localtransport.AllDriversFrag3;
import com.iiysoftware.localtransport.AssignedStudents;
import com.iiysoftware.localtransport.Drivers;
import com.iiysoftware.localtransport.DriversAdapter1;
import com.iiysoftware.localtransport.R;
import com.iiysoftware.localtransport.Students;
import com.iiysoftware.localtransport.StudentsAdapter1;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssToDriverFrag extends Fragment {

    private RecyclerView driverList;
    private LinearLayoutManager layoutManager;
    private StudentsAdapter1 adapter;
    private FirebaseFirestore db;

    public AssToDriverFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_ass_to_driver, container, false);

        String uid = getArguments().getString("uid");

        driverList = view.findViewById(R.id.all_students_list1);
        layoutManager = new LinearLayoutManager(getContext());
        driverList.setHasFixedSize(true);
        driverList.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("AssignedStudents").whereEqualTo("driver", uid);
        final FirestoreRecyclerOptions<AssignedStudents> options = new FirestoreRecyclerOptions.Builder<AssignedStudents>()
                .setQuery(query, AssignedStudents.class)
                .build();

        adapter = new StudentsAdapter1(getContext(), options);
        driverList.setAdapter(adapter);

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        AssignedStudFrag profileFragment = new AssignedStudFrag();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.stud_container, profileFragment)
                                .commit();
                        return true;
                    }
                }
                return false;
            }
        });

        return view;
    }
    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
