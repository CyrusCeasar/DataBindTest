
package com.example.chenlei2.databindtest.ui.base;

/**.
 * @Title: LazyInitFragment.java 
 * @Package: com.twsz.app.ivysensortree.ui.page.base 
 * @Description: TODO
 * @author chenlei2  
 * @date 2015-3-18 下午4:57:37 
 * @version 1.3.1 
 */

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;

import com.apkfuns.logutils.LogUtils;
import com.example.chenlei2.databindtest.ui.base.action.IAtutoFresh;
import com.example.chenlei2.databindtest.ui.plugin.InitingView;


/**.
 * @Description 
 * @author chenlei2
 * @date 2015-3-18 下午4:57:37 
 * @version V1.3.1
 */

public abstract class LazyInitFragment extends BaseFragment implements IAtutoFresh {

    protected boolean isPrepared = false;
    protected boolean isVesible = false;

    protected ViewGroup mRootView;

    /**.
     * Description 
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return 
     * @see com.twsz.app.ivysensortree.ui.page.base.BaseFragment#onCreateView(LayoutInflater, ViewGroup, Bundle)
     */

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
            @Nullable Bundle savedInstanceState) {
        if (mRootView == null) {
            mRootView = new InitingView(getActivity());
            if (isVesible) {
                init();
            }
        }
        ViewGroup parentGroup = (ViewGroup) mRootView.getParent();
        if (parentGroup != null) {
            parentGroup.removeAllViews();
        }
        return mRootView;
    }

    /**
     * Description  
     * @see com.twsz.app.ivysensortree.ui.page.base.BaseFragment#onResume() 
     */

    @Override
    public void onResume() {
        super.onResume();
        LogUtils.d("onResume isVisible ==  " + isVisible());
        LogUtils.d( "onResume isPrepared ==  " + isPrepared);
        isVesible = isVisible() && isPrepared;

    }

    /**
     * Description  
     * @see com.twsz.app.ivysensortree.ui.page.base.BaseFragment#onPause() 
     */

    @Override
    public void onPause() {
        super.onPause();
        isVesible = false;

    }

    /**.
     * Description 
     * @param isVisibleToUser 
     * @see android.support.v4.app.Fragment#setUserVisibleHint(boolean) 
     */

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        isVesible = isVisibleToUser;
        if (mRootView != null) {
            if (isVisibleToUser && !isPrepared) {
                init();
            } else if (isVisibleToUser && isPrepared) {
                loadData();
            }
        }
    }

    private void init() {

        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT,
                LayoutParams.MATCH_PARENT);
        View view = initView(mLayoutInflater);
        if (view != null) {
            mRootView.removeAllViews();
            mRootView.addView(view, layoutParams);
            isPrepared = true;
            loadData();
        }

        //        wheatherLoad();
    }

    public abstract View initView(LayoutInflater layoutInflater);

}
