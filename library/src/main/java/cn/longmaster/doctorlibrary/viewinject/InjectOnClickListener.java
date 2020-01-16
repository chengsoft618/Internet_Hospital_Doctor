package cn.longmaster.doctorlibrary.viewinject;

import android.view.View;
import android.view.View.OnClickListener;

import java.lang.reflect.Method;

/**
 * @author yangyong 2015年2月7日
 * @see OnClick
 */
public class InjectOnClickListener implements OnClickListener {
    private Method method;
    private Object target;

    public InjectOnClickListener(String methodStr, Object target) {
        super();
        try {
            this.method = target.getClass().getMethod(methodStr, View.class);
            this.target = target;
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public InjectOnClickListener(Method method, Object target) {
        super();
        this.method = method;
        this.target = target;
    }

    @Override
    public void onClick(View v) {
        try {
            method.invoke(target, v);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
