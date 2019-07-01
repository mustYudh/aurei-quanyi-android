package com.yu.common.toast.inner;

import android.app.Activity;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewManager;
import android.view.ViewParent;
import android.view.WindowManager;
import com.yu.common.toast.DUtil;

import java.util.Comparator;

/**
 * @Date: 2018/11/13
 * @Author: heweizong
 * @Description:
 */
class DovaTN extends Handler {
    private final static int REMOVE = 2;

    private final DPriorityQueue<DovaToast> toastQueue;//列表中成员要求非空

    private DovaTN() {
        toastQueue = new DPriorityQueue<>(new Comparator<DovaToast>() {
            @Override
            public int compare(DovaToast x, DovaToast y) {
                //往队列中add元素时，x为新增，y为原队列中元素
                // skip showing DToast
                if (y.isShowing()) return 1;
                if (x.getTimestamp() == y.getTimestamp()) return 0;
                return x.getTimestamp() < y.getTimestamp() ? -1 : 1;//值小的排队首
            }
        });
    }

    static DovaTN instance() {
        return SingletonHolder.mTn;
    }

    private static class SingletonHolder {
        private static final DovaTN mTn = new DovaTN();
    }

    /**
     * 新增Toast任务加入队列
     */
    public void add(DovaToast toast) {
        if (toast == null) return;
        DovaToast mToast = toast.clone();
        if (mToast == null) return;

        notifyNewToastComeIn(mToast);
    }

    //当前有toast在展示
    private boolean isShowing() {
        return toastQueue.size() > 0;
    }

    private void notifyNewToastComeIn(@NonNull DovaToast mToast) {
        boolean isShowing = isShowing();
        //检查有没有时间戳，没有则一定要打上时间戳
        if (mToast.getTimestamp() <= 0) {
            mToast.setTimestamp(System.currentTimeMillis());
        }
        //然后加入队列
        toastQueue.add(mToast);

        //如果有toast正在展示
        if (isShowing) {
            if (toastQueue.size() == 2) {
                //获取当前正在展示的toast
                DovaToast showing = toastQueue.peek();
                //允许新加入的toast终止当前的展示
                if (mToast.getPriority() >= showing.getPriority()) {
                    //立即终止当前正在展示toast,并开始展示下一个
                    sendRemoveMsg(showing);
                } else {
                    //do nothing ...
                    return;
                }
            } else {
                //do nothing ...
                return;
            }
        } else {
            showNextToast();
        }
    }

    private void remove(DovaToast toast) {
        toastQueue.remove(toast);
        removeInternal(toast);
    }

    void cancelAll() {
        removeMessages(REMOVE);
        if (!toastQueue.isEmpty()) {
            removeInternal(toastQueue.peek());
        }
        toastQueue.clear();
    }

    void cancelActivityToast(Activity activity) {
        if (activity == null) return;
        for (DovaToast t : toastQueue
                ) {
            if (t instanceof ActivityToast && t.getContext() == activity) {
                remove(t);
            }
        }
    }

    private void removeInternal(DovaToast toast) {
        if (toast != null && toast.isShowing()) {
            WindowManager windowManager = toast.getWMManager();
            if (windowManager != null) {
                try {
                    DUtil.log("removeInternal: removeView");
                    windowManager.removeViewImmediate(toast.getViewInternal());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            toast.isShowing = false;
        }
    }


    private void showNextToast() {
        if (toastQueue.isEmpty()) return;
        DovaToast toast = toastQueue.peek();
        if (null == toast) {
            toastQueue.poll();
            showNextToast();
        } else {
            if (toastQueue.size() > 1) {
                DovaToast next = toastQueue.get(1);
                if (next.getPriority() >= toast.getPriority()) {
                    toastQueue.remove(toast);
                    showNextToast();
                } else {
                    displayToast(toast);
                }
            } else {
                displayToast(toast);
            }
        }
    }

    private void sendRemoveMsgDelay(DovaToast toast) {
        removeMessages(REMOVE);
        Message message = obtainMessage(REMOVE);
        message.obj = toast;
        sendMessageDelayed(message, toast.getDuration());
    }

    private void sendRemoveMsg(DovaToast toast) {
        removeMessages(REMOVE);
        Message message = obtainMessage(REMOVE);
        message.obj = toast;
        sendMessage(message);
    }

    private void displayToast(@NonNull DovaToast toast) {
        WindowManager windowManager = toast.getWMManager();
        if (windowManager == null) return;
        View toastView = toast.getViewInternal();
        if (toastView == null) {
            toastQueue.remove(toast);
            showNextToast();
            return;
        }
        ViewParent parent = toastView.getParent();
        if (parent instanceof ViewManager) {
            ((ViewManager) parent).removeView(toastView);
        }
        try {
            DUtil.log("displayToast: addView");
            windowManager.addView(toastView, toast.getWMParams());

            toast.isShowing = true;
            sendRemoveMsgDelay(toast);
        } catch (Exception e) {
            if (e instanceof WindowManager.BadTokenException
                    && e.getMessage() != null
                    && (e.getMessage().contains("token null is not valid") || e.getMessage().contains("is your activity running"))) {
                if (toast instanceof ActivityToast) {
                    DovaToast.Count4BadTokenException = 0;
                } else {
                    DovaToast.Count4BadTokenException++;
                    if (toast.getContext() instanceof Activity) {
                        toastQueue.remove(toast);//从队列移除
                        removeMessages(REMOVE);//清除已发送的延时消息
                        toast.isShowing = false;//更新toast状态
                        try {
                            windowManager.removeViewImmediate(toastView);
                        } catch (Exception me) {
                            DUtil.log("windowManager removeViewImmediate error.Do not care this!");
                        }
                        new ActivityToast(toast.getContext())
                                .setTimestamp(toast.getTimestamp())
                                .setView(toastView)
                                .setDuration(toast.getDuration())
                                .setGravity(toast.getGravity(), toast.getXOffset(), toast.getYOffset())
                                .show();
                        return;
                    }
                }
            }
            e.printStackTrace();
        }
    }


    @Override
    public void handleMessage(Message message) {
        if (message == null) return;
        switch (message.what) {
            case REMOVE:
                //移除当前
                remove((DovaToast) message.obj);
                // 展示下一个Toast
                showNextToast();
                break;
            default:
                break;
        }
    }
}
