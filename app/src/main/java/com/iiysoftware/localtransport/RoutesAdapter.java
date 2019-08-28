package com.iiysoftware.localtransport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


public class RoutesAdapter extends FirestoreRecyclerAdapter<Routes, RoutesAdapter.ViewHolder> {

    private Context mContext;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    OnItemClick onItemClick;

    public RoutesAdapter(Context mContext, FirestoreRecyclerOptions<Routes> options) {
        super(options);
        this.mContext = mContext;
        this.notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {

        void getPosition(String userId, String from, String dist, String stops, String to, boolean isAssigned);

    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Routes model) {

//        Picasso picasso = Picasso.get();
//        picasso.setIndicatorsEnabled(false);
//        picasso.load(model.getCity_name()).placeholder(R.drawable.default_image).into(holder.userImage);
//        holder.name.setText(model.getUser_name());
          holder.from.setText(model.getFrom());
          holder.to.setText(model.getTo());
          holder.distance.setText(model.getDist());
          holder.stops.setText(model.getStops());

          holder.routeCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.getPosition(model.getUid(), model.getFrom(), model.getDist(), model.getStops(), model.getTo(), model.isAssigend());
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_route_layout, viewGroup, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView from, to, distance, stops;
        private CardView routeCard;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            from = itemView.findViewById(R.id.from_route);
            to = itemView.findViewById(R.id.to_route);
            distance = itemView.findViewById(R.id.total_distance_route);
            stops = itemView.findViewById(R.id.total_stops_route);
            routeCard = itemView.findViewById(R.id.route_card);

        }

    }
}
