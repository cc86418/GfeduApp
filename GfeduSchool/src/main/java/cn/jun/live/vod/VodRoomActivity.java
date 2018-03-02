package cn.jun.live.vod;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.widget.Toast;

import com.gensee.common.ServiceType;
import com.gensee.entity.ChatMsg;
import com.gensee.entity.InitParam;
import com.gensee.entity.QAMsg;
import com.gensee.entity.VodObject;
import com.gensee.vod.VodSite;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jun.bean.ConfigApp;
import cn.jun.bean.VodRoomBean;
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

public class VodRoomActivity extends Activity implements VodSite.OnVodListener {
    //本地存储
    private SharedPreferences sp;
    //用户ID
    private int userId;
    //课表id
    private int scheduleId;
    //课程id
    private int classId;
    //点播间参数
    private String setDomain;
    private int VodCount;
    private ArrayList<String> VodNumberList;
    private String vod_vid;
    private String setLoginAccount;
    private String setLoginPwd;
    private String setJoinPwd;
    private VodSite vodSite;
    private ServiceType serviceType = ServiceType.WEBCAST;
    private String playType = ConfigApp.PLAY_VOD;//直播/点播
    private static final String EXTRA = "_vod";

    private String Void;

    public static List<QAMsg> QAList;
    private InitParam initParam;

    private DialogInterface mDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.vod_room);

//         initParam =  ConfigApp.getIns().getInitParam();

//        scheduleId = 134;
//        classId = 181;
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            scheduleId = bundle.getInt("scheduleId");
            classId = bundle.getInt("classId");
        }

        Log.d("scheduleId -- ", "" + scheduleId);
        Log.d("classId -- ", "" + classId);
        sp = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);

        if (HttpUtils.isNetworkConnected(this)) {
            initDate();
        }
    }

    private void initDate() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostServer httpPostService = retrofit.create(HttpPostServer.class);
        RequestBody body = commParam();
        Observable<CommonBean<VodRoomBean>> observable = httpPostService.getVodRoom(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<VodRoomBean>>() {
                    @Override
                    public void onCompleted() {
//                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(VodRoomActivity.this, "网络异常，请返回重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<VodRoomBean> vodRoomBean) {
                        if (100 == vodRoomBean.getCode()) {
                            Log.i(" ==== ", "" + vodRoomBean.getMessage());
                            setDomain = vodRoomBean.getBody().getSetDomain();
                            VodCount = vodRoomBean.getBody().getVodCount();
                            VodNumberList = vodRoomBean.getBody().getVodNumberList();
//                            setLoginAccount = vodRoomBean.getBody().getSetLoginAccount();
                            setLoginAccount = "admin@gfedu.com";
                            setLoginPwd = "gfedu123";
//                            setLoginPwd = vodRoomBean.getBody().getSetLoginPwd();
                            setJoinPwd = vodRoomBean.getBody().getSetJoinPwd();
                            if (VodCount > 0) {
                                if (VodCount > 1) {
                                    dialogList(VodNumberList);
                                } else {
                                    vod_vid = VodNumberList.get(0);
                                    initParam = getInitParam();
                                }
                            }
                        } else {
                            Toast.makeText(VodRoomActivity.this, vodRoomBean.getMessage(), Toast.LENGTH_SHORT).show();
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
            obj.put("classid", classId);
            obj.put("scheduleId", scheduleId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }


    public InitParam getInitParam() {
//        String domain = "webcast.gfedu.cn";
////        String number = "45497801";
//        String number = "01178808";
//        String account = "admin@gfedu.com";
//        String accountPwd = "gfedu123";
//        String joinPwd = "111111";

        String domain = setDomain;
        String number = vod_vid;
        String account = setLoginAccount;
        String accountPwd = setLoginPwd;
        String joinPwd = setJoinPwd;
        String nickName = "test";
        String kValue = "";
        InitParam initParam = new InitParam();
        // 设置域名
        initParam.setDomain(domain);
        if (number.length() == 8 && isNumber(number)) {//此判断是为了测试方便，请勿模仿，实际使用时直接使用其中一种设置
            // 设置编号,8位数字字符串，
            initParam.setNumber(number);
        } else {
            // 如果只有直播间id（混合字符串
            // 如：8d5261f20870499782fb74be021a7e49）可以使用setLiveId("")代替setNumber()
            String liveId = number;
            initParam.setLiveId(liveId);

        }
        // 设置站点登录帐号（根据配置可选）
        initParam.setLoginAccount(account);
        // 设置站点登录密码（根据配置可选）
        initParam.setLoginPwd(accountPwd);
        // 设置显示昵称 不能为空,请传入正确的昵称，有显示和统计的作用
        // 设置显示昵称，如果设置为空，请确保
        initParam.setNickName(nickName);
        // 设置加入口令（根据配置可选）
        initParam.setJoinPwd(joinPwd);
        // 设置服务类型，如果站点是webcast类型则设置为ServiceType.ST_CASTLINE，
        // training类型则设置为ServiceType.ST_TRAINING
        initParam.setServiceType(serviceType);
        //如果启用第三方认证，必填项，且要正确有效

//		initParam.setUserId(1000000001);//大于1000000000有效
        //站点 系统设置 的 第三方集成 中直播模块 “认证“  启用时请确保”第三方K值“（你们的k值）的正确性 ；如果没有启用则忽略这个参数
//        initParam.setK(k);

//		initParam.setUserData("vip=1&city=上海");
        mHandler.sendMessage(mHandler
                .obtainMessage(RESULT_JC.REDY));
        return initParam;
    }

    /**
     * 聊天记录 getChatHistory 响应 vodId 点播id list 聊天记录
     */
    @Override
    public void onChatHistory(String s, List<ChatMsg> list, int i, boolean b) {
        //GenseeLog.d(TAG, "onChatHistory vodId = " + vodId + " " + list);
        // ChatMsg msg;
        // msg.getContent();//消息内容
        // msg.getSenderId();//发送者用户id
        // msg.getSender();//发送者昵称
        // msg.getTimeStamp();//发送时间，单位毫秒
    }

    /**
     * 问答记录 getQaHistory 响应 list 问答记录 vodId 点播id
     */
    @Override
    public void onQaHistory(String vodId, List<QAMsg> list, int pageIndex, boolean more) {
        Log.i("问答记录 ==> ", " " + vodId + " " + list);
        QAList = list;
        if (more) {
            // 如果还有更多的历史，还可以继续获取记录（pageIndex+1）页的记录
        }
        // QAMsg msg;
        // msg.getQuestion();//问题
        // msg.getQuestId();//问题id
        // msg.getQuestOwnerId();//提问人id
        // msg.getQuestOwnerName();//提问人昵称
        // msg.getQuestTimgstamp();//提问时间 单位毫秒
        //
        // msg.getAnswer();//回复的内容
        // msg.getAnswerId();//“本条回复”的id 不是回答者的用户id
        // msg.getAnswerOwner();//回复人的昵称
        // msg.getAnswerTimestamp();//回复时间 单位毫秒
    }

    // int ERR_DOMAIN = -100; // ip(domain)不正确
    // int ERR_TIME_OUT = -101; // 超时
    // int ERR_UNKNOWN = -102; // 未知错误
    // int ERR_SITE_UNUSED = -103; // 站点不可用
    // int ERR_UN_NET = -104; // 无网络
    // int ERR_DATA_TIMEOUT = -105; // 数据过期
    // int ERR_SERVICE = -106; // 服务不正确
    // int ERR_PARAM = -107; // 参数不正确
    // int ERR_PARAM = -107; // 参数不正确
    // int ERR_THIRD_CERTIFICATION_AUTHORITY //第三方认证失败
    // int ERR_UN_INVOKE_GETOBJECT = -201; //没有调用getVodObject
    // int ERR_VOD_INTI_FAIL = 14; //点播getVodObject失败
    // int ERR_VOD_NUM_UNEXIST = 15; //点播编号不存在或点播不存在
    // int ERR_VOD_PWD_ERR = 16; //点播密码错误
    // int ERR_VOD_ACC_PWD_ERR = 17; //帐号或帐号密码错误
    // int ERR_UNSURPORT_MOBILE = 18; //不支持移动设备
    @Override
    public void onVodErr(int i) {

    }

    /**
     * getVodObject的响应，vodId 接下来可以下载后播放
     */
    @Override
    public void onVodObject(String vodId) {
//        GenseeLog.d(TAG, "onVodObject vodId = " + vodId);
        Log.i("onVodObject ====== ", " onVodObject ");
        Void = vodId;
        mHandler.sendMessage(mHandler
                .obtainMessage(RESULT_JC.ON_GET_VODOBJ, vodId));

        //获取问答记录
        vodSite.getQaHistory(Void, 1);

    }

    /**
     * 获取点播详情
     */
    @Override
    public void onVodDetail(VodObject vodObj) {
//        GenseeLog.d(TAG, "onVodDetail " + vodObj);
        Log.i("获取点播详情 ----- ", "" + vodObj);
        if (vodObj != null) {
            vodObj.getDuration();// 时长
            vodObj.getEndTime();// 录制结束时间 始于1970的utc时间毫秒数
            vodObj.getStartTime();// 录制开始时间 始于1970的utc时间毫秒数
            vodObj.getStorage();// 大小 单位为Byte
        }
    }

    public interface RESULT_JC {
        int DIALOG = 102;
        int ON_GET_VODOBJ = 100;
        int REDY = 101;
    }

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case RESULT_JC.REDY:
                    Log.i("RESULT_JC ====== ", " REDY ");
                    Gson s = new Gson();

                    Log.i(" 数据。。。。 ", "" + s.toJson(initParam).toString());
                    vodSite = new VodSite(VodRoomActivity.this);
                    vodSite.setVodListener(VodRoomActivity.this);
                    vodSite.getVodObject(initParam);
                    break;

                case RESULT_JC.ON_GET_VODOBJ:
                    final String vodId = (String) msg.obj;
                    // 在线播放
                    Intent vod = new Intent(VodRoomActivity.this,
                            PlayActivity.class);
                    vod.putExtra("play_param", vodId);
                    Bundle bundle = new Bundle();
                    bundle.putInt("scheduleId", scheduleId);
                    vod.putExtras(bundle);
                    startActivity(vod);
                    finish();
                    if (null != mDialog) {
                        mDialog.dismiss();
                    }
                    break;

                case RESULT_JC.DIALOG:
                    mDialog.dismiss();
                    break;

                default:
                    break;
            }
        }

    };


    private boolean isNumber(String number) {
        try {
            Long.parseLong(number);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }


    /**
     * 列表
     */
    private void dialogList(ArrayList<String> mList) {
//        final String items[] = {"刘德华", "张柏芝", "蔡依林", "张学友"};
        final ArrayList<String> mNameList = new ArrayList<>();
        final String[] items = (String[]) mList.toArray(new String[mList.size()]);//使用了第二种接口，返回值和参数均为结果
        for (int i = 0; i < items.length; i++) {
            mNameList.add("回放 " + i);
        }

        final String[] itemNames = (String[]) mNameList.toArray(new String[mNameList.size()]);
        AlertDialog.Builder builder = new AlertDialog.Builder(this, 3);
        builder.setTitle("回放");
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setItems(itemNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                mDialog = dialog;
                vod_vid = items[which];
                initParam = getInitParam();
//                Toast.makeText(VodRoomActivity.this, items[which],
//                        Toast.LENGTH_SHORT).show();
//                mHandler.sendMessage(mHandler
//                        .obtainMessage(RESULT_JC.DIALOG));

            }
        });
        builder.setPositiveButton("返回", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                finish();
            }
        });

        builder.setCancelable(false);
        builder.create().show();
    }
}
