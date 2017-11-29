package com.itheima.googleplay.protocol;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itheima.googleplay.base.BaseProtocol;

import java.util.List;

/**
 * 排行页面的相关协议类
 */
public class HotProtocol extends BaseProtocol<List<String>> {
    @NonNull
    @Override
    public String getInterfaceKey() {
        return "hot";
    }

//    @Override
//    protected List<String> parseJson(String resJsonString) {
//        return new Gson().fromJson(resJsonString,new TypeToken<List<String>>(){}.getType());
//    }
}
