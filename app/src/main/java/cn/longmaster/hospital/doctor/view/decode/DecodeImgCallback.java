package cn.longmaster.hospital.doctor.view.decode;


import cn.longmaster.hospital.doctor.view.google.Result;

/**
 * Created by yzq on 2017/10/18.
 * <p>
 * 解析图片的回调
 */

public interface DecodeImgCallback {
    public void onImageDecodeSuccess(Result result);

    public void onImageDecodeFailed();
}