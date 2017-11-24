package com.tianshen.cash.utils;

import android.content.Context;
import android.os.Environment;

import com.tianshen.cash.R;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class FileUtils {

    /**
     * 判断SD卡是否存在
     *
     * @return
     * @author 王帅
     * @date 2014-1-8 上午9:42:59
     * @returnType boolean
     */
    public static boolean existSDCard() {
        boolean sdCardExist = Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED); // 判断sd卡是否存在
        return sdCardExist;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件绝对路径
     * @return true 成功
     * @author 王帅
     * @date 2014-1-8 上午9:42:02
     * @returnType boolean
     */
    public static boolean deleteFile(String filePath) {
        boolean ret = false;
        File file = new File(filePath);
        if (file.exists()) {
            ret = file.delete();
        }
        return ret;
    }

    public static File compressFile(String path, String compressPath) {
        File file = new File(path);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return file;
    }

    /**
     * 判断两个文件是否一样
     */
    public static boolean isSameFile(String fileName1, String fileName2) {

        FileInputStream fis1 = null;
        FileInputStream fis2 = null;
        try {
            fis1 = new FileInputStream(fileName1);
            fis2 = new FileInputStream(fileName2);

            int len1 = fis1.available();//返回总的字节数
            int len2 = fis2.available();

            if (len1 == len2) {//长度相同，则比较具体内容
                //建立两个字节缓冲区
                byte[] data1 = new byte[len1];
                byte[] data2 = new byte[len2];

                //分别将两个文件的内容读入缓冲区
                fis1.read(data1);
                fis2.read(data2);

                //依次比较文件中的每一个字节
                for (int i = 0; i < len1; i++) {
                    //只要有一个字节不同，两个文件就不一样
                    if (data1[i] != data2[i]) {
                        return false;
                    }
                }
                return true;
            } else {
                return false;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {//关闭文件流，防止内存泄漏
            if (fis1 != null) {
                try {
                    fis1.close();
                } catch (IOException e) {
                    //忽略
                    e.printStackTrace();
                }
            }
            if (fis2 != null) {
                try {
                    fis2.close();
                } catch (IOException e) {
                    //忽略
                    e.printStackTrace();
                }
            }
        }
        return false;
    }

    /**
     * 复制文件
     *
     * @param srcFileName  源文件名字
     * @param destFileName 目标文件名字
     * @param overlay      是否覆盖
     * @return
     */
    public static boolean copyFile(String srcFileName, String destFileName,
                                   boolean overlay) {
        File srcFile = new File(srcFileName);

        // 判断源文件是否存在
        if (!srcFile.exists()) {
            return false;
        } else if (!srcFile.isFile()) {
            return false;
        }
        // 判断目标文件是否存在
        File destFile = new File(destFileName);
        if (destFile.exists()) {
            // 如果目标文件存在并允许覆盖
            if (overlay) {
                // 删除已经存在的目标文件，无论目标文件是目录还是单个文件
                new File(destFileName).delete();
            }
        } else {
            // 如果目标文件所在目录不存在，则创建目录
            if (!destFile.getParentFile().exists()) {
                // 目标文件所在目录不存在
                if (!destFile.getParentFile().mkdirs()) {
                    // 复制文件失败：创建目标文件所在目录失败
                    return false;
                }
            }
        }

        // 复制文件
        int byteread = 0; // 读取的字节数
        InputStream in = null;
        OutputStream out = null;

        try {
            in = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            byte[] buffer = new byte[1024];

            while ((byteread = in.read(buffer)) != -1) {
                out.write(buffer, 0, byteread);
            }
            return true;
        } catch (FileNotFoundException e) {
            return false;
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (out != null)
                    out.close();
                if (in != null)
                    in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 身份认证扫脸 身份证保存图片
     *
     * @param mContext
     * @param data
     * @param type
     * @return
     */
    public static String authIdentitySaveJPGFile(Context mContext, byte[] data, int type) {
        if (data == null)
            return null;
        File mediaStorageDir = mContext.getExternalFilesDir("idCardAndLiveness");
        if (null == mediaStorageDir) {
            return null;
        }
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        BufferedOutputStream bos = null;
        FileOutputStream fos = null;
        try {
            String jpgFileName = type + ".jpg";
            fos = new FileOutputStream(mediaStorageDir + "/" + jpgFileName);
            bos = new BufferedOutputStream(fos);
            bos.write(data);
            return mediaStorageDir.getAbsolutePath() + "/" + jpgFileName;
        } catch (Exception e) {
            e.printStackTrace();
            MobclickAgent.reportError(mContext, LogUtil.getException(e));
        } finally {
            if (bos != null) {
                try {
                    bos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    MobclickAgent.reportError(mContext, LogUtil.getException(e1));
                }
            }
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                    MobclickAgent.reportError(mContext, LogUtil.getException(e1));
                }
            }
        }
        return null;
    }

    /**
     * 获取银行卡icon信息
     * @return
     */
    public static HashMap<String, Integer> getBankIconInfo() {
        HashMap<String, Integer> bankInfo = new HashMap<>();
        bankInfo.put("ABC", R.drawable.bank_abc_icon);
        bankInfo.put("CMB", R.drawable.bank_cmb_icon);
        bankInfo.put("BOB", R.drawable.bank_bob_icon);
        bankInfo.put("BOC", R.drawable.bank_boc_icon);
        bankInfo.put("BOS", R.drawable.bank_bos_icon);
        bankInfo.put("BOT", R.drawable.bank_bot_icon);
        bankInfo.put("BQD", R.drawable.bank_bqd_icon);
        bankInfo.put("CCB", R.drawable.bank_ccb_icon);
        bankInfo.put("CEB", R.drawable.bank_ceb_icon);
        bankInfo.put("CIB", R.drawable.bank_cib_icon);
        bankInfo.put("CITIC", R.drawable.bank_citic_icon);
        bankInfo.put("CMBC", R.drawable.bank_cmbc_icon);
        bankInfo.put("COMM", R.drawable.bank_comm_icon);
        bankInfo.put("CZB", R.drawable.bank_czb_icon);
        bankInfo.put("GDB", R.drawable.bank_gdb_icon);
        bankInfo.put("GZCB", R.drawable.bank_gzcb_icon);
        bankInfo.put("HUISHANG", R.drawable.bank_huishang_icon);
        bankInfo.put("HXB", R.drawable.bank_hxb_icon);
        bankInfo.put("ICBC", R.drawable.bank_icbc_icon);
        bankInfo.put("LANZHOU", R.drawable.bank_lanzhou_icon);
        bankInfo.put("NINGBO", R.drawable.bank_ningbo_icon);
        bankInfo.put("PSBC", R.drawable.bank_psbc_icon);
        bankInfo.put("SDB", R.drawable.bank_sdb_icon);
        bankInfo.put("SPAB", R.drawable.bank_spab_icon);
        bankInfo.put("SPDB", R.drawable.bank_spdb_icon);
        bankInfo.put("YHCRB", R.drawable.bank_yhrcb_icon);
        return bankInfo;
    }
}