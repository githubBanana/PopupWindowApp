package com.xs.popupwindowlib.base.anim;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.PopupWindow;

import com.xs.popupwindowlib.core.PopupWindowProxy;

import java.lang.reflect.Field;

/**
 * @Description
 * @Author xs.lin
 * @Date 2017/3/23 13:53
 */

public abstract class AnimPopupWindow implements PopupWindowProxy.IPopupWindowLifeCycle{

    private PopupWindowProxy popupWindow;
    private View contentView;
    private Activity activity;
    private Animation enterAnimation;
    private Animation exitAnimation;
    //锚点view的location
    private int[] anchorViewLocation;
    private int contentViewHeight;
    private int contentViewWidth;

    public AnimPopupWindow(Activity activity) {
        initView(activity, ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
    }

    public AnimPopupWindow(Activity activity, int width, int height) {
        initView(activity,width,height);
    }

    /**
     * 初始化
     */
    private void initView(Activity activity,int width,int heigth) {
        this.activity = activity;
        anchorViewLocation = new int[2];
        contentView = setPopupContentView();
        popupWindow = new PopupWindowProxy(contentView,width,heigth,this);
        setPopupWindowParam();
        fixMeasureNullPoint(width,heigth);
        setAnimation();
    }

    /**
     * 设置属性
     */
    private void setPopupWindowParam() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            popupWindow.setElevation(10f);
        }
        setBackPressEnable(setOutsideTouchable());
    }

    private void setAnimation() {
        enterAnimation = setEnterAnimation();
        exitAnimation = setExitAnimation();
    }

    /**
     * 修复可能出现的android 4.2的measure空指针问题【借鉴于github BasePopup项目】
     */
    private void fixMeasureNullPoint(int width,int heigth) {
        //修复可能出现的android 4.2的measure空指针问题
        if (contentView != null) {
            int contentViewHeightTemp = ViewGroup.LayoutParams.MATCH_PARENT;
            final ViewGroup.LayoutParams layoutParams = contentView.getLayoutParams();
            if (layoutParams != null && layoutParams.height == ViewGroup.LayoutParams.WRAP_CONTENT) {
                contentViewHeightTemp = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
            ViewGroup.LayoutParams p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, contentViewHeight);
            contentView.setLayoutParams(p);
            contentView.measure(width, heigth);
            contentViewHeight = contentView.getMeasuredHeight();
            contentViewWidth = contentView.getMeasuredWidth();
            contentView.setFocusableInTouchMode(true);
        }
    }

    public void show(View anchorView) {
        anchorView.getLocationOnScreen(anchorViewLocation);
//        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,anchorViewLocation[0]+anchorView.getWidth(), (int) (anchorView.getHeight() - 300*getDensity()));
        popupWindow.showAtLocation(anchorView, Gravity.NO_GRAVITY,anchorViewLocation[0], -contentViewHeight+anchorViewLocation[1]);


        if (enterAnimation != null && contentView != null) {
            contentView.clearAnimation();
            contentView.startAnimation(enterAnimation);
        }

    }

    /**
     * 点击返回键以及外部是否取消PopupWindow
     */
    public void setBackPressEnable(boolean enable) {
        if (enable) {
            popupWindow.setFocusable(true);
            popupWindow.setOutsideTouchable(true);
            popupWindow.setBackgroundDrawable(new ColorDrawable());
        } else {
            popupWindow.setFocusable(false);
            popupWindow.setOutsideTouchable(false);
            popupWindow.setBackgroundDrawable(null);
        }
    }

    public Context getContext() {
        return activity;
    }

    protected View findViewById(int id) {
        if (contentView != null && id != 0) {
            return contentView.findViewById(id);
        }
        return null;
    }

    /**
     * 是否允许popupwindow覆盖屏幕（包含状态栏）
     */
    private void fitPopupWindowOverStatusBar(boolean needFullScreen) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            try {
                Field mLayoutInScreen = PopupWindow.class.getDeclaredField("mLayoutInScreen");
                mLayoutInScreen.setAccessible(true);
                mLayoutInScreen.set(popupWindow, needFullScreen);
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * 获取屏幕高度(px)
     */
    private int getScreenHeight() {
        float a = activity.getResources().getDisplayMetrics().density;
        int b = activity.getResources().getDisplayMetrics().densityDpi;
        return activity.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 获取屏幕宽度(px)
     */
    private int getScreenWidth() {
        return activity.getResources().getDisplayMetrics().widthPixels;
    }

    private float getDensity() {
        return activity.getResources().getDisplayMetrics().density;
    }

    @Override
    public void dismiss() {
        if (exitAnimation != null && contentView != null) {
            exitAnimation.setAnimationListener(exitAnimationListener);
            contentView.clearAnimation();
            contentView.startAnimation(exitAnimation);
        }
    }

    private Animation.AnimationListener exitAnimationListener = new Animation.AnimationListener() {
        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            popupWindow.superDismiss();
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    };

    /*初始化popupWindow的视图*/
    protected abstract View setPopupContentView();

    /*设置点击外部是否dismiss*/
    protected abstract boolean setOutsideTouchable();

    /*设置开始动画*/
    protected abstract Animation setEnterAnimation();

    /*设置退出动画*/
    protected abstract Animation setExitAnimation();
}
