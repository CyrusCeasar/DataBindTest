
package com.example.chenlei2.databindtest.ui.base;


import com.example.chenlei2.databindtest.ui.base.action.IAtutoFresh;

public abstract class AutoFreshActivity extends BaseActivity implements IAtutoFresh {
    protected final AutoFreshChecker mAutoFreshChecker = new AutoFreshChecker(this);

    @Override
    protected void onResume() {
        super.onResume();
        mAutoFreshChecker.loadData();
    }
}
