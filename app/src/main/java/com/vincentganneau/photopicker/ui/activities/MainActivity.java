package com.vincentganneau.photopicker.ui.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.vincentganneau.photopicker.R;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * {@link AppCompatActivity} that enables the user to pick a photo.
 *
 * @author Vincent Ganneau
 *
 * Copyright (C) 2015
 */
public class MainActivity extends AppCompatActivity {

    // Views
    private ImageView mImageView;
    private Button mPickPhotoButton;

    // Photo
    private Uri mPhotoUri;
    private String mPhotoPath;
    private String mPhotoFilePath;
    private static final String KEY_PHOTO_URI = "com.vincentganneau.photopicker.key.PHOTO_URI";
    private static final String KEY_PHOTO_PATH = "com.vincentganneau.photopicker.key.PHOTO_PATH";

    // Request code
    private static final int PICK_PHOTO_REQUEST = 1;

    // Permissions
    private static final int REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION = 0;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImageView = (ImageView) findViewById(R.id.image);
        mPickPhotoButton = (Button) findViewById(R.id.btn_pick_photo);
        if (savedInstanceState != null || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            mPickPhotoButton.setEnabled(true);
        } else {
            ActivityCompat.requestPermissions(this, new String[] { Manifest.permission.WRITE_EXTERNAL_STORAGE }, REQUEST_WRITE_EXTERNAL_STORAGE_PERMISSION);
        }
        if (savedInstanceState != null) {
            mPhotoUri = savedInstanceState.getParcelable(KEY_PHOTO_URI);
            mPhotoPath = savedInstanceState.getString(KEY_PHOTO_PATH);
            setPhoto();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(KEY_PHOTO_URI, mPhotoUri);
        outState.putString(KEY_PHOTO_PATH, mPhotoPath);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mPickPhotoButton.setEnabled(true);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PICK_PHOTO_REQUEST && resultCode == Activity.RESULT_OK) {
            final Uri uri = data.getData();
            if (uri != null) {
                mPhotoUri = uri;
                mPhotoPath = null;
            } else {
                mPhotoUri = null;
                mPhotoPath = mPhotoFilePath;
            }
            setPhoto();
        }
    }

    // Methods
    /**
     * Called when the "Pick photo" button has been clicked.
     * @param view the "Pick photo" button.
     */
    public void onPickPhotoButtonClick(View view) {
        final Intent pickPhotoIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        final Intent chooserIntent = Intent.createChooser(pickPhotoIntent, getString(R.string.action_chooser_pick_photo));
        File photoFile = null;
        try {
            photoFile = createPhotoFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (photoFile != null) {
            final Intent takePhotoIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            takePhotoIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[] { takePhotoIntent });
        }
        startActivityForResult(chooserIntent, PICK_PHOTO_REQUEST);
    }

    private File createPhotoFile() throws IOException {
        final String filename = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        final File directory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        final File file = File.createTempFile(filename, ".jpg", directory);
        mPhotoFilePath = file.getAbsolutePath();
        return file;
    }

    private void setPhoto() {
        if (mPhotoUri != null) {
            mImageView.setImageURI(mPhotoUri);
        } else if (mPhotoPath != null) {
            final Bitmap bitmap = BitmapFactory.decodeFile(mPhotoPath);
            mImageView.setImageBitmap(bitmap);
        }
    }
}
