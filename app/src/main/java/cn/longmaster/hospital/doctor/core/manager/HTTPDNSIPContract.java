package cn.longmaster.hospital.doctor.core.manager;

import android.provider.BaseColumns;

/**
 * Created by W·H·K on 2018/6/13.
 */

public class HTTPDNSIPContract {
    private HTTPDNSIPContract() {
    }

    public static abstract class HTTPDNSEntry implements BaseColumns {
        public static final String TABLE_NAME = "t_domainname_url_cfg";//表名
        public static final String ID = "_id";//
        public static final String TYPE = "type";//域名类型
        public static final String URL = "url";//url
        public static final String IP = "ip";//ip

    }
}
