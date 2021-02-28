package com.taest.mynetdisk.util;

import org.springframework.lang.Nullable;

import java.util.Collection;

public final class MyCollectionUtils {


    public static boolean isEmpty(@Nullable Collection<?> collection) {
        return (collection == null || collection.isEmpty());
    }
}
