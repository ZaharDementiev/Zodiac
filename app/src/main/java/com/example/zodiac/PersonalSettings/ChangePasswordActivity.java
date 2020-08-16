package com.example.zodiac.PersonalSettings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.TextView;

import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.R;
import com.example.zodiac.Users.User;

public class ChangePasswordActivity extends AppCompatActivity {

    private TextView changePasswordTextView;
    private TextView changeEmailButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent givenIntent = getIntent();
        User mainUser = givenIntent.getParcelableExtra("user");
        changePasswordTextView = findViewById(R.id.changePasswordTextView);
        changeEmailButton = findViewById(R.id.changeEmailButton);
        changePasswordTextView.setText(mainUser.getPassword());

        changeEmailButton.setOnClickListener(view -> {
            startActivity(Utils.goToNextActivity(mainUser, new Intent(ChangePasswordActivity.this, NewPasswordActivity.class)));
            finish();
        });
    }
}