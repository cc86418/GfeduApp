package cn.gfedu.gfeduapp;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.gfedu.home_pager.BaseFragment;
import cn.gfedu.home_pager.ITabClickListener;
import cn.jun.bean.Const;
import cn.jun.bean.ProjectListBean;
import cn.jun.bean.ProjectList_NoAllBean;
import cn.jun.indexmain.NewsFragment;
import cn.jun.utils.HttpPostServer;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.courselist.SearchCourseActivity;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class DiscoverFragment extends BaseFragment implements ITabClickListener, View.OnClickListener {
    //fragment数据集合
    private ArrayList<Fragment> FragmentList = new ArrayList<Fragment>();
    // fragment管理器
    private View view;
    private FragmentManager fm;
    public static CategoryTabStrip tabs;
    public static ViewPager pager;
    public static MyPagerAdapter adapter;


    private HttpUtils httpUtils = new HttpUtils();
    //数据源
    public static ProjectListBean projectListBean = new ProjectListBean();
    public static ProjectList_NoAllBean projectList_noAllBean = new ProjectList_NoAllBean();
    public static ArrayList<ProjectList_NoAllBean> NoAllList = new ArrayList<>();
    private List<String> catalogs = new ArrayList<String>();
    //标题搜索布局
    private RelativeLayout title_bar;
    //进度
    public static Dialog mDialog;

    private boolean isAll = true;
    private boolean noAll = false;

    @Override
    public void fetchData() {

    }

    public void showProcessDialog(Activity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (httpUtils.isNetworkConnected(getActivity())) {
            showProcessDialog(getActivity(),
                    R.layout.loading_show_dialog_color);
            view = inflater.inflate(R.layout.discvover_layout, container, false);
            // 获取fragmentManger管理器
            fm = getActivity().getSupportFragmentManager();
            //获取首页数据
            GetIndexInfo();
//            IndexTaskAllDate(isAll);
//            IndexTaskDateNoAll(noAll);
        } else {
            view = inflater.inflate(R.layout.discvover_null_layout, container, false);
            NoFoundApiOrDateLayout();
        }

        return view;
    }

    private void GetIndexInfo() {
        IndexTask IndexTask = new IndexTask();
        IndexTask.execute();

        IndexNoAllTask no_IndexTask = new IndexNoAllTask();
        no_IndexTask.execute();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_bar:
//                Intent ssIntent = new Intent(this,SearchCourseActivity.class);
//                startActivity(ssIntent);
                break;


        }
    }

    private void IndexTaskAllDate(boolean isAll) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostServer httpPostService = retrofit.create(HttpPostServer.class);
        RequestBody body = commParam(isAll);
        Observable<CommonBean<ProjectListBean>> observable = httpPostService.getProductListAll(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<ProjectListBean>>() {
                    @Override
                    public void onCompleted() {
//                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "网络异常，请返回重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<ProjectListBean> ProjectListBean) {
                        Log.i("size --- ", " " + ProjectListBean.getBody().getBody().getProjectList().size());
                        if (100 == ProjectListBean.getCode()) {
                            Log.i("code --- ", " " + ProjectListBean.getCode());
                            projectListBean = ProjectListBean.getBody();
                            Log.i("ProjectListBean --- ", " ok ok ok ok ");
                            Log.i("size  --- ", " " + ProjectListBean.getBody().getBody().getProjectList().size());
                            title_bar = (RelativeLayout) view.findViewById(R.id.title_bar);
                            title_bar.setBackgroundResource(R.drawable.sousuo_title);
                            title_bar.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(getActivity(), SearchCourseActivity.class);
                                    startActivity(intent);
                                }
                            });
                            if (projectListBean.getBody().getProjectList().size() > 0 && null != projectListBean.getBody().getProjectList()) {
                                for (int i = 0; i < projectListBean.getBody().getProjectList().size(); i++) {
                                    catalogs.add(projectListBean.getBody().getProjectList().get(i).getCT_Name());
                                    String IsAllOption = projectListBean.getBody().getProjectList().get(i).getCT_Name();
                                    int CT_ID = projectListBean.getBody().getProjectList().get(i).getCT_ID();
                                    String CT_NAME = projectListBean.getBody().getProjectList().get(i).getCT_Name();

                                    NewsFragment f = new NewsFragment();
                                    Bundle b = new Bundle();
                                    b.putInt("FragmentPosition", i);
                                    b.putString("IsAllOption", IsAllOption);

                                    b.putInt("CT_ID", CT_ID);
                                    b.putString("CT_NAME", CT_NAME);
                                    f.setArguments(b);
                                    FragmentList.add(f);
                                    tabs = (CategoryTabStrip) view.findViewById(R.id.category_strip);
                                    pager = (ViewPager) view.findViewById(R.id.view_pager);
                                    adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager(), FragmentList);
                                    pager.setAdapter(adapter);
                                    tabs.setViewPager(pager);
                                    tabs.getPagerPosition();


                                }

                            }
                        }
                        mDialog.dismiss();
                    }
                });
    }

    private void IndexTaskDateNoAll(boolean isAll) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostServer httpPostService = retrofit.create(HttpPostServer.class);
        RequestBody body = commParam(isAll);
        Observable<CommonBean<ProjectList_NoAllBean>> observable = httpPostService.getProductListNoAll(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<ProjectList_NoAllBean>>() {
                    @Override
                    public void onCompleted() {
//                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(getActivity(), "网络异常，请返回重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<ProjectList_NoAllBean> ProjectList_NoAllBean) {
                        if (100 == ProjectList_NoAllBean.getCode()) {
                            Log.i("获取直播参数 --- ", " --- ");
                            NoAllList.add(projectList_noAllBean);
                        }
                    }
                });
    }

    //公共参数
    private RequestBody commParam(boolean isAll) {
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(getActivity());
        try {
            // 用户id
            obj.put("UserId", "0");
            obj.put("version", Const.VERSION);
            obj.put("appname", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("TimeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str("0" + commParam.getTimeStamp() + "android!%@%$@#$"));

            obj.put("projectId", "0");
            if (isAll) {
                obj.put("addAllOption", "1");//是否添加全部按钮 1：是 0：否
            } else {
                obj.put("addAllOption", "0");//是否添加全部按钮 1：是 0：否
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }


    class IndexTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                projectListBean = httpUtils.getProjectListBean(Const.URL + Const.GetProjectListAPI, "0", "1");
//                projectList_noAllBean = httpUtils.getProjectList_NoAllBean(Const.URL + Const.GetProjectListAPI, "0", "0");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
//            if ("100".equals(projectList_noAllBean.getCode())) {
//                NoAllList.add(projectList_noAllBean);
//            }
            if ("100".equals(projectListBean.getCode())) {
                title_bar = (RelativeLayout) view.findViewById(R.id.title_bar);
                title_bar.setBackgroundResource(R.drawable.sousuo_title);
                title_bar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getActivity(), SearchCourseActivity.class);
                        startActivity(intent);
                    }
                });
                if (projectListBean.getBody().getProjectList().size() > 0 && null != projectListBean.getBody().getProjectList()) {
                    for (int i = 0; i < projectListBean.getBody().getProjectList().size(); i++) {
                        catalogs.add(projectListBean.getBody().getProjectList().get(i).getCT_Name());
                        String IsAllOption = projectListBean.getBody().getProjectList().get(i).getCT_Name();
                        int CT_ID = projectListBean.getBody().getProjectList().get(i).getCT_ID();
                        String CT_NAME = projectListBean.getBody().getProjectList().get(i).getCT_Name();

                        NewsFragment f = new NewsFragment();
                        Bundle b = new Bundle();
                        b.putInt("FragmentPosition", i);
                        b.putString("IsAllOption", IsAllOption);
//                        b.putSerializable("NoAllList", NoAllList);
//                        b.putParcelableArrayList("NoAllList", NoAllList);
                        b.putInt("CT_ID", CT_ID);
                        b.putString("CT_NAME", CT_NAME);
                        f.setArguments(b);
                        FragmentList.add(f);
                        tabs = (CategoryTabStrip) view.findViewById(R.id.category_strip);
                        pager = (ViewPager) view.findViewById(R.id.view_pager);
                        adapter = new MyPagerAdapter(getActivity().getSupportFragmentManager(), FragmentList);
                        pager.setAdapter(adapter);
                        tabs.setViewPager(pager);
                        tabs.getPagerPosition();


                    }

                }

            }
            mDialog.dismiss();
        }
    }


    class IndexNoAllTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                projectList_noAllBean = httpUtils.getProjectList_NoAllBean(Const.URL + Const.GetProjectListAPI, "0", "0");
            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if ("100".equals(projectList_noAllBean.getCode())) {
                NoAllList.add(projectList_noAllBean);
            }
        }
    }

    private void NoFoundApiOrDateLayout() {
        RelativeLayout no_date_realtive = (RelativeLayout) view.findViewById(R.id.no_date_realtive);
        ImageView no_data_im = (ImageView) view.findViewById(R.id.no_data_im);
        no_date_realtive.setVisibility(View.VISIBLE);
        no_date_realtive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (httpUtils.isNetworkConnected(getActivity())) {
                    MainActivity.reLoadFragView();
                }
            }
        });
        no_data_im.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (httpUtils.isNetworkConnected(getActivity())) {
                    MainActivity.reLoadFragView();
                }
            }
        });
    }


    public class MyPagerAdapter extends FragmentPagerAdapter {
        private ArrayList<Fragment> mFragmentList = new ArrayList<Fragment>();

        public MyPagerAdapter(FragmentManager fm, ArrayList<Fragment> FragmentList) {
            super(fm);
            this.mFragmentList = FragmentList;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            if (catalogs.size() > 0) {
                return catalogs.get(position);
            } else {
                return null;
            }

        }

        @Override
        public int getCount() {
            return catalogs.size();
//            return mFragmentList.size();
        }

        @Override
        public Fragment getItem(int position) {
//            return NewsFragment.newInstance(position);
            return mFragmentList.get(position);
        }
    }

    @Override
    public void onMenuItemClick() {

    }

    @Override
    public BaseFragment getFragment() {
        return this;
    }


    //获取时间戳
    public String getTimeStamp() {
        long time = System.currentTimeMillis() / 1000;// 获取系统时间的10位的时间戳
        String str = String.valueOf(time);
        return str;
    }
}
