package com.iiysoftware.localtransport;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SelectLocationActivity extends AppCompatActivity implements LocationListener {
    int AUTOCOMPLETE_REQUEST_CODE = 1;
    private FirebaseAuth mAuth;
    private TextView totalFare, yourLocation;
    RelativeLayout selectStudent;
    Button calculate;
    EditText destinyLocation;
    private TextView myLocation, allTotal, totalDist;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    String student;
    Button myLocationBtn, calculateDist;
    private LocationManager locationManager;
    String latitude, longitude;
    public static final int REQUEST_LOCATION = 100;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_location);

        student = getIntent().getStringExtra("student");

        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION},REQUEST_LOCATION);

        mAuth = FirebaseAuth.getInstance();
        String currentUid = mAuth.getCurrentUser().getUid();
        myLocation = findViewById(R.id.selected_location_text);
        totalFare = findViewById(R.id.total_fare);
        yourLocation = findViewById(R.id.my_location_text);
        selectStudent = findViewById(R.id.select_student_lay);
        calculate = findViewById(R.id.calculate_distance_btn);
        destinyLocation = findViewById(R.id.search_location_field);
        final TextView studentName = findViewById(R.id.student_name);
        allTotal = findViewById(R.id.all_total_fair);
        totalDist = findViewById(R.id.total_distance);
        calculateDist = findViewById(R.id.calculte_distance_with_mLocation);

        db.collection("Parent").document(student).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    String name = task.getResult().get("student_name").toString();
                    String dest = task.getResult().get("destiny").toString();
                    double dis = task.getResult().getDouble("travel");

                    calculateFair(dis);
                    totalDist.setText("Total Distance:" + " " + dis + " " + "KM");
                    studentName.setText(name);
                    destinyLocation.setText(dest);
                }
            }
        });

        myLocationBtn = findViewById(R.id.get_my_location_btn);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    Activity#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for Activity#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);

        myLocationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getMyLocation();
            }
        });

        Places.initialize(getApplicationContext(), "AIzaSyDD7dcn5EYWSw0PUVMF_-k7KdEY4nuNxiA");

// Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(this);

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyDD7dcn5EYWSw0PUVMF_-k7KdEY4nuNxiA");
        }

// Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragmentMy = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.autocomplete_fragment_my);

        autocompleteFragmentMy.setCountry("IN");    //country type

        autocompleteFragmentMy.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragmentMy.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                String name = place.getName();
                LatLng latlng = place.getLatLng();
                destinyLocation.setText(place.getName());
                Toast.makeText(SelectLocationActivity.this,  place.getName(), Toast.LENGTH_SHORT).show();
                Log.i("", "Place: " + place.getName() + ", " + place.getId()+"" + place.getLatLng());
                calculate.setVisibility(View.VISIBLE);
                calculateDist.setVisibility(View.GONE);
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("", "An error occurred: " + status);
            }
        });

        db.collection("LocalTransport").document(currentUid)
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful())
                {
                    String place = task.getResult().get("place").toString();
                    long lat = task.getResult().getLong("latitude");
                    long lng = task.getResult().getLong("longitude");
                    yourLocation.setText(place);

                }
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = destinyLocation.getText().toString();
                String myLocation = yourLocation.getText().toString();

                if (!location.equals("") || !myLocation.equals(""))
                {
                    if ((location != null || !location.equals("")) && (myLocation != null || !myLocation.equals(""))) {
                        List<Address> addressList1 = null;
                        List<Address> addressList2 = null;
                        Geocoder geocoder = new Geocoder(SelectLocationActivity.this);
                        try {
                            addressList1 = geocoder.getFromLocationName(location, 1);
                            addressList2 = geocoder.getFromLocationName(myLocation, 1);

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        Address myaddress = addressList1.get(0);
                        Address destiny = addressList2.get(0);

                        LatLng originAdd = new LatLng(myaddress.getLatitude(), myaddress.getLongitude());
                        LatLng destAdd = new LatLng(destiny.getLatitude(), destiny.getLongitude());

                        Toast.makeText(SelectLocationActivity.this,myaddress.getLatitude()+" "+myaddress.getLongitude()+"  " +destiny.getLatitude()+" " +destiny.getLongitude(),Toast.LENGTH_LONG).show();

                        //handle distance
                        String str_origin = "origin=" + originAdd.latitude + "," + originAdd.longitude;
                        String str_dest = "destination=" + destAdd.latitude + "," + destAdd.longitude;
                        String sensor = "sensor=false";
                        String parameters = str_origin + "&" + str_dest + "&" + sensor;
                        String output = "json";
                        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

                        Log.d("onMapClick", url.toString());
                        RouteDisFrag.FetchUrl FetchUrl = new RouteDisFrag.FetchUrl();
                        FetchUrl.execute(url);

                        Location originLoc = new Location("Delhi");
                        originLoc.setLatitude(originAdd.latitude);
                        originLoc.setLongitude(originAdd.longitude);

                        Location destLoc = new Location("Chandigarh");
                        destLoc.setLatitude(destAdd.latitude);
                        destLoc.setLongitude(destAdd.longitude);

                        double distance = (originLoc.distanceTo(destLoc))* 0.001 ;
                        // here we will get our result in metre so we multiply it by  0.000621371 to convert it to miles

                        totalDist.setText("Total Distance:" + " " + distance + " " + "KM");

                        calculateFair(distance);
                        HashMap<String, Object> hashMap = new HashMap<>();
                        hashMap.put("travel", distance);
                        hashMap.put("destiny", location);

                        db.collection("Parent").document(student).update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(SelectLocationActivity.this, "Saved on database", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    else {
                        Toast.makeText(SelectLocationActivity.this, "fill my location and destiny both fields", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(SelectLocationActivity.this, "fill my location and destiny both fields", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getMyLocation() {
        calculate.setVisibility(View.GONE);
        calculateDist.setVisibility(View.VISIBLE);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            Toast.makeText(this, "Permission not granted", Toast.LENGTH_SHORT).show();
        }else if (locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)){
            getLocation();
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(SelectLocationActivity.this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                    SelectLocationActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED ){
            ActivityCompat.requestPermissions(SelectLocationActivity.this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        }else {
            Location location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            if (location != null){
                final double latt = location.getLatitude();
                final double longit = location.getLongitude();
                latitude = String.valueOf(latt);
                longitude = String.valueOf(longit);

                destinyLocation.setText("Current Location:"+ "\n" +
                        "Latitude:"+ latitude+ "\n" +
                        "Longitude"+ longitude);

                calculateDist.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String location = destinyLocation.getText().toString();
                        String myLocation = yourLocation.getText().toString();

                        if (!location.equals("") || !myLocation.equals(""))
                        {
                            if ((location != null || !location.equals("")) && (myLocation != null || !myLocation.equals(""))) {
                                List<Address> addressList1 = null;
                                Geocoder geocoder = new Geocoder(SelectLocationActivity.this);
                                try {
                                    addressList1 = geocoder.getFromLocationName(myLocation, 1);

                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Address myaddress = addressList1.get(0);

                                LatLng originAdd = new LatLng(myaddress.getLatitude(), myaddress.getLongitude());
                                LatLng destAdd = new LatLng(latt, longit);

                                Toast.makeText(SelectLocationActivity.this,myaddress.getLatitude()+" "+myaddress.getLongitude()+"  " +latt+" " +longit,Toast.LENGTH_LONG).show();

                                //handle distance
                                String str_origin = "origin=" + originAdd.latitude + "," + originAdd.longitude;
                                String str_dest = "destination=" + latt + "," + longit;
                                String sensor = "sensor=false";
                                String parameters = str_origin + "&" + str_dest + "&" + sensor;
                                String output = "json";
                                String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

                                Log.d("onMapClick", url.toString());
                                RouteDisFrag.FetchUrl FetchUrl = new RouteDisFrag.FetchUrl();
                                FetchUrl.execute(url);

                                Location originLoc = new Location("Delhi");
                                originLoc.setLatitude(originAdd.latitude);
                                originLoc.setLongitude(originAdd.longitude);

                                Location destLoc = new Location("Chandigarh");
                                destLoc.setLatitude(latt);
                                destLoc.setLongitude(longit);

                                double distance = (originLoc.distanceTo(destLoc))* 0.001 ;
                                // here we will get our result in metre so we multiply it by  0.000621371 to convert it to miles

                                totalDist.setText("Total Distance:" + " " + distance + " " + "KM");

                                calculateFair(distance);
                                HashMap<String, Object> hashMap = new HashMap<>();
                                hashMap.put("travel", distance);
                                hashMap.put("destiny", location);

                                db.collection("Parent").document(student).update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(SelectLocationActivity.this, "Saved on database", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                            else {
                                Toast.makeText(SelectLocationActivity.this, "fill my location and destiny both fields", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else {
                            Toast.makeText(SelectLocationActivity.this, "fill my location and destiny both fields", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else {
                Toast.makeText(this, "Unable to trace location", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void calculateFair(double distance) {
        double d = distance;
        if (d>0 && d<=1){
            totalFare.setText("Charge Fair: 303.2Rs");
            allTotal.setText("Total Fair: 328.2Rs");
        }else if (d>1 && d<=2){
            totalFare.setText("Charge amount: 373.2Rs");
            allTotal.setText("Total Fair: 388.2Rs");
        }else if (d>2 && d<=3){
            totalFare.setText("Charge amount: 453.2Rs");
            allTotal.setText("Total Fair: 468.2Rs");

        }else if (d>3 && d<=4){
            totalFare.setText("Charge amount: 523.2Rs");
            allTotal.setText("Total Fair: 538.2Rs");

        }else if (d>4 && d<=5){
            totalFare.setText("Charge amount: 553.2Rs");
            allTotal.setText("Total Fair: 568.2Rs");

        }else if (d>5 && d<=6){
            totalFare.setText("Charge amount: 593.2Rs");
            allTotal.setText("Total Fair: 608.2Rs");

        }else if (d>6 && d<=7){
            totalFare.setText("Charge amount: 633.2Rs");
            allTotal.setText("Total Fair: 648.2Rs");

        }else if (d>7 && d<=8){
            totalFare.setText("Charge amount: 673.2Rs");
            allTotal.setText("Total Fair: 688.2Rs");

        }else if (d>8 && d<=9){
            totalFare.setText("Charge amount: 703.2Rs");
            allTotal.setText("Total Fair: 718.2Rs");

        }else if (d>9 && d<=10){
            totalFare.setText("Charge amount: 733.2Rs");
            allTotal.setText("Total Fair: 748.2Rs");

        }else if (d>10 && d<=11){
            totalFare.setText("Charge amount: 773.2Rs");
            allTotal.setText("Total Fair: 788.2Rs");

        }else if (d>11 && d<=12){
            totalFare.setText("Charge amount: 803.2Rs");
            allTotal.setText("Total Fair: 818.2Rs");

        }else if (d>12 && d<=13){
            totalFare.setText("Charge amount: 843.2Rs");
            allTotal.setText("Total Fair: 858.2Rs");

        }else if (d>13 && d<=14){
            totalFare.setText("Charge amount: 883.2Rs");
            allTotal.setText("Total Fair: 898.2Rs");

        }else if (d>14 && d<=15){
            totalFare.setText("Charge amount: 913.2Rs");
            allTotal.setText("Total Fair: 928.2Rs");

        }else if (d>15 && d<=16){
            totalFare.setText("Charge amount: 933.2Rs");
            allTotal.setText("Total Fair: 948.2Rs");

        }else if (d>16 && d<=17){
            totalFare.setText("Charge amount: 963.2Rs");
            allTotal.setText("Total Fair: 978.2Rs");

        }else if (d>17 && d<=18){
            totalFare.setText("Charge amount: 993.2Rs");
            allTotal.setText("Total Fair: 1018.2Rs");

        }else if (d>18 && d<=19){
            totalFare.setText("Charge amount: 1013.2Rs");
            allTotal.setText("Total Fair: 1028.2Rs");

        }else if (d>19 && d<=20){
            totalFare.setText("Charge amount: 1043.2Rs");
            allTotal.setText("Total Fair: 1058.2Rs");

        }else if (d>20 && d<=21){
            totalFare.setText("Charge amount: 1073.2Rs");
            allTotal.setText("Total Fair: 1088.2Rs");

        }else if (d>21 && d<=22){
            totalFare.setText("Charge amount: 1123.2Rs");
            allTotal.setText("Total Fair: 1138.2Rs");

        }else if (d>22 && d<=23){
            totalFare.setText("Charge amount: 1143.2Rs");
            allTotal.setText("Total Fair: 1158.2Rs");

        }else if (d>23 && d<=24){
            totalFare.setText("Charge amount: 1163.2Rs");
            allTotal.setText("Total Fair: 1178.2Rs");

        }else if (d>24 && d<=25){
            totalFare.setText("Charge amount: 1183.2Rs");
            allTotal.setText("Total Fair: 1198.2Rs");

        }else if (d>25 && d<=26){
            totalFare.setText("Charge amount: 1203.2Rs");
            allTotal.setText("Total Fair: 1218.2Rs");

        }else if (d>26 && d<=27){
            totalFare.setText("Charge amount: 1223.2Rs");
            allTotal.setText("Total Fair: 1238.2Rs");

        }else if (d>27 && d<=28){
            totalFare.setText("Charge amount: 1243.2Rs");
            allTotal.setText("Total Fair: 1258.2Rs");

        }else if (d>28 && d<=29){
            totalFare.setText("Charge amount: 1263.2Rs");
            allTotal.setText("Total Fair: 1278.2Rs");

        }else if (d>29 && d<=30){
            totalFare.setText("Charge amount: 1283.2Rs");
            allTotal.setText("Total Fair: 1298.2Rs");

        }else if (d>30 && d<=31){
            totalFare.setText("Charge amount: 1303.2Rs");
            allTotal.setText("Total Fair: 1318.2Rs");

        }else if (d>31 && d<=32){
            totalFare.setText("Charge amount: 1323.2Rs");
            allTotal.setText("Total Fair: 1338.2Rs");

        }else if (d>32 && d<=33){
            totalFare.setText("Charge amount: 1343.2Rs");
            allTotal.setText("Total Fair: 1358.2Rs");

        }else if (d>33 && d<=34){
            totalFare.setText("Charge amount: 1363.2Rs");
            allTotal.setText("Total Fair: 1378.2Rs");

        }else if (d>34 && d<=35){
            totalFare.setText("Charge amount: 1383.2Rs");
            allTotal.setText("Total Fair: 1398.2Rs");

        }else if (d>35 && d<=36){
            totalFare.setText("Charge amount: 1403.2Rs");
            allTotal.setText("Total Fair: 1418.2Rs");

        }else if (d>36 && d<=37){
            totalFare.setText("Charge amount: 1418.2Rs");
            allTotal.setText("Total Fair: 1433.2Rs");

        }else if (d>37 && d<=38){
            totalFare.setText("Charge amount: 1433.2Rs");
            allTotal.setText("Total Fair: 1448.2Rs");

        }else if (d>38 && d<=39){
            totalFare.setText("Charge amount: 1448.2Rs");
            allTotal.setText("Total Fair: 1463.2Rs");

        }else if (d>39 && d<=40){
            totalFare.setText("Charge amount: 1463.2Rs");
            allTotal.setText("Total Fair: 1478.2Rs");

        }else {
            totalFare.setText("Charge amount: 1463.2Rs");
            allTotal.setText("Total Fair: 1478.2Rs");
        }

        String tot = allTotal.getText().toString();
        HashMap<String, Object> hashMap = new HashMap<>();
        hashMap.put("fair", tot);
        db.collection("Parent").document(student).update(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Toast.makeText(SelectLocationActivity.this, "Saved on database", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    private class FetchUrl extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... url) {
            String data = "";

            try {
                data = downloadUrl(url[0]);
                Log.d("Background Task data", data.toString());
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            RouteDisFrag.ParserTask parserTask = new RouteDisFrag.ParserTask();
            parserTask.execute(result);

        }
    }

    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            Log.d("downloadUrl", data.toString());
            br.close();
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);
                Log.d("ParserTask",jsonData[0].toString());
                JSONParserTask parser = new JSONParserTask();
                Log.d("ParserTask", parser.toString());
                routes = parser.parse(jObject);
                Log.d("ParserTask","Executing routes");
                Log.d("ParserTask",routes.toString());

            } catch (Exception e) {
                Log.d("ParserTask",e.toString());
                e.printStackTrace();
            }
            return routes;
        }
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {
            ArrayList<LatLng> points;
            PolylineOptions lineOptions = null;
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<>();
                lineOptions = new PolylineOptions();
                List<HashMap<String, String>> path = result.get(i);
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);
                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);
                    points.add(position);
                }
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);

                Log.d("onPostExecute","onPostExecute lineoptions decoded");

            }
            if(lineOptions != null) {
//                mMap.addPolyline(lineOptions);
            }
            else {
                Log.d("onPostExecute","without Polylines drawn");
            }
        }
    }
}
