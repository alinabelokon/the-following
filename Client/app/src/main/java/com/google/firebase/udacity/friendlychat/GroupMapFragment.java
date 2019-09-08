package com.google.firebase.udacity.friendlychat;

import android.app.Dialog;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by Alina on 6/3/2017.
 */

public class GroupMapFragment extends Fragment {

    //region fields

    private static final int MAP_PERM_REQUEST = 1111;
    GoogleMap mGoogleMap;
    GoogleApiClient mGoogleApiClient;

    private HashMap<String, Marker> mMarkers = new HashMap<>();
    private HashMap<String, OurLocation> mLocations = new HashMap<>();
    private Object mLock = new Object();
    private Marker mMiddlePointMarker;


    private DatabaseReference mMessagesDatabaseReference;
    private FirebaseDatabase mFirebaseDatabase;
    private String GroupID;
    private Button setMidPointBtn;
    private Button delMidPointBtn;
    private final Gson gson = new Gson();


    //endregion

    //region api

    private void setUserMarker(String userName, String title, String snippet, double lat, double lng) {
        if (mGoogleMap == null)
            return;

        MarkerOptions options = getMarkerOptions(title, snippet, lat, lng, BitmapDescriptorFactory.HUE_RED);

        synchronized (mLock) {

            Marker existingMarker = mMarkers.remove(userName);
            mLocations.remove(userName);

            Marker newMarker = mGoogleMap.addMarker(options);
            mMarkers.put(userName, newMarker);
            OurLocation location = new OurLocation();
            location.setLat(lat);
            location.setLon(lng);

            mLocations.put(userName,location);

            if (existingMarker != null)
                existingMarker.remove();
        }
    }

    private void removeUserMarker(String userName) {
        if (mGoogleMap == null)
            return;

        synchronized (mLock) {
            Marker existingMarker = mMarkers.remove(userName);
            mLocations.remove(userName);

            if (existingMarker != null)
                existingMarker.remove();
        }
    }

    private void setMiddlePointMarker(String title, String snippet, double lat, double lng) {
        if (mGoogleMap == null)
            return;

        MarkerOptions options = getMarkerOptions(title, snippet, lat, lng, BitmapDescriptorFactory.HUE_BLUE);

        synchronized (mLock) {
            Marker updatedMarker = mGoogleMap.addMarker(options);

            if (mMiddlePointMarker != null)
                mMiddlePointMarker.remove();

            mMiddlePointMarker = updatedMarker;
        }
    }

    private void clearMiddlePointMarker() {
        if (mGoogleMap == null)
            return;

        synchronized (mLock) {
            if (mMiddlePointMarker != null)
                mMiddlePointMarker.remove();

            mMiddlePointMarker = null;
        }
    }

    private void goToLocation(double lat, double lng) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLng(ll);
        mGoogleMap.moveCamera(update);
    }

    private void goToLocationZoom(double lat, double lng, float zoom) {
        LatLng ll = new LatLng(lat, lng);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(ll, zoom);
        mGoogleMap.moveCamera(update);

    }

    //endregion

    //region life cycle methods

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_group_map, container, false);






        return view;
    }

    private String getLocationJson() {

        Object[] locations = mLocations.values().toArray();
        return gson.toJson(locations);
    }

    @Override
    public void onStart() {
        super.onStart();

        if (!shouldInitializeFragment()) {
            return;
        }

        if (!tryObtainLocationPermissions()) {
            Toast.makeText(getActivity(), "Map features will not be available due to insufficient permissions", Toast.LENGTH_LONG).show();
            return;
        }

        connectToGoogleApiclient();
        initializeMap();
        initDB();
    }

    //endregion

    //region init

    private void initializeMap() {
        MapFragment map = (MapFragment) getActivity().getFragmentManager().findFragmentById(R.id.ali_map);
        map.getMapAsync(onMapReadyCallback);

    }

    private void connectToGoogleApiclient() {
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(connectionCallbacks)
                .addOnConnectionFailedListener(connectionFailedListener)
                .build();

        mGoogleApiClient.connect();
    }

    private boolean shouldInitializeFragment() {
        if (!googleServicesAvailble()) {
            Toast.makeText(getActivity(), "Device does not support Google Play Services. This means that map related features will not be available.", Toast.LENGTH_LONG).show();
            return false;
        }

        return true;
    }

    private boolean googleServicesAvailble() {
        GoogleApiAvailability api = GoogleApiAvailability.getInstance();
        int isAvailble = api.isGooglePlayServicesAvailable(getActivity());

        if (isAvailble == ConnectionResult.SUCCESS) {
            return true;
        } else if (api.isUserResolvableError(isAvailble)) {
            Dialog dialog = api.getErrorDialog(getActivity(), isAvailble, 0);
            dialog.show();
        }

        return false;
    }

    private boolean tryObtainLocationPermissions() {

        boolean hasFineLocation = tryRequestPermissionOnce(android.Manifest.permission.ACCESS_FINE_LOCATION);

        if (!hasFineLocation) {
            Toast.makeText(getActivity(), "Without GPS permissions, your group members will receive inaccurate info regarding your location", Toast.LENGTH_LONG).show();
        }

        boolean hasCoarseLocation = tryRequestPermissionOnce(android.Manifest.permission.ACCESS_COARSE_LOCATION);

        if (!hasCoarseLocation) {
            Toast.makeText(getActivity(), "Without Cellular location permissions, your group members might not recieve info regarding your location when there's no GPS reception", Toast.LENGTH_LONG).show();
        }

        if (!hasFineLocation && !hasCoarseLocation) {
            Toast.makeText(getActivity(), "Without GPS & Cellular location permissions, your group members might not recieve info regarding your location", Toast.LENGTH_LONG).show();
        }

        return hasFineLocation || hasCoarseLocation;
    }

    private boolean tryRequestPermissionOnce(String permission) {
        boolean granted = doesHavePermission(permission);

        if (granted)
            return true;

        if (!granted) {
            requestPermissions(new String[]{permission}, MAP_PERM_REQUEST);
        }

        return doesHavePermission(permission);
    }

    private boolean doesHavePermission(String permission) {
        return ActivityCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }


    private void initDB() {

        try {
            mFirebaseDatabase = FirebaseDatabase.getInstance();
           // MyAppApplication CurrUserHelper = ((MyAppApplication) getApplicationContext());
            GroupID = getActivity().getIntent().getStringExtra("GroupID");
            mMessagesDatabaseReference = mFirebaseDatabase.getReference();
            setMidPointBtn= (Button) getView().findViewById(R.id.MiddlePointBtn);
            delMidPointBtn= (Button) getView().findViewById(R.id.DelMidPoint);
            setMidPointBtn.setVisibility(View.INVISIBLE);
            delMidPointBtn.setVisibility(View.INVISIBLE);
            DatabaseReference groups = mMessagesDatabaseReference.child("Groups");
            DatabaseReference group = groups.child(GroupID);
            DatabaseReference users = group.child("users");
            users.addChildEventListener(mChildEventListener);
            group.child("MiddlePoint").addChildEventListener(midPointListener);


            setMidPointBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String locations= getLocationJson();
                    FirebaseMessaging fm = FirebaseMessaging.getInstance();
                    AtomicInteger msgId;
                    fm.send(new RemoteMessage.Builder("1066282643284" + "@gcm.googleapis.com")
                            .setMessageId("1235666")
                            .addData("locations", locations)
                            .addData("action", ".MIDPOINT")
                            .addData("message_type","Receipt")
                            .addData("GroupID", getActivity().getIntent().getStringExtra("GroupID"))
                            .build());


                    //mMessagesDatabaseReference.child("Groups").child(GroupID).child("MiddlePoint").child("point").child("lat").setValue(midpoint.getLat());
                    //mMessagesDatabaseReference.child("Groups").child(GroupID).child("MiddlePoint").child("point").child("lon").setValue(midpoint.getLon());


                }
            });

            delMidPointBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //OurLocation midpoint= getLocationJson();
                    mMessagesDatabaseReference.child("Groups").child(GroupID).child("MiddlePoint").child("point").child("lat").setValue(0);
                    mMessagesDatabaseReference.child("Groups").child(GroupID).child("MiddlePoint").child("point").child("lon").setValue(0);
                    clearMiddlePointMarker();

                }
            });
        }
        catch (Throwable t)
        {
            int i= 0+1;
        }

    }

    //endregion

    //region listeners


    private ChildEventListener midPointListener= new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            OurLocation midPointLocation;

            midPointLocation= dataSnapshot.getValue(OurLocation.class);
            if(midPointLocation.getLat()==0.0 || midPointLocation.getLon()==0.0 )
            {
                delMidPointBtn.setVisibility(View.INVISIBLE);
                    setMidPointBtn.setVisibility(View.VISIBLE);

                return;}
            else {
                setMiddlePointMarker("MidPoint", "Go here", midPointLocation.getLat(), midPointLocation.getLon());
                setMidPointBtn.setVisibility(View.INVISIBLE);
                    delMidPointBtn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
            OurLocation midPointLocation;
            midPointLocation= dataSnapshot.getValue(OurLocation.class);
            if(midPointLocation.getLat()==0.0 || midPointLocation.getLon()==0.0 ){
                clearMiddlePointMarker();
                delMidPointBtn.setVisibility(View.INVISIBLE);
                setMidPointBtn.setVisibility(View.VISIBLE);
            }
            else {
                setMiddlePointMarker("MidPoint", "Go here", midPointLocation.getLat(), midPointLocation.getLon());
                setMidPointBtn.setVisibility(View.INVISIBLE);
                delMidPointBtn.setVisibility(View.VISIBLE);
            }
        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }
    };
    private final ChildEventListener mChildEventListener = new ChildEventListener() {
        String getUserName(DataSnapshot dataSnapshot) {
            try
            {
                String userName = dataSnapshot.child("Mail").getValue().toString();
                return userName;
            }
            catch (Throwable t)
            {
                int i = 0+3;
                return null;
            }

        }

        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            try {
                String userName = getUserName(dataSnapshot);
                ChildEventListener listener = childEventListenerFactory(userName);
                DatabaseReference IDS = mMessagesDatabaseReference.child("IDS");
                DatabaseReference user = IDS.child(userName.replace(".","*dot*"));
                DatabaseReference location = user.child("Location");
                location.addChildEventListener(listener);
            }
            catch (Throwable t)
            {
                int i = 0+1;
            }

        }

        public void onChildChanged(DataSnapshot dataSnapshot, String s) {
        }

        public void onChildRemoved(DataSnapshot dataSnapshot) {
            String userName = getUserName(dataSnapshot);
            removeUserMarker(userName);
        }

        public void onChildMoved(DataSnapshot dataSnapshot, String s) {
        }

        public void onCancelled(DatabaseError databaseError) {
            Log.e("group map db listener", "loadPost:onCancelled", databaseError.toException());

        }
    };

    private final GoogleApiClient.OnConnectionFailedListener connectionFailedListener = new GoogleApiClient.OnConnectionFailedListener() {
        @Override
        public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
            Toast.makeText(getActivity(), "Could not connect to Google Play Services. This means that map related features will not be available.", Toast.LENGTH_LONG).show();
        }
    };


    private final GoogleApiClient.ConnectionCallbacks connectionCallbacks = new GoogleApiClient.ConnectionCallbacks() {

        @Override
        public void onConnected(@Nullable Bundle bundle) {

            LocationRequest r = new LocationRequest();
            r.setInterval(100);

            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, r, GroupMapFragment.this.locationListener);
            } catch (SecurityException sex) {
                //TODO note: this will never happen since permissions have been checked
            }

        }

        @Override
        public void onConnectionSuspended(int i) {

        }
    };

    private final LocationListener locationListener = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
//            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
//            CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 15);
//
//            if (mGoogleMap != null)
//                mGoogleMap.animateCamera(cameraUpdate);
        }
    };

    private final OnMapReadyCallback onMapReadyCallback = new OnMapReadyCallback() {

        @Override
        public void onMapReady(GoogleMap googleMap) {
            mGoogleMap = googleMap;
            try {
                mGoogleMap.setMyLocationEnabled(true);
                mGoogleMap.getUiSettings().setZoomControlsEnabled(true);
            } catch (SecurityException sex) {
                //todo note this will never happen since onStart will not allow it
            }
        }
    };

    //endregion

    //region misc

    private ChildEventListener childEventListenerFactory(final String username) {
        return new ChildEventListener() {



            private void update(DataSnapshot dataSnapshot) {

                try
                {
                    OurLocation location = dataSnapshot.getValue(OurLocation.class);
                    if(location == null)
                        return;

                    GroupMapFragment.this.setUserMarker(username, username, "bla", location.getLat(), location.getLon());
                    //mMarkers.put(username,location);
                }
                catch (Throwable t)
                {
                    int i = 0+1;
                    //TODO note: no location info - do nothing
                }
            }

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                update(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                update(dataSnapshot);
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                GroupMapFragment.this.removeUserMarker(username);
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };
    }

    @NonNull
    private MarkerOptions getMarkerOptions(String title, String snippet, double lat, double lng, float color) {
        return new MarkerOptions()
                .title(title)
                .icon(BitmapDescriptorFactory.defaultMarker(color))
                .position(new LatLng(lat, lng))
                .snippet(snippet);
    }

    //endregion

    private void initMapsAccess() {

//        if (mGoogleMap != null){
//            mGoogleMap.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {
//                @Override
//                public View getInfoWindow(Marker marker) {
//
//                    return null;
//                }
//
//                @Override
//                public View getInfoContents(Marker marker) {
//                    View v = getActivity().getLayoutInflater().inflate(R.layout.info_window,null);
//                    TextView tvLocality = (TextView) v.findViewById(R.id.tv_locality);
//                    TextView tvlat = (TextView) v.findViewById(R.id.tv_lat);
//                    TextView tvLng = (TextView) v.findViewById(R.id.tv_lng);
//                    TextView tvsnippet = (TextView) v.findViewById(R.id.tv_snippet);
//
//                    LatLng ll = marker.getPosition();
//                    tvLocality.setText(marker.getTitle());
//                    tvlat.setText("Latitude:" + ll.latitude);
//                    tvLng.setText("Longtitude:" + ll.longitude);
//                    tvsnippet.setText(marker.getTitle());
//
//                    return v;
//                }
//            });
//        }

    }
}
