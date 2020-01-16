package cn.longmaster.hospital.doctor.ui.im;

import android.support.annotation.DrawableRes;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by YY on 17/8/15.
 */

public class BottomFunctionInfo {
    public static final int ALBUM = 0;
    public static final int CAMERA = 1;
    public static final int SET_SCHEDULING = 2;
    public static final int ISSUE_ADVISE = 3;
    public static final int ADD_MATERIAL = 4;
    public static final int CALL_SERVICE = 5;
    public static final int ENTER_ROOM = 6;
    public static final int REJECT = 7;
    public static final int DOCTOR_CARD = 8;
    public static final int HISTORY_MESSAGE = 9;

    @IntDef({ALBUM, CAMERA, SET_SCHEDULING, ISSUE_ADVISE, ADD_MATERIAL, CALL_SERVICE, ENTER_ROOM, REJECT, DOCTOR_CARD, HISTORY_MESSAGE})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FunctionType {
    }

    private int functionType;
    private int icon;
    private String title;

    public BottomFunctionInfo(@FunctionType int functionType, int icon, String title) {
        setFunctionType(functionType);
        setIcon(icon);
        setTitle(title);
    }

    public int getFunctionType() {
        return functionType;
    }

    public void setFunctionType(@FunctionType int functionType) {
        this.functionType = functionType;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(@DrawableRes int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
