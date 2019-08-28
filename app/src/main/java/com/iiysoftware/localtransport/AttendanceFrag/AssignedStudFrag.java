package com.iiysoftware.localtransport.AttendanceFrag;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.localtransport.Drivers;
import com.iiysoftware.localtransport.R;
import com.iiysoftware.localtransport.Students;
import com.iiysoftware.localtransport.StudentsAdapter;
import com.iiysoftware.localtransport.VehiclesFrag;

/**
 * A simple {@link Fragment} subclass.
 */
public class AssignedStudFrag extends Fragment {

    private RecyclerView studentsList;
    private LinearLayoutManager layoutManager;
    private DriversAdapter4 adapter;
    private FirebaseFirestore db;

    public AssignedStudFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assigned_stud, container, false);

        studentsList = view.findViewById(R.id.all_students_list);
        layoutManager = new LinearLayoutManager(getContext());
        studentsList.setHasFixedSize(true);
        studentsList.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Drivers");
        final FirestoreRecyclerOptions<Drivers> options = new FirestoreRecyclerOptions.Builder<Drivers>()
                .setQuery(query, Drivers.class)
                .build();

        adapter = new DriversAdapter4(getContext(), options);
        studentsList.setAdapter(adapter);

        adapter.setOnItemClick(new DriversAdapter4.OnItemClick() {
            @Override
            public void getPosition(String userId) {
                Bundle bundle = new Bundle();
                bundle.putString("uid", userId);
                AssToDriverFrag profileFragment = new AssToDriverFrag();
                profileFragment.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.stud_container, profileFragment)
                        .commit();
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
