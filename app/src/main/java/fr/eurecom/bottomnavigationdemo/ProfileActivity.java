package fr.eurecom.bottomnavigationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ProfileActivity extends AppCompatActivity {

    private final User user = User.getInstance();
    private String displayName;
    private TextView displayNameTextView;

    private String displayEmail;
    private TextView displayEmailTextView;

    private int displayAge;
    private TextView displayAgeTextView;

    private String displayPhone;
    private TextView displayPhoneTextView;

    private String displayGender;
    private TextView displayGenderTextView;

    private Button editProfileButton;

    final FirebaseDatabase database = FirebaseDatabase.getInstance();
    final DatabaseReference myRef = database.getReference("Users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // button for logout and initialing our button.
        Button logoutBtn = findViewById(R.id.idBtnLogout);

        // adding onclick listener for our logout button.
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // below line is for getting instance
                // for AuthUi and after that calling a
                // sign out method from FIrebase.
                AuthUI.getInstance()
                        .signOut(ProfileActivity.this)

                        // after sign out is executed we are redirecting
                        // our user to MainActivity where our login flow is being displayed.
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {

                                // below method is used after logout from device.
                                Toast.makeText(ProfileActivity.this, "User Signed Out", Toast.LENGTH_SHORT).show();

                                // below line is to go to MainActivity via an intent.
                                Intent i = new Intent(ProfileActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                        });
            }
        });


        getSupportActionBar().hide();

        // Sets the name to the displayname from Firebase Auth
        displayName = user.getName();
        displayNameTextView = findViewById(R.id.displayNameTextView);
        displayNameTextView.setText(displayName);

        // Sets the email to the email from Firebase Auth
        displayEmail = user.getEmail();
        displayEmailTextView = findViewById(R.id.emailTextView);
        displayEmailTextView.setText(displayEmail);

        // Sets the age to the display
        displayAge = user.getAge();
        displayAgeTextView = findViewById(R.id.ageTextView);
        displayAgeTextView.setText(Integer.toString(displayAge));

        // Sets the age to the display
        displayPhone = user.getPhone();
        displayPhoneTextView = findViewById(R.id.phoneTextView);
        displayPhoneTextView.setText(displayPhone);

        // Sets the age to the display
        displayGender = user.getGender();
        displayGenderTextView = findViewById(R.id.genderTextView);
        displayGenderTextView.setText(displayGender);


        editProfileButton = findViewById(R.id.editProfileButton);
        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editProfile();
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.profile);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.profile:
                        return true;

                    case R.id.shop:
                        startActivity(new Intent(getApplicationContext(), ShopActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.map:
                        startActivity(new Intent(getApplicationContext(), MapsActivity.class));
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    public void editProfile(){
        startActivity(new Intent(getApplicationContext(), EditProfileActivity.class));
    }
}