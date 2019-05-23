package com.rokkystudio.lamp.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.rokkystudio.lamp.R;

public class LogoView extends ImageView
{
    private final Context mContext;
    private boolean mShowLogo = false;
    private Animation mAnimationShow;
    private Animation mAnimationHide;

    public LogoView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public LogoView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public LogoView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init()
    {
        setVisibility(INVISIBLE);
        setImageResource(R.drawable.player_logo);
        setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

        mAnimationShow = AnimationUtils.loadAnimation(mContext, R.anim.anim_player_logo_show);
        mAnimationShow.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                setVisibility(VISIBLE);
            }
            @Override
            public void onAnimationEnd(Animation animation) {

            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        mAnimationHide = AnimationUtils.loadAnimation(mContext, R.anim.anim_player_logo_hide);
        mAnimationHide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }
            @Override
            public void onAnimationEnd(Animation animation) {
                setVisibility(INVISIBLE);
            }
            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void show() {
        if (!mShowLogo) {
            mShowLogo = true;
            startAnimation(mAnimationShow);
        }
    }

    public void hide() {
        if (mShowLogo) {
            mShowLogo = false;
            startAnimation(mAnimationHide);
        }
    }
}
