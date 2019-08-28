package com.iiysoftware.localtransport;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.util.Arrays;
import java.util.List;

import static com.android.volley.VolleyLog.TAG;

public class TestActivity extends AppCompatActivity {

    public EditText startLocationField;
    public EditText endLocationField;
    private PlacesClient placesClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        startLocationField = findViewById(R.id.Startfield);
        endLocationField = findViewById(R.id.Endfield);

        Places.initialize(getApplicationContext(), "AIzaSyCHdvmjWeZmd2LjrDnYSLv9QdwdBa21JwE");
        placesClient = Places.createClient(getApplicationContext());

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyCHdvmjWeZmd2LjrDnYSLv9QdwdBa21JwE");
        }

        startLocationField.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Set the fields to specify which types of place data to return.
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);

                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(
                        AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(getApplicationContext());
                startActivityForResult(intent, 1000);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        /**
         * This is called when the user has selected a location from the prediction box.
         * data contatins the place, extracted by getPlaceFromIntent() method.
         * use this place to get address and display
         * also get Latitude and longitude and put it into the map fragment LatLng holder.
         */
        if (requestCode == 1000) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                Log.i(TAG, "selected place: " + place.getName() + ", " + place.getId());

                Toast.makeText(this, place.getName(), Toast.LENGTH_SHORT).show();

            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i(TAG, status.getStatusMessage()) ;
                Toast.makeText(this, "" + status.getStatus(), Toast.LENGTH_SHORT).show();

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
        }
    }

}
