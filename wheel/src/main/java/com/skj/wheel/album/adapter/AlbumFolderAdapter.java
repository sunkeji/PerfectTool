package com.skj.wheel.album.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.skj.wheel.R;
import com.skj.wheel.album.activity.AlbumAllActivity;
import com.skj.wheel.album.utils.BitmapCache;
import com.skj.wheel.album.utils.ImageItem;
import com.skj.wheel.util.LogUtil;


/**
 * 这个是显示所有包含图片的文件夹的适配器
 */
public class AlbumFolderAdapter extends BaseAdapter {

    private BitmapCache cache;

    public AlbumFolderAdapter(Context c) {
        cache = new BitmapCache();
    }

    @Override
    public int getCount() {
        return AlbumAllActivity.contentList == null ? 0 : AlbumAllActivity.contentList.size();
    }

    @Override
    public Object getItem(int position) {
        return AlbumAllActivity.contentList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    private BitmapCache.ImageCallback callback = new BitmapCache.ImageCallback() {
        @Override
        public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params) {
            if (imageView != null && bitmap != null) {
                String url = (String) params[0];
                if (url != null && url.equals((String) imageView.getTag())) {
                    ((ImageView) imageView).setImageBitmap(bitmap);
                } else {
                    LogUtil.e("callback, bmp not match");
                }
            } else {
                LogUtil.e("callback, bmp null");
            }
        }
    };

    private class ViewHolder {
        // 封面
        public ImageView imageView;
        // 文件夹名称
        public TextView folderName;
        // 文件夹里面的图片数量
        public TextView fileCount;
    }

    ViewHolder holder = null;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.album_folder_item, null);
            holder = new ViewHolder();
            holder.imageView = (ImageView) convertView.findViewById(R.id.file_image);
            holder.folderName = (TextView) convertView.findViewById(R.id.file_name);
            holder.fileCount = (TextView) convertView.findViewById(R.id.file_count);
            holder.imageView.setAdjustViewBounds(true);
            holder.imageView.setScaleType(ImageView.ScaleType.FIT_XY);

            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();
        String path;

        if (AlbumAllActivity.contentList.get(position).imageList != null) {

            // path = photoAbsolutePathList.get(position);
            // 封面图片路径
            path = AlbumAllActivity.contentList.get(position).imageList.get(0).imagePath;
            // 给folderName设置值为文件夹名称
            // holder.folderName.setText(fileNameList.get(position));
            holder.folderName.setText(AlbumAllActivity.contentList.get(position).bucketName);

            // 给fileNum设置文件夹内图片数量
            // holder.fileNum.setText("" + fileNum.get(position));
            holder.fileCount.setText("" + AlbumAllActivity.contentList.get(position).count);

        } else
            path = "android_hybrid_camera_default";

        if (path.contains("android_hybrid_camera_default"))
            holder.imageView.setImageResource(R.mipmap.plugin_camera_no_pictures);
        else {
            // holder.imageView.setImageBitmap(
            // AlbumAllActivity.contentList.get(position).imageList.get(0).getBitmap());
            final ImageItem item = AlbumAllActivity.contentList.get(position).imageList.get(0);
            holder.imageView.setTag(item.imagePath);
            cache.displayBmp(holder.imageView, item.thumbnailPath, item.imagePath, callback, parent.getContext());
        }
        return convertView;
    }

}
