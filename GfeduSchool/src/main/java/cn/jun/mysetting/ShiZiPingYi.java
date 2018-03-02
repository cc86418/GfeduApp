package cn.jun.mysetting;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.jun.bean.ClassScheduleBean;
import cn.jun.utils.HttpPostServer;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ShiZiPingYi extends Activity implements View.OnClickListener {
    //用户ID
    private int UserID;
    //工具
    private HttpUtils httpUtils = new HttpUtils();
    //返回
    private RelativeLayout backLayout;
    //listView
    private RecyclerView sz_list;
    private SZPY_RecyclerAdapter sz_adapter;

    //数据源
//    private ClassScheduleBean ClassScheduleBean = new ClassScheduleBean();
    private ArrayList<cn.jun.bean.ClassScheduleBean.ClassScheduleList> mlist;

    private int type;

    public static Dialog mDialog;

    private RelativeLayout no_pingyiLayout;

    public void showProcessDialog(Activity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shizipingyi_activity);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            type = bundle.getInt("type");
        }

        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);

//        if (httpUtils.isNetworkConnected(this)) {
//            initData();
//        }
    }

    @Override
    protected void onResume() {
        initData();
        super.onResume();
    }

    private void initData() {
//        showProcessDialog(this,
//                R.layout.loading_show_dialog_color);
//        ShiZiPingYiTask shiZiPingYiTask = new ShiZiPingYiTask();
//        shiZiPingYiTask.execute();
        if (httpUtils.isNetworkConnected(this)) {
            SubmitDate();
        }

    }


    private void SubmitDate() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostServer httpPostService = retrofit.create(HttpPostServer.class);
        RequestBody body = commParam();
        Observable<CommonBean<ClassScheduleBean>> observable = httpPostService.getClassScheduleBean(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<ClassScheduleBean>>() {
                    @Override
                    public void onCompleted() {
//                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(ShiZiPingYi.this, "请检查你的网络，重新提交", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<ClassScheduleBean> classScheduleBean) {
                        if (100 == classScheduleBean.getCode()) {
                            if (classScheduleBean.getBody().getListCount() > 0) {
                                mlist = new ArrayList<>();
                                mlist = classScheduleBean.getBody().getClassScheduleList();
                                initView();
                            } else {//无数据
                                NoInitView();
                            }
                        } else {
                            Toast.makeText(ShiZiPingYi.this, classScheduleBean.getMessage(), Toast.LENGTH_SHORT).show();
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
            SharedPreferences sp = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            int userId = sp.getInt("S_ID", 0);
            obj.put("userId", userId);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }

//    class ShiZiPingYiTask extends AsyncTask<Void, Void, Void> {
//        @Override
//        protected Void doInBackground(Void... arg0) {
//            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
//            if (null != LoginPre && !"".equals(LoginPre)) {
//                UserID = LoginPre.getInt("S_ID", 0);
//            }
//            ClassScheduleBean = httpUtils.getclassschedulelist(Const.URL + Const.GetClassScheduleAPI, UserID);
//
//            return null;
//        }
//
//        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//        @SuppressLint("NewApi")
//        protected void onPostExecute(Void result) {
//            super.onPostExecute(result);
//            if (100 == ClassScheduleBean.getCode()) {
//                if (ClassScheduleBean.getBody().getListCount() > 0) {
//                    mlist = new ArrayList<>();
//                    mlist.add(ClassScheduleBean);
//                    initView();
//                } else {//无数据
//                    NoInitView();
//                }
//
//            } else {//接口请求失败
//                Toast.makeText(ShiZiPingYi.this, ClassScheduleBean.getMessage(), Toast.LENGTH_SHORT).show();
//            }
//
//            mDialog.dismiss();
//        }
//
//    }

    public void NoInitView() {
        no_pingyiLayout = (RelativeLayout) findViewById(R.id.no_pingyiLayout);
        no_pingyiLayout.setVisibility(View.VISIBLE);

        sz_list = (RecyclerView) findViewById(R.id.sz_list);
        sz_list.setVisibility(View.GONE);
    }

    public void initView() {
        no_pingyiLayout = (RelativeLayout) findViewById(R.id.no_pingyiLayout);
        no_pingyiLayout.setVisibility(View.GONE);


        sz_list = (RecyclerView) findViewById(R.id.sz_list);
        sz_list.setVisibility(View.VISIBLE);

        LinearLayoutManager mLayoutManager = new LinearLayoutManager(this);
        sz_list.setLayoutManager(mLayoutManager);
        sz_list.setNestedScrollingEnabled(false);
        sz_adapter = new SZPY_RecyclerAdapter(this, mlist);
        sz_list.setAdapter(sz_adapter);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                Log.i("type -- ", "" + type);
                if (0 == type) {
                    Intent it = new Intent();
                    setResult(3, it);
                    finish();
                } else {
                    finish();
                }
                break;


        }
    }
}
