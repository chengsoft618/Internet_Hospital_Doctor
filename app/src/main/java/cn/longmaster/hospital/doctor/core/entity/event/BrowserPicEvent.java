package cn.longmaster.hospital.doctor.core.entity.event;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

import cn.longmaster.hospital.doctor.core.entity.consult.AuxiliaryMaterialInfo;
import cn.longmaster.hospital.doctor.core.entity.consult.record.DiagnosisFileInfo;
import cn.longmaster.hospital.doctor.core.upload.SingleFileInfo;

/**
 * @author ABiao_Abiao
 * @date 2019/7/18 17:31
 * @description:
 */
public class BrowserPicEvent implements Parcelable {
    private int index;
    private int appointInfoId;
    private boolean isAssistant;
    private boolean isRounds;
    private boolean isDiagnosis;
    private boolean isPass;
    private List<String> serverFilePaths;
    private SingleFileInfo singleFileInfo;
    private List<SingleFileInfo> singleFileInfos;
    private List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos;
    private DiagnosisFileInfo diagnosisFileInfo;
    //1:出院小结0其他
    private int imageType;

    public boolean isAssistant() {
        return isAssistant;
    }

    public void setAssistant(boolean assistant) {
        isAssistant = assistant;
    }

    public boolean isRounds() {
        return isRounds;
    }

    public void setRounds(boolean rounds) {
        isRounds = rounds;
    }

    public boolean isDiagnosis() {
        return isDiagnosis;
    }

    public void setDiagnosis(boolean diagnosis) {
        isDiagnosis = diagnosis;
    }

    public boolean isPass() {
        return isPass;
    }

    public void setPass(boolean pass) {
        isPass = pass;
    }

    public List<String> getServerFilePaths() {
        return serverFilePaths;
    }

    public void setServerFilePaths(List<String> serverFilePaths) {
        this.serverFilePaths = serverFilePaths;
    }

    public int getIndex() {
        return index <= 0 ? 0 : index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public int getAppointInfoId() {
        return appointInfoId;
    }

    public void setAppointInfoId(int appointInfoId) {
        this.appointInfoId = appointInfoId;
    }

    public SingleFileInfo getSingleFileInfo() {
        return singleFileInfo;
    }

    public void setSingleFileInfo(SingleFileInfo singleFileInfo) {
        this.singleFileInfo = singleFileInfo;
    }

    public List<SingleFileInfo> getSingleFileInfos() {
        return singleFileInfos;
    }

    public void setSingleFileInfos(List<SingleFileInfo> singleFileInfos) {
        this.singleFileInfos = singleFileInfos;
    }

    public List<AuxiliaryMaterialInfo> getAuxiliaryMaterialInfos() {
        return auxiliaryMaterialInfos;
    }

    public void setAuxiliaryMaterialInfos(List<AuxiliaryMaterialInfo> auxiliaryMaterialInfos) {
        this.auxiliaryMaterialInfos = auxiliaryMaterialInfos;
    }

    public DiagnosisFileInfo getDiagnosisFileInfo() {
        return diagnosisFileInfo;
    }

    public void setDiagnosisFileInfo(DiagnosisFileInfo diagnosisFileInfo) {
        this.diagnosisFileInfo = diagnosisFileInfo;
    }

    public int getImageType() {
        return imageType;
    }

    public void setImageType(int imageType) {
        this.imageType = imageType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.index);
        dest.writeInt(this.appointInfoId);
        dest.writeByte(this.isAssistant ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isRounds ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isDiagnosis ? (byte) 1 : (byte) 0);
        dest.writeByte(this.isPass ? (byte) 1 : (byte) 0);
        dest.writeStringList(this.serverFilePaths);
        dest.writeSerializable(this.singleFileInfo);
        dest.writeList(this.singleFileInfos);
        dest.writeList(this.auxiliaryMaterialInfos);
        dest.writeSerializable(this.diagnosisFileInfo);
        dest.writeInt(this.imageType);
    }

    public BrowserPicEvent() {
    }

    protected BrowserPicEvent(Parcel in) {
        this.index = in.readInt();
        this.appointInfoId = in.readInt();
        this.isAssistant = in.readByte() != 0;
        this.isRounds = in.readByte() != 0;
        this.isDiagnosis = in.readByte() != 0;
        this.isPass = in.readByte() != 0;
        this.serverFilePaths = in.createStringArrayList();
        this.singleFileInfo = (SingleFileInfo) in.readSerializable();
        this.singleFileInfos = new ArrayList<SingleFileInfo>();
        in.readList(this.singleFileInfos, SingleFileInfo.class.getClassLoader());
        this.auxiliaryMaterialInfos = new ArrayList<AuxiliaryMaterialInfo>();
        in.readList(this.auxiliaryMaterialInfos, AuxiliaryMaterialInfo.class.getClassLoader());
        this.diagnosisFileInfo = (DiagnosisFileInfo) in.readSerializable();
        this.imageType = in.readInt();
    }

    public static final Parcelable.Creator<BrowserPicEvent> CREATOR = new Parcelable.Creator<BrowserPicEvent>() {
        @Override
        public BrowserPicEvent createFromParcel(Parcel source) {
            return new BrowserPicEvent(source);
        }

        @Override
        public BrowserPicEvent[] newArray(int size) {
            return new BrowserPicEvent[size];
        }
    };
}
