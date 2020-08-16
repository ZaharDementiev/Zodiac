package com.example.zodiac.NotificationWindows;

import androidx.appcompat.app.AppCompatActivity;

import com.example.zodiac.DB.SharedPrefManager;
import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.MainWindows.MainWindowActivity;
import com.example.zodiac.R;
import com.example.zodiac.Users.User;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;

public class SendNotificationsActivity extends AppCompatActivity {

    private Button notificationActivateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notifications);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        notificationActivateButton = findViewById(R.id.notificationActivateButton);
        Intent givenIntent = getIntent();
        User user = givenIntent.getParcelableExtra("user");

        SharedPrefManager.getInstance(getApplicationContext()).userLogin(
                user.getId(),
                user.getMail(),
                user.getPassword(),
                user.getPhoneNumber());

        notificationActivateButton.setOnClickListener(view -> startActivity(Utils
                .goToNextActivity(user, new Intent(SendNotificationsActivity.this, MainWindowActivity.class))));
    }
}
