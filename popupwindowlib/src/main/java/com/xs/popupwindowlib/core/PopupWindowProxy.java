package com.xs.popupwindowlib.core;

import android.view.View;
import android.widget.PopupWindow;

/**
 * @Description
 * @Author xs.lin
 * @Date 2017/3/23 15:41
 */

public class PopupWindowProxy extends PopupWindow {

    private IPopupWindowLifeCycle lifeCycle;


    public PopupWindowProxy(View contentView, IPopupWindowLifeCycle lifeCycle) {
        super(contentView);
        this.lifeCycle = lifeCycle;
    }

    public PopupWindowProxy(int width, int height, IPopupWindowLifeCycle lifeCycle) {
        super(width, height);
        this.lifeCycle = lifeCycle;
    }

    public PopupWindowProxy(View contentView, int width, int height, IPopupWindowLifeCycle lifeCycle) {
        super(contentView, width, height);
        this.lifeCycle = lifeCycle;
    }

    public PopupWindowProxy(View contentView, int width, int height, boolean focusable, IPopupWindowLifeCycle lifeCycle) {
        super(contentView, width, height, focusable);
        this.lifeCycle = lifeCycle;
    }

    @Override
    public void dismiss() {
        if (lifeCycle != null)
            lifeCycle.dismiss();
    }

    public void superDismiss() {
        super.dismiss();
    }

    public interface IPopupWindowLifeCycle {
        void dismiss();
    }
}
