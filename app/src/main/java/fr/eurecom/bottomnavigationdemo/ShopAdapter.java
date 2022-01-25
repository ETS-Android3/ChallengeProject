package fr.eurecom.bottomnavigationdemo;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ShopAdapter extends ArrayAdapter {

    public ShopAdapter(@NonNull Context context, ArrayList<Item> items) {
        super(context, 0, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        Item item = (Item) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_shop, parent, false);
        }
        TextView txtname = convertView.findViewById(R.id.setTextName);
        TextView txtprice = convertView.findViewById(R.id.setTextPrice);

        txtname.setText(item.getName());
        txtprice.setText(String.valueOf(item.getPrice()));



        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference imageRef = storage.getReferenceFromUrl("gs://challengeproject-334921.appspot.com/ShopItems/"+item.getID()+".jpeg");

        ImageView imageView = convertView.findViewById(R.id.itemPicture);
        //StorageReference photoReference= storageReference.child("pictures/photo1.jpg");

        final long ONE_MEGABYTE = 1024 * 1024;
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
                Log.i("Image", "Error setting image");
            }
        });


        return convertView;
    }
}
