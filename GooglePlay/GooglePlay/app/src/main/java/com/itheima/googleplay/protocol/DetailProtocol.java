package com.itheima.googleplay.protocol;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.itheima.googleplay.base.BaseProtocol;
import com.itheima.googleplay.bean.ItemBean;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by raynwang on 2017/7/21.
 */

public class DetailProtocol extends BaseProtocol<ItemBean> {
    //url-->http://localhost:8080/GooglePlayServer/detail?index=0
    //实际的url-->http://localhost:8080/GooglePlayServer/detail?packageName=com.itheima.www

    public String packageName;

    //通过构造方法，把包名传进来，
    public DetailProtocol(String packageName) {
        this.packageName = packageName;
    }

    @Override
    public String getInterfaceKey() {
        return "detail";
    }

//    @Override
//    protected ItemBean parseJson(String json) {
//        Gson gson = new Gson();
//        return gson.fromJson(json, ItemBean.class);
//    }

    @NonNull
    @Override
    public Map<String, Object> getParamsMap(int index) {
        Map<String, Object> parasmMap = new HashMap<>();
        parasmMap.put("packageName", packageName);
        return parasmMap;
    }

    /**
     * 覆写方法，加上包名就唯一性了
     * @param index
     * @return
     */
    @Override
    public String generateOnlyKey(int index) {
        return getInterfaceKey() + "." + packageName;
    }
}
