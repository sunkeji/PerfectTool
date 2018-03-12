package com.skj.wheel.util;


import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.support.annotation.RequiresApi;
import android.widget.ImageView;

import com.bumptech.glide.RequestBuilder;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.Transformation;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.bumptech.glide.load.resource.bitmap.TransformationUtils;
import com.bumptech.glide.request.RequestOptions;
import com.skj.app.GlideApp;
import com.skj.app.MyApplication;
import com.skj.wheel.R;
import com.skj.wheel.util.transformations.CropCircleTransformation;

import java.security.MessageDigest;

/**
 * Created by 孙科技 on 2018/3/1.
 */

public class GlideUtil {
    private static GlideUtil instance;

    public static GlideUtil getInstance() {
        if (instance == null) {
            synchronized (GlideUtil.class) {
                if (instance == null) {
                    instance = new GlideUtil();
                }
            }
        }
        return instance;
    }

    public void ImageLoader(ImageView imageView, Object url) {
        ImageLoader(imageView, url, R.mipmap.ic_launcher);
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
    public void ImageLoader(ImageView imageView, Object url, int defaultImg) {
        GlideApp.with(MyApplication.mContext).load(url)
                .error(defaultImg)
                .placeholder(defaultImg)
                .fallback(defaultImg)
                .into(imageView);
    }

    public void ImageLoader(ImageView imageView, Object url, int defaultImg, int width, int height) {
        GlideApp.with(MyApplication.mContext).load(url)
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
    public void ImageCircleLoader(ImageView imageView, Object url) {
        ImageCircleLoader(imageView, url, R.mipmap.ic_launcher, new CropCircleTransformation());
    }

    /**
     * 单个效果
     *
     * @param imageView
     * @param url
     * @param bitmapTransformation
     */
    public void ImageCircleLoader(ImageView imageView, Object url, int defaultImg, Transformation bitmapTransformation) {
        RequestBuilder<Bitmap> bitmapRequestBuilder = GlideApp.with(MyApplication.mContext)
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

    public void ImageCircleLoader(ImageView imageView, Object url, int defaultImg, int width, int height, Transformation bitmapTransformation) {
        RequestBuilder<Bitmap> bitmapRequestBuilder = GlideApp.with(MyApplication.mContext)
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
     * new BlurTransformation(DisplayUtil.dip2px(30)),
     * new CropCircleTransformation()
     * );
     *
     * @param imageView
     * @param url
     * @param multiTransformation
     */
    public void ImageCircleLoader(ImageView imageView, Object url, int defaultImg, MultiTransformation multiTransformation) {
        RequestBuilder<Bitmap> bitmapRequestBuilder = GlideApp.with(MyApplication.mContext)
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

    public void ImageCircleLoader(ImageView imageView, Object url, int defaultImg, int width, int height, MultiTransformation multiTransformation) {
        RequestBuilder<Bitmap> bitmapRequestBuilder = GlideApp.with(MyApplication.mContext)
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
     */

    /**
     * 高斯毛玻璃特效
     */
    public static class BlurTransformation extends BitmapTransformation {
        public static final float DEFAULT_RADIUS = 25.0f;
        private RenderScript rs;
        private float mRadius;

        public BlurTransformation(Context context) {
            this(context, DEFAULT_RADIUS);


        }

        public BlurTransformation(Context context, float radius) {
            super(context);
            this.mRadius = radius;
            rs = RenderScript.create(context);
        }

        @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Bitmap blurredBitmap = toTransform.copy(Bitmap.Config.ARGB_8888, true);

            // Allocate memory for Renderscript to work with
            Allocation input = Allocation.createFromBitmap(rs, blurredBitmap, Allocation.MipmapControl.MIPMAP_FULL, Allocation.USAGE_SHARED);
            Allocation output = Allocation.createTyped(rs, input.getType());

            // Load up an instance of the specific script that we want to use.
            ScriptIntrinsicBlur script = ScriptIntrinsicBlur.create(rs, Element.U8_4(rs));
            script.setInput(input);

            // Set the blur radius
            script.setRadius(mRadius);

            // Start the ScriptIntrinisicBlur
            script.forEach(output);

            // Copy the output to the blurred bitmap
            output.copyTo(blurredBitmap);

            return blurredBitmap;
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            messageDigest.update("blur transformation".getBytes());
        }
    }

    /**
     * 图片转换圆角图片
     */
    public static class GlideRoundTransform extends BitmapTransformation {
        private float radius = 0f;

        public GlideRoundTransform(Context context, int dp) {
            super(context);
            this.radius = Resources.getSystem().getDisplayMetrics().density * dp;
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            Bitmap bitmap = TransformationUtils.centerCrop(pool, toTransform, outWidth, outHeight);
            return roundCrop(pool, bitmap);
        }

        private Bitmap roundCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            Bitmap result = pool.get(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(source.getWidth(), source.getHeight(), Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(source, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            RectF rectF = new RectF(0f, 0f, source.getWidth(), source.getHeight());
            canvas.drawRoundRect(rectF, radius, radius, paint);
            return result;
        }

        public String getId() {
            return getClass().getName() + Math.round(radius);
        }

        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            messageDigest.update("round transformation".getBytes());
        }
    }


    /**
     * 图片转圆形
     */
    public static class GlideCircleTransform extends BitmapTransformation {
        public GlideCircleTransform(Context context) {
            super(context);
        }

        @Override
        protected Bitmap transform(BitmapPool pool, Bitmap toTransform, int outWidth, int outHeight) {
            return circleCrop(pool, toTransform);
        }

        private Bitmap circleCrop(BitmapPool pool, Bitmap source) {
            if (source == null) return null;

            int size = Math.min(source.getWidth(), source.getHeight());
            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squared = Bitmap.createBitmap(source, x, y, size, size);

            Bitmap result = pool.get(size, size, Bitmap.Config.ARGB_8888);
            if (result == null) {
                result = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888);
            }

            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setShader(new BitmapShader(squared, BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP));
            paint.setAntiAlias(true);
            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);
            return result;
        }


        @Override
        public void updateDiskCacheKey(MessageDigest messageDigest) {
            messageDigest.update("circle transformation".getBytes());
        }
    }

}
