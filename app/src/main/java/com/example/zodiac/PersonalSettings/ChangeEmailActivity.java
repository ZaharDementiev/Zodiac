package com.example.zodiac.PersonalSettings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.R;
import com.example.zodiac.Users.User;

public class ChangeEmailActivity extends AppCompatActivity {

    private TextView changeEmailTextView;
    private TextView changeEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_email);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent givenIntent = getIntent();
        User mainUser = givenIntent.getParcelableExtra("user");
        changeEmailTextView = findViewById(R.id.changeEmailTextView);
        changeEmailButton = findViewById(R.id.changeEmailButton);
        changeEmailTextView.setText(mainUser.getMail());

        changeEmailButton.setOnClickListener(view -> {
            startActivity(Utils.goToNextActivity(mainUser, new Intent(ChangeEmailActivity.this, NewEmailActivity.class)));
            finish();
        });
    }
}