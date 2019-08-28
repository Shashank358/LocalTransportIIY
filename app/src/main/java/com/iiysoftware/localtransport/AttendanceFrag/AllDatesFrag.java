package com.iiysoftware.localtransport.AttendanceFrag;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.localtransport.Date;
import com.iiysoftware.localtransport.Drivers;
import com.iiysoftware.localtransport.R;
import com.iiysoftware.localtransport.RoutesFrag;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 */
public class AllDatesFrag extends Fragment {

    private RecyclerView attList;
    private LinearLayoutManager layoutManager;
    private PresentsAdapter adapter;
    private FirebaseFirestore db;
    TextView dateSelect;


    public AllDatesFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_dates, container, false);

        attList = view.findViewById(R.id.all_dates_list);
        layoutManager = new LinearLayoutManager(getActivity());
        attList.setHasFixedSize(true);
        attList.setLayoutManager(layoutManager);
        dateSelect = view.findViewById(R.id.att_date_select_text);

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Attendance");
        final FirestoreRecyclerOptions<Date> options = new FirestoreRecyclerOptions.Builder<Date>()
                .setQuery(query, Date.class)
                .build();

        adapter = new PresentsAdapter(getContext(), options);
        attList.setAdapter(adapter);

        adapter.setOnItemClick(new PresentsAdapter.OnItemClick() {
            @Override
            public void getPosition(String userId,  String date) {
                Bundle bundle = new Bundle();
                bundle.putString("uid", userId);
                bundle.putString("date",date);
                AttListFrag profileFragment = new AttListFrag();
                profileFragment.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.date_container, profileFragment)
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
