package fr.eurecom.bottomnavigationdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class EditProfileActivity extends AppCompatActivity {
    private User user = User.getInstance();
    private EditText etPhone, etGender, etAge;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        viewInitializations();
    }

    void viewInitializations() {
        etPhone = findViewById(R.id.et_phone_number);
        etGender = findViewById(R.id.et_gender);
        etAge = findViewById(R.id.et_age);

        // To show back button in actionbar
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    // Checking if the input in form is valid
    /*
    boolean validateInput() {

        if (etPhone.getText().toString().equals("")) {
            etPhone.setError("Please Enter Phone number No");
            return false;
        }
        if (etGender.getText().toString().equals("")) {
            etGender.setError("Please Enter Designation ");
            return false;
        }

        return true;
    }

     */

    boolean isEmailValid(String email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    // Hook Click Event

    public void performEditProfile (View v) {
        //if (validateInput()) {

            // Input is valid, here send data to your server
            String phoneNumber = etPhone.getText().toString();
            String gender = etGender.getText().toString();
            String age = etAge.getText().toString();

            user.setPhone(phoneNumber);
            user.setAge(Integer.parseInt(age));
            user.setGender(gender);

            Toast.makeText(this,"Profile Update Successfully",Toast.LENGTH_SHORT).show();
            // Here you can call you API

            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

     //   }
    }

}