package com.yu.common.framework;

import android.view.View;

import com.yu.common.launche.LauncherHelper;
import com.yu.common.mvp.BasePresenter;
import com.yu.common.mvp.Viewer;


public abstract class BaseViewPresenter<T extends Viewer> extends BasePresenter<T> {

    public BaseViewPresenter(T viewer) {
        super(viewer);
    }

    @Override public void createdView(View view) {

    }

    @Override public void resume() {

    }

    @Override public void pause() {

    }

    @Override public void willDestroy() {

    }

    protected LauncherHelper getLaunchHelper() {
        return LauncherHelper.from(getActivity());
    }



}
