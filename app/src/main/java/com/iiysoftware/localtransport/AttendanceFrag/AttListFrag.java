package com.iiysoftware.localtransport.AttendanceFrag;


import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.localtransport.AllDriverFrag;
import com.iiysoftware.localtransport.Date;
import com.iiysoftware.localtransport.Drivers;
import com.iiysoftware.localtransport.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.UUID;

import static com.iiysoftware.localtransport.RouteDisFrag.TAG;

/**
 * A simple {@link Fragment} subclass.
 */
public class AttListFrag extends Fragment {

    private RecyclerView attList;
    private LinearLayoutManager layoutManager;
    private PresentListAdapter adapter;
    private FirebaseFirestore db;
    TextView dateSelect;
    String dateId,date;


    public AttListFrag() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_att_list, container, false);

        dateId = getArguments().getString("uid");
        date = getArguments().getString("date");

        attList = view.findViewById(R.id.present_list);
        layoutManager = new LinearLayoutManager(getActivity());
        attList.setHasFixedSize(true);
        attList.setLayoutManager(layoutManager);
        dateSelect = view.findViewById(R.id.att_date_select_text);

        db = FirebaseFirestore.getInstance();

        dateSelect.setText(date);

        Query query = db.collection("Attendance").document(dateId).collection("Data");
        final FirestoreRecyclerOptions<Drivers> options = new FirestoreRecyclerOptions.Builder<Drivers>()
                .setQuery(query, Drivers.class)
                .build();

        adapter = new PresentListAdapter(getContext(), options);
        attList.setAdapter(adapter);

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        AllDatesFrag profileFragment = new AllDatesFrag();
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
