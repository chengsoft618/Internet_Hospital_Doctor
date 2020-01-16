package cn.longmaster.hospital.doctor.core.entity.consult;

import android.os.Parcel;
import android.os.Parcelable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 获取辅助资料列表
 * Created by JinKe on 2016-07-27.
 */
public class DischargedSummaryInfo implements Parcelable {
    @JsonField("file_name")
    private String fileName;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fileName);
    }

    public DischargedSummaryInfo() {
    }

    protected DischargedSummaryInfo(Parcel in) {
        this.fileName = in.readString();
    }

    public static final Parcelable.Creator<DischargedSummaryInfo> CREATOR = new Parcelable.Creator<DischargedSummaryInfo>() {
        @Override
        public DischargedSummaryInfo createFromParcel(Parcel source) {
            return new DischargedSummaryInfo(source);
        }

        @Override
        public DischargedSummaryInfo[] newArray(int size) {
            return new DischargedSummaryInfo[size];
        }
    };
}

