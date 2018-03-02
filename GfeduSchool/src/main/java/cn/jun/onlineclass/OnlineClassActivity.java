package cn.jun.onlineclass;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import cn.jun.VerticalScreen.VerticalScreenActivity;
import cn.jun.bean.Const;
import cn.jun.bean.StageLessonListBean;
import cn.jun.live.LiveRoomActivity;
import cn.jun.live.vod.VodRoomActivity;
import cn.jun.menory.bean.VideoItemBean;
import cn.jun.menory.manage_activity.ManagerActivity;
import cn.jun.menory.service.VideoDownloadManager;
import cn.jun.utils.HttpUtils;
import cn.jun.view.ShowNullDialog;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.ui.tiku.CardResultActivity;
import jc.cici.android.atom.ui.tiku.MyQuestionActivity;

public class OnlineClassActivity extends Activity implements View.OnClickListener {
    //返回
    private RelativeLayout backLayout;
    //标题
    private TextView class_title;
    //两层listview
    private ExpandableListView class_lv;
    //适配器
    private ClassExpandableListAdapter classAdapter;
    //工具
    private HttpUtils httpUtils = new HttpUtils();
    //浮动缓存icon
    private RelativeLayout FrameLayout_huancun_icon;
    private Button huancun_icon_redBtn;
    //学习进度显示
    private TextView jindu_tv;
    //上次学习到
    private TextView sckd_tv;
    //继续学习
    private TextView jxxx_tv;

    //接受传递过来的数据
    private String UserID;
    private String ClassId = "";
    private String ClassName = "";
    private String StageId = "";
    private String StageName = "";
    private StageLessonListBean LessonBean;
    // 数据源
    private ArrayList<StageLessonListBean.Body.ParentLevelList> LessonList = new ArrayList<StageLessonListBean.Body.ParentLevelList>();
    //数据库查询器
    private VideoDownloadManager vm = VideoDownloadManager.getInstance();
    private static final int Down_ICON = 2000;

    // 服务状态(笔记,问题,资料是否解锁)
    public String StageProblemStatus;
    public String StageNoteStatus;
    public String StageInformationStatus;

    //验证Vid的信息
    private String PoylvVid = "";

    private String Level_ParentID_GO;
    private String Level_PKID_GO;
    private String lessonId_GO;
    private String lessonName_GO;
    private String subjectId_GO;
    private String subjectName_GO;
    private String StudyKey_GO;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.online_class);
        Bundle bundle = getIntent().getExtras();
        ClassId = bundle.getString("ClassId");
        ClassName = bundle.getString("ClassName");
        StageId = bundle.getString("StageId");
        StageName = bundle.getString("StageName");
        StageProblemStatus = bundle.getString("StageProblem");
        StageNoteStatus = bundle.getString("StageNote");
        StageInformationStatus = bundle.getString("StageInformation");
        Log.i("ClassId--- > ", "" + ClassId);
        Log.i("ClassName--- > ", "" + ClassName);
        Log.i("StageId--- > ", "" + StageId);
        Log.i("StageName--- > ", "" + StageName);
        Log.i("ProblemStatus--- > ", "" + StageProblemStatus);
        Log.i("NoteStatus--- > ", "" + StageNoteStatus);
        Log.i("InformationStatus--- > ", "" + StageInformationStatus);
//        ClassName = bundle.getString("titleName");
//        ClassId = bundle.getString("classId");
//        StageId = bundle.getString("stageId");

        //控件
        initView();
        //时间
//        initData();
        //缓存浮动显示
        initHuanCunIcon();


    }

    //缓存图表icon数量增加
    private Handler CacheHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case Down_ICON:
                    List<VideoItemBean> data = vm.getBufferingVideos();
                    int size = data.size();
                    huancun_icon_redBtn.setText("" + size);
                    break;
            }
            super.handleMessage(msg);
        }
    };

    public void initHuanCunIcon() {
        class_lv.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (firstVisibleItem == 0) {
                    FrameLayout_huancun_icon.setVisibility(View.VISIBLE);
                } else if ((firstVisibleItem + visibleItemCount) == totalItemCount) {
                    FrameLayout_huancun_icon.setVisibility(View.GONE);
                }
            }
        });

        List<VideoItemBean> data = vm.getBufferingVideos();
        int size = data.size();
        huancun_icon_redBtn.setText("" + size);
    }

    public void initView() {
        backLayout = (RelativeLayout) findViewById(R.id.backLayout);
        backLayout.setOnClickListener(this);
        class_title = (TextView) findViewById(R.id.class_title);
        class_lv = (ExpandableListView) findViewById(R.id.class_lv);
        FrameLayout_huancun_icon = (RelativeLayout) findViewById(R.id.FrameLayout_huancun_icon);
        FrameLayout_huancun_icon.setOnClickListener(this);
        huancun_icon_redBtn = (Button) findViewById(R.id.huancun_icon_redBtn);
        jindu_tv = (TextView) findViewById(R.id.jindu_tv);
        sckd_tv = (TextView) findViewById(R.id.sckd_tv);
        jxxx_tv = (TextView) findViewById(R.id.jxxx_tv);
        jxxx_tv.setOnClickListener(this);
    }

    public void initData() {
        GetStageLessonListTask getStageLessonListTask = new GetStageLessonListTask();
        getStageLessonListTask.execute();
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("OnlineClass Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();


    }

    class GetStageLessonListTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                int SID = LoginPre.getInt("S_ID", 0);
                UserID = Integer.toString(SID);
            }

            LessonBean = httpUtils.getStagelessonlist
                    (Const.URL + Const.TonLineListAPI, Const.CLIENT, Const.VERSION, Const.APPNAME, UserID, StageId, ClassId);


            return null;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == LessonBean.getCode()) {
                if (null != LessonBean.getBody().getParentLevelList() && LessonBean.getBody().getParentLevelList().size() > 0) {
                    LessonList = LessonBean.getBody().getParentLevelList();
                    classAdapter = new ClassExpandableListAdapter(OnlineClassActivity.this, LessonList);
                    class_lv.setAdapter(classAdapter);
                    for (int i = 0; i < classAdapter.getGroupCount(); i++) {
                        class_lv.expandGroup(i);
                    }
                    //标题文字
                    String class_ = LessonBean.getBody().getStageName();
                    class_ = class_.replaceAll("&nbsp;", " ");
                    class_title.setText(class_);
                    //学习进度显示
                    String jindu = LessonBean.getBody().getStudySchedule();
                    Log.i("jindu  ====>> ", "" + jindu);
                    if (!"".equals(jindu) && null != jindu) {
                        if (jindu.contains(".")) {
                            jindu = jindu.substring(0, jindu.indexOf("."));
                            jindu_tv.setText(jindu + "%");
                        } else {
                            jindu_tv.setText(jindu);
                        }
                    } else {
                        jindu_tv.setText("0%");
                    }
                    if (null != LessonBean.getBody().getStudyInfo()) {
                        //上次学习到
                        String sckd = LessonBean.getBody().getStudyInfo().getLevel_ShowName();
                        sckd = sckd.replaceAll("&nbsp;", " ");
                        sckd_tv.setText("上次学习到" + sckd);
                        jxxx_tv.setVisibility(View.VISIBLE);
                    } else {
                        sckd_tv.setText("请开始您的学习");
                        jxxx_tv.setVisibility(View.GONE);
                    }
                } else {//请求成功,但是没有数据
                    ShowDialog("暂时没有学习课程，请点击确定返回");
                }
                CacheHandler.sendEmptyMessage(Down_ICON);
            } else {//请求失败
                ShowDialog("获取课程失败,稍后再试");
            }
        }

    }


    public class ClassExpandableListAdapter extends BaseExpandableListAdapter {
        private Activity ctx;
        // 数据源
        private ArrayList<StageLessonListBean.Body.ParentLevelList> mLessList;

        public ClassExpandableListAdapter(Activity ctx, ArrayList<StageLessonListBean.Body.ParentLevelList> mLessList) {
            this.ctx = ctx;
            this.mLessList = mLessList;
        }

        @Override
        public boolean areAllItemsEnabled() {
            return super.areAllItemsEnabled();
        }

        @Override
        public Object getGroup(int postion) {
            return mLessList.get(postion);

        }

        @Override
        public int getGroupCount() {
            return mLessList.size();
        }

        @Override
        public long getGroupId(int groupPosition) {
            return groupPosition;
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public boolean isChildSelectable(int arg0, int arg1) {
            return true;
        }

        @Override
        public boolean isEmpty() {
            return false;
        }

        @Override
        public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
            // 父类标题
            TextView tv_title;
            //数量
            TextView tv_count;
            // 展开收缩Icon
            ImageView jiantou;
            if (null == convertView) {
                convertView = LayoutInflater.from(ctx).inflate(
                        R.layout.onlineclass_group_item, null);
            }
            tv_title = (TextView) convertView.findViewById(R.id.tv_title);
            tv_count = (TextView) convertView.findViewById(R.id.tv_count);
            jiantou = (ImageView) convertView.findViewById(R.id.jiantou);
            String title_name = mLessList.get(groupPosition).getLevel_ShowName();
            tv_title.setText("科目: " + title_name);

            String title_Totalcount = mLessList.get(groupPosition).getTotalcount();
            String title_Studycount = mLessList.get(groupPosition).getStudycount();
            tv_count.setText("( " + title_Studycount + "/" + title_Totalcount + " )");
            if (isExpanded) {
                jiantou.setBackgroundResource(R.drawable.btn_zhankai_icon);
            } else {
                jiantou.setBackgroundResource(R.drawable.btn_shousu_icon);
            }

            return convertView;
        }

        @Override
        public int getChildrenCount(int groupPosition) {
//            Gson s = new Gson();
//            Log.i("111111",""+ s.toJson(mLessList.get(groupPosition).getChildList()));
            if (mLessList.get(groupPosition).getChildList() != null) {
                return mLessList.get(groupPosition).getChildList().size();
            } else {
                return 0;
            }


        }

        @Override
        public long getCombinedChildId(long arg0, long arg1) {
            return 0;
        }

        @Override
        public long getCombinedGroupId(long arg0) {
            return 0;
        }

        @Override
        public Object getChild(int groupPosition, int childPosition) {
            return mLessList.get(groupPosition).getChildList().get(childPosition);
        }

        @Override
        public long getChildId(int groupPosition, int childPosition) {
            return childPosition;
        }

        class ChildViewHolder {
            // 视频内容下载标题TextView
            public TextView tv_content;
            // 直播内容布局显示
            public RelativeLayout tv_content_relative;
            // 直播内容标题显示TextView
            public TextView tv_content_relative_tv;
            // 直播Icon显示
            public ImageView zhibo_icon_status;
            // 直播Time显示
            public TextView tv_content_relative_tv_time;
            // 布局点击事件
            public RelativeLayout layout_content;
            // Icon显示图片
            public ImageView img_title;
            // 下载按钮
            public Button btn_xiazai;
        }

        @Override
        public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
            final ChildViewHolder holder;
            convertView = LayoutInflater.from(ctx).inflate(R.layout.onlineclass_child_item,
                    null);
            holder = new ChildViewHolder();
            holder.tv_content = (TextView) convertView
                    .findViewById(R.id.tv_content);
            holder.tv_content_relative = (RelativeLayout) convertView.findViewById(R.id.tv_content_relative);
            holder.tv_content_relative_tv = (TextView) convertView.findViewById(R.id.tv_content_relative_tv);
            holder.zhibo_icon_status = (ImageView) convertView.findViewById(R.id.zhibo_icon_status);
            holder.tv_content_relative_tv_time = (TextView) convertView.findViewById(R.id.tv_content_relative_tv_time);
            holder.img_title = (ImageView) convertView.findViewById(R.id.img_title);
            holder.btn_xiazai = (Button) convertView.findViewById(R.id.btn_xiazai);
            //item点击条目
            holder.layout_content = (RelativeLayout) convertView.findViewById(R.id.layout_content);

            //视频VID
            final String vid = mLessList.get(groupPosition).getChildList().get(childPosition).getVID();
            //类型判断
            final String KeyType = mLessList.get(groupPosition).getChildList().get(childPosition).getKeyType();
            //课程名字
            String name =
                    mLessList.get(groupPosition).getChildList().get(childPosition).getLevel_ShowName();
            name = name.replace("&nbsp;", "");
            //课程ID
            final String LessonID = mLessList.get(groupPosition).getChildList().get(childPosition).getLevel_PKID();
            //直播日期
            String ZhiBoDate = mLessList.get(groupPosition).getChildList().get(childPosition).getDate();
            try {
                Date date = stringToDate(ZhiBoDate, "yyyy-MM-dd");
                ZhiBoDate = dateToString(date, "yyyy-MM-dd");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            //直播开始时间
            final String KaiShiDate = mLessList.get(groupPosition).getChildList().get(childPosition).getBeginTime();
            //直播结束时间
            final String JieShuDate = mLessList.get(groupPosition).getChildList().get(childPosition).getEndTime();
            //直播状态
            final String LiveStatus = mLessList.get(groupPosition).getChildList().get(childPosition).getLiveStatus();

            //试卷学习状态
            final String QusetionStatus = mLessList.get(groupPosition).getChildList().get(childPosition).getState();

            if ("1".equals(KeyType)) {//1:视频 2:试卷 3:资料 4:直播
                holder.tv_content_relative.setVisibility(View.GONE);
                holder.tv_content.setVisibility(View.VISIBLE);
                holder.tv_content.setText(name);
                holder.img_title.setBackgroundResource(R.drawable.btn_bofang_icon);
                holder.btn_xiazai.setVisibility(View.VISIBLE);

                // 判断视频下载状态
                String isExist = VideoDownloadManager.getInstance().SelectBufferedVideosItems(vid);
                Log.i("是否存在下载列表 -- ", "" + isExist);
                if (!"".equals(isExist) && null != isExist) {//存在下载列表
                    String isStatus = VideoDownloadManager.getInstance().SelectBufferedVideosItemsStatus(isExist);
                    if ("0".equals(isStatus)) {//0;----下载中
                        holder.btn_xiazai.setText("下载中");
                    } else if ("1".equals(isStatus)) {//1;----下载失败
                        //holder.btn_xiazai.setText("下载中");
                        holder.btn_xiazai.setText("下载中");
                    } else if ("2".equals(isStatus)) {//2;----下载成功
                        holder.btn_xiazai.setText("已下载");
                    } else if ("3".equals(isStatus)) {//3;----暂停
                        //holder.btn_xiazai.setText("暂停下载");
                        holder.btn_xiazai.setText("下载中");
                    }
                    //holder.btn_xiazai.setText("已下载");
                    holder.btn_xiazai.setTextColor(Color.parseColor("#DD5555"));
                    holder.btn_xiazai.setBackgroundResource(0);
                    holder.btn_xiazai.setOnClickListener(null);
                } else {
                    holder.btn_xiazai
                            .setBackgroundResource(R.drawable.btn_xiazai_n_icon);
                    //列表下载
                    holder.btn_xiazai.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            //判断读写权限
                            int permission = ActivityCompat.checkSelfPermission(OnlineClassActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                            if (permission != PackageManager.PERMISSION_GRANTED) {
                                // 无权限----
                                ActivityCompat.requestPermissions(OnlineClassActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                            } else {
                                /**学习记录**/
                                String StudyKey = mLessList.get(groupPosition).getChildList().get(childPosition).getStudyKey();
                                /**视频ID**/
                                String VideoId = mLessList.get(groupPosition).getChildList().get(childPosition).getVPKID();
                                /**视频播放的VID**/
                                String Vid = mLessList.get(groupPosition).getChildList().get(childPosition).getVID();
                                /**stage阶段名称*/
                                String stageName = LessonBean.getBody().getStageName();
                                /**lesson科目ID*/
//                    String lessonId = mLessList.get(groupPosition).getChildList().get(childPosition).getLevel_PKID();
//                    String lessonName = mLessList.get(groupPosition).getChildList().get(childPosition).getLevel_ShowName();
                                String lessonId = mLessList.get(groupPosition).getLevel_PKID();
                                String lessonName = mLessList.get(groupPosition).getLevel_ShowName();
                                /**subjectID**/
                                String subjectId = mLessList.get(groupPosition).getChildList().get(childPosition).getLevel_PKID();
                                /**subjectNAME**/
                                String subjectName = mLessList.get(groupPosition).getChildList().get(childPosition).getLevel_ShowName();
                                //TODO 下载测试
//                    if(>PublicFunc.SdcardMemorySize()){//判断SD卡读写空间
//                    }
                                //判断班级数据库
                                if (VideoDownloadManager.getInstance().addClassIdTassk(ClassId, ClassName)) {
                                    //判断阶段数据库
                                    if (VideoDownloadManager.getInstance().addStageIdTassk(ClassId, StageId, StageName)) {
                                        //判断下载列表数据
                                        if (!VideoDownloadManager.getInstance().addVideoTask(Vid, ClassId, lessonId, lessonName, subjectId, subjectName, StageId, VideoId, "isStudy", StudyKey, StageProblemStatus, StageNoteStatus, StageInformationStatus)) {
                                            Toast.makeText(OnlineClassActivity.this,
                                                    "已经到达同时最大下载数量5个，请稍后下载", Toast.LENGTH_LONG)
                                                    .show();
                                        } else {
                                            Log.i("下载队列 Vid ==> ", Vid);
                                            Log.i("下载队列 ClassId ==> ", ClassId);
                                            Log.i("下载队列 ClassName ==> ", ClassName);
                                            Log.i("下载队列 StageId ==> ", StageId);
                                            Log.i("下载队列 stageName ==> ", stageName);
                                            Log.i("下载队列 lessonId ==> ", lessonId);
                                            Log.i("下载队列 lessonName ==> ", lessonName);
                                            Log.i("下载队列 subjectId ==> ", subjectId);
                                            Log.i("下载队列 subjectName ==> ", subjectName);
//                                        holder.btn_xiazai
//                                                .setBackgroundResource(R.drawable.btn_xiazai_icon);
                                            holder.btn_xiazai.setText("下载中");
                                            holder.btn_xiazai.setTextColor(Color.parseColor("#DD5555"));
                                            holder.btn_xiazai.setBackgroundResource(0);
                                            holder.btn_xiazai.setOnClickListener(null);
                                            Toast.makeText(OnlineClassActivity.this, "成功添加进下载队列",
                                                    Toast.LENGTH_LONG).show();
                                            CacheHandler.sendEmptyMessage(Down_ICON);
                                        }
                                    }
                                }
                            }

                        }
                    });
                }
            } else if ("2".equals(KeyType)) {
                holder.tv_content_relative.setVisibility(View.GONE);
                holder.tv_content.setVisibility(View.VISIBLE);
                holder.tv_content.setText(name);
                holder.img_title.setBackgroundResource(R.drawable.btn_ceshi_icon);
                if ("未学".equals(QusetionStatus)) {
                    holder.btn_xiazai.setText("未答题");
                } else if ("学中".equals(QusetionStatus)) {
                    holder.btn_xiazai.setText("答题中");
                } else if ("已学".equals(QusetionStatus)) {
                    holder.btn_xiazai.setText("已答题");
                }
                holder.btn_xiazai.setTextColor(Color.parseColor("#DD5555"));
                holder.btn_xiazai.setBackgroundResource(0);
                holder.btn_xiazai.setVisibility(View.VISIBLE);
            } else if ("4".equals(KeyType)) {
                holder.tv_content.setVisibility(View.GONE);
                holder.tv_content_relative.setVisibility(View.VISIBLE);
                holder.tv_content_relative_tv.setText(name);
                holder.tv_content_relative_tv_time.setText(ZhiBoDate + " " + KaiShiDate + "-" + JieShuDate);
                holder.img_title.setBackgroundResource(R.drawable.btn_zhibo_icon);
                holder.btn_xiazai.setVisibility(View.GONE);
                if ("0".equals(LiveStatus)) {//直播状态：0 未开始 1 直播中 2 已结束
                    holder.zhibo_icon_status.setBackgroundResource(R.drawable.zb_icon_weikaishi);
                } else if ("1".equals(LiveStatus)) {
                    holder.zhibo_icon_status.setBackgroundResource(R.drawable.zb_icon_zhibozhong);
                } else if ("2".equals(LiveStatus)) {
                    int CS_IsPlayback = mLessList.get(groupPosition).getChildList().get(childPosition).getCS_IsPlayback();
                    if (1 == CS_IsPlayback) {
                        holder.zhibo_icon_status.setBackgroundResource(R.drawable.icon_huifang);
                    } else {
                        holder.zhibo_icon_status.setBackgroundResource(R.drawable.icon_wuhuifang);
                    }
                }
            }
            //点击条目跳转
            holder.layout_content.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /**视频VID**/
                    String VID = mLessList.get(groupPosition).getChildList().get(childPosition).getVID();
                    /**视频ID**/
                    String VPKID = mLessList.get(groupPosition).getChildList().get(childPosition).getVPKID();
                    /**stage阶段ID*/
                    String Level_ParentID = mLessList.get(groupPosition).getLevel_PKID();
                    /**lesson科目ID*/
                    String Level_PKID = mLessList.get(groupPosition).getChildList().get(childPosition).getLevel_PKID();
                    /**试卷ID**/
                    String PapgersId = mLessList.get(groupPosition).getChildList().get(childPosition).getTestPaper_PKID();
                    /**试卷名称**/
                    String PapgersName = mLessList.get(groupPosition).getChildList().get(childPosition).getLevel_ShowName();
                    /**直播H5**/
                    String LiveH5 = mLessList.get(groupPosition).getChildList().get(childPosition).getLevel_ShowName();

                    if ("1".equals(KeyType)) {//1:视频 2:试卷 3:资料 4:直播
                        /**LessonID**/
                        String lessonId = mLessList.get(groupPosition).getLevel_PKID();
                        /**LessonName**/
                        String lessonName = mLessList.get(groupPosition).getLevel_ShowName();
                        /**subjectID**/
                        String subjectId = mLessList.get(groupPosition).getChildList().get(childPosition).getLevel_PKID();
                        /**subjectNAME**/
                        String subjectName = mLessList.get(groupPosition).getChildList().get(childPosition).getLevel_ShowName();
                        /**学习记录**/
                        String StudyKey = mLessList.get(groupPosition).getChildList().get(childPosition).getStudyKey();


                        Level_ParentID_GO = Level_ParentID;
                        Level_PKID_GO = Level_PKID;
                        lessonId_GO = lessonId;
                        lessonName_GO = lessonName;
                        subjectId_GO = subjectId;
                        subjectName_GO = subjectName;
                        StudyKey_GO = StudyKey;

                        Intent VideoIntent = new Intent(ctx, VerticalScreenActivity.class);
                        Bundle VideoBundle = new Bundle();
                        VideoBundle.putString("VID", VID);
                        VideoBundle.putString("VPKID", VPKID);
                        VideoBundle.putString("ClassID", ClassId);
                        VideoBundle.putString("StageID", StageId);
                        VideoBundle.putString("Level_ParentID", Level_ParentID);
                        VideoBundle.putString("Level_PKID", Level_PKID);
                        /**新增传递的下载参数**/
                        VideoBundle.putString("className", ClassName);
                        VideoBundle.putString("stageName", StageName);
                        VideoBundle.putString("lessonId", lessonId);
                        VideoBundle.putString("lessonName", lessonName);
                        VideoBundle.putString("subjectId", subjectId);
                        VideoBundle.putString("subjectName", subjectName);
                        VideoBundle.putString("studyKey", StudyKey);

                        VideoBundle.putString("StageProblemStatus", StageProblemStatus);
                        VideoBundle.putString("StageNoteStatus", StageNoteStatus);
                        VideoBundle.putString("StageInformationStatus", StageInformationStatus);
                        /**新增下载参数的传递**/
                        VideoIntent.putExtras(VideoBundle);
                        startActivity(VideoIntent);
                    } else if ("2".equals(KeyType)) {
                        String StudyStatus = mLessList.get(groupPosition).getChildList().get(childPosition).getState();
                        if ("已学".equals(StudyStatus)) {
                            String lessonId = mLessList.get(groupPosition).getLevel_PKID();
                            String subjectId = mLessList.get(groupPosition).getChildList().get(childPosition).getLevel_PKID();
                            String TestPPKId = mLessList.get(groupPosition).getChildList().get(childPosition).getTestPaper_PKID();
                            int classid = Integer.parseInt(ClassId);
                            int stageid = Integer.parseInt(StageId);
                            int lessonid = Integer.parseInt(lessonId);
                            int subjectid = Integer.parseInt(subjectId);
                            int TestPPKID = Integer.parseInt(TestPPKId);
                            Intent PapersIntent = new Intent(ctx, CardResultActivity.class);
                            Bundle PapgersBundle = new Bundle();
                            PapgersBundle.putInt("TestPPKID", TestPPKID);
                            PapgersBundle.putInt("classId", classid);
                            PapgersBundle.putInt("stageId", stageid);
                            PapgersBundle.putInt("lessonId", lessonid);
                            PapgersBundle.putInt("levelId", subjectid);
                            PapgersBundle.putInt("isOnline", 1);
                            PapgersBundle.putString("name", PapgersName);
                            PapersIntent.putExtras(PapgersBundle);
                            startActivity(PapersIntent);
                        } else {
                            Intent PapersIntent = new Intent(ctx, MyQuestionActivity.class);
                            Bundle PapgersBundle = new Bundle();
                            int i = Integer.parseInt(PapgersId);
                            PapgersBundle.putInt("paperId", i);
                            PapgersBundle.putString("title", PapgersName);
                            //新增试卷学习状态参数
                            String lessonId = mLessList.get(groupPosition).getLevel_PKID();
                            String subjectId = mLessList.get(groupPosition).getChildList().get(childPosition).getLevel_PKID();
                            String _StudyKey = mLessList.get(groupPosition).getChildList().get(childPosition).getStudyKey();
                            int classid = Integer.parseInt(ClassId);
                            int stageid = Integer.parseInt(StageId);
                            int lessonid = Integer.parseInt(lessonId);
                            int subjectid = Integer.parseInt(subjectId);


                            PapgersBundle.putInt("classid", classid);
                            PapgersBundle.putInt("stageid", stageid);
                            PapgersBundle.putInt("lessonid", subjectid);
                            PapgersBundle.putInt("subjectid", lessonid);
                            PapgersBundle.putString("studyKey", _StudyKey);
                            PapgersBundle.putInt("online", 1);

                            Log.i("classid", "" + classid);
                            Log.i("stageid", "" + stageid);
                            Log.i("subjectid", "" + subjectid);
                            Log.i("lessonid", "" + lessonid);
                            PapersIntent.putExtras(PapgersBundle);
                            startActivity(PapersIntent);
                        }
                    } else if ("4".equals(KeyType)) {
                        if ("1".equals(LiveStatus)) {
                            String classidS = mLessList.get(groupPosition).getLevel_PKID();
                            int classid = Integer.parseInt(classidS);
                            int scheduleId = mLessList.get(groupPosition).getChildList().get(childPosition).getKeyID();
                            String LiveStatus = mLessList.get(groupPosition).getChildList().get(childPosition).getLiveStatus();
                            String Liveurl = mLessList.get(groupPosition).getChildList().get(childPosition).getLiveUrl();
//                            Intent LiveIntent = new Intent(ctx, LiveActivity.class);
//                            Bundle LiveBundle = new Bundle();
//                            LiveBundle.putString("LiveH5", Liveurl);
//                            LiveBundle.putString("LiveStatus", LiveStatus);
//                            LiveIntent.putExtras(LiveBundle);
//                            startActivity(LiveIntent);

                            Intent intent = new Intent(ctx, LiveRoomActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("scheduleId", scheduleId);
                            bundle.putInt("classid", classid);
                            bundle.putInt("searchType", 0);
                            intent.putExtras(bundle);
                            startActivity(intent);

                        } else if ("2".equals(LiveStatus)) {
                            int CS_IsPlayback = mLessList.get(groupPosition).getChildList().get(childPosition).getCS_IsPlayback();
                            if (1 == CS_IsPlayback) {
                                String classidS = mLessList.get(groupPosition).getLevel_PKID();
                                int classid = Integer.parseInt(classidS);
                                int scheduleId = mLessList.get(groupPosition).getChildList().get(childPosition).getKeyID();
                                String Liveurl = mLessList.get(groupPosition).getChildList().get(childPosition).getLiveUrl();
                                String LiveStatus = mLessList.get(groupPosition).getChildList().get(childPosition).getLiveStatus();
//                                Intent LiveIntent = new Intent(ctx, LiveActivity.class);
//                                Bundle LiveBundle = new Bundle();
//                                LiveBundle.putString("LiveH5", Liveurl);
//                                LiveBundle.putString("LiveStatus", LiveStatus);
//                                LiveIntent.putExtras(LiveBundle);
//                                startActivity(LiveIntent);
                                Intent intent = new Intent(ctx, VodRoomActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("scheduleId", scheduleId);
                                bundle.putInt("classid", classid);
                                bundle.putInt("searchType", 0);
                                intent.putExtras(bundle);
                                startActivity(intent);
                            }
                        }

                    }
                }
            });

            return convertView;
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backLayout:
                finish();
                break;

            case R.id.jxxx_tv:
                //继续学习内容
                GoOnStudy();
                break;

            case R.id.FrameLayout_huancun_icon:
                Intent new_cache_Intent = new Intent(this, ManagerActivity.class);
                startActivity(new_cache_Intent);
                break;
        }
    }

    private void GoOnStudy() {
        //类型1:视频 2:试卷
        String type = LessonBean.getBody().getStudyInfo().getKeyType();
        if (!"".equals(LessonBean.getBody().getStudyInfo()) && null != LessonBean.getBody().getStudyInfo()) {
            if ("1".equals(type)) {//视频
                String VID = LessonBean.getBody().getStudyInfo().getVID();
                String VPKID = LessonBean.getBody().getStudyInfo().getVPKID();
//                String StudyKey = LessonBean.getBody().getParentLevelList()
                String title = LessonBean.getBody().getStudyInfo().getLevel_ShowName();
                if (!"".equals(VID)) {
//                    IjkVideoActicity.intentTo(this, IjkVideoActicity.PlayMode.landScape, IjkVideoActicity.PlayType.vid, VID,
//                            true, title);
//                    IjkVideoActicity.intentTo(this, IjkVideoActicity.PlayMode.landScape, IjkVideoActicity.PlayType.vid, VID,
//                            true, title, ClassId, StageId, VPKID, "isStudy", VID, "0", StageProblemStatus, StageProblemStatus, StageInformationStatus);

                    Intent VideoIntent = new Intent(this, VerticalScreenActivity.class);
                    Bundle VideoBundle = new Bundle();
                    VideoBundle.putString("VID", VID);
                    VideoBundle.putString("VPKID", VPKID);
                    VideoBundle.putString("ClassID", ClassId);
                    VideoBundle.putString("StageID", StageId);
                    VideoBundle.putString("Level_ParentID", Level_ParentID_GO);
                    VideoBundle.putString("Level_PKID", Level_PKID_GO);
                    /**新增传递的下载参数**/
                    VideoBundle.putString("className", ClassName);
                    VideoBundle.putString("stageName", StageName);
                    VideoBundle.putString("lessonId", lessonId_GO);
                    VideoBundle.putString("lessonName", lessonName_GO);
                    VideoBundle.putString("subjectId", subjectId_GO);
                    VideoBundle.putString("subjectName", subjectName_GO);
                    VideoBundle.putString("studyKey", StudyKey_GO);

                    VideoBundle.putString("StageProblemStatus", StageProblemStatus);
                    VideoBundle.putString("StageNoteStatus", StageNoteStatus);
                    VideoBundle.putString("StageInformationStatus", StageInformationStatus);
                    /**新增下载参数的传递**/
                    VideoIntent.putExtras(VideoBundle);
                    startActivity(VideoIntent);


                } else {
                    Toast.makeText(this, "暂无视频", Toast.LENGTH_SHORT).show();
                }
            } else if ("2".equals(type)) {//试卷
                String PapgersId = LessonBean.getBody().getStudyInfo().getTestPaper_PKID();
                if (!"".equals(PapgersId)) {
                    String PapgersName = LessonBean.getBody().getStudyInfo().getLevel_ShowName();

                    String lessonId = LessonBean.getBody().getStudyInfo().getLevel_Parent();
                    String subjectId = LessonBean.getBody().getStudyInfo().getLevel_PKID();

                    int classid = Integer.parseInt(ClassId);
                    int stageid = Integer.parseInt(StageId);
                    int lessonid = Integer.parseInt(lessonId);
                    int subjectid = Integer.parseInt(subjectId);

                    Intent PapersIntent = new Intent(this, MyQuestionActivity.class);
                    Bundle PapgersBundle = new Bundle();
                    PapgersBundle.putString("paperId", PapgersId);
                    PapgersBundle.putString("title", PapgersName);

                    PapgersBundle.putInt("classid", classid);
                    PapgersBundle.putInt("stageid", stageid);
                    PapgersBundle.putInt("lessonid", lessonid);
                    PapgersBundle.putInt("subjectid", subjectid);
                    PapgersBundle.putInt("online", 1);
                    PapersIntent.putExtras(PapgersBundle);
                    startActivity(PapersIntent);

                } else {
                    Toast.makeText(this, "暂无试题", Toast.LENGTH_SHORT).show();
                }

            }
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();

    }


    //弹出对话框
    private void ShowDialog(String content) {
        final ShowNullDialog nullDialog = new ShowNullDialog(this, content);
        nullDialog.show();
        nullDialog.setonClick(new ShowNullDialog.ICoallBack() {
            @Override
            public void onClickOkButton(String s) {
                if ("null".equals(s)) {
                    nullDialog.dismiss();
                    OnlineClassActivity.this.finish();
                }
            }
        });
    }


    public static Date stringToDate(String strTime, String formatType)
            throws ParseException {
        SimpleDateFormat formatter = new SimpleDateFormat(formatType);
        Date date = null;
        date = formatter.parse(strTime);
        return date;
    }

    public static String dateToString(Date data, String formatType) {
        return new SimpleDateFormat(formatType).format(data);
    }


//    Runnable runnable = new Runnable() {
//        @Override
//        public void run() {
//            Message msg = new Message();
//            Bundle data = new Bundle();
//            data.putString("msg_vid", PoylvVid);
//            msg.setData(data);
//            PolyvSDKUtilHandler.sendMessage(msg);
//        }
//    };

//    private Handler PolyvSDKUtilHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            Log.i("111111111", "222222");
//            ArrayList exceptionList = new ArrayList();
//            PolyvVideoJSONVO videoJSONVO = SDKUtil.getVideoJSONString(msg.getData().getString("msg_vid"), exceptionList);
//            Video video = Video.fromJSONObject(msg.getData().getString("msg_vid"), videoJSONVO.getJson());
//            String videoBody = videoJSONVO.getBody();
//            int videoStaus = video.getStatus();
////                Video video = PolyvSDKUtil.getVideoJSONString(msg.getData().getString("msg_vid"));
////                int i = video.getStatus();
//            Log.i("验证视频信息是否完整可以下载", "" + video + " , " + videoStaus);
//
//        }
//    };


    @Override
    protected void onResume() {
        super.onResume();
        if (httpUtils.isNetworkConnected(this)) {
            initData();
        }

    }
}
