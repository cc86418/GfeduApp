package cn.jun.NotificationCenter;


import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import jc.cici.android.R;

public class NotifiInfoActivity extends Activity {
    //返回
    private ImageView iv_back;
    //图表
    private ImageView iv_icon;
    //标题
    private TextView title;
    private TextView msg_type;
    private String type;
    //内容
    private TextView msg_tv;
    private String content;
    //时间
    private TextView msg_time;
    private String time;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.noticaf_info);

        Bundle bundle = getIntent().getExtras();
        content = bundle.getString("content");
        time = bundle.getString("time");
        type = bundle.getString("type");

        initView();
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        iv_icon = (ImageView) findViewById(R.id.iv_icon);
        if ("系统通知".equals(type)) {
            iv_icon.setBackgroundResource(R.drawable.xitongtongzhi_icon);
        } else {
            iv_icon.setBackgroundResource(R.drawable.kechengtixing_icon);
        }
        title = (TextView) findViewById(R.id.title);
        title.setText(type);
        msg_type = (TextView) findViewById(R.id.msg_type);
        msg_type.setText(type);
        msg_tv = (TextView) findViewById(R.id.msg_tv);
        msg_tv.setText(content);
        msg_time = (TextView) findViewById(R.id.msg_time);
        msg_time.setText(time);

    }

}
