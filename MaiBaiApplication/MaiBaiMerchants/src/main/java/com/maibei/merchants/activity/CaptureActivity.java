package com.maibei.merchants.activity;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.maibei.merchants.R;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.constant.GlobalParams;
import com.maibei.merchants.qrcodescan.camera.CameraManager;
import com.maibei.merchants.qrcodescan.decoding.CaptureActivityHandler;
import com.maibei.merchants.qrcodescan.decoding.InactivityTimer;
import com.maibei.merchants.qrcodescan.view.ViewfinderView;
import com.maibei.merchants.utils.LogUtil;
import com.maibei.merchants.utils.SharedPreferencesUtil;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.List;
import java.util.Vector;

import static android.content.Context.AUDIO_SERVICE;

public class CaptureActivity extends BaseActivity implements OnClickListener, Callback {

	private CaptureActivityHandler handler;
	private ViewfinderView viewfinderView;
	private Vector<BarcodeFormat> decodeFormats;
	private InactivityTimer inactivityTimer;
	private MediaPlayer mediaPlayer;
	private boolean hasSurface;
	private boolean playBeep;
	private boolean vibrate;
	private static final float BEEP_VOLUME = 0.10f;
	private Boolean isLightTrunOn = false;
	public static Boolean isCameraPermission;
	private TextView tv_camera_permission;
	private String characterSet;
	private String TAG = "CaptureActivity";
	private String mSweepType;
	Camera camera;
	private Bundle mBundle;
//	private TextView tv_titlebar_title;
//	private ImageButton ib_titlebar_back;
//	private TextView tv_right;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.twodimcode);
		init();
		CameraManager.init(getApplication());
		isCameraPermission = true;
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	private void init() {
//		tv_right.setVisibility(View.INVISIBLE);
//		tv_right.setBackgroundResource(R.drawable.button_flash_lamp);
//		tv_titlebar_title.setText(mContext.getResources().getString(R.string.qr_code_scanning));
		mBundle = getIntent().getExtras();
		if (mBundle == null) {
			mBundle = new Bundle();
		}
		mSweepType = mBundle.getString("sweep_type");
	}

	@Override
	protected int setContentView() {
		return R.layout.twodimcode;
	}

	@Override
	protected void findViews() {
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		tv_camera_permission = (TextView) findViewById(R.id.tv_camera_permission);
//		tv_titlebar_title = (TextView) findViewById(R.id.tv_titlebar_title);
//		ib_titlebar_back = (ImageButton) findViewById(R.id.ib_titlebar_back);
//		tv_right = (TextView) findViewById(R.id.tv_right);
	}

	@Override
	protected void setListensers() {
//		ib_titlebar_back.setOnClickListener(this);
//		tv_right.setOnClickListener(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			initCamera(surfaceHolder);
		} else {
			surfaceHolder.addCallback(this);
		}
		decodeFormats = null;
		characterSet = null;

		playBeep = false;
		AudioManager audioService = (AudioManager) getSystemService(AUDIO_SERVICE);
		if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
			playBeep = false;
		}
		initBeepSound();
		vibrate = true;
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		CameraManager.get().closeDriver();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
//		case R.id.tv_right:
//			if (!isLightTrunOn) {
//				CameraManager.get().turnLightOn();
//				isLightTrunOn = true;
//			} else {
//				CameraManager.get().turnLightOff();
//				isLightTrunOn = false;
//			}
//			break;
//		case R.id.ib_titlebar_back:
//			backActivity();
//			break;
		default:
			break;
		}
	}

	public void turnLightOn(Camera mCamera) {
		if (mCamera == null && !SharedPreferencesUtil.getInstance(mContext).getBoolean(GlobalParams.IS_CAMERA_INITED)) {
			SharedPreferencesUtil.getInstance(mContext).putBoolean(GlobalParams.IS_CAMERA_INITED, false);
			return;
		}
		Parameters parameters = mCamera.getParameters();
		if (parameters == null) {
			return;
		}
		List<String> flashModes = parameters.getSupportedFlashModes();
		// Check if camera flash exists
		if (flashModes == null) {
			// Use the screen as a flashlight (next best thing)
			return;
		}
		String flashMode = parameters.getFlashMode();
		if (!Parameters.FLASH_MODE_TORCH.equals(flashMode)) {
			// Turn on the flash
			if (flashModes.contains(Parameters.FLASH_MODE_TORCH)) {
				parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
				mCamera.setParameters(parameters);
			} else {
			}
		}
	}

	public void turnLightOff(Camera mCamera) {
		if (mCamera == null && !SharedPreferencesUtil.getInstance(mContext).getBoolean(GlobalParams.IS_CAMERA_INITED)) {
			SharedPreferencesUtil.getInstance(mContext).putBoolean(GlobalParams.IS_CAMERA_INITED, false);
			return;
		}
		Parameters parameters = mCamera.getParameters();
		if (parameters == null) {
			return;
		}
		List<String> flashModes = parameters.getSupportedFlashModes();
		String flashMode = parameters.getFlashMode();
		// Check if camera flash exists
		if (flashModes == null) {
			return;
		}
		if (!Parameters.FLASH_MODE_OFF.equals(flashMode)) {
			// Turn off the flash
			if (flashModes.contains(Parameters.FLASH_MODE_OFF)) {
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
				mCamera.setParameters(parameters);
			} else {
				Log.e(TAG, "FLASH_MODE_OFF not supported");
			}
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
//			tv_camera_permission.setText(mSweepType.equals("1") ? mContext.getResources().getText(
//					R.string.water_dispenser_qr_code) : mContext.getResources().getText(R.string.goods_qr_code));
		} catch (IOException ioe) {
			MobclickAgent.reportError(mContext, LogUtil.getException(ioe));
			return;
		} catch (RuntimeException e) {
			LogUtil.e("ret", "拒绝权限＝＝＝＝＝");
			// 摄像头被禁止，请打开摄像头权限
			tv_camera_permission.setText(mContext.getResources().getText(R.string.msg_camera_permission));
			isCameraPermission = false;
			MobclickAgent.reportError(mContext, LogUtil.getException(e));
			return;
		}
		if (handler == null) {
			handler = new CaptureActivityHandler(this, decodeFormats, characterSet);
		}
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}

	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;

	}

	public ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();

	}

	public void handleDecode(Result obj, Bitmap barcode) {
		inactivityTimer.onActivity();
		viewfinderView.drawResultBitmap(barcode);
		playBeepSoundAndVibrate();
		if (mSweepType.equals("1")) {
			Bundle bundle = new Bundle();
			bundle.putString("url", obj.getText());
//			gotoActivity(mContext, AddTimeTaskWebViewActivity.class, bundle);
			this.finish();
		} else {
			String str = obj.getText();
			System.out.println("lalalla:" + str);
			Bundle bundle = new Bundle();
			bundle.putString("result", str);
			bundle.putBoolean("isSend", false);
//			gotoActivity(mContext, BucketHandoverCompleteActivity.class, bundle);
			this.finish();
		}
	}

	private void initBeepSound() {
		if (playBeep && mediaPlayer == null) {
			setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setOnCompletionListener(beepListener);
			AssetFileDescriptor file = getResources().openRawResourceFd(R.raw.beep);
			try {
				mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getLength());
				file.close();
				mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
				mediaPlayer.prepare();
			} catch (IOException e) {
				MobclickAgent.reportError(mContext, LogUtil.getException(e));
				mediaPlayer = null;
			}
		}
	}

	private static final long VIBRATE_DURATION = 200L;

	private void playBeepSoundAndVibrate() {
		if (playBeep && mediaPlayer != null) {
			mediaPlayer.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}

	/**
	 * When the beep has finished playing, rewind to queue up another one.
	 */
	private final OnCompletionListener beepListener = new OnCompletionListener() {
		public void onCompletion(MediaPlayer mediaPlayer) {
			mediaPlayer.seekTo(0);
		}
	};

}