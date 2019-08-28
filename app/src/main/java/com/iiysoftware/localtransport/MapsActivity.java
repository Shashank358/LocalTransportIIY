package com.iiysoftware.localtransport;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.iiysoftware.localtransport.NewPackage.Location;

import java.util.List;
import java.util.Locale;

import javax.annotation.Nullable;


public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    MapView mapView;
    //    FirebaseDatabase database;
//    DatabaseReference myRef;
    FirebaseFirestore db;
    String driverId = "";
    EditText addresset;
    Geocoder geocoder;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        uid = getIntent().getStringExtra("uid");
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        mapView = (MapView) findViewById(R.id.map);
        addresset = findViewById(R.id.address);
        addresset.setKeyListener(null);
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(this);
        //database = FirebaseDatabase.getInstance();
        db = FirebaseFirestore.getInstance();

        geocoder = new Geocoder(this, Locale.getDefault());
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        db.collection("Drivers").document(uid).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot documentSnapshot, @Nullable FirebaseFirestoreException e) {
                Location value = new Location();
                value.setLatittude(documentSnapshot.get("latitude").toString());
                value.setLongitude(documentSnapshot.get("longitude").toString());
                assert value != null;
                LatLng location = new LatLng(Double.parseDouble(value.getLatittude()),
                        Double.parseDouble(value.getLongitude()));


                MarkerOptions marker = new MarkerOptions().position(location);

                List<Address> addresses;
                try {
                    addresses = (List<Address>) geocoder.getFromLocation(
                            Double.parseDouble(value.getLatittude()),
                            Double.parseDouble(value.getLongitude()), 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

                    String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                    String city = addresses.get(0).getLocality();
//                                                String state = addresses.get(0).getAdminArea();
//                                                String country = addresses.get(0).getCountryName();
//                                                String postalCode = addresses.get(0).getPostalCode();
//                                                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL
                    addresset.setText(address + "," + city);
                }catch (Exception ee){
                    ee.printStackTrace();
                }
                // Changing marker icon
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.school_bus));
                mMap.clear();
                mMap.addMarker(marker);
                final CameraPosition cameraPosition = new CameraPosition.Builder()
                        .target(location)      // Sets the center of the map to User Position
                        .zoom(16)                         // Sets the zoom
                        .bearing(0)                      // Sets the orientation of the camera to east
                        .tilt(30)                         // Sets the tilt of the camera to 30 degrees
                        .build();                         //
                mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
            }
        });
    }
}

