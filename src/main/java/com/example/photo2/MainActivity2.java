package com.example.photo2;

import android.app.ProgressDialog;
//import android.net.IpConfiguration;

import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;

@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity2 extends FragmentActivity {


    private Button btnBiaochi;
    private Button btnArcore;

    private ProgressDialog progressDialog;
    public static String uploadUrlbiaochi = "";

    public static EditText editText;
    public static TextView tvType;

    public static String ip_Local = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        initView();
    }

    private void initView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("请稍候...");

        tvType = findViewById(R.id.tv_type);
        btnBiaochi = findViewById(R.id.btn_biaochi);
        btnArcore = findViewById(R.id.btn_arcore);

        editText = findViewById(R.id.edit_text);
        ip_Local = editText.getText().toString();


        btnBiaochi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, BiaochiActivity.class);
                startActivity(intent);

            }
        });

        btnArcore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity2.this, ArcoreActivity.class);
                startActivity(intent);
            }
        });

    }

}
