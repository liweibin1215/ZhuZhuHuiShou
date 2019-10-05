package com.lwb.framelibrary.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by Hsh on 2017/4/18.
 */
public class FileUtil {
    public static final String cached_pic_dir = "pic";
    /**
     * sd目录下项目文件根目录
     */
    public static final String rootPath = "递易";

    public static final String downloadPath = "Download";


    public static String getSdcardRootPath() {// 创建一个空文件用于写入
        // 获取扩展SD卡设备状态
        String sDStateString = Environment.getExternalStorageState();
        if (sDStateString != null && sDStateString.equals(Environment.MEDIA_MOUNTED)) {
            File fileSdcardDir = Environment.getExternalStorageDirectory();
            if (fileSdcardDir == null) {
                return null;
            }
            String sdcardPath = fileSdcardDir.getAbsolutePath();
            String sdcardRootPath = sdcardPath + File.separator + rootPath;
            File fileRootDir = new File(sdcardRootPath);
            if (!fileRootDir.exists()) {
                fileRootDir.mkdir();
            }
            return sdcardRootPath;
        } else {
            return null;
        }
    }


    /**
     * 缓存路径
     * @param context
     * @return
     */
    public static String getDefaultCacheDir(Context context) {
        String cachePath = null;
        if (Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())
                || !Environment.isExternalStorageRemovable()) {
            //sdcard/Android/data/cache
            cachePath = context.getExternalCacheDir().getPath();
        } else {
            ///data/data/cache
            cachePath = context.getCacheDir().getPath();
        }
        return cachePath;
    }

    public static String getDownloadPath() {
        String sdcardRootPath = getSdcardRootPath();
        if (sdcardRootPath != null) {
            String childPath = sdcardRootPath + File.separator + downloadPath;
            File fileChildDir = new File(childPath);
            if (!fileChildDir.exists()) {
                fileChildDir.mkdir();
            }
            return childPath;
        } else {
            return null;
        }
    }




    public static void copyFile(final String oldPath, final String newPath) {
        if (oldPath != null && newPath != null) {
            FileInputStream fileInputStream = null;
            FileOutputStream fileOutputStream = null;
            try {
                File oldFile = new File(oldPath);
                if (oldFile.exists()) {
                    File newFile = new File(newPath);
                    if (!newFile.exists()) {
                        newFile.createNewFile();//new FileOutputStream(newFile,true)才是追加的默认是覆盖
                    }
                    fileInputStream = new FileInputStream(oldFile);
                    fileOutputStream = new FileOutputStream(newFile);
                    byte[] bytes = new byte[1024];
                    int length = -1;
                    while ((length = fileInputStream.read(bytes)) != -1) {
                        fileOutputStream.write(bytes, 0, length);
                    }
                    fileOutputStream.flush();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (fileInputStream != null) {//谁先结束先关谁
                        fileInputStream.close();
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String saveBitmapWithJPG(final String savePath, final String lastFileName, final Bitmap bitmap) {
        if (savePath == null || lastFileName == null || bitmap == null) {
            return null;
        }
        String fileName = savePath + File.separator + lastFileName;
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            fileName = null;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /**
     * 图片保存到缓存目录中
     * @param context
     * @param lastFileName
     * @param bitmap
     * @return
     */
    public static String saveBitmapWithJPG(final Context context, final String lastFileName, final Bitmap bitmap) {
        if (lastFileName == null || bitmap == null) {
            return null;
        }
        File fileCacheDir = new File(getDefaultCacheDir(context), cached_pic_dir);//如果context.getCacheDir()都拿不到就应该报错的
        if (!fileCacheDir.exists()) {
            fileCacheDir.mkdir();
        }
        String fileName = fileCacheDir.getAbsolutePath() + File.separator + lastFileName;
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fileOutputStream);
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            fileName = null;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /**
     * 操作context的一律在主线程运行,再提醒一次,以后就不提醒了
     */
    public static String saveBitmapWithPNG(final Context context, final String lastFileName, final Bitmap bitmap) {
        if (lastFileName == null || bitmap == null) {
            return null;
        }
        File fileCacheDir = new File(getDefaultCacheDir(context), cached_pic_dir);//如果context.getCacheDir()都拿不到就应该报错的
        if (!fileCacheDir.exists()) {
            fileCacheDir.mkdir();
        }
        String fileName = fileCacheDir.getAbsolutePath() + File.separator + lastFileName;
        FileOutputStream fileOutputStream = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);//PNG不会受第二个参数大小的影响
            fileOutputStream.flush();
        } catch (IOException e) {
            e.printStackTrace();
            fileName = null;
        } finally {
            try {
                if (fileOutputStream != null) {
                    fileOutputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return fileName;
    }

    /**
     * 获取图片的宽高,不会返回null,一定会返回一个长度为2的数组
     */
    public static int[] getBitmapWidthAndHeight(final String fileName) {
        int[] res = new int[2];
        if (fileName == null) {
            return res;
        }
        File file = new File(fileName);
        if (!file.exists()) {
            return res;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);
        res[0] = options.outWidth;
        res[1] = options.outHeight;
        return res;
    }

    /**
     * 压缩图片
     */
    public static Bitmap getCompressBitmapFromSdcard(final String fileName, final int reqWidth, final int reqHeight) {
        if (fileName == null) {
            return null;
        }
        File file = new File(fileName);
        if (!file.exists()) {
            return null;
        }
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(fileName, options);
        int zoom = 1;
        if (reqWidth <= 0 && reqHeight <= 0) {
            zoom = 1;
        } else if (reqWidth > 0 && reqHeight <= 0) {
            int zoomWidth = (int) (options.outWidth / (float) reqWidth);
            zoom = zoomWidth;
        } else if (reqWidth <= 0 && reqHeight > 0) {
            int zoomHeight = (int) (options.outHeight / (float) reqHeight);
            zoom = zoomHeight;
        } else if (reqWidth > 0 && reqHeight > 0 && options.outWidth > 0 && options.outHeight > 0) {
            int zoomWidth = (int) (options.outWidth / (float) reqWidth);
            int zoomHeight = (int) (options.outHeight / (float) reqHeight);
            if (zoomWidth < zoomHeight) {//获得俩者间最小比例作为缩放比
                zoom = zoomWidth;
            } else {
                zoom = zoomHeight;
            }
        }
        if (zoom <= 0) {
            zoom = 1;//上面3种情况都有可能等于0的
        }
        options.inSampleSize = zoom;
        options.inJustDecodeBounds = false;
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        return BitmapFactory.decodeFile(fileName, options);
    }

    public static Bitmap getCompressBitmapFromBitmap(final Bitmap bitmap, final int reqWidth, final int reqHeight) {
        if (bitmap == null) {
            return null;
        }
        int zoom = 1;
        if (reqWidth <= 0 && reqHeight <= 0) {
            zoom = 1;
        } else if (reqWidth > 0 && reqHeight <= 0) {
            int zoomWidth = (int) (bitmap.getWidth() / (float) reqWidth);
            zoom = zoomWidth;
        } else if (reqWidth <= 0 && reqHeight > 0) {
            int zoomHeight = (int) (bitmap.getHeight() / (float) reqHeight);
            zoom = zoomHeight;
        } else if (reqWidth > 0 && reqHeight > 0 && bitmap.getWidth() > 0 && bitmap.getHeight() > 0) {
            int zoomWidth = (int) (bitmap.getWidth() / (float) reqWidth);
            int zoomHeight = (int) (bitmap.getHeight() / (float) reqHeight);
            if (zoomWidth < zoomHeight) {//获得俩者间最小比例作为缩放比
                zoom = zoomWidth;
            } else {
                zoom = zoomHeight;
            }
        }
        if (zoom <= 0) {
            zoom = 1;//上面3种情况都有可能等于0的
        }
        Matrix matrix = new Matrix();
        matrix.postScale(zoom, zoom);//如果等于0会报错的
        Bitmap theBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return theBitmap;
    }


    /**
     * 从resource中的raw文件夹中获取文件并读取数据（资源文件只能读不能写）
     */
    public static String readDataFromRaw(final Context context, final int resId) {

        InputStream inputStream = null;
        String result = null;
        try {
            inputStream = context.getResources().openRawResource(resId);
            int length = inputStream.available();
            byte[] bytes = new byte[length];
            inputStream.read(bytes);
            result = new String(bytes, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    /**
     * 从asset中获取文件并读取数据（资源文件只能读不能写）
     */
    public static String readDataFromAsset(final Context context, final String fileName) {
        String result = null;
        if (fileName == null) {
            return result;
        }
        InputStream inputStream = null;
        try {
            inputStream = context.getResources().getAssets().open(fileName);
            int length = inputStream.available();
            byte[] bytes = new byte[length];
            inputStream.read(bytes);
            result = new String(bytes, "utf-8");
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (inputStream != null) {
                    inputStream.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return result;
    }




    /**
     * 删除指定目录下文件及目录
     */
    public static void deleteFolderFile(final String filePath) {
        if (filePath != null) {
            try {
                File file = new File(filePath);
                if (file.isFile()) {
                    file.delete();
                    return;
                }
                if (file.isDirectory()) {
                    File[] fileLists = file.listFiles();
                    if (fileLists == null || fileLists.length == 0) {
                        file.delete();
                        return;
                    }
                    for (int i = 0; i < fileLists.length; i++) {
                        if (fileLists[i] != null) {
                            deleteFolderFile(fileLists[i].getAbsolutePath());
                        }
                    }
                    file.delete();//全部子文件删除了删除自己
                }
            } catch (Exception e) {//递归操作文件夹毕竟不靠谱万一有未知bug呢,还是加上Exception吧
                e.printStackTrace();
            }
        }

    }
}
