package com.example.photo2;

import android.app.ProgressDialog;
//import android.net.IpConfiguration;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ImageView;
import android.content.Intent;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;

import com.example.photo2.take_photo.TakePhotoHelper;
import com.example.photo2.take_photo.TakePhotoEmptyFragment;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


@RequiresApi(api = Build.VERSION_CODES.O)
public class MainActivity extends FragmentActivity {


    public static ImageView imgPicture;
    private Button btnTakePhotobiaochi;
    private Button btnTakePhotozu;
    private Button btnTakePhotofu;
    private Button btnTakePhotoce;
    private Button btnShowImagezu;
    private Button btnShowImagefu;
    private Button btnShowImagece;
    private Button btnShowImagebiaochi;
//    private Button btnSelectPhoto;
//    public ImageView imgPicture;

    private ProgressDialog progressDialog;

    // 上传后 服务端返回的图片链接
    public static String uploadUrl = "";
    public static String uploadUrlbiaochi = "";

    // 记录刚刚上传的是哪个类型的图片
    private int uploadType;
    public static EditText editText;
    public static TextView tvType;
    private Button btnShowImage;
    public String apiUrl = "";
    public static String ip_Local = "";

    LocalDate localDate = LocalDate.now(); // get the current date
    //        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("_yyyyMMdd_");
    String date = localDate.format(formatter);


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

    private void initView() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("请稍候...");
        btnTakePhotozu = findViewById(R.id.btn_take_photo_zu);
        btnTakePhotofu = findViewById(R.id.btn_take_photo_fu);
        btnTakePhotoce = findViewById(R.id.btn_take_photo_ce);
        btnTakePhotobiaochi = findViewById(R.id.btn_take_photo_biaochi);
        imgPicture = findViewById(R.id.img_picture);



        tvType = findViewById(R.id.tv_type);
        btnShowImagezu = findViewById(R.id.btn_show_image_zu);
        btnShowImagefu = findViewById(R.id.btn_show_image_fu);
        btnShowImagece = findViewById(R.id.btn_show_image_ce);
        btnShowImagebiaochi = findViewById(R.id.btn_show_image_biaochi);
        editText = findViewById(R.id.edit_text);
        ip_Local = editText.getText().toString();


        btnTakePhotobiaochi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new TakePhotoHelper().takePhoto(callback);
            }
        });

        btnTakePhotozu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入拍照+距离测量
                Intent intent = new Intent(MainActivity.this, MeasureActivity_zu.class);
                startActivity(intent);
            }
        });

        btnTakePhotofu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入拍照+距离测量
                Intent intent = new Intent(MainActivity.this, MeasureActivity_fu.class);
                startActivity(intent);
            }
        });

        btnTakePhotoce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //进入拍照+距离测量
                Intent intent = new Intent(MainActivity.this, MeasureActivity_ce.class);
                startActivity(intent);
            }
        });

//        btnSelectPhoto.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                // 点击选择相册
//                new TakePhotoHelper().selectPhoto(callback);
//            }
//        });

        btnShowImagezu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /// 点击查看图片
                Intent intent = new Intent(MainActivity.this, ShowImageActivity_zu.class);
//                intent.putExtra("url",uploadUrl);
                startActivity(intent);
            }
        });

        btnShowImagefu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /// 点击查看图片
                Intent intent = new Intent(MainActivity.this, ShowImageActivity_fu.class);
//                intent.putExtra("url",uploadUrl);
                startActivity(intent);
            }
        });

        btnShowImagece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /// 点击查看图片
                Intent intent = new Intent(MainActivity.this, ShowImageActivity_ce.class);
//                intent.putExtra("url",uploadUrl);
                startActivity(intent);
            }
        });

        btnShowImagebiaochi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /// 点击查看图片
                Intent intent = new Intent(MainActivity.this, ShowImageActivity_biaochi.class);
//                intent.putExtra("url",uploadUrl);
                startActivity(intent);
            }
        });


    }

    //选择拍照或选择照片的回调
    TakePhotoHelper.TakePhotoCallback callback = new TakePhotoHelper.TakePhotoCallback() {
        @Override
        public void onTakePhotoSucceed(@NonNull String photoPath, @Nullable Bitmap thumbImage) {
            // 拿到所选择的照片 显示到界面上 然后弹出选择类型弹窗
            Bitmap bmp = BitmapFactory.decodeFile(photoPath);
            MainActivity.imgPicture.setImageBitmap(bmp);
            showTypeDialog(photoPath);
        }

        @Override
        public void onTakePhotoFailed(@NonNull String msg) {
        }

        @NonNull
        @Override
        public FragmentManager getSupportFragmentManager() {
            return MainActivity.this.getSupportFragmentManager();
        }
    };

    // 侧视
    void showTypeDialog(String photoPath) {
        final String[] items = {"耳厚测量", "顶凹", "裂纹", "板筋", "底部瘤", "凸起", "内外耳宽", "面部长宽","侧面板宽","飞边荡边"};
//        final String[] items = {"耳厚测量","顶凹","裂纹","板筋","底部瘤","凸起","内外耳宽","面部长宽"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择上传类型");
        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                uploadFile(photoPath,i);
                Log.e("选择的类型是：", items[i]);
            }
        }).create().show();
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    void uploadFile(String filePath, int type) {
        EditText editText = MainActivity.editText;
        String ip_Local = editText.getText().toString();
        // 定义接口链接
//        String apiUrl = "";
        if (type == 0){
            // 类型1的接口
//            apiUrl = "https://替换为接口1";
//            apiUrl = "http://172.20.10.2:5001/upload/0";
            apiUrl = "http://" + ip_Local +":5001/upload/bc00";
        }
        else if (type == 1){
            // 类型1的接口
//            apiUrl = "https://替换为接口1";
//            apiUrl = "http://172.20.10.2:5001/upload/0";
            apiUrl = "http://" + ip_Local +":5001/upload/bc01";
        }
        else if (type == 2){
            // 类型1的接口
//            apiUrl = "https://替换为接口1";
//            apiUrl = "http://172.20.10.2:5001/upload/0";
            apiUrl = "http://" + ip_Local +":5001/upload/bc02";
        }
        else if (type == 3){
            // 类型1的接口
//            apiUrl = "https://替换为接口1";
//            apiUrl = "http://172.20.10.2:5001/upload/0";
            apiUrl = "http://" + ip_Local +":5001/upload/bc03";
        }
        else if (type == 4){
            // 类型1的接口
//            apiUrl = "https://替换为接口1";
//            apiUrl = "http://172.20.10.2:5001/upload/0";
            apiUrl = "http://" + ip_Local +":5001/upload/bc04";
        }
        else if (type == 5){
            // 类型1的接口
//            apiUrl = "https://替换为接口1";
//            apiUrl = "http://172.20.10.2:5001/upload/0";
            apiUrl = "http://" + ip_Local +":5001/upload/bc05";
        }
        else if (type == 6){
            // 类型1的接口
//            apiUrl = "https://替换为接口1";
//            apiUrl = "http://172.20.10.2:5001/upload/0";
            apiUrl = "http://" + ip_Local +":5001/upload/bc06";
        }
        else if (type == 7){
            // 类型1的接口
//            apiUrl = "https://替换为接口1";
//            apiUrl = "http://172.20.10.2:5001/upload/0";
            apiUrl = "http://" + ip_Local +":5001/upload/bc07";
        }
        else if (type == 8){
            apiUrl = "http://" + ip_Local +":5001/upload/bc08";
        }
        else if (type == 9){
            apiUrl = "http://" + ip_Local +":5001/upload/bc09";
        }

	
        //进入侧视图接口
        progressDialog.show();
        // 创建一个OkHttpClient实例
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        // 创建一个图片文件
        File file = new File(filePath);

        // 创建一个RequestBody实例
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image.jpg",
                        RequestBody.create(MediaType.parse("image/jpeg"), file))
                .addFormDataPart("date",date)
                .build();
        // 创建一个Request实例
        Request request = new Request.Builder()
                .url(apiUrl)
                .post(requestBody)
                .build();

        // 使用client发送请求
        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressDialog.dismiss();
                        Toast.makeText(MainActivity.this,"上传失败：" + e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override

            public void onResponse(Call call, Response response) throws IOException {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        uploadUrlbiaochi = response.body().string();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            uploadType = type;
                            MainActivity.tvType.setText("点击下方按钮查看结果");
                            Toast.makeText(MainActivity.this, "上传成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MainActivity.this, "上传失败：" + response.message(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }

}
