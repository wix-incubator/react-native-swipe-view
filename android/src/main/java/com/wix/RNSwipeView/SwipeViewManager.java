package com.wix.RNSwipeView;

import android.view.View;

import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.ViewGroupManager;

/**
 * Created by yedidyak on 07/09/2016.
 */
public class SwipeViewManager extends ViewGroupManager {
    @Override
    public String getName() {
        return "SwipeView";
    }

    @Override
    protected View createViewInstance(ThemedReactContext reactContext) {
        return null;
    }

    @Override
    public void updateExtraData(View root, Object extraData) {

    }
}
