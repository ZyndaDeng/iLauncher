package com.zynda.android;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;

import com.android.launcher3.R;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

public class LauncherIconTheme {
    private static LauncherIconTheme INSTANCE;
    private Map<String,String> iconNames;
    private static String TAG = "LauncherIconTheme";

    public static LauncherIconTheme getInstance(){
        if(INSTANCE==null){
            INSTANCE=new LauncherIconTheme();
        }
        return INSTANCE;
    }

    private LauncherIconTheme(){
        iconNames=new HashMap<>();
        initIconName();
    }

    //根据包名、类名获取Bitmap
//    public Bitmap getIconBitmap(Context context , String packageName , String className) {
//        Resources resources = context.getResources();
//        int iconId = getIconId(packageName, className);
//        if (iconId != -1){
//            return BitmapFactory.decodeResource(resources, iconId);
//        }
//        return null;
//    }
    //根据包名、类名获取Drawable   要用到的就是这个方法
    public  Drawable getIconDrawable(Context context , String packageName , String className) {
        Resources resources = context.getResources();
        String iconId = getIconId(packageName, className);
        if ( iconId != null) {
            //return resources.getDrawable(iconId);

            Drawable d=null;
            AssetManager am = context.getAssets();
            try
            {
                InputStream is = am.open(iconId);
                d = Drawable.createFromStream(is,null);
                is.close();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }

            return d;

        }
        return null;
    }
    //根据包名、类名获取资源定义的图标资源id
    private String getIconId(String packageName , String className){
        if(className.equals("com.android.contacts.activities.DialtactsActivity")){
            return "icons/com.android.contacts.activities.DialtactsActivity.png";
        }else if(iconNames.containsKey(packageName)){
            return iconNames.get(packageName);
        }else {
            return null;
        }
    }

    private void initIconName() {
        iconNames.put("com.android.browser","icons/com.android.browser.png");
        iconNames.put("com.android.calculator","icons/com.android.calculator2.png");
        iconNames.put("com.android.calculator2","icons/com.android.calculator.png");
        iconNames.put("com.android.calendar","icons/com.android.calendar.png");
        iconNames.put("com.android.chrome","icons/com.android.chrome.png");

        iconNames.put("com.android.contacts.activities.DialtactsActivity","icons/com.android.contacts.activities.DialtactsActivity.png");
        iconNames.put("com.android.contacts","icons/com.android.contacts.png");
        iconNames.put("com.android.deskclock","icons/com.android.deskclock.png");
        iconNames.put("com.android.email","icons/com.android.email.png");
        iconNames.put("com.android.mms","icons/com.android.mms.png");

        iconNames.put("com.android.settings","icons/com.android.settings.png");
        iconNames.put("com.autonavi.minimap","icons/com.autonavi.minimap.png");
        iconNames.put("com.evenwell.android.memo","icons/com.evenwell.android.memo.png");
        iconNames.put("com.evenwell.fmradio","icons/com.evenwell.fmradio.png");
        iconNames.put("com.evenwell.gallery","icons/com.evenwell.gallery.png");

        iconNames.put("com.evenwell.systemdashboard.mobileassistant","icons/com.evenwell.systemdashboard.mobileassistant.png");
        iconNames.put("com.evenwell.weather","icons/com.evenwell.weather.png");
        iconNames.put("com.hmdglobal.appstore","icons/com.hmdglobal.appstore.png");
        iconNames.put("com.hmdglobal.camera2","icons/com.hmdglobal.camera2.png");
        iconNames.put("com.kugou.android","icons/com.kugou.android.png");

        iconNames.put("com.nbc.datatransfer","icons/com.nbc.datatransfer.png");
        iconNames.put("com.nbc.filemanager","icons/com.nbc.filemanager.png");
        iconNames.put("com.nbc.voiceassistant","icons/com.nbc.voiceassistant.png");
        iconNames.put("com.nbc.willcloud.themestore","icons/com.nbc.willcloud.themestore.png");
        iconNames.put("com.redteamobile.global.roaming","icons/com.redteamobile.global.roaming.png");

        iconNames.put("com.tencent.mm","icons/com.tencent.mm.png");
    }


}

