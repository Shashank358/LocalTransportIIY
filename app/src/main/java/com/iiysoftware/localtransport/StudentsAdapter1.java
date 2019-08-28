package com.iiysoftware.localtransport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;


public class StudentsAdapter1 extends FirestoreRecyclerAdapter<AssignedStudents, StudentsAdapter1.ViewHolder> {

    private Context mContext;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    OnItemClick onItemClick;

    public StudentsAdapter1(Context mContext, FirestoreRecyclerOptions<AssignedStudents> options) {
        super(options);
        this.mContext = mContext;
        this.notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {

        void getPosition(String userId);

    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final AssignedStudents model) {

//        Picasso picasso = Picasso.get();
//        picasso.setIndicatorsEnabled(false);
//        picasso.load(model.getImage()).placeholder(R.drawable.avatar).into(holder.profile);
          holder.name.setText(model.getName());
          holder.id.setText(model.get_class());

//
//        db.collection("AssignedRoutes").document(model.getPush_id())
//                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
//                    @Override
//                    public void onEvent(@Nullable final DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
//                        if (documentSnapshot.exists())
//                        {
//                            holder.assign.setText("See route on map");
//                            holder.unassign.setVisibility(View.VISIBLE);
////                            holder.assign.setOnClickListener(new View.OnClickListener() {
////                                @Override
////                                public void onClick(View v) {
////                                    Intent intent = new Intent(mContext, MapsActivity.class);
////                                    intent.putExtra("uid", model.getPush_id());
////                                    mContext.startActivity(intent);
////                                }
////                            });
//                        }
//                    }
//                });
//
//        holder.unassign.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                db.collection("AssignedRoutes").document(model.getPush_id()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
//                    @Override
//                    public void onComplete(@NonNull Task<Void> task) {
//                        holder.unassign.setVisibility(View.GONE);
//                        Toast.makeText(mContext, "unassigned", Toast.LENGTH_SHORT).show();
//                    }
//                });
//            }
//        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_student_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout layout;
        private TextView name, id;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.single_student_name);
            id = itemView.findViewById(R.id.single_student_id);
            layout = itemView.findViewById(R.id.single_student_layout);

        }

    }
}
