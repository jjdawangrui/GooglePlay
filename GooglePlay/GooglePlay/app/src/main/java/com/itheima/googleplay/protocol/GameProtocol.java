package com.itheima.googleplay.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itheima.googleplay.base.BaseProtocol;
import com.itheima.googleplay.bean.ItemBean;

import java.util.List;

/**
 * Created by raynwang on 2017/7/16.
 */

public class GameProtocol extends BaseProtocol<List<ItemBean>> {
    @Override
    public String getInterfaceKey() {
        return "game";
    }


//    @Override
//    protected List<ItemBean> parseJson(String json) {
//        Gson gson = new Gson();
//        List<ItemBean> itemBeanList = gson.fromJson(json,new TypeToken<List<ItemBean>>(){}.getType());//固定写法
//        return itemBeanList;
//    }
}
