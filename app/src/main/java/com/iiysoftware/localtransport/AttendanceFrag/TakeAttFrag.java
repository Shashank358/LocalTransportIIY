package com.iiysoftware.localtransport.AttendanceFrag;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.localtransport.Drivers;
import com.iiysoftware.localtransport.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class TakeAttFrag extends Fragment {

    private RecyclerView driverList;
    private LinearLayoutManager layoutManager;
    private AttendanceDriversAdapter adapter;
    private FirebaseFirestore db;

    public TakeAttFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_take_att, container, false);

        driverList = view.findViewById(R.id.driver_attendance_list);
        layoutManager = new LinearLayoutManager(getActivity());
        driverList.setHasFixedSize(true);
        driverList.setLayoutManager(layoutManager);


        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Drivers");
        final FirestoreRecyclerOptions<Drivers> options = new FirestoreRecyclerOptions.Builder<Drivers>()
                .setQuery(query, Drivers.class)
                .build();

        adapter = new AttendanceDriversAdapter(getContext(), options);
        driverList.setAdapter(adapter);


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
