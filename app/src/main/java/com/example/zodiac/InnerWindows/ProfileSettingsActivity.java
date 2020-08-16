package com.example.zodiac.InnerWindows;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.VolleyError;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.crystal.crystalrangeseekbar.widgets.CrystalSeekbar;
import com.example.zodiac.Chat.ChatItem;
import com.example.zodiac.DB.DBHandler;
import com.example.zodiac.DB.SharedPrefManager;
import com.example.zodiac.Helpers.ModelsFiller;
import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.Images.Image;
import com.example.zodiac.MainActivity;
import com.example.zodiac.NotificationWindows.GeoDataActivity;
import com.example.zodiac.NotificationWindows.SendNotificationsActivity;
import com.example.zodiac.PersonalSettings.ChangeEmailActivity;
import com.example.zodiac.PersonalSettings.ChangePasswordActivity;
import com.example.zodiac.PersonalSettings.ChangePhoneNumberActivity;
import com.example.zodiac.R;
import com.example.zodiac.SearchingData.SearchingSettings;
import com.example.zodiac.Users.User;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ProfileSettingsActivity extends AppCompatActivity {

    private User mainUser;

    private TextView emailSettingsTextView;
    private TextView numberSettingsText;
    private TextView passwordSettingsText;
    private CrystalSeekbar geoSeekBar;
    private CrystalRangeSeekbar ageSeekBar;

    private LinearLayout emailLayout;
    private LinearLayout numberSettingsLinear;
    private LinearLayout passwordSettingsLinear;

    private TextView saveButton;
    private TextView exitButton;
    private TextView deleteAccount;
    private TextView backButton;

    private TextView geoTextView;
    private TextView currentAge;

    private Switch aSwitch;

    private String oldEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_settings);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent givenIntent = getIntent();
        mainUser = givenIntent.getParcelableExtra("user");
        oldEmail = mainUser.getMail();
        emailSettingsTextView = findViewById(R.id.emailSettingsTextView);
        numberSettingsText = findViewById(R.id.numberSettingsText);
        passwordSettingsText = findViewById(R.id.passwordSettingsText);
        geoSeekBar = findViewById(R.id.geoSeekBar);
        ageSeekBar = findViewById(R.id.ageSeekBar);
        saveButton = findViewById(R.id.saveSettingsButton);
        exitButton = findViewById(R.id.exitButton);
        deleteAccount = findViewById(R.id.deleteAccount);
        backButton = findViewById(R.id.backButton);

        emailLayout = findViewById(R.id.emailLayout);
        numberSettingsLinear = findViewById(R.id.numberSettingsLinear);
        passwordSettingsLinear = findViewById(R.id.passwordSettingsLinear);

        geoTextView = findViewById(R.id.currentGeo);
        currentAge = findViewById(R.id.currentAge);
        aSwitch = findViewById(R.id.geoCheck);

        DBHandler.getUserById(ProfileSettingsActivity.this, mainUser.getId(), new DBHandler.VolleyCallback() {
            @Override
            public void onSuccess(String result) {

                try {
                    JSONObject obj = new JSONObject(result);
                    mainUser = ModelsFiller.fillUser(obj);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                emailSettingsTextView.setText(mainUser.getMail());
                numberSettingsText.setText(mainUser.getPhoneNumber());
                passwordSettingsText.setText(mainUser.getPassword());

                emailLayout.setOnClickListener(view -> startActivity(Utils.goToNextActivity(mainUser, new Intent(ProfileSettingsActivity.this, ChangeEmailActivity.class))));
                numberSettingsLinear.setOnClickListener(view -> startActivity(Utils.goToNextActivity(mainUser, new Intent(ProfileSettingsActivity.this, ChangePhoneNumberActivity.class))));
                passwordSettingsLinear.setOnClickListener(view -> startActivity(Utils.goToNextActivity(mainUser, new Intent(ProfileSettingsActivity.this, ChangePasswordActivity.class))));
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

        backButton.setOnClickListener(view -> finish());

        saveButton.setOnClickListener(view -> {
            SearchingSettings searchingSettings = new SearchingSettings();
            searchingSettings.setUserId(mainUser.getId());
            searchingSettings.setFindingRadius(geoSeekBar.getSelectedMinValue().intValue());
            searchingSettings.setAgeStart(ageSeekBar.getSelectedMinValue().intValue());
            searchingSettings.setAgeEnd(ageSeekBar.getSelectedMaxValue().intValue());
            DBHandler.updateSearchingSettings(ProfileSettingsActivity.this, searchingSettings);
            finish();
        });

        if (ContextCompat.checkSelfPermission(ProfileSettingsActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == -1)
            aSwitch.setChecked(false);
        else
            aSwitch.setChecked(true);

        aSwitch.setOnClickListener(view -> {
            if (aSwitch.isChecked())
                ActivityCompat.requestPermissions(ProfileSettingsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 228);
            else
                Toast.makeText(ProfileSettingsActivity.this, "Перейдите в настройки устройства", Toast.LENGTH_SHORT).show();
        });

        geoSeekBar.setOnSeekbarChangeListener((value) -> geoTextView.setText(String.valueOf(value)+ " km"));
        ageSeekBar.setOnRangeSeekbarChangeListener((minValue, maxValue) -> currentAge.setText(String.valueOf(minValue) + " - " + String.valueOf(maxValue)));

        exitButton.setOnClickListener(view ->  logOut());

        deleteAccount.setOnClickListener(view -> {
            DBHandler.deleteUser(ProfileSettingsActivity.this, mainUser.getMail());
            DBHandler.deleteSympathy(ProfileSettingsActivity.this, mainUser.getId());
            DBHandler.deleteSwipe(ProfileSettingsActivity.this, mainUser.getId());
            DBHandler.deleteSearchingSettings(ProfileSettingsActivity.this, mainUser.getId());
            List<Long> chats = new ArrayList<>();
            DBHandler.getChats(ProfileSettingsActivity.this, mainUser.getId(), new DBHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    try {
                        JSONArray jsonArray = new JSONObject(result).getJSONArray("result");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            ChatItem item = ModelsFiller.chatItemFillerId(jsonArray.getJSONObject(i));
                            chats.add(item.getId());
                        }
                        for (int i = 0; i < chats.size(); i++)
                            DBHandler.deleteMessages(ProfileSettingsActivity.this, chats.get(i));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                @Override
                public void onError(VolleyError error) { }
            });
            DBHandler.deletePhotos(ProfileSettingsActivity.this, mainUser.getMail());
            DBHandler.deleteChats(ProfileSettingsActivity.this, mainUser.getId());
            logOut();
        });
    }

    private void logOut() {
        SharedPrefManager.getInstance(ProfileSettingsActivity.this).logout();
        finish();
        startActivity(new Intent(ProfileSettingsActivity.this, MainActivity.class));
    }
}