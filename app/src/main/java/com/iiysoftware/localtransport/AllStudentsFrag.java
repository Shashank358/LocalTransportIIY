package com.iiysoftware.localtransport;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;


/**
 * A simple {@link Fragment} subclass.
 */
public class AllStudentsFrag extends Fragment {

    private RecyclerView studentsList;
    private LinearLayoutManager layoutManager;
    private StudentsAdapter adapter;
    private FirebaseFirestore db;

    public AllStudentsFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_all_students, container, false);

        studentsList = view.findViewById(R.id.all_students_list);
        layoutManager = new LinearLayoutManager(getContext());
        studentsList.setHasFixedSize(true);
        studentsList.setLayoutManager(layoutManager);

        db = FirebaseFirestore.getInstance();

        Query query = db.collection("Parent");
        final FirestoreRecyclerOptions<Students> options = new FirestoreRecyclerOptions.Builder<Students>()
                .setQuery(query, Students.class)
                .build();

        adapter = new StudentsAdapter(getContext(), options);
        studentsList.setAdapter(adapter);

        adapter.setOnItemClick(new StudentsAdapter.OnItemClick() {
            @Override
            public void getPosition(String userId) {
                Intent intent = new Intent(getActivity(), SelectLocationActivity.class);
                intent.putExtra("student", userId);
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
