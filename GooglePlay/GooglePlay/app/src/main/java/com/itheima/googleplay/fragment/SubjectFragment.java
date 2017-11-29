package com.itheima.googleplay.fragment;

import android.os.Bundle;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.googleplay.base.BaseFragment;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.base.LoadingPager;
import com.itheima.googleplay.base.SuperBaseAdapter;
import com.itheima.googleplay.bean.ItemBean;
import com.itheima.googleplay.bean.SubjectBean;
import com.itheima.googleplay.factory.ListViewFactory;
import com.itheima.googleplay.holder.ItemHolder;
import com.itheima.googleplay.holder.SubjectHolder;
import com.itheima.googleplay.protocol.SubjectProtocol;
import com.itheima.googleplay.utils.UIUtils;

import java.util.List;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class SubjectFragment extends BaseFragment {

    private List<SubjectBean> mDatas;
    private SubjectProtocol mSubjectProtocol;

    @Override
    protected LoadingPager.LoadDataResult initData() {
        mSubjectProtocol = new SubjectProtocol();
        try {
            mDatas = mSubjectProtocol.loadData(0);
            return checkResult(mDatas);
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadDataResult.ERROR;
        }
    }

    @Override
    protected View initSuccessView() {
        ListView listView = ListViewFactory.createListView();
        listView.setAdapter(new SubjectAdapter(mDatas,listView));
        return listView;
    }

    class SubjectAdapter extends SuperBaseAdapter<SubjectBean> {

        public SubjectAdapter(List dataSets, AbsListView absListView) {
            super(dataSets, absListView);
        }

        @Override
        public BaseHolder getSpecialBaseHolder(int position) {
            return new SubjectHolder();
        }

        @Override
        public boolean hasLoadMore() {
            return true;
        }

        @Override
        public List onLoadMore() throws Exception {
            SystemClock.sleep(2000);
            List<SubjectBean> list = mSubjectProtocol.loadData(mDatas.size());
            return list;
        }

        /**
         * 处理普通条目的点击事件
         */
        @Override
        public void onNormalitemClick(AdapterView<?> parent, View view, int position, long id) {
            //data
            SubjectBean subjectBean = mDatas.get(position);
            Toast.makeText(UIUtils.getContext(), subjectBean.des, Toast.LENGTH_SHORT).show();
        }
    }
}
