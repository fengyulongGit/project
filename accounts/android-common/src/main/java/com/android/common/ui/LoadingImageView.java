package com.android.common.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;

public class LoadingImageView extends ImageView {

    public LoadingImageView(Context context) {
        super(context);
    }

    public LoadingImageView(Context context, AttributeSet attributeset) {
        super(context, attributeset);
    }

    public LoadingImageView(Context context, AttributeSet attributeset, int i) {
        super(context, attributeset, i);
    }

    public void a() {
        if (getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animationdrawable = (AnimationDrawable) getBackground();
            if (animationdrawable.isRunning())
                animationdrawable.stop();
        }
        clearAnimation();
    }

    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animationdrawable = (AnimationDrawable) getBackground();
            if (!animationdrawable.isRunning())
                animationdrawable.start();
        }
    }

    protected void onDetachedFromWindow() {
        a();
        super.onDetachedFromWindow();
    }

    @SuppressLint({"NewApi"})
    protected void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (getAnimation() != null)
            if (i == 0)
                getAnimation().start();
            else
                getAnimation().cancel();
        if (i != 0)
            a();
        else if (getBackground() instanceof AnimationDrawable) {
            AnimationDrawable animationdrawable = (AnimationDrawable) getBackground();
            if (!animationdrawable.isRunning()) {
                animationdrawable.start();
                return;
            }
        }
    }

    // @SuppressLint({ "NewApi", "Override" })
    // public void setBackground(Drawable drawable) {
    // a();
    // super.setBackground(drawable);
    // }

    @Override
    public void setBackgroundDrawable(Drawable drawable) {
        a();
        super.setBackgroundDrawable(drawable);
    }
}