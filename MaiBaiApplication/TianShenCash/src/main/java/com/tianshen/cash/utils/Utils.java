package com.tianshen.cash.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.ComponentName;
import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import com.meituan.android.walle.WalleChannelReader;
import com.tianshen.cash.base.MyApplicationLike;
import com.tianshen.cash.constant.GlobalParams;
import com.tianshen.cash.model.OrderBean;
import com.tianshen.cash.model.OrderRealPayBean;
import com.tianshen.cash.net.base.UserUtil;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Utils {

    public static String SHA1MD5AndReverse(String Number) {

        String sha1number = Utils.getSHA1(Number).toLowerCase();

        StringBuffer sb = new StringBuffer(Utils.md5(sha1number));

        String key = sb.reverse().toString();
        return key;
    }

    public static String MD5SHA1AndReverse(String Number) {
//        Log.d("ret", "srcData = " + Number);
        String md5Number = Utils.md5(Number).toLowerCase();
//        Log.d("ret", "md5 = " + md5Number);
        String sha1Number = Utils.getSHA1(md5Number);
//        Log.d("ret", "sha1 = " + sha1Number);
        StringBuffer sb = new StringBuffer(sha1Number);
        String key = sb.reverse().toString();
//        Log.d("ret", "reverse = " + key);
        return key;
    }

    /**
     * 会根据手机横竖屏状态，与高度互换 According to the resolution width of the phone
     */
    public static int getWidthPixels(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    /**
     * 会根据手机横竖屏状态，与宽度互换 According to phone resolution height
     */
    public static int getHeightPixels(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    /**
     * 根据手机是否加载sd卡，配置对应缓存路径 .../mnt/sdcard/Android/data/包名/cache
     */
    public static String getCachePath(Context context) {
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())) {
            File file = context.getExternalCacheDir();
            if (file != null) {
                return file.getPath() + "/"; // /storage/emulated/0/Android/data/com.yinwei.uu.fitness/cache/
            } else {
                return Environment.getExternalStorageDirectory().getPath() + "/"; // /storage/emulated/0/
            }
        } else {
            return context.getCacheDir().getPath() + "/"; // /data/data/com.yinwei.uu.fitness/cache/
        }
    }

    /**
     * 判断程序是否重复启动
     */
    public static boolean isApplicationRepeat(Context applicationContext) {
        int pid = android.os.Process.myPid();
        String processName = null;
        ActivityManager am = (ActivityManager) applicationContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<RunningAppProcessInfo> l = am.getRunningAppProcesses();
        Iterator<RunningAppProcessInfo> i = l.iterator();
        // PackageManager pm = applicationContext.getPackageManager();
        while (i.hasNext()) {
            RunningAppProcessInfo info = (RunningAppProcessInfo) (i.next());
            try {
                if (info.pid == pid) {
                    // CharSequence c =
                    // pm.getApplicationLabel(pm.getApplicationInfo(info.processName,
                    // PackageManager.GET_META_DATA));
                    processName = info.processName;
                }
            } catch (Exception e) {
            }
        }
        if (processName == null || !processName.equalsIgnoreCase(applicationContext.getPackageName())) {
            return true;
        }
        return false;
    }

    /**
     * 将指定byte数组转换成16进制字符串
     *
     * @param b
     * @return
     */
    public static String byteToHexString(byte[] b) {
        StringBuffer hexString = new StringBuffer();
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    /**
     * 密码加密
     *
     * @param paramString
     * @return
     */
    public static String md5(String paramString) {
        String returnStr;
        try {
            MessageDigest localMessageDigest = MessageDigest.getInstance("MD5");
            localMessageDigest.update(paramString.getBytes());
            returnStr = byteToHexString(localMessageDigest.digest());
            return returnStr;
        } catch (Exception e) {
            return paramString;
        }
    }

    /**
     * 计算SHA1
     * <p/>
     * number
     *
     * @return SHA1
     */

    public static String getSHA1(String number) {
        String digest = getDigestOfString(number.getBytes());
        return digest;
    }

    private final static int[] abcde = {0x67452301, 0xefcdab89, 0x98badcfe, 0x10325476, 0xc3d2e1f0};
    // 摘要数据存储数组
    private static int[] digestInt = new int[5];
    // 计算过程中的临时数据存储数组
    private static int[] tmpData = new int[80];

    // 计算sha-1摘要
    private static int process_input_bytes(byte[] bytedata) {
        // 初试化常量
        System.arraycopy(abcde, 0, digestInt, 0, abcde.length);
        // 格式化输入字节数组，补10及长度数据
        byte[] newbyte = byteArrayFormatData(bytedata);
        // 获取数据摘要计算的数据单元个数
        int MCount = newbyte.length / 64;
        // 循环对每个数据单元进行摘要计算
        for (int pos = 0; pos < MCount; pos++) {
            // 将每个单元的数据转换成16个整型数据，并保存到tmpData的前16个数组元素中
            for (int j = 0; j < 16; j++) {
                tmpData[j] = byteArrayToInt(newbyte, (pos * 64) + (j * 4));
            }
            // 摘要计算函数
            encrypt();
        }
        return 20;
    }

    // 格式化输入字节数组格式
    private static byte[] byteArrayFormatData(byte[] bytedata) {
        // 补0数量
        int zeros = 0;
        // 补位后总位数
        int size = 0;
        // 原始数据长度
        int n = bytedata.length;
        // 模64后的剩余位数
        int m = n % 64;
        // 计算添加0的个数以及添加10后的总长度
        if (m < 56) {
            zeros = 55 - m;
            size = n - m + 64;
        } else if (m == 56) {
            zeros = 63;
            size = n + 8 + 64;
        } else {
            zeros = 63 - m + 56;
            size = (n + 64) - m + 64;
        }
        // 补位后生成的新数组内容
        byte[] newbyte = new byte[size];
        // 复制数组的前面部分
        System.arraycopy(bytedata, 0, newbyte, 0, n);
        // 获得数组Append数据元素的位置
        int l = n;
        // 补1操作
        newbyte[l++] = (byte) 0x80;
        // 补0操作
        for (int i = 0; i < zeros; i++) {
            newbyte[l++] = (byte) 0x00;
        }
        // 计算数据长度，补数据长度位共8字节，长整型
        long N = (long) n * 8;
        byte h8 = (byte) (N & 0xFF);
        byte h7 = (byte) ((N >> 8) & 0xFF);
        byte h6 = (byte) ((N >> 16) & 0xFF);
        byte h5 = (byte) ((N >> 24) & 0xFF);
        byte h4 = (byte) ((N >> 32) & 0xFF);
        byte h3 = (byte) ((N >> 40) & 0xFF);
        byte h2 = (byte) ((N >> 48) & 0xFF);
        byte h1 = (byte) (N >> 56);
        newbyte[l++] = h1;
        newbyte[l++] = h2;
        newbyte[l++] = h3;
        newbyte[l++] = h4;
        newbyte[l++] = h5;
        newbyte[l++] = h6;
        newbyte[l++] = h7;
        newbyte[l++] = h8;
        return newbyte;
    }

    private static int f1(int x, int y, int z) {
        return (x & y) | (~x & z);
    }

    private static int f2(int x, int y, int z) {
        return x ^ y ^ z;
    }

    private static int f3(int x, int y, int z) {
        return (x & y) | (x & z) | (y & z);
    }

    private static int f4(int x, int y) {
        return (x << y) | x >>> (32 - y);
    }

    // 单元摘要计算函数
    private static void encrypt() {
        for (int i = 16; i <= 79; i++) {
            tmpData[i] = f4(tmpData[i - 3] ^ tmpData[i - 8] ^ tmpData[i - 14] ^ tmpData[i - 16], 1);
        }
        int[] tmpabcde = new int[5];
        for (int i1 = 0; i1 < tmpabcde.length; i1++) {
            tmpabcde[i1] = digestInt[i1];
        }
        for (int j = 0; j <= 19; j++) {
            int tmp = f4(tmpabcde[0], 5) + f1(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + tmpData[j]
                    + 0x5a827999;
            tmpabcde[4] = tmpabcde[3];
            tmpabcde[3] = tmpabcde[2];
            tmpabcde[2] = f4(tmpabcde[1], 30);
            tmpabcde[1] = tmpabcde[0];
            tmpabcde[0] = tmp;
        }
        for (int k = 20; k <= 39; k++) {
            int tmp = f4(tmpabcde[0], 5) + f2(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + tmpData[k]
                    + 0x6ed9eba1;
            tmpabcde[4] = tmpabcde[3];
            tmpabcde[3] = tmpabcde[2];
            tmpabcde[2] = f4(tmpabcde[1], 30);
            tmpabcde[1] = tmpabcde[0];
            tmpabcde[0] = tmp;
        }
        for (int l = 40; l <= 59; l++) {
            int tmp = f4(tmpabcde[0], 5) + f3(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + tmpData[l]
                    + 0x8f1bbcdc;
            tmpabcde[4] = tmpabcde[3];
            tmpabcde[3] = tmpabcde[2];
            tmpabcde[2] = f4(tmpabcde[1], 30);
            tmpabcde[1] = tmpabcde[0];
            tmpabcde[0] = tmp;
        }
        for (int m = 60; m <= 79; m++) {
            int tmp = f4(tmpabcde[0], 5) + f2(tmpabcde[1], tmpabcde[2], tmpabcde[3]) + tmpabcde[4] + tmpData[m]
                    + 0xca62c1d6;
            tmpabcde[4] = tmpabcde[3];
            tmpabcde[3] = tmpabcde[2];
            tmpabcde[2] = f4(tmpabcde[1], 30);
            tmpabcde[1] = tmpabcde[0];
            tmpabcde[0] = tmp;
        }
        for (int i2 = 0; i2 < tmpabcde.length; i2++) {
            digestInt[i2] = digestInt[i2] + tmpabcde[i2];
        }
        for (int n = 0; n < tmpData.length; n++) {
            tmpData[n] = 0;
        }
    }

    // 4字节数组转换为整数
    private static int byteArrayToInt(byte[] bytedata, int i) {
        return ((bytedata[i] & 0xff) << 24) | ((bytedata[i + 1] & 0xff) << 16) | ((bytedata[i + 2] & 0xff) << 8)
                | (bytedata[i + 3] & 0xff);
    }

    // 整数转换为4字节数组
    private static void intToByteArray(int intValue, byte[] byteData, int i) {
        byteData[i] = (byte) (intValue >>> 24);
        byteData[i + 1] = (byte) (intValue >>> 16);
        byteData[i + 2] = (byte) (intValue >>> 8);
        byteData[i + 3] = (byte) intValue;
    }

    // 将字节转换为十六进制字符串
    private static String byteToHexString(byte ib) {
        char[] Digit = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        char[] ob = new char[2];
        ob[0] = Digit[(ib >>> 4) & 0X0F];
        ob[1] = Digit[ib & 0X0F];
        String s = new String(ob);
        return s;
    }

    // 将字节数组转换为十六进制字符串
    private static String byteArrayToHexString(byte[] bytearray) {
        String strDigest = "";
        for (int i = 0; i < bytearray.length; i++) {
            strDigest += byteToHexString(bytearray[i]);
        }
        return strDigest;
    }

    // 转化十六进制编码为字符串
    public static String toStringHex(String s) {
        byte[] baKeyword = new byte[s.length() / 2];
        for (int i = 0; i < baKeyword.length; i++) {
            try {
                baKeyword[i] = (byte) (0xff & Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            s = new String(baKeyword, "utf-8");// UTF-16le:Not
        } catch (Exception e1) {
            e1.printStackTrace();
        }
        return s;
    }


    // 计算sha-1摘要，返回相应的字节数组
    public static byte[] getDigestOfBytes(byte[] byteData) {
        process_input_bytes(byteData);
        byte[] digest = new byte[20];
        for (int i = 0; i < digestInt.length; i++) {
            intToByteArray(digestInt[i], digest, i * 4);
        }
        return digest;
    }

    // 计算sha-1摘要，返回相应的十六进制字符串
    public static String getDigestOfString(byte[] byteData) {
        return byteArrayToHexString(getDigestOfBytes(byteData));
    }


    /**
     * 根据byte数组，生成图片
     */
    public static String saveJPGFile(Context mContext, byte[] data, String key) {
        if (data == null)
            return null;

        File mediaStorageDir = mContext
                .getExternalFilesDir("MaiBei_image");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            String jpgFileName = System.currentTimeMillis() + ""
                    + new Random().nextInt(1000000) + "_" + key + ".jpg";
            fos = new FileOutputStream(mediaStorageDir + "/" + jpgFileName);
            bos = new BufferedOutputStream(fos);
            bos.write(data);
            return mediaStorageDir.getAbsolutePath() + "/" + jpgFileName;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return null;
    }

    public static void delJPGFile(Context mContext) {
        File mediaStorageDir = mContext
                .getExternalFilesDir("MaiBei_image");

        if (mediaStorageDir != null && mediaStorageDir.exists()) {
            mediaStorageDir.delete();
        }
    }

    public static int getAmountType(Context mContext) {
        int type = 0;
        String amountStr = UserUtil.getCustomerAmount(mContext);
        Long amountLong = Long.valueOf(amountStr, 10);
        if (amountLong == GlobalParams.MONEY_LEVER_1ST) {
            type = GlobalParams.AMOUNT_TYPE_0;
        } else if (amountLong == GlobalParams.MONEY_LEVER_2ND) {
            type = GlobalParams.AMOUNT_TYPE_200;
        } else if (amountLong == GlobalParams.MONEY_LEVER_3RD) {
            type = GlobalParams.AMOUNT_TYPE_500;
        } else {
            type = GlobalParams.AMOUNT_TYPE_500_PLUS;
        }
        return type;
    }

    /**
     * 原消费额度+现消费额度-优惠额度 <= 500
     *
     * @param currentConsume
     * @param discount
     * @return
     */
    public static int getConsumeLever(Context mContext, long currentConsume, long discount) {
        int type = 0;
        long amount = Long.valueOf(UserUtil.getCustomerAmount(mContext), 10);
        long balanceAmount = Long.valueOf(UserUtil.getBalanceAmount(mContext), 10);
        long totalConsume = amount - balanceAmount + currentConsume - discount;
        if (totalConsume == GlobalParams.MONEY_LEVER_1ST) {
            type = GlobalParams.CONSUME_TYPE_0;
        } else if (totalConsume <= GlobalParams.MONEY_LEVER_2ND) {
            type = GlobalParams.CONSUME_TYPE_LESS_200;
        } else if (totalConsume <= GlobalParams.MONEY_LEVER_3RD) {
            type = GlobalParams.CONSUME_TYPE_LESS_500;
        } else { // 各种情况下，消费金额大于500时
            type = GlobalParams.CONSUME_TYPE_500_PLUS;
        }
        return type;
    }

    public static boolean isCurMoneyEnoughToConsume(Context mContext, long currentConsume, long discount) {
        long amount = Long.valueOf(UserUtil.getCustomerAmount(mContext), 10);
        long balanceAmount = Long.valueOf(UserUtil.getBalanceAmount(mContext), 10);
        long totalConsume = balanceAmount - currentConsume + discount;
        if (totalConsume >= 0) {
            return true;
        } else {
            return false;
        }
    }

//    private static int getTypeWhenTotalConsumeMore500(Context mContext, int type, long amount, long totalConsume) {
//        int amountType = getAmountType(mContext);
//        switch (amountType) {
//            case GlobalParams.AMOUNT_TYPE_0:
//                if (totalConsume <= GlobalParams.MONEY_LEVER_4TH) {
//                    type = GlobalParams.CONSUME_TYPE_0_TO_2000_MINUS;
//                } else {
//                    type = GlobalParams.CONSUME_TYPE_0_TO_2000_PLUS;
//                }
//                break;
//            case GlobalParams.AMOUNT_TYPE_200:
//                if (totalConsume <= GlobalParams.MONEY_LEVER_4TH) {
//                    type = GlobalParams.CONSUME_TYPE_200_TO_2000_MINUS;
//                } else {
//                    type = GlobalParams.CONSUME_TYPE_200_TO_2000_PLUS;
//                }
//                break;
//            case GlobalParams.AMOUNT_TYPE_500:
//                if (totalConsume <= GlobalParams.MONEY_LEVER_4TH) {
//                    type = GlobalParams.CONSUME_TYPE_500_TO_2000_MINUS;
//                } else {
//                    type = GlobalParams.CONSUME_TYPE_500_TO_2000_PLUS;
//                }
//                break;
//            case GlobalParams.AMOUNT_TYPE_500_TO_2000:
//                if (totalConsume <= amount) {
//                    type = GlobalParams.CONSUME_TYPE_LESS_2000_ENOUGH;
//                } else if (totalConsume <= GlobalParams.MONEY_LEVER_4TH) {
//                    type = GlobalParams.CONSUME_TYPE_LESS_2000_TO_2000_MINUS;
//                } else if (totalConsume > GlobalParams.MONEY_LEVER_4TH) {
//                    type = GlobalParams.CONSUME_TYPE_LESS_2000_TO_2000_PLUS;
//                }
//                break;
//            case GlobalParams.AMOUNT_TYPE_2000_PLUS:
//                if (totalConsume <= amount) {
//                    type = GlobalParams.CONSUME_TYPE_MORE_2000_ENOUGH;
//                } else {
//                    type = GlobalParams.CONSUME_TYPE_MORE_2000_NOT_ENOUGH;
//                }
//                break;
//        }
//        return type;
//    }

    public static boolean isCustomerAmountGreaterOrEqual500(Context mContext) {
        String amountStr = UserUtil.getCustomerAmount(mContext);
        Long amountLong = Long.valueOf(amountStr, 10);
        if (amountLong >= GlobalParams.MONEY_LEVER_3RD) {
            return true;
        } else {
            return false;
        }
    }

    public static String getTopActivityName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ComponentName cn = am.getRunningTasks(1).get(0).topActivity;
        return cn.getClassName();
    }

    public static OrderBean orderRealPayBean2OrderBean(OrderRealPayBean paramT) {
        OrderBean orderBean = new OrderBean();
        orderBean.setCode(paramT.getCode());
        orderBean.setMsg(paramT.getMsg());
        orderBean.getData().setReal_pay(paramT.getData().getReal_pay());
        orderBean.getData().setAmount((paramT.getData().getAmount()));
        orderBean.getData().setBalance_amount(paramT.getData().getBalance_amount());
        orderBean.getData().setRepay_type(paramT.getData().getRepay_type());
        orderBean.getData().setRepay_date(paramT.getData().getRepay_date());
        orderBean.getData().setRepay_times(paramT.getData().getRepay_times());
        orderBean.getData().setRepay_principal(paramT.getData().getRepay_principal());
        orderBean.getData().setRepay_interest(paramT.getData().getRepay_interest());
        orderBean.getData().setRepay_total(paramT.getData().getRepay_total());
        orderBean.getData().setCredit(paramT.getData().getCredit());
        orderBean.getData().setMax_amount(paramT.getData().getMax_amount());
        orderBean.getData().setDiscount(paramT.getData().getDiscount());
        orderBean.getData().setDown_payment(paramT.getData().getDown_payment());
        return orderBean;
    }


    /**
     * 计算 传入的时间 和当前时间 是否是同一天ts 上次时间的毫秒值
     */
    public static boolean isSameDay(long ts) {
        if (ts == 0) {
            return false;
        }
        Calendar calender = Calendar.getInstance();
        calender.setTime(new Date());//设置当前时间
        int yearNow = calender.get(Calendar.YEAR);//现在的年份
        int dayNow = calender.get(Calendar.DAY_OF_YEAR);//消息在这一年的day

        calender.setTime(new Date(ts));//设置传入的时间
        int yearMsg = calender.get(Calendar.YEAR);//消息的年份
        int dayMsg = calender.get(Calendar.DAY_OF_YEAR);//消息的day
        if (yearNow == yearMsg && dayNow == dayMsg) {//说明是同天
            return true;
        }
        return false;
    }

    /**
     * 获取渠道号  默认2000 -> server
     * @return
     */
    public static String getChannelId() {
        String channel_id = WalleChannelReader.getChannel(MyApplicationLike.getsApplication());
        LogUtil.d("abc", "channel_id--->" + channel_id);
        if (TextUtils.isEmpty(channel_id)) {
            channel_id = "2000";
        }
        return channel_id;
    }
}
