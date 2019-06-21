package com.skj.wheel.util;


import android.content.Context;
import android.graphics.Bitmap;
import android.os.Looper;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.request.RequestOptions;
import com.skj.app.GlideApp;
import com.skj.wheel.R;
import com.skj.wheel.util.transformations.CropCircleTransformation;

/**
 * Created by 孙科技 on 2018/3/1.
 */

public class KGlideUtil {
    private static KGlideUtil instance;

    public static KGlideUtil getInstance() {
        if (instance == null) {
            synchronized (KGlideUtil.class) {
                if (instance == null) {
                    instance = new KGlideUtil();
                }
            }
        }
        return instance;
    }

    public void ImageLoader(Context context, ImageView imageView, Object url) {
        ImageLoader(context, imageView, url, R.mipmap.ic_launcher);
    }

    /**
     * 加载图片 设置自定义默认图片
     * <p>
     * CENTER /center  按图片的原来size居中显示，当图片长/宽超过View的长/宽，则截取图片的居中部分显示
     * CENTER_CROP / centerCrop  按比例扩大图片的size居中显示，使得图片长(宽)等于或大于View的长(宽)
     * CENTER_INSIDE / centerInside  将图片的内容完整居中显示，通过按比例缩小或原来的size使得图片长/宽等于或小于View的长/宽
     * FIT_CENTER / fitCenter  把图片按比例扩大/缩小到View的宽度，居中显示
     * FIT_END / fitEnd   把图片按比例扩大/缩小到View的宽度，显示在View的下部分位置
     * FIT_START / fitStart  把图片按比例扩大/缩小到View的宽度，显示在View的上部分位置
     * FIT_XY / fitXY  把图片不按比例扩大/缩小到View的大小显示
     * MATRIX / matrix 用矩阵来绘制
     *
     * @param imageView
     * @param url
     * @param defaultImg
     */
    public void ImageLoader(Context context, ImageView imageView, Object url, int defaultImg) {
        GlideApp.with(context).load(url)
                .error(defaultImg)
                .placeholder(defaultImg)
                .fallback(defaultImg)
                .into(imageView);
    }

    public void ImageLoader(Context context, ImageView imageView, Object url, int defaultImg, int width, int height) {
        GlideApp.with(context).load(url)
                .error(defaultImg)
                .placeholder(defaultImg)
                .fallback(defaultImg)
                .override(width, height)
                .into(imageView);
    }

    /**
     * 对图片转换
     *
     * @param imageView
     * @param url
     */
    public void ImageCircleLoader(Context context, ImageView imageView, Object url) {
        ImageCircleLoader(context, imageView, url, R.mipmap.ic_launcher, new CropCircleTransformation());
    }

    /**
     * 单个效果
     *
     * @param imageView
     * @param url
     * @param bitmapTransformation
     */
    public void ImageCircleLoader(Context context, ImageView imageView, Object url, int defaultImg,
                                  Transformation bitmapTransformation) {
        RequestBuilder<Bitmap> bitmapRequestBuilder = GlideApp.with(context)
                .asBitmap()//指定Bitmap类型的RequestBuilder
                .load(url)//网络URL
                .error(defaultImg)//异常图片
                .placeholder(defaultImg)//占位图片
                .fallback(defaultImg);//当url为空时，显示图片

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.transform(bitmapTransformation);
        //RequestBuilder<Bitmap> 中添加RequestOptions
        bitmapRequestBuilder.apply(requestOptions)
                .into(imageView);
    }

    public void ImageCircleLoader(Context context, ImageView imageView, Object url, int defaultImg,
                                  int width, int height, Transformation bitmapTransformation) {
        RequestBuilder<Bitmap> bitmapRequestBuilder = GlideApp.with(context)
                .asBitmap()//指定Bitmap类型的RequestBuilder
                .load(url)//网络URL
                .override(width, height)
                .error(defaultImg)//异常图片
                .placeholder(defaultImg)//占位图片
                .fallback(defaultImg);//当url为空时，显示图片

        RequestOptions requestOptions = new RequestOptions();
        requestOptions.transform(bitmapTransformation);
        //RequestBuilder<Bitmap> 中添加RequestOptions
        bitmapRequestBuilder.apply(requestOptions)
                .into(imageView);
    }

    /**
     * 多组合效果
     * <p>
     * MultiTransformation multiTransformation = new MultiTransformation(
     * new BlurTransformation(KDisplayUtil.dip2px(30)),
     * new CropCircleTransformation()
     * );
     * Crop
     * 默认：CropTransformation,
     * 圆形：CropCircleTransformation,
     * 方形：CropSquareTransformation,
     * 圆角：RoundedCornersTransformation
     * <p>
     * Color
     * 颜色覆盖：ColorFilterTransformation,
     * 置灰：GrayscaleTransformation
     * <p>
     * Blur
     * 毛玻璃：BlurTransformation
     * <p>
     * Mask
     * 还未弄明白：MaskTransformation
     *
     * @param imageView
     * @param url
     * @param multiTransformation
     */

    public void ImageCircleLoader(Context context, ImageView imageView, Object url, int defaultImg,
                                  MultiTransformation multiTransformation) {
        RequestBuilder<Bitmap> bitmapRequestBuilder = GlideApp.with(context)
                .asBitmap()//指定Bitmap类型的RequestBuilder
                .load(url)//网络URL
                .error(defaultImg)//异常图片
                .placeholder(defaultImg)//占位图片
                .fallback(defaultImg);//当url为空时，显示图片

        RequestOptions requestOptions = new RequestOptions();
        //在RequestOptions中使用Transformations
        requestOptions.transform(multiTransformation);
        //RequestBuilder<Bitmap> 中添加RequestOptions
        bitmapRequestBuilder.apply(requestOptions)
                .into(imageView);
    }

    public void ImageCircleLoader(Context context, ImageView imageView, Object url, int defaultImg,
                                  int width, int height, MultiTransformation multiTransformation) {
        RequestBuilder<Bitmap> bitmapRequestBuilder = GlideApp.with(context)
                .asBitmap()//指定Bitmap类型的RequestBuilder
                .load(url)//网络URL
                .override(width, height)
                .error(defaultImg)//异常图片
                .placeholder(defaultImg)//占位图片
                .fallback(defaultImg);//当url为空时，显示图片

        RequestOptions requestOptions = new RequestOptions();
        //在RequestOptions中使用Transformations
        requestOptions.transform(multiTransformation);
        //RequestBuilder<Bitmap> 中添加RequestOptions
        bitmapRequestBuilder.apply(requestOptions)
                .into(imageView);
    }

    /**
     * 清除图片磁盘缓存
     */
    public void clearImageDiskCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        Glide.get(context).clearDiskCache();
                    }
                }).start();
            } else {
                Glide.get(context).clearDiskCache();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 清除图片内存缓存
     */
    public void clearImageMemoryCache(final Context context) {
        try {
            if (Looper.myLooper() == Looper.getMainLooper()) { //只能在主线程执行
                Glide.get(context).clearMemory();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * 清除图片所有缓存
     */
    public void clearImageAllCache(Context context) {
        clearImageDiskCache(context);
        clearImageMemoryCache(context);
    }

}
