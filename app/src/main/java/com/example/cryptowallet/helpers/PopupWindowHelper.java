package com.example.cryptowallet.helpers;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.cryptowallet.R;

public class PopupWindowHelper {
    private Context context;
    private PopupWindow popupWindow;

    public PopupWindowHelper(Context context) {
        this.context = context;
    }

    public void showPopupWindow(View anchorView) {
        // Inflate the custom layout for the Popup Window

        // Create the Popup Window with full screen dimensions
        int width = WindowManager.LayoutParams.MATCH_PARENT;
        int height = WindowManager.LayoutParams.WRAP_CONTENT;
        popupWindow = new PopupWindow(anchorView, width, height);
        popupWindow.setFocusable(true);
        popupWindow.setOutsideTouchable(true);

        // Set background drawable to handle touch events outside the Popup Window
        Drawable backgroundDrawable = new ColorDrawable(Color.TRANSPARENT);
        popupWindow.setBackgroundDrawable(backgroundDrawable);

        // Set the animation style for the Popup Window
        //popupWindow.setAnimationStyle(R.style.PopupAnimation);

        // Show the Popup Window
        popupWindow.showAtLocation(anchorView, Gravity.CENTER, 0, 0);
    }

    public void dismissPopupWindow() {
        if (popupWindow != null && popupWindow.isShowing()) {
            popupWindow.dismiss();
        }
    }
}