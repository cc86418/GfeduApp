package cn.jun.live;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import jc.cici.android.R;
import jc.cici.android.atom.common.Global;

public class MyLiveActivity extends FragmentActivity implements View.OnClickListener {
    private RelativeLayout backLayout;
    private LinearLayout jq_click, ls_click;
    private static FragmentTransaction ft;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mylive_activity);

        initView();



    }

    public void initView() {
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        jq_click = (LinearLayout) findViewById(R.id.jq_click);
        ls_click = (LinearLayout) findViewById(R.id.ls_click);
        jq_click.setOnClickListener(this);
        ls_click.setOnClickListener(this);
        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new JinQiFragment());
        ft.commit();
        jq_click.setBackgroundResource(R.drawable.btn_jinqizhibo_h);
        ls_click.setBackgroundResource(R.drawable.btn_lishizhibo_n);
    }

    @Override
    public void onClick(View v) {
        ft = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;
            case R.id.jq_click:
                ft.replace(R.id.content, new JinQiFragment());
                jq_click.setBackgroundResource(R.drawable.btn_jinqizhibo_h);
                ls_click.setBackgroundResource(R.drawable.btn_lishizhibo_n);
                break;

            case R.id.ls_click:
                ft.replace(R.id.content, new LiShiFragment());
                jq_click.setBackgroundResource(R.drawable.btn_jinqizhibo_n);
                ls_click.setBackgroundResource(R.drawable.btn_lishizhibo_h);
                break;
        }
        ft.commit();
    }


    private boolean getUserID() {
        SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        if (null != LoginPre && !"".equals(LoginPre)) {
            int UserID = LoginPre.getInt("S_ID", 0);
            if (0 == UserID) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }
}
