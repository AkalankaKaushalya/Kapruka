package com.kapruka;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jsibbold.zoomage.ZoomageView;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class SingelImageViewActivity extends AppCompatActivity {
    ZoomageView ImageZoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_singel_image_view);
        ImageZoom = findViewById(R.id.myZoomageView);
        Intent intent = getIntent();
        String imageurl = intent.getStringExtra("ImageUrl");

        Picasso.get().load(imageurl).into(ImageZoom, new Callback() {
            @Override
            public void onSuccess() {
            }

            @Override
            public void onError(Exception e) {
            }
        });
    }
}