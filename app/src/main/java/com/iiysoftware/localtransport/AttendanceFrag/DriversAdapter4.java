package com.iiysoftware.localtransport.AttendanceFrag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.iiysoftware.localtransport.Drivers;
import com.iiysoftware.localtransport.R;
import com.squareup.picasso.Picasso;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;


public class DriversAdapter4 extends FirestoreRecyclerAdapter<Drivers, DriversAdapter4.ViewHolder> {

    private Context mContext;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    OnItemClick onItemClick;

    public DriversAdapter4(Context mContext, FirestoreRecyclerOptions<Drivers> options) {
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
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Drivers model) {

        holder.assign.setText("See Assigned Students");
        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(false);
        picasso.load(model.getImage()).placeholder(R.drawable.avatar).into(holder.profile);
        holder.name.setText(model.getUser_name());
        holder.email.setText(model.getUser_email());

        holder.assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.getPosition(model.getPush_id());
            }
        });


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
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_driver_item, viewGroup, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profile;
        private TextView name, email;
        private CardView cardView;
        private Button assign, unassign;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.driver_name);
            cardView = itemView.findViewById(R.id.student_card);
            profile = itemView.findViewById(R.id.driver_image);
            email = itemView.findViewById(R.id.driver_email);
            assign = itemView.findViewById(R.id.assign_route_btn);
            unassign = itemView.findViewById(R.id.unassign_route_btn);

        }

    }
}
