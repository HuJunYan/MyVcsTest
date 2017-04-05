package com.tianshen.cash.manager;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.os.Process;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import com.tianshen.cash.R;
import com.tianshen.cash.net.base.UserUtil;
import com.tianshen.cash.utils.ToastUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by 14658 on 2016/7/4.
 */
public class UpdateManager {

    private Context mContext;

    private String apkUrl;//下载路径
    private String explain;//下载说明
    private String upgradeType;//下载类型(0为非强制升级，1为强制升级)

    private Dialog noticeDialog;//通知升级窗口
    private Dialog downloadDialog;//升级窗口
//    private Dialog errorDialog;//下载进度过慢窗口
    /* 下载包安装路径 */
    private static final String savePath = "/sdcard/maibei/";

    private static final String saveFileName = savePath + "MaiBaiMerchants.apk";

    /* 进度条与通知ui刷新的handler和msg常量 */
    private ProgressBar mProgress;

    private static final int DOWN_UPDATE = 1;
    private static final int DOWN_OVER = 2;
    private final int UPDATE_ERROR = 3;
    private int progress;
    private Thread downLoadThread;
    private boolean interceptFlag = false;

    private final int CHECK_PROGRESS_TIME=8000;
    private Timer errorTimer;
    private int lastCount = 0;
    private Control control;
    public UpdateManager(Context context, String apkUrl, String explain, String upgradeType) {
        this.mContext = context;
        this.apkUrl = apkUrl;
        this.explain = explain;
        this.upgradeType = upgradeType;
        this.control=(Control)context;
    }

    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case DOWN_UPDATE:
                    mProgress.setProgress(progress);
                    break;
                case DOWN_OVER:
                    installApk();
                    break;
                case UPDATE_ERROR:
                //十秒进度无变化的操作
                       /* if (!errorDialog.isShowing()) {
                            errorDialog.show();
                        }*/
                    ToastUtil.showToast(mContext,"下载速度过慢，请耐心等待");

                    break;
                default:
                    break;
            }
        }

        ;
    };


    /**
     * 禁用点击返回按钮事件
     */
    private DialogInterface.OnKeyListener onKeyListener = new DialogInterface.OnKeyListener() {
        @Override
        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0)
                return true;
            else
                return false;
        }
    };

    //外部接口让主Activity调用
    public void checkUpdateInfo() {
        showNoticeDialog();
    }

    private void showNoticeDialog() {
        if (upgradeType.equals("1")) {//强制升级
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("软件版本更新");
            builder.setMessage(explain);
            builder.setOnKeyListener(onKeyListener);
            builder.setCancelable(false);
            builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    showDownloadDialog();
                }
            });
            builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    Process.killProcess(Process.myPid());//获取PID
                    System.exit(0);//退出当前应用
                }
            });
            noticeDialog = builder.create();
            noticeDialog.show();
            UserUtil.removeUser(mContext);
        } else if (upgradeType.equals("0")) {//非强制升级
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("软件版本更新");
            builder.setMessage(explain);
            builder.setOnKeyListener(onKeyListener);
            builder.setCancelable(false);
            builder.setPositiveButton("下载", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    showDownloadDialog();
                }
            });
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    control.cancelUpdate();
                }
            });
            noticeDialog = builder.create();
            noticeDialog.show();
        }
    }

    private void showDownloadDialog() {
        if (upgradeType.equals("1")) {//强制升级
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("软件版本更新");

            final LayoutInflater inflater = LayoutInflater.from(mContext);
            View v = inflater.inflate(R.layout.progress_update, null);
            mProgress = (ProgressBar) v.findViewById(R.id.pb_loading);

            builder.setOnKeyListener(onKeyListener);
            builder.setCancelable(false);
            builder.setView(v);
            builder.setNegativeButton("退出", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    errorTimer.cancel();
                    dialog.dismiss();
                    interceptFlag = true;
                    Process.killProcess(Process.myPid());//获取PID
                    System.exit(0);//退出当前应用
                }
            });
            downloadDialog = builder.create();
            downloadDialog.show();
        }
        if (upgradeType.equals("0")) {//非强制升级
            AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
            builder.setTitle("软件版本更新");

            final LayoutInflater inflater = LayoutInflater.from(mContext);
            View v = inflater.inflate(R.layout.progress_update, null);
            mProgress = (ProgressBar) v.findViewById(R.id.pb_loading);

            builder.setOnKeyListener(onKeyListener);
            builder.setCancelable(false);
            builder.setView(v);
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    errorTimer.cancel();
                    dialog.dismiss();
                    interceptFlag = true;
                    control.cancelUpdate();
                }
            });
            downloadDialog = builder.create();
            downloadDialog.show();
        }
        downloadApk();
    }

    private Runnable mdownApkRunnable = new Runnable() {
        int count = 0;
        @Override
        public void run() {
            try {
                TimerTask timerTask = new TimerTask() {
                    @Override
                    public void run() {
                        if (lastCount == count) {
                            mHandler.sendEmptyMessage(UPDATE_ERROR);
                        } else {
                            lastCount = count;
                        }
                    }
                };
                errorTimer=new Timer(true);
                errorTimer.schedule(timerTask, CHECK_PROGRESS_TIME, CHECK_PROGRESS_TIME);
                URL url = new URL(apkUrl);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();
                int length = conn.getContentLength();
                InputStream is = conn.getInputStream();

                File file = new File(savePath);
                if (!file.exists()) {
                    file.mkdir();
                }
                String apkFile = saveFileName;
                File ApkFile = new File(apkFile);
                FileOutputStream fos = new FileOutputStream(ApkFile);

                do {
                    byte buf[] = new byte[1024];
                    int numread = is.read(buf);
                    count += numread;
                    progress = (int) (((float) count / length) * 100);
                    //更新进度
                    mHandler.sendEmptyMessage(DOWN_UPDATE);
                    if (numread <= 0) {
                        //下载完成通知安装
                        mHandler.sendEmptyMessage(DOWN_OVER);
                        break;
                    }
                    fos.write(buf, 0, numread);
                } while (!interceptFlag);//点击取消就停止下载.
                fos.close();
                is.close();
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    };

    /**
     * 下载apk
     */

    private void downloadApk() {
        downLoadThread = new Thread(mdownApkRunnable);
        downLoadThread.start();
    }

    /**
     * 安装apk
     */
    private void installApk() {
        File apkfile = new File(saveFileName);
        if (!apkfile.exists()) {
            return;
        }
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        i.setDataAndType(Uri.parse("file://" + apkfile.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(i);
        Process.killProcess(Process.myPid());

    }

public interface Control{
    public void cancelUpdate();
}
}