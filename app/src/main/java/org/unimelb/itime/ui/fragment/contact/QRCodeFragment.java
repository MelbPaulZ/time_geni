package org.unimelb.itime.ui.fragment.contact;

import android.support.v4.app.Fragment;
import android.view.SurfaceHolder;

/**
 * Created by 37925 on 2016/12/18.
 */

public class QRCodeFragment extends Fragment implements SurfaceHolder.Callback{
    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {

    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {

    }
//    private static final String TAG = CaptureActivityContact.class.getSimpleName();
//    private CameraManager cameraManager;
//    private CaptureActivityHandler handler;
//    private InactivityTimer inactivityTimer;
//    private BeepManager beepManager;
//
//    private SurfaceView scanPreview = null;
//    private RelativeLayout scanContainer;
//    private RelativeLayout scanCropView;
//    private ImageView scanLine;
//
//    private Rect mCropRect = null;
//    private boolean isHasSurface = false;
//    private QrCodeScanBinding binding;
//    public Handler getHandler() {
//        return handler;
//    }
//
//    public CameraManager getCameraManager() {
//        return cameraManager;
//    }
//
//
//    public View onCreateView(LayoutInflater inflater,
//                             ViewGroup container, Bundle savedInstanceState) {
//        Window window = getActivity().getWindow();
//        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
//
//        binding = DataBindingUtil.inflate(inflater, R.layout.activity_qr_code_scan, container, false);
//        scanPreview = binding.capturePreview;
//        scanContainer = binding.captureContainer;
//        scanCropView = binding.captureCropView;
//        scanLine = binding.captureScanLine;
//
//        inactivityTimer = new InactivityTimer(getActivity());
//        beepManager = new BeepManager(getActivity());
//
//        TranslateAnimation animation = new TranslateAnimation(Animation.RELATIVE_TO_PARENT, 0.0f, Animation
//                .RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT, 0.0f, Animation.RELATIVE_TO_PARENT,
//                0.9f);
//        animation.setDuration(4500);
//        animation.setRepeatCount(-1);
//        animation.setRepeatMode(Animation.RESTART);
//        scanLine.startAnimation(animation);
//        return binding.getRoot();
//    }
//
//    @Override
//    public void onResume() {
//        super.onResume();
//
//        // CameraManager must be initialized here, not in onCreate(). This is
//        // necessary because we don't
//        // want to open the camera driver and measure the screen size if we're
//        // going to show the help on
//        // first launch. That led to bugs where the scanning rectangle was the
//        // wrong size and partially
//        // off screen.
//        cameraManager = new CameraManager(getActivity());
//
//        handler = null;
//
//        if (isHasSurface) {
//            // The activity was paused but not stopped, so the surface still
//            // exists. Therefore
//            // surfaceCreated() won't be called, so init the camera here.
//            initCamera(scanPreview.getHolder());
//        } else {
//            // Install the callback and wait for surfaceCreated() to init the
//            // camera.
//            scanPreview.getHolder().addCallback(this);
//        }
//
//        inactivityTimer.onResume();
//    }
//
//    @Override
//    public void onPause() {
//        if (handler != null) {
//            handler.quitSynchronously();
//            handler = null;
//        }
//        inactivityTimer.onPause();
//        beepManager.close();
//        cameraManager.closeDriver();
//        if (!isHasSurface) {
//            scanPreview.getHolder().removeCallback(this);
//        }
//        super.onPause();
//    }
//
//    @Override
//    public void onDestroy() {
//        inactivityTimer.shutdown();
//        super.onDestroy();
//    }
//
//    @Override
//    public void surfaceCreated(SurfaceHolder holder) {
//        if (holder == null) {
//            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
//        }
//        if (!isHasSurface) {
//            isHasSurface = true;
//            initCamera(holder);
//        }
//    }
//
//    @Override
//    public void surfaceDestroyed(SurfaceHolder holder) {
//        isHasSurface = false;
//    }
//
//    @Override
//    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
//
//    }
//
//    /**
//     * A valid barcode has been found, so give an indication of success and show
//     * the results.
//     *
//     * @param rawResult The contents of the barcode.
//     * @param bundle    The extras
//     */
//    public void handleDecode(Result rawResult, Bundle bundle) {
//        inactivityTimer.onActivity();
//        beepManager.playBeepSoundAndVibrate();
//
//        Intent resultIntent = new Intent();
//        bundle.putInt("width", mCropRect.width());
//        bundle.putInt("height", mCropRect.height());
//        bundle.putString("result", rawResult.getText());
//        resultIntent.putExtras(bundle);
//        this.getActivity().setResult(RESULT_OK, resultIntent);
//        this.getActivity().finish();
//    }
//
//    private void initCamera(SurfaceHolder surfaceHolder) {
//        if (surfaceHolder == null) {
//            throw new IllegalStateException("No SurfaceHolder provided");
//        }
//        if (cameraManager.isOpen()) {
//            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
//            return;
//        }
//        try {
//            cameraManager.openDriver(surfaceHolder);
//            // Creating the handler starts the preview, which can also throw a
//            // RuntimeException.
//            if (handler == null) {
//                handler = new CaptureActivityHandler((CaptureActivityContact) this.getActivity(), cameraManager, DecodeThread.ALL_MODE);
//            }
//
//            initCrop();
//        } catch (IOException ioe) {
//            Log.w(TAG, ioe);
//            displayFrameworkBugMessageAndExit();
//        } catch (RuntimeException e) {
//            // Barcode Scanner has seen crashes in the wild of this variety:
//            // java.?lang.?RuntimeException: Fail to connect to camera service
//            Log.w(TAG, "Unexpected error initializing camera", e);
//            displayFrameworkBugMessageAndExit();
//        }
//    }
//
//    private void displayFrameworkBugMessageAndExit() {
//        // camera error
//        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//        builder.setMessage("Camera error");
//        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
//
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                getActivity().onBackPressed();
//            }
//
//        });
//        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
//
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                getActivity().onBackPressed();
//            }
//        });
//        builder.show();
//    }
//
//    public void restartPreviewAfterDelay(long delayMS) {
//        if (handler != null) {
//            handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
//        }
//    }
//
//    public Rect getCropRect() {
//        return mCropRect;
//    }
//
//    /**
//     * 初始化截取的矩形区域
//     */
//    private void initCrop() {
//        int cameraWidth = cameraManager.getCameraResolution().y;
//        int cameraHeight = cameraManager.getCameraResolution().x;
//
//        /** 获取布局中扫描框的位置信息 */
//        int[] location = new int[2];
//        scanCropView.getLocationInWindow(location);
//
//        int cropLeft = location[0];
//        int cropTop = location[1] - getStatusBarHeight();
//
//        int cropWidth = scanCropView.getWidth();
//        int cropHeight = scanCropView.getHeight();
//
//        /** 获取布局容器的宽高 */
//        int containerWidth = scanContainer.getWidth();
//        int containerHeight = scanContainer.getHeight();
//
//        /** 计算最终截取的矩形的左上角顶点x坐标 */
//        int x = cropLeft * cameraWidth / containerWidth;
//        /** 计算最终截取的矩形的左上角顶点y坐标 */
//        int y = cropTop * cameraHeight / containerHeight;
//
//        /** 计算最终截取的矩形的宽度 */
//        int width = cropWidth * cameraWidth / containerWidth;
//        /** 计算最终截取的矩形的高度 */
//        int height = cropHeight * cameraHeight / containerHeight;
//
//        /** 生成最终的截取的矩形 */
//        mCropRect = new Rect(x, y, width + x, height + y);
//    }
//
//    private int getStatusBarHeight() {
//        try {
//            Class<?> c = Class.forName("com.android.internal.R$dimen");
//            Object obj = c.newInstance();
//            Field field = c.getField("status_bar_height");
//            int x = Integer.parseInt(field.get(obj).toString());
//            return getResources().getDimensionPixelSize(x);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return 0;
//    }
}
