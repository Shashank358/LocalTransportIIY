package com.iiysoftware.localtransport;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import javax.annotation.Nullable;

import de.hdodenhof.circleimageview.CircleImageView;


public class StudentsAssinAdapter extends FirestoreRecyclerAdapter<Students, StudentsAssinAdapter.ViewHolder> {

    private Context mContext;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    OnAssignedItemClick onAssignedItemClick;
    OnAddItemClick onAddItemClick;

    public StudentsAssinAdapter(Context mContext, FirestoreRecyclerOptions<Students> options) {
        super(options);
        this.mContext = mContext;
        this.notifyDataSetChanged();
    }

    public void setOnAssignItemClick(OnAssignedItemClick onAssignedItemClick) {
        this.onAssignedItemClick = onAssignedItemClick;
    }
    public void setOnAddItemClick(OnAddItemClick onAddItemClick) {
        this.onAddItemClick = onAddItemClick;
    }
    public interface OnAssignedItemClick {

        void getPosition(String userId, String name);

    }
    public interface OnAddItemClick {

        void getPosition(String userId, String name, String sClass);

    }
    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Students model) {

//        Picasso picasso = Picasso.get();
//        picasso.setIndicatorsEnabled(false);
//        picasso.load(model.get()).placeholder(R.drawable.avatar).into(holder.profile);
        holder.name.setText(model.getStudent_name());
        holder.email.setText(model.getStudent_email());

        holder.add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddItemClick.getPosition(model.getUid(), model.getStudent_name(), model.getStudent_class());
                holder.assigned.setVisibility(View.VISIBLE);
                holder.add.setVisibility(View.GONE);
            }
        });


        holder.assigned.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAssignedItemClick.getPosition(model.getUid(), model.getStudent_name());
                holder.assigned.setVisibility(View.GONE);
                holder.add.setVisibility(View.GONE);
            }
        });

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_students_item, viewGroup, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profile;
        private TextView name, email;
        private CardView cardView;
        private ImageView add, assigned;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.single_student_name);
            cardView = itemView.findViewById(R.id.student_card);
            profile = itemView.findViewById(R.id.single_student_image);
            email = itemView.findViewById(R.id.single_student_email);
            assigned = itemView.findViewById(R.id.add_student_view);
            add = itemView.findViewById(R.id.asigned_student_view);

        }

    }
}
