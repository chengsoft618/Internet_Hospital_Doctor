package cn.longmaster.hospital.doctor.core.entity.consult;

import java.io.Serializable;
import java.util.List;

import cn.longmaster.hospital.doctor.core.entity.consult.record.DrugPlanInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.HistoryInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.OperationPlanInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.PhysicalPlanInfo;

/**
 * 患者信息
 * Created by Yang² on 2016-08-3.
 */
public class PatientInfo implements Serializable {
    private PatientBaseInfo patientBaseInfo;//患者基本信息
    private List<PhysicalPlanInfo> physicalPlanList;//物理治疗
    private List<OperationPlanInfo> operationPlanList;//手术治疗
    private List<DrugPlanInfo> drugPlanList;//药物治疗
    private List<HistoryInfo> historyInfoList;//患者病史信息

    public PatientBaseInfo getPatientBaseInfo() {
        return patientBaseInfo;
    }

    public void setPatientBaseInfo(PatientBaseInfo patientBaseInfo) {
        this.patientBaseInfo = patientBaseInfo;
    }

    public List<PhysicalPlanInfo> getPhysicalPlanList() {
        return physicalPlanList;
    }

    public void setPhysicalPlanList(List<PhysicalPlanInfo> physicalPlanList) {
        this.physicalPlanList = physicalPlanList;
    }

    public List<OperationPlanInfo> getOperationPlanList() {
        return operationPlanList;
    }

    public void setOperationPlanList(List<OperationPlanInfo> operationPlanList) {
        this.operationPlanList = operationPlanList;
    }

    public List<DrugPlanInfo> getDrugPlanList() {
        return drugPlanList;
    }

    public void setDrugPlanList(List<DrugPlanInfo> drugPlanList) {
        this.drugPlanList = drugPlanList;
    }

    public List<HistoryInfo> getHistoryInfoList() {
        return historyInfoList;
    }

    public void setHistoryInfoList(List<HistoryInfo> historyInfoList) {
        this.historyInfoList = historyInfoList;
    }

    @Override
    public String toString() {
        return "PatientInfo{" +
                "patientBaseInfo=" + patientBaseInfo +
                ", physicalPlanList=" + physicalPlanList +
                ", operationPlanList=" + operationPlanList +
                ", drugPlanList=" + drugPlanList +
                ", historyInfoList=" + historyInfoList +
                '}';
    }
}

