package fr.eurecom.bottomnavigationdemo;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.ui.auth.AuthUI;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class ShopActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        final DatabaseReference myRef = database.getReference("ShopItems");

        final ArrayList<Item> itemsArray = new ArrayList<>();
        final ShopAdapter adapter = new ShopAdapter(this, itemsArray);

        final ListView listView = findViewById(R.id.listView);
        listView.setAdapter(adapter);

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                itemsArray.clear();
                adapter.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Item read_item = dataSnapshot.getValue(Item.class);
                    read_item.setID(dataSnapshot.getKey());
                    itemsArray.add(read_item);
                    Collections.sort(itemsArray, Item.ItemNameComparator);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        // button for logout and initialing our button.
        Button logoutBtn = findViewById(R.id.idBtnLogout);

        // adding onclick listener for our logout button.
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                User.logOut();
                // below line is for getting instance
                // for AuthUi and after that calling a
                // sign out method from FIrebase.
                AuthUI.getInstance()
                        .signOut(ShopActivity.this)

                        // after sign out is executed we are redirecting
                        // our user to MainActivity where our login flow is being displayed.
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            public void onComplete(@NonNull Task<Void> task) {

                                // below method is used after logout from device.
                                Toast.makeText(ShopActivity.this, "User Signed Out", Toast.LENGTH_SHORT).show();

                                // below line is to go to MainActivity via an intent.
                                Intent i = new Intent(ShopActivity.this, LoginActivity.class);
                                startActivity(i);
                            }
                        });
            }
        });



        // Removes title bar
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        getSupportActionBar().hide();

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.shop);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()){
                    case R.id.profile:
                        startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.shop:
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
}