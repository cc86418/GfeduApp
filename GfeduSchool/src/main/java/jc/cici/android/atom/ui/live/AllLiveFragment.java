package jc.cici.android.atom.ui.live;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import cn.gfedu.gfeduapp.CategoryTabStrip;
import cn.gfedu.home_pager.BaseFragment;
import cn.gfedu.home_pager.ITabClickListener;
import jc.cici.android.R;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.LiveProject;
import jc.cici.android.atom.bean.Project;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 直播首页
 * Created by atom on 2017/11/8.
 */

public class AllLiveFragment extends BaseFragment implements ITabClickListener {

    private Activity baseActivity;
    private Unbinder unbinder;
    @BindView(R.id.title_bar_layout)
    RelativeLayout title_bar_layout;
    @BindView(R.id.tabs)
    CategoryTabStrip tabs;
    @BindView(R.id.viewPager)
    ViewPager viewPager;
    @BindView(R.id.empty_layout)
    RelativeLayout empty_layout;

    private Dialog mDialog;
    private ArrayList<Fragment> liveFragmentList = new ArrayList<>();
    private MyPagerAdapter adapter;
    private ArrayList<String> cateList = new ArrayList<>();
    private SharedPreferences sp;
    // 用户id
    private int userId;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        baseActivity = this.getActivity();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        System.out.println("onCreateView()");
        View view = inflater.inflate(getLayoutId(), container, false);
        unbinder = ButterKnife.bind(this, view);
        initView();
        if (NetUtil.isMobileConnected(baseActivity)) {
            initData();
        } else {
            empty_layout.setVisibility(View.VISIBLE);
            Toast.makeText(baseActivity, "网络异常，请检查网络连接", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    /**
     * 初始化
     */
    private void initView() {
        sp = baseActivity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);
    }

    public void showProcessDialog(Activity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
    }

    private void initData() {
        showProcessDialog(baseActivity, R.layout.loading_show_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commonPramas();
        Observable<CommonBean<LiveProject>> observable = httpPostService.getLiveProjectListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<LiveProject>>() {
                    @Override
                    public void onCompleted() {
                        if (null != mDialog && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null != mDialog && mDialog.isShowing()) {
                            mDialog.dismiss();
                        }
                        Toast.makeText(baseActivity, "网络请求失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<LiveProject> liveProjectCommonBean) {
                        if (100 == liveProjectCommonBean.getCode()) {
                            ArrayList<Project> proList = liveProjectCommonBean.getBody().getProjectList();
                            if (null != proList && proList.size() > 0) {
                                if (empty_layout.getVisibility() == View.VISIBLE) {
                                    empty_layout.setVisibility(View.GONE);
                                }
                                for (int i = 0; i < proList.size(); i++) {
                                    int projectID = proList.get(i).getCT_ID();
                                    String proName = proList.get(i).getCT_Name();
                                    cateList.add(proName);
                                    LiveContentFragment liveContentFragment = new LiveContentFragment();
                                    Bundle bundle = new Bundle();
                                    bundle.putInt("projectId", projectID);
                                    bundle.putString("projectName", proName);
                                    liveContentFragment.setArguments(bundle);
                                    liveFragmentList.add(liveContentFragment);
                                    adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager(), liveFragmentList);
                                    viewPager.setAdapter(adapter);
                                    tabs.setViewPager(viewPager);
                                    tabs.getPagerPosition();
                                }
                            } else {
                                empty_layout.setVisibility(View.VISIBLE);
                                Toast.makeText(baseActivity, "暂无数据", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(baseActivity, liveProjectCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private RequestBody commonPramas() {
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(baseActivity);
        try {
            // 用户id
            obj.put("userId", userId);
            // 0 表示全部
            obj.put("projectId", 0);
            // 添加全部
            obj.put("addAllOption", 1);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }

    private int getLayoutId() {
        return R.layout.fragment_live;
    }

    @OnClick({R.id.title_bar_layout,R.id.empty_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.title_bar_layout: // 搜索监听
                Intent it = new Intent(baseActivity, LiveSearchActivity.class);
                baseActivity.startActivity(it);
                break;
            case R.id.empty_layout: // 空视图监听
                initData();
                break;
        }
    }

    @Override
    public void fetchData() {

    }

    @Override
    public void onMenuItemClick() {

    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }

    class MyPagerAdapter extends FragmentPagerAdapter {

        private ArrayList<Fragment> mFragmentList;

        public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> fragmentLsit) {
            super(fm);
            this.mFragmentList = fragmentLsit;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return cateList.size() > 0 ? cateList.get(position) : null;
        }

        @Override
        public int getCount() {
            return cateList.size();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }
}
