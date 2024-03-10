package com.example.collegeapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class ImageViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        ImageView imageview = findViewById(R.id.imageView);
        int imageResId = getIntent().getIntExtra("image", 0);
        if (imageResId != 0) {
            Glide.with(ImageViewActivity.this).load(imageResId).into(imageview);
        }



    }
}