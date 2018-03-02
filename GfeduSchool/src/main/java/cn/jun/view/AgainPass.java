package cn.jun.view;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.io.File;

import cn.gfedu.gfeduapp.MainActivity;
import cn.jun.bean.Const;
import cn.jun.bean.UnBindPhone;
import cn.jun.menory.service.VideoDownloadManager;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.utils.ToolUtils;

public class AgainPass extends Dialog {
    //进度
    private Dialog mDialog;
    //Button
    private Button  quitebtn;
    private ImageButton xBtn;
    private EditText edit_massage;
    private String EditPass;
    //上下文
    private Activity activity;
    private String message;
    //工具
    private HttpUtils httpUtils = new HttpUtils();
    private SharedPreferences loginPre;
    private SharedPreferences.Editor loginEditor;

    private SharedPreferences LastloginPre;
    private SharedPreferences.Editor LastloginEditor;
    private UnBindPhone unBindPhone;

    public AgainPass(Activity context) {
        super(context);
        this.activity = context;

    }

    public AgainPass(Activity context, int theme) {
        super(context, theme);
        this.activity = context;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.again_pass);
        setCanceledOnTouchOutside(false);
        xBtn = (ImageButton) findViewById(R.id.xBtn);
        edit_massage = (EditText) findViewById(R.id.edit_massage);
        quitebtn = (Button) findViewById(R.id.backBtn);
        xBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        quitebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                showProcessDialog(activity,
                        R.layout.loading_show_dialog_color);
                EditPass = edit_massage.getText().toString().trim();
                /**退出并解绑**/
                /**清除所有用户行为记录**/
                QuiteBindUser(EditPass);

            }
        });
    }

    public void showProcessDialog(Activity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);

    }

    private void QuiteBindUser(String password) {
        // 解除绑定
        if (httpUtils.isNetworkConnected(activity)) {
            UnBindDeviceTask UnBindDeviceTask = new UnBindDeviceTask();
            UnBindDeviceTask.execute(password);
        } else {
            Toast.makeText(activity, "请检查网络设置", Toast.LENGTH_SHORT)
                    .show();
        }
    }

    //清除用户保存的个人信息
    private void loginEditorClear() {
        loginPre = activity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        loginEditor = loginPre.edit();
        loginEditor.clear();
        loginEditor.commit();


        LastloginPre = activity.getSharedPreferences(Global.LAST_LOGIN_FLAG, Activity.MODE_PRIVATE);
        LastloginEditor = LastloginPre.edit();
        LastloginEditor.clear();
        LastloginEditor.commit();
    }

    //清除本地已下载文件
    private void DelectPolyvDownload() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {// 获取sd卡
            String path = Environment.getExternalStorageDirectory()
                    + "/".concat("polyvdownload");
            File file = new File(path);
            if (file.exists()) {// 文件路径存在
                DelectFile(file);
            }
        }
    }

    private void DelectFile(File file) {
        if (file.isFile()) {
            file.delete();
            return;
        }
        if (file.isDirectory()) {
            File[] childFile = file.listFiles();
            if (childFile == null || childFile.length == 0) {
                file.delete();
                return;
            }
            for (File f : childFile) {
                DelectFile(f);
            }
            file.delete();
        }

    }

    //清空或者删除数据库表
    private void DelectSQLDateBase() {
        VideoDownloadManager.getInstance().deleteClassTable();
        VideoDownloadManager.getInstance().deleteStageTable();
        VideoDownloadManager.getInstance().deleteDownLoadTable();

    }


    /**解除User绑定**/
    /**
     * 解除绑定
     **/
    class UnBindDeviceTask extends AsyncTask<String, Void, Void> {
        @Override
        protected Void doInBackground(String... arg0) {
            String pass = arg0[0];
            loginPre = activity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            int S_ID = loginPre.getInt("S_ID", 0);
            String S_Name = loginPre.getString("S_Name", "");
            String S_Pawwsord = pass;
            // 获取本机UUID
            String UUID_str = ToolUtils.getUUID(activity);
//            String MD5 = httpUtils.getMD5Str2(S_Name + S_Pawwsord);
            unBindPhone = httpUtils.unbinddevice(
                    Const.URL + Const.UnBindDeviceAPI, S_ID,
                    Const.CLIENT, Const.VERSION, S_Name, S_Pawwsord, UUID_str,
                    Const.APPNAME);

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            mDialog.dismiss();
            if (100 == unBindPhone.getCode()) {
                Toast.makeText(activity, "解绑成功!", Toast.LENGTH_LONG).show();
                loginEditorClear();
                DelectPolyvDownload();
                DelectSQLDateBase();
                Intent refreshIntent = new Intent(activity, MainActivity.class);
                activity.startActivity(refreshIntent);
                activity.finish();
            } else {
                String msg = unBindPhone.getMessage();
                Toast.makeText(activity, msg, Toast.LENGTH_LONG).show();
            }

        }
    }
}
