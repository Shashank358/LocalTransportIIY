package com.iiysoftware.localtransport.NewPackage;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iiysoftware.localtransport.AllDriverFrag1;
import com.iiysoftware.localtransport.R;
import com.iiysoftware.localtransport.VehicleAdapter;
import com.iiysoftware.localtransport.Vehicles;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssignedVehiclesFrag extends Fragment {

    private RecyclerView vehicleList;
    private LinearLayoutManager layoutManager;
    private VehicleAdapter adapter;
    private FirebaseFirestore db;
    private Toolbar toolbar;
    TextView vehicleMsg;

    public AssignedVehiclesFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_vehicles, container, false);

        final String uid = getArguments().getString("uid");

        vehicleList = view.findViewById(R.id.all_vehicles_list);
        layoutManager = new LinearLayoutManager(getActivity());
        vehicleList.setHasFixedSize(true);
        vehicleList.setLayoutManager(layoutManager);
        vehicleMsg = view.findViewById(R.id.vehicle_message);

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Vehicles");
        final FirestoreRecyclerOptions<Vehicles> options = new FirestoreRecyclerOptions.Builder<Vehicles>()
                .setQuery(query, Vehicles.class)
                .build();

        adapter = new VehicleAdapter(getContext(), options);
        vehicleList.setAdapter(adapter);
        db.collection("Vehicles").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (QueryDocumentSnapshot doc : task.getResult()){
                        if (doc.get("driver_id").toString().equals(uid)){
                            vehicleList.setAdapter(adapter);
                        }else {
                            vehicleMsg.setVisibility(View.VISIBLE);
                            vehicleList.setVisibility(View.GONE);
                        }
                    }
                }
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
