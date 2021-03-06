package com.example.armani.unilocator.fragments;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import com.example.armani.unilocator.R;
import com.example.armani.unilocator.model.Unilocator;
import com.example.armani.unilocator.services.DataService;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MainFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MainFragment extends Fragment implements OnMapReadyCallback {


    private GoogleMap mMap; //This is the actual map that i can use
    private MarkerOptions userMarker;
    private LocationsListFragment mListFragment;


    public MainFragment() {
        // Required empty public constructor
    }

    public static MainFragment newInstance() {
        MainFragment fragment = new MainFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.v("DONKEY", "Check if this is being created");

        View view = inflater.inflate(R.layout.fragment_main, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        //This must be used to actually use the map
        mapFragment.getMapAsync(this);

        //Add the fragment of the List
        mListFragment = (LocationsListFragment)getActivity().getSupportFragmentManager().findFragmentById(R.id.container_locations_list);

        if (mListFragment == null) {
            mListFragment = LocationsListFragment.newInstance();
            getActivity().getSupportFragmentManager().beginTransaction().add(R.id.container_locations_list, mListFragment).commit();
        }

        //Someone does a search , they press enter key then pass that into the upday
        final EditText campusText = (EditText)view.findViewById(R.id.campus_text);
        campusText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if ((event.getAction() == KeyEvent.ACTION_DOWN) && keyCode == KeyEvent.KEYCODE_ENTER){

                String text = campusText.getText().toString();

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(campusText.getWindowToken(), 0);

                    showList();
                    updateMapForCampus(text);
                    return true;
            }

            return false;


        }
        });
        hideList();
        return view;

    }




    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

    }

    //Set a marker based on USER position
    public void setUserMarker(LatLng latLng) {
        if (userMarker == null){
            userMarker = new MarkerOptions().position(latLng).title("Current Location");
            mMap.addMarker(userMarker);
            Log.v("DONKEY" , "Current location: " + latLng.latitude + " Long: " + latLng.longitude);
        }
/*
        try {
            Geocoder geocoder = new Geocoder(getContext(), Locale.getDefault());
            List<Address> campusLoc = geocoder.getFromLocation(latLng.latitude,latLng.longitude, 1);
            String campus = campusLoc.get(0);
        } catch (IOException exception) {

        }
        */

        updateMapForCampus("stjohns");
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));

    }

    //Change to work with the update
    private void updateMapForCampus(String campus){

        //Check 10 mile radius for stuff
        ArrayList<Unilocator> locations = DataService.getInstance().getCampusLocationsWithin10milesofEnteredSite(campus);

        for (int x = 0; x < locations.size(); x++) {
            Unilocator loc = locations.get(x);
            MarkerOptions marker = new MarkerOptions().position(new LatLng(loc.getLatitude(), loc.getLongitude()));
            marker.title(loc.getUserTitle());
            marker.snippet(loc.getUserDescription());
            if (loc.getUserDescription() == "Sports related") { //change too
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.weight_map_pin));
                mMap.addMarker(marker);
                Log.v("DONKEY", "Hey this is the sports hall!");

            } else if (loc.getUserDescription() == "Lecture Hall") {
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.book_map_pin));
                mMap.addMarker(marker);
                Log.v("DONKEY", "Hey, this is the lec hall! ");

            } else {
                Log.v("DONKEY", "Hey you made it this far! ");
                //Default code looking pin
                marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_pin));
                mMap.addMarker(marker);
            }
        }
    }





    //Hides the List
    private void hideList(){
        getActivity().getSupportFragmentManager().beginTransaction().hide(mListFragment).commit();

    }

    //Shows the List
    private void showList(){
        getActivity().getSupportFragmentManager().beginTransaction().show(mListFragment).commit();
    }


}
