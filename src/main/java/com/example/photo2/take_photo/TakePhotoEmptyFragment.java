package com.example.photo2.take_photo;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;

import com.example.photo2.BuildConfig;

import java.io.File;

// 选择照片的插件
public class TakePhotoEmptyFragment extends Fragment {
    public static final String TAG = TakePhotoEmptyFragment.class.getSimpleName();

    /**
     * 请求拍照
     */
    public static final int REQ_TAKE_PHOTO = 2;

    /**
     * 请求选择图片
     */
    public static final int REQ_SELECT_IMAGE = 3;

    /**
     * 传入参数，请求类型，拍照或者选择图片
     */
    private static final String ARG_TYPE = "arg_type";

    /**
     * 请求申请权限
     */
    private final int REQ_PERMISSION = 1;

    /**
     * 当前拍照图片地址
     */
    private String mCurrentPath;

    /**
     * 请求拍照还是请求选择照片
     */
    private int mReqType;

    private TakePhotoHelper.TakePhotoCallback mTakePhotoCallback;

    /**
     * 获取实例
     *
     * @param req 拍照 {@link TakePhotoEmptyFragment#REQ_TAKE_PHOTO} 或者
     * {@link TakePhotoEmptyFragment#REQ_SELECT_IMAGE}
     * @return {@link TakePhotoEmptyFragment}
     */
    public static TakePhotoEmptyFragment newInstance(int req) {

        Bundle args = new Bundle();
        args.putInt(ARG_TYPE, req);
        TakePhotoEmptyFragment fragment = new TakePhotoEmptyFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //禁止缓存实例
        setReenterTransition(false);
    }

    @Override
    public void onAttach(Context context) {
        mReqType = getArguments().getInt(ARG_TYPE);
        super.onAttach(context);
        if (REQ_TAKE_PHOTO == mReqType) {
            checkPermissionThanTakePhoto(Manifest.permission.CAMERA);
        } else if (REQ_SELECT_IMAGE == mReqType) {
            //请求获取读写sd卡权限 （对于同一个权限组的权限，当获取一个，系统会自动授予其他权限）
            checkPermissionThanTakePhoto(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        } else {
            throw new IllegalStateException("传入未知类型操作：" + mReqType);
        }
    }

    /**
     * 设置拍照回调
     * @param takePhotoCallback 拍照回调
     */
    public void setTakePhotoCallback(TakePhotoHelper.TakePhotoCallback takePhotoCallback) {
        mTakePhotoCallback = takePhotoCallback;
    }


    /**
     * 检查权限然后拍照
     */
    public void checkPermissionThanTakePhoto(String permission) {
        Activity act = getActivity();
        if (act == null) {
            if (mTakePhotoCallback != null) {
                mTakePhotoCallback.onTakePhotoFailed("上下文为空");
            }
            return;
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // android 6.0 判断是否有权限
            if (ContextCompat.checkSelfPermission(getActivity(), permission) !=
                    PackageManager.PERMISSION_GRANTED) {
                //权限没有开通

                //请求权限
                requestPermissions(new String[]{permission}, REQ_PERMISSION);
            } else {
                //权限已经开通，直接开始拍照
                doWork();
            }
        } else {
            // 低于Android 6.0 不需要申请权限，直接拍照
            doWork();
        }
    }

    /**
     * 开始拍照
     */
    private void doWork() {
        if (mReqType == REQ_TAKE_PHOTO) {
            takePhoto();
        } else if (mReqType == REQ_SELECT_IMAGE) {
            selectPhoto();
        }
    }

    /**
     * 选择图片
     */
    private void selectPhoto() {
        Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(pickPhoto , REQ_SELECT_IMAGE);
    }


    /**
     * 拍照
     */
    private void takePhoto() {
        Activity act = getActivity();
        if (act == null) {
            return;
        }
        Intent it = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (it.resolveActivity(act.getPackageManager()) == null) {
            if (mTakePhotoCallback != null) {
                mTakePhotoCallback.onTakePhotoFailed("没有可用于拍照的应用");
            }
            return;
        }

        //生成一个地址用于缓存图片
        String fileSavePath = act.getCacheDir() + File.separator + "tmpimg";
        File fileSaveDir = new File(fileSavePath);
        if (!fileSaveDir.exists()) {
            boolean createSucceed = fileSaveDir.mkdirs();
            Log.v(TAG, "创建图片缓存文件夹：" + createSucceed);
            if (!createSucceed) {
                if (mTakePhotoCallback != null) {
                    mTakePhotoCallback.onTakePhotoFailed("无法创建缓存图片目录");
                }
                return;
            }
        }
        //保存图片文件地址，需要定时清理图片
        File saveFile = new File(fileSaveDir, String.valueOf(System.currentTimeMillis()));
        mCurrentPath = saveFile.getAbsolutePath();
        Log.v(TAG, "图片缓存文件：" + saveFile);

        // file provider xml 中添加 ！！！

        /*
        <cache-path
            name="data"
            path="." />
        * */

        Uri uri = FileProvider.getUriForFile(act,
                BuildConfig.APPLICATION_ID + ".provider", saveFile);
        it.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        startActivityForResult(it, REQ_TAKE_PHOTO);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull
            int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQ_PERMISSION) {
            //请求权限返回
            if (grantResults.length <= 0) {
                if (mTakePhotoCallback != null) {
                    mTakePhotoCallback.onTakePhotoFailed("请求权限出错");
                }
                return;
            }
            int result = grantResults[0];
            if (PackageManager.PERMISSION_GRANTED != result) {
                if (mTakePhotoCallback != null) {
                    mTakePhotoCallback.onTakePhotoFailed("无权限");
                }
                return;
            }
            //有权限
            doWork();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == REQ_TAKE_PHOTO) {
            Bitmap bitmap = null;
            //拍照
            String path = mCurrentPath;
            try {
                //不同厂家手机获取到返回值不同
                if (data != null) {
                    Bundle extras = data.getExtras();
                    //缩略图
                    if (extras != null) {
                        bitmap = (Bitmap) extras.get("data");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();

            }
            if (mTakePhotoCallback != null) {
                mTakePhotoCallback.onTakePhotoSucceed(path, bitmap);
            }
        } else if (requestCode == REQ_SELECT_IMAGE) {
            //选择图片
            Uri selectedImage = data.getData();
            if (mTakePhotoCallback != null) {
                //没有缩略图
                String path = convertMediaUriToPath(selectedImage);
                mTakePhotoCallback.onTakePhotoSucceed(path, null);
            }
        }
    }

    /**
     * 将uri 转化为文件路径
     *
     * @param uri uri
     * @return 文件路径
     */
    private String convertMediaUriToPath(Uri uri) {
        String path = null;
        String [] projection = { MediaStore.Images.Media.DATA };
        Cursor cursor = getActivity().getContentResolver()
                .query(uri, projection,  null, null, null);
        if (cursor != null) {
            int index = cursor.getColumnIndex(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            path = cursor.getString(index);
            cursor.close();
        }

        return path;
    }


}
