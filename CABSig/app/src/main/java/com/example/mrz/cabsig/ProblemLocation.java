package com.example.mrz.cabsig;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.widget.Toast;

import com.google.android.gms.location.LocationResult;

public class ProblemLocation extends BroadcastReceiver {

    public static final String ACTION_PROCESS_UPDATE = "com.example.railwaycabstation.UPDATE_LOCATION";

    @Override
    public void onReceive(Context context, Intent intent) {

        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATE.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);

                if (result != null) {
                    Location location = result.getLastLocation();

                   /* String location_string = new StringBuilder(""+location.getLatitude())
                            .append("/")
                            .append(location.getLongitude())
                            .toString();*/
                    try {
                        ProblemActivity.getInstance().updateTextView(location.getLatitude(), location.getLongitude());

                    } catch (Exception ex) {
                        Toast.makeText(context, "Something Is Wrong", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        }

    }
}
