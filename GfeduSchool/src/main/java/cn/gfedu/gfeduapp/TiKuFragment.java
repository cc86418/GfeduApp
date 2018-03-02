package cn.gfedu.gfeduapp;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import cn.gfedu.home_pager.BaseFragment;
import cn.gfedu.home_pager.ITabClickListener;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;


public class TiKuFragment extends BaseFragment implements ITabClickListener, View.OnClickListener {
    //用户信息
    private String userID;
    private String userRealName;
    private String gxqm_str;


    // Storage Permissions
    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    //按钮
    private ImageView btn;

    @Override
    public void fetchData() {

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tiku_layout, container, false);
        btn = (ImageView) view.findViewById(R.id.btn);
        btn.setOnClickListener(this);
        //获取用户信息
        GetUserSharePreferences();

        return view;
    }

    private void GetUserSharePreferences() {
        SharedPreferences LoginPre = getActivity().getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        if (null != LoginPre && !"".equals(LoginPre)) {
            int SID = LoginPre.getInt("S_ID", 0);
            userID = Integer.toString(SID);
            userRealName = LoginPre.getString("S_RealName", "");
            userRealName = userRealName.replace("&nbsp;", " ");

            gxqm_str = LoginPre.getString("S_Telephone", "");
            gxqm_str = gxqm_str.replace("&nbsp;", " ");
        }

    }

    @Override
    public void onMenuItemClick() {

    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn:
//                Intent intent = new Intent(Intent.ACTION_MAIN);
//                //知道要跳转应用的包名、类名
//                ComponentName componentName = new ComponentName("jc.cici.android.gfedu", "jun.jc.MyActivity.My_Activity");
//                intent.setComponent(componentName);
//                startActivity(intent);

                Intent intent = getActivity().getPackageManager().getLaunchIntentForPackage("jc.cici.android.gfedu");
                // 这里如果intent为空，就说名没有安装要跳转的应用嘛
                if (intent != null) {
                    // 这里跟Activity传递参数一样的嘛，不要担心怎么传递参数，还有接收参数也是跟Activity和Activity传参数一样
//                    intent.putExtra("name", "Liu xiang");
//                    intent.putExtra("birthday", "1983-7-13");
                    startActivity(intent);
                } else {
                    // 没有安装要跳转的app应用，提醒一下
                    Toast.makeText(getActivity(), "哟，赶紧下载安装金程学习APP吧", Toast.LENGTH_LONG).show();
                }
                break;
        }
    }


}

