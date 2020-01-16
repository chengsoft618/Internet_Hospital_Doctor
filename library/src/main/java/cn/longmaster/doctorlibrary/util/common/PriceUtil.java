package cn.longmaster.doctorlibrary.util.common;

/**
 * 价格工具
 * Created by LJ.cao on 2016/2/24.
 */
public class PriceUtil {

    public static String fixDot0(String price) {
        String p = price;
        boolean isDecimal = p.matches("[0-9]+\\.[0-9]*");
        if (isDecimal) {
            for (int i = p.length() - 1; i >= 0; i--) {
                String tail = p.charAt(i) + "";
                if ("0".equals(tail)) {
                    p = p.substring(0, p.length() - 1);
                } else if (".".equals(tail)) {
                    p = p.substring(0, p.length() - 1);
                    break;
                } else {
                    break;
                }
            }
        }
        return p;
    }

    public static double fixDot0(double price) {
        return Double.parseDouble(fixDot0(price + ""));
    }

    public static float fixDot0(float price) {
        return Float.parseFloat(fixDot0(price + ""));
    }

    public static long fixDot0(long price) {
        return Long.parseLong(fixDot0(price + ""));
    }

    public static int fixDot0(int price) {
        return Integer.parseInt(fixDot0(price + ""));
    }

    /**
     * 截取至两位小数，最小精度至分
     *
     * @param price 价格
     * @return
     */
    public static String fixPrecision2Cent(String price) {
        double p = Double.parseDouble(price);
        return String.format("%.2f", p);
    }

    /**
     * 使用java正则表达式去掉多余的.与0
     *
     * @param s
     * @return
     */
    public static String subZeroAndDot(String s) {
        if (s.indexOf(".") > 0) {
            s = s.replaceAll("0+?$", "");//去掉多余的0
            s = s.replaceAll("[.]$", "");//如最后一位是.则去掉
        }
        return s;
    }
}
