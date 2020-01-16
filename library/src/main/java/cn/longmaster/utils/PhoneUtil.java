package cn.longmaster.utils;

/**
 * @author ABiao_Abiao
 * @date 2019/11/19 9:50
 * @description:
 */
public class PhoneUtil {
    /**
     * Returns true if the string is a phone number.
     */
    public static boolean isPhoneNumber(String number) {
        if (StringUtils.isEmpty(number)) {
            return false;
        }
        return StringUtils.isDigitsOnly(number) && StringUtils.length(number) == 11;
    }
}
