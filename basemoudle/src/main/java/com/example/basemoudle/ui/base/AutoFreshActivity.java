
package com.example.basemoudle.ui.base;


import com.example.basemoudle.ui.base.action.IAtutoFresh;

public abstract class AutoFreshActivity extends BaseActivity implements IAtutoFresh {
    protected final AutoFreshChecker mAutoFreshChecker = new AutoFreshChecker(this);

    @Override
    protected void onResume() {
        super.onResume();
        mAutoFreshChecker.loadData();
    }
}

