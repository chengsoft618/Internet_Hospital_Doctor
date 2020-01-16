package cn.longmaster.hospital.doctor.view.zxing.decode;

import android.content.Context;
import android.graphics.Bitmap;

import java.util.Hashtable;
import java.util.Vector;

import cn.longmaster.hospital.doctor.view.google.BarcodeFormat;
import cn.longmaster.hospital.doctor.view.google.BinaryBitmap;
import cn.longmaster.hospital.doctor.view.google.DecodeHintType;
import cn.longmaster.hospital.doctor.view.google.MultiFormatReader;
import cn.longmaster.hospital.doctor.view.google.NotFoundException;
import cn.longmaster.hospital.doctor.view.google.Result;
import cn.longmaster.hospital.doctor.view.google.common.HybridBinarizer;


/**
 * 从bitmap解码
 *
 * @author hugo
 */
public class BitmapDecoder {

    MultiFormatReader multiFormatReader;

    public BitmapDecoder(Context context) {

        multiFormatReader = new MultiFormatReader();

        // 解码的参数
        Hashtable<DecodeHintType, Object> hints = new Hashtable<DecodeHintType, Object>(
                2);
        // 可以解析的编码类型
        Vector<BarcodeFormat> decodeFormats = new Vector<BarcodeFormat>();
        if (decodeFormats == null || decodeFormats.isEmpty()) {
            decodeFormats = new Vector<BarcodeFormat>();

            // 这里设置可扫描的类型，我这里选择了都支持
            decodeFormats.addAll(DecodeFormatManager.ONE_D_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.QR_CODE_FORMATS);
            decodeFormats.addAll(DecodeFormatManager.DATA_MATRIX_FORMATS);
        }
        hints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);

        // 设置继续的字符编码格式为UTF8
        hints.put(DecodeHintType.CHARACTER_SET, "UTF8");

        // 设置解析配置参数
        multiFormatReader.setHints(hints);

    }

    /**
     * 获取解码结果
     *
     * @param bitmap
     * @return
     */
    public Result getRawResult(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }

        try {
            return multiFormatReader.decodeWithState(new BinaryBitmap(new HybridBinarizer(new BitmapLuminanceSource(bitmap))));
        } catch (NotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }
}