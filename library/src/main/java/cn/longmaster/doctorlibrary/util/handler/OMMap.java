package cn.longmaster.doctorlibrary.util.handler;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 一对多映射关系,注意：该Map不是线程安全的
 * Created by yangyong on 2015/7/2.
 */
public class OMMap<K, V> {

    ConcurrentHashMap<K, ArrayList<V>> map = new ConcurrentHashMap<K, ArrayList<V>>();

    /**
     * 清空map
     */
    public void clear() {
        map.clear();
    }

    public boolean containsKey(K k) {
        return map.containsKey(k);
    }

    /**
     * 获取一对多的映射关系数量
     *
     * @return
     */
    public int size() {
        int size = map.size();
        return size;
    }

    /**
     * 把一个V放入到映射中,如果含有这个K,这把V放入K对应的映射中,Map的大小不变,如果不含,则生成新的映射
     *
     * @param k
     * @param v
     */
    public void put(K k, V v) {
        ArrayList<V> list = map.get(k);
        if (list == null) {
            list = new ArrayList<V>();
            map.put(k, list);
        }
        list.add(v);
    }

    /**
     * 得到这个键对应地 多个值
     *
     * @param k
     * @return
     */
    public ArrayList<V> get(K k) {
        ArrayList<V> list = map.get(k);
        return list;
    }

    /**
     * 移除这个键 对应的多个值
     *
     * @param k
     * @return
     */
    public ArrayList<V> remove(K k) {
        ArrayList<V> list = map.remove(k);
        return list;
    }

    /**
     * 移除一个映射关系
     *
     * @param k
     * @param v
     * @return 该键值对剩余的映射数量
     */
    public int remove(K k, V v) {
        int result = 0;
        ArrayList<V> list = map.get(k);
        if (list != null) {
            list.remove(v);
            result = list.size();
            if (result == 0) {
                map.remove(k);
            }
        }
        return result;
    }

}
