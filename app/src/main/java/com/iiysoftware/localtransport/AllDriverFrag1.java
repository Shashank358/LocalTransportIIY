package com.iiysoftware.localtransport;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iiysoftware.localtransport.NewPackage.AssignedVehiclesFrag;
import com.iiysoftware.localtransport.NewPackage.SeeAssignedVehicleFrag;

import java.util.HashMap;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllDriverFrag1 extends Fragment {

    private RecyclerView driverList;
    private LinearLayoutManager layoutManager;
    private DriversAdapter1 adapter;
    private FirebaseFirestore db;

    public AllDriverFrag1() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_driver_frag1, container, false);

        driverList = view.findViewById(R.id.all_drivers_list1);
        layoutManager = new LinearLayoutManager(getContext());
        driverList.setHasFixedSize(true);
        driverList.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Drivers");
        final FirestoreRecyclerOptions<Drivers> options = new FirestoreRecyclerOptions.Builder<Drivers>()
                .setQuery(query, Drivers.class)
                .build();

        adapter = new DriversAdapter1(getContext(), options);
        driverList.setAdapter(adapter);

        adapter.setOnAssignItemClick(new DriversAdapter1.OnAssignItemClick() {
            @Override
            public void getPosition(String userId, Button assign) {
                Bundle bundle = new Bundle();
                bundle.putString("uid", userId);
                VehiclesFrag profileFragment = new VehiclesFrag();
                profileFragment.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.vehicle_container, profileFragment)
                        .commit();
            }
        });

        adapter.setOnUnAssignItemClick(new DriversAdapter1.OnUnassignItemClick() {
            @Override
            public void getPosition(String userId) {
                Bundle bundle = new Bundle();
                bundle.putString("uid", userId);
                SeeAssignedVehicleFrag profileFragment = new SeeAssignedVehicleFrag();
                profileFragment.setArguments(bundle);
                getFragmentManager()
                        .beginTransaction()
                        .replace(R.id.vehicle_container, profileFragment)
                        .commit();
            }
        });

        adapter.setOnItemClick(new DriversAdapter1.OnItemClick() {
            @Override
            public void getPosition(final String userId) {

                db.collection("AssignedVehicles").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()){
                            String uid = task.getResult().get("uid").toString();
                            HashMap<String, Object> hashMap1 = new HashMap<>();
                            hashMap1.put("isAssigned", false);

                            db.collection("Vehicles").document(uid).update(hashMap1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){

                                    }
                                }
                            });
                            db.collection("AssignedVehicles").document(userId)
                                    .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful())
                                    {
                                        Toast.makeText(getContext(), "unassigned", Toast.LENGTH_SHORT).show();

                                    }
                                }
                            });
                        }
                    }
                });
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
