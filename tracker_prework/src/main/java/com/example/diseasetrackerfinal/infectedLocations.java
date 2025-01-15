package com.example.diseasetrackerfinal;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.osmdroid.config.Configuration;
import org.osmdroid.events.MapEventsReceiver;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.MapEventsOverlay;
import org.osmdroid.views.overlay.Marker;
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider;
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

public class infectedLocations extends AppCompatActivity {



    private Marker lastMarker = null;
    private MapView map = null;
    private MyLocationNewOverlay mLocationOverlay;
    private Geocoder geocoder;
    private String locationName = null;
    private double locationLat = 0.0;
    private double locationLon = 0.0;

    private int userId = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Toast.makeText(getApplicationContext(),"Please select current location to go to your location",Toast.LENGTH_LONG).show();

        Configuration.getInstance().load(getApplicationContext(),
                PreferenceManager.getDefaultSharedPreferences(getApplicationContext()));
        setContentView(R.layout.activity_infected_locations);
        geocoder = new Geocoder(this, Locale.getDefault());

        Bundle extras = getIntent().getExtras();
        userId = Integer.parseInt(extras.getString("id"));


        initMapView();


        EditText searchLocation = findViewById(R.id.search_location);
        Button searchButton = findViewById(R.id.search_button);
        searchButton.setOnClickListener(view -> searchForLocation(searchLocation.getText().toString()));


        LinearLayout l = findViewById(R.id.popupLayout);
        Button close = findViewById(R.id.backwards);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                l.setVisibility(View.GONE);
            }
        });
        GpsMyLocationProvider provider = new GpsMyLocationProvider(this);
        this.mLocationOverlay = new MyLocationNewOverlay(provider, map);
        this.mLocationOverlay.enableMyLocation();
        map.getOverlays().add(this.mLocationOverlay);
        if (provider.getLastKnownLocation() != null) {
            GeoPoint currentLocation = new GeoPoint(provider.getLastKnownLocation().getLatitude(), provider.getLastKnownLocation().getLongitude());
            map.getController().setCenter(currentLocation);
        }


        Button currentLocationButton = findViewById(R.id.btn_current_location);
        currentLocationButton.setOnClickListener(v -> {
            if (provider.getLastKnownLocation() != null) {
                GeoPoint currentLocation = new GeoPoint(provider.getLastKnownLocation().getLatitude(), provider.getLastKnownLocation().getLongitude());
                moveToLocation(currentLocation);

            } else {
                Toast.makeText(this, "Current location not available", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initMapView() {

        map = findViewById(R.id.map_view);
        map.setTileSource(TileSourceFactory.MAPNIK);
        map.setBuiltInZoomControls(false);
        map.setMultiTouchControls(true);
        this.mLocationOverlay = new MyLocationNewOverlay(new GpsMyLocationProvider(this), map);
        this.mLocationOverlay.enableMyLocation();
        //this.mLocationOverlay.enableFollowLocation();
        map.getOverlays().add(this.mLocationOverlay);
        map.getController().setZoom(18);

        MapEventsReceiver mReceive = new MapEventsReceiver() {
            @Override
            public boolean singleTapConfirmedHelper(GeoPoint p) {
                return onMapSingleTap(p);
            }

            @Override
            public boolean longPressHelper(GeoPoint p) {
                return false;
            }
        };
        map.getOverlays().add(new MapEventsOverlay(mReceive));
    }

    private boolean onMapSingleTap(GeoPoint p) {
        if (lastMarker != null) {
            map.getOverlays().remove(lastMarker);
        }

        Marker startMarker = new Marker(map);
        startMarker.setPosition(p);
        startMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);

        lastMarker = startMarker;

        map.getOverlays().add(lastMarker);
        map.invalidate();

        List<Address> addresses;
        try {
            addresses = geocoder.getFromLocation(p.getLatitude(), p.getLongitude(), 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);

                String thoroughfare = address.getThoroughfare();
                String locality = address.getLocality();
                String country = address.getCountryName();
                locationName = thoroughfare + ", " + locality + ", " + country;

                locationLat = p.getLatitude();
                locationLon = p.getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(this, "Failed to get location name", Toast.LENGTH_SHORT).show();
        }

        Toast.makeText(getApplicationContext(), "Name: " + locationName + "Lat: " + p.getLatitude() + " | Long: " + p.getLongitude(), Toast.LENGTH_LONG).show();
        return true;
    }



    private void searchForLocation(String location) {
        try {
            List<Address> addresses = geocoder.getFromLocationName(location, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                locationName = address.getFeatureName();
                locationLat = address.getLatitude();
                locationLon = address.getLongitude();

                GeoPoint geoPoint = new GeoPoint(locationLat, locationLon);
                moveToLocation(geoPoint);
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void moveToLocation(GeoPoint geoPoint) {
        map.getController().animateTo(geoPoint);

        if (lastMarker != null) {
            map.getOverlays().remove(lastMarker);
        }

        Marker marker = new Marker(map);
        marker.setPosition(geoPoint);
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);


        lastMarker = marker;

        map.getOverlays().add(lastMarker);
    }

    public void displayInfected(View view){
        NetworkCalls networkCalls = new NetworkCalls();
        LinearLayout l = findViewById(R.id.popupLayout);

        if(locationLat == 0.0 || locationLon == 0.0){
            Toast.makeText(getApplicationContext(), "Please choose a location",Toast.LENGTH_LONG).show();
        }else{
            networkCalls.infectedLocations(locationLat, locationLon, new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {

                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) throws IOException {
                    if (response.isSuccessful()){
                        String responseBody = response.body().string();
                        try {
                            JSONArray jsonArray = new JSONArray(responseBody);
                            TableLayout tableLayout = findViewById(R.id.tableLayout);

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                String location_name = jsonObject.getString("location_name");
                                String count = jsonObject.getString("count");

                                TextView locationTextView = new TextView(infectedLocations.this);
                                locationTextView.setText(location_name);
                                locationTextView.setTextColor(Color.RED);
                                locationTextView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

                                TextView countTextView = new TextView(infectedLocations.this);
                                countTextView.setTextColor(Color.RED);
                                countTextView.setText(count);
                                countTextView.setLayoutParams(new TableRow.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1));

                                TableRow tableRow = new TableRow(infectedLocations.this);
                                tableRow.addView(locationTextView);
                                tableRow.addView(countTextView);

                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        l.setVisibility(View.VISIBLE);
                                        tableLayout.addView(tableRow);
                                    }
                                });
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }else{
                        String responseBody = response.message();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Toast.makeText(getApplicationContext(),responseBody,Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }
            });

        }



    }




    @Override
    public void onResume() {
        super.onResume();
        map.onResume();

        GpsMyLocationProvider provider = new GpsMyLocationProvider(this);
        if (provider.getLastKnownLocation() != null) {
            GeoPoint currentLocation = new GeoPoint(provider.getLastKnownLocation().getLatitude(), provider.getLastKnownLocation().getLongitude());
            map.getController().setCenter(currentLocation);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        map.onPause();
    }

    public void onBackPressed(){
        finish();
    }
}
