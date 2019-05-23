package com.rokkystudio.lamp.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.rokkystudio.lamp.Playlist;
import com.rokkystudio.lamp.R;
import com.rokkystudio.lamp.Track;

public class PlayerView extends ViewGroup
{


    public enum PlayerMode {
        REPEAT_ALL, REPEAT_ONE, SHUFFLE_ALL
    }

    private final Context mContext;
    private GestureDetector mGestureDetector;
    private LogoView mLogoView;
    private ImageButton mSettingsButton;
    private ImageButton mModeButton;
    private PlayerViewListener mListener = null;

    public PlayerView(Context context) {
        super(context);
        mContext = context;
        mLogoView = new LogoView(context);
        mSettingsButton = new ImageButton(context);
        mModeButton = new ImageButton(context);
        init();
    }

    public PlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        mLogoView = new LogoView(context, attrs);
        mSettingsButton = new ImageButton(context, attrs);
        mModeButton = new ImageButton(context, attrs);
        init();
    }

    public PlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        mLogoView = new LogoView(context, attrs, defStyleAttr);
        mSettingsButton = new ImageButton(context, attrs, defStyleAttr);
        mModeButton = new ImageButton(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        addView(mLogoView);
        addView(mSettingsButton);
        addView(mModeButton);

        mSettingsButton.setImageResource(R.drawable.player_settings);
        mSettingsButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mSettingsButton.setBackgroundColor(0);
        mSettingsButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

        mModeButton.setImageResource(R.drawable.player_repeat_all);
        mModeButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        mModeButton.setBackgroundColor(0);
        mModeButton.setScaleType(ImageView.ScaleType.FIT_CENTER);

        setBackgroundColor(Color.parseColor("#111111"));
        mGestureDetector = new GestureDetector(mContext, new MyGestureListener());

        mModeButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            if (mListener != null) mListener.onModeButtonClick();
            }
        });

        mSettingsButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            if (mListener != null) mListener.onSettingsButtonClick();
            }
        });

        postDelayed(new Runnable() {
            @Override
            public void run() {
                mLogoView.show();
            }
        }, 500);
    }

    public void setPlaylist(Playlist playlist) {
        for (Track track : playlist) {
            track.getCover();
        }
    }

    public void showMenu() {

    }

    public void hideMenu() {

    }

    public void showCarousel() {

    }

    public void hideCarousel() {

    }

    public void showNext() {

    }

    public void showPrev() {

    }

    public void showPause() {

    }

    public void showPlay() {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        //int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        measureChild(mLogoView, widthMeasureSpec, heightMeasureSpec);
        measureChild(mSettingsButton, widthMeasureSpec, heightMeasureSpec);
        measureChild(mModeButton, widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //drawLogo(canvas);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        int w = mLogoView.getMeasuredWidth() / 3;
        int h = mLogoView.getMeasuredHeight() / 3;
        int x = getMeasuredWidth() / 2 - w / 2;
        int y = getMeasuredHeight() - h - 20;
        mLogoView.layout(x, y, x + w, y + h);
        mSettingsButton.layout(getMeasuredWidth() - 120, 20, getMeasuredWidth() - 20, 120);
        mModeButton.layout(20, 20, 120, 120);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (mGestureDetector.onTouchEvent(event)) return true;
        return super.onTouchEvent(event);
    }

    public interface GestureListener {
        void onSlideDown();
        void onSlideUp();
        void onSlideLeft();
        void onSlideRight();
        void onTouchOnce();
        void onTouchHold();
    }

    private class MyGestureListener extends android.view.GestureDetector.SimpleOnGestureListener
    {
        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            //Toast.makeText(mContext, "OnScroll", Toast.LENGTH_SHORT).show();
            mLogoView.scrollBy((int)distanceX, (int)distanceY);
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY)
        {
            Toast.makeText(mContext, "OnFling", Toast.LENGTH_SHORT).show();
            //scroller.fling(getScrollX(), getScrollY(), -(int)velocityX, -(int)velocityY, 0, image.getWidth() - getWidth(), 0, image.getHeight() - getHeight());
            //awakenScrollBars(scroller.getDuration());
            return true;
        }
    }

    public void setPlayerMode(PlayerMode mode) {
        if (mode == PlayerMode.REPEAT_ALL) {
            mModeButton.setImageResource(R.drawable.player_repeat_all);
        } else if (mode == PlayerMode.REPEAT_ONE) {
            mModeButton.setImageResource(R.drawable.player_repeat_one);
        } else if (mode == PlayerMode.SHUFFLE_ALL) {
            mModeButton.setImageResource(R.drawable.player_shuffle);
        }
        mModeButton.invalidate();
    }

    public void setPlayerViewListener(PlayerViewListener listener) {
        mListener = listener;
    }

    public interface PlayerViewListener {
        void onModeButtonClick();
        void onSettingsButtonClick();
    }
}
