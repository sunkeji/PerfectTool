package com.skj.wheel.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Transformation;

import java.io.File;

public class KPicassoUtil {
    public static KPicassoUtil KPicassoUtil;
    public Context mContext;

    public KPicassoUtil(Context mContext) {
        this.mContext = mContext;
    }

    public static KPicassoUtil getInstance(Context mContext) {
        if (KPicassoUtil == null) {
            synchronized (KPicassoUtil.class) {
                if (KPicassoUtil == null) {
                    KPicassoUtil = new KPicassoUtil(mContext);
                }
            }
        }
        return KPicassoUtil;
    }

    public void ImageLoader(ImageView imageView, String url, int defaultImg) {
        Picasso.get()
                .load(url)
                .placeholder(defaultImg)
                .error(defaultImg)
                .transform(new CircleTransform())
                .into(imageView);
    }

    public void ImageLoader(ImageView imageView, File url, int defaultImg) {
        Picasso.get()
                .load(url)
                .placeholder(defaultImg)
                .error(defaultImg)
                .transform(new CircleTransform())
                .into(imageView);
    }

    public class CircleTransform implements Transformation {
        @Override
        public Bitmap transform(Bitmap source) {

            int size = Math.min(source.getWidth(), source.getHeight());

            int x = (source.getWidth() - size) / 2;
            int y = (source.getHeight() - size) / 2;

            Bitmap squaredBitmap = Bitmap.createBitmap(source, x, y, size, size);
            if (squaredBitmap != source) {
                source.recycle();
            }

            Bitmap bitmap = Bitmap.createBitmap(size, size, source.getConfig());

            Canvas canvas = new Canvas(bitmap);
            Paint paint = new Paint();
            BitmapShader shader = new BitmapShader(squaredBitmap,
                    BitmapShader.TileMode.CLAMP, BitmapShader.TileMode.CLAMP);
            paint.setShader(shader);
            paint.setAntiAlias(true);

            float r = size / 2f;
            canvas.drawCircle(r, r, r, paint);

            squaredBitmap.recycle();
            return bitmap;
        }

        @Override
        public String key() {
            return "circle";
        }

    }
}
