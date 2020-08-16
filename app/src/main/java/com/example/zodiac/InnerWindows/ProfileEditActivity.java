package com.example.zodiac.InnerWindows;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.example.zodiac.DB.DBHandler;
import com.example.zodiac.Helpers.ModelsFiller;
import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.MainWindows.MainWindowActivity;
import com.example.zodiac.R;
import com.example.zodiac.Users.User;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ProfileEditActivity extends AppCompatActivity {

    private TextView edit;
    private TextView view;

    private User mainUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit);
        edit = findViewById(R.id.profileEditTextView);
        view = findViewById(R.id.profileViewTextView);
        LinearLayout navigationLayout = findViewById(R.id.settingsEdit);
        edit.setTextColor(getResources().getColor(R.color.mainColor));
        edit.setBackground(getDrawable(R.drawable.bottom_stroke_pressed));

        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        mainUser = getIntent().getParcelableExtra("user");
        Bundle bundle = new Bundle();
        bundle.putParcelable("user", mainUser);

        DBHandler.getPhotos(this, mainUser.getMail(), new DBHandler.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                List<String> images = new ArrayList<>();
                ModelsFiller.fillImagesList(images, result);
                mainUser.setPhotos(images);
            }

            @Override
            public void onError(VolleyError error) {

            }
        });

        getSupportFragmentManager().beginTransaction().replace(R.id.settingsContainer, new ProfileInfoEditFragment(mainUser)).commit();

        edit.setOnClickListener(v -> {
            Fragment selectedFragment = null;
            selectedFragment = new ProfileInfoEditFragment();
            selectedFragment.setArguments(bundle);
            edit.setTextColor(getResources().getColor(R.color.white));
            view.setTextColor(getResources().getColor(R.color.gray_light));
            edit.setBackground(getDrawable(R.drawable.bottom_stroke_pressed));
            view.setBackground(getDrawable(R.drawable.bottom_stroke_code));
            getSupportFragmentManager().beginTransaction().replace(R.id.settingsContainer, selectedFragment).commit();
        });

        view.setOnClickListener(v -> {
            Fragment selectedFragment = null;
            selectedFragment = new ProfileViewFragment();
            selectedFragment.setArguments(bundle);
            edit.setTextColor(getResources().getColor(R.color.gray_light));
            view.setTextColor(getResources().getColor(R.color.white));
            edit.setBackground(getDrawable(R.drawable.bottom_stroke_code));
            view.setBackground(getDrawable(R.drawable.bottom_stroke_pressed));
            getSupportFragmentManager().beginTransaction().replace(R.id.settingsContainer, selectedFragment).commit();
        });
    }

    protected User getMainUser() {
        return mainUser;
    }

    protected void setMainUser(User user) {
        mainUser = user;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (Fragment fragment : getSupportFragmentManager().getFragments()) {
            fragment.onActivityResult(requestCode, resultCode, data);
        }
    }
}