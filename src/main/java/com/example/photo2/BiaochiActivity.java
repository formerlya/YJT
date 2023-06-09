package com.example.photo2;

import static com.example.photo2.MainActivity2.ip_Local;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.wildma.idcardcamera.camera.IDCardCamera;
import com.wildma.idcardcamera.utils.FileUtils;

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

public class BiaochiActivity extends AppCompatActivity {


    private int uploadType;
    public String apiUrl = "";
    public static String uploadUrlbiaochi = "";
    private ProgressDialog progressDialog;
    private String path = "";
    LocalDate localDate = LocalDate.now(); // get the current date
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("_yyyyMMdd_");
    String date = localDate.format(formatter);

    private ImageView m_banjin;

    private Button btnbanjin;
//    private Button btnDingtu;
//    private Button btnGubao;
//    private Button btnDibuliu;
//    private Button btnFeibian;
//    private Button btnDangbian;
//    private Button btnEar;
//    private Button btnThick;
//    private Button btnFace;
    private Button btnBiaochiResult;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ip_Local = MainActivity2.editText.getText().toString();
        setContentView(R.layout.activity_biaochi);
        m_banjin = (ImageView) findViewById(R.id.banjin);
        btnBiaochiResult = findViewById(R.id.btn_biaochiresult);

        if(ip_Local.length() != 0){
            Toast.makeText(BiaochiActivity.this, "已获取服务器ip地址", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(BiaochiActivity.this, "请返回至主界面填写服务器地址", Toast.LENGTH_LONG).show();
        }



//        btnBiaochiResult.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                /// 点击查看图片
//                Intent intent = new Intent(BiaochiActivity.this, ShowImageActivity_biaochi.class);
////                intent.putExtra("url",uploadUrl);
//                startActivity(intent);
//            }
//        });
    }

    public void click_banjin(View view) {

        IDCardCamera.create(this).openCamera(IDCardCamera.TYPE_IDCARD_BANJIN);
        FileUtils.clearCache(this);

    }





    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == IDCardCamera.RESULT_CODE) {
            //获取图片路径，显示图片
            path = IDCardCamera.getImagePath(data);
            if (!TextUtils.isEmpty(path)) {
                if (requestCode == IDCardCamera.TYPE_IDCARD_BANJIN) { //身份证正面
                    uploadType = 2;
                    m_banjin.setImageBitmap(BitmapFactory.decodeFile(path));
                    uploadFile(path,uploadType);



                } else if (requestCode == IDCardCamera.TYPE_IDCARD_BACK) {  //身份证反面
//                    m_banjin.setImageBitmap(BitmapFactory.decodeFile(path));
                }

            }

        }



    }



    void uploadFile(String filePath, int type) {
        EditText editText = MainActivity2.editText;
        String ip_Local = MainActivity2.editText.getText().toString();

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
                        Toast.makeText(BiaochiActivity.this,"上传失败：" + e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
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
                            Toast.makeText(BiaochiActivity.this, "上传成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(BiaochiActivity.this, "上传失败：" + response.message(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
//    private void initView(){
//
//        Button btnBanjin = findViewById(R.id.btn_banjin);
//        Button btnDingtu = findViewById(R.id.btn_dingtu);
//        Button btnGubao = findViewById(R.id.btn_gubao);
//        Button btnDibuliu = findViewById(R.id.btn_dibuliu);
//        Button btnLiewen = findViewById(R.id.btn_liewen);
//        Button btnFeibian = findViewById(R.id.btn_feibian);
//        Button btnDangbian = findViewById(R.id.btn_dangbian);
//        Button btnEar = findViewById(R.id.btn_ear);
//        Button btnThick = findViewById(R.id.btn_thick);
//        Button btnFace = findViewById(R.id.btn_face);
//        Button btnBiaochiResult = findViewById(R.id.btn_biaochiresult);
//
//
//        btnBanjin.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //进入拍照+距离测量
//                Intent intent = new Intent(BiaochiActivity.this, MeasureActivity_zu.class);
//                startActivity(intent);
//            }
//        });
//    }







}