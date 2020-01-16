package cn.longmaster.doctorlibrary.viewinject;

import android.app.Activity;
import android.app.Dialog;
import android.view.View;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * IOC 注解,支持findViewById和setOnClickListener，使用方法
 * <p/>
 * <pre>
 * // 控件注解
 * &#064;FindViewById(R.id.view_inject_textview)
 * private TextView textView;
 *
 * // setOnClickListener注解
 * &#064;OnClick({ R.id.view_inject_textview, R.id.button1 })
 * private void onClick(View view) {
 * 	Toast.makeText(this, &quot;我被点击了！&quot;, Toast.LENGTH_SHORT).show();
 * }
 *
 * // 别忘了加上如下语句,有多种重载方法
 * ViewInjecter.inject(activity);
 * </pre>
 *
 * @author yangyong
 */
public class ViewInjecter {

    /**
     * 注入View和OnClickListener，通常在Fragment和Adapter中使用
     *
     * @param target 需要被注入的对象
     * @param view   view查找对象
     */
    public static void inject(Object target, View view) {
        injectView(target, ViewFinder.create(view));
        injectOnClick(target, ViewFinder.create(view));
    }

    /**
     * 注入View和OnClickListener
     */
    public static void inject(Dialog dialog) {
        injectView(dialog, ViewFinder.create(dialog));
        injectOnClick(dialog, ViewFinder.create(dialog));
    }

    /**
     * 注入View和OnClickListener
     */
    public static void inject(Activity activity) {
        injectView(activity, ViewFinder.create(activity));
        injectOnClick(activity, ViewFinder.create(activity));
    }

    /**
     * 注入View和OnClickListener, 通常在Fragment和Adapter中使用
     */
    public static void inject(Object target, ViewFinder viewFinder) {
        injectView(target, viewFinder);
        injectOnClick(target, viewFinder);
    }

    /**
     * 注入View {@link #injectView(Object, ViewFinder)}
     */
    public static void injectView(Object target, View view) {
        injectView(target, ViewFinder.create(view));
    }

    /**
     * 注入View {@link #injectView(Object, ViewFinder)}
     */
    public static void injectView(Dialog dialog) {
        injectView(dialog, ViewFinder.create(dialog));
    }

    /**
     * 注入View {@link #injectView(Object, ViewFinder)}
     */
    public static void injectView(Activity activity) {
        injectView(activity, ViewFinder.create(activity));
    }

    /**
     * 注入View
     *
     * @param target
     * @param viewFinder
     */
    public static void injectView(Object target, ViewFinder viewFinder) {
        if (target == null || viewFinder == null) {
            throw new NullPointerException();
        }
        Class<?> cls = target.getClass();
        Field[] declaredFields = cls.getDeclaredFields();
        for (Field field : declaredFields) {
            if (!field.isAccessible()) {
                field.setAccessible(true);
            }
            FindViewById findViewById = field.getAnnotation(FindViewById.class);
            if (findViewById == null) {
                continue;
            } else {
                int id = findViewById.value();
                View view = viewFinder.findViewById(id);
                if (view == null) {
                    // 根据注解的ViewID，无法查找到View， 检查控件在对应的XML中的ID是否存在
                    String msg = field.getName() + " 根据ID不能查找到对应的View，请检查XML资源文件中的id属性是否等于注解的ID";
                    throw new ViewInjectException(msg);
                } else if (!field.getType().isInstance(view)) {
                    // 控件类型不匹配， 请检查XML中的控件类型和java代码类型是否匹配
                    String msg = field.getName() + " 类型匹配错误，java类型：" + field.getType().getSimpleName()
                            + ", View在XML中申明的类型：" + view.getClass().getSimpleName() + "，请检查XML中的控件类型和java代码类型是否匹配";
                    throw new ViewInjectException(msg);
                } else {
                    try {
                        field.set(target, view);
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    } catch (IllegalArgumentException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void injectOnClick(Object target, View view) {
        injectOnClick(target, ViewFinder.create(view));
    }

    public static void injectOnClick(Dialog dialog) {
        injectOnClick(dialog, ViewFinder.create(dialog));
    }

    public static void injectOnClick(Activity activity) {
        injectOnClick(activity, ViewFinder.create(activity));
    }

    public static void injectOnClick(Object target, ViewFinder viewFinder) {
        if (target == null || viewFinder == null) {
            throw new NullPointerException();
        }
        Method[] methods = target.getClass().getDeclaredMethods();
        for (Method method : methods) {
            if (!method.isAccessible()) {
                method.setAccessible(true);
            }

            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                Class<?> viewClass = null;
                if (parameterTypes.length != 1) {
                    String msg = method.getName() + " OnClick注解方法有且只能有一个参数！";
                    throw new ViewInjectException(msg);
                } else {
                    viewClass = parameterTypes[0];
                }

                int[] ids = onClick.value();
                for (int id : ids) {
                    View targetView = viewFinder.findViewById(id);
                    if (targetView == null) {
                        // 根据注解的ViewID，无法查找到对应的View， 检查控件在对应的XML中的ID是否存在
                        String msg = method.getName() + " 根据ID不能查找到对应的View，请检查XML资源文件中的id属性是否等于注解的ID";
                        throw new ViewInjectException(msg);
                    } else if (!viewClass.isAssignableFrom(targetView.getClass())) {
                        // 控件类型和方法参数不匹配， 请检查方法参数的类型
                        String msg = method.getName() + " 方法参数类型：" + viewClass.getName() + ", View在XML中申明的类型："
                                + targetView.getClass().getName();
                        throw new ViewInjectException(msg);
                    } else {
                        targetView.setOnClickListener(new InjectOnClickListener(method, target));
                    }
                }
            }
        }
    }
}
