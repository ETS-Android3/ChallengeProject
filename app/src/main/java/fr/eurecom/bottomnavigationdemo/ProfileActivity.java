package fr.eurecom.bottomnavigationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

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

    private String displayStatus;
    private TextView displayStatusTextView;

    private Drawable displayAvatar;
    private ImageView displayAvatarImageView;

    private Button editProfileButton;
    private Button editAvatarButton;

    private ImageView imageView;
    private Button setProfilePicBtn;


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

        // Sets the status to the display
        displayStatus = user.getStatus();
        displayStatusTextView = findViewById(R.id.statusTextView);
        displayStatusTextView.setText(displayStatus);

        // Sets the avatar to the display
        displayAvatar = user.getAvatar();
        displayAvatarImageView = findViewById(R.id.avatarView);
        displayAvatarImageView.setImageDrawable(displayAvatar);

        imageView = findViewById(R.id.ProfilePicImageView);

        setProfilePicBtn = findViewById(R.id.idBtnAddProfilePic);
        setProfilePicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });

        setImage();

        editAvatarButton = findViewById(R.id.editAvatarButton);
        editAvatarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), EditAvatar.class));
            }
        });

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

    //testing code to get Image:

    private Uri filePath;
    FirebaseStorage storage = FirebaseStorage.getInstance();
    StorageReference ref  = storage.getReference("/ProfilePictures");

    StorageReference imageRef = storage.getReferenceFromUrl("gs://challengeproject-334921.appspot.com/ProfilePictures/"+user.getUID());

    final long ONE_MEGABYTE = 1024 * 1024;

     private void setImage() {

         imageRef.getBytes(ONE_MEGABYTE).addOnSuccessListener(new OnSuccessListener<byte[]>() {
             @Override
             public void onSuccess(byte[] bytes) {
                 Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
                 imageView.setImageBitmap(bmp);
                 Log.i("Image", "success");

             }
         }).addOnFailureListener(new OnFailureListener() {
             @Override
             public void onFailure(@NonNull Exception exception) {
                 Toast.makeText(ProfileActivity.this,"Profile picture could not be loaded/set correctly try uploading a new one", Toast.LENGTH_SHORT).show();

                 Log.i("Image", "Error setting image");
             }
         });

     }

    private final int PICK_IMAGE_REQUEST = 22;
    private void selectImage() {

        // Defining Implicit Intent to mobile gallery
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select profile picture from: "), PICK_IMAGE_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // checking request code and result code
        // setting the filepath of requested profile picture, and initializing upload
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {

            filePath = data.getData();
            Log.i("OnActivityResult: ", "filePath = "+filePath);

            uploadImage();

        }
    }



    private void uploadImage() {
        if (filePath != null) {

            // Code for showing progressDialog while uploading
            ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading");
            progressDialog.show();

            // Defining the child of storageReference
            StorageReference StorRef = ref.child(user.getUID());//"images/" + UUID.randomUUID().toString());

            // adding listeners on upload
            // or failure of image
            StorRef.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    // Image uploaded successfully
                                    // Dismiss dialog
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this,"Profile picture uploaded!", Toast.LENGTH_SHORT).show();
                            setImage();

                           try {
                               Bitmap bitmap = MediaStore
                                       .Images
                                       .Media
                                       .getBitmap(getContentResolver(), filePath);
                               imageView.setImageBitmap(bitmap);
                           } catch (Exception e) {
                               Log.e("upLoadImage: ", "error setting image: "+e.toString());
                           }
                        }
                    })

                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                            // Error, Image not uploaded
                            progressDialog.dismiss();
                            Toast.makeText(ProfileActivity.this,"Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        // Showing progress of upload:
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int)progress + "%");
                        }
                    });
        }
    }
}