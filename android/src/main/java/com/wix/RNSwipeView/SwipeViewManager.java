package com.wix.RNSwipeView;

import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.UIManagerModule;
import com.facebook.react.uimanager.ViewGroupManager;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.facebook.react.uimanager.events.EventDispatcher;
import com.wix.RNSwipeView.events.BouncedBackEvent;
import com.wix.RNSwipeView.events.SwipeStartEvent;
import com.wix.RNSwipeView.events.SwipedOutEvent;
import com.wix.RNSwipeView.events.WillBeSwipedOutEvent;
import com.wix.RNSwipeView.events.WillBounceBackEvent;

import java.util.Map;

import javax.annotation.Nullable;

public class SwipeViewManager extends ViewGroupManager<SwipeView> {
    @Override
    public String getName() {
        return "SwipeView";
    }

    @Override
    protected SwipeView createViewInstance(ThemedReactContext reactContext) {
        return new SwipeView(reactContext);
    }

    @Override
    public void updateExtraData(SwipeView root, Object extraData) {

    }

    @ReactProp(name = "changeOpacity")
    public void setChangeOpactity(SwipeView view, boolean changeOpacity) {
        view.setAnimateOpacity(changeOpacity);
    }

    @Override
    protected void addEventEmitters(ThemedReactContext reactContext, SwipeView view) {
        view.setListener(new SwipeEventsEmitter(view, reactContext.getNativeModule(UIManagerModule.class).getEventDispatcher()));
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomDirectEventTypeConstants() {
        return MapBuilder.<String, Object>builder()
                .put("onSwipeStart", MapBuilder.of("registrationName", "onSwipeStart"))
                .put("onWillBeSwipedOut", MapBuilder.of("registrationName", "onWillBeSwipedOut"))
                .put("onSwipedOut", MapBuilder.of("registrationName", "onSwipedOut"))
                .put("onWillBounceBack", MapBuilder.of("registrationName", "onWillBounceBack"))
                .put("onBouncedBack", MapBuilder.of("registrationName", "onBouncedBack"))
                .build();
    }

    private static class SwipeEventsEmitter implements SwipeView.SwipeViewListener {
        private final SwipeView mSwipeView;
        private final EventDispatcher mEventDispatcher;

        public SwipeEventsEmitter(SwipeView view, EventDispatcher eventDispatcher) {
            mSwipeView = view;
            mEventDispatcher = eventDispatcher;
        }

        @Override
        public void onSwipeStart() {
            mEventDispatcher.dispatchEvent(new SwipeStartEvent(mSwipeView.getId()));
        }

        @Override
        public void onWillBeSwipedOut() {
            mEventDispatcher.dispatchEvent(new WillBeSwipedOutEvent(mSwipeView.getId()));
        }

        @Override
        public void onSwipedOut() {
            mEventDispatcher.dispatchEvent(new SwipedOutEvent(mSwipeView.getId()));
        }

        @Override
        public void onWillBounceBack() {
            mEventDispatcher.dispatchEvent(new WillBounceBackEvent(mSwipeView.getId()));
        }

        @Override
        public void onBouncedBack() {
            mEventDispatcher.dispatchEvent(new BouncedBackEvent(mSwipeView.getId()));
        }
    }
}
