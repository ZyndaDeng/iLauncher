package com.google.android.apps.nexuslauncher;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Process;
import android.util.Log;

import com.android.launcher3.FastBitmapDrawable;
import com.android.launcher3.ItemInfo;
import com.android.launcher3.LauncherModel;
import com.android.launcher3.LauncherSettings;
import com.android.launcher3.Utilities;
import com.android.launcher3.util.ComponentKey;
import com.android.launcher3.util.LooperExecutor;
import com.google.android.apps.nexuslauncher.clock.CustomClock;
import com.google.android.apps.nexuslauncher.utils.ActionIntentFilter;
import com.zynda.android.LauncherIconTheme;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Semaphore;

public class CustomDrawableFactory extends DynamicDrawableFactory implements Runnable {
    private final Context mContext;
    private final BroadcastReceiver mAutoUpdatePack;
    private boolean mRegistered = false;

    String iconPack;
    final Map<ComponentName, Integer> packComponents = new HashMap<>();
    final Map<ComponentName, String> packCalendars = new HashMap<>();
    final Map<Integer, CustomClock.Metadata> packClocks = new HashMap<>();

    private CustomClock mCustomClockDrawer;
    private Semaphore waiter = new Semaphore(0);

    public CustomDrawableFactory(Context context) {
        super(context);
        mContext = context;
        mCustomClockDrawer = new CustomClock(context);
        CustomIconUtils.setCurrentPack(context, "com.zynda.android.itheme");
        mAutoUpdatePack = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                if (!CustomIconUtils.usingValidPack(context)) {
                    CustomIconUtils.setCurrentPack(context, "com.zynda.android.itheme");
                }
                CustomIconUtils.applyIconPackAsync(context);
            }
        };

        new LooperExecutor(LauncherModel.getWorkerLooper()).execute(this);
    }

    @Override
    public void run() {
        reloadIconPack();
        waiter.release();
    }

    void reloadIconPack() {
        iconPack = CustomIconUtils.getCurrentPack(mContext);

        if (mRegistered) {
            mContext.unregisterReceiver(mAutoUpdatePack);
            mRegistered = false;
        }
        if (!iconPack.isEmpty()) {
            mContext.registerReceiver(mAutoUpdatePack, ActionIntentFilter.newInstance(iconPack,
                    Intent.ACTION_PACKAGE_CHANGED,
                    Intent.ACTION_PACKAGE_REPLACED,
                    Intent.ACTION_PACKAGE_FULLY_REMOVED),
                    null,
                    new Handler(LauncherModel.getWorkerLooper()));
            mRegistered = true;
        }

        packComponents.clear();
        packCalendars.clear();
        packClocks.clear();
        //if (CustomIconUtils.usingValidPack(mContext)) {
            CustomIconUtils.parsePack(this, mContext.getPackageManager(), iconPack);
       // }
    }

    synchronized void ensureInitialLoadComplete() {
        if (waiter != null) {
            waiter.acquireUninterruptibly();
            waiter.release();
            waiter = null;
        }
    }

    @Override
    public FastBitmapDrawable newIcon(Bitmap icon, ItemInfo info) {
        ensureInitialLoadComplete();
        ComponentName componentName = info.getTargetComponent();
        Log.d("cn",info.getTargetComponent().toString());
        if (packComponents.containsKey(info.getTargetComponent()) &&
                CustomIconProvider.isEnabledForApp(mContext, new ComponentKey(componentName, info.user))) {
            if (Utilities.ATLEAST_OREO &&
                    info.itemType == LauncherSettings.Favorites.ITEM_TYPE_APPLICATION &&
                    info.user.equals(Process.myUserHandle())) {
                int drawableId = packComponents.get(componentName);
                if(packCalendars.containsKey(componentName)){
                    try {
                        PackageManager pm = mContext.getPackageManager();
                        Resources res = pm.getResourcesForApplication(iconPack);
                        drawableId = res.getIdentifier(packCalendars.get(componentName), "drawable", iconPack);
                        Drawable drawable = mContext.getPackageManager().getDrawable(iconPack, drawableId, null);
                        return new AutoUpdateCalender(((BitmapDrawable) drawable).getBitmap(),res.getDisplayMetrics().density);
                    }catch (PackageManager.NameNotFoundException ignored) {
                    }
                }else if (packClocks.containsKey(drawableId)) {
                    Drawable drawable = mContext.getPackageManager().getDrawable(iconPack, drawableId, null);
                    return mCustomClockDrawer.drawIcon(icon, drawable, packClocks.get(drawableId));
                }else{
                    Drawable drawable = mContext.getPackageManager().getDrawable(iconPack, drawableId, null);
                    return new FastBitmapDrawable(((BitmapDrawable) drawable).getBitmap());
                }
            }
            return new FastBitmapDrawable(icon);
        }
        return super.newIcon(icon, info);
    }
}
