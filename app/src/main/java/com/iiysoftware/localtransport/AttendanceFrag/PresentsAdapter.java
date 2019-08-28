package com.iiysoftware.localtransport.AttendanceFrag;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.iiysoftware.localtransport.Date;
import com.iiysoftware.localtransport.Drivers;
import com.iiysoftware.localtransport.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;


public class PresentsAdapter extends FirestoreRecyclerAdapter<Date, PresentsAdapter.ViewHolder> {

    private Context mContext;

    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private PresentListAdapter adapter;

    OnItemClick onItemClick;

    public PresentsAdapter(Context mContext, FirestoreRecyclerOptions<Date> options) {
        super(options);
        this.mContext = mContext;
        this.notifyDataSetChanged();
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public interface OnItemClick {

        void getPosition(String userId, String date);

    }

    @Override
    protected void onBindViewHolder(@NonNull final ViewHolder holder, final int position, @NonNull final Date model) {

        holder.date.setText(model.getDate());

        holder.date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClick.getPosition(model.getPushId(), model.getDate());
            }
        });
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.single_present_driver_att, viewGroup, false);

        return new ViewHolder(view);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {


        private TextView date;
        private RecyclerView recyclerView;
        private LinearLayoutManager layoutManager;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.single_date_text);
            recyclerView = itemView.findViewById(R.id.present_single_driver_list);
            layoutManager = new LinearLayoutManager(mContext);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(layoutManager);


        }

    }
}
