package com.taest.mynetdisk.util;

import cn.hutool.core.util.StrUtil;
import org.springframework.lang.Nullable;
import org.springframework.util.StringUtils;

public final class MyStringUtils {

    public static boolean isEmpty(@Nullable Object str) {
        return str == null || "".equals(str);
    }

    public static boolean isNotEmpty(@Nullable Object str) {
        return !isEmpty(str);
    }

}
