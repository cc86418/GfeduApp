package cn.jun.bean;


import android.os.Environment;

import com.gensee.entity.InitParam;

import java.io.File;

public class ConfigApp {
    public static final String PARAMS_DOMAIN = "PARAMS_DOMAIN";
    public static final String PARAMS_TYPE = "PARAMS_TYPE";
    public static final String PARAMS_NUMBER = "PARAMS_NUMBER";
    public static final String PARAMS_ACCOUNT = "PARAMS_ACCOUNT";
    public static final String PARAMS_PWD = "PARAMS_PWD";
    public static final String PARAMS_NICKNAME = "PARAMS_NICKNAME";
    public static final String PARAMS_JOINPWD = "PARAMS_JOINPWD";
    public static final String PARAMS_K = "PARAMS_k";
    public static final String PARAMS_PLAY_TYPE = "PARAMS_PLAY_JC";
    public static final String PARAMS_SERVICE = "PARAMS_SERVICE";

    public static final String PARAMS_JOINSUCCESS = "PARAMS_JOINSUCCESS";
    public static final String PARAMS_VIDEO_FULLSCREEN = "PARAMS_VIDEO_FULLSCREEN";


    public static final String WEBCAST = "WEBCAST";
    public static final String TRAINING = "TRAINING";

    public static final String PREFERENCED = "DemoPrefereced";
    public static final String PLAY_LIVE = "直播(live)";
    public static final String PLAY_VOD = "点播(vod)";

    public static String ROOTPAHT = Environment.getExternalStorageDirectory()
            .getAbsolutePath() + File.separator + "playersdkdemo" + File.separator;
    public static String LOGPATH = ROOTPAHT + "log" + File.separator;
    private static ConfigApp ins;

    public static ConfigApp getIns() {
        if (ins == null) {
            synchronized (ConfigApp.class) {
                if (ins == null) {
                    ins = new ConfigApp();
                }
            }
        }
        return ins;
    }

    private ConfigApp(){}
    private InitParam initParam;


    public InitParam getInitParam() {
        return initParam;
    }


    public void setInitParam(InitParam initParam) {
        this.initParam = initParam;
    }
}
