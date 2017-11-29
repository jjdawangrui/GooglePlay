package com.itheima.googleplay.fragment;

import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;

import com.itheima.googleplay.base.BaseFragment;
import com.itheima.googleplay.base.BaseHolder;
import com.itheima.googleplay.base.LoadingPager;
import com.itheima.googleplay.base.SuperBaseAdapter;
import com.itheima.googleplay.bean.CategoryBean;
import com.itheima.googleplay.factory.ListViewFactory;
import com.itheima.googleplay.holder.CategoryNormalHolder;
import com.itheima.googleplay.holder.CategoryTitleHolder;
import com.itheima.googleplay.protocol.CategoryProtocol;
import com.itheima.googleplay.utils.LogUtils;

import java.util.List;

/**
 * 创建者     伍碧林
 * 版权       传智播客.黑马程序员
 * 描述	      ${TODO}
 */
public class CategoryFragment extends BaseFragment {

    private List<CategoryBean> mData;

    @Override
    protected LoadingPager.LoadDataResult initData() {
        CategoryProtocol protocol = new CategoryProtocol();
        try {
            mData = protocol.loadData(0);
            return checkResult(mData);
        } catch (Exception e) {
            e.printStackTrace();
            return LoadingPager.LoadDataResult.ERROR;

        }
    }

    @Override
    protected View initSuccessView() {
        ListView listView = ListViewFactory.createListView();
        listView.setAdapter(new CategoryAdatper(mData, listView));
        return listView;
    }

    class CategoryAdatper extends SuperBaseAdapter<CategoryBean> {

        public CategoryAdatper(List<CategoryBean> dataSets, AbsListView absListView) {
            super(dataSets, absListView);
        }

        @Override
        public BaseHolder getSpecialBaseHolder(int position) {
            CategoryBean categoryBean = mData.get(position);
            if (categoryBean.isTitle) {
                return new CategoryTitleHolder();
            } else {
                return new CategoryNormalHolder();
            }
        }

        @Override
        public int getViewTypeCount() {
            return super.getViewTypeCount() + 1;//再加一种类型
        }

           /*--------------- 实现方案一 ---------------*/
//        @Override
//        public int getItemViewType(int position) {
//            CategoryBean itemBean = mData.get(position);
//            if (position == getCount() - 1) {
//                return VIEWTYPE_LOADMORE;//加载更多-->0
//            } else {
//                if (itemBean.isTitle) {
//                    return 1;
//                } else {
//                    return 2;
//                }
//            }
//        }

        @Override
        public int getNormalItemViewType(int position) {
            CategoryBean itemBean = mData.get(position);
            if (itemBean.isTitle) {
                return 1;//0是加载更多
            } else {
                return 2;
            }
        }
    }
}
