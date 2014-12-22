package com.insanedevelopers.doglovers;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.StrictMode;

import com.insanedevelopers.doglovers.quickblox.Constants;
import com.insanedevelopers.doglovers.quickblox.SplashActivity;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseInstallation;
import com.parse.PushService;

public class App extends Application {
	
	
	 private static App mInstance;
	 private static Context mAppContext;
	    
    @TargetApi(Build.VERSION_CODES.GINGERBREAD)
    @SuppressWarnings({ "unused", "deprecation" })
    @Override
    public void onCreate() {
        if (Constants.CONFIG_DEVELOPER_MODE && Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
            StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder().detectAll().penaltyDialog().build());
            StrictMode.setVmPolicy(new StrictMode.VmPolicy.Builder().detectAll().penaltyDeath().build());
        }

        Parse.initialize(this, "MF7lnf21BBCshcmIry4XMD9yq50aN4Va1SebF0qV", "2Pc1cLcNUvFsjGWUAfSf2mfkWHpEXKcioMuthutU");
		PushService.setDefaultPushCallback(getApplicationContext(),SplashActivity.class);
		ParseInstallation.getCurrentInstallation().saveInBackground();
		
        super.onCreate();
        

        initImageLoader(getApplicationContext());
    }
    
    public static App getInstance(){
        return mInstance;}
    public static Context getAppContext() {
        return mAppContext;}
    public void setAppContext(Context mAppContext) {
        this.mAppContext = mAppContext;}

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
              .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
        .tasksProcessingOrder(QueueProcessingType.LIFO)
        
        
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
    }
}