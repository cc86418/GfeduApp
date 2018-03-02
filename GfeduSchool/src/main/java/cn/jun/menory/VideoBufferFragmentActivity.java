package cn.jun.menory;


import android.app.Dialog;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.PolyvDownloadProgressListener;
import com.easefun.polyvsdk.PolyvDownloaderErrorReason;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import cn.jun.menory.bean.VideoClassBean;
import cn.jun.menory.bean.VideoClassStageBean;
import cn.jun.menory.bean.VideoItemBean;
import cn.jun.menory.service.VideoDownloadManager;
import cn.jun.view.MyViewPager;
import jc.cici.android.R;

public class VideoBufferFragmentActivity extends FragmentActivity implements
        RadioGroup.OnCheckedChangeListener, View.OnClickListener,
        AdapterView.OnItemLongClickListener {

    // 删除视频进度条
    private Dialog mDialog;
    private RadioGroup rgTabs;
    private LinearLayout llButtons;
    private View vSpline;
    private TextView tvManager;
    private TextView tvAllCheck;

    private boolean editMode = false; // 管理 与 编辑 状态
    private boolean allChecked = false; // 缓存中选中所有

    private VideoListBufferingAdapter videoListBufferingAdapter;


    private ListView lvVideoBuffering;

    /**
     * 新增
     */
    private LinearLayout lvVideoBuffered;
    //    private VideoListBufferedExpandableAdapter videoListBufferedAdapter;
    private int scrllViewWidth = 0, scrollViewMiddle = 0;
    //从数据库获取的班级--数据源
    private View views[];
    //    private String toolsList[];
    private TextView toolsTextViews[];
    private LayoutInflater inflater;
    private ArrayList<VideoClassBean> VideoClassList = new ArrayList<>();
    private ScrollView scrollView;
    private MyViewPager shop_pager;
    private ShopAdapter shopAdapter;
    private int currentItem = 0;
    private ArrayList<VideoClassStageBean> VideoStageList;
    //    private ArrayList<VideoItemBean> VideoStageBeanList = new ArrayList<>();
    private Cursor StageCursor;
    private Cursor DownLoadListCursor;

    /**
     * 改变textView的颜色
     */
    private void changeTextColor(int id) {
        for (int i = 0; i < toolsTextViews.length; i++) {
            if (i != id) {
                toolsTextViews[i].setBackgroundResource(android.R.color.white);
                toolsTextViews[i].setTextColor(Color.parseColor("#000000"));
            }
        }
        toolsTextViews[id].setBackgroundResource(android.R.color.white);
        toolsTextViews[id].setTextColor(0xffff5d5e);
    }

    /**
     * 改变栏目位置
     */
    private void changeTextLocation(int clickPosition) {
        int x = (views[clickPosition].getTop() - getScrollViewMiddle() + (getViewheight(views[clickPosition]) / 2));
        scrollView.smoothScrollTo(0, x);
    }

    /**
     * 返回scrollview的中间位置
     */
    private int getScrollViewMiddle() {
        if (scrollViewMiddle == 0)
            scrollViewMiddle = getScrollViewheight() / 2;
        return scrollViewMiddle;
    }

    /**
     * 返回ScrollView的宽度
     */
    private int getScrollViewheight() {
        if (scrllViewWidth == 0)
            scrllViewWidth = scrollView.getBottom() - scrollView.getTop();
        return scrllViewWidth;
    }

    /**
     * 返回view的宽度
     */
    private int getViewheight(View view) {
        return view.getBottom() - view.getTop();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        inflater = LayoutInflater.from(this);
        setContentView(R.layout.videobuffer_fragmentactivity);

        initViews();
        initData();
    }

    private void initData() {
        videoListBufferingAdapter = new VideoListBufferingAdapter(this);
        final VideoDownloadManager vm = VideoDownloadManager.getInstance();
//        List<VideoItemBean> data = vm.getBufferingVideos();
        List<VideoItemBean> data = vm.getBufferedVideos();
        int size = data.size();
        for (int i = 0; i < size; i++) {
            final VideoItemBean info = data.get(i);
            MyPolyvDownloadProgressListener downloadListener = new MyPolyvDownloadProgressListener(
                    info);
            vm.putDownloadListener(info.vid, downloadListener);
        }
        videoListBufferingAdapter.setData(data);
        lvVideoBuffering.setAdapter(videoListBufferingAdapter);
        lvVideoBuffering.setOnItemLongClickListener(this);


        VideoClassList = vm.getVideoClassTable();
        showToolsView();
        initPager();
    }

    private void showToolsView() {
        LinearLayout toolsLayout = (LinearLayout) findViewById(R.id.tools);
        toolsTextViews = new TextView[VideoClassList.size()];
        views = new View[VideoClassList.size()];
        for (int i = 0; i < VideoClassList.size(); i++) {
            View view = inflater.inflate(R.layout.item_b_top_nav_layout, null);
            view.setId(i);
            view.setOnClickListener(toolsItemListener);
            TextView textView = (TextView) view.findViewById(R.id.text);
            textView.setText(VideoClassList.get(i).getClassname());
            toolsLayout.addView(view);
            toolsTextViews[i] = textView;
            views[i] = view;
        }
        changeTextColor(0);
    }

    private View.OnClickListener toolsItemListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.i("toolsItemListener --- ", v.getId() + "");
            shop_pager.setCurrentItem(v.getId());

        }
    };

    /**
     * initPager<br/>
     * 初始化ViewPager控件相关内容
     */
    private void initPager() {
        shop_pager = (MyViewPager) findViewById(R.id.goods_pager);
        shop_pager.setNoScroll(true);
        shop_pager.setOnPageChangeListener(onPageChangeListener);
        shopAdapter = new ShopAdapter(getSupportFragmentManager());
        shop_pager.setAdapter(shopAdapter);
    }

    /**
     * OnPageChangeListener<br/>
     * 监听ViewPager选项卡变化事的事件
     */
    private ViewPager.OnPageChangeListener onPageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageSelected(int arg0) {
//            Log.i("shop_pager === > ", shop_pager.getCurrentItem() + "");
//            Log.i("arg0 === > ", arg0 + "");
            if (shop_pager.getCurrentItem() != arg0) shop_pager.setCurrentItem(arg0);
            if (currentItem != arg0) {
                changeTextColor(arg0);
                changeTextLocation(arg0);
            }
            currentItem = arg0;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /**
     * ViewPager 加载选项卡
     */
    private class ShopAdapter extends FragmentPagerAdapter {
        public ShopAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int arg0) {
            Fragment fragment = new Fragment_pro_type();
            Bundle bundle = new Bundle();
            String str = VideoClassList.get(arg0).getClassname();
            bundle.putString("typename", str);
            bundle.putInt("currentItem", arg0);

            bundle.putSerializable("VideoClassList", VideoClassList);
//            bundle.putSerializable("VideoStageList", VideoStageList);
            fragment.setArguments(bundle);


            Toast.makeText(VideoBufferFragmentActivity.this, str + " , " + arg0, Toast.LENGTH_SHORT).show();

            return fragment;
        }

        @Override
        public int getCount() {
            return VideoClassList.size();
        }


    }

    private void initViews() {
        lvVideoBuffered = (LinearLayout) findViewById(R.id.lv_buffered);
        scrollView = (ScrollView) findViewById(R.id.tools_scrlllview);

        lvVideoBuffering = (ListView) findViewById(R.id.lv_buffering);


        vSpline = findViewById(R.id.v_spline);
        llButtons = (LinearLayout) findViewById(R.id.ll_bottons);
        tvAllCheck = (TextView) findViewById(R.id.tv_all_check);

        tvManager = (TextView) findViewById(R.id.tv_manager);

        rgTabs = (RadioGroup) findViewById(R.id.rg_tabs);
        rgTabs.setOnCheckedChangeListener(this);
        rgTabs.check(R.id.rb_right);

        findViewById(R.id.tv_manager).setOnClickListener(this);
        findViewById(R.id.iv_back).setOnClickListener(this);
        findViewById(R.id.tv_all_check).setOnClickListener(this);
        findViewById(R.id.tv_delete).setOnClickListener(this);


    }


    class MyPolyvDownloadProgressListener implements
            PolyvDownloadProgressListener {
        private VideoItemBean video;

        public MyPolyvDownloadProgressListener(VideoItemBean video) {
            this.video = video;
        }

        @Override
        public void onDownload(long current, long total) {
            video.current = current;
            video.total = total;
            Log.i("bad-boy", "cu: " + current + ", total: " + total);
            updateList();
        }

        @Override
        public void onDownloadFail(PolyvDownloaderErrorReason reson) {
            video.status = VideoItemBean.STATUS_FAILED;
            updateList();
        }

        @Override
        public void onDownloadSuccess() {
            videoListBufferingAdapter.removeItem(video);
//            videoListBufferedAdapter.addItem(video);
            updateList();
        }
    }

    private boolean toUpdate = false;
    private Handler h = new Handler();

    private void updateList() {
        if (!toUpdate) {
            toUpdate = true;
            h.postDelayed(updateRunnable, 1000);
        }
    }

    private Runnable updateRunnable = new Runnable() {
        @Override
        public void run() {
            toUpdate = false;
            videoListBufferingAdapter.notifyDataSetChanged();
//            videoListBufferedAdapter.notifyDataSetChanged();
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.tv_manager:
                doManager();
                break;
        }
    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        if (checkedId == R.id.rb_left) {
            tvManager.setVisibility(View.VISIBLE);
            lvVideoBuffered.setVisibility(View.VISIBLE);
            lvVideoBuffering.setVisibility(View.GONE);
            showBottomButtons(editMode);
        } else if (checkedId == R.id.rb_right) {
            tvManager.setVisibility(View.GONE);
            lvVideoBuffered.setVisibility(View.GONE);
            lvVideoBuffering.setVisibility(View.VISIBLE);
            showBottomButtons(false);
        }
    }

    // 管理
    private void doManager() {
        editMode = !editMode;
        tvManager.setText(editMode ? "完成" : "管理");
        showBottomButtons(editMode);
        Fragment_pro_type.ooo(editMode);
    }

    private void showBottomButtons(boolean show) {
        int visibility = show ? View.VISIBLE : View.GONE;
        vSpline.setVisibility(visibility);
        llButtons.setVisibility(visibility);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
        final int pos = position;
        WhiteDialog dialog = new WhiteDialog(this, "确定要删除视频？");
        dialog.setOnCommonDialogClickListener(new WhiteDialog.OnCommonDialogClickListener() {

            @Override
            public void onConfirmClick() {
//                VideoItemBean item = (VideoItemBean) videoListBufferingAdapter
//                        .getItem(pos);
//                if (item != null) {
//                    VideoDownloadManager.getInstance().deleteVideo(item);
//                }
//                videoListBufferingAdapter.removeItem(pos);
                Log.i("删除视频====> "," == " + pos);
            }

            @Override
            public void onCancelClick() {

            }
        });
        dialog.show();
        return true;

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        VideoDownloadManager.getInstance().clearDownloadListener();
    }


    public static ArrayList method(ArrayList array) {
        ArrayList arr = new ArrayList();
        Iterator it = array.iterator();
        while (it.hasNext()) {
            Object obj = it.next();
            if (!arr.contains(obj))
                arr.add(obj);
        }
        return arr;
    }

}