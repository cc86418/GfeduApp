package cn.jun.view;


import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
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

import static jc.cici.android.R.id.okBtn;

public class QuiteDialog extends Dialog {
    //Button
    private Button quiteUnbingbtn, quitebtn;
    private ImageButton xBtn;
    //上下文
    private Activity activity;
    private String message;
    //工具
    private HttpUtils httpUtils = new HttpUtils();
    private SharedPreferences loginPre;
    private SharedPreferences.Editor loginEditor;
    private UnBindPhone unBindPhone;

    public QuiteDialog(Activity context) {
        super(context);
        this.activity = context;

    }

    public QuiteDialog(Activity context, int theme) {
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
        setContentView(R.layout.quite_dialog);
        setCanceledOnTouchOutside(false);
        xBtn = (ImageButton) findViewById(R.id.xBtn);
        quiteUnbingbtn = (Button) findViewById(okBtn);
        quitebtn = (Button) findViewById(R.id.backBtn);
        xBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        quiteUnbingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AgainPass againPass = new AgainPass(activity);
                againPass.show();
                dismiss();
//                /**退出并解绑**/
//                /**清除所有用户行为记录**/
//                QuiteBindUser();
//                loginEditorClear();
//                DelectPolyvDownload();
//                DelectSQLDateBase();
            }
        });
        quitebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                /**只退出**/
                /**清除本地用户保存记录，不清除上一次的登陆信息和缓存信息**/
                loginEditorClear();
                Intent refreshIntent = new Intent(activity, MainActivity.class);
                activity.startActivity(refreshIntent);
                activity.finish();
            }
        });
    }

    private void QuiteBindUser() {
        // 解除绑定
        if (httpUtils.isNetworkConnected(activity)) {
            UnBindDeviceTask UnBindDeviceTask = new UnBindDeviceTask();
            UnBindDeviceTask.execute();
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
    }

    //清除本地已下载文件
    private void DelectPolyvDownload() {
        if (android.os.Environment.MEDIA_MOUNTED.equals(Environment
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
    class UnBindDeviceTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            loginPre = activity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            int S_ID = loginPre.getInt("S_ID", 0);
            String S_Name = loginPre.getString("S_Name", "");
            String S_Pawwsord = loginPre.getString("S_PassWord","");
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
            if (100 == unBindPhone.getCode()) {
                Toast.makeText(activity, "解绑成功!", Toast.LENGTH_SHORT).show();
                loginEditorClear();
                DelectPolyvDownload();
                DelectSQLDateBase();
                Intent refreshIntent = new Intent(activity, MainActivity.class);
                activity.startActivity(refreshIntent);
                activity.finish();
            } else {
                String msg = unBindPhone.getMessage();
                Toast.makeText(activity, msg, Toast.LENGTH_SHORT).show();
            }

        }
    }
}
