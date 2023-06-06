package com.example.photo2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

public class ShowImageActivity_zu extends Activity {

    private ImageView image;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_zu);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView() {
        image = findViewById(R.id.imagezu);
        String base64zu = null;
        base64zu = MeasureActivity_zu.uploadUrlzu;
        byte[] decodedStringzu = Base64.decode(base64zu, Base64.DEFAULT);
        Bitmap decodedBytezu = BitmapFactory.decodeByteArray(decodedStringzu, 0, decodedStringzu.length);
        image.setImageBitmap(decodedBytezu);

//        Glide.with(this).load(url).into(image);
    }
}

