package com.example.zodiac.PersonalSettings;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.zodiac.DB.DBHandler;
import com.example.zodiac.DB.SharedPrefManager;
import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.R;
import com.example.zodiac.Registration.ConfirmCodeActivity;
import com.example.zodiac.Registration.PhoneNumberActivity;
import com.example.zodiac.SignIn.ConfirmCodeSignInActivity;
import com.example.zodiac.SignIn.PhoneSignInActivity;
import com.example.zodiac.Users.User;
import com.hbb20.CountryCodePicker;

public class NewPhoneNumberActivity extends AppCompatActivity {

    private EditText phoneText;
    private Button nextButton;
    private CountryCodePicker ccp;
    private TextView errorPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_phone_number);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        User user = getIntent().getParcelableExtra("user");

        ccp = findViewById(R.id.ccp);
        phoneText = findViewById(R.id.phoneText);
        ccp.registerCarrierNumberEditText(phoneText);
        nextButton = findViewById(R.id.nextButton);
        errorPhone = findViewById(R.id.errorPhone);

        Utils.registrationButton(phoneText, nextButton, errorPhone, getResources().getString(R.string.number_registered_error));

        nextButton.setOnClickListener(v -> {
            user.setPhoneNumber(ccp.getFullNumberWithPlus().replace(" ", ""));
            DBHandler.updateUserById(NewPhoneNumberActivity.this, user, new DBHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    SharedPrefManager.getInstance(getApplicationContext()).userLogin(
                            user.getId(),
                            user.getMail(),
                            user.getPassword(),
                            user.getPhoneNumber());
                }

                @Override
                public void onError(VolleyError error) {

                }
            });
            finish();
        });
    }
}