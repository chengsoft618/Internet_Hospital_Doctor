package cn.longmaster.doctorlibrary.util.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * md5工具类
 * Created by yangyong on 2015/7/13.
 */
public class MD5Util {

    /**
     * 默认的密码字符串组合，用来将字节转换成 16 进制表示的字符,apache校验下载的文件的正确性用的就是默认的这个组合
     */
    protected static char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 生成字符串的md5校验值
     *
     * @param string 需要加密的字符串
     * @return 加密过后的字符串
     */
    public static String md5(String string) {
        return md5(string.getBytes());
    }

    /**
     * 生成输入流的md5校验值，注意，<b>调用该方法后应该自己关闭输入流</b>
     *
     * @return String 文件MD5
     * @throws Exception
     */
    public static String md5(InputStream inputStream) {
        if (inputStream != null) {
            try {
                MessageDigest messageDigest = MessageDigest.getInstance("MD5");
                byte[] buffer = new byte[1024];
                int numRead = 0;
                while ((numRead = inputStream.read(buffer)) > 0) {
                    messageDigest.update(buffer, 0, numRead);
                }
                return bufferToHex(messageDigest.digest());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return "";
    }

    /**
     * 生成文件的md5校验值
     *
     * @param file
     * @return md5校验值
     * @throws IOException
     */
    public static String md5(File file) {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream(file);
            return md5(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return "";
    }

    public static String md5(byte[] bytes) {
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.update(bytes);
            return bufferToHex(messageDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return "";
        }
    }

    private static String bufferToHex(byte bytes[]) {
        return bufferToHex(bytes, 0, bytes.length);
    }

    private static String bufferToHex(byte bytes[], int m, int n) {
        StringBuffer stringbuffer = new StringBuffer(2 * n);
        int k = m + n;
        for (int l = m; l < k; l++) {
            appendHexPair(bytes[l], stringbuffer);
        }
        return stringbuffer.toString();
    }

    private static void appendHexPair(byte bt, StringBuffer stringbuffer) {
        char c0 = hexDigits[(bt & 0xf0) >> 4];
        char c1 = hexDigits[bt & 0xf];
        stringbuffer.append(c0);
        stringbuffer.append(c1);
    }
}
