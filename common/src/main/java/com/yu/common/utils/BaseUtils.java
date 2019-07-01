package com.yu.common.utils;

import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class BaseUtils {


  public static boolean isShouldHideInput(View focusView, MotionEvent event, View view) {
    if (focusView != null && (focusView instanceof EditText)) {
      return !isEditTextTouched(event, view);
    }
    return false;
  }


  private static boolean isEditTextTouched(MotionEvent event, View view) {

    if (view == null || !view.isEnabled()) {
      return false;
    }

    if (view instanceof EditText) {

      int[] l = { 0, 0 };
      view.getLocationInWindow(l);

      int left = l[0], top = l[1], bottom = top + view.getHeight(), right = left + view.getWidth();

      if (event.getX() > left
          && event.getX() < right
          && event.getY() > top
          && event.getY() < bottom) {
        return true;
      } else {
        return false;
      }
    }

    if (view instanceof ViewGroup) {
      for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {

        if (isEditTextTouched(event, ((ViewGroup) view).getChildAt(i))) {
          return true;
        }
      }
    }

    return false;
  }
}
