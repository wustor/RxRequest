package com.fatchao.rxrequest.utils;

import android.content.Context;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

public class HintUtils {

    /**
     * 弹出信息
     *
     * @param context
     * @param msg
     */
    public static void showToast(final Context context, final String msg) {
        // Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();
                Toast toast = Toast.makeText(context, msg, Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
                Looper.loop();
            }
        }).start();
    }
}
