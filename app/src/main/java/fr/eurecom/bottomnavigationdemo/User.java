package fr.eurecom.bottomnavigationdemo;

import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public final class User {

    private static User INSTANCE;

    private String UID;
    private String name;
    private String age;
    private String gender;
    private String phone;

    private Location location;
    private LocationManager locationManager;

    DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Users");
    GeoFire geoFire = new GeoFire(ref);


    final FirebaseAuth auth = FirebaseAuth.getInstance();

    private User (){
        this.UID = auth.getUid();
        this.location = fetchLocation();
        setLocation(location);
    }

    private User(String UID, String name, String age, String gender, String phone) {
        setName(name);
        setAge(age);
        setGender(gender);
        setPhone(phone);
        //setLocation);
        this.UID = auth.getUid();

    }

    public static User getInstance(){
        if (INSTANCE == null){
            return new User();
        }
        else {
            return INSTANCE;
        }
    }

    public String getUID() {
        return UID;
    }

    public void setUID(String UID) {
        this.UID = UID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Location getLocation() {
        return this.location;
    }

    public void setLocation(Location location) {
        Log.i("User", "setLocation()");
        geoFire.setLocation(UID, new GeoLocation(location.getLatitude(), location.getLongitude()));
        geoFire.setLocation(UID, new GeoLocation(location.getLatitude(), location.getLongitude()));

    }

    public Location fetchLocation(){
        Location location = new Location("");
        location.setLatitude(12.0);
        location.setLongitude(12.0);
        return location;
        //return null;
    }





}
