package com.itheima.googleplay.base;

import android.widget.BaseAdapter;

import java.util.List;

//针对BaseAdapter简单封装,针对的是其中的3个方法(getCount,getItem,getItemId)
public abstract class MyBaseAdapter extends BaseAdapter {
    List mDataSets;

    //子类通过构造方法，把数据集传过来
    public MyBaseAdapter (List dataSets){
        mDataSets = dataSets;
    }

    @Override
    public int getCount() {
        if (mDataSets != null) {
            return mDataSets.size();
        }
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (mDataSets != null) {
            return mDataSets.get(i);
        }
        return null;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }
}
