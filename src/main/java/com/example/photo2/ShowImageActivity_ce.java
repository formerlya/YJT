package com.example.photo2;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;

import com.example.photo2.MeasureActivity_ce;
import com.example.photo2.R;

public class ShowImageActivity_ce extends Activity {

    public ImageView image;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image_ce);
        initView();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void initView() {
        image = findViewById(R.id.imagece);
        String base64ce = null;

        base64ce = MeasureActivity_ce.uploadUrlce;

        byte[] decodedStringce = Base64.decode(base64ce, Base64.DEFAULT);
        Bitmap decodedBytece = BitmapFactory.decodeByteArray(decodedStringce, 0, decodedStringce.length);
        image.setImageBitmap(decodedBytece);

//        Glide.with(this).load(url).into(image);
    }
}

