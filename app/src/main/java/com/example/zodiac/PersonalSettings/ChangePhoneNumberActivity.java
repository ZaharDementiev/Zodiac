package com.example.zodiac.PersonalSettings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.R;
import com.example.zodiac.Users.User;

public class ChangePhoneNumberActivity extends AppCompatActivity {

    private TextView changePhoneNumberTextView;
    private TextView changePhoneNumberButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_phone_number);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent givenIntent = getIntent();
        User mainUser = givenIntent.getParcelableExtra("user");
        changePhoneNumberTextView = findViewById(R.id.changePhoneNumberTextView);
        changePhoneNumberButton = findViewById(R.id.changePhoneNumberButton);
        changePhoneNumberTextView.setText(mainUser.getPhoneNumber());

        changePhoneNumberButton.setOnClickListener(view -> {
            startActivity(Utils.goToNextActivity(mainUser, new Intent(ChangePhoneNumberActivity.this, NewPhoneNumberActivity.class)));
            finish();
        });
    }
}