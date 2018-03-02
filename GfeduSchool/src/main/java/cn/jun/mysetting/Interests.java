package cn.jun.mysetting;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.TextPaint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.GridLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;

import cn.jun.bean.AllUserAreasBean;
import cn.jun.utils.HttpUtils;
import cn.jun.utils.PublicFunc;
import jc.cici.android.R;

public class Interests extends Activity implements View.OnClickListener{
    //进度条
    private Dialog mDialog;
    private TextView tv_title,textview2;
    private GridLayout GridLayout_Button;
    private RelativeLayout childRelativeLayout;
    private CheckBox mCheckBox;
    private HashSet<String> mSetStatusList = new HashSet<String>();
    //工具类
    private HttpUtils httpUtils = new HttpUtils();
    // MD5
    private PublicFunc MD5 = new PublicFunc();
    // Md5参数
    private String MD5Result;
    // &MD5code=
    private String MD5_Code = "&MD5code=";
    // 用户感兴趣项目
    private String AllUserAreasUrl = "http://m.gfedu.cn/StudentWebService.asmx/GetAllProject?Student=";
    private String AllUserAreasParams;
    // 提交用户感兴趣项目
    private String SetUserAreasUrl = "http://m.gfedu.cn/StudentWebService.asmx/SetUserProject?Student=";
    private String SetUserAreasParams;
    private String SetUserAreasResult;
    private String SetUserAreasData[];
    // 数据源
    private AllUserAreasBean all_glp;
    private ArrayList<AllUserAreasBean.ResultData> mAll_List = new ArrayList<AllUserAreasBean.ResultData>();
    private String userId = "26146";
    // 屏幕高和宽
	private int width;
	private int height;
    private String NewProIDs_String;
    //选好了保存按钮
    private RelativeLayout bottom_layout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.interests);
        initView();
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        width = wm.getDefaultDisplay().getWidth();// 屏幕宽度
        height = (wm.getDefaultDisplay().getHeight());// 屏幕高度


        showProcessDialog(this,
                R.layout.loading_show_dialog_color);


    }

    public void showProcessDialog(Activity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext,R.style.showdialog).create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);

        if (httpUtils.isNetworkConnected(this)) {
            GridViewTask GridViewTask = new GridViewTask();
            GridViewTask.execute();
        }

    }

    public void initView(){
        tv_title = (TextView) findViewById(R.id.tv_title);
        TextPaint tp = tv_title.getPaint();
        tp.setFakeBoldText(true);
        textview2 = (TextView) findViewById(R.id.textview2);
        TextPaint tp2 = textview2.getPaint();
        tp2.setFakeBoldText(true);

        GridLayout_Button = (GridLayout) findViewById(R.id.GridLayout_Button);

        bottom_layout = (RelativeLayout) findViewById(R.id.bottom_layout);
        bottom_layout.setOnClickListener(this);
    }

    class GridViewTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            try {
                AllUserAreasParams = "{'StudentID':'" + userId + "'}";
                String urlEncoding = URLEncoder.encode(AllUserAreasParams,
                        "UTF-8");
                MD5Result = MD5.getMD5Str(AllUserAreasParams + MD5.MD5_KEY);

                all_glp = httpUtils.getAllUserAreasBeanList(AllUserAreasUrl
                        + urlEncoding + MD5_Code + MD5Result);

                System.out.println("全部项目请求地址 --- " + AllUserAreasUrl
                        + AllUserAreasParams + MD5_Code + MD5Result);
                if (all_glp != null) {
                    mAll_List.addAll(all_glp.getResultData());

                }

            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mDialog.dismiss();
            String ResultState = all_glp.getResultState();
            String ResulMessage = all_glp.getResultStr();
            if ("1".equals(ResultState)) {
                for (int i = 0; i < mAll_List.size(); i++) {
                    GridLayout.LayoutParams lp = new GridLayout.LayoutParams();
                    lp.rowSpec = GridLayout.spec(i / 3);
                    lp.columnSpec = GridLayout.spec(i % 3);
                    lp.width = width / 3;
                    lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                    childRelativeLayout = (RelativeLayout) LayoutInflater.from(
                            Interests.this).inflate(
                            R.layout.interest_areas_edit_item_button, null);

                    mCheckBox = (CheckBox) childRelativeLayout
                            .findViewById(R.id.CheckBox);
                    mCheckBox.setTag(i);
                    mCheckBox.setText(mAll_List.get(i).getProName());
                    GridLayout_Button.addView(childRelativeLayout, lp);

                    // 是否选择的项目状态

                    String ProStatus = mAll_List.get(i).getProStatus();
                    if ("1".equals(ProStatus)) {
                        mCheckBox.setChecked(true);

                    } else if ("0".equals(ProStatus)) {
                        mCheckBox.setChecked(false);
                    }

                    if (mCheckBox.isChecked() == true) {
                        String cPro = mAll_List.get(i).getProID();
                        System.out.println("选中状态处理 --- " + cPro);
                        mSetStatusList.add(cPro);

                        SetUserAreasData = new String[mSetStatusList.size()];
                        // 初始化 Set-->数组
                        mSetStatusList.toArray(SetUserAreasData);
                        System.out.println("初始化     --- "
                                + Arrays.toString(SetUserAreasData));

                    } else if (mCheckBox.isChecked() == false) {
                        String c_Pro = mAll_List.get(i).getProID();
                        System.out.println("未选中状态处理 --- " + c_Pro);
                        mSetStatusList.remove(c_Pro);

                    }

                    mCheckBox.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            CheckBox checkButton = (CheckBox) v;
                            final int tag = (Integer) v.getTag();
                            if (checkButton.isChecked() == true) {
                                String cProID = mAll_List.get(tag).getProID();
                                mSetStatusList.add(cProID);
                                SetUserAreasData = new String[mSetStatusList
                                        .size()];
                                // 添加 Set-->数组
                                mSetStatusList.toArray(SetUserAreasData);
                                System.out.println("添加---Set-->数组     --- "
                                        + Arrays.toString(SetUserAreasData));

                            } else if (checkButton.isChecked() == false) {
                                String c_ProID = mAll_List.get(tag).getProID();
                                mSetStatusList.remove(c_ProID);
                                SetUserAreasData = new String[mSetStatusList
                                        .size()];
                                // 删除 Set-->数组
                                mSetStatusList.toArray(SetUserAreasData);
                                System.out.println("删除---Set-->数组     --- "
                                        + Arrays.toString(SetUserAreasData));

                            }
                        }
                    });

                }

            } else {
                Toast.makeText(Interests.this, ResulMessage, Toast.LENGTH_SHORT)
                        .show();
            }
//            mDialog.dismiss();
        }
    }




    class SetUserAreasTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... arg0) {
            NewProIDs_String = Arrays.toString(SetUserAreasData);
            NewProIDs_String = NewProIDs_String.replaceAll("[\\[\\]]", "");
            NewProIDs_String = NewProIDs_String.replaceAll("[\\[\\]]", "");
            NewProIDs_String = NewProIDs_String.replaceAll(" ", "");

            try {
                SetUserAreasParams = "{'StudentID':'" + userId
                        + "','NewProIDs':'" + NewProIDs_String + "'}";
                String urlEncoding = URLEncoder.encode(SetUserAreasParams,
                        "UTF-8");
                MD5Result = MD5.getMD5Str(SetUserAreasParams + MD5.MD5_KEY);

                SetUserAreasResult = httpUtils.SumbitIntensest(SetUserAreasUrl
                        + urlEncoding + MD5_Code + MD5Result);

                System.out.println("提交项目的请求 --- " + SetUserAreasUrl
                        + SetUserAreasParams + MD5_Code + MD5Result);

                System.out.println("提交项目的返回码 --- " + SetUserAreasResult);

            } catch (Exception e) {

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if ("1".equals(SetUserAreasResult)) {
                Toast.makeText(Interests.this, "保存成功", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(Interests.this, "请选择感兴趣的项目", Toast.LENGTH_SHORT)
                        .show();
            }
//            mDialog.dismiss();
        }

    }


    @Override
    public void onClick(View v) {
            switch (v.getId()){
                case R.id.bottom_layout:
                    if (httpUtils.isNetworkConnected(Interests.this)) {
                        SetUserAreasTask SetUserAreasTask = new SetUserAreasTask();
                        SetUserAreasTask.execute();
                    } else {
                        Toast.makeText(this, "网络连接失败!", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
    }
}
