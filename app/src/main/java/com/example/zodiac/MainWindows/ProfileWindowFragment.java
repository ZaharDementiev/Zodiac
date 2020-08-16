package com.example.zodiac.MainWindows;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.InnerWindows.ProfileEditActivity;
import com.example.zodiac.InnerWindows.ProfileSettingsActivity;
import com.example.zodiac.R;
import com.example.zodiac.Users.User;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfileWindowFragment extends Fragment {

    private User mainUser;
    private CircleImageView userProfilePhoto;
    private CircleImageView addPhoto;
    private CircleImageView settings;
    private CircleImageView editProfile;
    private TextView nameAgeProfileTextView;
    private TextView bioProfileTextView;
    private TextView settingsText;
    private TextView addPhotosText;
    private TextView editProfileText;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (getArguments() != null)
            mainUser = (User) getArguments().get("user");
        return inflater.inflate(R.layout.activity_profile_window_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        userProfilePhoto = getActivity().findViewById(R.id.userProfilePhoto);
        nameAgeProfileTextView = getActivity().findViewById(R.id.nameAgeProfileTextView);
        bioProfileTextView = getActivity().findViewById(R.id.bioProfileTextView);
        loadInfo();

        //addPhoto = getActivity().findViewById(R.id.addPhotosButton);
        settings = getActivity().findViewById(R.id.settingsButton);
        editProfile = getActivity().findViewById(R.id.editProfile);

        //addPhotosText = getActivity().findViewById(R.id.addPhotosText);
        settingsText = getActivity().findViewById(R.id.settingsText);
        editProfileText = getActivity().findViewById(R.id.editProfileText);

        //addPhoto.setOnClickListener(view -> startActivity(Utils.goToNextActivity(mainUser, new Intent(getActivity(), ProfilePhotosActivity.class))));
        //addPhotosText.setOnClickListener(view -> startActivity(Utils.goToNextActivity(mainUser, new Intent(getActivity(), ProfilePhotosActivity.class))));
        settings.setOnClickListener(view -> startActivity(Utils.goToNextActivity(mainUser, new Intent(getActivity(), ProfileSettingsActivity.class))));
        settingsText.setOnClickListener(view -> startActivity(Utils.goToNextActivity(mainUser, new Intent(getActivity(), ProfileSettingsActivity.class))));
        editProfile.setOnClickListener(view -> startActivityForResult(Utils.goToNextActivity(mainUser, new Intent(getActivity(), ProfileEditActivity.class)), 1));
        editProfileText.setOnClickListener(view -> startActivityForResult(Utils.goToNextActivity(mainUser, new Intent(getActivity(), ProfileEditActivity.class)), 1));
    }

    @SuppressLint("ResourceAsColor")
    private void loadInfo() {
        if (!mainUser.getMainPhoto().equals(""))
            Picasso.get().load("http://" + mainUser.getMainPhoto()).into(userProfilePhoto);
        String years = Utils.howOld(mainUser.getBirthday());
        nameAgeProfileTextView.setText(mainUser.getName() + ", " + years);
        bioProfileTextView.setText(mainUser.getBio());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1) {
            if (resultCode == getActivity().RESULT_OK){
                mainUser = data.getParcelableExtra("result");
                loadInfo();
            }
        }
    }
}