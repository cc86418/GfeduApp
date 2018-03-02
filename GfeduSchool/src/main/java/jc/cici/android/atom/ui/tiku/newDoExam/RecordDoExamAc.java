package jc.cici.android.atom.ui.tiku.newDoExam;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.base.AppManager;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.TikuHomeBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.tiku.MyFragmentPageAdapter;
import jc.cici.android.atom.ui.tiku.NetUtil;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.TopMiddlePopup;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 做题记录Activity
 * Created by atom on 2018/1/12.
 */

public class RecordDoExamAc extends BaseActivity {

    private BaseActivity baseActivity;
    private Unbinder unbinder;
    // 返回按钮布局
    @BindView(R.id.back_layout)
    RelativeLayout back_layout;
    // 标题布局
    @BindView(R.id.titleName_layout)
    RelativeLayout titleName_layout;
    // 标题
    @BindView(R.id.titleName_txt)
    TextView titleName_txt;
    // tab布局
    @BindView(R.id.tabs)
    TabLayout tabs;
    // viewPager
    @BindView(R.id.viewPager)
    ViewPager viewPager;

    private SharedPreferences sp;
    // 用户id
    private int userId;
    // 项目id
    private int projectId;
    // 试卷类型
    private int tPaperType;
    // 二级项目列表
    private ArrayList<TikuHomeBean.TikuProject.Node> nodeList = new ArrayList<>();
    private TopMiddlePopup topMiddlePopup;
    private Dialog mDialog;
    private String[] tabTitle = {"知识点做题历史", "试卷做题历史"};//每个页面顶部标签的名字
    private Fragment testFragment1,testFragment2;
    private ArrayList<Fragment> fragments = new ArrayList<>();
    private MyFragmentStatePagerAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_record_do_exam;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppManager.getInstance().addActivity(this);
        baseActivity = this;
        projectId = getIntent().getIntExtra("projectId", 0);
        tPaperType = getIntent().getIntExtra("tPaperType", 0);
        System.out.println("projectId >>>:" + projectId + ",tPaperType >>>:" + tPaperType);
        setContentView(getLayoutId());
        unbinder = ButterKnife.bind(this);
        // 初始化视图
        initView();
        // 初始化数据
        if (NetUtil.isMobileConnected(baseActivity)) {
            // 初始化数据
            initData();
        } else {
            Toast.makeText(baseActivity, "网络连接失败,请检查网络连接", Toast.LENGTH_SHORT).show();
        }
    }

    private void initData() {
        showProcessDialog(baseActivity, R.layout.loading_show_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commonPramas();
        Observable<CommonBean<TikuHomeBean>> observable = httpPostService.getExamListInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<TikuHomeBean>>() {
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
                        Toast.makeText(baseActivity, "网络请求异常", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<TikuHomeBean> tikuHomeBeanCommonBean) {
                        if(100 == tikuHomeBeanCommonBean.getCode()){
                            ArrayList<TikuHomeBean.TikuProject> parentList = tikuHomeBeanCommonBean.getBody().getList();
                            if (null != parentList && parentList.size() > 0) {
                                for (int i = 0; i < parentList.size(); i++) {
                                    ArrayList<TikuHomeBean.TikuProject.Node> secondList = parentList.get(i).getLevelTwo();
                                    if (!"null".equals(secondList) && secondList.size() > 0) {
                                        for (int j = 0; j < secondList.size(); j++) {
                                            if (projectId == secondList.get(j).getProjectId()) {
                                                if (nodeList.size() > 0) {
                                                    nodeList.clear();
                                                }
                                                nodeList.addAll(secondList);
                                                // 设置标题
                                                titleName_txt.setText(secondList.get(j).getProjectName());
                                                break;
                                            }
                                        }
                                    }
                                }
                            }

                            testFragment1 = new TestOrKnowledgeFragment(projectId,1);
                            testFragment2 = new TestOrKnowledgeFragment(projectId,2);
                            fragments.add(testFragment1);
                            fragments.add(testFragment2);
                            //这里注意的是，因为我是在fragment中创建MyFragmentStatePagerAdapter，所以要传getChildFragmentManager()
                            adapter = new MyFragmentStatePagerAdapter(getSupportFragmentManager(), fragments, Arrays.asList(tabTitle));
                            viewPager.setAdapter(adapter);
                            //TabLayout和ViewPager的关联
                            tabs.setupWithViewPager(viewPager);
                            //在设置viewpager页面滑动监听时，创建TabLayout的滑动监听
                            viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                                @Override
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                }

                                @Override
                                public void onPageSelected(int position) {
                                    viewPager.setCurrentItem(position);
                                }

                                @Override
                                public void onPageScrollStateChanged(int state) {

                                }
                            });

                        }else{
                            Toast.makeText(baseActivity, tikuHomeBeanCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
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
            obj.put("projectId", projectId);
            obj.put("tpaperType", tPaperType);
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


    private void initView() {

        sp = baseActivity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);

//        testFragment1 = new TestOrKnowledgeFragment(projectId,1);
//        testFragment2 = new TestOrKnowledgeFragment(projectId,2);
//        fragments.add(testFragment1);
//        fragments.add(testFragment2);
//
//        //这里注意的是，因为我是在fragment中创建MyFragmentStatePagerAdapter，所以要传getChildFragmentManager()
//        viewPager.setAdapter(new MyFragmentStatePagerAdapter(getSupportFragmentManager(), fragments, Arrays.asList(tabTitle)));
//        //TabLayout和ViewPager的关联
//        tabs.setupWithViewPager(viewPager);
//        //在设置viewpager页面滑动监听时，创建TabLayout的滑动监听
//        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//            @Override
//            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//
//            }
//
//            @Override
//            public void onPageSelected(int position) {
//                viewPager.setCurrentItem(position);
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//
//            }
//        });

    }

    public void showProcessDialog(Activity mContext, int layout) {
        mDialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
    }

    @OnClick({R.id.back_layout, R.id.titleName_layout})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_layout: // 返回按钮
                baseActivity.finish();
                break;
            case R.id.titleName_layout:
                int screenWidth = ToolUtils.getScreenPixels(baseActivity).widthPixels;
                int screenHeight = ToolUtils.getScreenPixels(baseActivity).heightPixels;
                topMiddlePopup = new TopMiddlePopup(baseActivity, screenWidth, screenHeight, onItemClickListener, nodeList);
                topMiddlePopup.show(titleName_txt);
                break;
        }
    }

    private BaseRecycleerAdapter.OnItemClickListener onItemClickListener = new BaseRecycleerAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(View view, int position) {
            projectId = nodeList.get(position).getProjectId();
            titleName_txt.setText(nodeList.get(position).getProjectName());
            fragments.clear();
            adapter.notifyDataSetChanged();
            initData();
            topMiddlePopup.dismiss();
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (null != unbinder) {
            unbinder.unbind();
        }
    }

    class MyFragmentStatePagerAdapter extends FragmentPagerAdapter {

        private List<Fragment> mFragments;
        private List<String> mTitles;

        public MyFragmentStatePagerAdapter(FragmentManager fm, List<Fragment> fragments, List<String> titles) {
            super(fm);
            mFragments = fragments;
            mTitles = titles;
        }

        @Override
        public Fragment getItem(int position) {
            return mFragments.get(position);
        }

        @Override
        public int getCount() {
            return mFragments == null ? 0 : mFragments.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mTitles.get(position);
        }

        @Override
        public void notifyDataSetChanged() {
            super.notifyDataSetChanged();
        }

        @Override
        public int getItemPosition(Object object) {
            return POSITION_NONE;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            // TODO Auto-generated method stub
                removeFragment(container,position);
            return super.instantiateItem(container, position);
        }

        private void removeFragment(ViewGroup container,int index) {
            String tag = getFragmentTag(container.getId(), index);
            Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
            if (fragment == null)
                return;
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.remove(fragment);
            ft.commit();
            ft = null;
            getSupportFragmentManager().executePendingTransactions();
        }

        private String getFragmentTag(int viewId, int index) {
            try {
                Class<FragmentPagerAdapter> cls = FragmentPagerAdapter.class;
                Class<?>[] parameterTypes = { int.class, long.class };
                Method method = cls.getDeclaredMethod("makeFragmentName",
                        parameterTypes);
                method.setAccessible(true);
                String tag = (String) method.invoke(this, viewId, index);
                return tag;
            } catch (Exception e) {
                e.printStackTrace();
                return "";
            }
        }
    }
}
