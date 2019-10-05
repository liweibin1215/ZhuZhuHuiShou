package com.rys.smartrecycler.tool;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Environment;
import android.os.Looper;
import android.view.Gravity;
import android.widget.Toast;

import com.lwb.framelibrary.tool.AppManagerUtils;
import com.lwb.framelibrary.tool.SharedprefUtil;
import com.rys.smartrecycler.view.activity.entrance.MainHomeActivity;

import java.io.File;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 创建时间：2018/3/8
 * 作者：李伟斌
 * 功能描述: 程序异常崩溃捕获
 */

public class CrashHandler implements Thread.UncaughtExceptionHandler {
    public static final String SYSTEM_SOURSE_PATH = Environment.getExternalStorageDirectory().getPath().toString()+"/DIYI";
    public static final String TAG = "CrashHandler";
    private Thread.UncaughtExceptionHandler mDefaultHandler;
    private static CrashHandler INSTANCE = new CrashHandler();
    private Context mContext;
    private long lastErrorTime =0;
    private DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
    private CrashHandler() {
    }
    public static CrashHandler getInstance() {
        return INSTANCE;
    }
    public void init(Context context) {
        mContext = context;
        mDefaultHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        lastErrorTime = Long.parseLong(SharedprefUtil.get(context,"LastErrorTime","0"));
    }
    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (!handleException(e) && mDefaultHandler != null) {
            mDefaultHandler.uncaughtException(t, e);
        } else {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ee) {
            }
            new Thread() {
                @Override
                public void run() {
                    long nowTime = System.currentTimeMillis();
                    if(nowTime - lastErrorTime < 10000){
                        lastErrorTime = System.currentTimeMillis();
                        SharedprefUtil.put(mContext,"LastErrorTime",String.valueOf(lastErrorTime));
                        AppManagerUtils.getInstance().finishAllActivity();
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    }else{
                        lastErrorTime = System.currentTimeMillis();
                        SharedprefUtil.put(mContext,"LastErrorTime",String.valueOf(lastErrorTime));
                        AppManagerUtils.getInstance().finishAllActivity();
                        Intent intent = new Intent(mContext, MainHomeActivity.class);
                        @SuppressLint("WrongConstant")
                        PendingIntent restartIntent = PendingIntent.getActivity(mContext, 0, intent, FLAG_ACTIVITY_NEW_TASK);
                        AlarmManager mgr = (AlarmManager) mContext.getSystemService(Context.ALARM_SERVICE);
                        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 5000, restartIntent);
                        android.os.Process.killProcess(android.os.Process.myPid());
                        System.exit(0);
                    }

                }
            }.start();
        }
    }
    private boolean handleException(final Throwable ex) {
        if (ex == null) {
            return false;
        }
        new Thread() {
            @Override
            public void run() {
                Looper.prepare();
                Toast toast = Toast.makeText(mContext, "程序异常退出，即将重启", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                Looper.loop();
            }
        }.start();
        writeFileSdcardFile(SYSTEM_SOURSE_PATH, "Crash_" + System.currentTimeMillis() + ".txt",
                getCrashInfo(ex), ex.getMessage());
        return true;
    }
    public String getCrashInfo(Throwable e) {
        StringBuilder sb = new StringBuilder();
        sb.append("生产厂商：\n");
        sb.append(Build.MANUFACTURER).append("\n\n");
        sb.append("手机型号：\n");
        sb.append(Build.MODEL).append("\n\n");
        sb.append("系统版本：\n");
        sb.append(Build.VERSION.RELEASE).append("\n\n");
        sb.append("异常时间：\n");
        sb.append(formatter.format(new Date())).append("\n\n");
        sb.append("异常类型：\n");
        sb.append(e.getClass().getName()).append("\n\n");
        sb.append("异常信息：\n");
        sb.append(e.getMessage()).append("\n\n");
        sb.append("异常堆栈：\n");
        Writer writer = new StringWriter();
        PrintWriter printWriter = new PrintWriter(writer);
        e.printStackTrace(printWriter);
        Throwable cause = e.getCause();
        while (cause != null) {
            cause.printStackTrace(printWriter);
            cause = cause.getCause();
        }
        printWriter.close();
        String result = writer.toString();
        sb.append(result);
        return sb.toString();
    }
    /**
     * 保存错误信息到文件中
     *
     * @param path
     * @param fileName  文件名
     * @param write_str 错误日志
     * @param ex        错误信息
     */
    public void writeFileSdcardFile(String path, String fileName, String write_str, String ex) {
        String path2 =SYSTEM_SOURSE_PATH;
        File file2 = new File(path2);
        if (!file2.exists()) {
            file2.mkdirs();
        }
        try {
            FileOutputStream fout = new FileOutputStream(path +"/"+ fileName);
            byte[] bytes = write_str.getBytes();
            fout.write(bytes);
            fout.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
