package com.example.zodiac.NotificationWindows;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.VolleyError;
import com.example.zodiac.DB.DBHandler;
import com.example.zodiac.Helpers.ModelsFiller;
import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.SearchingData.MyLocationServices;
import com.example.zodiac.R;
import com.example.zodiac.Users.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.Button;

import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class GeoDataActivity extends AppCompatActivity {

    private Button locationActivateButton;
    private User user;

    private static GeoDataActivity instance;
    private LocationRequest locationRequest;
    private FusedLocationProviderClient fusedLocationProviderClient;

    public static GeoDataActivity getInstance() {
        return instance;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_data);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent givenIntent = getIntent();
        user = givenIntent.getParcelableExtra("user");

        locationActivateButton = findViewById(R.id.locationActivateButton);
        locationActivateButton.setOnClickListener(view -> {
            DBHandler.getUserByEmailAndPassword(this, user.getMail(), user.getPassword(), new DBHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONObject obj = new JSONObject(result);
                        user = ModelsFiller.fillUser(obj);

                        DBHandler.getPhotos(GeoDataActivity.this, user.getMail(), new DBHandler.VolleyCallback() {
                            @Override
                            public void onSuccess(String result) {
                                List<String> images = new ArrayList<>();
                                ModelsFiller.fillImagesList(images, result);
                                if (images.size() > 0)
                                    user.setMainPhoto(images.get(0));
                                else
                                    user.setMainPhoto("");

                                DBHandler.createSearchingSettings(GeoDataActivity.this, user.getId(), 5, 18, 23);
                                Dexter.withActivity(GeoDataActivity.this).withPermission(Manifest.permission.ACCESS_FINE_LOCATION).withListener(new PermissionListener() {
                                    @Override
                                    public void onPermissionGranted(PermissionGrantedResponse response) {
                                        updateLocation();
                                        startActivity(Utils.goToNextActivity(user, new Intent(GeoDataActivity.this, SendNotificationsActivity.class)));
                                    }

                                    @Override
                                    public void onPermissionDenied(PermissionDeniedResponse response) {
                                        startActivity(Utils.goToNextActivity(user, new Intent(GeoDataActivity.this, SendNotificationsActivity.class)));
                                    }

                                    @Override
                                    public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {

                                    }
                                }).check();
                                DBHandler.updateUser(GeoDataActivity.this, user, new DBHandler.VolleyCallback() {
                                    @Override
                                    public void onSuccess(String result) {
                                    }

                                    @Override
                                    public void onError(VolleyError error) {

                                    }
                                });
                            }
                            @Override
                            public void onError(VolleyError error) {
                            }
                        });
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onError(VolleyError error) {
                }
            });
        });
    }

    public void updateLocation() {
        buildLocationRequest();
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(GeoDataActivity.this);
        if (ActivityCompat.checkSelfPermission(GeoDataActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(GeoDataActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, getPendingIntent());
    }

    private PendingIntent getPendingIntent() {
        Intent intent = new Intent(GeoDataActivity.this, MyLocationServices.class);
        intent.setAction(MyLocationServices.ACTION_PROCESS_UPDATE);
        return PendingIntent.getBroadcast(GeoDataActivity.this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
    }

    private void buildLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(3000);
        locationRequest.setSmallestDisplacement(10f);
    }

    public void sentLocation(final double latitude, final double longitude) {
        GeoDataActivity.this.runOnUiThread(() -> {
            user.setLatitude(latitude);
            user.setLongitude(longitude);
            DBHandler.updateUser(GeoDataActivity.this, user, new DBHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {

                }

                @Override
                public void onError(VolleyError error) {

                }
            });
        });
    }
}
