    package com.example.map.ui.home;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.map.MyOpenHelper;
import com.example.map.R;

import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;

import com.directions.route.AbstractRouting;
import com.directions.route.Route;
import com.directions.route.RouteException;
import com.directions.route.Routing;
import com.directions.route.RoutingListener;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class HomeFragment extends Fragment implements GoogleMap.OnMarkerClickListener, OnMapReadyCallback,
        GoogleApiClient.OnConnectionFailedListener, RoutingListener {

    //google map object
    private GoogleMap mMap;

    //current and destination location objects
    Location myLocation = null;
    Location destinationLocation = null;
    protected LatLng start = null;
    protected LatLng end = null;

    //Buttons
    Button SearchBtn, plusZoom, minusZoom;
    ImageButton GoBtn;
    FloatingActionButton fabLoc, fabPlusMark;

    //to get location permissions.
    private final static int LOCATION_REQUEST_CODE = 23;
    boolean locationPermission = false;
    boolean fr_start = true;

    //polyline object
    private List<Polyline> polylines = null;

    //active switch
    private ArrayList<Switch> activeSwitch = new ArrayList<>();

    //Sqlite DB
    static MyOpenHelper dbHelper;
    SQLiteDatabase db;

    boolean isCategoryRepeat = false;

    Marker m1 = null, m3 = null,
            m4 = null;
    Polyline p1 = null, p2 = null;

    private OnMapReadyCallback callback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mMap = googleMap;

            dbHelper = new MyOpenHelper(getContext(), "MyDB3", null, 1);
            db = dbHelper.getReadableDatabase();

            if(isGeoDisabled()) {
                Dialog d = new DialogWithGeo().createDialog();
                d.show();
            }

            if (locationPermission) {
                getMyLocation();
            }

            if(fr_start) {
                try {
                    LatLng ltlng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                            ltlng, 16f);
                    mMap.animateCamera(cameraUpdate);
                    fr_start = false;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    };

    //to get user location
    void getMyLocation() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location location) {
                myLocation = location;
//                if(fr_start) {
//                    LatLng ltlng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
//                    CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
//                            ltlng, zoom);
//                    mMap.animateCamera(cameraUpdate);
//                    fr_start = false;
//                }
            }
        });

        //get destination location when user click on map
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                end = latLng;

                if( m1 != null) {
                    if (Objects.equals(m1.getTag(), 0))
                        m1.remove();
                }

                if( p1 != null && p2 != null) {
                    if (Objects.equals(p1.getTag(), 0) && Objects.equals(p2.getTag(), 0)) {
                        p1.remove();
                        p2.remove();
                    }
                }
                if( m3 != null || m4 != null){
                    if (Objects.equals(m3.getTag(), 0) && Objects.equals(m4.getTag(), 0)) {
                        m3.remove();
                        m4.remove();
                    }
                }

                start = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());

                m1 = mMap.addMarker(new MarkerOptions().position(end));

                assert m1 != null;
                m1.setTag(0);

                mMap.setOnMarkerClickListener(HomeFragment.this::onMarkerClick);
                mMap.setOnMarkerClickListener(marker -> {
                    start = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    end = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                    return false;
                });
            }
        });

    }

    public boolean isGeoDisabled() {
        LocationManager mLocationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean mIsGPSEnabled = mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean mIsNetworkEnabled = mLocationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        boolean mIsGeoDisabled = !mIsGPSEnabled && !mIsNetworkEnabled;
        return mIsGeoDisabled;
    }

    // function to find Routes.
    public void Findroutes(LatLng Start, LatLng End) {
        if (Start == null || End == null) {
            Toast.makeText(getContext(), "Unable to get location", Toast.LENGTH_LONG).show();
        } else {

            Routing routing = new Routing.Builder()
                    .travelMode(AbstractRouting.TravelMode.WALKING)
                    .withListener(this)
                    .alternativeRoutes(true)
                    .waypoints(Start, End)
                    .key("AIzaSyC003GAnyHOz7ZRTBG-MIPb1wUUqmm1iC8")  //also define your api key here.
                    .build();
            routing.execute();
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        requestPermision();
        View v = inflater.inflate(R.layout.fragment_map, container, false);

        SearchBtn = v.findViewById(R.id.SearchBtn);
        fabLoc = v.findViewById(R.id.fab);
        fabPlusMark = v.findViewById(R.id.PlusMarkerBtn);
        plusZoom = v.findViewById(R.id.PlusZoomBtn);
        minusZoom = v.findViewById(R.id.minusZoomBtn);
        GoBtn = v.findViewById(R.id.GoBtn);

        plusZoom.setOnClickListener( b-> {
            CameraUpdate cameraUpdate = CameraUpdateFactory.zoomBy(1f);
            mMap.animateCamera(cameraUpdate);
        });

        minusZoom.setOnClickListener( b-> {
            CameraUpdate cameraUpdate = CameraUpdateFactory.zoomBy(-1f);
            mMap.animateCamera(cameraUpdate);
        });

        fabLoc.setOnClickListener( b-> {
            if(myLocation == null) { getMyLocation(); }
            else {
                LatLng ltlng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
                        ltlng, 16f);
                mMap.animateCamera(cameraUpdate);
            }
        });

        SearchBtn.setOnClickListener( b-> {
                mMap.clear();
                if(m1 != null && m3 != null && m4 != null) {
                    m1.remove();
                    m3.remove();
                    m4.remove();
                }

                Dialog d = new WorkWithDialog().createDialog();
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
        });

        GoBtn.setOnClickListener( b-> {
            Findroutes(start, end);
        });

        return v;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_REQUEST_CODE: {
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //if permission granted.
                    locationPermission = true;
                    getMyLocation();

                } else {
                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }


    private void requestPermision() {
        if (ContextCompat.checkSelfPermission(getContext(),
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                    LOCATION_REQUEST_CODE);
        } else {
            locationPermission = true;
        }
    }

    @Override
    public boolean onMarkerClick(@NonNull Marker marker) {

        //Toast.makeText(getContext(), "Hello", Toast.LENGTH_LONG).show();
//        LatLng start = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
//        LatLng end = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
//        Findroutes(start, end);

        return false;
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        SupportMapFragment mapFragment =
                (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(callback);
        }
    }

    @Override
    public void onRoutingFailure(RouteException e) {
        Findroutes(start,end);
    }

    @Override
    public void onRoutingStart() {
    }

    @Override
    public void onRoutingSuccess(ArrayList<Route> route, int shortestRouteIndex) {
        CameraUpdate center = CameraUpdateFactory.newLatLng(start);
        CameraUpdate zoom = CameraUpdateFactory.zoomTo(16);
        if (polylines != null) {
            polylines.clear();
        }
        PolylineOptions polyOptions = new PolylineOptions();
        LatLng polylineStartLatLng = null;
        LatLng polylineEndLatLng = null;


        polylines = new ArrayList<>();
        //add route(s) to the map using polyline
        for (int i = 0; i < route.size(); i++) {

            if (i == shortestRouteIndex) {
                polyOptions.color(getResources().getColor(R.color.black));
                polyOptions.width(7);
                polyOptions.addAll(route.get(shortestRouteIndex).getPoints());
                p1 = mMap.addPolyline(polyOptions);
                p1.setTag(0);
                polylineStartLatLng = p1.getPoints().get(0);
                int k = p1.getPoints().size();
                polylineEndLatLng = p1.getPoints().get(k - 1);
                polylines.add(p1);
            } else {

            }
        }

        //Add Marker on route starting position
        MarkerOptions startMarker = new MarkerOptions();
        assert polylineStartLatLng != null;
        startMarker.position(polylineStartLatLng);
        Objects.requireNonNull(m3 = mMap.addMarker(startMarker));
        m3.setTag(0);


        //Add Marker on route ending position
        MarkerOptions endMarker = new MarkerOptions();
        assert polylineEndLatLng != null;
        endMarker.position(polylineEndLatLng);
        Objects.requireNonNull(m4 = mMap.addMarker(endMarker));
        m4.setVisible(false);
        m4.setTag(0);

        PolylineOptions options = new PolylineOptions().width(7).color(Color.BLUE).geodesic(true);
        options.add(m4.getPosition());
        options.add( new LatLng (end.latitude, end.longitude));
        p2 = mMap.addPolyline(options);
        p2.setTag(0);
    }

    @Override
    public void onRoutingCancelled() {
        Findroutes(start,end);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Findroutes(start,end);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

    }

    class WorkWithDialog{
        private AlertDialog.Builder builder;
        private Dialog d;
        CardView relax, health, eat,
                    shop, money, other;

        public WorkWithDialog(){
            LayoutInflater factory = LayoutInflater.from(getContext());
            builder = new AlertDialog.Builder(getContext());
            View dialogView = factory.inflate(R.layout.category, null);
            builder.setView(dialogView);

            relax = dialogView.findViewById(R.id.CardRelaxation);
            health = dialogView.findViewById(R.id.CardHealth);
            eat = dialogView.findViewById(R.id.CardEat);
            shop = dialogView.findViewById(R.id.CardShop);
            money = dialogView.findViewById(R.id.CardMoney);
            other = dialogView.findViewById(R.id.CardOther);

            relax.setOnClickListener(n->{
                Dialog d = new WorkWithCategory(1).createDialog();
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
            });
            health.setOnClickListener(n->{
                Dialog d = new WorkWithCategory(2).createDialog();
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
            });
            eat.setOnClickListener(n->{
                Dialog d = new WorkWithCategory(3).createDialog();
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
            });
            shop.setOnClickListener(n->{
                Dialog d = new WorkWithCategory(4).createDialog();
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
            });
            money.setOnClickListener(n->{
                Dialog d = new WorkWithCategory(5).createDialog();
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
            });
            other.setOnClickListener(n->{
                Dialog d = new WorkWithCategory(6).createDialog();
                d.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                d.show();
            });


            Button btn = dialogView.findViewById(R.id.saveSettingsBtnCategory);
            btn.setOnClickListener(b -> {
                for(int i = 0; i < activeSwitch.size(); i++){
                    @SuppressLint("Recycle") Cursor cursor2 = db.rawQuery("SELECT * FROM markers WHERE idCategory = ?",new String[]{activeSwitch.get(i).getText().toString()});
                    if(cursor2.getCount() > 0){
                        cursor2.moveToFirst();
                        mMap.clear();
                        for (int j = 1; j <= cursor2.getCount(); j++){

                            LatLng pos = new LatLng(cursor2.getFloat(cursor2.getColumnIndex("lat")),
                                    cursor2.getFloat(cursor2.getColumnIndex("lot")));

                            Objects.requireNonNull(mMap.addMarker(new MarkerOptions().position(pos).title(cursor2.getString(cursor2.getColumnIndex("name"))))).setTag(1);
                            cursor2.moveToNext();
                        }

                    }

                }
                closeDialog();
            });
        }

        public Dialog createDialog(){
            d =  builder.create();
            return d;
        }
        public void closeDialog(){
            d.cancel();
        }


    }

    class DialogWithGeo{
    private AlertDialog.Builder builder;
    private Dialog d;
    public DialogWithGeo(){ ;
        LayoutInflater factory = LayoutInflater.from(getContext());
        builder = new AlertDialog.Builder(getContext());
        View dialogView = factory.inflate(R.layout.geo, null);
        builder.setView(dialogView);

        Button btn = dialogView.findViewById(R.id.GoGeoBnt);
        btn.setOnClickListener(b -> {
            startActivity(new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            closeDialog();
        });
    }

    public Dialog createDialog(){
        d =  builder.create();
        return d;
    }
    public void closeDialog(){
        d.cancel();
    }
    }

    class WorkWithCategory {
        private AlertDialog.Builder builder;
        private Dialog d;
        private int id;
        View dialogView;
        @SuppressLint("InflateParams")
        public WorkWithCategory(int id){
            LayoutInflater factory = LayoutInflater.from(getContext());
            builder = new AlertDialog.Builder(getContext());
            this.id = id;
            switch (id){
                case (1):{
                    dialogView = factory.inflate(R.layout.relaxation, null);
                    builder.setView(dialogView);
                    break;
                }
                case (2):{
                    dialogView = factory.inflate(R.layout.health, null);
                    builder.setView(dialogView);
                    break;
                }
                case(3):{
                    dialogView = factory.inflate(R.layout.to_eat, null);
                    builder.setView(dialogView);
                    break;
                }
                case(4):{
                    dialogView = factory.inflate(R.layout.shopping, null);
                    builder.setView(dialogView);
                    break;
                }
                case(5):{
                    dialogView = factory.inflate(R.layout.money, null);
                    builder.setView(dialogView);
                    break;
                }
                case(6):{
                    dialogView = factory.inflate(R.layout.other, null);
                    builder.setView(dialogView);
                    break;
                }
            }

            Button btn = dialogView.findViewById(R.id.saveSettingsBtn);
            btn.setOnClickListener(b -> {
                ArrayList<View> touchableViews = dialogView.getTouchables();
                int j = 0;
                for (View v:touchableViews){
                    if (v instanceof Switch){
                        if (((Switch) v).isChecked()){
                            j++;
                            isCategoryRepeat = false;
                            for(int i = 0; i < activeSwitch.size(); i++){
                                if(activeSwitch.get(i).getText().toString().equals(((Switch) v).getText().toString()))
                                    isCategoryRepeat = true;
                            }
                            if(activeSwitch.size() == 0 || !isCategoryRepeat) activeSwitch.add((Switch) v);
                            Toast.makeText(getContext(),activeSwitch.get(activeSwitch.size()-1).getText().toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }
                closeDialog();
            });
        }

        public Dialog createDialog(){
            d =  builder.create();
            return d;
        }
        public void closeDialog(){
            d.cancel();
        }
    }
}