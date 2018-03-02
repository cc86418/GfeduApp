package cn.jun.live;


import android.app.Activity;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import cn.gfedu.gfeduapp.BaseFragment;
import cn.jun.bean.Const;
import cn.jun.bean.GetMyLiveBean;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;

public class LiShiFragment extends BaseFragment {
    private GetMyLiveBean MyLiveBean = new GetMyLiveBean();
    private ArrayList<GetMyLiveBean> mList;
    private HttpUtils httpUtils = new HttpUtils();
    //用户ID
    private int UserID;
    private View view;
    private MyLiveOverExpandableListAdapter adapter;
    private ExpandableListView expandableListView;
    private RelativeLayout noView;
    @Override
    protected void lazyLoad() {

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lishi_layout, container, false);
        initDatas();
        return view;
    }

    public void initDatas() {
        if (httpUtils.isNetworkConnected(getActivity())) {
            LiShiTask task = new LiShiTask();
            task.execute();
        }
    }


    class LiShiTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                SharedPreferences LoginPre = getActivity().getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
                if (null != LoginPre && !"".equals(LoginPre)) {
                    UserID = LoginPre.getInt("S_ID", 0);
                }
                MyLiveBean = httpUtils.getMyLive(Const.URL + Const.GetMyLiveAPI, UserID, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == MyLiveBean.getCode()) {
                if (0 != MyLiveBean.getBody().getListCount()) {
                    mList = new ArrayList<>();
                    mList.add(MyLiveBean);
                    initView(view);
                }else{
                    initViewNone(view);
                }

            }
        }
    }


    public void initViewNone(View view){
        expandableListView = (ExpandableListView) view.findViewById(R.id.ExpandableListView);
        noView = (RelativeLayout) view.findViewById(R.id.no_list);
        expandableListView.setVisibility(View.GONE);
        noView.setVisibility(View.VISIBLE);
    }

    public void initView(View view) {
        expandableListView = (ExpandableListView) view.findViewById(R.id.ExpandableListView);
        noView = (RelativeLayout) view.findViewById(R.id.no_list);
        expandableListView.setVisibility(View.VISIBLE);
        noView.setVisibility(View.GONE);
        //初始化adapter
        adapter = new MyLiveOverExpandableListAdapter(getActivity(), mList);
        expandableListView.setAdapter(adapter);
        expandableListView.expandGroup(0);
        expandableListView.setGroupIndicator(null);
        for (int i = 0; i < adapter.getGroupCount(); i++) {
            expandableListView.expandGroup(i);
        }

    }

}
