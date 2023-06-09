package com.example.photo2;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.os.Build.VERSION_CODES;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.PixelCopy;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import com.example.photo2.take_photo.TakePhotoHelper;
import com.google.ar.core.Anchor;
import com.google.ar.core.HitResult;
import com.google.ar.core.Plane;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DecimalFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;


@RequiresApi(api = VERSION_CODES.O)
public class MeasureActivity_fu extends AppCompatActivity {

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_measure);
//    }

    private static final String TAG = MeasureActivity_fu.class.getSimpleName();
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
    public static String uploadUrlfu = "";

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

    LocalDate localDate = LocalDate.now(); // get the current date
    //        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("_yyyyMMdd_");
    String date = localDate.format(formatter);
    






    @RequiresApi(api = VERSION_CODES.O)
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

        setContentView(R.layout.activity_measure_fu);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.ux_fragment);
        rectangleview = (View) findViewById(R.id.viewfu);
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
                        if(Math.abs((fl_measurement-fl_measurement34))<0.1f) {
                            text.setText("每100像素点代表的距离" +
                                    form_numbers.format(measurement));
                            //获取当前时刻的ArSceneView并保存至相册
                            ArSceneView arSceneView = arFragment.getArSceneView();
                            screenshot(arSceneView);
                            //将bitmap截图后保存并获取本地图片路径
                            Uri photoPath = saveImage("fu_"+date+"_"+measurement, mScreenBitmap);
                            String Path = String.valueOf(photoPath);
                            String SubPath = Path.substring(7);//取出正确的photopath
//                            new TakePhotoHelper().selectPhoto(callback);//使用该方法要在拍照后进行相册图片选择
                            Bitmap bmp = BitmapFactory.decodeFile(SubPath);
                            //根据本地图片位置，将图片上传至服务器
                            showTypeDialog(SubPath);
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
    public Uri saveImage(String name, Bitmap bitmap) {
        File pathFile = new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_PICTURES + File.separator);
        if (!pathFile.exists()) {
            pathFile.mkdir();
        }
        File file = new File(pathFile, name + ".jpg");
        Uri localUri = Uri.fromFile(file);
        try {
            FileOutputStream fos = new FileOutputStream(file);
            //对ArSceneView进行截图，分别为源bitmap文件，截图的起始点x,y坐标，截图的高度和宽度，得到最后的截图结果
            Bitmap bitmap1 = Bitmap.createBitmap(bitmap, bitmap.getWidth()/2-160, bitmap.getHeight()/2-800, 320, 1600);
            bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fos);
            fos.flush();
            fos.close();
            // 最后通知图库更新
            Toast.makeText(this, "保存成功",Toast.LENGTH_SHORT);
//            Uri localUri = Uri.fromFile(file);
            Intent localIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri);
            sendBroadcast(localIntent);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return localUri;
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

    //选择拍照或选择照片的回调
    TakePhotoHelper.TakePhotoCallback callback = new TakePhotoHelper.TakePhotoCallback() {
        @Override
        public void onTakePhotoSucceed(@NonNull String photoPath, @Nullable Bitmap thumbImage) {
            // 拿到所选择的照片 显示到界面上 然后弹出选择类型弹窗
            Bitmap bmp = BitmapFactory.decodeFile(photoPath);
            showTypeDialog(photoPath);
        }

        @Override
        public void onTakePhotoFailed(@NonNull String msg) {
        }

        @NonNull
        @Override
        public FragmentManager getSupportFragmentManager() {
            return MeasureActivity_fu.this.getSupportFragmentManager();
        }
    };

// 俯视
    void showTypeDialog(String photoPath) {
        final String[] items = {"耳部厚度", "耳部歪斜", "鼓包检测","耳部裂纹"};
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
        EditText editText = MainActivity2.editText;
        String ip_Local = editText.getText().toString();
        // 定义接口链接
//        String apiUrl = "";
        if (type == 0){
            // 类型1的接口
//            apiUrl = "https://替换为接口1";
//            apiUrl = "http://172.20.10.2:5001/upload/0";
            apiUrl = "http://" + ip_Local +":5001/upload/fu00";
        }
        else if (type == 1){
            // 类型2的接口
            apiUrl = "http://" + ip_Local +":5001/upload/fu01";
        }
        else if (type == 2){
            // 类型3的接口
            apiUrl = "http://" + ip_Local +":5001/upload/fu02";
        }
        else{
            // 类型4的接口
            apiUrl = "http://" + ip_Local +":5001/upload/fu03";
        }
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
                        Toast.makeText(MeasureActivity_fu.this,"上传失败：" + e.getLocalizedMessage(),Toast.LENGTH_LONG).show();
                    }
                });
            }

            @Override

            public void onResponse(Call call, Response response) throws IOException {
                progressDialog.dismiss();
                try {
                    if (response.isSuccessful()) {
                        uploadUrlfu = response.body().string();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (response.isSuccessful()) {
                            uploadType = type;
                            MainActivity2.tvType.setText("点击下方按钮查看结果");
                            Toast.makeText(MeasureActivity_fu.this, "上传成功", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(MeasureActivity_fu.this, "上传失败：" + response.message(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}

