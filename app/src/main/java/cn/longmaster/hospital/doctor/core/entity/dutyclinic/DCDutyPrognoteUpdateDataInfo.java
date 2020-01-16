package cn.longmaster.hospital.doctor.core.entity.dutyclinic;

/**
 * @author ABiao_Abiao
 * @date 2020/1/3 9:23
 * @description: 值班门诊实体类，不可更改为驼峰式命名，不然提交不了，不可用于其它破坏命名规则
 */
public class DCDutyPrognoteUpdateDataInfo {

    /**
     * type : 0
     * data_type : 2
     * data_val :
     * material_pic : 123.png
     * duration :
     */

    private int type;
    private int data_type;
    private String data_val;
    private String material_pic;
    private int duration;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getData_type() {
        return data_type;
    }

    public void setData_type(int data_type) {
        this.data_type = data_type;
    }

    public String getData_val() {
        return data_val;
    }

    public void setData_val(String data_val) {
        this.data_val = data_val;
    }

    public String getMaterial_pic() {
        return material_pic;
    }

    public void setMaterial_pic(String material_pic) {
        this.material_pic = material_pic;
    }

    public int getDuration() {
        return duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }
}
