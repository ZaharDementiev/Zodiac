package com.example.zodiac.Registration;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.R;
import com.example.zodiac.Users.User;

public class PasswordActivity extends AppCompatActivity {

    private EditText registerPasswordEditText;
    private EditText registerConfirmPasswordEditText;
    private TextView errorPassword;
    private Button passwordNextButton;
    private String passwordErrorText = "";
    private String currentPassword = "";
    private static final String SYMBOLS = "[!#$:%&*()_+=|<>?{}\\[\\]~]";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        registerPasswordEditText = findViewById(R.id.registerPasswordEditText);
        registerConfirmPasswordEditText = findViewById(R.id.registerConfirmPasswordEditText);
        errorPassword = findViewById(R.id.errorPassword);
        passwordNextButton = findViewById(R.id.passwordNextButton);

        Intent givenIntent = getIntent();
        User user = givenIntent.getParcelableExtra("user");

        Utils.registrationButton(registerConfirmPasswordEditText, passwordNextButton, errorPassword, passwordErrorText, currentPassword);
        passwordNextButton.setOnClickListener(v -> register(user));
    }

    private void register(User user) {
        //Проверки корректности пароля
        if ((!(Utils.isFieldNotValid(registerPasswordEditText.getText().toString().trim(), SYMBOLS))) &&
                (!(Utils.isFieldNotValid(registerConfirmPasswordEditText.getText().toString().trim(), SYMBOLS))))
            passwordErrorText = getResources().getString(R.string.incorrect_password_error);
        else if (!(registerPasswordEditText.getText().toString().equals(registerConfirmPasswordEditText.getText().toString())))
            passwordErrorText = getResources().getString(R.string.passwords_dont_match_error);
        else if (((registerPasswordEditText.length() < 7) && (registerConfirmPasswordEditText.length() < 7)))
            passwordErrorText = getResources().getString(R.string.password_symbols_error);
        else {
            errorPassword.setVisibility(View.INVISIBLE);
            Intent intent = new Intent(PasswordActivity.this, AdditionalActivity.class);
            user.setPassword(registerPasswordEditText.getText().toString().trim());
            startActivity(Utils.goToNextActivity(user, intent));
            passwordErrorText = "";
        }

        if (!passwordErrorText.equals("")) {
            Utils.switchOffButton(passwordNextButton, errorPassword, passwordErrorText);
            currentPassword = registerConfirmPasswordEditText.getText().toString().trim();
        }
    }
}