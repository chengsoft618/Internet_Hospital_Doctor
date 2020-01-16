package cn.longmaster.hospital.doctor.view.zxing;

import android.app.AlertDialog;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import cn.longmaster.executor.AppExecutors;
import cn.longmaster.hospital.doctor.R;
import cn.longmaster.hospital.doctor.ui.base.NewBaseActivity;
import cn.longmaster.hospital.doctor.view.dialog.CommonDialog;
import cn.longmaster.hospital.doctor.view.google.BarcodeFormat;
import cn.longmaster.hospital.doctor.view.google.DecodeHintType;
import cn.longmaster.hospital.doctor.view.google.Result;
import cn.longmaster.hospital.doctor.view.google.client.result.ResultParser;
import cn.longmaster.hospital.doctor.view.zxing.camera.CameraManager;
import cn.longmaster.hospital.doctor.view.zxing.common.BitmapUtils;
import cn.longmaster.hospital.doctor.view.zxing.decode.BitmapDecoder;
import cn.longmaster.hospital.doctor.view.zxing.decode.CaptureActivityHandler;
import cn.longmaster.hospital.doctor.view.zxing.view.ViewfinderView;
import cn.longmaster.utils.PermissionConstants;
import cn.longmaster.utils.PermissionHelper;
import cn.longmaster.utils.Utils;
import io.reactivex.disposables.Disposable;


public class CaptureActivity extends NewBaseActivity implements SurfaceHolder.Callback, View.OnClickListener {
    private static final int REQUEST_CODE = 100;
    private static final int PARSE_BARCODE_FAIL = 300;
    private static final int PARSE_BARCODE_SUC = 200;

    private LinearLayout ll_bottom;
    private LinearLayout albumLayout;
    private TextView tv_flashlight;
    private Button capture_flashlight;
    private RelativeLayout ll_top;
    private LinearLayout top_leftLy;
    /**
     * 是否有预览
     */
    private boolean hasSurface;

    /**
     * 活动监控器。如果手机没有连接电源线，那么当相机开启后如果一直处于不被使用状态则该服务会将当前activity关闭。
     * 活动监控器全程监控扫描活跃状态，与CaptureActivity生命周期相同.每一次扫描过后都会重置该监控，即重新倒计时。
     */
//	private InactivityTimer inactivityTimer;

    /**
     * 声音震动管理器。如果扫描成功后可以播放一段音频，也可以震动提醒，可以通过配置来决定扫描成功后的行为。
     */
    private BeepManager beepManager;

    /**
     * 闪光灯调节器。自动检测环境光线强弱并决定是否开启闪光灯
     */
    private AmbientLightManager ambientLightManager;

    private CameraManager cameraManager;
    /**
     * 扫描区域
     */
    private ViewfinderView viewfinderView;

    private CaptureActivityHandler handler;

    private Result lastResult;

    private boolean isFlashlightOpen;

    /**
     * 【辅助解码的参数(用作MultiFormatReader的参数)】 编码类型，该参数告诉扫描器采用何种编码方式解码，即EAN-13，QR
     * Code等等 对应于DecodeHintType.POSSIBLE_FORMATS类型
     * 参考DecodeThread构造函数中如下代码：hints.put(DecodeHintType.POSSIBLE_FORMATS,
     * decodeFormats);
     */
    private Collection<BarcodeFormat> decodeFormats;

    /**
     * 【辅助解码的参数(用作MultiFormatReader的参数)】 该参数最终会传入MultiFormatReader，
     * 上面的decodeFormats和characterSet最终会先加入到decodeHints中 最终被设置到MultiFormatReader中
     * 参考DecodeHandler构造器中如下代码：multiFormatReader.setHints(hints);
     */
    private Map<DecodeHintType, ?> decodeHints;

    /**
     * 【辅助解码的参数(用作MultiFormatReader的参数)】 字符集，告诉扫描器该以何种字符集进行解码
     * 对应于DecodeHintType.CHARACTER_SET类型
     * 参考DecodeThread构造器如下代码：hints.put(DecodeHintType.CHARACTER_SET,
     * characterSet);
     */
    private String characterSet;

    private Result savedResultToShow;

    private IntentSource source;

    /**
     * 图片的路径
     */
    private String photoPath;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PARSE_BARCODE_SUC: // 解析图片成功
                    String result = (String) msg.obj;
                    Intent intent = getIntent();
                    intent.putExtra("result", result);
                    setResult(RESULT_OK, intent);
                    finish();
                    break;
                case PARSE_BARCODE_FAIL:// 解析图片失败
                    Toast.makeText(CaptureActivity.this, "解析图片失败", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private boolean isfisrt = false;
    SurfaceView surfaceView;
    private boolean autoEnlarged = true;//是否具有自动放大功能(功能仅仅限扫描的是二维码，条形码不放大)

    @Override
    protected void onResume() {
        super.onResume();


        // 相机初始化的动作需要开启相机并测量屏幕大小，这些操作
        // 不建议放到onCreate中，因为如果在onCreate中加上首次启动展示帮助信息的代码的 话，
        // 会导致扫描窗口的尺寸计算有误的bug
        cameraManager = new CameraManager(getApplication());

        viewfinderView = (ViewfinderView) findViewById(R.id.capture_viewfinder_view);
        viewfinderView.setCameraManager(cameraManager);

        handler = null;
        lastResult = null;

        // 摄像头预览功能必须借助SurfaceView，因此也需要在一开始对其进行初始化
        // 如果需要了解SurfaceView的原理
        // 参考:http://blog.csdn.net/luoshengyang/article/details/8661317
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still
            // exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            initCamera(surfaceHolder);

        } else {
            // 防止sdk8的设备初始化预览异常
            surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

            // Install the callback and wait for surfaceCreated() to init the
            // camera.
            surfaceHolder.addCallback(this);
        }

        // 加载声音配置，其实在BeemManager的构造器中也会调用该方法，即在onCreate的时候会调用一次
        beepManager.updatePrefs();

        // 启动闪光灯调节器
        ambientLightManager.start(cameraManager);

        // 恢复活动监控器
//		inactivityTimer.onResume();

        source = IntentSource.NONE;
        decodeFormats = null;
        characterSet = null;

        if (isfisrt) {
            initCamera(surfaceView.getHolder());

            if (handler != null) {
                handler.restartPreviewAndDecode();
            }

        }
    }

    @Override
    protected void initDatas() {
        Disposable disposable = PermissionHelper
                .init(getThisActivity())
                .addPermissionGroups(PermissionConstants.CAMERA)
                .requestEachCombined()
                .subscribe(permission -> {
                    if (!permission.granted) {
                        new CommonDialog.Builder(getThisActivity())
                                .setTitle("权限授予")
                                .setCancelable(false)
                                .setMessage("在设置-应用管理-权限中开启相机权限，才能正常使用扫描功能")
                                .setPositiveButton("取消", () -> {
                                    finish();
                                })
                                .setNegativeButton("确定", () -> {
                                    Utils.gotoAppDetailSetting();
                                    finish();
                                })
                                .show();
                    }
                });
        compositeDisposable.add(disposable);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
    }

    @Override
    protected int getContentViewId() {
        return R.layout.activity_capture_zxing;
    }

    @Override
    protected void initViews() {
        autoEnlarged = getIntent().getBooleanExtra("autoEnlarged", false);

        hasSurface = false;
//		inactivityTimer = new InactivityTimer(this);
        beepManager = new BeepManager(this);
        ambientLightManager = new AmbientLightManager(this);

//		// 监听图片识别按钮
//		findViewById(R.id.capture_scan_photo).setOnClickListener(this);
        surfaceView = findViewById(R.id.capture_preview_view); // 预览

        capture_flashlight = findViewById(R.id.capture_flashlight);
        ll_bottom = findViewById(R.id.ll_bottom);
        albumLayout = findViewById(R.id.albumLayout);
        tv_flashlight = findViewById(R.id.tv_flashlight);
        ll_top = findViewById(R.id.ll_top);
        top_leftLy = findViewById(R.id.top_leftLy);
        ll_bottom.getBackground().setAlpha(130);
        capture_flashlight.setOnClickListener(this);
        top_leftLy.setOnClickListener(this);
        ll_top.getBackground().setAlpha(130);
        albumLayout.setOnClickListener(view -> {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_PICK);
            intent.setType("image/*");
            startActivityForResult(intent, REQUEST_CODE);
        });
    }

    @Override
    protected void onPause() {

//		inactivityTimer.onPause();
        ambientLightManager.stop();
        beepManager.close();

        // 关闭摄像头
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
        super.onPause();
    }

    @Override
    protected void onDestroy() {
//		inactivityTimer.shutdown();
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_BACK:
                if ((source == IntentSource.NONE) && lastResult != null) { // 重新进行扫描
                    restartPreviewAfterDelay(0L);
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_FOCUS:
            case KeyEvent.KEYCODE_CAMERA:
                return true;
            default:
                break;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, final Intent intent) {
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQUEST_CODE:
                    // 获取选中图片的路径
                    Cursor cursor = getContentResolver().query(intent.getData(), null, null, null, null);
                    if (cursor.moveToFirst()) {
                        photoPath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
                    }
                    cursor.close();
                    Log.e(TAG, "*** path" + intent.getData() + ",photoPath:" + photoPath);
                    AppExecutors.getInstance().networkIO().execute(() -> {
                        Bitmap img = BitmapUtils.getCompressedBitmap(photoPath);
                        Log.e(TAG, "*** scanBitmap" + img);
                        BitmapDecoder decoder = new BitmapDecoder(CaptureActivity.this);
                        Result result = decoder.getRawResult(img);
                        if (result != null) {
                            Message m = mHandler.obtainMessage();
                            m.what = PARSE_BARCODE_SUC;
                            m.obj = ResultParser.parseResult(result).toString();
                            mHandler.sendMessage(m);
                        } else {
                            Message m = mHandler.obtainMessage();
                            m.what = PARSE_BARCODE_FAIL;
                            mHandler.sendMessage(m);
                        }
                    });
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG,
                    "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        hasSurface = false;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {

    }

    /**
     * 播放声音
     */
    public void playSound() {
        beepManager.playBeepSoundAndVibrate();
    }

    /**
     * A valid barcode has been found, so give an indication of success and show
     * the results.
     *
     * @param rawResult The contents of the barcode.
     */
    public void handleDecode(Result rawResult) {

        // 重新计时
//		inactivityTimer.onActivity();
        lastResult = rawResult;
        // 把图片画到扫描框
//		viewfinderView.drawResultBitmap(barcode);

        //改到playSound(int type)方法中调用
        playSound();
        String raw = rawResult.getText();
        if (!TextUtils.isEmpty(raw)) {
            Intent intent = new Intent();
            intent.putExtra("result", raw);
            setResult(RESULT_OK, intent);
            finish();
        }
        isfisrt = true;
    }

    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
        }
        resetStatusView();
    }

    public ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    public Handler getHandler() {
        return handler;
    }

    public CameraManager getCameraManager() {
        return cameraManager;
    }

    private void resetStatusView() {
        viewfinderView.setVisibility(View.VISIBLE);
        lastResult = null;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }

        if (cameraManager.isOpen()) {
            Log.w(TAG,
                    "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder);
            // Creating the handler starts the preview, which can also throw a
            // RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, decodeFormats,
                        decodeHints, characterSet, cameraManager);
            }
            decodeOrStoreSavedBitmap(null, null);
        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    /**
     * 向CaptureActivityHandler中发送消息，并展示扫描到的图像
     *
     * @param bitmap
     * @param result
     */
    private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
        // Bitmap isn't used yet -- will be used soon
        if (handler == null) {
            savedResultToShow = result;
        } else {
            if (result != null) {
                savedResultToShow = result;
            }
            if (savedResultToShow != null) {
                Message message = Message.obtain(handler,
                        R.id.decode_succeeded, savedResultToShow);
                handler.sendMessage(message);
            }
            savedResultToShow = null;
        }
    }

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name));
        builder.setMessage(getString(R.string.msg_camera_framework_bug));
        builder.setPositiveButton(R.string.sure_str, new FinishListener(this));
        builder.setOnCancelListener(new FinishListener(this));
        builder.show();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.capture_flashlight) {
            if (isFlashlightOpen) {
                cameraManager.setTorch(false); // 关闭闪光灯
                isFlashlightOpen = false;
                tv_flashlight.setText(R.string.flashlight_off);
                capture_flashlight.setBackgroundResource(R.drawable.ic_close);

            } else {
                cameraManager.setTorch(true); // 打开闪光灯
                isFlashlightOpen = true;
                tv_flashlight.setText(R.string.flashlight_on);
                capture_flashlight.setBackgroundResource(R.drawable.ic_open);
            }

        } else if (i == R.id.top_leftLy) {
            finish();


        } else {
        }

    }

    public boolean isAutoEnlarged() {
        return autoEnlarged;
    }
}