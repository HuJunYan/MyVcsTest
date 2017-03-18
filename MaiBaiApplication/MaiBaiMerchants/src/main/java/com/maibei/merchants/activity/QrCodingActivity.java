package com.maibei.merchants.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.maibei.merchants.R;
import com.maibei.merchants.base.BaseActivity;
import com.maibei.merchants.net.base.UserUtil;
import com.maibei.merchants.utils.LogUtil;
import com.umeng.analytics.MobclickAgent;

import java.util.Hashtable;

/**
 * Created by huxiao on 2016/6/23.
 */
public class QrCodingActivity extends BaseActivity implements View.OnClickListener {
    private int QR_WIDTH = 800;
    private int QR_HEIGHT = 800;

    private ImageView iv_my_qrcode;
    private ImageButton ib_return_home_qr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = this.getIntent().getExtras();
        createQRImage(UserUtil.getMerchantId(mContext));
    }

    @Override
    protected int setContentView() {
        return R.layout.activity_qr_code;
    }

    @Override
    protected void findViews() {
        iv_my_qrcode = (ImageView) findViewById(R.id.iv_my_qrcode);
        ib_return_home_qr = (ImageButton) findViewById(R.id.ib_return_home_qr);
    }

    @Override
    protected void setListensers() {
        ib_return_home_qr.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ib_return_home_qr){
            backActivity();
        }
    }

    public void createQRImage(String qrCode) {
        try {
            // 判断URL合法性
            if (qrCode == null || "".equals(qrCode) || qrCode.length() < 1) {
                return;
            }
            Hashtable<EncodeHintType, String> hints = new Hashtable<EncodeHintType, String>();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            // 图像数据转换，使用了矩阵转换
            BitMatrix bitMatrix = new QRCodeWriter().encode(qrCode, BarcodeFormat.QR_CODE, QR_WIDTH, QR_HEIGHT, hints);
            int[] pixels = new int[QR_WIDTH * QR_HEIGHT];
            // 下面这里按照二维码的算法，逐个生成二维码的图片，
            // 两个for循环是图片横列扫描的结果
            for (int y = 0; y < QR_HEIGHT; y++) {
                for (int x = 0; x < QR_WIDTH; x++) {
                    if (bitMatrix.get(x, y)) {
                        pixels[y * QR_WIDTH + x] = 0xff000000;
                    } else {
                        pixels[y * QR_WIDTH + x] = 0xffffffff;
                    }
                }
            }
            // 生成二维码图片的格式，使用ARGB_8888
            Bitmap bitmap = Bitmap.createBitmap(QR_WIDTH, QR_HEIGHT, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, QR_WIDTH, 0, 0, QR_WIDTH, QR_HEIGHT);
            // 显示到一个ImageView上面
            iv_my_qrcode.setImageBitmap(bitmap);
        } catch (WriterException e) {
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
            e.printStackTrace();
        }
    }
}
