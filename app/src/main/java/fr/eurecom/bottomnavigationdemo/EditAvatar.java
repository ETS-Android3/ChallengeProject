package fr.eurecom.bottomnavigationdemo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class EditAvatar extends AppCompatActivity {
    private User user = User.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_avatar);
    }


    public void chosedGuyFunction (View v) {
        //if (validateInput()) {

        // Input is valid, here send data to your server
        Drawable avatar = ResourcesCompat.getDrawable(getApplicationContext().getResources(), R.drawable.normalboy, null);


        user.setAvatar(avatar);

        Toast.makeText(this,"Avatar Updated Successfully",Toast.LENGTH_SHORT).show();
        // Here you can call you API

        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

        //   }
    }

    public void chosedGirlFunction (View v) {
        //if (validateInput()) {

        // Input is valid, here send data to your server
        Drawable avatar = ResourcesCompat.getDrawable(getApplicationContext().getResources(), R.drawable.blackoutfitgirl, null);

        user.setAvatar(avatar);

        Toast.makeText(this,"Avatar Updated Successfully",Toast.LENGTH_SHORT).show();
        // Here you can call you API

        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

        //   }
    }

    public void returnFromAvatar (View v) {
        //if (validateInput())

        Toast.makeText(this,"Avatar not updated",Toast.LENGTH_SHORT).show();
        // Here you can call you API

        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

        //   }
    }
}