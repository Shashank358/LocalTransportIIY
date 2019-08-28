package com.iiysoftware.localtransport.NewPackage;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.localtransport.Drivers;
import com.iiysoftware.localtransport.DriversAdapter;
import com.iiysoftware.localtransport.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class RectificationReqFrag extends Fragment {

    private RecyclerView requestList;
    private LinearLayoutManager layoutManager;
    private ReqAdapter adapter;
    private FirebaseFirestore db;
    private RelativeLayout frameLayout;

    public RectificationReqFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_rectification_req, container, false);

        requestList = view.findViewById(R.id.all_req_list);
        layoutManager = new LinearLayoutManager(getContext());
        requestList.setHasFixedSize(true);
        requestList.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();
        frameLayout = view.findViewById(R.id.req_container);

        Query query = db.collection("RectificationRequests").whereEqualTo("status", "pending");
        final FirestoreRecyclerOptions<Request> options = new FirestoreRecyclerOptions.Builder<Request>()
                .setQuery(query, Request.class)
                .build();

        adapter = new ReqAdapter(getContext(), options);
        requestList.setAdapter(adapter);

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
