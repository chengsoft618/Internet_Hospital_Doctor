package cn.longmaster.hospital.doctor.util;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ABiao_Abiao
 * @date 2019/10/28 11:04
 * @description: H5常量池
 */
public class WebUrlConstant {
    private static WebUrlConstant INSTANCE;
    private List<String> homePages;

    public static WebUrlConstant getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new WebUrlConstant();
        }
        return INSTANCE;
    }

    private WebUrlConstant() {
        if (homePages == null) {
            homePages = new ArrayList<>();
        }
        homePages.add("DataCollect/index");
    }

    public List<String> getHomePages() {
        return homePages;
    }
}
