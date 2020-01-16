package cn.longmaster.utils;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.lang.ref.WeakReference;
import java.lang.reflect.Array;

import io.reactivex.Observable;

/**
 * @author YH001_Abiao
 * @version 0.01
 * @date 2019/4/17 10:23
 * @description:
 * @since Version0.01
 */
public final class PermissionHelper {
    private static String TAG = PermissionHelper.class.getSimpleName();
    private static RxPermissions rxPermissions;
    private String[] permissions;

    public static PermissionHelper init(Fragment fragment) {
        return new PermissionHelper(fragment);
    }

    public static PermissionHelper init(FragmentActivity activity) {
        return new PermissionHelper(activity);
    }

    private PermissionHelper(FragmentActivity activity) {
        WeakReference<FragmentActivity> activityWeakReference = new WeakReference<>(activity);
        rxPermissions = new RxPermissions(activityWeakReference.get());
    }

    private PermissionHelper(Fragment fragment) {
        WeakReference<Fragment> fragmentWeakReference = new WeakReference<>(fragment);
        rxPermissions = new RxPermissions(fragmentWeakReference.get());
    }

    public PermissionHelper addPermissions(String... permissions) {
        this.permissions = permissions;
        return this;
    }

    public PermissionHelper addPermissionGroups(@PermissionConstants.Permission String... permissionGroups) {
        String[] requestPermission = new String[0];
        for (String permissionGroup : permissionGroups) {
            requestPermission = addAll(requestPermission, PermissionConstants.getPermissions(permissionGroup));
        }
        this.permissions = requestPermission;
        return this;
    }

    public Observable<Boolean> requst() {
        if (null == rxPermissions) {
            throw new NullPointerException("rxPermissions is not init");
        }
        return rxPermissions.request(permissions);
    }

    public Observable<Permission> requestEach() {
        if (null == rxPermissions) {
            throw new NullPointerException("rxPermissions is not init");
        }
        return rxPermissions.requestEach(permissions);
    }

    public Observable<Permission> requestEachCombined() {
        if (null == rxPermissions) {
            throw new NullPointerException("rxPermissions is not init");
        }
        return rxPermissions.requestEachCombined(permissions);
    }

    private static String[] addAll(final String[] array1, final String... array2) {
        if (array1 == null) {
            return clone(array2);
        } else if (array2 == null) {
            return clone(array1);
        }
        final Class<?> type1 = array1.getClass().getComponentType();
        @SuppressWarnings("unchecked") // OK, because array is of type T
        final String[] joinedArray = (String[]) Array.newInstance(type1, array1.length + array2.length);
        System.arraycopy(array1, 0, joinedArray, 0, array1.length);
        try {
            System.arraycopy(array2, 0, joinedArray, array1.length, array2.length);
        } catch (final ArrayStoreException ase) {
            // Check if problem was due to incompatible types
            /*
             * We do this here, rather than before the copy because:
             * - it would be a wasted check most of the time
             * - safer, in case check turns out to be too strict
             */
            final Class<?> type2 = array2.getClass().getComponentType();
            if (!type1.isAssignableFrom(type2)) {
                throw new IllegalArgumentException("Cannot store " + type2.getName() + " in an array of "
                        + type1.getName(), ase);
            }
            throw ase; // No, so rethrow original
        }
        return joinedArray;
    }

    private static <T> T[] clone(final T[] array) {
        if (array == null) {
            return null;
        }
        return array.clone();
    }
}
