package cn.longmaster.utils;

import android.util.SparseArray;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author Abiao
 * @version 0.01
 * @date 2018/10/15 16:21
 * @description:
 * @since Version0.01
 */
public final class LibCollections {
    /**
     * @param collection
     * @return boolean
     * @description: 集合判空
     * @author Abiao
     * @date 2018/11/21 10:57
     */
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * @param collection
     * @return boolean
     * @description: 集合判空
     * @author Abiao
     * @date 2018/11/21 10:57
     */
    public static boolean isEmpty(Map<?, ?> collection) {
        return collection == null || collection.isEmpty();
    }

    /**
     * @param collection
     * @return boolean
     * @description: 集合判非空
     * @author Abiao
     * @date 2018/11/21 10:57
     */
    public static boolean isNotEmpty(Collection<?> collection) {
        return !isEmpty(collection);
    }

    public static boolean isNotEmpty(Map<?, ?> collection) {
        return !isEmpty(collection);
    }

    /**
     * 判空
     *
     * @param sparseArray
     * @return
     */
    public static boolean isEmpty(SparseArray<?> sparseArray) {
        return null == sparseArray || sparseArray.size() == 0;
    }

    /**
     * @param collection
     * @return int
     * @description: 集合大小
     * @author Abiao
     * @date 2018/11/21 10:57
     */
    public static int size(Collection<?> collection) {
        return collection != null ? collection.size() : 0;
    }

    public static int size(Map<?, ?> collection) {
        return collection != null ? collection.size() : 0;
    }

    /**
     * 大小
     *
     * @param sparseArray
     * @return
     */
    public static int size(SparseArray<?> sparseArray) {
        return sparseArray == null ? 0 : sparseArray.size();
    }

    /**
     * @param c1
     * @param c2
     * @return boolean
     * @description: 集合是都相等
     * @author Abiao
     * @date 2018/11/21 10:58
     */
    public static boolean equals(Collection<?> c1, Collection<?> c2) {
        if (c1 != null) {
            return c1.equals(c2);
        }
        return false;
    }

    /**
     * @param c1
     * @param c2
     * @return boolean
     * @description: List
     * @author Abiao
     * @date 2018/11/21 10:58
     */
    public static boolean equals(List<?> c1, List<?> c2) {
        if (c1 != null) {
            return c1.equals(c2);
        }
        return false;
    }

    /**
     * @param c1
     * @param c2
     * @return boolean
     * @description: MAP
     * @author Abiao
     * @date 2018/11/21 10:58
     */
    public static boolean equals(Map<?, ?> c1, Map<?, ?> c2) {
        if (c1 != null) {
            return c1.equals(c2);
        }
        return false;
    }
}
