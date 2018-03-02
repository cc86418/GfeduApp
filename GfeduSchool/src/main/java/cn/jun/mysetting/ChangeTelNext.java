package cn.jun.mysetting;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import cn.jun.bean.ChangeTelBean;
import cn.jun.bean.Const;
import cn.jun.utils.HttpUtils;
import cn.jun.utils.PublicFunc;
import jc.cici.android.R;


public class ChangeTelNext extends Activity implements View.OnClickListener {
    //返回
    private RelativeLayout backLayout;
    //输入框
    private EditText tel_edit, yzm_edit;
    private String telString = "", yzmString = "";
    //错误提示
    private TextView tv_error, tv_error2;
    //手机号边框
    private RelativeLayout tel_layout_bg;
    //验证码边框
    private RelativeLayout yzm_layout_bg;
    //按钮
    private Button yzm_btn, ok_button;
    // 获取短信验证码倒数状态
    private TimeCount time;
    //保存临时获取的验证码
    private String TempYzm;
    //保存获取验证码的手机号
    private String TempTelPhone;
    // 短信验证返回码
//    private String Msg_ResultCode = null;
    // MD5
    private PublicFunc MD5 = new PublicFunc();
    // Md5参数
    private String MD5Result;
    // &MD5code=
    private String MD5_Code = "&MD5code=";
    //工具类
    private HttpUtils httpUtils = new HttpUtils();
    //数据源
    private ChangeTelBean changeTelBean;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_telnext);
        initView();
        time = new TimeCount(60000, 1000);


    }

    public void initView() {
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        tel_layout_bg = (RelativeLayout) findViewById(R.id.edit_layout_bg);
        yzm_layout_bg = (RelativeLayout) findViewById(R.id.yzm_layout_bg);
        tv_error = (TextView) findViewById(R.id.tv_error);
        tv_error2 = (TextView) findViewById(R.id.tv_error2);
        tel_edit = (EditText) findViewById(R.id.tel_edit);
        yzm_edit = (EditText) findViewById(R.id.yzm_edit);
        yzm_btn = (Button) findViewById(R.id.yzm_btn);
        yzm_btn.setOnClickListener(this);
        ok_button = (Button) findViewById(R.id.ok_button);
        ok_button.setOnClickListener(this);
    }

    public void initData() {
        telString = tel_edit.getText().toString().trim();
//        yzmString  = yzm_edit.getText().toString().trim();
        if (!"".equals(telString)) {
            if (true == isMobileNO(telString)) {
                tel_layout_bg.setBackgroundResource(R.drawable.edit_background_all);
                tel_edit.setBackgroundResource(R.drawable.edit_bg_all);
                tv_error.setText("");
                tv_error.setVisibility(View.GONE);
                //发送验证码
                if (httpUtils.isNetworkConnected(this)) {
                    SetTelMessageTask setMessagetask = new SetTelMessageTask();
                    setMessagetask.execute();
                }
            } else {
                tel_layout_bg.setBackgroundResource(R.drawable.edit_editerror_background);
                tel_edit.setBackgroundResource(R.drawable.edit_bg_all_error);
                tv_error.setText("手机号格式不正确");
                tv_error.setVisibility(View.VISIBLE);
            }
        } else {
            tel_layout_bg.setBackgroundResource(R.drawable.edit_editerror_background);
            tel_edit.setBackgroundResource(R.drawable.edit_bg_all_error);
            tv_error.setText("请输入手机号");
            tv_error.setVisibility(View.VISIBLE);
        }
    }

    public void initOKBtn() {
        yzmString = yzm_edit.getText().toString().trim();
        telString = tel_edit.getText().toString().trim();
        if (!"".equals(yzmString) && !"".equals(telString)) {
            if (TempTelPhone.equals(telString)) {
                tel_layout_bg.setBackgroundResource(R.drawable.edit_background_all);
                tel_edit.setBackgroundResource(R.drawable.edit_bg_all);
                tv_error.setText("");
                tv_error.setVisibility(View.GONE);
                //确认更改
                if (httpUtils.isNetworkConnected(this)) {

                }
            } else {
                tel_layout_bg.setBackgroundResource(R.drawable.edit_editerror_background);
                tel_edit.setBackgroundResource(R.drawable.edit_bg_all_error);
                tv_error.setText("请确认手机号和验证码是否正确");
                tv_error.setVisibility(View.VISIBLE);
            }
        } else {
            tel_layout_bg.setBackgroundResource(R.drawable.edit_editerror_background);
            tel_edit.setBackgroundResource(R.drawable.edit_bg_all_error);
            tv_error.setText("请输入手机号和验证码");
            tv_error.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 定时器发送短信验证码
     **/
    class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
        }

        @Override
        public void onTick(long millisUntilFinished) {
            yzm_btn
                    .setBackgroundResource(R.drawable.yzm_message_onclikc);
            yzm_btn.setClickable(false);
            // register_get_message_btn.setText(millisUntilFinished / 1000
            // + "秒后可重新发送");
            yzm_btn.setText("重新发送  " + "("
                    + millisUntilFinished / 1000 + ")");
        }

        @Override
        public void onFinish() {
            yzm_btn.setText("重新获取验证码");
            yzm_btn.setClickable(true);
            yzm_btn.setBackgroundResource(R.drawable.btn_input_entry);
        }
    }

    /**
     * 获取短信验证码异步
     **/
    class SetTelMessageTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... arg0) {
            String tel = arg0[0];
            String MD5String = httpUtils.getMD5Str2(tel);
            changeTelBean = httpUtils.getChangeTelMsg(Const.URL + Const.GetMobleCodeAPI
                    , tel, MD5String, Const.CLIENT);
            Log.d(" ---- > ", changeTelBean.getCode() + "");
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            MessageResultCode();
        }
    }

    public void MessageResultCode() {
        if (100 == changeTelBean.getCode()) {
            TempTelPhone = telString;
            time.start();
            tel_layout_bg.setBackgroundResource(R.drawable.edit_background_all);
            tel_edit.setBackgroundResource(R.drawable.edit_bg_all);
            tv_error.setText("");
            tv_error.setVisibility(View.GONE);
        } else {
            yzm_layout_bg.setBackgroundResource(R.drawable.edit_editerror_background);
            yzm_edit.setBackgroundResource(R.drawable.edit_bg_all_error);
            tv_error2.setText("" + changeTelBean.getMessage());
            tv_error2.setVisibility(View.VISIBLE);
        }
    }


    /**
     * 获取短信验证码异步
     **/
    class ChangeTelPhoneTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            changeTelBean = httpUtils.getChangeTelPhone(Const.URL + Const.GetChangeMobleAPI
                    , telString, yzmString);
            Log.d(" ---- > ", changeTelBean.getCode() + "");
            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == changeTelBean.getCode()) {
                ChangeTelNext.this.finish();
            }
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;

            case R.id.yzm_btn:
                initData();
                break;

            case R.id.ok_button:
                initOKBtn();
                break;
        }
    }


    /**
     * 验证手机格式
     */
    public static boolean isMobileNO(String mobiles) {
    /*
    移动：134、135、136、137、138、139、150、151、157(TD)、158、159、187、188
    联通：130、131、132、152、155、156、185、186
    电信：133、153、180、189、（1349卫通）/^0?1[3|4|5|7|8][0-9]\d{8}$/
    总结起来就是第一位必定为1，第二位必定为3或5或8或7（电信运营商），其他位置的可以为0-9
    */
        String telRegex = "[1][3578]\\d{9}";//"[1]"代表第1位为数字1，"[358]"代表第二位可以为3、5、7、8中的一个，"\\d{9}"代表后面是可以是0～9的数字，有9位。
        if (TextUtils.isEmpty(mobiles))
            return false;
        else
            return mobiles.matches(telRegex);
    }
}
