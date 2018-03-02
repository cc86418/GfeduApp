package cn.jun.mysetting;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.jun.adapter.OverClassAdapter;
import cn.jun.bean.Const;
import cn.jun.bean.OverClassListBean;
import cn.jun.utils.HttpUtils;
import cn.jun.view.ShowNullDialog;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;

public class OverClassListActivity extends Activity implements View.OnClickListener {
    private ImageView iv_back;
    private RelativeLayout back_re;
    private ListView over_listview;
    private OverClassAdapter adapter;
    private HttpUtils httpUtils = new HttpUtils();
    private OverClassListBean overClassListBean;
    private ArrayList<OverClassListBean> mList = new ArrayList<>();
    private String UserID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.over_class_list);
        initView();
        if (httpUtils.isNetworkConnected(this)) {
            initData();
        } else {
            Toast.makeText(this, "网络连接中断!", Toast.LENGTH_SHORT).show();
        }

    }

    public void initView() {
        back_re = (RelativeLayout) findViewById(R.id.back_re);
        back_re.setOnClickListener(this);
        iv_back = (ImageView) findViewById(R.id.iv_back);
//        iv_back.setOnClickListener(this);
        over_listview = (ListView) findViewById(R.id.over_listview);
    }

    public void initData() {
        OverClassTask overClassTask = new OverClassTask();
        overClassTask.execute();
    }

    class OverClassTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                int SID = LoginPre.getInt("S_ID", 0);
                UserID = Integer.toString(SID);
            }
            overClassListBean = httpUtils.getOverClassListBean
                    (Const.URL + Const.OverClassListAPI, Const.CLIENT, Const.VERSION, Const.APPNAME, UserID);
//            overClassListBean = httpUtils.getOverClassListBean
//                    (Const.URL + Const.OverClassListAPI, Const.CLIENT, Const.VERSION, Const.APPNAME, "123898");

            return null;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if ("100".equals(overClassListBean.getCode())) {
                if (null != overClassListBean.getBody().getEndList() && overClassListBean.getBody().getEndList().size() > 0) {
                    mList.add(overClassListBean);
                    adapter = new OverClassAdapter(OverClassListActivity.this, mList);
                    Gson g = new Gson();
                    Log.i("已结束课程 1 ", "" + g.toJson(overClassListBean).toString());
                    Log.i("已结束课程 2 ", "" + g.toJson(mList).toString());
                    over_listview.setAdapter(adapter);
                } else {//请求成功,但是没有数据
                    ShowDialog("暂无已结束课程，请点击确定返回");
                }
            } else {//请求失败
                ShowDialog("获取课程失败,稍后再试");
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.back_re:
                finish();
                break;
        }

    }

    //弹出对话框
    private void ShowDialog(String content) {
        final ShowNullDialog nullDialog = new ShowNullDialog(this, content);
        nullDialog.show();
        nullDialog.setonClick(new ShowNullDialog.ICoallBack() {
            @Override
            public void onClickOkButton(String s) {
                if ("null".equals(s)) {
                    nullDialog.dismiss();
                    OverClassListActivity.this.finish();
                }
            }
        });
    }
}
