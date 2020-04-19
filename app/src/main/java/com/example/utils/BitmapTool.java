package com.example.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaMetadataRetriever;
import android.util.Base64;

/**
 * 功能说明：Bitmap处理工具
 * 版权申明：浙江大华技术股份有限公司
 * 创建标记：30764 2018/1/23
 */

public class BitmapTool {

    /**
     * 获取视频帧画面
     * @param context
     * @param filepath 视频路径
     * @param value 图片像素大小
     * @return
     */
    public static Bitmap getCover(Context context, String filepath, int value) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(filepath);
        Bitmap bitmap = mmr.getFrameAtTime();
        mmr.release();
        return zoomImg(context, bitmap, value);
    }

    public static Bitmap getVideoCover(String filepath) {
        MediaMetadataRetriever mmr = new MediaMetadataRetriever();
        mmr.setDataSource(filepath);
        Bitmap bitmap = mmr.getFrameAtTime();
        mmr.release();
        return bitmap;
    }

    /**
     * 按照给定尺寸缩放Bitmap
     * @param context
     * @param bitmap 原bitmap
     * @param value 图片像素大小
     * @return
     */
    public static Bitmap zoomImg(Context context, Bitmap bitmap, int value) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float w = px2dip(context, (float) width);
        float h = px2dip(context, (float) height);
        if (w < value && h < value) {
            return bitmap;
        }
        float max = w > h ? w : h;
        float scale = (float) value / max;
        return Bitmap.createBitmap(bitmap, 0, 0, (int) (width * scale), (int) (height * scale));
    }

    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * Base64字符串解码得到Bitmap
     * @param Base64Str
     * @return
     */
    public static Bitmap getPicBase64Str(String Base64Str) {
        try {
            return getPicFromBytes(Base64.decode(Base64Str, Base64.NO_WRAP), null);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public Bitmap getPicBase64Str(String Base64Str, int flags) {
        try {
            return getPicFromBytes(Base64.decode(Base64Str, flags), null);
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap getPicFromBytes(byte[] bytes, BitmapFactory.Options opts) {
        if (bytes != null)
            if (opts != null) {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length,
                        opts);
            } else {
                return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            }
        return null;
    }
}
