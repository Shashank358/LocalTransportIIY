package com.iiysoftware.localtransport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.iiysoftware.localtransport.AttendanceFrag.TakeAttFrag;
import com.iiysoftware.localtransport.NewPackage.RectificationReqFrag;

public class MainActivity extends AppCompatActivity {
    CoordinatorLayout coordinatorLayout;
    private ImageView filterIcon;
    BottomSheetBehavior sheetBehavior;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    TextView dropLocation, liveLocation, assignRoute, assignVehicle, attendanceDriver, assignStudent,request;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        String name = getIntent().getStringExtra("place");
        String key = getIntent().getStringExtra("key");



        final Context context = getApplicationContext();

        getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setElevation(0);

        LinearLayout contentLayout = findViewById(R.id.contentLayout);
        final ConstraintLayout c = findViewById(R.id.constr);

        sheetBehavior = BottomSheetBehavior.from(contentLayout);
        sheetBehavior.setFitToContents(false);
        sheetBehavior.setHideable(false);//prevents the boottom sheet from completely hiding off the screen
        sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);//initially state to fully expanded
        sheetBehavior.setBottomSheetCallback(new MyBottomSheetCallback());

        dropLocation = (TextView) findViewById(R.id.drop_location);
        liveLocation = (TextView) findViewById(R.id.live_location);
        assignRoute = (TextView) findViewById(R.id.assign_route);
        assignVehicle = (TextView) findViewById(R.id.assign_vehicle);
        attendanceDriver = (TextView) findViewById(R.id.attendance_driver);
        assignStudent = (TextView) findViewById(R.id.assign_student);
        request = (TextView) findViewById(R.id.rectification_request);

        request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RectificationReqFrag driverFrag = new RectificationReqFrag();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.constr, driverFrag).commit();
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        assignStudent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StudAttMainFrag driverFrag = new StudAttMainFrag();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.constr, driverFrag).commit();
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        assignRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllDriverFrag driverFrag = new AllDriverFrag();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.constr, driverFrag).commit();
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        assignVehicle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllDriverFrag1 driverFrag1 = new AllDriverFrag1();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.constr, driverFrag1).commit();
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        dropLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllStudentsFrag studentsFrag = new AllStudentsFrag();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.constr, studentsFrag).commit();
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        liveLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AllDriverFrag2 driverFrag2 = new AllDriverFrag2();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.constr, driverFrag2).commit();
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        attendanceDriver.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TakeAttFrag attendanceFragment = new TakeAttFrag();
                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                transaction.replace(R.id.constr, attendanceFragment).commit();
                sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);

            }
        });

        AllStudentsFrag studentsFrag = new AllStudentsFrag();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.constr, studentsFrag).commit();

        sheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                if (newState == BottomSheetBehavior.STATE_DRAGGING) {
                    sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
            }
        });
    }

    private void RunAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.text_animation);
        a.reset();
        TextView tv = (TextView) findViewById(R.id.textView7);
        tv.clearAnimation();
        tv.startAnimation(a);
    }

    private void RunSlideAnimation()
    {
        Animation a = AnimationUtils.loadAnimation(this, R.anim.slide_left);
        a.reset();
        dropLocation.clearAnimation();
        dropLocation.startAnimation(a);
        liveLocation.clearAnimation();
        liveLocation.startAnimation(a);
        assignRoute.clearAnimation();
        assignRoute.startAnimation(a);
        assignVehicle.clearAnimation();
        assignVehicle.startAnimation(a);
        assignStudent.clearAnimation();
        assignStudent.startAnimation(a);
        attendanceDriver.clearAnimation();
        attendanceDriver.startAnimation(a);
    }

    private void RunAlphaAnimation(){
        Animation a = AnimationUtils.loadAnimation(this, R.anim.fade_id);

        a.reset();
        dropLocation.clearAnimation();
        dropLocation.startAnimation(a);
        liveLocation.clearAnimation();
        liveLocation.startAnimation(a);
        assignRoute.clearAnimation();
        assignRoute.startAnimation(a);
        assignVehicle.clearAnimation();
        assignVehicle.startAnimation(a);
        assignStudent.clearAnimation();
        assignStudent.startAnimation(a);
        attendanceDriver.clearAnimation();
        attendanceDriver.startAnimation(a);
    }

    private void toggleFilters(){
        if(sheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED){
            sheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_clear_white);

//            RunSlideAnimation();
            RunAlphaAnimation();
            RunAnimation();
        }
        else {
            sheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.ic_menu_white);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();
        if (id == android.R.id.home){
            toggleFilters();

        }
        else if(id == R.id.logout_menu){
            Toast.makeText(this, "logout", Toast.LENGTH_SHORT).show();
            FirebaseAuth.getInstance().signOut();
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }

        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser == null)
        {
            Intent intent = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }
    private class MyBottomSheetCallback extends BottomSheetBehavior.BottomSheetCallback {
        @Override
        public void onStateChanged(@NonNull View bottomSheet, int newState) {
            if (newState == BottomSheetBehavior.STATE_EXPANDED) {
                if (sheetBehavior instanceof LockableBottomSheetBehavior) {
                    ((LockableBottomSheetBehavior) sheetBehavior).setLocked(true);
                }
            }
        }

        @Override
        public void onSlide(@NonNull View bottomSheet, float slideOffset) {

        }
    }

}
