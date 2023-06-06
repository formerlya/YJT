package com.example.photo2.take_photo;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;

// 选择照片的插件
public class TakePhotoHelper {


    /**
     * 开始拍照
     * @param callback 回调
     */
    public void takePhoto(@NonNull TakePhotoCallback callback) {
        selectPhotoWithType(callback, TakePhotoEmptyFragment.REQ_TAKE_PHOTO);
    }

    /**
     * 开始选择图片
     * @param callback 回调
     */
    public void selectPhoto(@NonNull TakePhotoCallback callback) {
        selectPhotoWithType(callback, TakePhotoEmptyFragment.REQ_SELECT_IMAGE);
    }

    /**
     * 根据类型选择图片
     *
     * @param callback 回调
     * @param type 类型， {@link TakePhotoEmptyFragment#REQ_TAKE_PHOTO} 或者
     * {@link TakePhotoEmptyFragment#REQ_SELECT_IMAGE}
     */
    private void selectPhotoWithType(@NonNull TakePhotoCallback callback, int type) {
        TakePhotoEmptyFragment fragment =
                (TakePhotoEmptyFragment) callback.getSupportFragmentManager()
                        .findFragmentByTag(TakePhotoEmptyFragment.TAG);
        if (fragment != null) {
            //移除已经存在的fragment
            callback.getSupportFragmentManager().beginTransaction().remove(fragment).commit();
        }
        fragment = TakePhotoEmptyFragment.newInstance(type);
        fragment.setTakePhotoCallback(callback);
        callback.getSupportFragmentManager().beginTransaction().add(fragment,
                TakePhotoEmptyFragment.TAG).commit();
    }


    /**
     * 选择图片回调
     */
    public interface TakePhotoCallback {

        /**
         * 拍照成功
         * @param photoPath 图片地址
         * @param thumbImage 缩略图
         */
        void onTakePhotoSucceed(@NonNull String photoPath, @Nullable Bitmap thumbImage);

        /**
         * 拍照失败
         * @param msg
         */
        void onTakePhotoFailed(@NonNull String msg);

        /**
         * 拍照Activity
         */
        @NonNull
        FragmentManager getSupportFragmentManager();
    }
}
