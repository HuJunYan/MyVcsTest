package com.maibai.user.activity;

import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Vibrator;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.maibai.user.R;
import com.maibai.user.base.BaseActivity;
import com.maibai.user.qrcodescan.camera.CameraManager;
import com.maibai.user.qrcodescan.decoding.CaptureActivityHandler;
import com.maibai.user.qrcodescan.decoding.InactivityTimer;
import com.maibai.user.qrcodescan.view.ViewfinderView;
import com.maibai.user.utils.LogUtil;
import com.maibai.user.utils.PermissionUtils;
import com.umeng.analytics.MobclickAgent;

import java.io.IOException;
import java.util.Vector;

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

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		CameraManager.init(getApplication());
		isCameraPermission = true;
		hasSurface = false;
		inactivityTimer = new InactivityTimer(this);
	}

	@Override
	protected int setContentView() {
		return R.layout.twodimcode;
	}

	@Override
	protected void findViews() {
		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		tv_camera_permission = (TextView) findViewById(R.id.tv_camera_permission);
	}

	@Override
	protected void setListensers() {
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
		case R.id.tv_right:
			if (!isLightTrunOn) {
				CameraManager.get().turnLightOn();
				isLightTrunOn = true;
			} else {
				CameraManager.get().turnLightOff();
				isLightTrunOn = false;
			}
			break;
		default:
			break;
		}
	}

	private void initCamera(SurfaceHolder surfaceHolder) {
		try {
			CameraManager.get().openDriver(surfaceHolder);
		} catch (IOException ioe) {
			MobclickAgent.reportError(mContext, LogUtil.getException(ioe));
			return;
		} catch (RuntimeException e) {
			LogUtil.e("ret", "拒绝权限＝＝＝＝＝");
			// 摄像头被禁止，请打开摄像头权限
			tv_camera_permission.setText(mContext.getResources().getText(R.string.msg_camera_permission));
			isCameraPermission = false;
			MobclickAgent.reportError(mContext, LogUtil.getException(e));
			new PermissionUtils(mContext).showPermissionDialog(3);
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
		Bundle bundle = new Bundle();
		bundle.putString("merchant_id", obj.getText());
		gotoActivity(mContext, PayActivity.class, bundle);
		this.finish();
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