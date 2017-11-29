package com.itheima.googleplay.factory;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.widget.ListView;

import com.itheima.googleplay.utils.UIUtils;

/**
 * 生产ListView的工厂类
 */
public class ListViewFactory {
    /**
     * 得到一个listview的实例对象
     */
    public static ListView createListView() {
        ListView listView = new ListView(UIUtils.getContext());
        //常规设置
        listView.setDividerHeight(0);//去掉分割线
        listView.setCacheColorHint(Color.TRANSPARENT);//在3.0一下系统的一些兼容问题
        listView.setFastScrollEnabled(true);//快速滑动滚动条
        listView.setSelector(new ColorDrawable(Color.TRANSPARENT));//去掉默认的点击效果
        return listView;
    }
}
