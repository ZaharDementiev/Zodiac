package com.example.zodiac.PersonalSettings;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.zodiac.DB.Constants;
import com.example.zodiac.DB.DBHandler;
import com.example.zodiac.DB.RequestHandler;
import com.example.zodiac.DB.SharedPrefManager;
import com.example.zodiac.Helpers.ModelsFiller;
import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.InnerWindows.ProfileSettingsActivity;
import com.example.zodiac.R;
import com.example.zodiac.Registration.EmailActivity;
import com.example.zodiac.Registration.PasswordActivity;
import com.example.zodiac.Users.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewEmailActivity extends AppCompatActivity {

    private EditText registerEmailEditText;
    private Button nextActivity;
    private TextView errorEmail;
    private static final String SYMBOLS = "[!#$:%&*()_+=|<>?{}\\[\\]~]";
    private String currentMail = "";
    private boolean isExist;
    private String oldEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email_change);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        nextActivity = findViewById(R.id.emailNextButton);
        registerEmailEditText = findViewById(R.id.registerEmailEditText);
        errorEmail = findViewById(R.id.errorEmail);

        Intent givenIntent = getIntent();
        User user = givenIntent.getParcelableExtra("user");
        oldEmail = user.getMail();

        Utils.registrationButton(registerEmailEditText, nextActivity, errorEmail, "Почта не введена", currentMail);
        nextActivity.setOnClickListener(v ->
                isEmailExist(new DBHandler.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {
                        boolean check = Utils.isFieldNotValid(registerEmailEditText.getText().toString().trim(), SYMBOLS);
                        boolean check2 = !(registerEmailEditText.getText().toString().contains("@"));
                        boolean check3 = !(registerEmailEditText.getText().toString().contains("."));
                        String error = "";
                        if (!check || (check2 || check3))
                            error = "Неверный формат почты";
                        else if (isExist)
                            error = "Данная почта уже зарегестрирована";
                        else {
                            user.setMail(registerEmailEditText.getText().toString().trim());
                            DBHandler.updateUserById(NewEmailActivity.this, user, new DBHandler.VolleyCallback() {
                                @Override
                                public void onSuccess(String result) {
                                    DBHandler.getPhotos(NewEmailActivity.this, oldEmail, new DBHandler.VolleyCallback() {
                                        @Override
                                        public void onSuccess(String result) {
                                            List<String> images = new ArrayList<>();
                                            ModelsFiller.fillImagesList(images, result);
                                            for (String s : images) {
                                                DBHandler.updatePhotoOwner(NewEmailActivity.this, s, user.getMail());
                                            }
                                            SharedPrefManager.getInstance(getApplicationContext()).userLogin(
                                                    user.getId(),
                                                    user.getMail(),
                                                    user.getPassword(),
                                                    user.getPhoneNumber());
                                            finish();
                                        }
                                        @Override
                                        public void onError(VolleyError error) {
                                        }
                                    });
                                }

                                @Override
                                public void onError(VolleyError error) {

                                }
                            });
                        }
                        if (!error.equals("")) {
                            Utils.switchOffButton(nextActivity, errorEmail, error);
                            currentMail = registerEmailEditText.getText().toString().trim();
                        }
                    }

                    @Override
                    public void onError(VolleyError error) { }
                }));
    }

    // Проверка наличия введенной почты в БД
    private void isEmailExist(final DBHandler.VolleyCallback callback) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_USER_CHECK_MAIL, response -> {
            isExist = !response.isEmpty();
            callback.onSuccess(response);
        }, error -> {}
        ) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("email", registerEmailEditText.getText().toString());
                return params;
            }
        };
        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
