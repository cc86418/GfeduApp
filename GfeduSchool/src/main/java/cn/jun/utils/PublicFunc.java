package cn.jun.utils;

import android.os.Environment;
import android.os.StatFs;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class PublicFunc {
    public final static String MD5_KEY = "GfeduAPISecretKey";

    public static void clearArrayList(ArrayList<?> arrList) {
        if (arrList == null)
            return;
        /*
		 * XJM：JAVA用这段代码应该没意义，C++才有意义，实现内存释放，Java看来做不到手动释放ArrayList中的Node的内存
		 *
		 * for(int i = 0;i<arrList.size();i++) { Object object = arrList.get(i);
		 * object = null; }
		 */
        arrList.clear();

        // XJM：arrList值传递，调用者的arrList并没有变为null
        arrList = null;
    }

    public static String ToDBC(String input) {
        char[] c = input.toCharArray();
        for (int i = 0; i < c.length; i++) {
            if (c[i] == 12288) {
                c[i] = (char) 32;
                continue;
            }
            if (c[i] > 65280 && c[i] < 65375)
                c[i] = (char) (c[i] - 65248);
        }
        return new String(c);
    }

    public float getFloat(String temp) {

        float res = 0;
        try {
            res = Float.valueOf(temp);
            // BigDecimal b = new BigDecimal(res);
            // res = b.setScale(2, BigDecimal.ROUND_HALF_UP).floatValue();
        } catch (Exception e) {
        }
        return res;
    }

    /*
     * MD5加密
     */
    @SuppressWarnings("unused")
    public static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        byte[] byteArray = messageDigest.digest();

        StringBuffer md5StrBuff = new StringBuffer();

        for (int i = 0; i < byteArray.length; i++) {
            if (Integer.toHexString(0xFF & byteArray[i]).length() == 1)
                md5StrBuff.append("0").append(
                        Integer.toHexString(0xFF & byteArray[i]));
            else
                md5StrBuff.append(Integer.toHexString(0xFF & byteArray[i]));
        }
        // 16位加密，从第9位到25位
        return md5StrBuff.substring(8, 24).toString().toUpperCase();
    }

    private static final int ERROR = -1;

    /**
     * SDCARD是否存
     */
    public static boolean externalMemoryAvailable() {
        return android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED);
    }

    /**
     * 获取SDCARD剩余存储空间
     *
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (externalMemoryAvailable()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return (availableBlocks * blockSize) / (1024 * 1024 * 1024);
        } else {
            return ERROR;
        }
    }

    // /**
    // * 单位换算
    // *
    // * @param size
    // * 单位为B
    // * @param isInteger
    // * 是否返回取整的单位
    // * @return 转换后的单位
    // */
    // public static String formatFileSize(long size, boolean isInteger) {
    // DecimalFormat df = isInteger ? fileIntegerFormat : fileDecimalFormat;
    // String fileSizeString = "0M";
    // if (size < 1024 && size > 0) {
    // fileSizeString = df.format((double) size) + "B";
    // } else if (size < 1024 * 1024) {
    // fileSizeString = df.format((double) size / 1024) + "K";
    // } else if (size < 1024 * 1024 * 1024) {
    // fileSizeString = df.format((double) size / (1024 * 1024)) + "M";
    // } else {
    // fileSizeString = df.format((double) size / (1024 * 1024 * 1024))
    // + "G";
    // }
    // return fileSizeString;
    // }

    /**
     * 获得sd卡的内存状态
     */
    public static int SdcardMemorySize() {
        File sdcardFileDir = Environment.getExternalStorageDirectory();
        String sdcardMemory = bytes2kb(getMemoryInfo(sdcardFileDir));
        sdcardMemory = sdcardMemory.substring(0,
                sdcardMemory.indexOf("."));
        int sdcardMemorySize = Integer.parseInt(sdcardMemory);
        if (sdcardMemorySize > 0) {
            return sdcardMemorySize;
        } else {
            return 0;
        }
    }

    private  static long getMemoryInfo(File path) {
        // 获得一个磁盘状态对象
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize(); // 获得一个扇区的大小
        long totalBlocks = stat.getBlockCount(); // 获得扇区的总数
        long availableBlocks = stat.getAvailableBlocks(); // 获得可用的扇区数量
        // // 总空间
        // String totalMemory = Formatter.formatFileSize(mContext, totalBlocks
        // * blockSize);
        // // 可用空间
        // String availableMemory = Formatter.formatFileSize(mContext,
        // availableBlocks * blockSize);
        // return "总空间: " + totalMemory + " 可用空间: " + availableMemory;
        return availableBlocks * blockSize;
    }

    public static String bytes2kb(long bytes) {
        BigDecimal filesize = new BigDecimal(bytes);
        BigDecimal megabyte = new BigDecimal(1024 * 1024);
        float returnValue = filesize.divide(megabyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        if (returnValue > 1)
            // return (returnValue + "MB");
            return (returnValue + "");
        BigDecimal kilobyte = new BigDecimal(1024);
        returnValue = filesize.divide(kilobyte, 2, BigDecimal.ROUND_UP)
                .floatValue();
        // return (returnValue + "KB");
        return (returnValue + "");
    }

}
