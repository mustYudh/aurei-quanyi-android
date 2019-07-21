package com.yu.common.web;

import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.webkit.WebView;
import android.widget.ProgressBar;

/**
 * @author chenwei
 * @date 2017/8/23
 */
public class ProgressWebChromeClient extends android.webkit.WebChromeClient {
  private int currProgress = -1;
  private Handler mHandler = new Handler();
  private ProgressBar progressbar;

  public ProgressWebChromeClient(ProgressBar progressbar) {
    this.progressbar = progressbar;
  }

  private Runnable runnable = new Runnable() {
    @Override public void run() {
      if (currProgress < 80) {
        progressbar.setProgress(currProgress);
        currProgress++;
        mHandler.postDelayed(this, currProgress < 20 ? 40 : 2 * currProgress);
      } else {
        if (currProgress == 100) {
          AlphaAnimation animation = new AlphaAnimation(1, 0);
          animation.setDuration(250);
          animation.setAnimationListener(new Animation.AnimationListener() {
            @Override public void onAnimationEnd(Animation animation) {
              progressbar.setVisibility(View.GONE);
            }

            @Override public void onAnimationRepeat(Animation animation) {
            }

            @Override public void onAnimationStart(Animation animation) {
            }
          });
          progressbar.startAnimation(animation);
        } else {
          mHandler.postDelayed(this, 10);
        }
      }
    }
  };

  @Override public void onProgressChanged(WebView view, int newProgress) {
    Log.e("======>",newProgress + "");
    if (currProgress < 0) {
      currProgress = 0;
      if (progressbar.getVisibility() == View.GONE
          || progressbar.getVisibility() == View.INVISIBLE) {
        progressbar.setVisibility(View.VISIBLE);
      }
      mHandler.postDelayed(runnable, 200);
    }
    currProgress = 100 - (int) ((100 - currProgress) * (1.0 - newProgress / 100.0));
    progressbar.setProgress(currProgress);
    super.onProgressChanged(view, newProgress);
  }
}
