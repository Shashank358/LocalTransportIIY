package com.iiysoftware.localtransport;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 */
public class SelectFrag extends Fragment {

    int AUTOCOMPLETE_REQUEST_CODE = 1;

    private TextView myLocation;
    public SelectFrag() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_select, container, false);

        myLocation = view.findViewById(R.id.selected_location_text);

        Places.initialize(getContext(), "AIzaSyDD7dcn5EYWSw0PUVMF_-k7KdEY4nuNxiA");

// Create a new Places client instance.
        PlacesClient placesClient = Places.createClient(getActivity());

        if (!Places.isInitialized()) {
            Places.initialize(getContext(), "AIzaSyDD7dcn5EYWSw0PUVMF_-k7KdEY4nuNxiA");
        }

// Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragmentMy = (AutocompleteSupportFragment)
                getFragmentManager().findFragmentById(R.id.autocomplete_fragment_my);

        autocompleteFragmentMy.setCountry("IN");    //country type

        autocompleteFragmentMy.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        autocompleteFragmentMy.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                // TODO: Get info about the selected place.
                String name = place.getName();
                LatLng latlng = place.getLatLng();
                myLocation.setText(place.getName());
                Intent intent = new Intent(getActivity(), MainActivity.class);
                intent.putExtra("key", "select");
                intent.putExtra("place", name);
                startActivity(intent);
                Toast.makeText(getActivity(), "" + place.getLatLng(), Toast.LENGTH_SHORT).show();
                Log.i("", "Place: " + place.getName() + ", " + place.getId());
            }

            @Override
            public void onError(Status status) {
                // TODO: Handle the error.
                Log.i("", "An error occurred: " + status);
            }
        });

        return view;
    }

}
