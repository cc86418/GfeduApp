package cn.jun.pushMessage;


import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import cn.jun.NotificationCenter.NotificatioCenterActivity;
import cn.jun.bean.Const;
import cn.jun.bean.GetPushAndroid;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;

public class PushMessage_Service extends Service {
    private MyBind myBind = new MyBind();
    private HttpUtils httpUtils = new HttpUtils();
    // 程序是否是开启状态
    boolean isAppOpen = true;
    //用户userId;
    private int UserID;
    //数据源
    private GetPushAndroid PushAndroid = new GetPushAndroid();

    private Handler handler = new Handler();

    public static boolean isNewPush = false;

    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //要做的事情，这里再次调用此Runnable对象，以实现每两秒实现一次的定时器操作
            // 执行该方法
            ShowPush();
            handler.postDelayed(this, 30000);
        }
    };


    @Override
    public void onDestroy() {
        isAppOpen = false;
    }

    public IBinder onBind(Intent intent) {
        return myBind;
    }

    public class MyBind extends Binder implements IService {
        public String getName() {
            return "NEWSTOPPORT";
        }

    }

    public interface IService {
        String getName();
    }

    public int onStartCommand(Intent intent, int flags, int startid) {
        Log.i("接收 onStartCommand ", " -- > onStartCommand ");
//        initThread();
        handler.postDelayed(runnable, 2000);
        return super.onStartCommand(intent, flags, startid);
    }


    private void ShowPush() {
        if (httpUtils.isNetworkConnected(this)) {
            Log.i("接收push", "接收push");
            PushMessageTask push_task = new PushMessageTask();
            push_task.execute();
//            showNotification();
        }
    }

    class PushMessageTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                UserID = LoginPre.getInt("S_ID", 0);
            }
            Log.i("UserID = ", "" + UserID);
            if (0 != UserID) {
                PushAndroid = httpUtils.getpushandroid(Const.URL + Const.GetPusAPI, UserID);
            }
            return null;

        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == PushAndroid.getCode()) {//接口请求成功
                int PushCount = PushAndroid.getBody().getInform_PKID();
                Log.i("PushCount = ", "" + PushCount);
                if (0 != PushCount) {//有新通知
                    Log.i("发送通知 = ", " 通知");
                    showNotification(PushAndroid.getBody().getInform_Content());
                    isNewPush = true;
                } else {//无新通知
                    isNewPush = false;
                    Log.i("不发送通知 = ", " 通知");
                }

            }
        }

    }

    private void showNotification(String Contnt) {
//        // 创建一个NotificationManager的引用
        NotificationManager notificationManager = (NotificationManager) this
                .getSystemService(android.content.Context.NOTIFICATION_SERVICE);
        Notification.Builder builder = new Notification.Builder(this);
        builder.setWhen(System.currentTimeMillis()); //发送时间
        builder.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
        builder.setAutoCancel(true);//打开程序后图标消失
//        builder.setTicker("显示第二个通知");
//        builder.setContentInfo("补充内容");
        builder.setContentText(Contnt);
        builder.setContentTitle("金程网校");
        builder.setSmallIcon(R.mipmap.ic_launcher);
//        builder.setTicker("新消息");
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        Intent intent = new Intent(this, NotificatioCenterActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        builder.setContentIntent(pendingIntent);
        Notification new_notification = builder.build();
        notificationManager.notify(0, new_notification);


//        Notification.Builder builder1 = new Notification.Builder(MainActivity.this);
//        builder1.setSmallIcon(R.drawable.advise2); //设置图标
//        builder1.setTicker("显示第二个通知");
//        builder1.setContentTitle("通知"); //设置标题
//        builder1.setContentText("点击查看详细内容"); //消息内容
//        builder1.setWhen(System.currentTimeMillis()); //发送时间
//        builder1.setDefaults(Notification.DEFAULT_ALL); //设置默认的提示音，振动方式，灯光
//        builder1.setAutoCancel(true);//打开程序后图标消失
//        Intent intent =new Intent (MainActivity.this,Center.class);
//        PendingIntent pendingIntent =PendingIntent.getActivity(MainActivity.this, 0, intent, 0);
//        builder1.setContentIntent(pendingIntent);
//        Notification notification1 = builder1.build();
//        notificationManager.notify(124, notification1); // 通过通知管理器发送通知

    }


}
