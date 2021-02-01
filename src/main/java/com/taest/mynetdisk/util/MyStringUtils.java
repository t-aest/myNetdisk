package com.taest.mynetdisk.util;

import org.springframework.lang.Nullable;

/**
 * String Utils
 */
public final class MyStringUtils {

    public static boolean isEmpty(@Nullable Object str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(@Nullable Object str) {
        return !isEmpty(str);
    }
}
