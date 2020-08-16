package com.example.zodiac.AdditionalRegistration;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.R;
import com.example.zodiac.Users.User;
import java.util.Calendar;

public class BirthdayActivity extends AppCompatActivity {

    private EditText editText;
    private Button dateNextButton;
    private TextView errorDate;
    private String current = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_birthday);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent givenIntent = getIntent();
        User user = givenIntent.getParcelableExtra("user");
        Intent intent = new Intent(BirthdayActivity.this, PhotosUploadActivity.class);

        dateNextButton = findViewById(R.id.dateNextButton);
        dateNextButton.setEnabled(false);
        editText = findViewById(R.id.myDate);
        errorDate = findViewById(R.id.errorDate);
        birthdayTextChangeListener();

        Utils.switchOffButton(dateNextButton);
        dateNextButton.setOnClickListener(view -> {

            if (Integer.parseInt(Utils.howOld(current)) >= 18) {
                errorDate.setVisibility(View.INVISIBLE);
                user.setBirthday(current);
                startActivity(Utils.goToNextActivity(user, intent));
            } else {
                errorDate.setVisibility(View.VISIBLE);
                errorDate.setText("Вам нету 18");
            }
        });
    }

    private void birthdayTextChangeListener() {
        editText.addTextChangedListener(new TextWatcher() {
            String ddmmyyyy = "ДДMMГГГГ";
            Calendar cal = Calendar.getInstance();

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]", "");
                    String cleanC = current.replaceAll("[^\\d.]", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) sel++;
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8) clean = clean + ddmmyyyy.substring(clean.length());

                    else {
                        int day = Integer.parseInt(clean.substring(0, 2));
                        int mon = Integer.parseInt(clean.substring(2, 4));
                        int year = Integer.parseInt(clean.substring(4, 8));

                        if (mon > 12) mon = 12;
                        cal.set(Calendar.MONTH, mon - 1);
                        year = (year < 1900) ? 1900 : Math.min(year, 2100);
                        cal.set(Calendar.YEAR, year);

                        day = Math.min(day, cal.getActualMaximum(Calendar.DATE));
                        clean = String.format("%02d%02d%02d", day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2), clean.substring(2, 4), clean.substring(4, 8));

                    sel = Math.max(sel, 0);
                    current = clean;
                    editText.setText(current);
                    editText.setSelection(Math.min(sel, current.length()));

                    String tmp = current.replace("/", "");
                    try {
                        int date = Integer.parseInt(tmp);
                        if (tmp.length() == 8) {
                            dateNextButton.setEnabled(true);
                            dateNextButton.setBackgroundResource(R.drawable.next_button_background);
                        } else
                            blockButton();
                    } catch (Exception e) {
                        blockButton();
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void blockButton() {
        dateNextButton.setEnabled(false);
        dateNextButton.setBackgroundResource(R.drawable.disabled_button);
    }
}
