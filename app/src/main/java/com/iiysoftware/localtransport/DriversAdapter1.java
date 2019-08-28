package com.iiysoftware.localtransport;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;


public class DriversAdapter1 extends FirestoreRecyclerAdapter<Drivers, DriversAdapter1.ViewHolder> {

    private Context mContext;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;


    public DriversAdapter1(Context mContext, FirestoreRecyclerOptions<Drivers> options) {
        super(options);
        this.mContext = mContext;
        this.notifyDataSetChanged();
    }
    OnAssignItemClick onAssignItemClick;
    OnUnassignItemClick onUnassignItemClick;
    OnItemClick onItemClick;


    public void setOnAssignItemClick(OnAssignItemClick onAssignItemClick) {
        this.onAssignItemClick = onAssignItemClick;
    }
    public void setOnUnAssignItemClick(OnUnassignItemClick onUnassignItemClick) {
        this.onUnassignItemClick = onUnassignItemClick;
    }
    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }
    public interface OnAssignItemClick {

        void getPosition(String userId, Button button);
    }
    public interface OnUnassignItemClick {
        void getPosition(String userId);

    }

    public interface OnItemClick {
        void getPosition(String userId);

    }
        @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Drivers model) {

        holder.assign.setText("Assign Vehicle");
        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(false);
        picasso.load(model.getImage()).placeholder(R.drawable.avatar).into(holder.profile);
        holder.name.setText(model.getUser_name());
        holder.email.setText(model.getUser_email());

        holder.assign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAssignItemClick.getPosition(model.getPush_id(), holder.assign);
            }
        });

        db.collection("AssignedVehicles").document(model.getPush_id())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    if (task.getResult().exists())
                    {
                        holder.unassign.setVisibility(View.VISIBLE);
                        holder.assign.setVisibility(View.GONE);
                        holder.seeroute.setVisibility(View.VISIBLE);

                    }
                }
            }
        });

        holder.seeroute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUnassignItemClick.getPosition(model.getPush_id()); }
        });

        holder.unassign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.getPosition(model.getPush_id());
                holder.unassign.setVisibility(View.GONE);
                holder.assign.setEnabled(true);
                holder.assign.setVisibility(View.VISIBLE);
                holder.seeroute.setVisibility(View.GONE);

            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_driver_vehicle_item, viewGroup, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profile;
        private TextView name, email;
        private CardView cardView;
        private Button assign, unassign, seeroute;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.driver_name);
            cardView = itemView.findViewById(R.id.student_card);
            profile = itemView.findViewById(R.id.driver_image);
            email = itemView.findViewById(R.id.driver_email);
            assign = itemView.findViewById(R.id.assign_route_btn);
            unassign = itemView.findViewById(R.id.unassign_vehicle_btn);
            seeroute = itemView.findViewById(R.id.see_route_btn);

        }

    }
}
