package com.wix.RNSwipeView;

import android.content.Context;
import android.support.v4.view.MotionEventCompat;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

import com.facebook.react.uimanager.RootViewUtil;

public class SwipeView extends ViewGroup {

    public interface SwipeViewListener {
        void onSwipeStart();
        void onWillBeSwipedOut();
        void onSwipedOut();
        void onWillBounceBack();
        void onBouncedBack();
    }

    private static final float MIN_DISABLE_SCROLL = new ViewConfiguration().getScaledPagingTouchSlop();
    private static final boolean DEFAULT_ANIMATE_OPACITY = true;
    private float initialX = 0;
    private boolean swiping = false;
    private boolean animateOpacity = DEFAULT_ANIMATE_OPACITY;
    private int swipeOutDistance = Integer.MAX_VALUE;
    private SwipeViewListener listener;

    public SwipeView(Context context) {
        super(context);
    }

    public void setListener(SwipeViewListener listener) {
        this.listener = listener;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        swipeOutDistance = w/2;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);
        switch(action) {
            case (MotionEvent.ACTION_DOWN):
                initialX = event.getRawX();
                return false;
            case (MotionEvent.ACTION_MOVE) :
                float deltaX = event.getRawX() - initialX;
                boolean nowSwiping = Math.abs(deltaX) > MIN_DISABLE_SCROLL;
                if (!swiping && nowSwiping && listener != null) {
                    listener.onSwipeStart();
                }

                swiping = swiping || nowSwiping;
                if(swiping) {
                    RootViewUtil.getRootView(this).onChildStartedNativeGesture(event);
                }
                return swiping;
            case (MotionEvent.ACTION_UP) :
            case (MotionEvent.ACTION_CANCEL) :
            case (MotionEvent.ACTION_OUTSIDE) :
            default:
                return false;
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = MotionEventCompat.getActionMasked(event);

        switch(action) {
            case (MotionEvent.ACTION_MOVE) :
                float deltaX = event.getRawX() - initialX;
                handleMove(deltaX);
                return true;
            case (MotionEvent.ACTION_UP) :
            case (MotionEvent.ACTION_CANCEL) :
            case (MotionEvent.ACTION_OUTSIDE) :
                return handleUp(event);
            default :
                return super.onTouchEvent(event);
        }
    }

    private void handleMove(float deltaX) {
        setTranslationX(deltaX);
        if (animateOpacity) {
            float newAlpha = 1 - 0.9f * Math.min(1, Math.abs(deltaX) / swipeOutDistance);
            setAlpha(newAlpha);
        }
        requestDisallowInterceptTouchEvent(swiping);
    }

    private boolean handleUp(MotionEvent event) {
        float deltaX = event.getRawX() - initialX;

        if(Math.abs(deltaX) >= swipeOutDistance) {
            animateOut(deltaX > 0);
        } else {
            animateBack(swiping);
        }
        swiping = false;
        return super.onTouchEvent(event);
    }

    private void animateBack(final boolean wasSwiping) {
        if (wasSwiping && listener != null) {
            listener.onWillBounceBack();
        }
        animate().alpha(1).setListener(null);
        animate().translationX(0).setListener(null).withEndAction(new Runnable() {
            @Override
            public void run() {
                if (wasSwiping && listener != null) {
                    listener.onBouncedBack();
                }
            }
        });
    }

    private void animateOut(boolean left) {
        if (listener != null) {
            listener.onWillBeSwipedOut();
        }
        animate().alpha(0);
        animate().translationX((left ? 1 : -1) * getContext().getResources().getDisplayMetrics().widthPixels)
                .setListener(null)
                .withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        if (listener != null) {
                            listener.onSwipedOut();
                        }
                    }
                });
    }

    public void setAnimateOpacity(boolean animateOpacity) {
        this.animateOpacity = animateOpacity;
    }
}