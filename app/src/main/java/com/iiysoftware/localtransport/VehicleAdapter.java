package com.iiysoftware.localtransport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class VehicleAdapter extends FirestoreRecyclerAdapter<Vehicles, VehicleAdapter.ViewHolder> {

    private Context mContext;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    OnItemClick onItemClick;

    public VehicleAdapter(Context mContext, FirestoreRecyclerOptions<Vehicles> options) {
        super(options);
        this.mContext = mContext;
        this.notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {

        void getPosition(String userId, String image, String name, boolean isAssigned);

    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Vehicles model) {


          holder.name.setText(model.getName_plate());
          holder.uid.setText(model.getUid());
        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(false);
        picasso.load(model.getImage()).placeholder(R.drawable.avatar).into(holder.image);

        holder.assign.setVisibility(View.GONE);

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.getPosition(model.getUid(), model.getImage(), model.getName_plate(), model.isAssigned());
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

        private CircleImageView image;
        private TextView name, uid;
        private CardView cardView;
        private Button assign;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.driver_name);
            cardView = itemView.findViewById(R.id.student_card);
            image = itemView.findViewById(R.id.driver_image);
            uid = itemView.findViewById(R.id.driver_email);
            assign = itemView.findViewById(R.id.assign_route_btn);

        }

    }
}
