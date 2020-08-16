package com.example.zodiac.InnerWindows;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.example.zodiac.AdditionalRegistration.PhotosUploadActivity;
import com.example.zodiac.DB.DBHandler;
import com.example.zodiac.DB.SharedPrefManager;
import com.example.zodiac.Helpers.ModelsFiller;
import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.MainWindows.MainWindowActivity;
import com.example.zodiac.PersonalSettings.NewEmailActivity;
import com.example.zodiac.R;
import com.example.zodiac.Users.User;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProfileInfoEditFragment extends Fragment {

    private User mainUser;
    private LinearLayout linearLayout;
    private EditText aboutTextView;
    private TextView saveButton;
    private TextView backButton;
    private List<ImageView> imageViews;

    private static int IMAGE_QUALITY = 74;
    private ArrayList<Uri> imageList = new ArrayList<>();
    private ArrayList<String> imageListStrings = new ArrayList<>();
    private int counter = 0;
    private int preloadedImages = 0;

    ProfileInfoEditFragment(User user) {
        this.mainUser = user;
    }

    ProfileInfoEditFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            mainUser = ((ProfileEditActivity)getActivity()).getMainUser();
        return inflater.inflate(R.layout.activity_profile_info_fragment,container,false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        addImageButtonPressed();
        initImageViews();

        DBHandler.getPhotos(getActivity(), mainUser.getMail(), new DBHandler.VolleyCallback() {
            @Override
            public void onSuccess(String result) {
                ModelsFiller.fillImagesList(imageListStrings, result);
                preloadedImages = imageListStrings.size();
                counter = preloadedImages;
                for (int i = 0; i < preloadedImages; i++) {
                    Picasso.get().load("http://" + imageListStrings.get(i)).into(imageViews.get(i));
                }
            }
            @Override
            public void onError(VolleyError error) {
            }
        });

        linearLayout = getActivity().findViewById(R.id.photoLayoutEdit);
        Display display = getActivity().getWindowManager().getDefaultDisplay();
        int id = R.id.saveButton;
        Utils.displayPhotosLayout(linearLayout, display,50, id);

        saveButton = getActivity().findViewById(R.id.saveButton);
        backButton = getActivity().findViewById(R.id.backButton);
        aboutTextView = getActivity().findViewById(R.id.aboutEditText);
        aboutTextView.setText(mainUser.getBio());

        backButton.setOnClickListener(view -> getActivity().onBackPressed());

        saveButton.setOnClickListener(view -> {
            mainUser.setBio(aboutTextView.getText().toString());
            DBHandler.updateUser(ProfileInfoEditFragment.this.getActivity(), mainUser, new DBHandler.VolleyCallback() {
                @Override
                public void onSuccess(String result) {
                    ((ProfileEditActivity)getActivity()).setMainUser(mainUser);
                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",mainUser);
                    getActivity().setResult(getActivity().RESULT_OK,returnIntent);
                }

                @Override
                public void onError(VolleyError error) {

                }
            });

            if (preloadedImages > 0) {
                for (int i = preloadedImages; i < imageListStrings.size(); i++)
                    DBHandler.uploadPhoto(getActivity(), imageListStrings.get(i), mainUser.getMail(), new DBHandler.VolleyCallback() {
                        @Override
                        public void onSuccess(String result) {

                        }

                        @Override
                        public void onError(VolleyError error) {

                        }
                    });
            }
            //getActivity().onBackPressed();
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = CropImage.getPickImageResultUri(getActivity(), data);
            CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(70,100).start(this.getActivity());
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && CropImage.getActivityResult(data) != null && counter != 9) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageList.add(result.getUri());
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(this.getContext().getContentResolver(), result.getUri());
                Picasso.get().load(result.getUri()).into(imageViews.get(counter));
                counter += 1;
            } catch (IOException e) {
                e.printStackTrace();
            }
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, IMAGE_QUALITY, outputStream);
            byte[] imageBytes = outputStream.toByteArray();
            String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imageListStrings.add(encodedImage);
        }
    }

    private void chooseImage() {
        CropImage.startPickImageActivity(getActivity());
    }

    private void initImageViews() {
        ImageView photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9;

        photo1 = getActivity().findViewById(R.id.photo11);
        photo2 = getActivity().findViewById(R.id.photo22);
        photo3 = getActivity().findViewById(R.id.photo33);
        photo4 = getActivity().findViewById(R.id.photo44);
        photo5 = getActivity().findViewById(R.id.photo55);
        photo6 = getActivity().findViewById(R.id.photo66);
        photo7 = getActivity().findViewById(R.id.photo77);
        photo8 = getActivity().findViewById(R.id.photo88);
        photo9 = getActivity().findViewById(R.id.photo99);

        imageViews = new ArrayList<>();
        imageViews.add(photo1);
        imageViews.add(photo2);
        imageViews.add(photo3);
        imageViews.add(photo4);
        imageViews.add(photo5);
        imageViews.add(photo6);
        imageViews.add(photo7);
        imageViews.add(photo8);
        imageViews.add(photo9);
    }

    private void addImageButtonPressed() {
        ImageView photo1Add, photo2Add, photo3Add, photo4Add, photo5Add, photo6Add, photo7Add, photo8Add, photo9Add;
        photo1Add = getActivity().findViewById(R.id.photo11Add);
        photo2Add = getActivity().findViewById(R.id.photo22Add);
        photo3Add = getActivity().findViewById(R.id.photo33Add);
        photo4Add = getActivity().findViewById(R.id.photo44Add);
        photo5Add = getActivity().findViewById(R.id.photo55Add);
        photo6Add = getActivity().findViewById(R.id.photo66Add);
        photo7Add = getActivity().findViewById(R.id.photo77Add);
        photo8Add = getActivity().findViewById(R.id.photo88Add);
        photo9Add = getActivity().findViewById(R.id.photo99Add);

        photo1Add.setOnClickListener(view -> chooseImage());
        photo2Add.setOnClickListener(view -> chooseImage());
        photo3Add.setOnClickListener(view -> chooseImage());
        photo4Add.setOnClickListener(view -> chooseImage());
        photo5Add.setOnClickListener(view -> chooseImage());
        photo6Add.setOnClickListener(view -> chooseImage());
        photo7Add.setOnClickListener(view -> chooseImage());
        photo8Add.setOnClickListener(view -> chooseImage());
        photo9Add.setOnClickListener(view -> chooseImage());
    }
}