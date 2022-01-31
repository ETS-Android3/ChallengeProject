package fr.eurecom.bottomnavigationdemo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.ArrayList;

public class RequestAdapter extends ArrayAdapter {

    public RequestAdapter(@NonNull Context context, ArrayList<ConnectionRequest> items) {
        super(context, 0, items);
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        ConnectionRequest user = (ConnectionRequest) getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.row_request, parent, false);
        }
        TextView txtname = convertView.findViewById(R.id.requestingUser);

        txtname.setText(user.getMessageUser());

        return convertView;
    }

}
