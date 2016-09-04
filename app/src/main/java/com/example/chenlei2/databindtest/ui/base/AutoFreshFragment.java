
package com.example.chenlei2.databindtest.ui.base;


import com.example.chenlei2.databindtest.ui.base.action.IAtutoFresh;

public abstract class AutoFreshFragment extends BaseFragment implements IAtutoFresh {

    protected final IAtutoFresh mAutoFreshChecker = new AutoFreshChecker(this);

    @Override
    public void onResume() {
        super.onResume();
        mAutoFreshChecker.loadData();
    }

}
