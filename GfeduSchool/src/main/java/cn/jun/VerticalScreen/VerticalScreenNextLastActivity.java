package cn.jun.VerticalScreen;


import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.ijk.IjkVideoView;
import com.easefun.polyvsdk.ijk.OnPreparedListener;
import com.easefun.polyvsdk.util.ScreenTool;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import cn.jun.VerticalScreenPolyv.VerticalMediaController;
import cn.jun.VerticalScreenPolyv.VerticalVideoViewContainer;
import cn.jun.adapter.BiJiAdapter;
import cn.jun.adapter.WenDaAdapter;
import cn.jun.bean.AddvideostudylogBean;
import cn.jun.bean.Const;
import cn.jun.bean.NotesListBean;
import cn.jun.bean.OnLineInfoBean;
import cn.jun.bean.QuesListBean;
import cn.jun.danmu.BarrageView;
import cn.jun.menory.service.VideoDownloadManager;
import cn.jun.utils.HttpUtils;
import cn.jun.view.XListView;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.ui.note.AddNoteActivity;
import jc.cici.android.atom.ui.note.NoteDetailActivity;
import jc.cici.android.atom.ui.note.QuestionDetailActivity;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public class VerticalScreenNextLastActivity extends Activity implements View.OnClickListener, XListView.IXListViewListener {
    private BarrageView barrageView;
    private int SID ;
    private String UserTel;
    private String S_RealName;
    public static VerticalScreenNextLastActivity VerticalAc = null;
    private boolean startNow = false;
    private static final String TAG = "VerticalScreenActivity";
    //    private ScreenListener screenListener;
    private static IjkVideoView videoView = null;
    int w = 0, h = 0, adjusted_h = 0;
    private VerticalVideoViewContainer rl = null;
    private VerticalMediaController mediaController = null;
    private ProgressBar progressBar;
    private ImageView logo = null;
    //    private int stopPosition = 0;
    private int fastForwardPos = 0;
    /**
     * 是否在播放中，用于锁频后返回继续播放
     */
    private boolean isPlay = false;
    /**
     * 是否暂停中，用于home键切出去回来后暂停播放
     */
    private boolean isPause = false;
    //控件
    private RelativeLayout topbar, jianjie_relative;
    // 学过了
    //private ImageView xgl_ImageView;
    private ImageButton backbtn;
    //切换按钮
    private RelativeLayout xuexi_btn, wenda_btn, biji_btn;
    //底部布局
    private RelativeLayout bottom_bar;
    //导航条按钮
    private ImageButton imagebtn_biji, imagebtn_wenti;
    private RelativeLayout bottom_tv;
    //listview切换
//    private ListView wendaListView;
    private XListView wendaListView;
    //    private ListView bijiListView;
    private XListView bijiListView;
    //适配器
    private WenDaAdapter wenDaAdapter;
    private BiJiAdapter bijiAdapter;
    // 接收传递过来的视频VID
    private static String VID;
    private static String VPKID;
    private static String Level_ParentID;
    private static String Level_PKID;
    private static String ClassID;
    private static String StageID;
    private static String studyKey;
//    private String ClassName;
//    private String StageName;
//    private String lessonName;
//    private String subjectId;
//    private String subjectName;


    //课程大标题
    private TextView lesson_title;
    //底部标题
    private TextView bottom_tv_title;
    //左
    private ImageButton left_ivBtn;
    //右
    private ImageButton right_ivBtn;
    //上拉加载更多count
    private static int refreshCnt = 0;
    private int QusePagerstart = 0;
    private int NotesPagerstart = 1;
    private int PagerSize = 10;

    private HttpUtils httpUtils = new HttpUtils();
    private NotesListBean notesListBean;
    private QuesListBean quesListBean;
    private ArrayList<NotesListBean> notesArrayList = new ArrayList<>();
    private ArrayList<QuesListBean> quesArrayList = new ArrayList<>();

    //课程详情信息
    private OnLineInfoBean OnLineBean;
//    private ArrayList<OnLineInfoBean> OnLineInfoList = new ArrayList<>();

    //记录学习进度时间
    private AddvideostudylogBean addvideostudylogBean;
    //用户ID
    private String userID;
    private static int classId;
    private static int lessonId;
    private static int stageId;
    private static int videoId;

    private String downClassName;
    private String downStageName;
    private String downlessonId;
    private String downlessonName;
    private String downsubjectId;
    private String downsubjectName;

    //下载按钮
    private Button zsd_down_icon;
    //视频时长
    private TextView sp_time;
    //视频名字
    private static String VideoName;
    // 服务状态(笔记,问题,资料是否解锁)
    public String StageProblemStatus;
    public String StageNoteStatus;
    public String StageInformationStatus;

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vertical_screen_activity);
        VerticalAc = this;
        Bundle bundle = getIntent().getExtras();
        ClassID = bundle.getString("ClassID");
        StageID = bundle.getString("StageID");
        VID = bundle.getString("VID");
        VPKID = bundle.getString("VPKID");
        Level_ParentID = bundle.getString("Level_ParentID");
        Level_PKID = bundle.getString("Level_PKID");

        downClassName = bundle.getString("className");
        downStageName = bundle.getString("stageName");
        downlessonId = bundle.getString("lessonId");
        downlessonName = bundle.getString("lessonName");
        downsubjectId = bundle.getString("subjectId");
        downsubjectName = bundle.getString("subjectName");
        studyKey = bundle.getString("studyKey");

        StageProblemStatus = bundle.getString("StageProblemStatus");
        StageNoteStatus = bundle.getString("StageNoteStatus");
        StageInformationStatus = bundle.getString("StageInformationStatus");

        Log.i("ClassID ===> ", "" + ClassID);
        Log.i("StageID ===> ", "" + StageID);
        Log.i("VID ===> ", "" + VID);
        Log.i("VPKID ===> ", "" + VPKID);
        Log.i("Level_ParentID ===> ", "" + Level_ParentID);
        Log.i("Level_PKID ===> ", "" + Level_PKID);

        Log.i("downClassName ===> ", "" + downClassName);
        Log.i("downStageName ===> ", "" + downStageName);
        Log.i("downlessonId ===> ", "" + downlessonId);
        Log.i("downlessonName ===> ", "" + downlessonName);
        Log.i("downsubjectId ===> ", "" + downsubjectId);
        Log.i("downsubjectName ===> ", "" + downsubjectName);
        Log.i("studyKey ===> ", "" + studyKey);

        Log.i("StageProblemStatus ", "" + StageProblemStatus);
        Log.i("StageNoteStatus  ", "" + StageNoteStatus);
        Log.i("StageInformationStatus ", "" + StageInformationStatus);


        //控件
        initView();
        //课程详情数据
        initInfoData();
        //视频
        initPoly();
//        //lv数据
//        initData();
        //视频播放加载
        initPlay(PlayMode.portrait.getCode(),
                PlayType.vid.getCode(), VID);

        initPlayMode(PlayMode.portrait.getCode());
    }

    public void initInfoData() {
        InfoTask infoTask = new InfoTask();
        infoTask.execute();

    }

    class InfoTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                int SID = LoginPre.getInt("S_ID", 0);
                userID = Integer.toString(SID);
            }
            OnLineBean = httpUtils.getOnLineInfoBean
                    (Const.URL + Const.OnLineInfoAPI,
                            Const.CLIENT,
                            Const.VERSION,
                            Const.APPNAME,
                            userID,
                            StageID,
                            ClassID,
                            Level_ParentID,
                            Level_PKID);
            //测试上一题下一题课程
//            OnLineBean = httpUtils.getOnLineInfoBean
//                    (Const.URL + Const.OnLineInfoAPI,
//                            Const.CLIENT,
//                            Const.VERSION,
//                            Const.APPNAME,
//                            "123898",
//                            "43", "64", "43", "46");

            return null;
        }

//        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if ("100".equals(OnLineBean.getCode())) {
                Log.i("传递过来的VID ==> ", VID);
                Log.i("重新获取的VID ==> ", OnLineBean.getBody().getInfo().getVID());
                VideoName = OnLineBean.getBody().getInfo().getLevel_ShowName();
                VideoName = VideoName.replaceAll("&nbsp;"," ");
                lesson_title.setText(VideoName);
                bottom_tv_title.setText(VideoName);
                //课程时长
                String Vtime = OnLineBean.getBody().getInfo().getVTime();
                sp_time.setText(Vtime);

                Log.i("StudyKey ==> ", OnLineBean.getBody().getInfo().getStudyKey());

                SharedPreferences LoginJson_preferences = getSharedPreferences(
                        "xgl_StudyKey", Activity.MODE_PRIVATE);
                SharedPreferences.Editor editor = LoginJson_preferences.edit();
                editor.putString("StudyKey", OnLineBean.getBody().getInfo().getStudyKey());
                editor.commit();
            }


        }

    }


    public void initView() {
        topbar = (RelativeLayout) findViewById(R.id.topbar);
        jianjie_relative = (RelativeLayout) findViewById(R.id.jianjie_relative);
        bottom_bar = (RelativeLayout) findViewById(R.id.bottom_bar);

        backbtn = (ImageButton) findViewById(R.id.backbtn);
        backbtn.setOnClickListener(this);

        bottom_tv = (RelativeLayout) findViewById(R.id.bottom_tv);

//        // 学过了
//        xgl_ImageView = (ImageView) findViewById(R.id.xueguo_iv);
//        xgl_ImageView.setOnClickListener(this);

        xuexi_btn = (RelativeLayout) findViewById(R.id.xuexi);
        wenda_btn = (RelativeLayout) findViewById(R.id.wenda);
        biji_btn = (RelativeLayout) findViewById(R.id.biji);
        xuexi_btn.setOnClickListener(this);
        wenda_btn.setOnClickListener(this);
        biji_btn.setOnClickListener(this);


        imagebtn_biji = (ImageButton) findViewById(R.id.imagebtn_biji);
        imagebtn_biji.setOnClickListener(this);
        imagebtn_wenti = (ImageButton) findViewById(R.id.imagebtn_wenti);
        imagebtn_wenti.setOnClickListener(this);

        if ("1".equals(StageProblemStatus)) {//问答服务开放
            imagebtn_wenti.setVisibility(View.VISIBLE);
            wenda_btn.setVisibility(View.VISIBLE);
        } else if ("0".equals(StageProblemStatus)) {//问答服务关闭
            imagebtn_wenti.setVisibility(View.GONE);
            wenda_btn.setVisibility(View.GONE);
        }
        if ("1".equals(StageNoteStatus)) {//笔记服务开放
            imagebtn_biji.setVisibility(View.VISIBLE);
            biji_btn.setVisibility(View.VISIBLE);
        } else if ("0".equals(StageNoteStatus)) {//笔记服务关闭
            imagebtn_biji.setVisibility(View.GONE);
            biji_btn.setVisibility(View.GONE);
        }

        wendaListView = (XListView) findViewById(R.id.wenda_listview);
        wendaListView.setPullLoadEnable(true);
        wendaListView.setXListViewListener(this);
        wendaListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("wendaListViewItemClick", "wendaListViewItemClick");
                String quesIdS = quesArrayList.get(0).getBody().getQuesList().get(position).getQuesId();
                int quesId = Integer.parseInt(quesIdS);
                String isUserS = quesArrayList.get(0).getBody().getQuesList().get(position).getMyQues();
                int isUser = Integer.parseInt(isUserS);
                Intent wendaIntent = new Intent(VerticalScreenNextLastActivity.this, QuestionDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("quesId", quesId);
                bundle.putInt("page", 0);
                bundle.putInt("childClassTypeId", 0);
                Log.i("isUser", "" + isUser);
                if (0 == isUser) {
                    bundle.putInt("jumpFlag", 2);
                } else {
                    bundle.putInt("jumpFlag", 1);
                }

                wendaIntent.putExtras(bundle);
                startActivity(wendaIntent);
            }
        });
        bijiListView = (XListView) findViewById(R.id.biji_listview);
        bijiListView.setPullLoadEnable(true);
        bijiListView.setXListViewListener(this);
        bijiListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.i("bijiListViewItemClick", "bijiListViewItemClick");
                String noteIdS = notesArrayList.get(0).getBody().getNotesList().get(position).getNTBPkid();
                int noteId = Integer.parseInt(noteIdS);
                String isUserS = notesArrayList.get(0).getBody().getNotesList().get(position).getIsUser();
                int isUser = Integer.parseInt(isUserS);
                Intent bijiIntent = new Intent(VerticalScreenNextLastActivity.this, NoteDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("noteId", noteId);
                Log.i("isUser", "" + isUser);
                if (0 == isUser) {
                    bundle.putInt("key", 2);
                } else {
                    bundle.putInt("key", 1);
                }
                bijiIntent.putExtras(bundle);
                startActivity(bijiIntent);
            }
        });

        //课程标题
        lesson_title = (TextView) findViewById(R.id.lesson_title);
        //课程时长
        sp_time = (TextView) findViewById(R.id.sp_time);
        //底部标题
        bottom_tv_title = (TextView) findViewById(R.id.bottom_tv_title);
        //左
        left_ivBtn = (ImageButton) findViewById(R.id.left_ivBtn);
        left_ivBtn.setOnClickListener(this);
        //右
        right_ivBtn = (ImageButton) findViewById(R.id.right_ivBtn);
        right_ivBtn.setOnClickListener(this);

        //下载按钮
        zsd_down_icon = (Button) findViewById(R.id.zsd_down_icon);
        // 判断视频下载状态
        String isExist = VideoDownloadManager.getInstance().SelectBufferedVideosItems(VID);
        if (!"".equals(isExist) && null != isExist) {
            zsd_down_icon.setText("已下载");
            zsd_down_icon.setTextColor(Color.parseColor("#DD5555"));
            zsd_down_icon.setBackgroundResource(0);
            zsd_down_icon.setOnClickListener(null);
        } else {
            zsd_down_icon.setBackgroundResource(R.drawable.btn_xiazai_n_icon);
            zsd_down_icon.setOnClickListener(this);
        }
    }

    private void initPoly() {
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        int[] wh = ScreenTool.getNormalWH(this);
        w = wh[0];
        h = wh[1];
        // 小窗口的比例
        float ratio = (float) 16 / 8;
        adjusted_h = (int) Math.ceil((float) w / ratio);
        rl = (VerticalVideoViewContainer) findViewById(R.id.rl);
        rl.setLayoutParams(new RelativeLayout.LayoutParams(w, adjusted_h));
        videoView = (IjkVideoView) findViewById(R.id.videoview);
        videoView.setOpenSRT(true);
        videoView.setNeedGestureDetector(true);
        videoView.setAutoContinue(true);
        videoView.setOpenTeaser(true);
        videoView.setOpenAd(true);
        videoView.setOpenQuestion(true);

        progressBar = (ProgressBar) findViewById(R.id.loadingprogress);
        TextView videoAdCountDown = (TextView) findViewById(R.id.count_down);
        logo = (ImageView) findViewById(R.id.logo);
        // 在缓冲时出现的loading


        videoView.setMediaBufferingIndicator(progressBar);
        videoView.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);

        videoView.setOnPreparedListener(new OnPreparedListener() {
            @Override
            public void onPrepared(IMediaPlayer mp) {
                videoView.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
                logo.setVisibility(View.GONE);
//                System.out.println("stopPosition 进度-- " + stopPosition);
//                if (stopPosition > 0) {
//                    videoView.seekTo(stopPosition);
//                }

                if (isPause) {
                    videoView.pause(true);
                }
                System.out.println(" ===是否在线播放 " + videoView.isLocalPlay());
            }
        });

        videoView
                .setOnVideoStatusListener(new IjkVideoView.OnVideoStatusListener() {

                    @Override
                    public void onStatus(int status) {

                    }
                });

        videoView
                .setOnVideoPlayErrorLisener(new IjkVideoView.OnVideoPlayErrorLisener() {

                    @Override
                    public boolean onVideoPlayError(IjkVideoView.ErrorReason errorReason) {
                        return false;
                    }

                });


        videoView.setOnPlayPauseListener(new IjkVideoView.OnPlayPauseListener() {

            @Override
            public void onPlay() {
                isPause = false;
            }

            @Override
            public void onPause() {
                isPause = true;
            }

            @Override
            public void onCompletion() {
                mediaController.setProgressMax();
                int VideoTime = videoView.getCurrentPosition();
                // 播放完成，上传学习进度
                if (httpUtils.isNetworkConnected(VerticalScreenNextLastActivity.this)) {
                    AddVideoTask addVideoLog = new AddVideoTask();
                    addVideoLog.execute(VideoTime);
                }

                if (videoView != null) {
                    videoView.destroy();
                }
                finish();

            }
        });

        // 新增事件--音量亮度控制
        videoView.setClick(new IjkVideoView.Click() {

            @Override
            public void callback(boolean start, boolean end) {
                mediaController.toggleVisiblity();
            }
        });

        videoView.setLeftUp(new IjkVideoView.LeftUp() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftUp %b %b brightness %d", start, end, videoView.getBrightness()));
                int brightness = videoView.getBrightness() + 5;
                if (brightness > 100) {
                    brightness = 100;
                }

                videoView.setBrightness(brightness);

            }
        });

        videoView.setLeftDown(new IjkVideoView.LeftDown() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("LeftDown %b %b brightness %d", start, end, videoView.getBrightness()));
                int brightness = videoView.getBrightness() - 5;
                if (brightness < 0) {
                    brightness = 0;
                }

                videoView.setBrightness(brightness);

            }
        });

        videoView.setRightUp(new IjkVideoView.RightUp() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightUp %b %b volume %d", start, end, videoView.getVolume()));
                // 加减单位最小为10，否则无效果
                int volume = videoView.getVolume() + 10;
                if (volume > 100) {
                    volume = 100;
                }

                videoView.setVolume(volume);
            }
        });

        videoView.setRightDown(new IjkVideoView.RightDown() {

            @Override
            public void callback(boolean start, boolean end) {
                Log.d(TAG, String.format("RightDown %b %b volume %d", start, end, videoView.getVolume()));
                // 加减单位最小为10，否则无效果
                int volume = videoView.getVolume() - 10;
                if (volume < 0) {
                    volume = 0;
                }

                videoView.setVolume(volume);
            }
        });

        videoView.setSwipeLeft(new IjkVideoView.SwipeLeft() {

            @Override
            public void callback(boolean start, boolean end) {
                // TODO 左滑事件
                Log.d(TAG, String.format("SwipeLeft %b %b", start, end));
                if (fastForwardPos == 0) {
                    fastForwardPos = videoView.getCurrentPosition();
                }

                if (end) {
                    if (fastForwardPos < 0)
                        fastForwardPos = 0;
                    videoView.seekTo(fastForwardPos);
                    fastForwardPos = 0;
                } else {
                    fastForwardPos -= 10000;
                }

            }
        });

        videoView.setSwipeRight(new IjkVideoView.SwipeRight() {

            @Override
            public void callback(boolean start, boolean end) {
                // TODO 右滑事件
                Log.d(TAG, String.format("SwipeRight %b %b", start, end));
                if (fastForwardPos == 0) {
                    fastForwardPos = videoView.getCurrentPosition();
                }

                if (end) {
                    if (fastForwardPos > videoView.getDuration())
                        fastForwardPos = videoView.getDuration();
                    videoView.seekTo(fastForwardPos);
                    fastForwardPos = 0;
                } else {
                    fastForwardPos += 10000;
                }
            }
        });


        mediaController = new VerticalMediaController(VerticalScreenNextLastActivity.this, true, downsubjectName, StageProblemStatus, StageNoteStatus, StageInformationStatus);
        mediaController.setIjkVideoView(videoView);
        mediaController.setAnchorView(videoView);
        mediaController.setInstantSeeking(false);
        videoView.setMediaController(mediaController);
        // 设置切屏事件
        mediaController
                .setOnBoardChangeListener(new VerticalMediaController.OnBoardChangeListener() {

                    @Override
                    public void onPortrait() {
                        changeToLandscape();
                    }

                    @Override
                    public void onLandscape() {
                        changeToPortrait();
                    }
                });

//        screenListener = new ScreenListener(this);
//        screenListener.begin(new ScreenListener.ScreenStateListener() {
//            @Override
//            public void onScreenOn() {
//                 System.out.println("打开屏幕");
//                videoView.pause();
//            }
//
//            @Override
//            public void onScreenOff() {
//                 System.out.println("屏幕关闭");
//                videoView.pause();
//            }
//
//            @Override
//            public void onUserPresent() {
//                 System.out.println("解锁");
//                videoView.pause();
//            }
//        });

        //新增弹幕跑马灯显示
        SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        if (null != LoginPre && !"".equals(LoginPre)) {
            SID = LoginPre.getInt("S_ID", 0);
            UserTel = LoginPre.getString("S_Telephone", "");
            S_RealName =   LoginPre.getString("S_RealName", "");
        }
        barrageView = (BarrageView) findViewById(R.id.barrageView);
        List<String> contens = new ArrayList<>();
        contens.add(S_RealName + " (" + UserTel + ") ,您好,欢迎您");
        barrageView.setBarrageContents(contens);
        //开启弹幕
        barrageView.startBarraging();

    }

    private void initData() {
        GetListTask getListTask = new GetListTask();
        getListTask.execute();
    }

    class GetListTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            String UserID = null;
            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                int SID = LoginPre.getInt("S_ID", 0);
                UserID = Integer.toString(SID);
            }
            //123898
            //UserID
            quesListBean = httpUtils.getQuesListBeanlist(Const.URL + Const.QuesListAPI, Const.CLIENT, Const.VERSION, Const.APPNAME, UserID, VPKID, "3", NotesPagerstart, PagerSize);
            notesListBean = httpUtils.getNotesListBeanlist(Const.URL + Const.NotesListAPI, Const.CLIENT, Const.VERSION, Const.APPNAME, UserID, "0", "3", VPKID, NotesPagerstart, PagerSize);
//            quesListBean = httpUtils.getQuesListBeanlist(Const.URL + Const.QuesListAPI, Const.CLIENT, Const.VERSION, Const.APPNAME, "123898", "84", "3", NotesPagerstart, PagerSize);
//            notesListBean = httpUtils.getNotesListBeanlist(Const.URL + Const.NotesListAPI, Const.CLIENT, Const.VERSION, Const.APPNAME, "123898", "0", "3", "84", NotesPagerstart, PagerSize);
            return null;
        }

//        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if ("100".equals(quesListBean.getCode())) {
                if (quesListBean.getBody().getQuesList() != null) {
                    quesArrayList =  new ArrayList<>();
                    quesArrayList.add(quesListBean);
                    wenDaAdapter = new WenDaAdapter(VerticalScreenNextLastActivity.this, quesArrayList);
                    wendaListView.setAdapter(wenDaAdapter);

                }

            }
            if ("100".equals(notesListBean.getCode())) {
                if (notesListBean.getBody().getNotesList() != null) {
                    notesArrayList =  new ArrayList<>();
                    notesArrayList.add(notesListBean);
                    bijiAdapter = new BiJiAdapter(VerticalScreenNextLastActivity.this, notesArrayList);
                    bijiListView.setAdapter(bijiAdapter);
                }

            }

        }

    }


    public void initPlayMode(int MplayMode) {
        PlayMode playMode = PlayMode.getPlayMode(MplayMode);
        switch (playMode) {
            case landScape:
                changeToLandscape();
                break;

            case portrait:
                changeToPortrait();
                break;
        }

        rl.setVideoView(videoView);
        ScreenTool.setHideStatusBarListener(this, 2000);
    }

    public void initPlay(int MplayMode, int MplayType, String vid) {
        PlayMode playMode = PlayMode.getPlayMode(MplayMode);
        PlayType playType = PlayType.getPlayType(MplayType);

        switch (playMode) {
            case landScape:
                changeToLandscape();
                break;

            case portrait:
                changeToPortrait();
                break;
        }

        switch (playType) {
            case vid:
                videoView.setVid(vid);
//                System.out.println("setVid === ");
                break;
            case url:
                progressBar.setVisibility(View.GONE);
                videoView.setVideoPath(vid);
                break;
        }


        rl.setVideoView(videoView);
        ScreenTool.setHideStatusBarListener(this, 2000);
    }

    /**
     * 切换到横屏
     */
    public void changeToLandscape() {
        VerticalMediaController.topbar_bg.setVisibility(View.VISIBLE);
        VerticalMediaController.xhl_btn.setVisibility(View.VISIBLE);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //隐藏状态栏
        topbar.setVisibility(View.GONE);
        jianjie_relative.setVisibility(View.GONE);
        bottom_bar.setVisibility(View.GONE);
        bottom_tv.setVisibility(View.GONE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        // 由于切换到横屏获取到的宽高可能和竖屏的不一样，所以需要重新获取宽高
        int[] wh = ScreenTool.getNormalWH(this);
//        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(wh[0],
//                wh[1]);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(-1,
                -1);
        rl.setLayoutParams(p);
//        stopPosition = videoView.getCurrentPosition();
    }

    /**
     * 切换到竖屏
     */
    public void changeToPortrait() {
        VerticalMediaController.topbar_bg.setVisibility(View.GONE);
        VerticalMediaController.xhl_btn.setVisibility(View.GONE);
        getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN); //显示状态栏
        topbar.setVisibility(View.VISIBLE);
        jianjie_relative.setVisibility(View.VISIBLE);
        bottom_bar.setVisibility(View.VISIBLE);
        bottom_tv.setVisibility(View.VISIBLE);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(w,
                adjusted_h);
        rl.setLayoutParams(p);
//        stopPosition = videoView.getCurrentPosition();
    }

    // 配置文件设置congfigchange 切屏调用一次该方法，hide()之后再次show才会出现在正确位置
    @Override
    public void onConfigurationChanged(Configuration arg0) {
        super.onConfigurationChanged(arg0);
        videoView.setVideoLayout(IjkVideoView.VIDEO_LAYOUT_SCALE);
        mediaController.hide();
        // 隐藏或显示状态栏
//        ScreenTool.reSetStatusBar(this);
    }

    /**
     * 播放类型
     *
     * @author TanQu
     */
    public enum PlayType {
        /**
         * 使用vid播放
         */
        vid(1),
        /**
         * 使用url播放
         */
        url(2);
        private final int code;

        private PlayType(int code) {
            this.code = code;
        }

        /**
         * 取得类型对应的code
         *
         * @return
         */
        public int getCode() {
            return code;
        }

        public static PlayType getPlayType(int code) {
            switch (code) {
                case 1:
                    return vid;
                case 2:
                    return url;
            }

            return null;
        }
    }

    /**
     * 播放模式
     */
    public enum PlayMode {
        /**
         * 横屏
         */
        landScape(3),
        /**
         * 竖屏
         */
        portrait(4);

        private final int code;

        private PlayMode(int code) {
            this.code = code;
        }

        /**
         * 取得类型对应的code
         */
        public int getCode() {
            return code;
        }

        public static PlayMode getPlayMode(int code) {
            switch (code) {
                case 3:
                    return landScape;
                case 4:
                    return portrait;
            }

            return null;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        boolean value = mediaController.dispatchKeyEvent(event);
        if (value)
            return true;
        // 播放完成，上传学习进度
        int VideoTime = videoView.getCurrentPosition();
        if (httpUtils.isNetworkConnected(VerticalScreenNextLastActivity.this)) {
            AddVideoTask addVideoLog = new AddVideoTask();
            addVideoLog.execute(VideoTime);
        }

//        if (keyCode == KeyEvent.KEYCODE_BACK
//                && event.getAction() == KeyEvent.ACTION_DOWN) {
//            if (2 == VerticalScreenActivity.this.getResources().getConfiguration().orientation) {
//                changeToPortrait();
//            } else {
//                VerticalScreenActivity.this.finish();
//            }
//
//            return true;
//        }
        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (videoView != null) {
            videoView.destroy();
//            videoView.release(true);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        barrageView.startBarraging();
        //lv数据
        if (httpUtils.isNetworkConnected(VerticalScreenNextLastActivity.this)) {
            initData();
            initPlayMode(PlayMode.portrait.getCode());
            Log.i("播放器界面---onResume ", "播放器界面---onResume ");
        }

        //锁频回来继续播放
        if (videoView != null && videoView.isPausState() && isPlay) {
            videoView.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (videoView != null) {
            videoView.destroy();
//            videoView.release(true);
        }
        barrageView.stopBarraging();
    }

    @Override
    protected void onStop() {
        super.onStop();
        barrageView.stopBarraging();
//        if (videoView != null && videoView.isPlayState()) {
//            videoView.pause();
//        }
        //锁频的时候出去暂停播放
        if (videoView != null && videoView.isPlayState()) {
            isPlay = true;
            videoView.pause();
        } else {
            isPlay = false;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.backbtn:
                int VideoTime = videoView.getCurrentPosition();
                // 播放完成，上传学习进度
                if (httpUtils.isNetworkConnected(VerticalScreenNextLastActivity.this)) {
                    AddVideoTask addVideoLog = new AddVideoTask();
                    addVideoLog.execute(VideoTime);
                }
                finish();
                break;

            case R.id.xuexi:
                xuexi_btn.setBackgroundResource(R.drawable.bottom_button_shape_pre);
                wenda_btn.setBackgroundResource(R.drawable.bottom_button_shape);
                biji_btn.setBackgroundResource(R.drawable.bottom_button_shape);
                bottom_bar.setVisibility(View.VISIBLE);
                bijiListView.setVisibility(View.GONE);
                wendaListView.setVisibility(View.GONE);
                break;

            case R.id.wenda:
                xuexi_btn.setBackgroundResource(R.drawable.bottom_button_shape);
                wenda_btn.setBackgroundResource(R.drawable.bottom_button_shape_pre);
                biji_btn.setBackgroundResource(R.drawable.bottom_button_shape);
                bottom_bar.setVisibility(View.GONE);
                bijiListView.setVisibility(View.GONE);
                wendaListView.setVisibility(View.VISIBLE);
                break;

            case R.id.biji:
                xuexi_btn.setBackgroundResource(R.drawable.bottom_button_shape);
                wenda_btn.setBackgroundResource(R.drawable.bottom_button_shape);
                biji_btn.setBackgroundResource(R.drawable.bottom_button_shape_pre);
                bottom_bar.setVisibility(View.GONE);
                bijiListView.setVisibility(View.VISIBLE);
                wendaListView.setVisibility(View.GONE);
                break;

            //添加笔记
            case R.id.imagebtn_biji:
                classId = Integer.parseInt(ClassID);
//                stageId = Integer.parseInt(Level_ParentID);
                stageId = Integer.parseInt(StageID);
                lessonId = Integer.parseInt(Level_PKID);
                videoId = Integer.parseInt(VPKID);

                Log.i("添加笔记 -- ", "" + classId);
                Log.i("添加笔记 -- ", "" + stageId);
                Log.i("添加笔记 -- ", "" + lessonId);
                Log.i("添加笔记 -- ", "" + videoId);

                Intent bijiIntent = new Intent(this, AddNoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("jumpFlag", 0);
                bundle.putInt("classId", classId);
                bundle.putInt("stageId", stageId);
                bundle.putInt("videoId", videoId);
                bundle.putInt("childClassType_LevelID", lessonId);
                bundle.putString("VideoName", VideoName);
                bundle.putInt("videoOrTestFlag", 6);
                bijiIntent.putExtras(bundle);
                startActivity(bijiIntent);
                break;

            //添加问题
            case R.id.imagebtn_wenti:
                classId = Integer.parseInt(ClassID);
                stageId = Integer.parseInt(Level_ParentID);
                lessonId = Integer.parseInt(Level_PKID);
                videoId = Integer.parseInt(VPKID);
                Log.i("classId", "" + classId);
                Log.i("stageId", "" + stageId);
                Log.i("lessonId", "" + lessonId);
                Intent wentiIntent = new Intent(this, AddNoteActivity.class);
                Bundle bundle_w = new Bundle();
                bundle_w.putInt("jumpFlag", 1);
                bundle_w.putInt("classId", classId);
                bundle_w.putInt("childClassTypeId", lessonId);
                bundle_w.putInt("afterType", 4);
                bundle_w.putInt("videoId", videoId);
                bundle_w.putString("VideoName", VideoName);
                bundle_w.putInt("videoOrTestFlag", 6);
                wentiIntent.putExtras(bundle_w);
                startActivity(wentiIntent);
                break;

            case R.id.left_ivBtn:
                if (httpUtils.isNetworkConnected(VerticalScreenNextLastActivity.this)) {
                    Log.i("左边上一课 2--- > ", "" + OnLineBean.getBody().getTopinfo());
                    if (null != OnLineBean.getBody().getTopinfo()) {
                        left_ivBtn.setBackgroundResource(R.drawable.btn_last);
                        //判断上一个或者下一个任务是否是视频
                        String TopInfoType = OnLineBean.getBody().getTopinfo().getKeyType();
                        String VID = OnLineBean.getBody().getTopinfo().getVID();
                        String VPKID = OnLineBean.getBody().getTopinfo().getVPKID();
//                String Level_ParentID  = OnLineBean.getBody().getTopinfo().getLevel_PKID();
//                String Level_PKID  = OnLineBean.getBody().getTopinfo().getVID();
                        String LastLevel_PKID = OnLineBean.getBody().getTopinfo().getLevel_PKID();
                        Log.i("左边上一课 2--- > ", "" + TopInfoType);
                        if ("1".equals(TopInfoType)) {
                            Intent intent = new Intent(VerticalScreenNextLastActivity.this, VerticalScreenActivity.class);
                            finish();
                            Bundle VideoBundle = new Bundle();
                            VideoBundle.putString("VID", VID);
                            VideoBundle.putString("VPKID", VPKID);
                            VideoBundle.putString("ClassID", ClassID);
                            VideoBundle.putString("StageID", StageID);
                            VideoBundle.putString("Level_ParentID", Level_ParentID);
                            VideoBundle.putString("Level_PKID", LastLevel_PKID);

                            /**新增传递的下载参数**/
                            VideoBundle.putString("className", downClassName);
                            VideoBundle.putString("stageName", downStageName);
                            VideoBundle.putString("lessonId", downlessonId);
                            VideoBundle.putString("lessonName", downlessonName);
                            VideoBundle.putString("subjectId", downsubjectId);
                            VideoBundle.putString("subjectName", downsubjectName);
                            VideoBundle.putString("studyKey", studyKey);

                            VideoBundle.putString("StageProblemStatus", StageProblemStatus);
                            VideoBundle.putString("StageNoteStatus", StageNoteStatus);
                            VideoBundle.putString("StageInformationStatus", StageInformationStatus);
                            /**新增下载参数的传递**/

                            intent.putExtras(VideoBundle);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(VerticalScreenNextLastActivity.this, "暂无课程", Toast.LENGTH_SHORT).show();
                            left_ivBtn.setBackgroundResource(R.drawable.btn_last_s);
                        }
                    } else {
                        Toast.makeText(VerticalScreenNextLastActivity.this, "暂无课程", Toast.LENGTH_SHORT).show();
                        left_ivBtn.setBackgroundResource(R.drawable.btn_last_s);
                    }
                }


                break;

            case R.id.right_ivBtn:
                if (httpUtils.isNetworkConnected(VerticalScreenNextLastActivity.this)) {
                    Gson s = new Gson();
                    Log.i("右边下一课 2 --- > ", "" + s.toJson(OnLineBean.getBody().getDowninfo()).toString());
                    if (null != OnLineBean.getBody().getDowninfo()) {
                        right_ivBtn.setBackgroundResource(R.drawable.btn_next);
                        //判断上一个或者下一个任务是否是视频
                        String DownInfoType = OnLineBean.getBody().getDowninfo().getKeyType();
                        String VID = OnLineBean.getBody().getDowninfo().getVID();
                        String VPKID = OnLineBean.getBody().getDowninfo().getVPKID();
                        String NextLevel_PKID = OnLineBean.getBody().getDowninfo().getLevel_PKID();
                        Log.i("右边下一课 2--- > ", "" + DownInfoType);
                        if ("1".equals(DownInfoType)) {
                            Intent intent = new Intent(VerticalScreenNextLastActivity.this, VerticalScreenActivity.class);
//                        Intent intent = getIntent();
                            Bundle VideoBundle = new Bundle();
                            VideoBundle.putString("VID", VID);
                            VideoBundle.putString("VPKID", VPKID);
                            VideoBundle.putString("ClassID", ClassID);
                            VideoBundle.putString("StageID", StageID);
                            VideoBundle.putString("Level_ParentID", Level_ParentID);
                            VideoBundle.putString("Level_PKID", NextLevel_PKID);

                            /**新增传递的下载参数**/
                            VideoBundle.putString("className", downClassName);
                            VideoBundle.putString("stageName", downStageName);
                            VideoBundle.putString("lessonId", downlessonId);
                            VideoBundle.putString("lessonName", downlessonName);
                            VideoBundle.putString("subjectId", downsubjectId);
                            VideoBundle.putString("subjectName", downsubjectName);
                            VideoBundle.putString("studyKey", studyKey);

                            VideoBundle.putString("StageProblemStatus", StageProblemStatus);
                            VideoBundle.putString("StageNoteStatus", StageNoteStatus);
                            VideoBundle.putString("StageInformationStatus", StageInformationStatus);
                            /**新增下载参数的传递**/

                            intent.putExtras(VideoBundle);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(VerticalScreenNextLastActivity.this, "暂无课程", Toast.LENGTH_SHORT).show();
                            right_ivBtn.setBackgroundResource(R.drawable.btn_next_s);
                        }
                    } else {
                        Toast.makeText(VerticalScreenNextLastActivity.this, "暂无课程", Toast.LENGTH_SHORT).show();
                        right_ivBtn.setBackgroundResource(R.drawable.btn_next_s);
                    }
                }


                break;

            case R.id.zsd_down_icon:
                //判断读写权限
                int permission = ActivityCompat.checkSelfPermission(VerticalScreenNextLastActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
                if (permission != PackageManager.PERMISSION_GRANTED) {
                    // 无权限----
                    ActivityCompat.requestPermissions(VerticalScreenNextLastActivity.this, PERMISSIONS_STORAGE, REQUEST_EXTERNAL_STORAGE);
                } else {
                    DownLoad();
                }

                break;
        }
    }

    private void DownLoad() {
        //判断班级数据库
        if (VideoDownloadManager.getInstance().addClassIdTassk(ClassID, downClassName)) {
            //判断阶段数据库
            if (VideoDownloadManager.getInstance().addStageIdTassk(ClassID, StageID, downStageName)) {
                if (!VideoDownloadManager.getInstance().addVideoTask(VID, ClassID, downlessonId, downlessonName, downsubjectId, downsubjectName, StageID, VPKID, "isStudy", studyKey, StageProblemStatus, StageNoteStatus, StageInformationStatus)) {
                    Toast.makeText(VerticalScreenNextLastActivity.this,
                            "已经到达同时最大下载数量5个，请稍后下载", Toast.LENGTH_LONG)
                            .show();
                } else {
                    Log.i("下载队列 Vid ==> ", VID);
                    Log.i("下载队列 ClassId ==> ", ClassID);
                    Log.i("下载队列 ClassName ==> ", downClassName);
                    Log.i("下载队列 StageId ==> ", StageID);
                    Log.i("下载队列 stageName ==> ", downStageName);
                    Log.i("下载队列 lessonId ==> ", downlessonId);
                    Log.i("下载队列 lessonName ==> ", downlessonName);
                    Log.i("下载队列 subjectId ==> ", downsubjectId);
                    Log.i("下载队列 subjectName ==> ", downsubjectName);
                    zsd_down_icon
                            .setBackgroundResource(R.drawable.btn_xiazai_icon);
                    Toast.makeText(VerticalScreenNextLastActivity.this, "成功添加进下载队列",
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }


    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {
        //visible-0，invisible-4，gone-8
        if (View.VISIBLE == wendaListView.getVisibility()) {
            geneWenDaItems();
//            wenDaAdapter.notifyDataSetChanged();
//            onLoad();
            Log.i("wendaListView ", "wendaListView====>");
        }
        if (View.VISIBLE == bijiListView.getVisibility()) {
            geneBiJiItems();
//            bijiAdapter.notifyDataSetChanged();
//            onLoad();
            Log.i("bijiListView ", "bijiListView====>");
        }

    }

    private void onLoad() {
        if (View.VISIBLE == bijiListView.getVisibility()) {
            bijiListView.stopRefresh();
            bijiListView.stopLoadMore();
            bijiListView.setRefreshTime("刚刚");
        }

        if (View.VISIBLE == wendaListView.getVisibility()) {
            wendaListView.stopRefresh();
            wendaListView.stopLoadMore();
            wendaListView.setRefreshTime("刚刚");
        }

    }


    public void geneWenDaItems() {
        // TODO: 2017/6/23
        QusePagerstart = QusePagerstart + 1;
        QuesListTaskMore quesListMore = new QuesListTaskMore();
        quesListMore.execute();
    }

    public void geneBiJiItems() {
        // TODO: 2017/6/23
        NotesPagerstart = NotesPagerstart + 1;
        NotesListTaskMore notesListMore = new NotesListTaskMore();
        notesListMore.execute();
    }


    class QuesListTaskMore extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            String UserID = null;
            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                int SID = LoginPre.getInt("S_ID", 0);
                UserID = Integer.toString(SID);
            }
//            quesListBean = httpUtils.getQuesListBeanlist(Const.URL + Const.QuesListAPI, Const.CLIENT, Const.VERSION, Const.APPNAME, "123898", "84", "3", NotesPagerstart, PagerSize);
            quesListBean = httpUtils.getQuesListBeanlist(Const.URL + Const.QuesListAPI, Const.CLIENT, Const.VERSION, Const.APPNAME, UserID, VPKID, "3", NotesPagerstart, PagerSize);
            return null;
        }

//        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if ("100".equals(quesListBean.getCode())) {
                if (quesListBean.getBody().getQuesList() != null) {
                    quesArrayList.add(quesListBean);
//                    quesArrayList = quesListBean.getBody().getQuesList();
//                    wenDaAdapter = new WenDaAdapter(VerticalScreenActivity.this, quesArrayList);
//                    wendaListView.setAdapter(wenDaAdapter);
                    wenDaAdapter.notifyDataSetChanged();
                    onLoad();
                } else {
                    onLoad();
                }

            }

        }

    }

    class NotesListTaskMore extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            String UserID = null;
            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                int SID = LoginPre.getInt("S_ID", 0);
                UserID = Integer.toString(SID);
            }
//            notesListBean = httpUtils.getNotesListBeanlist(Const.URL + Const.NotesListAPI, Const.CLIENT, Const.VERSION, Const.APPNAME, "123898", "0", "3", "84", NotesPagerstart, PagerSize);
            notesListBean = httpUtils.getNotesListBeanlist(Const.URL + Const.NotesListAPI, Const.CLIENT, Const.VERSION, Const.APPNAME, UserID, "0", "3", VPKID, NotesPagerstart, PagerSize);
            return null;
        }

//        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if ("100".equals(notesListBean.getCode())) {
                if (notesListBean.getBody().getNotesList() != null) {
                    notesArrayList.add(notesListBean);
//                    notesArrayList = notesListBean.getBody().getNotesList();
//                    bijiAdapter = new BiJiAdapter(VerticalScreenActivity.this, notesArrayList);
//                    bijiListView.setAdapter(bijiAdapter);
                    bijiAdapter.notifyDataSetChanged();
                    onLoad();
                } else {
                    onLoad();
                }
            }
        }
    }

    //上传学习时间进度
    class AddVideoTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... arg0) {
            String ViedoTime=  timeParse(arg0[0]);
//            String ViedoTime = Integer.toString(arg0[0]);
            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            int userID = LoginPre.getInt("S_ID", 0);
            Gson s = new Gson();
            Log.i("学习进度", "KeyValue -- " + s.toJson(OnLineBean).toString());
            String KeyValue = OnLineBean.getBody().getInfo().getStudyKey();
            String ViedoID = OnLineBean.getBody().getInfo().getVPKID();
            addvideostudylogBean = httpUtils.addvideostudylog(Const.URL + Const.AddVideoLogAPI
                    , userID, Const.CLIENT, Const.VERSION, Const.APPNAME, KeyValue, ViedoID, ViedoTime);

            return null;
        }

//        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
//        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

    }

    public static Handler changeHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            System.out.println("接收到的 -- " + msg.what);
            switch (msg.what) {
                case 0:
                    if (videoView != null) {
                        videoView.destroy();
                        VerticalAc.finish();
                    }
                    break;
                //添加笔记
                case 1:
                    classId = Integer.parseInt(ClassID);
//                stageId = Integer.parseInt(Level_ParentID);
                    stageId = Integer.parseInt(StageID);
                    lessonId = Integer.parseInt(Level_PKID);
                    videoId = Integer.parseInt(VPKID);

                    Log.i("添加笔记 -- ", "" + classId);
                    Log.i("添加笔记 -- ", "" + stageId);
                    Log.i("添加笔记 -- ", "" + lessonId);
                    Log.i("添加笔记 -- ", "" + videoId);
                    Intent bijiIntent = new Intent(VerticalAc, AddNoteActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("jumpFlag", 0);
                    bundle.putInt("classId", classId);
                    bundle.putInt("stageId", stageId);
                    bundle.putInt("videoId", videoId);
                    bundle.putInt("childClassType_LevelID", lessonId);
                    bundle.putString("VideoName", VideoName);
                    bundle.putInt("videoOrTestFlag", 6);
                    bijiIntent.putExtras(bundle);
                    VerticalAc.startActivity(bijiIntent);
                    break;
                //添加问题
                case 2:
                    classId = Integer.parseInt(ClassID);
                    stageId = Integer.parseInt(Level_ParentID);
                    lessonId = Integer.parseInt(Level_PKID);
                    videoId = Integer.parseInt(VPKID);
                    Log.i("classId", "" + classId);
                    Log.i("stageId", "" + stageId);
                    Log.i("lessonId", "" + lessonId);
                    Intent wentiIntent = new Intent(VerticalAc, AddNoteActivity.class);
                    Bundle bundle_w = new Bundle();
                    bundle_w.putInt("jumpFlag", 1);
                    bundle_w.putInt("classId", classId);
                    bundle_w.putInt("childClassTypeId", lessonId);
                    bundle_w.putInt("afterType", 4);
                    bundle_w.putInt("videoId", videoId);
                    bundle_w.putString("VideoName", VideoName);
                    bundle_w.putInt("videoOrTestFlag", 6);
                    wentiIntent.putExtras(bundle_w);
                    VerticalAc.startActivity(wentiIntent);
                    break;

            }
            super.handleMessage(msg);
        }

    };

    public static String timeParse(long duration) {
        String time = "";
        long minute = duration / 60000;
        long seconds = duration % 60000;
        long second = Math.round((float) seconds / 1000);
        if (minute < 10) {
            time += "0";
        }
        time += minute + ":";
        if (second < 10) {
            time += "0";
        }
        time += second;
        return time;
    }
}
