package fr.eurecom.bottomnavigationdemo;

import android.location.Location;
import android.location.LocationManager;


public class User {

    private String UID;
    private String name;
    private String age;
    private String gender;
    private String phone;

    private Location location;
    private LocationManager locationManager;


    public User(String UID, String name, String age, String gender, String phone) {
        setUID(UID);
        setName(name);
        setAge(age);
        setGender(gender);
        setPhone(phone);
        //setLocation);


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
