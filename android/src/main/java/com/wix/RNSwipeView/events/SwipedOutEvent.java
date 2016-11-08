package com.wix.RNSwipeView.events;

import com.facebook.react.uimanager.events.Event;
import com.facebook.react.uimanager.events.RCTEventEmitter;

/**
 * Created by yedidyak on 07/09/2016.
 */
public class SwipedOutEvent extends Event<SwipedOutEvent> {

    public SwipedOutEvent(int viewTag) {
        super(viewTag);
    }

    @Override
    public String getEventName() {
        return "onSwipedOut";
    }

    @Override
    public void dispatch(RCTEventEmitter rctEventEmitter) {
        rctEventEmitter.receiveEvent(getViewTag(), getEventName(), null);
    }
}
