package com.example.zodiac.SignIn;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.VolleyError;
import com.example.zodiac.DB.DBHandler;
import com.example.zodiac.DB.SharedPrefManager;
import com.example.zodiac.Helpers.ModelsFiller;
import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.MainWindows.MainWindowActivity;
import com.example.zodiac.R;
import com.example.zodiac.Users.User;
import com.hbb20.CountryCodePicker;

import org.json.JSONException;
import org.json.JSONObject;

public class PhoneSignInActivity extends AppCompatActivity {

    private EditText phoneText;
    private EditText passwordSignInEditText;
    private Button nextButton;
    private CountryCodePicker ccp;
    private TextView errorPhone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_number_signin);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        ccp = findViewById(R.id.ccpSign);
        phoneText = findViewById(R.id.phoneTextSign);
        ccp.registerCarrierNumberEditText(phoneText);
        nextButton = findViewById(R.id.phoneSignInButton);
        errorPhone = findViewById(R.id.errorPasswordPhone);
        passwordSignInEditText = findViewById(R.id.passwordSignInEditText);

        Utils.registrationButton(phoneText, nextButton, errorPhone, "Вы не ввели номер телефона");
        nextButton.setOnClickListener(v -> signIn());
    }

    private void signIn() {
        DBHandler.getUserByPhoneAndPassword(PhoneSignInActivity.this, ccp.getFullNumberWithPlus().replace(" ", ""),
                passwordSignInEditText.getText().toString().trim(), new DBHandler.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        try {
                            errorPhone.setVisibility(View.INVISIBLE);
                            JSONObject obj = new JSONObject(result);

                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(
                                    obj.getInt("id"),
                                    obj.getString("email"),
                                    obj.getString("password"),
                                    obj.getString("phone"));

                            User mainUser = ModelsFiller.fillUser(obj);

                            startActivity(Utils.goToNextActivity(mainUser, new Intent(PhoneSignInActivity.this, MainWindowActivity.class)));
                            finish();
                        } catch (JSONException e) {
                            e.printStackTrace();
                            errorPhone.setVisibility(View.VISIBLE);
                            errorPhone.setText("Неверные данные");
                        }
                    }

                    @Override
                    public void onError(VolleyError error) { }
                });
    }
}