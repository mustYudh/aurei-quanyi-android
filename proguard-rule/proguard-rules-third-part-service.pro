#TalkingData防止混淆后导出APK时报Warning而导出失败
-dontwarn com.tendcloud.**

#---------------------------------------
# ######## umeng 友盟 ########
#---------------------------------------
-keep class com.umeng.** { *; }

#---------------------------------------
# ######## getui 个推 ########
#---------------------------------------
-dontwarn com.igexin.**
-keep class com.igexin.** { *; }
-keep class org.json.** { *; }

#---------------------------------------
# ######## sensorsdata 神策数据埋点 ########
#---------------------------------------
-dontwarn com.sensorsdata.analytics.android.sdk.**
-keep class com.sensorsdata.analytics.android.sdk.** {*;}
# 使用可视化埋点需添加
-keep class **.R$* {<fields>;}

-keep class * extends android.support.v4.app.Fragment {
 public void setUserVisibleHint(boolean);
 public void onHiddenChanged(boolean);
 public void onResume();
 public void onPause();
}

#---------------------------------------
# ######## lianlian pay 连连支付 ########
#---------------------------------------

# 连连混淆keep规则，请添加
-keep class com.yintong.secure.activityproxy.PayIntro$LLJavascriptInterface{*;}

# 连连混淆keep规则
-keep public class com.yintong.** {
    <fields>;
    <methods>;
}



-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclassmembers public class * extends android.view.View {
   void set*(***);
   *** get*();
}
-keepclassmembers class * extends android.app.Activity {
   public void *(android.view.View);
}
-keepclassmembers enum * {
    public static **[] values();
    public static ** valueOf(java.lang.String);
}

-keep class * implements android.os.Parcelable {
    public static final android.os.Parcelable$Creator *;
}

#---------------------------------------
# ######## 科大讯飞 ########
#---------------------------------------
-dontwarn com.iflytek.speech.**
-keep class com.iflytek.speech.**{*;}

#---------------------------------------
# ######## Bugly ########
#---------------------------------------
-dontwarn com.tencent.bugly.**
-keep public class com.tencent.bugly.**{*;}

#---------------------------------------
# ######## 信鸽 ########
#---------------------------------------
-keep public class * extends android.app.Service
-keep public class * extends android.content.BroadcastReceiver
-keep class com.tencent.android.tpush.**  {*;}
-keep class com.tencent.mid.**  {*;}

#---------------------------------------
# ######## ShareSDK ########
#---------------------------------------
-keep class android.net.http.SslError
-keep class android.webkit.**{*;}
-keep class cn.sharesdk.**{*;}
-keep class com.sina.**{*;}
-keep class com.alipay.share.sdk.**{*;}

#---------------------------------------
# ######## Wechat pay 微信支付 ########
#---------------------------------------
-keep class com.tencent.mm.sdk.** {
   *;
}

#---------------------------------------
# ######## Alipay 支付宝 ########
#---------------------------------------
-keep class com.alipay.android.app.IAlixPay{*;}
-keep class com.alipay.android.app.IAlixPay$Stub{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback{*;}
-keep class com.alipay.android.app.IRemoteServiceCallback$Stub{*;}
-keep class com.alipay.sdk.app.PayTask{ public *;}
-keep class com.alipay.sdk.app.AuthTask{ public *;}
-dontwarn com.alipay.**

#---------------------------------------
# ######## Baidu map ########
#---------------------------------------
-keep class com.baidu.** { *; }
-keep class vi.com.gdi.bgl.android.**{*;}

#---------------------------------------
# ######## JPush ########
#---------------------------------------
-dontoptimize
-dontpreverify

-dontwarn cn.jpush.**
-keep class cn.jpush.** { *; }
-keep class * extends cn.jpush.android.helpers.JPushMessageReceiver { *; }

-dontwarn cn.jiguang.**
-keep class cn.jiguang.** { *; }

# Glide
#-keep resourcexmlelements manifest/application/meta-data@value=GlideModule


-keep public class * implements com.bumptech.glide.module.GlideModule
-keep public class * extends com.bumptech.glide.AppGlideModule
-keep public enum com.bumptech.glide.load.resource.bitmap.ImageHeaderParser$** {
  **[] $VALUES;
  public *;
}

# for DexGuard only
#-keepresourcexmlelements manifest/application/meta-data@value=GlideModule


# okhttp3

-keepattributes Signature
-keepattributes *Annotation*
-keep class okhttp3.** { *; }
-keep interface okhttp3.** { *; }
-dontwarn okhttp3.**

# okio

-keep class sun.misc.Unsafe { *; }
-dontwarn java.nio.file.*
-dontwarn org.codehaus.mojo.animal_sniffer.IgnoreJRERequirement
-dontwarn okio.**

#gif-drawable
-keep public class pl.droidsonroids.gif.GifIOException{<init>(int);}
-keep class pl.droidsonroids.gif.GifInfoHandle{<init>(long,int,int,int);}

#gson
-keep class sun.misc.Unsafe { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.tencent.mm.sdk.** {
   *;
}

#aliyun-oss
-keep class com.alibaba.sdk.android.** {
}

#org.apache
-dontwarn org.apache.http.**
-dontwarn org.apache.commons.**


#PINGYIN4J
-dontwarn net.soureceforge.pinyin4j.**
-dontwarn demo.**
-keep class net.sourceforge.pinyin4j.** { *;}
-keep class demo.** { *;}


#greenDao
-keep class de.greenrobot.dao.** {*;}
-keepclassmembers class * extends de.greenrobot.dao.AbstractDao {
    public static java.lang.String TABLENAME;
}
-keep class **$Properties

#android-gif-drawable
-keep public class pl.droidsonroids.gif.GifIOException{<init>(int);}
-keep class pl.droidsonroids.gif.GifInfoHandle{<init>(long,int,int,int);}
## ----------------------------------
##   ######## Otto ########
## ----------------------------------
-keepattributes *Annotation*
-keepclassmembers class ** {
    @com.squareup.otto.Subscribe public *;
    @com.squareup.otto.Produce public *;
}
## ----------------------------------
##   ######## ButterKnife ########
## ----------------------------------
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}
# PhotoPicker
-keep public class me.iwf.photopicker.**{*;}
# lombok
-dontwarn lombok.**

# easy permission
-keep class pub.devrel.easypermissions.** {*;}

#qimo客服
-keep class com.moor.imkf.** { *; }

#TalkingData
-keep class com.talkingdata.sdk.** {*;}
-keep class com.tendcloud.** {*;}
-keep public class com.tendcloud.** {  public protected *;}
