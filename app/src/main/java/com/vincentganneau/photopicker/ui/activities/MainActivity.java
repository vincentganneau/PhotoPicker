package com.vincentganneau.photopicker.ui.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.vincentganneau.photopicker.R;

/**
 * {@link AppCompatActivity} that enables the user to pick a photo.
 *
 * @author Vincent Ganneau
 *
 * Copyright (C) 2015
 */
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    // Methods
    /**
     * Called when the "Pick photo" button has been clicked.
     * @param view the "Pick photo" button.
     */
    public void onPickPhotoButtonClick(View view) {
        // TODO:
    }
}
