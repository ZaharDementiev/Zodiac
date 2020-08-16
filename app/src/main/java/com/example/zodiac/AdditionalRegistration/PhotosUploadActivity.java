package com.example.zodiac.AdditionalRegistration;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.Display;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.android.volley.VolleyError;
import com.example.zodiac.DB.DBHandler;
import com.example.zodiac.Helpers.Utils;
import com.example.zodiac.NotificationWindows.GeoDataActivity;
import com.example.zodiac.R;
import com.example.zodiac.Users.User;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static com.example.zodiac.Helpers.Utils.displayPhotosLayout;

public class PhotosUploadActivity extends AppCompatActivity {

    private List<ImageView> imageViews;
    private Button nextButtonPhoto;
    private ArrayList<Uri> imageList = new ArrayList<>();
    private ArrayList<String> imageListStrings = new ArrayList<>();
    private User user;
    private static int IMAGE_QUALITY = 74;
    int counter = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photos_upload);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Intent givenIntent = getIntent();
        user = givenIntent.getParcelableExtra("user");

        Intent intent = new Intent(PhotosUploadActivity.this, GeoDataActivity.class);

        addImageButtonPressed();
        initImageViews();
        nextButtonPhoto.setOnClickListener(view -> {

            for (int i = 0; i < imageListStrings.size(); i++)
                DBHandler.uploadPhoto(PhotosUploadActivity.this, imageListStrings.get(i), user.getMail(), new DBHandler.VolleyCallback() {
                    @Override
                    public void onSuccess(String result) {

                    }

                    @Override
                    public void onError(VolleyError error) {

                    }
                });
            DBHandler.createUser(this, user);

            startActivity(Utils.goToNextActivity(user, intent));
            finish();
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.PICK_IMAGE_CHOOSER_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            Uri uri = CropImage.getPickImageResultUri(this, data);
            CropImage.activity(uri).setGuidelines(CropImageView.Guidelines.ON).setAspectRatio(70,100).start(this);
        }

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && CropImage.getActivityResult(data) != null && counter != 9) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imageList.add(result.getUri());
            Bitmap bitmap = null;
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), result.getUri());
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
        CropImage.startPickImageActivity(this);
    }

    private void initImageViews() {
        ImageView photo1, photo2, photo3, photo4, photo5, photo6, photo7, photo8, photo9;

        photo1 = findViewById(R.id.photo1);
        photo2 = findViewById(R.id.photo2);
        photo3 = findViewById(R.id.photo3);
        photo4 = findViewById(R.id.photo4);
        photo5 = findViewById(R.id.photo5);
        photo6 = findViewById(R.id.photo6);
        photo7 = findViewById(R.id.photo7);
        photo8 = findViewById(R.id.photo8);
        photo9 = findViewById(R.id.photo9);
        nextButtonPhoto = findViewById(R.id.nextButtonPhoto);

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
        photo1Add = findViewById(R.id.photo1Add);
        photo2Add = findViewById(R.id.photo2Add);
        photo3Add = findViewById(R.id.photo3Add);
        photo4Add = findViewById(R.id.photo4Add);
        photo5Add = findViewById(R.id.photo5Add);
        photo6Add = findViewById(R.id.photo6Add);
        photo7Add = findViewById(R.id.photo7Add);
        photo8Add = findViewById(R.id.photo8Add);
        photo9Add = findViewById(R.id.photo9Add);

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
