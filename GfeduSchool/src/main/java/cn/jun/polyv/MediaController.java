package cn.jun.polyv;


import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.easefun.polyvsdk.BitRateEnum;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.ijk.IjkBaseMediaController;
import com.easefun.polyvsdk.ijk.IjkUtil;
import com.easefun.polyvsdk.ijk.IjkValidateM3U8VideoReturnType;
import com.easefun.polyvsdk.ijk.IjkVideoView;
import com.easefun.polyvsdk.screenshot.ActivityTool;
import com.easefun.polyvsdk.screenshot.PolyvScreenshot;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import cn.jun.bean.AddvideostudylogBean;
import cn.jun.bean.Const;
import cn.jun.bean.SqlNextLastBean;
import cn.jun.bean.StudyEndBean;
import cn.jun.menory.service.VideoDownloadManager;
import cn.jun.utils.HttpPostServer;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.note.AddNoteActivity;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MediaController extends IjkBaseMediaController {
    //SqlLastNextBean
    private SqlNextLastBean SqlNextLastBean;
    private List<SqlNextLastBean> SqlList = new ArrayList<>();
    //工具类
    private HttpUtils httpUtils = new HttpUtils();
    //实体类
    private StudyEndBean StudyEndBean;
    //笔记和问答
    private Button imagebtn_wenti, imagebtn_biji;
    //学过了
    private Button xhl_btn;
    //标题文字
    private Activity activity;
    //left
    private ImageButton last_btn;
    //right
    private ImageButton next_btn;
    private TextView title;
    private String titleName;
    private String classId;
    private String stageId;
    private String videoId;
    private String studyKey;
    private String vid;
    private String isCache;

    private String StageProblemStatus;
    private String StageNoteStatus;
    private String StageInformationStatus;

    private Button backBtn;
    private static final String TAG = "MediaController";
    private MediaPlayerControl mPlayer;
    private Context mContext;
    private PopupWindow mWindow;
    private int mAnimStyle;
    private View mAnchor;
    private View mRoot;
    private ProgressBar mProgress;
    private TextView mEndTime, mCurrentTime;
    private IjkVideoView ijkVideoView;
    private long mDuration;
    private boolean mShowing;
    private boolean mDragging;
    private boolean mInstantSeeking = true;
    private static final int sDefaultTimeout = 3000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private boolean mFromXml = false;
    private ImageButton mPauseButton;
    private ImageButton mFfwdButton;
    private ImageButton mRewButton;
    private ImageButton mNextButton;
    private ImageButton mPreButton;
    private AudioManager mAM;
    private boolean isUsePreNext = false;
    private OnBoardChangeListener onBoardChangeListener;
    private OnResetViewListener onResetViewListener;
    private OnUpdateStartNow onUpdateStartNow;
    private ImageButton btn_boardChange;
    private Button selectSRT = null;
    private Button selectBitrate = null;
    private LinearLayout bitrateLinearLayout = null;
    private SparseArray<Button> bitRateBtnArray = null;
    //    private PolyvPlayerSRTPopupView sRTPopupView = null;
    private OnPreNextListener onPreNextListener;
    // 弹幕
//    private boolean isShowDanmaku = true;
//    private IDanmakuView mDanmakuView;
//    private Button showDanmaku, showDanmakuDialog;
    // 用于记录每次播放器更新的时间，与下一次更新的时间进行比较，以获取准确的时间
    private int newtime = -1;
    // 用于记录播放器更新的时间，且其大于当前缓冲更新的时间
    private int gttime = -1;

    //记录学习进度时间
    private AddvideostudylogBean addvideostudylogBean;

    public void setNewtime(int newtime) {
        this.newtime = newtime;
    }

//    public void setIDanmakuView(IDanmakuView mDanmakuView) {
//        this.mDanmakuView = mDanmakuView;
//    }

    private int userId;
    //本地存储
    private SharedPreferences sp;
    private Videos videos;
    private ArrayList<Videos> mVideos;


    public MediaController(Context context, AttributeSet attrs) {
        super(context, attrs);
        mRoot = this;
        mFromXml = true;
        initController(context);
    }

    /**
     * 当你不需要实现上一集下一集按钮时，设置isUsePreNext 为false，需要时设为true
     * 并实现setPreNextListener()方法
     */
    public MediaController(Context context, boolean isUsePreNext, Activity activity,
                           String titleName,
                           String classId,
                           String stageId,
                           String videoId,
                           String studyKey,
                           String vid,
                           String isCache,
                           String StageProblemStatus,
                           String StageNoteStatus,
                           String StageInformationStatus) {
        super(context);
        if (!mFromXml && initController(context))
            initFloatingWindow();
        this.isUsePreNext = isUsePreNext;
        this.activity = activity;
        this.titleName = titleName;
        this.classId = classId;
        this.stageId = stageId;
        this.videoId = videoId;
        this.studyKey = studyKey;
        this.vid = vid;
        this.isCache = isCache;
        this.StageProblemStatus = StageProblemStatus;
        this.StageNoteStatus = StageNoteStatus;
        this.StageInformationStatus = StageInformationStatus;
    }

    private boolean initController(Context context) {
        mContext = context;
        mAM = (AudioManager) mContext.getSystemService(Context.AUDIO_SERVICE);
        return true;
    }

    @Override
    public void onFinishInflate() {
        if (mRoot != null)
            initControllerView(mRoot);
    }

    private void initFloatingWindow() {
        mWindow = new PopupWindow(mContext);
        mWindow.setFocusable(false);
        mWindow.setTouchable(true);
        mWindow.setBackgroundDrawable(null);
        mWindow.setOutsideTouchable(true);
        mAnimStyle = android.R.style.Animation;
    }

    /**
     * Set the view that acts as the anchor for the control view. This can for
     * example be a VideoView, or your Activity's main view.
     *
     * @param view The view to which to anchor the controller when it is visible.
     */
    @Override
    public void setAnchorView(View view) {
        mAnchor = view;
        if (!mFromXml) {
            removeAllViews();
            mRoot = makeControllerView();
            mWindow.setContentView(mRoot);
            mWindow.setWidth(mAnchor.getWidth());
            mWindow.setHeight(mAnchor.getHeight());
        }
        initControllerView(mRoot);
    }

    @Override
    protected View makeControllerView() {
        mRoot = ((LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.ijkmedia_controller, this);
        return mRoot;
    }

    @SuppressLint("ShowToast")
    @Override
    protected void initControllerView(View v) {
        xhl_btn = (Button) v.findViewById(R.id.xhl_btn);
        xhl_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("学会了 =====> ", "学会了 =====>");
                //TODO
                if (httpUtils.isNetworkConnected(mContext)) {
                    StudyEed();
                    SaveVideosTime();
                }
            }
        });

        Log.i("StageProblemStatus ==> ", "" + StageProblemStatus);
        Log.i("StageNoteStatus ==> ", "" + StageNoteStatus);
        imagebtn_wenti = (Button) v.findViewById(R.id.imagebtn_wenti);
        imagebtn_biji = (Button) v.findViewById(R.id.imagebtn_biji);

        if ("1".equals(StageProblemStatus)) {//笔记服务开放
            imagebtn_biji.setVisibility(View.VISIBLE);
        } else if ("0".equals(StageProblemStatus)) {//笔记服务关闭
            imagebtn_biji.setVisibility(View.GONE);
        }
        if ("1".equals(StageNoteStatus)) {//问答服务开放
            imagebtn_wenti.setVisibility(View.VISIBLE);
        } else if ("0".equals(StageNoteStatus)) {//问答服务关闭
            imagebtn_wenti.setVisibility(View.GONE);
        }

        imagebtn_wenti.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("问题  =====> ", "问题 =====>");
                int classId_i = Integer.parseInt(classId);
                int stageId_i = Integer.parseInt(stageId);
                int videoId_i = Integer.parseInt(videoId);
                Intent wentiIntent = new Intent(activity, AddNoteActivity.class);
                Bundle bundle_w = new Bundle();
                bundle_w.putInt("jumpFlag", 1);
                bundle_w.putInt("classId", classId_i);
                bundle_w.putInt("childClassTypeId", stageId_i);
                bundle_w.putInt("afterType", 4);
                bundle_w.putInt("videoId", videoId_i);
                bundle_w.putString("VideoName", titleName);
                bundle_w.putInt("isOnline", 6);
                wentiIntent.putExtras(bundle_w);
                activity.startActivity(wentiIntent);
            }
        });
        imagebtn_biji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("笔记  =====> ", "笔记 =====>");
                int classId_i = Integer.parseInt(classId);
                int stageId_i = Integer.parseInt(stageId);
                int videoId_i = Integer.parseInt(videoId);
                Intent bijiIntent = new Intent(activity, AddNoteActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("jumpFlag", 0);
                bundle.putInt("classId", classId_i);
                bundle.putInt("stageId", stageId_i);
                bundle.putInt("videoId", videoId_i);
                bundle.putInt("isOnline", 6);
                bundle.putString("VideoName", titleName);
                bijiIntent.putExtras(bundle);
                activity.startActivity(bijiIntent);
            }
        });


        last_btn = (ImageButton) v.findViewById(R.id.last_btn);
        next_btn = (ImageButton) v.findViewById(R.id.next_btn);
        last_btn.setOnClickListener(new OnClickListener() {//上一个
            @Override
            public void onClick(View v) {
                Log.i("控制台---- vid ", "" + vid);
                Log.i("控制台---- isCache ", "" + isCache);
                if (!"".equals(vid) && null != vid) {
                    int vid_id = VideoDownloadManager.getInstance().SelectBufferedVideos_id(vid);
                    if (0 != vid_id) {
                        SqlNextLastBean = VideoDownloadManager.getInstance().getSqlNextLastVideos(vid_id, "last");
                        if (null != SqlNextLastBean && !"".equals(SqlNextLastBean)) {
                            if (!"".equals(SqlNextLastBean.vid) && null != SqlNextLastBean.vid) {
                                activity.finish();
                                IjkVideoActicity.intentTo(activity, IjkVideoActicity.PlayMode.landScape, IjkVideoActicity.PlayType.vid, SqlNextLastBean.vid,
                                        true, SqlNextLastBean.subjectname, SqlNextLastBean.classid, SqlNextLastBean.stageid, SqlNextLastBean.videoid, SqlNextLastBean.isStudy, SqlNextLastBean.vid, "1", StageProblemStatus, StageProblemStatus, StageInformationStatus);
                            }
                        } else {
                            Toast.makeText(activity, "没有更多视频了", Toast.LENGTH_SHORT).show();
                        }

                    }
                }


            }
        });
        next_btn.setOnClickListener(new OnClickListener() {//下一个
            @Override
            public void onClick(View v) {
                Log.i("控制台---- vid ", "" + vid);
                Log.i("控制台---- isCache ", "" + isCache);
                if (!"".equals(vid) && null != vid) {
                    int vid_id = VideoDownloadManager.getInstance().SelectBufferedVideos_id(vid);
                    if (0 != vid_id) {
                        SqlNextLastBean = VideoDownloadManager.getInstance().getSqlNextLastVideos(vid_id, "next");
                        if (null != SqlNextLastBean && !"".equals(SqlNextLastBean)) {
                            if (!"".equals(SqlNextLastBean.vid) && null != SqlNextLastBean.vid) {
                                activity.finish();
                                IjkVideoActicity.intentTo(activity, IjkVideoActicity.PlayMode.landScape, IjkVideoActicity.PlayType.vid, SqlNextLastBean.vid,
                                        true, SqlNextLastBean.subjectname, SqlNextLastBean.classid, SqlNextLastBean.stageid, SqlNextLastBean.videoid, SqlNextLastBean.isStudy, SqlNextLastBean.vid, "1", StageProblemStatus, StageProblemStatus, StageInformationStatus);
                            }
                        } else {
                            Toast.makeText(activity, "没有更多视频了", Toast.LENGTH_SHORT).show();
                        }

                    }
                }
            }
        });
        title = (TextView) v.findViewById(R.id.title);
        title.setText(titleName);
        //返回按钮
        backBtn = (Button) v.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                SaveVideosTime();

            }
        });
        mPauseButton = (ImageButton) v.findViewById(R.id.mediacontroller_play_pause);
        if (mPauseButton != null) {
            mPauseButton.requestFocus();
            mPauseButton.setOnClickListener(mPauseListener);
        }

        mFfwdButton = (ImageButton) v.findViewById(R.id.ffwd);
        mFfwdButton.setOnClickListener(mFfwdListener);

        mRewButton = (ImageButton) v.findViewById(R.id.rew);
        mRewButton.setOnClickListener(mRewListener);
        btn_boardChange = (ImageButton) v.findViewById(R.id.landscape);
        btn_boardChange.setOnClickListener(mBoardListener);

        mPreButton = (ImageButton) v.findViewById(R.id.prev);
        mNextButton = (ImageButton) v.findViewById(R.id.next);
        if (isUsePreNext) {
            mPreButton.setVisibility(View.VISIBLE);
            mNextButton.setVisibility(View.VISIBLE);
        }

        mPreButton.setOnClickListener(mPreListener);
        mNextButton.setOnClickListener(mNextListener);
        mProgress = (SeekBar) v.findViewById(R.id.mediacontroller_seekbar);
        if (mProgress != null) {
            if (mProgress instanceof SeekBar) {
                SeekBar seeker = (SeekBar) mProgress;
                seeker.setOnSeekBarChangeListener(mSeekListener);
                seeker.setClickable(false);
                seeker.setThumbOffset(1);
            }

            mProgress.setMax(1000);
        }

        mEndTime = (TextView) v.findViewById(R.id.mediacontroller_time_total);
        mCurrentTime = (TextView) v.findViewById(R.id.mediacontroller_time_current);


        // 码率选择功能涉及的控件
        selectBitrate = (Button) mRoot.findViewById(R.id.select_bitrate);
        selectBitrate.setOnClickListener(mSelectBitRate);
        bitrateLinearLayout = (LinearLayout) mRoot.findViewById(R.id.bitrate_linear_layout);

        bitRateBtnArray = new SparseArray<Button>();
        Button zidongBtn = (Button) mRoot.findViewById(R.id.zidong);
        zidongBtn.setText(BitRateEnum.ziDong.getName());
        bitRateBtnArray.append(BitRateEnum.ziDong.getNum(), zidongBtn);

        Button liuchangBtn = (Button) mRoot.findViewById(R.id.liuchang);
        liuchangBtn.setText(BitRateEnum.liuChang.getName());
        bitRateBtnArray.append(BitRateEnum.liuChang.getNum(), liuchangBtn);

        Button gaoqingBtn = (Button) mRoot.findViewById(R.id.gaoqing);
        gaoqingBtn.setText(BitRateEnum.gaoQing.getName());
        bitRateBtnArray.append(BitRateEnum.gaoQing.getNum(), gaoqingBtn);

        Button chaoqingBtn = (Button) mRoot.findViewById(R.id.chaoqing);
        chaoqingBtn.setText(BitRateEnum.chaoQing.getName());
        bitRateBtnArray.append(BitRateEnum.chaoQing.getNum(), chaoqingBtn);

        // 倍速
        SpeedButton speedButton = (SpeedButton) findViewById(R.id.speed);
        speedButton.init(ijkVideoView, onUpdateStartNow);
        // 截图
        Button screenShot = (Button) findViewById(R.id.screenshot);
        final PolyvScreenshot polyvScreenshot = new PolyvScreenshot();
        polyvScreenshot.setScreenshotListener(new PolyvScreenshot.ScreenshotListener() {

            @Override
            public void success(String filepath) {
                ActivityTool.toastMsg(mContext, "截图成功：" + filepath);
            }

            @Override
            public void fail(int category) {
                switch (category) {
                    case PolyvScreenshot.CREATE_FILE_FAIL:
                        ActivityTool.toastMsg(mContext, "截图失败：文件创建失败");
                        break;
                    case PolyvScreenshot.NETWORK_EXCEPTION:
                        ActivityTool.toastMsg(mContext, "截图失败：网络异常");
                        break;
                    case PolyvScreenshot.RESPONSE_FAIL:
                        ActivityTool.toastMsg(mContext, "截图失败：当前时间无法截图");
                        break;
                    case PolyvScreenshot.STORAGE_NOT_ENOUGH:
                        ActivityTool.toastMsg(mContext, "截图失败：内存不足");
                        break;
                    case PolyvScreenshot.GETVIDEO_FAIL:
                        ActivityTool.toastMsg(mContext, "截图失败：无法获取video对象");
                        break;
                    case PolyvScreenshot.DATA_EXCEPTION:
                        ActivityTool.toastMsg(mContext, "截图失败：数据异常");
                        break;
                }
            }
        });
        screenShot.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // 判断有没有写入sd卡的权限
                if (ContextCompat.checkSelfPermission(mContext,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityTool.toastMsg(mContext, "没有写入sd卡文件的权限");
                    return;
                }
                // 设置保存路径，没有设置时将使用默认的保存路径
                // polyvScreenshot.setSavePath(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test");
                polyvScreenshot.screenshot(ijkVideoView, mContext, true);
            }
        });
    }

    public void setOnBoardChangeListener(OnBoardChangeListener l) {
        onBoardChangeListener = l;
    }

    public void setOnResetViewListener(OnResetViewListener l) {
        onResetViewListener = l;
    }

    public void setOnUpdateStartNow(OnUpdateStartNow l) {
        onUpdateStartNow = l;
    }

    public void setOnPreNextListener(OnPreNextListener l) {
        onPreNextListener = l;
    }

    public interface OnBoardChangeListener {
        public void onLandscape();

        public void onPortrait();
    }

    public interface OnPreNextListener {
        public void onPreviou();

        public void onNext();
    }

    public interface OnResetViewListener {
        public void onReset();
    }

    public interface OnUpdateStartNow {
        public void onUpdate(boolean startNow);
    }

    private boolean isScreenPortrait() {
        return mContext.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    private View.OnClickListener mPreListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            if (onPreNextListener != null)
                onPreNextListener.onPreviou();
        }
    };

    private View.OnClickListener mNextListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            if (onPreNextListener != null)
                onPreNextListener.onNext();
        }
    };
    private View.OnClickListener mBoardListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (isScreenPortrait()) {//
                if (onBoardChangeListener != null)
                    onBoardChangeListener.onPortrait();
            } else {
                if (onBoardChangeListener != null)
                    onBoardChangeListener.onLandscape();
            }

        }
    };

    private View.OnClickListener mFfwdListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            Log.v("MediaController", "into the mFfw button");
            int pos = mPlayer.getCurrentPosition();
            pos += 5000; // milliseconds
            mPlayer.seekTo(pos);
            setProgress();

            show(sDefaultTimeout);
        }
    };
    private View.OnClickListener mRewListener = new View.OnClickListener() {

        @Override
        public void onClick(View arg0) {
            Log.v("MediaController", "into the mRew button");
            int pos = mPlayer.getCurrentPosition();
            pos -= 5000; // milliseconds
            mPlayer.seekTo(pos);
            setProgress();
            show(sDefaultTimeout);
        }
    };

    private View.OnClickListener mSelectBitRate = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (bitrateLinearLayout.getVisibility() == View.INVISIBLE) {
                bitrateLinearLayout.setVisibility(View.VISIBLE);
            } else {
                bitrateLinearLayout.setVisibility(View.INVISIBLE);
            }
        }
    };

    @Override
    public void setMediaPlayer(MediaPlayerControl player) {
        mPlayer = player;
        updatePausePlay();
    }

    public void setInstantSeeking(boolean seekWhenDragging) {
        mInstantSeeking = seekWhenDragging;
    }

    @Override
    public void show() {
        show(sDefaultTimeout);
    }

    private void disableUnsupportedButtons() {
        try {
            if (mPauseButton != null && !mPlayer.canPause())
                mPauseButton.setEnabled(false);
        } catch (IncompatibleClassChangeError ex) {
        }
    }

    /**
     * Change the animation style resource for this controller. If the
     * controller is showing, calling this method will take effect only the next
     * time the controller is shown.
     *
     * @param animationStyle animation style to use when the controller appears and
     *                       disappears. Set to -1 for the default animation, 0 for no
     *                       animation, or a resource identifier for an explicit animation.
     */
    public void setAnimationStyle(int animationStyle) {
        mAnimStyle = animationStyle;
    }

    /**
     * Show the controller on screen. It will go away automatically after
     * 'timeout' milliseconds of inactivity.
     *
     * @param timeout The timeout in milliseconds. Use 0 to show the controller
     *                until hide() is called.
     */
    @Override
    public void show(int timeout) {
        if (mIsCanShow == false)
            return;
        if (!mShowing && mAnchor != null && mAnchor.getWindowToken() != null) {
            if (mPauseButton != null)
                mPauseButton.requestFocus();
            disableUnsupportedButtons();

            if (mFromXml) {
                setVisibility(View.VISIBLE);
            } else {
                int[] location = new int[2];

                mAnchor.getLocationInWindow(location);
                Rect anchorRect = new Rect(location[0], location[1], location[0] + mAnchor.getWidth(),
                        location[1] + mAnchor.getHeight());
                mWindow.setWidth(mAnchor.getWidth());
                mWindow.setHeight(mAnchor.getHeight());
                mWindow.setAnimationStyle(mAnimStyle);
                mWindow.showAtLocation(mAnchor, Gravity.NO_GRAVITY, 0, anchorRect.top);
            }
            mShowing = true;
            if (mShownListener != null)
                mShownListener.onShown();
        }
        updatePausePlay();
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(FADE_OUT), timeout);
        }
    }

    public boolean isShowing() {
        return mShowing;
    }

    public void hide() {
        if (mAnchor == null)
            return;
        if (mShowing) {
            bitrateLinearLayout.setVisibility(View.INVISIBLE);
            try {
                mHandler.removeMessages(SHOW_PROGRESS);
                if (mFromXml)
                    setVisibility(View.GONE);
                else
                    mWindow.dismiss();
            } catch (IllegalArgumentException ex) {
                Log.d(TAG, "MediaController already removed");
            }
            mShowing = false;
            if (mHiddenListener != null)
                mHiddenListener.onHidden();
        }
    }

    public void toggleVisiblity() {
        if (isShowing()) {
            hide();
        } else {
            show();
        }
    }

    public interface OnShownListener {
        public void onShown();
    }

    private OnShownListener mShownListener;

    public void setOnShownListener(OnShownListener l) {
        mShownListener = l;
    }

    public interface OnHiddenListener {
        public void onHidden();
    }

    private OnHiddenListener mHiddenListener;

    public void setOnHiddenListener(OnHiddenListener l) {
        mHiddenListener = l;
    }

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            long pos;
            switch (msg.what) {
                case FADE_OUT:
                    hide();
                    break;
                case SHOW_PROGRESS:
                    pos = setProgress();
                    if (!mDragging && mShowing) {
                        msg = obtainMessage(SHOW_PROGRESS);
                        sendMessageDelayed(msg, 1000 - (pos % 1000));
                        updatePausePlay();
                    }
                    break;
            }
        }
    };

    private long setProgress() {
        if (mPlayer == null || mDragging)
            return 0;

        int position = mPlayer.getCurrentPosition();
        int duration = mPlayer.getDuration();
        if (mProgress != null) {
            if (duration > 0) {
                long pos = 1000L * position / duration;
                mProgress.setProgress((int) pos);
            }
            int percent = mPlayer.getBufferPercentage();
            mProgress.setSecondaryProgress(percent * 10);
        }

        mDuration = duration;
        if (mEndTime != null)
            mEndTime.setText(generateTime(mDuration));
        if (mCurrentTime != null)
            mCurrentTime.setText(generateTime(position));
        // 获取播放器稳定的时间后再seekTo
//        if (mDanmakuView != null)
//            correctSeekTo(position);

        return position;
    }

    /**
     * 设置进度为最大，因为播放器的当前时间点不准确，在最后总是差一两秒， 因此在视频播放完后调用此方法来设置进度。
     */
    public void setProgressMax() {
        if (mProgress != null) {
            mProgress.setProgress(mProgress.getMax());
        }

        mDuration = mPlayer.getDuration();
        if (mEndTime != null) {
            mEndTime.setText(generateTime(mDuration));
        }

        if (mCurrentTime != null) {
            mCurrentTime.setText(generateTime(mDuration));
        }
    }

    private static String generateTime(long position) {
        int totalSeconds = (int) (position / 1000);

        int seconds = totalSeconds % 60;
        int minutes = (totalSeconds / 60) % 60;
        int hours = totalSeconds / 3600;

        if (hours > 0) {
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes, seconds).toString();
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds).toString();
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return ijkVideoView.dispatchTouchEvent(event);
    }

    @Override
    public boolean onTrackballEvent(MotionEvent ev) {
        show(sDefaultTimeout);
        return false;
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (mPlayer == null)
            return false;
        if (ijkVideoView == null)
            return false;
        if (ijkVideoView.isPlayStageMain() == false)
            return false;
        int keyCode = event.getKeyCode();
        if (event.getRepeatCount() == 0 && (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
                || keyCode == KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE || keyCode == KeyEvent.KEYCODE_SPACE)) {
            doPauseResume();
            show(sDefaultTimeout);
            if (mPauseButton != null)
                mPauseButton.requestFocus();
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_MEDIA_STOP) {
            if (mPlayer.isPlaying()) {
                mPlayer.pause();
                updatePausePlay();
            }
            return true;
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isShowing()) {
                hide();
                return true;
            }

            if (isScreenPortrait() == false) {
                if (onBoardChangeListener != null)
                    onBoardChangeListener.onLandscape();
                return true;
            }

            return false;
        } else if (keyCode == KeyEvent.KEYCODE_MENU) {
            show(sDefaultTimeout);
        } else {
            show(sDefaultTimeout);
        }
        return super.dispatchKeyEvent(event);
    }

    private View.OnClickListener mPauseListener = new View.OnClickListener() {
        public void onClick(View v) {
            doPauseResume();
            show(sDefaultTimeout);
        }
    };

    private void updatePausePlay() {
        if (mRoot == null || mPauseButton == null)
            return;
        if (mPlayer.isPlaying()) {
            mPauseButton.setImageResource(R.drawable.media_pause);
        } else {
            mPauseButton.setImageResource(R.drawable.media_play);
        }
    }

    private void doPauseResume() {
        if (mPlayer.isPlaying()) {
            mPlayer.pause();
        } else {
            mPlayer.start();
        }
        updatePausePlay();
    }

    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
            mDragging = true;
            show(3600000);
            mHandler.removeMessages(SHOW_PROGRESS);
            if (mInstantSeeking)
                mAM.setStreamMute(AudioManager.STREAM_MUSIC, true);

        }

        public void onProgressChanged(SeekBar bar, int progress, boolean fromuser) {
            if (!fromuser)
                return;

            long newposition = (mDuration * progress) / 1000;
            String time = generateTime(newposition);
            if (mInstantSeeking)
                mPlayer.seekTo(newposition);

            if (mCurrentTime != null)
                mCurrentTime.setText(time);
        }

        public void onStopTrackingTouch(SeekBar bar) {
            if (!mInstantSeeking)
                mPlayer.seekTo((mDuration * bar.getProgress()) / 1000);
            show(sDefaultTimeout);
            mHandler.removeMessages(SHOW_PROGRESS);
            mAM.setStreamMute(AudioManager.STREAM_MUSIC, false);
            mDragging = false;
            mHandler.sendEmptyMessageDelayed(SHOW_PROGRESS, 1000);
        }
    };

    // 把弹幕seekto到播放器的正确进度
    private void correctSeekTo(int position) {
        if (newtime != -1 && position < newtime)
            commomSeekTo(position);
        else if (newtime != -1 && gttime != -1)
            commomSeekTo(position);
        else if (position >= newtime)
            gttime = position;
    }

    private void commomSeekTo(int position) {
        newtime = -1;
        gttime = -1;
    }

    @Override
    public void setEnabled(boolean enabled) {
        if (mPauseButton != null)
            mPauseButton.setEnabled(enabled);
        if (mProgress != null)
            mProgress.setEnabled(enabled);
        disableUnsupportedButtons();
        super.setEnabled(enabled);
    }

    /**
     * 设置IjkVideoView 对象，如果没有设置则在码率按钮切换码率的操作中会报错
     *
     * @param ijkVideoView
     */
    public void setIjkVideoView(IjkVideoView ijkVideoView) {
        this.ijkVideoView = ijkVideoView;
    }

    @Override
    public void setViewBitRate(String vid, int bitRate) {
        new GetDFNumWork().execute(vid, String.valueOf(bitRate));
    }

    /**
     * 取得dfNum 任务
     *
     * @author TanQu 2015-10-8
     */
    private class GetDFNumWork extends AsyncTask<String, String, Integer> {

        private String vid = "";
        private int currBitRate = 0;

        @Override
        protected Integer doInBackground(String... params) {
            vid = params[0];
            currBitRate = Integer.parseInt(params[1]);
            if (currBitRate < 0)
                currBitRate = 0;
            int dfNum = PolyvSDKClient.getInstance().getVideoDBService().getDFNum(vid);
            return dfNum;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 0)
                return;

            selectBitrate.setText(BitRateEnum.getBitRateName(currBitRate));
            Button bitRateBtn = null;
            int minBitRate = BitRateEnum.getMinBitRateFromAll().getNum();
            int maxBitRate = BitRateEnum.getMaxBitRate(result).getNum();
            for (int i = maxBitRate; i >= minBitRate; i--) {
                bitRateBtn = bitRateBtnArray.get(i);
                bitRateBtn.setVisibility(View.VISIBLE);
                bitRateBtn.setOnClickListener(new bitRateClientListener(vid, currBitRate, i));
            }
        }
    }

    /**
     * 码率按钮单击事件监听方法
     *
     * @author TanQu 2015-10-8
     */
    private class bitRateClientListener implements View.OnClickListener {
        private final String vid;
        private final int currBitRate;
        private final int targetBitRate;

        public bitRateClientListener(String vid, int currBitRate, int targetBitRate) {
            this.vid = vid;
            this.currBitRate = currBitRate;
            this.targetBitRate = targetBitRate;
        }

        @Override
        public void onClick(View v) {
            if (currBitRate == targetBitRate) {
                bitrateLinearLayout.setVisibility(View.INVISIBLE);
                return;
            }

            if (onResetViewListener != null) {
                onResetViewListener.onReset();
            }

            if (onUpdateStartNow != null) {
                onUpdateStartNow.onUpdate(true);
            }

            AlertDialog.Builder builder = null;
            String bitRateName = BitRateEnum.getBitRate(targetBitRate).getName();
            int type = IjkUtil.validateM3U8Video(vid, targetBitRate, ijkVideoView.getHlsSpeedType());
            switch (type) {
                case IjkValidateM3U8VideoReturnType.M3U8_CORRECT:
                    builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("提示");
                    builder.setMessage(String.format("%s码率视频已经缓存，是否切换到缓存播放", bitRateName));
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            hide();
                            ijkVideoView.switchLevel(targetBitRate);
                        }
                    });

                    builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                        }
                    });

                    builder.setCancelable(false);
                    builder.show();
                    break;
                case IjkValidateM3U8VideoReturnType.M3U8_FILE_NOT_FOUND:
                    int currType = IjkUtil.validateM3U8Video(vid, currBitRate, ijkVideoView.getHlsSpeedType());
                    if (currType == IjkValidateM3U8VideoReturnType.M3U8_CORRECT) {
                        builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("提示");
                        builder.setMessage(String.format("%s码率视频没有缓存，是否切换到网络播放", bitRateName));
                        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                hide();
                                ijkVideoView.switchLevel(targetBitRate);
                            }
                        });

                        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        });

                        builder.setCancelable(false);
                        builder.show();
                    } else {
                        hide();
                        ijkVideoView.switchLevel(targetBitRate);
                    }

                    break;
                case IjkValidateM3U8VideoReturnType.M3U8_FILE_CONTENT_EMPTY:
                case IjkValidateM3U8VideoReturnType.M3U8_TS_LIST_EMPTY:
                case IjkValidateM3U8VideoReturnType.M3U8_TS_FILE_NOT_FOUND:
                case IjkValidateM3U8VideoReturnType.M3U8_KEY_FILE_NOT_FOUND:
                    builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("提示");
                    builder.setMessage(String.format("%s码率视频本地缓存损坏，请重新缓存后再播放", bitRateName));
                    builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int whichButton) {
                            dialog.dismiss();
                            activity.finish();
                        }
                    });

                    builder.setCancelable(false);
                    builder.show();
                    break;
            }
        }
    }

    /**
     * 是否能显示，有些在显示的时候，本控制条界面是不能弹出的
     */
    private static boolean mIsCanShow = true;

    public static void setCanShow(boolean isCanShow) {
        mIsCanShow = isCanShow;
    }


    public void StudyEed() {
        StudyEed endTask = new StudyEed();
        endTask.execute();
    }

    class StudyEed extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            String userID = null;
            SharedPreferences LoginPre = mContext.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                int SID = LoginPre.getInt("S_ID", 0);
                userID = Integer.toString(SID);
            }
            StudyEndBean = httpUtils.getStudyEndBean
                    (Const.URL + Const.StudyEndAPI,
                            Const.CLIENT, Const.VERSION, Const.APPNAME, userID, studyKey);
            return null;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if ("100".equals(StudyEndBean.getCode())) {
                activity.finish();
            }

        }

    }


    public void SaveVideosTime() {
        SharedPreferences preferDataList = activity.getSharedPreferences(Const.VideoTime_FLAG, activity.MODE_PRIVATE);
        String getjson = preferDataList.getString("videos", null);
        Gson s = new Gson();
        Log.i("SaveVideosTime ===  ", "" + getjson);
        Log.i("VideoTime vid == ", "" + vid);

        if (getjson != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Videos>>() {
            }.getType();
            mVideos = new ArrayList<>();
            mVideos = gson.fromJson(getjson, type);

            Log.i("fromJson ===  ", "" + s.toJson(mVideos).toString());

            int VideoTime = ijkVideoView.getCurrentPosition();
            Log.i("VideoTime == ", "" + VideoTime / 1000);
            videos = new Videos();
            videos.setVideoTime(VideoTime / 1000 + "");
//            videos.setVideoTime(timeParse(VideoTime));
            videos.setVideoID(videoId);
            videos.setKeyValue(studyKey);
            mVideos.add(videos);
            SharedPreferences.Editor editor = activity.getSharedPreferences(Const.VideoTime_FLAG, activity.MODE_PRIVATE).edit();
            Gson VideosGson = new Gson();
            String VideosJson = VideosGson.toJson(mVideos);
            editor.putString("videos", VideosJson);
            editor.commit();

            Log.i("add ===  ", "" + s.toJson(mVideos).toString());
        } else {
            mVideos = new ArrayList<>();
            int VideoTime = ijkVideoView.getCurrentPosition();
            Log.i("VideoTime == ", "" + VideoTime / 1000);
            videos = new Videos();
            videos.setVideoTime(VideoTime / 1000 + "");
//            videos.setVideoTime(timeParse(VideoTime));
            videos.setVideoID(videoId);
            videos.setKeyValue(studyKey);
            mVideos.add(videos);
            SharedPreferences.Editor editor = activity.getSharedPreferences(Const.VideoTime_FLAG, activity.MODE_PRIVATE).edit();
            Gson VideosGson = new Gson();
            String VideosJson = VideosGson.toJson(mVideos);
            editor.putString("videos", VideosJson);
            editor.commit();

            Log.i("two  ===  ", "" + s.toJson(mVideos).toString());
        }

        if (IjkVideoActicity.videoView.isLocalPlay() == false) {

            int VideoTime = IjkVideoActicity.videoView.getCurrentPosition();
            Log.i("在线 == "," === "+VideoTime);
            // 在线播放完成，上传学习进度
            AddVideoTask addVideoLog = new AddVideoTask();
            addVideoLog.execute(VideoTime);
        } else {
            Log.i("本地 == "," === ");
            if (httpUtils.isNetworkConnected(activity)) {
                Log.i("有网络 === 》 ", "上传数据 ==== ");
                initVideoTimeTask();
            }
        }



    }

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


    /**
     * 日期转换成秒数
     */
    public static long getSecondsFromDate(String expireDate) {
        if (expireDate == null || expireDate.trim().equals(""))
            return 0;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Date date = null;
        try {
            date = sdf.parse(expireDate);
            return (long) (date.getTime() / 1000);
        } catch (ParseException e) {
            e.printStackTrace();
            return 0L;
        }
    }

    //上传学习时间进度
    class AddVideoTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... arg0) {
            String ViedoTime = arg0[0] / 1000 + "";

            sp = activity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            userId = sp.getInt("S_ID", 0);

            addvideostudylogBean = httpUtils.addvideostudylog(Const.URL + Const.AddVideoLogAPI
                    , userId, Const.CLIENT, Const.VERSION, Const.APPNAME, studyKey, videoId, ViedoTime);

            return null;
        }

        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

        }

    }


    private void initVideoTimeTask() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostServer httpPostService = retrofit.create(HttpPostServer.class);
        RequestBody body = commParam();
        Observable<CommonBean<VideoTimeLog>> observable;
//        if (IjkVideoActicity.videoView.isLocalPlay() == false) {
//            observable = httpPostService.getVideoTimeLog_live(body);
//        } else {
        observable = httpPostService.getVideoTimeLog(body);
//        }

        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<VideoTimeLog>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
//                        Toast.makeText(activity, "网络异常，请返回重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<VideoTimeLog> videoTimeLog) {
                        if (100 == videoTimeLog.getCode()) {
                            SharedPreferences preferDataList = activity.getSharedPreferences(Const.VideoTime_FLAG, activity.MODE_PRIVATE);
                            SharedPreferences.Editor editor = preferDataList.edit();
                            editor.clear();
                            editor.commit();
                        }
                    }
                });
    }


    //公共参数
    private RequestBody commParam() {
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(activity);
        try {
            sp = activity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            userId = sp.getInt("S_ID", 0);
            // 用户id
            obj.put("userId", userId);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("version", commParam.getVersion());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));

            JSONObject json;
            JSONArray arr = new JSONArray();
            for (int i = 0; i < mVideos.size(); i++) {
                json = new JSONObject();
                json.put("VideoTime", mVideos.get(i).getVideoTime());
                json.put("VideoID", mVideos.get(i).getVideoID());
                json.put("KeyValue", mVideos.get(i).getKeyValue());
                arr.put(json);
            }
            obj.put("Videos", arr);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }


}
