package com.skj.wheel.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.Set;


/**
 * Created by 孙科技 on 2017/10/31.
 */

public class SPUtil {
    /**
     * 保存在手机里面的文件名
     */
    public static final String APP_COMMON_FILE_NAME = "sp_";
    private static SPUtil spUtil;

    private Context mContext;

    public static SPUtil getInstance(Context context) {
        if (spUtil == null)
            spUtil = new SPUtil(context);
        return spUtil;
    }

    public SPUtil(Context context) {
        this.mContext = context;

    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     *
     * @param key
     * @param object
     */
    public boolean saveToApp(String key, Object object) {
        SharedPreferences sp = mContext
                .getSharedPreferences(APP_COMMON_FILE_NAME,
                        Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        if (object instanceof String) {
            editor.putString(key, (String) object);
        } else if (object instanceof Integer) {
            editor.putInt(key, (Integer) object);
        } else if (object instanceof Boolean) {
            editor.putBoolean(key, (Boolean) object);
        } else if (object instanceof Float) {
            editor.putFloat(key, (Float) object);
        } else if (object instanceof Long) {
            editor.putLong(key, (Long) object);
        } else if (object instanceof Set) {
            editor.putStringSet(key, (Set<String>) object);
        } else {
            editor.putString(key, "");
        }
        return editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     *
     * @param key
     * @param defaultObject
     * @return
     */
    public Object getFromApp(String key, Object defaultObject) {
        SharedPreferences sp = mContext
                .getSharedPreferences(APP_COMMON_FILE_NAME,
                        Context.MODE_PRIVATE);
        if (defaultObject instanceof String) {
            return sp.getString(key, (String) defaultObject);
        } else if (defaultObject instanceof Integer) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if (defaultObject instanceof Boolean) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if (defaultObject instanceof Float) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if (defaultObject instanceof Long) {
            return sp.getLong(key, (Long) defaultObject);
        } else if (defaultObject instanceof Set) {
            return sp.getStringSet(key, ((Set<String>) defaultObject));
        }
        return null;
    }

    /**
     * 序列化对象
     *
     * @return
     * @throws IOException
     */
    public String serialize(Object object) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
        objectOutputStream.writeObject(object);
        String serStr = byteArrayOutputStream.toString("ISO-8859-1");
        serStr = java.net.URLEncoder.encode(serStr, "UTF-8");
        objectOutputStream.close();
        byteArrayOutputStream.close();
        return serStr;
    }

    /**
     * 反序列化对象
     *
     * @param str
     * @return
     * @throws IOException
     * @throws ClassNotFoundException
     */
    public Object deSerialization(String str) throws IOException, ClassNotFoundException {
        if (TextUtil.isEmpty(str))
            return null;
        String redStr = java.net.URLDecoder.decode(str, "UTF-8");
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
        ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream);
        Object object = (Object) objectInputStream.readObject();
        objectInputStream.close();
        byteArrayInputStream.close();
        return object;
    }

    /**
     * 保存、提取信息
     *
     * @param data
     * @return
     */
    public boolean putData(Object data) {
        String str = "";
        try {
            str = serialize(data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return saveToApp("", str);
    }

    public Object getData() {
        Object data = null;
        String str = (String) getFromApp("", "");
        try {
            data = (Object) deSerialization(str);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }


    /**
     * 获取保存信息
     *
     * @return
     */
    public String getString() {
        String data = (String) getFromApp("", "");
        if (TextUtils.isEmpty(data)) {
            return "";
        }
        return data;
    }


    /**
     * 清空保存在手机的数据
     */
    public void clear() {
        remove("");
    }

    /**
     * 移除某个key值已经对应的值
     *
     * @param key
     */
    public void remove(String key) {
        try {
            SharedPreferences sp = mContext.getSharedPreferences(
                    APP_COMMON_FILE_NAME, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.remove(key);
            editor.commit();
        } catch (Exception e) {
        }
    }
}
