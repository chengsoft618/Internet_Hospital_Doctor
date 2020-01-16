package cn.longmaster.doctorlibrary.util.imageloader.cache;

import android.graphics.Bitmap;

import java.util.List;

/**
 * lruCache
 *
 * @param <K>
 * @param <V>
 * @author zdxing
 */
public interface BitmapLruCache {

    void put(String key, Bitmap value);

    Bitmap get(String key);

    void remove(String key);

    List<String> getAllKey();

    void clear();

    int getMaxSize();

    int getCurrentSize();
}

