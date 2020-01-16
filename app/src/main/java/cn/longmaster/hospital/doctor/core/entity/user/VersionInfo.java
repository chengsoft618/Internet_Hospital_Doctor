package cn.longmaster.hospital.doctor.core.entity.user;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 版本更新
 * Created by Yang² on 2017/9/12.
 */

public class VersionInfo implements Serializable {
    @JsonField("pes_id")
    private int pesId;
    @JsonField("phone_os_type")
    private int phoneOsType;
    @JsonField("open_user_power")
    private int openUserPower;
    @JsonField("client_version_limit")
    private int clientVersionLimit;
    @JsonField("client_version_latest")
    private int clientVersionLatest;
    @JsonField("client_version_latest_wizard")
    private int clientVersionLatestWizard;
    @JsonField("last_upd_dt")
    private String lastUpdDt;

    public int getPesId() {
        return pesId;
    }

    public void setPesId(int pesId) {
        this.pesId = pesId;
    }

    public int getPhoneOsType() {
        return phoneOsType;
    }

    public void setPhoneOsType(int phoneOsType) {
        this.phoneOsType = phoneOsType;
    }

    public int getOpenUserPower() {
        return openUserPower;
    }

    public void setOpenUserPower(int openUserPower) {
        this.openUserPower = openUserPower;
    }

    public int getClientVersionLimit() {
        return clientVersionLimit;
    }

    public void setClientVersionLimit(int clientVersionLimit) {
        this.clientVersionLimit = clientVersionLimit;
    }

    public int getClientVersionLatest() {
        return clientVersionLatest;
    }

    public void setClientVersionLatest(int clientVersionLatest) {
        this.clientVersionLatest = clientVersionLatest;
    }

    public int getClientVersionLatestWizard() {
        return clientVersionLatestWizard;
    }

    public void setClientVersionLatestWizard(int clientVersionLatestWizard) {
        this.clientVersionLatestWizard = clientVersionLatestWizard;
    }

    public String getLastUpdDt() {
        return lastUpdDt;
    }

    public void setLastUpdDt(String lastUpdDt) {
        this.lastUpdDt = lastUpdDt;
    }

    @Override
    public String toString() {
        return "VersionInfo{" +
                "pesId=" + pesId +
                ", phoneOsType=" + phoneOsType +
                ", openUserPower=" + openUserPower +
                ", clientVersionLimit=" + clientVersionLimit +
                ", clientVersionLatest=" + clientVersionLatest +
                ", clientVersionLatestWizard=" + clientVersionLatestWizard +
                ", lastUpdDt='" + lastUpdDt + '\'' +
                '}';
    }
}
