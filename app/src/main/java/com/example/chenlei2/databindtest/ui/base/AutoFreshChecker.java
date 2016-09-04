
package com.example.chenlei2.databindtest.ui.base;


import com.example.chenlei2.databindtest.ui.base.action.IAtutoFresh;

public class AutoFreshChecker implements IAtutoFresh {

    private final IAtutoFresh freshAction;
    private long lastFreshTime;
    private long maxIntervalTime;

    public AutoFreshChecker(IAtutoFresh freshAction) {
        this.freshAction = freshAction;
    }

    private boolean loadAble() {
        return System.currentTimeMillis() - lastFreshTime > maxIntervalTime ? true : false;
    }

    public long getLastFreshTime() {
        return lastFreshTime;
    }

    public void setLastFreshTime(long lastFreshTime) {
        this.lastFreshTime = lastFreshTime;
    }

    public long getMaxIntervalTime() {
        return maxIntervalTime;
    }

    public void setMaxIntervalTime(long maxIntervalTime) {
        this.maxIntervalTime = maxIntervalTime;
    }

    @Override
    public void loadData() {
        if (loadAble()) {
            freshAction.loadData();
            setLastFreshTime(System.currentTimeMillis());
        }
    }

}
