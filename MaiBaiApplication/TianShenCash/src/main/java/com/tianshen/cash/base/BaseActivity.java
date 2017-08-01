package com.tianshen.cash.base;import android.app.Activity;import android.app.Dialog;import android.content.Context;import android.content.Intent;import android.os.Build;import android.os.Bundle;import android.support.v13.view.ViewCompat;import android.support.v7.app.AppCompatActivity;import android.view.Gravity;import android.view.KeyEvent;import android.view.LayoutInflater;import android.view.View;import android.view.ViewGroup;import android.view.Window;import android.view.WindowManager;import android.widget.ImageView;import android.widget.LinearLayout;import android.widget.LinearLayout.LayoutParams;import android.widget.TextView;import com.tianshen.cash.R;import com.tianshen.cash.activity.MainActivity;import com.tianshen.cash.event.DummyEvent;import com.tianshen.cash.event.FinishCurrentActivityEvent;import com.tianshen.cash.event.ServiceErrorEvent;import com.tianshen.cash.event.UpdateEvent;import com.tianshen.cash.manager.UpdateManager;import com.tianshen.cash.utils.RomUtils;import com.tianshen.cash.utils.StatusBarUtil;import com.tianshen.cash.utils.ViewUtil;import com.umeng.analytics.MobclickAgent;import org.greenrobot.eventbus.EventBus;import org.greenrobot.eventbus.Subscribe;import butterknife.ButterKnife;import butterknife.Unbinder;import cn.jpush.android.api.JPushInterface;import me.yokeyword.fragmentation.SupportActivity;public abstract class BaseActivity extends SupportActivity {    protected Context mContext;    private Unbinder mUnBinder;    @Override    protected void onCreate(Bundle savedInstanceState) {        super.onCreate(savedInstanceState);        EventBus.getDefault().register(this);        setContentView();        setContentView(R.layout.activity_base_layout);        // 更改状态栏颜色  支持5.1以上 项目需求是6.0        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {            Window window = getWindow();            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);            window.setStatusBarColor(getResources().getColor(R.color.global_bg_white));            //底部导航栏            //window.setNavigationBarColor(activity.getResources().getColor(colorResId));        }        //适配miui        if (MyApplicationLike.isMIUI) {            RomUtils.setStatusBarDarkMode(true, this);        } else if (RomUtils.FlymeSetStatusBarLightMode(getWindow(), true)) { //适配Flyme4.0以上            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.M){                StatusBarUtil.setStatusBarWhite(this);            }        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {            //设置6.0以上字体颜色为深色            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);        }        initParent();        mUnBinder = ButterKnife.bind(this);        findViews();        setListensers();        MyApplicationLike myApplicationLike = MyApplicationLike.getMyApplicationLike();        myApplicationLike.addTempActivityInBackStack(this);    }    @Override    protected void onActivityResult(int arg0, int arg1, Intent arg2) {        // TODO Auto-generated method stub        super.onActivityResult(arg0, arg1, arg2);    }    @Override    protected void onDestroy() {        super.onDestroy();        mUnBinder.unbind();        MyApplicationLike.getMyApplicationLike().removeTempActivityInBackStack(this);        EventBus.getDefault().unregister(this);        ViewUtil.cancelLoadingDialog();    }    private void initParent() {        mContext = this;        LinearLayout subCententView = (LinearLayout) this.findViewById(R.id.base_sub_activty_layout);        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,                ViewGroup.LayoutParams.MATCH_PARENT);        View centerView = View.inflate(mContext, setContentView(), null);        subCententView.addView(centerView, layoutParams);    }    protected boolean isShowNoNetworksPrompt() {        return true;    }    /**     * 跳转到某个Activity     */    protected void gotoActivity(Context mContext, Class<?> toActivityClass, Bundle bundle) {        Intent intent = new Intent(mContext, toActivityClass);        if (bundle != null) {            intent.putExtras(bundle);        }        mContext.startActivity(intent);        ((Activity) mContext).overridePendingTransition(R.anim.push_right_in, R.anim.not_exit_push_left_out);    }    /**     * 退出到某个Activity     */    protected void backActivity() {        finish();        overridePendingTransition(R.anim.not_exit_push_left_in, R.anim.push_right_out);    }    @Override    public boolean onKeyDown(int keyCode, KeyEvent event) {        // 所有需要统一处理的onKeyDown写在这个if里面        if (isOnKeyDown()) {            if (keyCode == KeyEvent.KEYCODE_BACK) {                backActivity();            }        }        return super.onKeyDown(keyCode, event);    }    protected boolean isOnKeyDown() {        return true;    }    /**     * 加载子类布局     */    protected abstract int setContentView();    /**     * 加载控件     */    protected abstract void findViews();    /**     * 设置监听     */    protected abstract void setListensers();    @Override    protected void onResume() {        super.onResume();        MobclickAgent.onResume(this);        if (JPushInterface.isPushStopped(getApplicationContext())) {            JPushInterface.resumePush(getApplicationContext());        }    }    @Override    protected void onPause() {        super.onPause();        MobclickAgent.onPause(this);//		JPushInterface.onPause(this);    }    /**     * 该方法不执行，只是让Event编译通过     */    @Subscribe    public void dummy(DummyEvent event) {    }    @Subscribe    public void onFinishCurrentActivity(FinishCurrentActivityEvent event) {        if (!(this instanceof MainActivity)) {            finish();        } else {            if (event.isMainExit) {                finish();            }        }    }    /**     * 显示系统维护的dialog     */    private void showServiceErrorDialog(String msg) {        if (isFinishing()) {            return;        }        LayoutInflater mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);        View view = mLayoutInflater.inflate(R.layout.dialog_service_error, null, false);        final Dialog mDialog = new Dialog(mContext, R.style.MyDialog);        mDialog.setContentView(view);        mDialog.setCanceledOnTouchOutside(false);        mDialog.setCancelable(false);        TextView tv_dialog_error_tips = (TextView) view.findViewById(R.id.tv_dialog_error_tips);        tv_dialog_error_tips.setText(msg);        ImageView iv_dialog_error_close = (ImageView) view.findViewById(R.id.iv_dialog_error_close);        iv_dialog_error_close.setOnClickListener(new View.OnClickListener() {            @Override            public void onClick(View v) {                mDialog.dismiss();                finish();            }        });        mDialog.show();    }    /**     * 收到了强制升级的消息     */    @Subscribe    public void onUpdateEvent(UpdateEvent event) {        String downloadUrl = event.getDownload_url();        String introduction = event.getIntroduction();        UpdateManager mUpdateManager = new UpdateManager(mContext, downloadUrl, introduction, "1", null);        mUpdateManager.checkUpdateInfo();    }    /**     * 收到了服务器维护的消息     */    @Subscribe    public void onServiceErrorEvent(ServiceErrorEvent event) {        showServiceErrorDialog(event.getMsg());    }}