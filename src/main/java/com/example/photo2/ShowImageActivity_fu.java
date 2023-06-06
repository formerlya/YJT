package com.example.photo2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.example.photo2.MeasureActivity_fu;
import com.example.photo2.R;

public class ShowImageActivity_fu extends Activity {

    public ImageView image;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_fu);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView() {
        image = findViewById(R.id.imagefu);
        String base64fu = null;
        base64fu = MeasureActivity_fu.uploadUrlfu;
        byte[] decodedStringfu = Base64.decode(base64fu, Base64.DEFAULT);
        Bitmap decodedBytefu = BitmapFactory.decodeByteArray(decodedStringfu, 0, decodedStringfu.length);
        image.setImageBitmap(decodedBytefu);

//        Glide.with(this).load(url).into(image);
    }
}

