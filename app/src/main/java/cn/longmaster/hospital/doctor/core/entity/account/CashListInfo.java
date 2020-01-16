package cn.longmaster.hospital.doctor.core.entity.account;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by W·H·K on 2019/1/24.
 */
public class CashListInfo implements Serializable {
    @JsonField("income_list")
    private List<AccountListInfo> incomeList;//收入list
    @JsonField("arrear_list")
    private List<AccountListInfo> arrearList;//欠款list
    @JsonField("other_list")
    private List<AccountListInfo> otherList;//其他

    public List<AccountListInfo> getIncomeList() {
        return incomeList;
    }

    public void setIncomeList(List<AccountListInfo> incomeList) {
        this.incomeList = incomeList;
    }

    public List<AccountListInfo> getArrearList() {
        return arrearList;
    }

    public void setArrearList(List<AccountListInfo> arrearList) {
        this.arrearList = arrearList;
    }

    public List<AccountListInfo> getOtherList() {
        return otherList;
    }

    public void setOtherList(List<AccountListInfo> otherList) {
        this.otherList = otherList;
    }

    @Override
    public String toString() {
        return "CashListInfo{" +
                "incomeList=" + incomeList +
                ", arrearList=" + arrearList +
                ", otherList=" + otherList +
                '}';
    }
}
