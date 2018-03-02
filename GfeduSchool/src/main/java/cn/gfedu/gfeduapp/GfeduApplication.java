package cn.gfedu.gfeduapp;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.multidex.MultiDex;
import android.support.multidex.MultiDexApplication;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.easefun.polyvsdk.PolyvDevMountInfo;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.server.AndroidService;
import com.gensee.taskret.OnTaskRet;
import com.gensee.vod.VodSite;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.common.QueuedWork;
import com.zhy.autolayout.config.AutoLayoutConifg;

import java.io.File;

import cn.jun.menory.service.PolyvDemoService;
import cn.jun.menory.service.VideoDownloadManager;
import cn.jun.pushMessage.PushMessage_Service;

public class GfeduApplication extends MultiDexApplication {

    private static final String TAG = GfeduApplication.class.getSimpleName();
    //创建MyApplication对象
    public static GfeduApplication application;

    // 获取实例
    public static GfeduApplication getInstance() {
        return application;
    }

    private static Context context;
    //保利威视
    private ServiceStartErrorBroadcastReceiver serviceStartErrorBroadcastReceiver = null;
    private String aeskey = "VXtlHmwfS2oYm0CZ";
    private String iv = "2u9gDPKdX6GyQJKU";
//    private int drmServerPort;

    public static Context getContext() {
        return context;
    }


    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

//    public int getDrmServerPort() {
//        return drmServerPort;
//    }

//    public void setDrmServerPort(int drmServerPort) {
//        this.drmServerPort = drmServerPort;
//    }

    @Override
    public void onTerminate() {
        super.onTerminate();
        PolyvSDKClient client = PolyvSDKClient.getInstance();
        client.stopService(getApplicationContext(), PolyvDemoService.class);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
        //友盟
        initUMSDK();
        //图库
        context = this.getApplicationContext();
        VideoDownloadManager.getInstance().init(this);
        // 异常处理
//        CrashException.getInstance().init(getApplicationContext());
        application = this;
        ImageLoader();
        // 保利威视SDK
        initPolyvCilent();

        startService(new Intent(this, PushMessage_Service.class));

        //展视互动
        /**
         * 优先调用进行组件加载，有条件的情况下可以放到application启动时候的恰当时机调用
         */
        VodSite.init(this, new OnTaskRet() {

            @Override
            public void onTaskRet(boolean arg0, int arg1, String arg2) {

            }
        });

    }

    /**
     * 初始化ImageLoader配置文件
     **/
    private void ImageLoader() {
        System.out.println("初始化 - ImageLoad方法");
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
                this.getApplicationContext())
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app

                .build();

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);

    }


    public void initUMSDK() {
        //开启debug模式，方便定位错误，具体错误检查方式可以查看http://dev.umeng.com/social/android/quick-integration的报错必看，正式发布，请关闭该模式
        Config.DEBUG = true;
        QueuedWork.isUseThreadPool = false;
//        UMShareAPI.get(this);
        {
            PlatformConfig.setWeixin("wx47b381963fec27aa", "d4624c36b6795d1d99dcf0547af5443d");
            PlatformConfig.setQQZone("1104520651", "xCNiMO2Fw1iyU1VU");
            PlatformConfig.setSinaWeibo("1673809322", "99879f5875e9169770aa5f4c46eb7f01", "http://www.gfedu.com/");
        }
    }


    public void initPolyvCilent() {
        //OPPO手机自动熄屏一段时间后，会启用系统自带的电量优化管理，禁止一切自启动的APP（用户设置的自启动白名单除外）。
        //如果startService异常，就会发送消息上来提醒异常了
        //如不需要额外处理，也可不接收此信息
        IntentFilter statusIntentFilter = new IntentFilter(AndroidService.SERVICE_START_ERROR_BROADCAST_ACTION);
        statusIntentFilter.addCategory(Intent.CATEGORY_DEFAULT);
        serviceStartErrorBroadcastReceiver = new ServiceStartErrorBroadcastReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(serviceStartErrorBroadcastReceiver, statusIntentFilter);

        //网络方式取得SDK加密串，（推荐）
//		new LoadConfigTask().execute();
        PolyvSDKClient client = PolyvSDKClient.getInstance();
        //设置SDK加密串
//		client.setConfig("你的SDK加密串", aeskey, iv);
        client.setConfig("JLzeybUrzzqMOra5K0Vi+7+/evHz5cviv9AWBS/3zhOTXgNlW/+vmD2ZBeXcwL6UKFibMxCHLQZrLJZHz+h+H+CMNIhQUGrXS3+kma4G298rOs9KcMrbl5X/l936FzP/3H0vaKVAwA1M0yJvTe+RwA==", aeskey, iv, getApplicationContext());
        //初始化数据库服务
        client.initDatabaseService(this);
        //启动服务
        client.startService(getApplicationContext(), PolyvDemoService.class);
//        //启动Bugly
//		client.initCrashReport(getApplicationContext());
//        //启动Bugly后，在学员登录时设置学员id
//		client.crashReportSetUserId(userId);
        //获取SD卡信息
        PolyvDevMountInfo.getInstance().init(this, new PolyvDevMountInfo.OnLoadCallback() {

            @Override
            public void callback() {
                if (PolyvDevMountInfo.getInstance().isSDCardAvaiable() == false) {
                    // TODO 没有可用的存储设备
                    Log.e(TAG, "没有可用的存储设备");
                    return;
                }

                StringBuilder dirPath = new StringBuilder();
                dirPath.append(PolyvDevMountInfo.getInstance().getSDCardPath()).append(File.separator).append("polyvdownload");
                File saveDir = new File(dirPath.toString());
                if (saveDir.exists() == false) {
                    saveDir.mkdir();
                }

                //如果生成不了文件夹，可能是外部SD卡需要写入特定目录/storage/sdcard1/Android/data/包名/
                if (saveDir.exists() == false) {
                    dirPath.delete(0, dirPath.length());
                    dirPath.append(PolyvDevMountInfo.getInstance().getSDCardPath()).append(File.separator).append("Android").append(File.separator).append("data")
                            .append(File.separator).append(getPackageName()).append(File.separator).append("polyvdownload");
                    saveDir = new File(dirPath.toString());
                    getExternalFilesDir(null); // 生成包名目录
                    saveDir.mkdirs();
                }

                PolyvSDKClient.getInstance().setDownloadDir(saveDir);
            }
        });

    }

    private class ServiceStartErrorBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String msg = intent.getStringExtra("msg");
            Log.e(TAG, msg);
        }
    }


}
