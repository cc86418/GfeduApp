package cn.jun.mysetting;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import cn.jun.bean.AddapPraiseBean;
import cn.jun.bean.Const;
import cn.jun.utils.HttpUtils;
import cn.jun.view.PingJiaDialog;
import cn.jun.view.PingJiaErrorDialog;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;

public class ShiZiOver extends Activity {
    private PingJiaDialog dialog;
    private PingJiaErrorDialog Errordialog;
    //用户ID
    private int UserID;
    private HttpUtils httpUtils = new HttpUtils();
    private TextView szpy_text;
    private ImageView iv_back;
    private Button submit_btn;
    private RadioGroup radioGroupID;
    private RadioButton rb_you, rb_liang, rb_yiban, rb_cha;
    //评议id
    private int Appraise_PKID;
    //评议选项 1.优秀   2.好  3.一般   4.差
    private int Appraise_OverallMerit;

    //返回数据
    private AddapPraiseBean addapPraise = new AddapPraiseBean();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shizipingyi_activity_over);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Appraise_PKID = bundle.getInt("Appraise_PKID");
        }
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        szpy_text = (TextView) findViewById(R.id.szpy_text);
        String a = "<font color='#333333'>" + "请对我们" + "</font>";
        String b = "<font color='#dd5555'>" + "班主任" + "</font>";
        String c = "<font color='#333333'>" + "的服务做出评价" + "</font>";
        szpy_text.setText(Html.fromHtml(a + b + c));

        rb_you = (RadioButton) findViewById(R.id.rb_you);
//        rb_you.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 1.优秀   2.好  3.一般   4.差
//                Appraise_OverallMerit = 1;
//
//            }
//        });
        rb_liang = (RadioButton) findViewById(R.id.rb_liang);
//        rb_liang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Appraise_OverallMerit = 2;
//            }
//        });
        rb_yiban = (RadioButton) findViewById(R.id.rb_yiban);
//        rb_yiban.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Appraise_OverallMerit = 3;
//            }
//        });
        rb_cha = (RadioButton) findViewById(R.id.rb_cha);
//        rb_cha.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Appraise_OverallMerit = 4;
//            }
//        });
        radioGroupID = (RadioGroup) findViewById(R.id.radioGroupID2);
        radioGroupID.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.i("Appraise ==> ", "111111111");
                if (rb_you.getId() == checkedId) {
                    Appraise_OverallMerit = 1;
                    Log.i("Appraise ==> ", "" + Appraise_OverallMerit);
                }
                if (rb_liang.getId() == checkedId) {
                    Appraise_OverallMerit = 2;
                    Log.i("Appraise ==> ", "" + Appraise_OverallMerit);
                }
                if (rb_yiban.getId() == checkedId) {
                    Appraise_OverallMerit = 3;
                    Log.i("Appraise ==> ", "" + Appraise_OverallMerit);
                }
                if (rb_cha.getId() == checkedId) {
                    Appraise_OverallMerit = 4;
                    Log.i("Appraise ==> ", "" + Appraise_OverallMerit);
                }
            }
        });

        submit_btn = (Button) findViewById(R.id.submit_btn);
        submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("Appraise_Ove == ", "" + Appraise_OverallMerit);
                Log.i("Appraise_PKID == ", "" + Appraise_PKID);
                if (0 != Appraise_OverallMerit && 0 != Appraise_PKID) {
                    if (httpUtils.isNetworkConnected(ShiZiOver.this)) {
                        SubmitPingYiTask submitPingYiTask = new SubmitPingYiTask();
                        submitPingYiTask.execute();
                    }
                }
            }
        });

    }

    private static final int MESSAGE_OK = 1;
    private static final int MESSAGE_ERROR = 0;

    private Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if (msg.what == MESSAGE_OK) {
                ShiZiOver.this.finish();
                dialog.dismiss();
            } else if (msg.what == MESSAGE_ERROR) {
                Errordialog.dismiss();
            }
            return true;
        }
    });

    class SubmitPingYiTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                UserID = LoginPre.getInt("S_ID", 0);
            }
//            UserID = 123898;
            addapPraise = httpUtils.getAddapPraiseAll(Const.URL + Const.SetOverAllMerit, UserID, Appraise_PKID, Appraise_OverallMerit);


            return null;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == addapPraise.getCode()) {
                dialog = new PingJiaDialog(ShiZiOver.this);
                dialog.show();
                handler.sendEmptyMessageDelayed(MESSAGE_OK, 3000);
            } else {//接口请求失败
                Errordialog = new PingJiaErrorDialog(ShiZiOver.this);
                Errordialog.show();
                handler.sendEmptyMessageDelayed(MESSAGE_ERROR, 3000);
            }
        }

    }

}
