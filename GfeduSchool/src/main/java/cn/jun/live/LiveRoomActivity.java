package cn.jun.live;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.gensee.common.ServiceType;
import com.gensee.entity.InitParam;
import com.gensee.fastsdk.GenseeLive;
import com.gensee.fastsdk.core.GSFastConfig;

import org.json.JSONObject;

import cn.jun.bean.LiveRoomBean;
import cn.jun.utils.HttpPostServer;
import jc.cici.android.R;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.tiku.HttpUtils;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class LiveRoomActivity extends Activity {
    //本地存储
    private SharedPreferences sp;
    //用户ID
    private int userId;
    //课表id
    private int scheduleId;
    //直播间参数
    private String setDomain;
    private String setNumber;
    private String setLoginAccount;
    private String setLoginPwd;
    private String setJoinPwd;
    private String UniqueName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.live_room);

//        scheduleId = 134;
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            scheduleId = bundle.getInt("scheduleId");
        }

        Log.d("scheduleId -- ", "" + scheduleId);

        sp = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);
        if (HttpUtils.isNetworkConnected(this)) {
            initDate();
            //test
//            setDomain = "webcast.gfedu.cn";
//            setNumber = "48973208";
//            setLoginAccount = "admin@gfedu.com";
//            setLoginPwd = "gfedu123";
//            setJoinPwd = "111111";
//            UniqueName = "张老师123897";
//            initJoinLiveRoom();
        }

    }

    private void initDate() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostServer httpPostService = retrofit.create(HttpPostServer.class);
        RequestBody body = commParam();
        Observable<CommonBean<LiveRoomBean>> observable = httpPostService.getLiveRoom(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<LiveRoomBean>>() {
                    @Override
                    public void onCompleted() {
//                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(LiveRoomActivity.this, "网络异常，请返回重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<LiveRoomBean> liveRoomBean) {
                        if (100 == liveRoomBean.getCode()) {
                            Log.i("获取直播参数 --- ", " --- ");
                            setDomain = liveRoomBean.getBody().getSetDomain();
                            setNumber = liveRoomBean.getBody().getSetNumber();
//                            setLoginAccount = liveRoomBean.getBody().getSetLoginAccount();
                            setLoginAccount = "admin@gfedu.com";
                            setLoginPwd = "gfedu123";
//                            setLoginPwd = liveRoomBean.getBody().getSetLoginPwd();
                            setJoinPwd = liveRoomBean.getBody().getSetJoinPwd();
                            UniqueName = liveRoomBean.getBody().getUniqueName();
                            initJoinLiveRoom();
                        } else {
                            Toast.makeText(LiveRoomActivity.this, liveRoomBean.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }


    //公共参数
    private RequestBody commParam() {
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
            obj.put("scheduleId", scheduleId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }


    private void initJoinLiveRoom() {
        Log.i("setDomain ", " --- "+setDomain);
        Log.i("setNumber ", " --- "+setNumber);
        Log.i("setLoginAccount ", " --- "+setLoginAccount);
        Log.i("setLoginPwd ", " --- "+setLoginPwd);
        Log.i("UniqueName ", " --- "+UniqueName);
        Log.i("setJoinPwd ", " --- "+setJoinPwd);
        GSFastConfig gsFastConfig = new GSFastConfig();
        InitParam initParam = new InitParam();
        //若一个url为http://test.gensee.com/site/webcast   域名是“test.gensee.com”
        initParam.setDomain(setDomain);
        //设置对应编号，如果是点播则是点播编号，是直播便是直播编号。
        //请注意不要将id理解为编号。
        //作用等价于id，但不是id。有id可以不用编号，有编号可以不用id
        initParam.setNumber(setNumber);
        //设置站点认证账号 即登录站点的账号
        initParam.setLoginAccount(setLoginAccount);
        //设置站点认证密码 即登录站点的密码,如果后台设置直播需要登录或点播需要登录，那么登录密码要正确  且帐号同时也必须填写正确
        initParam.setLoginPwd(setLoginPwd);
        //设置昵称  用于直播间显示或统计   一定要填写
        initParam.setNickName(UniqueName);
        //可选 如果后台设置了保护密码 请填写对应的口令
        initParam.setJoinPwd(setJoinPwd);
        //第三方认证K值，如果启用第三方集成的时候必须传入有效的K值
//        initParam.setK(k);
        //若一个url为http://test.gensee.com/site/webcast ,serviceType是 ServiceType.WEBCAST,
        //url为http://test.gensee.com/site/training,serviceTypeserviceType是 ServiceType.TRAINING
        initParam.setServiceType(ServiceType.WEBCAST);
        //isPublishMode  true=发布端，false=观看端
        gsFastConfig.setPublish(false);
        //观看模式
//        GSFastConfig.WATCH_SCREEN_MODE_VIDEO_DOC;//文档+视频
//        GSFastConfig.WATCH_SCREEN_MODE_PORTRAIT;//竖屏模式
        gsFastConfig.setWatchScreenMode(GSFastConfig.WATCH_SCREEN_MODE_VIDEO_DOC);
//        gsFastConfig.setShowPIP(false);
        //发布端
//        gsFastConfig.setPublishScreenMode(defPubScreenMode);//观看模式横屏竖屏
//        gsFastConfig.setHardEncode(defPubHardEncode);//硬解软解
//        gsFastConfig.setPubQuality(defQuality);//高清标清
//        //配置观看端打赏固定金额面板,最多支持6个固定金额,固定金额最高不能超过200000(即2000.00元)
//        gsFastConfig.setFixedMoneyArray(arr);

        //分屏观看端,界面配置
        gsFastConfig.setShowDoc(true);//文档是否显示
        gsFastConfig.setShowChat(true);//聊天是否显示
        gsFastConfig.setShowQa(true);//问答是否显示
        gsFastConfig.setShowPIP(false);//全屏模式画中画是否显示
        gsFastConfig.setShowHand(false);//举手是否显示
        gsFastConfig.setShownetSwitch(false);//优选网络是否显示
        gsFastConfig.setShowDanmuBtn(false);//弹幕是否显示
        gsFastConfig.setSkinType(1);//换肤功能(0:晚上,1:白天)
        GenseeLive.startLive(LiveRoomActivity.this, gsFastConfig, initParam);
        LiveRoomActivity.this.finish();

    }


    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
