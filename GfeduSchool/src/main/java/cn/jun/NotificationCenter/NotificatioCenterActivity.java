package cn.jun.NotificationCenter;


import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.gfedu.gfeduapp.MainActivity;
import jc.cici.android.R;


public class NotificatioCenterActivity extends FragmentActivity implements View.OnClickListener {
    //返回
    private ImageView iv_back;
    private RelativeLayout xx_click;
    private LinearLayout wd_click;
    private TextView xx_lable, wd_lable;
    private ImageView left_red_line, right_red_line;
    private static FragmentTransaction ft;
    //未读数量
    private static TextView noRead_btn_count;
    private int noReadCount;
    //跳转标记
    private String jumpflag;

    public static Handler HanlerCount = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    Log.i("HanlerCount", "" + NotificatioFragment.noReadCount);
                    noRead_btn_count.setText("" + NotificatioFragment.noReadCount);
                    break;

            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification_center);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            jumpflag = bundle.getString("jumpflag");
        }

        initView();


    }

    public void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        xx_click = (RelativeLayout) findViewById(R.id.xx_click);
        wd_click = (LinearLayout) findViewById(R.id.wd_click);
        xx_lable = (TextView) findViewById(R.id.xx_lable);
        wd_lable = (TextView) findViewById(R.id.wd_lable);
        left_red_line = (ImageView) findViewById(R.id.left_red_line);
        right_red_line = (ImageView) findViewById(R.id.right_red_line);

        noRead_btn_count = (TextView) findViewById(R.id.noRead_btn_count);

        iv_back.setOnClickListener(this);
        xx_click.setOnClickListener(this);
        wd_click.setOnClickListener(this);

        ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content, new NotificatioFragment());
        ft.commit();
        xx_lable.setTextColor(Color.parseColor("#dd5555"));
        wd_lable.setTextColor(Color.parseColor("#666666"));
        left_red_line.setVisibility(View.VISIBLE);
        right_red_line.setVisibility(View.GONE);


        noRead_btn_count.setText("" + NotificatioFragment.noReadCount);
    }

    @Override
    public void onClick(View v) {
        ft = getSupportFragmentManager().beginTransaction();
        switch (v.getId()) {
            case R.id.iv_back:
                if ("1".equals(jumpflag)) {
                    finish();
                } else {
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }

                break;

            case R.id.xx_click:
                ft.replace(R.id.content, new NotificatioFragment());
                xx_lable.setTextColor(Color.parseColor("#dd5555"));
                wd_lable.setTextColor(Color.parseColor("#666666"));
                left_red_line.setVisibility(View.VISIBLE);
                right_red_line.setVisibility(View.GONE);
                break;

            case R.id.wd_click:
                ft.replace(R.id.content, new ReadNotificationFragment());
                wd_lable.setTextColor(Color.parseColor("#dd5555"));
                xx_lable.setTextColor(Color.parseColor("#666666"));
                left_red_line.setVisibility(View.GONE);
                right_red_line.setVisibility(View.VISIBLE);
                break;

        }
        ft.commit();
    }

}
