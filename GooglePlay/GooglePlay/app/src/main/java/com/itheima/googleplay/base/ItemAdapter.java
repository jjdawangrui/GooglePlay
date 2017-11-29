package com.itheima.googleplay.base;

import android.content.Intent;
import android.os.SystemClock;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Toast;

import com.itheima.googleplay.activity.DetailActivity;
import com.itheima.googleplay.bean.HomeBean;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.holder.ItemHolder;
import com.itheima.googleplay.manager.DownLoadManager;
import com.itheima.googleplay.utils.UIUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by raynwang on 2017/7/20.
 */

public class ItemAdapter extends SuperBaseAdapter<ItemBean> {

    //创建一个集合保存所有的ItemHolder(观察者)
    public List<ItemHolder> mItemHolders = new ArrayList<>();

    public ItemAdapter(List dataSets, AbsListView absListView) {
        super(dataSets,absListView);
    }

    public BaseHolder getSpecialBaseHolder(int position) {
        ItemHolder itemHolder = new ItemHolder();
        //添加观察者到集合中
        mItemHolders.add(itemHolder);

        //把观察者添加到观察者集合中
        DownLoadManager.getInstance().addObserver(itemHolder);

        return itemHolder;
    }

    @Override
    public boolean hasLoadMore() {
        return true;
    }


    @Override
    public void onNormalitemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ItemBean itemBean = (ItemBean) mDataSets.get(i);
        //intent跳转
        Intent intent = new Intent(UIUtils.getContext(), DetailActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);//下面的上下文是一个全局的上下文，所以要加一个flag
        //传值
        intent.putExtra("packageName", itemBean.packageName);
        UIUtils.getContext().startActivity(intent);
    }
}
