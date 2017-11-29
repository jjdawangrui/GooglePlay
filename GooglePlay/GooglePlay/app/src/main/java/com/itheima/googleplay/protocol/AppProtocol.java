package com.itheima.googleplay.protocol;

import android.content.ClipData;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itheima.googleplay.base.BaseProtocol;
import com.itheima.googleplay.bean.ItemBean;

import java.util.List;

/**
 * Created by raynwang on 2017/7/16.
 */

public class AppProtocol extends BaseProtocol<List<ItemBean>> {
    @Override
    public String getInterfaceKey() {
        return "app";
    }


    /**
     * json解析的3中方式：
     * 1.结点解析
     * 2.bean解析
     * 3.泛型解析，jsonString-->List/Map，下面用的就是这个
     * @param json
     * @return
     */
//    @Override
//    protected List<ItemBean> parseJson(String json) {
//        Gson gson = new Gson();
//        List<ItemBean> itemBeanList = gson.fromJson(json,new TypeToken<List<ItemBean>>(){}.getType());//固定写法
//        return itemBeanList;
//    }
}
