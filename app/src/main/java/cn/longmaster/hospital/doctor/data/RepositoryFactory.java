package cn.longmaster.hospital.doctor.data;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author ABiao_Abiao
 * @date 2019/12/24 15:17
 * @description:
 */
public class RepositoryFactory {
    private Map<String, DataSource> dataSourceCache = new LinkedHashMap<>();

    public <T extends DataSource> T createRepository(Class<T> cls) {
        if (dataSourceCache.get(cls.getSimpleName()) != null) {
            return (T) dataSourceCache.get(cls.getSimpleName());
        }
        return null;
    }
}
