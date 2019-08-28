package com.iiysoftware.localtransport.NewPackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
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
import com.iiysoftware.localtransport.Drivers;
import com.iiysoftware.localtransport.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;


public class ReqAdapter extends FirestoreRecyclerAdapter<Request, ReqAdapter.ViewHolder> {

    private Context mContext;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    OnItemClick onItemClick;

    public ReqAdapter(Context mContext, FirestoreRecyclerOptions<Request> options) {
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
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Request model) {


        holder.reason.setText(model.getReason());

        db.collection("Drivers").document(model.getUser_id()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {

                String image = documentSnapshot.get("image").toString();
                String name = documentSnapshot.get("user_name").toString();

                Picasso picasso = Picasso.get();
                picasso.setIndicatorsEnabled(false);
                picasso.load(image).placeholder(R.drawable.avatar).into(holder.profile);
                holder.name.setText(name);
                holder.date.setText(model.getDate());
            }
        });

        holder.approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("status", "approved");
                db.collection("RectificationRequests").document(model.getPush()).update(hashMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    final HashMap<String, Object> dateMap = new HashMap<>();
                                    dateMap.put("date", model.getDate());
                                    dateMap.put("attendence", "Present");

                                    db.collection("Drivers").document(model.getUser_id()).collection("Attencence")
                                            .document(model.getDate()).set(dateMap)
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(mContext, "approved", Toast.LENGTH_SHORT).show();

                                                    }else {
                                                        Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
                                                    }
                                                }
                                            });

                                }
                            }
                        });
            }
        });

        holder.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                HashMap<String, Object> hashMap = new HashMap<>();
                hashMap.put("status", "Declined");
                db.collection("RectificationRequests").document(model.getPush()).update(hashMap)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    db.collection("Drivers").document(model.getUser_id()).collection("Attencence")
                                            .document(model.getDate()).delete()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()){
                                                        Toast.makeText(mContext, "Declined", Toast.LENGTH_SHORT).show();

                                                    }else {
                                                    }
                                                }
                                            });
                                }
                            }
                        });
            }
        });



    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_req_list, viewGroup, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profile;
        private TextView name, reason, date;
        private ImageView approve, decline;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.req_single_name);
            profile = itemView.findViewById(R.id.req_single_image);
            reason = itemView.findViewById(R.id.req_single_reason);
            date = itemView.findViewById(R.id.req_single_date);
            approve = itemView.findViewById(R.id.req_approve);
            decline = itemView.findViewById(R.id.req_decline);

        }

    }
}
