package com.example.zodiac.SearchingData;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import com.example.zodiac.NotificationWindows.GeoDataActivity;
import com.google.android.gms.location.LocationResult;

public class MyLocationServices extends BroadcastReceiver {

    public static final String ACTION_PROCESS_UPDATE = "com.example.zodiac.Location.UPDATE_LOCATION";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_PROCESS_UPDATE.equals(action)) {
                LocationResult result = LocationResult.extractResult(intent);
                if (result != null) {
                    Location location = result.getLastLocation();
                    try {
                        GeoDataActivity.getInstance().sentLocation(location.getLatitude(), location.getLongitude());
                    } catch (Exception e) {
                        e.getMessage();
                    }
                }
            }
        }
    }
}
