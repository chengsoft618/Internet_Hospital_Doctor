package cn.longmaster.hospital.doctor.core.requests;

import cn.longmaster.doctorlibrary.util.json.JsonField;

/**
 * 接口返回结果
 * Created by Yang² on 2016/7/27.
 */
public class BaseResult {
    @JsonField("op_type")
    private int opType;
    @JsonField("task_id")
    private String taskId;
    @JsonField("code")
    private int code;
    @JsonField("reson")
    private int reson;
    @JsonField("reason")
    private int reason;
    @JsonField("token")
    private String token;
    @JsonField("msg")
    private String msg;
    @JsonField("error")
    private String error;
    //分页
    @JsonField("count")
    private int count;//条数
    @JsonField("is_finish")
    private int isFinish;//是否有下一页 0：否 1：是
    @JsonField("symbol")
    private int symbol;//分页参数
    @JsonField("backlog_dt")
    private String backlogDt;//最早待办事项时间

    public int getOpType() {
        return opType;
    }

    public void setOpType(int opType) {
        this.opType = opType;
    }

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public int getReson() {
        return reson;
    }

    public void setReson(int reson) {
        this.reson = reson;
    }

    public int getReason() {
        return reason;
    }

    public void setReason(int reason) {
        this.reason = reason;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public int getIsFinish() {
        return isFinish;
    }

    public void setIsFinish(int isFinish) {
        this.isFinish = isFinish;
    }

    public int getSymbol() {
        return symbol;
    }

    public void setSymbol(int symbol) {
        this.symbol = symbol;
    }

    public String getBacklogDt() {
        return backlogDt;
    }

    public void setBacklogDt(String backlogDt) {
        this.backlogDt = backlogDt;
    }

    public boolean isFinish() {
        return isFinish == 0;
    }

    @Override
    public String toString() {
        return "BaseResult{" +
                "opType=" + opType +
                ", taskId='" + taskId + '\'' +
                ", code=" + code +
                ", reson=" + reson +
                ", reason=" + reason +
                ", token='" + token + '\'' +
                ", msg='" + msg + '\'' +
                ", count=" + count +
                ", isFinish=" + isFinish +
                ", symbol=" + symbol +
                ", backlogDt='" + backlogDt + '\'' +
                '}';
    }

}
