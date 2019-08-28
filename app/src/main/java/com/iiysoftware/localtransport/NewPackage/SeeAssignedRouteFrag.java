package com.iiysoftware.localtransport.NewPackage;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.iiysoftware.localtransport.AllDriverFrag;
import com.iiysoftware.localtransport.R;
import com.iiysoftware.localtransport.Routes;
import com.iiysoftware.localtransport.RoutesAdapter;

import javax.annotation.Nullable;

/**
 * A simple {@link Fragment} subclass.
 */
public class SeeAssignedRouteFrag extends Fragment {

    private RecyclerView routeList;
    private LinearLayoutManager layoutManager;
    private RoutesAdapter adapter;
    private FirebaseFirestore db;
    String currentUser, driver_id;

    public SeeAssignedRouteFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_see_assigned_route, container, false);
        driver_id = getArguments().getString("uid");
        Toast.makeText(getContext(), driver_id, Toast.LENGTH_SHORT).show();

        routeList = view.findViewById(R.id.all_route_list);
        layoutManager = new LinearLayoutManager(getActivity());
        routeList.setHasFixedSize(true);
        routeList.setLayoutManager(layoutManager);
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("AssignedRoutes").whereEqualTo("driver", driver_id);
        final FirestoreRecyclerOptions<Routes> options = new FirestoreRecyclerOptions.Builder<Routes>()
                .setQuery(query, Routes.class)
                .build();
        adapter = new RoutesAdapter(getContext(), options);
        routeList.setAdapter(adapter);

        adapter.setOnItemClick(new RoutesAdapter.OnItemClick() {
            @Override
            public void getPosition(String userId, String from, String dist, String stops, String to, boolean isAssigned) {
                Toast.makeText(getActivity(), "clicked", Toast.LENGTH_SHORT).show();
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        AllDriverFrag profileFragment = new AllDriverFrag();
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
