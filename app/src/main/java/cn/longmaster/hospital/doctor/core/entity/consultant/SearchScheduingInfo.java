package cn.longmaster.hospital.doctor.core.entity.consultant;

import java.io.Serializable;

/**
 * Created by W·H·K on 2018/1/8.
 */

public class SearchScheduingInfo implements Serializable {
    private String keyWord;
    private String searchType;

    public String getKeyWord() {
        return keyWord;
    }

    public void setKeyWord(String keyWord) {
        this.keyWord = keyWord;
    }

    public String getSearchType() {
        return searchType;
    }

    public void setSearchType(String searchType) {
        this.searchType = searchType;
    }

    @Override
    public String toString() {
        return "SearchScheduingInfo{" +
                "keyWord='" + keyWord + '\'' +
                ", searchType=" + searchType +
                '}';
    }
}
