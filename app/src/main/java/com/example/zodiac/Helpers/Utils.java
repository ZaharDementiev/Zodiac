package com.example.zodiac.Helpers;

import android.content.Intent;
import android.graphics.Point;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.example.zodiac.R;
import com.example.zodiac.Users.User;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    public static Intent goToNextActivity(User user, Intent intent) {
        intent.putExtra("user", user);
        return intent;
    }

    public static boolean isFieldNotValid(String value, String symbols) {
        Pattern special = Pattern.compile (symbols);
        Matcher hasSpecial = special.matcher(value);
        return !(TextUtils.isEmpty(value) || hasSpecial.find());
    }

    public static boolean isFieldNotValid(String value, String symbols1, String symbols2) {
        Pattern digit = Pattern.compile(symbols1);
        Pattern special = Pattern.compile (symbols2);
        Matcher hasDigit = digit.matcher(value);
        Matcher hasSpecial = special.matcher(value);
        return (TextUtils.isEmpty(value) || hasDigit.find() || hasSpecial.find());
    }

    // Кнопка далее активна, если введено более 1 символа
    public static void registrationButton(EditText editText, Button button, TextView errorTextView, String errorText) {
        switchOffButton(button);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0)
                    enableButton(button, errorTextView);
                else
                    switchOffButton(button, errorTextView, errorText);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    // Кнопка далее активна, если введено более 1 символа и вводимое значение не равно предыдущему
    public static void registrationButton(EditText editText, Button button, TextView errorTextView, String errorText, String currentInputtingValue) {
        switchOffButton(button);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() > 0 && !editText.getText().toString().equals(currentInputtingValue))
                    enableButton(button, errorTextView);
                else
                    switchOffButton(button, errorTextView, errorText);
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }
    // Кнопка далее активна, если введено определенное кол-во символов
    public static void registrationButton(EditText editText, Button button, int symbolsCount, TextView errorTextView) {
        switchOffButton(button);
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().trim().length() == symbolsCount)
                    enableButton(button, errorTextView);
                else
                    switchOffButton(button, errorTextView, "Вы ввели неверное кол-во символов");
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private static void enableButton(Button button, TextView errorTextView) {
        button.setEnabled(true);
        button.setBackgroundResource(R.drawable.next_button_background);
        errorTextView.setVisibility(View.INVISIBLE);
    }
    public static void switchOffButton(Button button, TextView errorTextView, String text) {
        button.setEnabled(false);
        button.setBackgroundResource(R.drawable.disabled_button);
        errorTextView.setVisibility(View.VISIBLE);
        errorTextView.setText(text);
    }
    public static void switchOffButton(Button button) {
        button.setEnabled(false);
        button.setBackgroundResource(R.drawable.disabled_button);
    }

    public static String howOld(String userBirthday) {
        String years = "";
        String timeStamp = new SimpleDateFormat("yyyyMMdd").format(Calendar.getInstance().getTime());
        int day = Integer.parseInt(timeStamp.substring(6));
        int month = Integer.parseInt(timeStamp.substring(4, 6));
        int year = Integer.parseInt(timeStamp.substring(0, 4));

        int dayUser = Integer.parseInt(userBirthday.substring(0, 2));
        int monthUser = Integer.parseInt(userBirthday.substring(3, 5));
        int yearUser = Integer.parseInt(userBirthday.substring(6));


        if (month < monthUser)
            years = String.valueOf(year - yearUser - 1);
        else if (month == monthUser) {
            if (day > dayUser || day == dayUser)
                years = String.valueOf(year - yearUser);
            else
                years = String.valueOf(year - yearUser - 1);
        } else
            years = String.valueOf(year - yearUser);
        return years;
    }

    public static void displayPhotosLayout(LinearLayout linearLayout, Display display, int margin, int id){
        Point size = new Point();
        try {
            display.getRealSize(size);
        } catch (NoSuchMethodError err) {
            display.getSize(size);
        }
        int width = size.x;


        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                (int)(width*1.2));

        lp.addRule(RelativeLayout.BELOW,id);
        lp.setMargins(0,margin,0,0);

        linearLayout.setLayoutParams(lp);

    }
}
