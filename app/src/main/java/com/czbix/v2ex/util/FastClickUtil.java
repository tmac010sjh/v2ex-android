package com.czbix.v2ex.util;

/**
 * Created by Jason on 2018/11/9.
 */
public class FastClickUtil {

    private static long sLastClickTime = 0;

    /**
     * 检查是否是快速双击
     */
    public static boolean isFastDoubleClick(int intervalTime){
        long time = System.currentTimeMillis();
        long timeD = time - sLastClickTime;
        if (0 < timeD && timeD < intervalTime) {
            return true;
        }
        sLastClickTime = time;
        return false;
    }
}
