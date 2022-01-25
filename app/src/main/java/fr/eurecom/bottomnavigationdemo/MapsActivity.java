package fr.eurecom.bottomnavigationdemo;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.firebase.geofire.GeoQuery;
import com.firebase.geofire.GeoQueryEventListener;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;


import java.util.HashMap;

import fr.eurecom.bottomnavigationdemo.databinding.ActivityMapsBinding;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    GeoFire geoFire;
    GeoQuery geoQuery;

    private Button visibilityButton;
    private boolean firstTime = true;

    //GeoQuery geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), 1.0);

    protected LocationManager locationManager = null;
    private String provider;
    Location location;
    public static final int MY_PERMISSIONS_LOCATION = 0;

    HashMap<String, GeoLocation> usersArray = new HashMap<>();

    private boolean firstTime = true;

    private final LocationListener mLocationListener = new LocationListener() {
        @Override
        public void onLocationChanged(final Location locationChanged) {

            if(!(locationChanged.getLatitude() == location.getLatitude() && locationChanged.getLongitude() == location.getLongitude())) {
                location = getLocation();
                geoFire.setLocation(user.getUID(), new GeoLocation(location.getLatitude(), location.getLongitude()));
                user.setLocation(location);
                geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), 1.0);
                usersArray.clear();
                map.clear();
                createGeoQuery();
                updateLocationUI();
            }

            if(firstTime) {

                location = getLocation();
                geoFire.setLocation(user.getUID(), new GeoLocation(location.getLatitude(), location.getLongitude()));
                user.setLocation(location);
                geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), 1.0);
                usersArray.clear();
                map.clear();
                createGeoQuery();
                updateLocationUI();
                firstTime = false;
            }

        }
    };

    private ActivityMapsBinding binding;
    private GoogleMap map;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient fusedLocationProviderClient;

    private final LatLng defaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 15;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean locationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location lastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    private final User user = User.getInstance();




    private Location getLocation() {
        Criteria criteria = new Criteria();
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(criteria, false);
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.i("Permission: ", "To be checked");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, MY_PERMISSIONS_LOCATION);

        } else
            Log.i("Permission: ", "GRANTED");
        location = locationManager.getLastKnownLocation(provider);
        if (locationManager == null)
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        provider = locationManager.getBestProvider(criteria, false);
        location = locationManager.getLastKnownLocation(provider);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this.mLocationListener);

        return location;
    }

    //@Override
    public void onLocationChanged(Location location) {
        Log.i("Location","LOCATION CHANGED!!!"); //updateLocationView();
    }
    //@Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }
    // @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider " + provider, Toast.LENGTH_SHORT).show();
    }
    //@Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(this, "Disabled provider " + provider, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //checking if user credentials are set
        //if they are not set, start activity to set credentials
        //TODO


        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a PlacesClient
        Places.initialize(getApplicationContext(), Integer.toString(R.string.google_maps_key));

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.map);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.shop:
                        startActivity(new Intent(getApplicationContext(), MessagesActivity.class));
                        overridePendingTransition(0, 0);
                        return true;
                    case R.id.map:
                        return true;
                }
                return false;
            }
        });

        location = getLocation();

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Locations");
        geoFire = new GeoFire(ref);

        //geoFire.setLocation("UserDemoStj", new GeoLocation(63.4684, 10.9172));
        //geoFire.setLocation("UserDemoFarAwayTrd", new GeoLocation(63.4250, 10.4428));
        visibilityButton = findViewById(R.id.visibilityButton);
        visibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                toggleVisibility();
            }
        });
        createGeoQuery();

        Log.i("onCreate", "at end");

    }

    private void createGeoQuery() {

        geoQuery = geoFire.queryAtLocation(new GeoLocation(location.getLatitude(), location.getLongitude()), 1.0);

        final GeoQueryEventListener geoQueryEventListener = new GeoQueryEventListener() {

            @Override
            public void onKeyEntered(String key, GeoLocation geoLocation) {
                //System.out.println(String.format("Key %s entered the search area at [%f,%f]", key, geoLocation.latitude, geoLocation.longitude));
                usersArray.put(key, geoLocation);
                for(String useKey : usersArray.keySet()) {
                    Log.i("UserKey: ", useKey+ " location: " +usersArray.get(useKey).toString());
                }

                updateLocationUI();
            }

            @Override
            public void onKeyExited(String key) {
                //System.out.println(String.format("Key %s is no longer in the search area", key));
                usersArray.remove(key);
                for(String useKey : usersArray.keySet()) {
                    Log.i("UserKey: ", useKey+ " location: " +usersArray.get(useKey).toString());
                }
                updateLocationUI();
            }

            @Override
            public void onKeyMoved(String key, GeoLocation geoLocation) {
                //System.out.println(String.format("Key %s moved within the search area to [%f,%f]", key, geoLocation.latitude, geoLocation.longitude));
                usersArray.remove(key);
                usersArray.put(key, geoLocation);

                for(String useKey : usersArray.keySet()) {
                    Log.i("Moved! in array: ", useKey+ "location: " +usersArray.get(useKey).toString());
                }
                updateLocationUI();
            }

            @Override
            public void onGeoQueryReady() {
                Log.i("onGeoQueryReady", "ready");
            }

            @Override
            public void onGeoQueryError(DatabaseError error) {
                Log.i("onGeoQueryError", error.toString());

            }

        };

        geoQuery.addGeoQueryEventListener(geoQueryEventListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        //locationManager.removeUpdates(this.mLocationListener);
    }


    /**
     * Saves the state of the map when the activity is paused.
     */

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if (map != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, map.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
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
    public void onMapReady(GoogleMap map) {
        this.map = map;

        // Use a custom info window adapter to handle multiple lines of text in the
        // info window contents.
        this.map.setInfoWindowAdapter(new GoogleMap.InfoWindowAdapter() {

            @Override
            // Return null here, so that getInfoContents() is called next.
            public View getInfoWindow(Marker arg0) {
                return null;
            }

            @Override
            public View getInfoContents(Marker marker) {


                return null;
            }
        });



        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        //createGeoQuery(geoFire, location);
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();

                            if (lastKnownLocation != null) {
                                Log.i("TAG", "lastKnownLocation not null: "+lastKnownLocation);
                                map.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                            }
                        } else {
                            Log.d("TAG", "Current location is null. Using defaults.");
                            Log.e("TAG", "Exception: %s", task.getException());
                            map.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                            map.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
            //System.out.println("lastknownLoCATION: "+lastKnownLocation);
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }

        //System.out.println("lastKnownLocation: "+lastKnownLocation);
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        locationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    locationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    private void updateLocationUI() {
        if (map == null) {
            return;
        }
        map.clear();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReferenceFromUrl("gs://challengeproject-334921.appspot.com/Avatars/Avatar1.png");

        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if(task.isSuccessful()) {


                    try {
                        for (String keys : usersArray.keySet()) {
                            if (user.getUID() != keys) {
                                //testing new marker:
                                final long ONE_MEGABYTE = 1024 * 1024;
                                imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
                                    @Override
                                    public void onSuccess(byte[] bytes) {
                                        Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                                        //imageView.setImageBitmap(bmp);

                                        BitmapDescriptor bd = BitmapDescriptorFactory.fromBitmap(bmp);
                                        DataSnapshot snapshot = task.getResult();
                                        String snippet = (String) snapshot.child("status").getValue();

                                        GeoLocation usLoc = usersArray.get(keys);
                                        double lng = usLoc.longitude;
                                        double lat = usLoc.latitude;
                                        LatLng latLng = new LatLng(lat, lng);
                                        map.addMarker(new MarkerOptions()
                                                .position(latLng)
                                                .title((String) snapshot.child("name").getValue())
                                                .icon(bd)
                                                .snippet(snippet));
                                        //map.addMarker(new MarkerOptions().position(latLng).title(keys).snippet("HEEEI"));
                                        // map.addMarker(marker);


                                        Log.i("Image", "success");

                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception exception) {
                                        Log.i("Image", "Error setting image");
                                        GeoLocation usLoc = usersArray.get(keys);
                                        double lng = usLoc.longitude;
                                        double lat = usLoc.latitude;
                                        LatLng latLng = new LatLng(lat, lng);
                                        map.addMarker(new MarkerOptions().position(latLng).title(keys));
                                    }
                                });

                            }
                        }
                    }
                    catch (Exception e) {
                        Log.e("Marker: ", "exception setting marker: " + e.toString());
                    }
                }

            }
        });

        if (locationPermissionGranted) {
            map.setMyLocationEnabled(true);
            map.getUiSettings().setMyLocationButtonEnabled(true);


        } else {
            map.setMyLocationEnabled(false);
            map.getUiSettings().setMyLocationButtonEnabled(false);
            lastKnownLocation = null;
            getLocationPermission();
        }
    }

    private void toggleVisibility(){
        if (user.isVisible()){
            user.setVisible(false);
            user.setLocation(location);
            Toast.makeText(getApplicationContext(),"You are no longer visible to other users",Toast.LENGTH_SHORT).show();
            visibilityButton.setText("Invisible");
        }
        else{
            user.setVisible(true);
            user.setLocation(location);
            Toast.makeText(getApplicationContext(),"You are now visible to other users",Toast.LENGTH_SHORT).show();
            visibilityButton.setText("Visible");
        }

    }


}


