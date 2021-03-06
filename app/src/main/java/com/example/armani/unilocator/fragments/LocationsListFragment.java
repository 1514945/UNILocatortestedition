package com.example.armani.unilocator.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.armani.unilocator.R;
import com.example.armani.unilocator.adapters.LocationsAdapter;
import com.example.armani.unilocator.services.DataService;


public class LocationsListFragment extends Fragment {


    public LocationsListFragment() {
        // Required empty public constructor
    }


    // TODO: Rename and change types and number of parameters
    public static LocationsListFragment newInstance() {
        LocationsListFragment fragment = new LocationsListFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_locations_list, container, false);
        RecyclerView recyclerView = (RecyclerView)view.findViewById(R.id.recycler_locations);
        recyclerView.setHasFixedSize(true);

        LocationsAdapter adapter = new LocationsAdapter(DataService.getInstance().getUserDataForLocations("GYM"));
        // LocationsAdapter adapter = new LocationsAdapter(DataService.getInstance().getCampusLocationsWithin10milesofEnteredSite("GYM"));
        recyclerView.setAdapter(adapter);



        //Want this to pop up vertical
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.setLayoutManager(layoutManager);
        return view;
    }

}
