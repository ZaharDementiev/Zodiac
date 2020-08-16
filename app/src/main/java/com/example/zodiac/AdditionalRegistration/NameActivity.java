package com.example.zodiac.AdditionalRegistration;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.R;
import com.example.zodiac.Users.User;

public class NameActivity extends AppCompatActivity {

    private Button registerNameBtn;
    private TextView errorName;
    private EditText registerNameEditText;
    private static final String SYMBOLS1 = "[!@#$:%&*()+=|<>?{}\\[\\]~]";
    private static final String SYMBOLS2 = "[0-9]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        registerNameEditText = findViewById(R.id.registerNameEditText);
        errorName = findViewById(R.id.errorName);
        registerNameBtn = findViewById(R.id.registerNameBtn);

        Intent givenIntent = getIntent();
        User user = givenIntent.getParcelableExtra("user");

        Utils.registrationButton(registerNameEditText, registerNameBtn, errorName, "Вы не ввели имя");
        registerNameBtn.setOnClickListener(v -> register(user));
    }

    private void register(User user) {
        if (!Utils.isFieldNotValid(registerNameEditText.getText().toString().trim(), SYMBOLS1, SYMBOLS2)) {
            Intent intent = new Intent(NameActivity.this, BirthdayActivity.class);
            user.setName(registerNameEditText.getText().toString().trim());
            startActivity(Utils.goToNextActivity(user, intent));
        } else {
            Utils.switchOffButton(registerNameBtn, errorName, getResources().getString(R.string.name_registered_error));
        }
    }
}
