package com.example.photo2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

public class ShowImageActivity_biaochi extends Activity {

    public ImageView image;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_biaochi);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView() {
        image = findViewById(R.id.imagebiaochi);
        String base64 = null;
        base64 = BiaochiActivity.uploadUrlbiaochi;
        byte[] decodedString = Base64.decode(base64, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        image.setImageBitmap(decodedByte);

//        Glide.with(this).load(url).into(image);
    }
}

