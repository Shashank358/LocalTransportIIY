package com.iiysoftware.localtransport.AttendanceFrag;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
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
import com.google.firebase.firestore.FirebaseFirestore;
import com.iiysoftware.localtransport.Drivers;
import com.iiysoftware.localtransport.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;


public class AttendanceDriversAdapter extends FirestoreRecyclerAdapter<Drivers, AttendanceDriversAdapter.ViewHolder> {

    private Context mContext;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private ProgressDialog dialog;

    OnItemClick onItemClick;

    public AttendanceDriversAdapter(Context mContext, FirestoreRecyclerOptions<Drivers> options) {
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

        Picasso picasso = Picasso.get();
        picasso.setIndicatorsEnabled(false);
        picasso.load(model.getImage()).placeholder(R.drawable.avatar).into(holder.profile);
        holder.name.setText(model.getUser_name());
        holder.email.setText(model.getUser_email());

        dialog = new ProgressDialog(mContext);

        db = FirebaseFirestore.getInstance();

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (holder.checkBox.isChecked()){
                    dialog.setMessage("please wait");
                    dialog.setCanceledOnTouchOutside(false);
                    dialog.show();

                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    final String formattedDate = df.format(c);

                    final HashMap<String, Object> dateMap = new HashMap<>();
                    dateMap.put("date", formattedDate);
                    dateMap.put("attendence", "Present");

                    db.collection("Drivers").document(model.getPush_id()).collection("Attencence")
                            .document(formattedDate).set(dateMap)
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        dialog.dismiss();
                                        Toast.makeText(mContext, "checked", Toast.LENGTH_SHORT).show();

                                    }else {
                                        dialog.hide();
                                        Toast.makeText(mContext, "failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });

                }else {
                    Date c = Calendar.getInstance().getTime();
                    System.out.println("Current time => " + c);

                    SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                    String formattedDate = df.format(c);

                    db.collection("Drivers").document(model.getPush_id()).collection("Attencence")
                            .document(formattedDate).delete()
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        dialog.dismiss();
                                        Toast.makeText(mContext, "unchecked", Toast.LENGTH_SHORT).show();

                                    }else {
                                        dialog.hide();
                                        Toast.makeText(mContext, "error", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });


    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.driver_attendance, viewGroup, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView profile;
        private TextView name, email;
        private CardView cardView;
        private CheckBox checkBox;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.driver_name);
            cardView = itemView.findViewById(R.id.student_card);
            profile = itemView.findViewById(R.id.driver_image);
            email = itemView.findViewById(R.id.driver_email);
            checkBox = itemView.findViewById(R.id.checkBox);

        }

    }
}
