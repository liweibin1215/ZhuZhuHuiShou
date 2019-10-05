# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\androidstudio\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
#指定代码的压缩级别
-optimizationpasses 5
#包明不混合大小写
-dontusemixedcaseclassnames
#不去忽略非公共的库类
-dontskipnonpubliclibraryclasses
#预校验
-dontpreverify
#混淆时是否记录日志
-verbose
 #优化  不优化输入的类文件
-dontoptimize
 #混淆时所采用的算法
-optimizations !code/simplification/arithmetic,!field/*,!class/merging/*
#保护注解
-keepattributes *Annotation*
#忽略警告
-ignorewarning
-keepattributes EnclosingMethod
#记录生成的日志数据,gradle build时在本项目根目录输出
#apk 包内所有 class 的内部结构
-dump class_files.txt
#未混淆的类和成员
-printseeds seeds.txt
#列出从 apk 中删除的代码
-printusage unused.txt
#混淆前后的映射
-printmapping mapping.txt
-keepattributes InnerClasses
-dontoptimize
-dontwarn com.tencent.bugly.**
-keepattributes Signature
-dontwarn android.**
-dontwarn android.util.Xml.**
-dontwarn org.**
-dontwarn com.tencent.**
-dontwarn org.apache.tools.**
-dontoptimize
# 抛出异常时保留代码行号
-keepattributes SourceFile,LineNumberTable
#End of configure
######################################################################
#Start of Android

#Android相关
-keep public class * extends android.app.Fragment
-keep public class * extends android.app.Activity
-keep public class * extends android.app.Application
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep public class * extends android.content.ContentProvider
-keep public class * extends android.app.backup.BackupAgentHelper
-keep public class * extends android.preference.Preference
-keep public class com.android.vending.licensing.ILicensingService
-keep public class com.tencent.bugly.**{*;}
-keep public class * extends android.view.View {
    public <init>(android.content.Context);
    public <init>(android.content.Context, android.util.AttributeSet);
    public <init>(android.content.Context, android.util.AttributeSet, int);
    public void set*(...);
}
# 保留support下的所有类及其内部类
-keep class android.support.** {*;}
#不混淆R资源类
-keepclassmembers class **.R$* {
    public static <fields>;
}
#保持 native 方法不被混淆
-keepclasseswithmembernames class * {
    native <methods>;
}

#保持自定义控件类不被混淆
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet);
}
-keepclasseswithmembers class * {
    public <init>(android.content.Context, android.util.AttributeSet, int);
}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
# 保留枚举类不被混淆
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}
#保持 Parcelable 不被混淆
-keep class * implements android.os.Parcelable {
  public static final android.os.Parcelable$Creator *;
}
#保持 Serializable 不被混淆
-keepnames class * implements java.io.Serializable
#保持 Serializable 不被混淆并且enum 类也不被混淆
-keepclassmembers class * implements java.io.Serializable {
    static final long serialVersionUID;
    private static final java.io.ObjectStreamField[] serialPersistentFields;
    !static !transient <fields>;
    !private <fields>;
    !private <methods>;
    private void writeObject(java.io.ObjectOutputStream);
    private void readObject(java.io.ObjectInputStream);
    java.lang.Object writeReplace();
    java.lang.Object readResolve();
}
# 保留在Activity中的方法参数是view的方法，
# 这样以来我们在layout中写的onClick就不会被影响
-keepclassmembers class * extends android.app.Activity{
    public void *(android.view.View);
}
# 对于带有回调函数的onXXEvent、**On*Listener的，不能被混淆
-keepclassmembers class * {
    void *(**On*Event);
    void *(**On*Listener);
}

#如果有引用v4包可以添加下面这行
-keep public class * extends android.support.v4.app.Fragment
-keep public class * extends android.support.v7.**
-keep public class * extends android.support.annotation.**
######################################################################
#实体类不混淆
-keep class com.rys.smartrecycler.busbean.** { *; }
-keep class com.rys.smartrecycler.db.retbean.** { *; }
#End of Android
######################################################################
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
######################################################################
#Rxjava+retrofit+khttp3混淆规避
-dontwarn javax.annotation.**
-dontwarn javax.inject.**
# OkHttp3
-dontwarn okhttp3.logging.**
-keep public class com.squareup.okhttp.**{*;}
-keep class okhttp3.internal.**{*;}
-dontwarn okio.**
# Retrofit
-dontwarn retrofit2.**
-keep class retrofit2.** { *; }
-keepattributes Signature
-keepattributes Exceptions
# RxJava RxAndroid
-dontwarn sun.misc.**
-keepclassmembers class rx.internal.util.unsafe.*ArrayQueue*Field* {
    long producerIndex;
    long consumerIndex;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueProducerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode producerNode;
}
-keepclassmembers class rx.internal.util.unsafe.BaseLinkedQueueConsumerNodeRef {
    rx.internal.util.atomic.LinkedQueueNode consumerNode;
}
######################################################################
#eventbus不混淆
-keepattributes *Annotation*
-keepclassmembers class ** {
    @org.greenrobot.eventbus.Subscribe <methods>;
}
-keep enum org.greenrobot.eventbus.ThreadMode { *; }

# Only required if you use AsyncExecutor
-keepclassmembers class * extends org.greenrobot.eventbus.util.ThrowableFailureEvent {
    <init>(java.lang.Throwable);
}
######################################################################
# Gson
-keep class com.google.gson.** { *; }
-keepattributes EnclosingMethod
######################################################################
#极光推送避混淆
-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }
-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }
######################################################################
#阿里云OSS避混淆
-keep class com.alibaba.sdk.android.oss.** { *; }
-dontwarn okio.**
-dontwarn org.apache.commons.codec.binary.**
######################################################################
#butterknife避混淆
 -keep class butterknife.** { *; }
 -dontwarn butterknife.internal.**
 -keep class **$$ViewBinder { *; }
 -keepclasseswithmembernames class * {
     @butterknife.* <fields>;
 }
 -keepclasseswithmembernames class * {
     @butterknife.* <methods>;
 }
####################################################
#七牛混淆
-keep class com.qiniu.**{*;}
-keep class com.qiniu.**{public <init>();}
-ignorewarnings
####################################################
# banner 的混淆代码
-keep class com.youth.banner.** {*;}
####################################################
#picasso避混淆
-dontwarn com.squareup.okhttp.**
##########################################################################
# 腾讯bugly不混淆
-dontwarn com.tencent.bugly.**
-keep  class com.tencent.bugly.**{*;}
#####################################################
#zxing条码编译工具
-dontwarn com.google.zxing.**
-keep  class com.google.zxing.**{*;}
-keep  class com.uuzuche.lib_zxing.**{*;}

######################################################################
-dontwarn com.amap.api.**
-keep  class com.amap.api.**{*;}
######################################################################
-dontwarn com.intsig.**
-keep  class com.intsig.exp.sdk.**{*;}
-keep  class com.intsig.nativelib.**{*;}
######################################################################
-dontwarn com.veritrans.IdReader.**
-keep  class com.veritrans.IdReader.**{*;}
######################################################################
-dontwarn com.iflytek.**
-keep  class com.iflytek.**{*;}
######################################################################
-dontwarn com.printer.sdk.**
-keep  class com.printer.sdk.**{*;}
######################################################################
#zltd
-dontwarn com.zltd.**
-keep  class com.zltd.industry.**{*;}
-keep  class com.zltd.scanner.scan.**{*;}
-dontwarn com.printer.sdk.**
######################################################################
#NewbieGuide避混淆
-dontwarn com.app.hubert.guide.**
-keep  class com.app.hubert.guide.**{*;}
######################################################################
#Glide避混淆
-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}
######################################################################
# banner 的混淆代码
-keep class com.youth.banner.** {*;}
########################################################################
#Greendao避混淆
-dontwarn net.sqlcipher.database.**
-dontwarn org.greenrobot.greendao.**
-keep class org.greenrobot.greendao.**{*;}
-keep public interface org.greenrobot.greendao.**
-keepclassmembers class * extends org.greenrobot.greendao.AbstractDao {
public static java.lang.String TABLENAME;
}
-keep class **$Properties
-keep class net.sqlcipher.database.**{*;}
-keep public interface net.sqlcipher.database.**
-keep public class * extends org.greenrobot.greendao.AbstractDao
-keepclassmembers class **$Properties {*;}
########################################################################
# linkkit API
-keep class com.aliyun.alink.**{*;}
-keep class com.aliyun.linksdk.**{*;}
-dontwarn com.aliyun.**
-dontwarn com.alibaba.**
-dontwarn com.alipay.**
-dontwarn com.ut.**
# keep native method
-keepclasseswithmembernames class * {
    native <methods>;
}
# keep netty
-keepattributes Signature,InnerClasses
-keepclasseswithmembers class io.netty.** {
    *;
}
-dontwarn io.netty.**
-dontwarn sun.**
# keep mqtt
-keep public class org.eclipse.paho.**{*;}
# keep fastjson
-dontwarn com.alibaba.fastjson.**
-keep class com.alibaba.fastjson.**{*;}
# keep gson
-keep class com.google.gson.** { *;}
# keep network core
-keep class com.http.**{*;}
# keep okhttp
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn javax.annotation.**
-keep class okio.**{*;}
-keep class okhttp3.**{*;}
-keep class org.apache.commons.codec.**{*;}