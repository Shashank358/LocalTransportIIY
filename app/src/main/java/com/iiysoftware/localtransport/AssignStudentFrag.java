package com.iiysoftware.localtransport;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;

import javax.annotation.Nullable;


/**
 * A simple {@link Fragment} subclass.
 */
public class AssignStudentFrag extends Fragment {

    private RecyclerView driverList;
    private LinearLayoutManager layoutManager;
    private StudentsAssinAdapter adapter;
    private FirebaseFirestore db;
    String st_id;
    public AssignStudentFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_assign_student, container, false);

        final String driver_uid = getArguments().getString("uid");

        driverList = view.findViewById(R.id.all_students_lis);
        layoutManager = new LinearLayoutManager(getContext());
        driverList.setHasFixedSize(true);
        driverList.setLayoutManager(layoutManager);


        db = FirebaseFirestore.getInstance();
        Query query = db.collection("Parent").whereEqualTo("driver_id", "default");
        final FirestoreRecyclerOptions<Students> options = new FirestoreRecyclerOptions.Builder<Students>()
                .setQuery(query, Students.class)
                .build();

        adapter = new StudentsAssinAdapter(getContext(), options);
        driverList.setAdapter(adapter);
        adapter.startListening();

        adapter.setOnAddItemClick(new StudentsAssinAdapter.OnAddItemClick() {
            @Override
            public void getPosition(String userId, String name, String className) {
                st_id = userId;

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("isAssigned", true);
                hashMap.put("id", st_id);
                hashMap.put("name", name);
                hashMap.put("driver", driver_uid);
                hashMap.put("_class", className);

                db.collection("AssignedStudents").document(st_id)
                        .set(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(), "assigned", Toast.LENGTH_SHORT).show();
                            HashMap<String, Object> hashMap1 = new HashMap<>();
                            hashMap1.put("isAssigned", true);
                            hashMap1.put("driver_id", driver_uid);

                            db.collection("Parent").document(st_id).update(hashMap1);
                        }
                    }
                });
            }
        });

        adapter.setOnAssignItemClick(new StudentsAssinAdapter.OnAssignedItemClick() {
            @Override
            public void getPosition(String userId, String name) {

                db.collection("AssignedStudents").document(userId)
                        .delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(getActivity(), "Unselected", Toast.LENGTH_SHORT).show();

                            HashMap<String, Object> hashMap1 = new HashMap<>();
                            hashMap1.put("isAssigned", false);
                            hashMap1.put("driver_id", "default");

                            db.collection("Parent").document(st_id).update(hashMap1);
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
                        AllDriversFrag3 profileFragment = new AllDriversFrag3();
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
}
