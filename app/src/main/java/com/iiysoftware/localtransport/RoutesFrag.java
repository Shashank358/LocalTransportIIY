package com.iiysoftware.localtransport;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.MenuItemCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;


/**
 * A simple {@link Fragment} subclass.
 */
public class RoutesFrag extends Fragment {

    private RecyclerView routeList;
    private LinearLayoutManager layoutManager;
    private RoutesAdapter adapter;
    private FirebaseFirestore db;
    private Toolbar toolbar;
    String currentUser, driver_id;
    TextView routeMsg;

    public RoutesFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_routes, container, false);

        driver_id = getArguments().getString("uid");

        routeList = view.findViewById(R.id.all_route_list);
        layoutManager = new LinearLayoutManager(getActivity());
        routeList.setHasFixedSize(true);
        routeList.setLayoutManager(layoutManager);
        currentUser = FirebaseAuth.getInstance().getCurrentUser().getUid();

        db = FirebaseFirestore.getInstance();
        routeMsg = view.findViewById(R.id.route_message);

        Query query = db.collection("Routes");
        final FirestoreRecyclerOptions<Routes> options = new FirestoreRecyclerOptions.Builder<Routes>()
                .setQuery(query, Routes.class)
                .build();
        adapter = new RoutesAdapter(getContext(), options);
        routeList.setAdapter(adapter);

//        db.collection("Routes").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                if (task.isSuccessful()){
//                    for (QueryDocumentSnapshot doc : task.getResult()){
//                        if (doc.getBoolean("isAssigned") == false){
//                            routeList.setAdapter(adapter);
//                        }else {
//                            routeMsg.setVisibility(View.VISIBLE);
//                            routeList.setVisibility(View.GONE);
//                        }
//                    }
//                }
//            }
//        });

        adapter.setOnItemClick(new RoutesAdapter.OnItemClick() {
            @Override
            public void getPosition(String userId, String from, String dist, String stops, String to, boolean isAssigned) {
                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("uid    ", userId);
                hashMap.put("from", from);
                hashMap.put("to", to);
                hashMap.put("isAssigned", isAssigned);
                hashMap.put("stops", stops);
                hashMap.put("dist", dist);
                hashMap.put("driver", driver_id);

                HashMap<String, Object> hashMap1 = new HashMap<>();
                hashMap1.put("isAssigned", true);

                db.collection("Routes").document(userId).update(hashMap1);
                db.collection("AssignedRoutes").document(driver_id)
                        .set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getActivity() , "Route assigned to driver", Toast.LENGTH_SHORT).show();

                        AllDriverFrag profileFragment = new AllDriverFrag();
                        getFragmentManager()
                                .beginTransaction()
                                .replace(R.id.vehicle_container, profileFragment)
                                .commit();
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
