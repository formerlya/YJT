package com.example.photo2;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentManager;


import com.example.photo2.MainActivity.*;

import java.time.format.DateTimeFormatter;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.math.MathContext;
import android.app.Application;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.Image;
import android.os.Build;
import android.os.Handler;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.PixelCopy;
import android.view.View.MeasureSpec;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.Canvas;

import com.example.photo2.take_photo.TakePhotoHelper;
import com.google.ar.core.*;
import com.google.ar.core.Anchor;
import com.google.ar.core.Frame;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.core.Point;
import com.google.ar.core.Pose;
import com.google.ar.core.exceptions.NotYetAvailableException;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.TimeUnit;

import android.net.Uri;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


public class MeasureActivity extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_measure);
//    }

    private static final String TAG = MeasureActivity.class.getSimpleName();
    private static final double MIN_OPENGL_VERSION = 3.0;
    private static View arview;
    private ArFragment arFragment;
    private ModelRenderable andyRenderable;
    private AnchorNode myanchornode;
    private DecimalFormat form_numbers = new DecimalFormat("#0.00 cm");

    private Anchor anchor1 = null, anchor2 = null,anchor3 = null,anchor4 = null;

    private HitResult myhit;

    long downTime = SystemClock.uptimeMillis();
    Bitmap mScreenBitmap = null;
    private TextView text;
    private Button btn_width;
    private View rectangleview;

    private Image img;

    // 记录刚刚上传的是哪个类型的图片
    private int uploadType;
    private String apiUrl = "";

    // 上传后 服务端返回的图片链接
    public static String uploadUrl = "";

    //记录当前日期，用于图片命名
    private LocalDate localDate;

    private ProgressDialog progressDialog;
//    private TextView tvType;

//    final MotionEvent downEvent1 = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN,100,1000, 0);
//    final MotionEvent upEvent1 = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_UP,100,1000, 0);
//    final MotionEvent downEvent2 = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN,800,1000, 0);
//    final MotionEvent upEvent2 = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_UP,800,1000, 0);

    List<AnchorNode> anchorNodes = new ArrayList<>();

    private ArrayList<String> arl_saved = new ArrayList<String>();

    private float fl_measurement = 0.0f;
    private float fl_measurement34 = 0.0f;
    private float measurement = 0.0f;

    private String message;
    private Object ArFragment;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (!checkIsSupportedDeviceOrFinish(this)) {
            return;
        }
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (NullPointerException e){}

        setContentView(R.layout.activity_measure);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        rectangleview = (View) findViewById(R.id.view);
        text = (TextView) findViewById(R.id.text);

        btn_width = (Button) findViewById(R.id.btn_width);
        progressDialog = new ProgressDialog(this);




        btn_width.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetLayout();
                text.setText("请将手机调整至水平或者竖直状态");

                WindowManager windowManager = getWindow().getWindowManager();
                Display display = windowManager.getDefaultDisplay();
                //屏幕可用宽度(像素个数)
                int width = display.getWidth();
                //屏幕可用高度(像素个数)
                int height = display.getHeight();

                float x = (width)*0.5f;
                float y = (height)*0.5f;


                ArSceneView arSceneView = arFragment.getArSceneView();
//设置4次点击
                MotionEvent downEvent1 = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN,x-200f,y-200f, 0);
                MotionEvent upEvent1 = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_UP,x-200f,y-200f, 0);
                MotionEvent downEvent2 = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN,x+200f,y-200f, 0);
                MotionEvent upEvent2 = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_UP,x+200f,y-200f, 0);
                MotionEvent downEvent3 = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN,x-200f,y+200f, 0);
                MotionEvent upEvent3 = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_UP,x-200f,y+200f, 0);
                MotionEvent downEvent4 = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN,x+200f,y+200f, 0);
                MotionEvent upEvent4 = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_UP,x+200f,y+200f, 0);

                arSceneView.dispatchTouchEvent(downEvent1);
                arSceneView.dispatchTouchEvent(upEvent1);
                arSceneView.dispatchTouchEvent(downEvent2);
                arSceneView.dispatchTouchEvent(upEvent2);
                arSceneView.dispatchTouchEvent(downEvent3);
                arSceneView.dispatchTouchEvent(upEvent3);
                arSceneView.dispatchTouchEvent(downEvent4);
                arSceneView.dispatchTouchEvent(upEvent4);
            }
        });



//构建3D锚点程序，后期将锚点隐藏了，故不可见
        ModelRenderable.builder()
                .setSource(this, R.raw.cubito3)
                .build()
                .thenAccept(renderable -> andyRenderable = renderable)
//                .thenAccept(modelRenderable -> ShapeFactory.makeSphere(0.02f,Vector3.zero(), andyRenderable.getMaterial()))
                .exceptionally(
                        throwable -> {
                            Toast toast =
                                    Toast.makeText(this, "Unable to load andy renderable", Toast.LENGTH_LONG);
                            toast.setGravity(Gravity.CENTER, 0, 0);
                            toast.show();
                            return null;
                        });
//AR平面监听（主要代码部分）



        arFragment.getArSceneView().getPlaneRenderer().getMaterial().thenAcceptBoth(
                arFragment.getArSceneView().getPlaneRenderer().getMaterial(),
                (uXyZ, texture) -> {
                    texture.setFloat4("u_Color", 0.0f, 0.0f, 0.0f, 0.0f);//设置为透明
                    texture.setFloat("u_blendMode", 1.0f);
                });
        arFragment.getArSceneView().getPlaneRenderer().setVisible(false);//隐藏平面的小白点





        arFragment.setOnTapArPlaneListener(
                (HitResult hitResult, Plane plane, MotionEvent motionEvent) -> {
                    if (andyRenderable == null) {
                        return;
                    }

                    myhit = hitResult;
                    // Create the Anchor.
                    Anchor anchor = hitResult.createAnchor();

                    AnchorNode anchorNode = new AnchorNode(anchor);


                    if (anchor1 == null) {
                        anchor1 = anchor;
                    }
                    else if(anchor2 == null){
                        anchor2 = anchor;
                    }
                    else if(anchor3 == null){
                        anchor3 = anchor;
                    }
                    else{
                        anchor4 = anchor;
                        fl_measurement = (getMetersBetweenAnchors(anchor1, anchor2))/4;
                        fl_measurement34 = (getMetersBetweenAnchors(anchor3, anchor4))/4;
                        measurement = ((fl_measurement + fl_measurement34) * 0.5f);
                        if(Math.abs((fl_measurement-fl_measurement34))<2f) {
                            text.setText("每100像素点代表的距离" +
                                    form_numbers.format(measurement));

                            //获取当前时刻的ArSceneView并保存至相册
                            ArSceneView arSceneView = arFragment.getArSceneView();
                            screenshot(arSceneView);
                            //将bitmap截图后保存
//                            WindowManager windowManager = getWindow().getWindowManager();
//                            Display display = windowManager.getDefaultDisplay();
                            saveImage(Float.toString(measurement),mScreenBitmap);
                            new TakePhotoHelper().selectPhoto(callback);
//                            Intent intent = new Intent(MeasureActivity.this,MainActivity.class);
//                intent.putExtra("url",uploadUrl);
//                            startActivityForResult(intent,1);
//                            int width = display.getWidth();
//                            int height = display.getHeight();
//
//                            float x = (width)*0.5f;
//                            float y = (height)*0.5f;

//                            MotionEvent downEvent1 = MotionEvent.obtain(downTime, downTime, MotionEvent.ACTION_DOWN,x-200f,y-200f, 0);
//                            Bundle bundle = new Bundle();
//                            bundle.putSerializable(mScreenBitmap, measurement);
                            //对当前保存图片进行功能选择
//                            Intent intent = new Intent(MeasureActivity.this,MainActivity.class);
//                            Intent intent = new Intent(MeasureActivity.this,MainActivity.class);
//                            startActivityForResult(intent,1);
//                            Intent intent1=getIntent();
//                            //intent1.putExtra("avatar", avatar);
//                            intent1.setAction(Intent.ACTION_CHOOSER);



                        }
                        else {
                            text.setText("请继续调整拍摄角度直至水平或垂直");
                        }
                    }


                    myanchornode = anchorNode;
                    anchorNodes.add(anchorNode);

                    // Create the transformable andy and add it to the anchor.
                    TransformableNode andy = new TransformableNode(arFragment.getTransformationSystem());
                    andy.setParent(anchorNode);
                    andy.setRenderable(andyRenderable);
                    andy.select();
                    andy.getScaleController().setEnabled(false);
                });
    }


    //Handler()机制很好！
    public void screenshot(ArSceneView view){
        //需要截取的长和宽
        int outWidth = view.getWidth();
        int outHeight = view.getHeight();

        mScreenBitmap = Bitmap.createBitmap(outWidth, outHeight,Bitmap.Config.ARGB_8888);
        //在图片保存时进行部分截图
        PixelCopy.request(view, mScreenBitmap, new PixelCopy.OnPixelCopyFinishedListener() {
            @Override
            public void onPixelCopyFinished(int copyResult){
                if (PixelCopy.SUCCESS == copyResult) {
//                    onSuccessCallback(mScreenBitmap);
                    Log.i("gyx","SUCCESS ");
                } else {
                    Log.i("gyx","FAILED");
                    // onErrorCallback()
                }
            }
        }, new Handler());



    }


    /**
     * 保存图片到本地
     *
     * @param name   图片的名字，比如传入“123”，最终保存的图片为“123.jpg”
     * @param bitmap    本地图片或者网络图片转成的Bitmap格式的文件
     * @return
     */
    public void saveImage(String name, Bitmap bitmap) {
        File pathFile = new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES + File.separator);
        if (!pathFile.exists()) {
            pathFile.mkdir();
        }
        File file = new File(pathFile, name + ".jpg");
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //对ArSceneView进行截图，分别为源bitmap文件，截图的起始点x,y坐标，截图的高度和宽度，得到最后的截图结果
            Bitmap bitmap1 = Bitmap.createBitmap(bitmap, bitmap.getWidth()/3, bitmap.getHeight()/4, bitmap.getWidth()/3, bitmap.getHeight()/2);
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            // 最后通知图库更新
            Toast.makeText(this, "保存成功",Toast.LENGTH_SHORT);
            Uri localUri = Uri.fromFile(file);
            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
            sendBroadcast(localIntent);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }







    /**
     * Function to return the distance in meters between two objects placed in ArPlane
     * @param anchor1 first object's anchor
     * @param anchor2 second object's anchor
     * @return the distance between the two anchors in cm
     */
    //两锚点间距离计算
    private float getMetersBetweenAnchors(Anchor anchor1, Anchor anchor2) {
        float[] distance_vector = anchor1.getPose().inverse()
                .compose(anchor2.getPose()).getTranslation();
        float totalDistanceSquared = 0;
        for (int i = 0; i < 3; ++i)
            totalDistanceSquared += distance_vector[i] * distance_vector[i];
        return (float) Math.sqrt(totalDistanceSquared)*100;
    }




    /**
     * Check whether the device supports the tools required to use the measurement tools
     * @param activity
     * @return boolean determining whether the device is supported or not
     */
    //检查设备是否支持
    private boolean checkIsSupportedDeviceOrFinish(final Activity activity) {
        if (Build.VERSION.SDK_INT < VERSION_CODES.N) {
            Log.e(TAG, "Sceneform requires Android N or later");
            Toast.makeText(activity, "Sceneform requires Android N or later", Toast.LENGTH_LONG).show();
            activity.finish();
            return false;
        }
        String openGlVersionString =
                ((ActivityManager) activity.getSystemService(Context.ACTIVITY_SERVICE))
                        .getDeviceConfigurationInfo()
                        .getGlEsVersion();
        if (Double.parseDouble(openGlVersionString) < MIN_OPENGL_VERSION) {
            Log.e(TAG, "Sceneform requires OpenGL ES 3.0 later");
            Toast.makeText(activity, "Sceneform requires OpenGL ES 3.0 or later", Toast.LENGTH_LONG)
                    .show();
            activity.finish();
            return false;
        }
        return true;
    }


    /**
     * Set layout to its initial state
     */
    private void resetLayout(){
        emptyAnchors();
    }

    //清除上一次设定的锚点
    private void emptyAnchors(){
        anchor1 = null;
        anchor2 = null;
        anchor3 = null;
        anchor4 = null;
        for (AnchorNode n : anchorNodes) {
            arFragment.getArSceneView().getScene().removeChild(n);
            n.getAnchor().detach();
            n.setParent(null);
            n = null;
        }
    }

    //创造图片与宽度信息的map表
//    public class KeyPhoto extends Context {
//        public void main(String[] args) {
//            Map <Bitmap,Float> map=new HashMap<>();
//            map.put(mScreenBitmap, measurement);
//        }

    /**
     * 自己实现Application，实现数据共享
     * @author jason
     */
//    public class MyPhoto extends Application {
//        // 共享变量
//        private MyHandler handler = null;
//
//        // set方法
//        public void setHandler(MyHandler handler) {
//            this.handler = handler;
//        }
//
//        // get方法
//        public MyHandler getHandler() {
//            return handler;
//        }
//
//    }


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
            return MeasureActivity.this.getSupportFragmentManager();
        }
    };


    void showTypeDialog(String photoPath) {
//        final String[] items = {"类型1", "类型2", "类型3", "类型4"};
        final String[] items = {"裂纹检测", "耳部检测", "毛刺检测", "鼓包检测", "扭耳检测","耳部v3"};
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setTitle("选择上传类型");
        alertBuilder.setItems(items, new DialogInterface.OnClickListener() {
            @RequiresApi(api = VERSION_CODES.O)
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                uploadFile(photoPath,i);
                Log.e("选择的类型是：", items[i]);
            }
        }).create().show();
    }
    @RequiresApi(api = VERSION_CODES.O)
    void uploadFile(String filePath, int type) {
        EditText editText = MainActivity.editText;
        String ip_Local = editText.getText().toString();
        // 定义接口链接
//        String apiUrl = "";
        if (type == 0){
            // 类型1的接口
//            apiUrl = "https://替换为接口1";
//            apiUrl = "http://172.20.10.2:5001/upload/0";
            apiUrl = "http://" + ip_Local +":5001/upload/0";
        }
        else if (type == 1){
            // 类型2的接口
            apiUrl = "http://" + ip_Local +":5001/upload/1";
        }
        else if (type == 2){
            // 类型3的接口
            apiUrl = "http://" + ip_Local +":5001/upload/2";
        }
        else if (type == 3){
            // 类型4的接口
            apiUrl = "http://" + ip_Local +":5001/upload/3";
        }
        else if (type == 4){
            // 类型5的接口
            apiUrl = "http://" + ip_Local +":5001/upload/4";
        }
        else{
            // 类型6的接口
            apiUrl = "http://" + ip_Local +":5001/upload/5";
        }
        progressDialog.show();
        // 创建一个OkHttpClient实例
        OkHttpClient client = new OkHttpClient().newBuilder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .build();
        // 创建一个图片文件
        File file = new File(filePath);
        LocalDate localDate = LocalDate.now(); // get the current date
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("_yyyyMMdd_");
        String date = localDate.format(formatter);




        // 创建一个RequestBody实例
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("image", "image.jpg",
                        RequestBody.create(MediaType.parse("image/jpeg"), file))
                .addFormDataPart("measurement",Float.toString(measurement))
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
                        Toast.makeText(MeasureActivity.this,"上传失败：" + e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override

            public void onResponse(Call call, Response response) throws IOException {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        uploadUrl = response.body().string();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            uploadType = type;
//                            tvType.setText("刚刚通过通道" + (type + 1)  + "上传了图片,点击下方按钮查看结果");
                            Toast.makeText(MeasureActivity.this, "上传成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MeasureActivity.this, "上传失败：" + response.message(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}

