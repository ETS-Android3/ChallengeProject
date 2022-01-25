package fr.eurecom.bottomnavigationdemo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.util.Log;

import androidx.annotation.NonNull;

import com.firebase.geofire.GeoFire;
import com.firebase.geofire.GeoLocation;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;


public final class User {

    private static User INSTANCE;

    public static void logOut() {
        INSTANCE = null;
    }

    // Currently fetched from Firebase Auth
    private String UID;
    private String name;
    private String email;

    // To be implemented now
    private int age;
    private String gender;
    private String phone;

    private boolean visible;

    private Location location;
    private LocationManager locationManager;

    DatabaseReference ref;
    DatabaseReference georef;
    GeoFire geoFire;

    final FirebaseAuth auth = FirebaseAuth.getInstance();
    final FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();

    private User() {

        this.UID = auth.getUid();
        this.name = auth.getCurrentUser().getDisplayName();
        this.email = auth.getCurrentUser().getEmail();
        this.location = fetchLocation();
        this.visible = false;

        ref = FirebaseDatabase.getInstance().getReference("Users").child(UID);
        ref.child("name").setValue(name);
        georef = FirebaseDatabase.getInstance().getReference("Locations");
        geoFire = new GeoFire(georef);

        ref.get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DataSnapshot> task) {
                if (!task.isSuccessful()) {
                    Log.e("firebase", "Error getting data", task.getException());
                } else {
                    Log.d("firebase", String.valueOf(task.getResult().getValue()));
                    updateUserData(task);
                }
            }
        });

        setLocation(location);
    }

    private User(String UID, String name, int age, String gender, String phone) {
        setName(name);
        setAge(age);
        setGender(gender);
        setPhone(phone);
        //setLocation);
        this.UID = auth.getUid();
    }

    public static User getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new User();
            return INSTANCE;

        } else {
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

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setName(String name) {
        this.name = name;
        // Updates the "DisplayName" in Firebase Auth
        firebaseUser.updateProfile(new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build()
        );
        ref.child("name").setValue(this.getName());
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
        ref.child("age").setValue(this.getAge());
    }

    public String getGender() {
        if (this.gender == null) {
            return "null";
        }
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
        ref.child("gender").setValue(this.getGender());
    }

    public String getPhone() {
        if (this.phone == null) {
            return "null";
        }
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
        ref.child("phone").setValue(this.getPhone());
    }

    public Location getLocation() {
        return this.location;
    }


    public void setLocation(Location location) {
        Log.i("User", "setLocation()");
        if(!isVisible()){
            georef.child(this.getUID()).removeValue();
        }
        else{
            geoFire.setLocation(this.getUID(), new GeoLocation(location.getLatitude(), location.getLongitude()));
        }


    }

    public boolean isVisible(){
        return this.visible;
    }

    public void setVisible(boolean bool){
        this.visible = bool;
    }

    public Location fetchLocation() {
        Location location = new Location("");
        location.setLatitude(12.0);
        location.setLongitude(12.0);
        return location;
    }



    private void updateUserData(Task<DataSnapshot> task) {
        if (task.getResult().child("age").getValue() != null ||
                task.getResult().child("phone").getValue() != null ||
                task.getResult().child("phone").getValue() != null) {
            this.setAge(((Long) task.getResult().child("age").getValue()).intValue());
            this.setGender((String) task.getResult().child("gender").getValue());
            this.setPhone((String) task.getResult().child("phone").getValue());
        }
    }

}
