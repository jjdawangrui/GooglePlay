package com.itheima.googleplay.protocol;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.itheima.googleplay.base.BaseProtocol;
import com.itheima.googleplay.bean.SubjectBean;

import java.util.List;

/**
 * Created by raynwang on 2017/7/17.
 */

public class SubjectProtocol extends BaseProtocol<List<SubjectBean>> {
    @Override
    public String getInterfaceKey() {
        return "subject";
    }

//    @Override
//    protected List<SubjectBean> parseJson(String json) {
//        Gson gson = new Gson();
//        return gson.fromJson(json, new TypeToken<List<SubjectBean>>(){}.getType());
//    }
}
