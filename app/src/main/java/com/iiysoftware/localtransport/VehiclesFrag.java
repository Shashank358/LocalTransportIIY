package com.iiysoftware.localtransport;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class VehiclesFrag extends Fragment {

    private RecyclerView vehicleList;
    private LinearLayoutManager layoutManager;
    private VehicleAdapter adapter;
    private FirebaseFirestore db;
    private Toolbar toolbar;
    TextView vehicleMsg;

    public VehiclesFrag() {
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

        Query query = db.collection("Vehicles").whereEqualTo("isAssigned", false);
        final FirestoreRecyclerOptions<Vehicles> options = new FirestoreRecyclerOptions.Builder<Vehicles>()
                .setQuery(query, Vehicles.class)
                .build();

        adapter = new VehicleAdapter(getContext(), options);
        vehicleList.setAdapter(adapter);

        adapter.setOnItemClick(new VehicleAdapter.OnItemClick() {
            @Override
            public void getPosition(String userId, String image, String name, boolean isAssigned) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("uid", userId);
                hashMap.put("driver_id", uid);
                hashMap.put("image", image);
                hashMap.put("name_plate", name);
                hashMap.put("isAssigned", isAssigned);

                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("isAssigned", true);
                hashMap1.put("driver_id", uid);

                db.collection("Vehicles").document(userId).update(hashMap1);
                db.collection("AssignedVehicles").document(uid).set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful())
                        {
                            Toast.makeText(getActivity() , "Vehicle assigned", Toast.LENGTH_SHORT).show();
                            AllDriverFrag1 profileFragment = new AllDriverFrag1();
                            getFragmentManager()
                                    .beginTransaction()
                                    .replace(R.id.vehicle_container, profileFragment)
                                    .commit();
                        }
                    }
                });
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        AllDriverFrag1 profileFragment = new AllDriverFrag1();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.vehicle_container, profileFragment)
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
