package com.example.zodiac.SignIn;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.R;

public class ConfirmCodeSignInActivity extends AppCompatActivity {

    private TextView myPhoneNumber;
    private Button nextButton;
    private EditText verifyCodeEditText;
    private TextView errorCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_code);

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //nextButton = findViewById(R.id.nextButtonConfirm);
        myPhoneNumber = findViewById(R.id.myCode);
        verifyCodeEditText = findViewById(R.id.verifyCodeEditText);
        errorCode = findViewById(R.id.errorCode);

        Utils.registrationButton(verifyCodeEditText, nextButton, 5, errorCode);

        //Intent intent = new Intent(ConfirmCodeSignInActivity.this, EmailAndPasswordActivity.class);
        //nextButton.setOnClickListener(v -> startActivity(Utils.goToNextActivity(user, intent)));
    }
}
