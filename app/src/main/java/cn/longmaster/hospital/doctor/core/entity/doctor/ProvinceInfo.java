package cn.longmaster.hospital.doctor.core.entity.doctor;

import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * @author lm_Abiao
 * @date 2019/5/29 20:18
 * @description:
 */
public class ProvinceInfo {

    /**
     * province : 上海
     * city_list : [{"city":"上海"}]
     */
    @JsonField("province")
    private String province;
    @JsonField("city_list")
    private List<CityListBean> cityList;

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public List<CityListBean> getCityList() {
        return cityList;
    }

    public void setCityList(List<CityListBean> cityList) {
        this.cityList = cityList;
    }

    public static class CityListBean {
        /**
         * city : 上海
         */
        @JsonField("city")
        private String city;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }
    }
}
