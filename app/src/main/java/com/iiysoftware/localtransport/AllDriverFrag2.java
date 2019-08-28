package com.iiysoftware.localtransport;


import android.content.Intent;
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


/**
 * A simple {@link Fragment} subclass.
 */
public class AllDriverFrag2 extends Fragment {


    private RecyclerView driverList;
    private LinearLayoutManager layoutManager;
    private DriversAdapter2 adapter;
    private FirebaseFirestore db;
    public AllDriverFrag2() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_driver_frag2, container, false);

        driverList = view.findViewById(R.id.all_drivers_list2);
        layoutManager = new LinearLayoutManager(getContext());
        driverList.setHasFixedSize(true);
        driverList.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Drivers");
        final FirestoreRecyclerOptions<Drivers> options = new FirestoreRecyclerOptions.Builder<Drivers>()
                .setQuery(query, Drivers.class)
                .build();

        adapter = new DriversAdapter2(getContext(), options);
        driverList.setAdapter(adapter);

        adapter.setOnItemClick(new DriversAdapter2.OnItemClick() {
            @Override
            public void getPosition(String userId) {

                Intent intent = new Intent(getActivity(), MapsActivity.class);
                intent.putExtra("uid", userId);
                startActivity(intent);
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
