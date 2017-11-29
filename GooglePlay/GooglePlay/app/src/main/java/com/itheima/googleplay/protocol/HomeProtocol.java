package com.itheima.googleplay.protocol;

import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.itheima.googleplay.base.BaseProtocol;
import com.itheima.googleplay.bean.HomeBean;

/**
 * 负责对HomeFragment里面涉及到的网络请求进行封装
 */
public class HomeProtocol extends BaseProtocol<HomeBean>{


    @NonNull
    @Override
    public String getInterfaceKey() {
        return "home";
    }

//    @Override
//    protected HomeBean parseJson(String json) {
//        Gson gson = new Gson();
//        HomeBean homeBean = gson.fromJson(json, HomeBean.class);
//        return homeBean;
//    }
}
