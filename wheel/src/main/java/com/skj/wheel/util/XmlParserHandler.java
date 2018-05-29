package com.skj.wheel.util;

import android.content.Context;


import org.apache.commons.io.IOUtil;
import org.xml.sax.helpers.DefaultHandler;

import java.io.InputStreamReader;

public class XmlParserHandler extends DefaultHandler {


    /**
     * 读取getAssets的城市json
     *
     * @param context
     * @return
     */
    public static String getStringFromAssert(Context context) {
        try {
            InputStreamReader inputReader = new InputStreamReader(context.getResources().getAssets().open("city.json"));
            String result = IOUtil.toString(inputReader);
            return "{\"root\":" + result + "}";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
