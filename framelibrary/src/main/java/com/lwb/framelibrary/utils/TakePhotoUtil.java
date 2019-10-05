package com.lwb.framelibrary.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;

import java.io.File;

import static android.content.Intent.FLAG_GRANT_READ_URI_PERMISSION;
import static android.content.Intent.FLAG_GRANT_WRITE_URI_PERMISSION;

/**
 * 创建时间：2017/08/03
 * 作者：李伟斌
 * 功能描述: 相机拍照、图库选择、裁剪、适配 7.0+
 * 7.0+ 适配需要 manifest 配置 FileProvider
 */
public class TakePhotoUtil {

    private static final int CAMERA_CODE = 9999;
    private static final int ALBUM_CODE = CAMERA_CODE - 1;
    private static final int CROP_CODE = ALBUM_CODE - 1;
    private static final String FILE_PROVIDER = "com.totcy.baseLibrary.fileProvider";

    private Uri outputUri;
    private File outputFile;
    private Object activity;
    private Context context;
    private boolean isCrop = false;

    public TakePhotoUtil(Object activity, Context context, boolean isCrop) {
        this.activity = activity;
        this.isCrop = isCrop;
        this.context = context;
    }

    private void initOutPutUri() {
        //这里的路径是应用的缓存路径，可以跳过6.0的权限申请，getExternalCacheDir()这个路径是不需要申请权限访问的。
        outputFile = new File(context.getExternalCacheDir(), System.currentTimeMillis() + ".jpg");
        outputUri = Uri.fromFile(outputFile);
    }

    /**
     * 打开相册选择图片
     */
    public void openAlbum() {
        initOutPutUri();
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        if (activity instanceof Activity) {
            ((Activity) activity).startActivityForResult(intent, ALBUM_CODE);

        } else if (activity instanceof Fragment) {
            ((Fragment) activity).startActivityForResult(intent, ALBUM_CODE);
        }
    }

    /**
     * 拍照获取图片
     */
    public void userCamera() {
        initOutPutUri();
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            //通过FileProvider创建一个content类型的Uri
            Uri uri = FileProvider.getUriForFile(context, FILE_PROVIDER, outputFile);
            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            //添加这一句表示对目标应用临时授权该Uri所代表的文件
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
        } else {
            intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);
        }
        if (activity instanceof Activity) {
            ((Activity) activity).startActivityForResult(intent, CAMERA_CODE);
        } else if (activity instanceof Fragment) {
            ((Fragment) activity).startActivityForResult(intent, CAMERA_CODE);
        }
    }

    /**
     * 图片裁剪
     *
     * @param inputUri
     */
    private void doCrop(Uri inputUri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        //sdk>=24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.setDataAndType(inputUri, "image/*");
            intent.addFlags(FLAG_GRANT_READ_URI_PERMISSION);
            intent.addFlags(FLAG_GRANT_WRITE_URI_PERMISSION);
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //这个方法是处理4.4以上图片返回的Uri对象不同的处理方法
            intent.setDataAndType(Uri.fromFile(new File(getPath(context, inputUri))), "image/*");
        } else {
            intent.setDataAndType(inputUri, "image/*");
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, outputUri);

        // 设置裁剪
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 450);
        intent.putExtra("outputY", 450);
        intent.putExtra("return-data", false);
        //去除默认的人脸识别，否则和剪裁匡重叠
        intent.putExtra("noFaceDetection", false);
        // 图片格式
        intent.putExtra("outputFormat", "JPEG");
        if (activity instanceof Activity) {
            ((Activity) activity).startActivityForResult(intent, CROP_CODE);
        } else if (activity instanceof Fragment) {
            ((Fragment) activity).startActivityForResult(intent, CROP_CODE);
        }
    }


    /**
     * activity的结果回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void attachToActivityForResult(int requestCode, int resultCode, Intent data) {

        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        switch (requestCode) {
            case CAMERA_CODE:
                //照相后返回
                Uri inputUri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    //通过FileProvider创建一个content类型的Uri
                    inputUri = FileProvider.getUriForFile(context, FILE_PROVIDER, outputFile);
                } else {
                    inputUri = outputUri;
                }
                //裁剪
                if (isCrop) {
                    doCrop(inputUri);
                } else {
                    if (onSelectUriListener != null) {
                        onSelectUriListener.onSelectResult(inputUri);
                    }
                }
                break;
            case ALBUM_CODE:
                Uri inputUriAlbum;
                //7.0以后 裁剪的uri一定要用 FileProvider 这么获取。
                // 如果不需要裁剪 data.getData()获取的uri也是可以直接显示的 glide
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    File imgUri = new File(getPath(context, data.getData()));
                    inputUriAlbum = FileProvider.getUriForFile(context, FILE_PROVIDER, imgUri);
                } else {
                    inputUriAlbum = data.getData();
                }
                //图库后返回
                if (isCrop) {
                    doCrop(inputUriAlbum);
                } else {
                    if (onSelectUriListener != null) {
                        onSelectUriListener.onSelectResult(inputUriAlbum);
                    }
                }

                break;
            case CROP_CODE:
                //裁剪结果
                if (onSelectUriListener != null) {
                    onSelectUriListener.onSelectResult(outputUri);
                }
                break;

            default:
                break;
        }

    }

    public interface OnSelectUriListener {
        /**
         * 选择图
         *
         * @param result 选择图片的Uri
         */
        void onSelectResult(Uri result);
    }

    private OnSelectUriListener onSelectUriListener;

    public void setOnSelectUriListener(OnSelectUriListener onSelectUriListener) {
        this.onSelectUriListener = onSelectUriListener;
    }

    public void setCrop(boolean crop) {
        isCrop = crop;
    }


    /*********************************************************/

    //  4.4以上  content://com.android.providers.media.documents/document/image:3952
    //  4.4以下  content://media/external/images/media/3951
    @SuppressLint("NewApi")
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {
                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            // Return the remote address
            if (isGooglePhotosUri(uri)) {
                return uri.getLastPathSegment();
            }

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Android 4.4以下版本自动使用该方法
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}
