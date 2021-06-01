package com.example.map;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;

import java.util.List;

public class MapFragment extends Fragment implements GoogleMap.OnMarkerClickListener{

    GoogleMap mainMap;
    //current and destination location objects
    Location myLocation=null;
    Location destinationLocation=null;
    protected LatLng start=null;
    protected LatLng end=null;

    //to get location permissions.
    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission=false;

    //polyline object
    private List<Polyline> polylines=null;


    private OnMapReadyCallback callback = new OnMapReadyCallback() {


        @Override
        public void onMapReady(GoogleMap googleMap) {
            mainMap = googleMap;
            LatLng kazan = new LatLng(56,50);
            LatLng kazan1 = new LatLng(55,50);
            LatLng kazan2 = new LatLng(57,50);
            Marker m1 = googleMap.addMarker(new MarkerOptions().position(kazan).title("Marker in Kazan"));
            Marker m2 = googleMap.addMarker(new MarkerOptions().position(kazan1).title("Marker in Kazan"));
            Marker m3 = googleMap.addMarker(new MarkerOptions().position(kazan2).title("Marker in Kazan"));
            m1.setTag(1);
            m2.setTag(2);
            m3.setTag(3);
            mainMap.setOnMarkerClickListener(MapFragment.this::onMarkerClick);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(kazan, 5));
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_map, container, false);
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {
        switch ((int)marker.getTag()){
            case 1:
                Toast.makeText(getContext(), "1",Toast.LENGTH_SHORT).show();
                break;
            case 2:
                Toast.makeText(getContext(), "2",Toast.LENGTH_SHORT).show();
                break;
            case 3:
                Toast.makeText(getContext(), "3",Toast.LENGTH_SHORT).show();
                break;
        }
        return false;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        super.onCreateOptionsMenu(menu,inflater);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        requestPermision();
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    private void requestPermision()
    {
        if(ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        }
        else{
            locationPermission=true;
        }
    }
}
