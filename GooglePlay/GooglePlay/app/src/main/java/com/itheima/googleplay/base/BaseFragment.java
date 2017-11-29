package com.itheima.googleplay.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itheima.googleplay.utils.UIUtils;

import java.util.List;
import java.util.Map;


public abstract class BaseFragment extends Fragment {

    private LoadingPager mLoadingPager;

    //这个暴露的方法那里用？
    public LoadingPager getLoadingPager(){
        return mLoadingPager;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (mLoadingPager==null) {
            //右边的就是一个子类，只不过是匿名子类，所以实现父类LoadingPager的抽象方法
            mLoadingPager = new LoadingPager(UIUtils.getContext()) {
                @Override
                public View initSuccessView() {
                    return BaseFragment.this.initSuccessView();//方法名一样，为了区别所以前面要加上  类名.this.
                }

                @Override
                public LoadDataResult initData() {
                    return BaseFragment.this.initData();
                }
            };
        }

        //触发加载数据，写在这不好，因为会有预加载，最好是点击那个页面就加载哪个
        //所以去MainActivity里面写个监听的方法
        //其实也不用，设置预加载的数量为0就行了
//        mLoadingPager.triggerLoadData();

        return mLoadingPager;
    }

    //这里继续来两个抽象方法，留给子类实现
    protected abstract LoadingPager.LoadDataResult initData();
    protected abstract View initSuccessView();

    //校验请求回来的数据
    public LoadingPager.LoadDataResult checkResult(Object resObj){
        if (resObj==null){
            return LoadingPager.LoadDataResult.EMPTY;
        }
        if (resObj instanceof List){
            if (((List) resObj).size()==0){
                return LoadingPager.LoadDataResult.EMPTY;
            }
        }
        if (resObj instanceof Map){
            if (((Map) resObj).size()==0){
                return LoadingPager.LoadDataResult.EMPTY;
            }
        }
        //如果走到这，说明有数据，访问成功了
        return LoadingPager.LoadDataResult.SUCCESS;
    }
}
