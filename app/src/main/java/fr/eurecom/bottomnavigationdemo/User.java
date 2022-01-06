package fr.eurecom.bottomnavigationdemo;

import android.location.Location;
import android.location.LocationManager;

import com.google.firebase.auth.FirebaseAuth;


public final class User {

    private static User INSTANCE;

    private String UID;
    private String name;
    private String age;
    private String gender;
    private String phone;

    private Location location;
    private LocationManager locationManager;

    final FirebaseAuth auth = FirebaseAuth.getInstance();

    private User (){
        this.UID = auth.getUid();
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
        this.location = location;
    }





}
