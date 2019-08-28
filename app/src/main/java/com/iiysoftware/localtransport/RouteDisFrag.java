package com.iiysoftware.localtransport;


import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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
import java.util.HashMap;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class RouteDisFrag extends Fragment {

    private TextView totalFare, yourLocation;
    RelativeLayout selectStudent;
    Button calculate;
    EditText searchLocation;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();

    private FirebaseAuth mAuth;

    public static final String TAG = "AutoCompleteActivity";
    private static final int AUTO_COMP_REQ_CODE = 2;

    public RouteDisFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_route_dis, container, false);

        String pla = getArguments().getString("name");

        mAuth = FirebaseAuth.getInstance();
        String currentUid = mAuth.getCurrentUser().getUid();
        totalFare = view.findViewById(R.id.total_fare);
        yourLocation = view.findViewById(R.id.selected_location_text);
        selectStudent = view.findViewById(R.id.select_student_lay);
        calculate = view.findViewById(R.id.calculate_distance_btn);
        searchLocation = view.findViewById(R.id.search_location_field);

        searchLocation.setText(pla);

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

                    searchLocation.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(getActivity(), SelectLocationActivity.class);
                            startActivity(intent);
                        }
                    });
                }
            }
        });

        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String location = searchLocation.getText().toString();
                String myLocation = yourLocation.getText().toString();

                if (!location.equals("") || !myLocation.equals(""))
                {
                    if ((location != null || !location.equals("")) && (myLocation != null || !myLocation.equals(""))) {
                        List<Address> addressList1 = null;
                        List<Address> addressList2 = null;
                        Geocoder geocoder = new Geocoder(getActivity());
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

                        Toast.makeText(getContext(),myaddress.getLatitude()+" "+myaddress.getLongitude()+"  " +destiny.getLatitude()+" " +destiny.getLongitude(),Toast.LENGTH_LONG).show();

                        //handle distance
                        String str_origin = "origin=" + originAdd.latitude + "," + originAdd.longitude;
                        String str_dest = "destination=" + destAdd.latitude + "," + destAdd.longitude;
                        String sensor = "sensor=false";
                        String parameters = str_origin + "&" + str_dest + "&" + sensor;
                        String output = "json";
                        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

                        Log.d("onMapClick", url.toString());
                        FetchUrl FetchUrl = new FetchUrl();
                        FetchUrl.execute(url);

                        Location originLoc = new Location("Delhi");
                        originLoc.setLatitude(originAdd.latitude);
                        originLoc.setLongitude(originAdd.longitude);

                        Location destLoc = new Location("Chandigarh");
                        destLoc.setLatitude(destAdd.latitude);
                        destLoc.setLongitude(destAdd.longitude);

                        double distance = (originLoc.distanceTo(destLoc))* 0.001 ;
                        // here we will get our result in metre so we multiply it by  0.000621371 to convert it to miles

                        totalFare.setText("Total Distance:" + " " + distance + " " + "KM");


                    }
                    else {
                        Toast.makeText(getActivity(), "fill my location and destiny both fields", Toast.LENGTH_SHORT).show();
                    }
                }
                else {
                    Toast.makeText(getActivity(), "fill my location and destiny both fields", Toast.LENGTH_SHORT).show();

                }
            }
        });

        return view;

    }

    static class FetchUrl extends AsyncTask<String, Void, String> {

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

            ParserTask parserTask = new ParserTask();
            parserTask.execute(result);

        }
    }

    private static String downloadUrl(String strUrl) throws IOException {
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
    protected static class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {
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
