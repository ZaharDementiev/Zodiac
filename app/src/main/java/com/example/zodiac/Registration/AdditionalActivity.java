package com.example.zodiac.Registration;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import com.example.zodiac.AdditionalRegistration.NameActivity;
import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.R;
import com.example.zodiac.Users.User;

public class AdditionalActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent givenIntent = getIntent();
        User user = givenIntent.getParcelableExtra("user");

        Button nextAfterReg = findViewById(R.id.nextAfterReg);
        nextAfterReg.setOnClickListener(v -> startActivity(Utils.goToNextActivity(user, new Intent(AdditionalActivity.this, NameActivity.class))));
    }
}
