package com.rainbow.kam.textviewroller;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.TextView;

public class RollingItem extends FrameLayout implements AnimationListener {
    private LayoutInflater inflater;

    private Animation appear, disappear;
    private ViewGroup container;


    private View[] viewPool = new View[2];
    private int currView, nextView;
    private int countNext = 1;
    private int repeatTime = 0;

    private TextView tv;
    private int value;
    TextView tvCurrent;
    TextView tvNext;


    public RollingItem(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }


    public RollingItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    public RollingItem(Context context) {
        super(context);
        init(context);
    }


    private void init(Context context) {
        inflater = LayoutInflater.from(context);
        inflateViews(context);
        setAnimation(context);

        initViewPool();
    }


    private void inflateViews(Context context) {
        inflate(context, R.layout.rolling_item, this);
        container = (ViewGroup) findViewById(R.id.RollingItemContainer);
    }


    private void setAnimation(Context context) {
        appear = AnimationUtils.loadAnimation(context, R.anim.up_appear);
        appear.setFillAfter(true);
        appear.setAnimationListener(this);
        disappear = AnimationUtils.loadAnimation(context, R.anim.up_disappear);
        disappear.setFillAfter(true);
    }


    private void initViewPool() {
        viewPool[0] = createView(0);
        viewPool[1] = createView(1);

        container.addView(viewPool[0]);
        container.addView(viewPool[1]);

        currView = 0;
        nextView = 1;
    }


    private View createView(int num) {
        tv = (TextView) inflater.inflate(R.layout.rolling_item_number, this, false);
        tv.setText(String.valueOf(num));
        tv.setVisibility(INVISIBLE);
        return tv;
    }


    public void startRolling(int value, int rollingCount) {
        repeatTime = rollingCount + 1;
        countNext = 0;
        this.value = value;
        tv.setVisibility(INVISIBLE);
        doNextMove();
    }


    private void doNextMove() {
        updateCount();
        updateRepeat();

        if (repeatTime != 0) {
            tvCurrent = (TextView) getCurrentView();
            tvCurrent.startAnimation(disappear);

            tvNext = (TextView) getNextView();
            tvNext.setText(String.valueOf(countNext));
            tvNext.startAnimation(appear);
            tvNext.setVisibility(View.VISIBLE);
            next();
        }else{
            tvNext.setText(String.valueOf(value));
        }
    }


    private void updateCount() {
        countNext++;
        if (10 <= countNext) {
            countNext = 0;
        }
    }


    private void updateRepeat() {
        repeatTime--;
        if (0 >= repeatTime) {
            repeatTime = 0;
        } else if (10 < repeatTime) {
            appear.setDuration(50);
            disappear.setDuration(50);
        } else if (4 < repeatTime) {
            appear.setDuration(70);
            disappear.setDuration(70);
        } else if (2 < repeatTime) {
            appear.setDuration(120);
            disappear.setDuration(120);
        } else if (0 < repeatTime) {
            appear.setDuration(150);
            disappear.setDuration(150);
        }
    }


    private View getCurrentView() {
        return viewPool[currView];
    }


    private View getNextView() {
        return viewPool[nextView];
    }


    private void next() {
        int length = viewPool.length;
        currView = nextView;

        nextView++;
        if (length <= nextView) {
            nextView = 0;
        }
    }


    @Override public void onAnimationEnd(Animation animation) {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                doNextMove();
            }
        }, 1);
    }


    @Override public void onAnimationStart(Animation animation) {

    }


    @Override public void onAnimationRepeat(Animation animation) {

    }
}