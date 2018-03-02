package cn.jun.NotificationCenter;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.gfedu.gfeduapp.BaseFragment;
import cn.jun.bean.Const;
import cn.jun.bean.GetNotifybyIsRead;
import cn.jun.bean.SetNotifyIsRead;
import cn.jun.utils.HttpUtils;
import edu.swu.pulltorefreshswipemenulistview.library.PullToRefreshSwipeMenuListView;
import edu.swu.pulltorefreshswipemenulistview.library.pulltorefresh.interfaces.IXListViewListener;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.ui.note.QuestionDetailActivity;
import jc.cici.android.atom.ui.order.OrderDetailActivity;

public class ReadNotificationFragment extends BaseFragment implements IXListViewListener {
    //工具
    private HttpUtils httpUtils = new HttpUtils();
    //视图
    private View view;
    //侧滑ListView
    private PullToRefreshSwipeMenuListView mListView;
    //适配器
    private MyReadSwipeAdapter mAdapter;
    //数据源
    private GetNotifybyIsRead NotifybyIsReadBean = new GetNotifybyIsRead();
    private ArrayList<GetNotifybyIsRead> mList = new ArrayList<>();
    //用户ID
    private int UserID;
    //页码页数
    private int PageIndex;
    private int PageSize;

    //设为已读数据源
    private SetNotifyIsRead SetNotifyIsRead = new SetNotifyIsRead();

    @Override
    public void onResume() {
        if (httpUtils.isNetworkConnected(getActivity())) {
            initDate();
        }
        super.onResume();
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (httpUtils.isNetworkConnected(getActivity())) {
            view = inflater.inflate(R.layout.message_fragemnt, container, false);
        } else {
            view = inflater.inflate(R.layout.message_fragemnt, container, false);
        }
        return view;
    }


    private void initDate() {
        PageIndex = 1;
        PageSize = 10;
        NotificationTask Notification = new NotificationTask();
        Notification.execute(PageIndex, PageSize);
    }

    class NotificationTask extends AsyncTask<Integer, Integer, Void> {
        @Override
        protected Void doInBackground(Integer... arg0) {
            int PageIndex = arg0[0];
            int PageSize = arg0[1];
            SharedPreferences LoginPre = getActivity().getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                UserID = LoginPre.getInt("S_ID", 0);
            }
            NotifybyIsReadBean = httpUtils.getNotifybyIsRead(Const.URL + Const.GetNotifyByIsReadAPI, UserID, 1, PageIndex, PageSize);

            return null;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == NotifybyIsReadBean.getCode()) {
                if (NotifybyIsReadBean.getBody().getListCount() > 0) {
                    mList.add(NotifybyIsReadBean);
                    initView();
                } else {//接口无数据
                    mListView = (PullToRefreshSwipeMenuListView) view.findViewById(R.id.listView);
                    mListView.stopLoadMore();
                }
            } else {//接口请求失败
                Toast.makeText(getActivity(), NotifybyIsReadBean.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void initView() {
        mListView = (PullToRefreshSwipeMenuListView) view.findViewById(R.id.listView);
        mAdapter = new MyReadSwipeAdapter(getActivity(), mList);
        mListView.setAdapter(mAdapter);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int Inform_Type = mList.get(0).getBody().getUserInformList().get(position - 1).getInform_Type();
                //通知类型 1.订单通知   2.课程通知   3.系统通知   4.学习互动
                Log.i("position -- ", "" + position);
                if (1 == Inform_Type) {
                    int OrderID = mList.get(0).getBody().getUserInformList().get(position - 1).getOrderID();
                    Intent intent = new Intent(getActivity(), OrderDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("orderId", OrderID);
                    Log.i("OrderID", "" + OrderID);
                    bundle.putInt("from", 0);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (2 == Inform_Type) {
//                    Intent intent = new Intent(getActivity(), MainActivity.class);
//                    startActivity(intent);
//                    getActivity().finish();
                    String content = mList.get(0).getBody().getUserInformList().get(position - 1).getInform_Content();
                    String time = mList.get(0).getBody().getUserInformList().get(position - 1).getInform_AddTime();
                    Intent intent = new Intent(getActivity(), NotifiInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "课程通知");
                    bundle.putString("content", content);
                    bundle.putString("time", time);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (3 == Inform_Type) {
                    String content = mList.get(0).getBody().getUserInformList().get(position - 1).getInform_Content();
                    String time = mList.get(0).getBody().getUserInformList().get(position - 1).getInform_AddTime();
                    Intent intent = new Intent(getActivity(), NotifiInfoActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putString("type", "系统通知");
                    bundle.putString("content", content);
                    bundle.putString("time", time);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } else if (4 == Inform_Type) {
                    int quesId = mList.get(0).getBody().getUserInformList().get(position - 1).getQuesID();
                    int page = mList.get(0).getBody().getUserInformList().get(position - 1).getPageIndex();
                    int childClassTypeId = mList.get(0).getBody().getUserInformList().get(position - 1).getChildClassTypeId();
                    int classId = mList.get(0).getBody().getUserInformList().get(position - 1).getClassId();
                    int lessonId = mList.get(0).getBody().getUserInformList().get(position - 1).getLessonID();
                    int StageProblemStatus = mList.get(0).getBody().getUserInformList().get(position - 1).getQuesState();
                    int StageNoteStatus = mList.get(0).getBody().getUserInformList().get(position - 1).getNoteState();
                    int StageInformationStatus = mList.get(0).getBody().getUserInformList().get(position - 1).getInformationState();
                    Intent intent = new Intent(getActivity(), QuestionDetailActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("quesId", quesId);
                    bundle.putInt("page", page);
                    bundle.putInt("childClassTypeId", childClassTypeId);
                    bundle.putInt("classId", classId);
                    bundle.putInt("lessonId", lessonId);
                    bundle.putInt("StageProblemStatus", StageProblemStatus);
                    bundle.putInt("StageNoteStatus", StageNoteStatus);
                    bundle.putInt("StageInformationStatus", StageInformationStatus);
                    bundle.putInt("jumpFlag", 0);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
//                if (httpUtils.isNetworkConnected(getActivity())) {
//                    SetNotifyIsReadTask SetNotifyTask = new SetNotifyIsReadTask();
//                    SetNotifyTask.execute();
//                }
            }
        });
    }

    class SetNotifyIsReadTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... arg0) {
            int Inform_PKID = arg0[0];
            SharedPreferences LoginPre = getActivity().getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                UserID = LoginPre.getInt("S_ID", 0);
            }
            SetNotifyIsRead = httpUtils.setNotifyIsRead(Const.URL + Const.SetNotifyIsRead, UserID, Inform_PKID);


            return null;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == SetNotifyIsRead.getCode()) {

            } else {//接口请求失败
                Toast.makeText(getActivity(), NotifybyIsReadBean.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    @Override
    protected void lazyLoad() {

    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    @Override
    public void onRefresh() {
        mListView.setRefreshTime("刚刚");
//        mListView.setRefreshTime(RefreshTime.getRefreshTime(getActivity()));
        mListView.stopRefresh();
        mListView.stopLoadMore();
    }

    @Override
    public void onLoadMore() {
        PageIndex = PageIndex + 1;
        PageSize = 10;
        NotificationTask Notification = new NotificationTask();
        Notification.execute(PageIndex, PageSize);
    }

}