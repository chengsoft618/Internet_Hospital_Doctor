package cn.longmaster.hospital.doctor.core.entity.im;

import java.io.Serializable;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * Created by WangHaiKun on 2017/9/11.
 */

public class PictureFileNameInfo implements Serializable {
    @JsonField("sourceFile")//上传的图片文件名
    private String source_file;
    @JsonField("newFile")//复制后的图片文件名
    private String new_file;

    public String getSource_file() {
        return source_file;
    }

    public void setSource_file(String source_file) {
        this.source_file = source_file;
    }

    public String getNew_file() {
        return new_file;
    }

    public void setNew_file(String new_file) {
        this.new_file = new_file;
    }

    @Override
    public String toString() {
        return "PictureFileNameInfo{" +
                "source_file='" + source_file + '\'' +
                ", new_file='" + new_file + '\'' +
                '}';
    }
}
