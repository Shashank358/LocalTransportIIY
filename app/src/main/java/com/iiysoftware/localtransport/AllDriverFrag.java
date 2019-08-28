package com.iiysoftware.localtransport;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.localtransport.NewPackage.SeeAssignedRouteFrag;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllDriverFrag extends Fragment {

    private RecyclerView driverList;
    private LinearLayoutManager layoutManager;
    private DriversAdapter adapter;
    private FirebaseFirestore db;
    private RelativeLayout frameLayout;

    public AllDriverFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_driver, container, false);

        driverList = view.findViewById(R.id.all_driver_list);
        layoutManager = new LinearLayoutManager(getContext());
        driverList.setHasFixedSize(true);
        driverList.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();
        frameLayout = view.findViewById(R.id.vehicle_container);

        Query query = db.collection("Drivers");
        final FirestoreRecyclerOptions<Drivers> options = new FirestoreRecyclerOptions.Builder<Drivers>()
                .setQuery(query, Drivers.class)
                .build();

        adapter = new DriversAdapter(getContext(), options);
        driverList.setAdapter(adapter);

        adapter.setOnItemClick(new DriversAdapter.OnItemClick() {
            @Override
            public void getPosition(String userId) {
                Bundle bundle = new Bundle();
                bundle.putString("uid", userId);
                RoutesFrag profileFragment = new RoutesFrag();
                profileFragment.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.vehicle_container, profileFragment)
                        .commit();
            }
        });


        adapter.setOnShowClick(new DriversAdapter.OnShowclick() {
            @Override
            public void getPosition(String userId) {
                Toast.makeText(getActivity(), "showed", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("uid", userId);
                SeeAssignedRouteFrag profileFragment = new SeeAssignedRouteFrag();
                profileFragment.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.vehicle_container, profileFragment)
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
