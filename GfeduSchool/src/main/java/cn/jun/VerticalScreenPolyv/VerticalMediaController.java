package cn.jun.VerticalScreenPolyv;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
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
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.SeekBar;
import android.widget.TextView;

import com.easefun.polyvsdk.BitRateEnum;
import com.easefun.polyvsdk.PolyvSDKClient;
import com.easefun.polyvsdk.ijk.IjkBaseMediaController;
import com.easefun.polyvsdk.ijk.IjkUtil;
import com.easefun.polyvsdk.ijk.IjkValidateM3U8VideoReturnType;
import com.easefun.polyvsdk.ijk.IjkVideoView;

import java.util.List;
import java.util.Locale;

import cn.jun.VerticalScreen.VerticalScreenActivity;
import cn.jun.bean.Const;
import cn.jun.bean.StudyEndBean;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;

public class VerticalMediaController extends IjkBaseMediaController {
    private HttpUtils httpUtils = new HttpUtils();
    private StudyEndBean StudyEndBean;
    private static final String TAG = "MediaController";
    private static MediaPlayerControl mPlayer;
    private Activity mContext;
    private PopupWindow mWindow;
    private int mAnimStyle;
    private View mAnchor;
    private View mRoot;
    private static ProgressBar mProgress;
    private static TextView mEndTime;
    private static TextView mCurrentTime;
    private IjkVideoView ijkVideoView;

    private static long mDuration;
    private boolean mShowing;
    private static boolean mDragging;
    private boolean mInstantSeeking = true;
    private static final int sDefaultTimeout = 3000;
    private static final int FADE_OUT = 1;
    private static final int SHOW_PROGRESS = 2;
    private boolean mFromXml = false;
    private ImageButton mPauseButton;
    private ImageButton mFfwdButton;
    private ImageButton mRewButton;
    // private ImageButton mNextButton;
    // private ImageButton mPreButton;
    private AudioManager mAM;
    private boolean isUsePreNext = false;
    private OnBoardChangeListener onBoardChangeListener;
    private OnVideoChangeListener onVideoChangeListener;
    private OnResetViewListener onResetViewListener;
    private OnUpdateStartNow onUpdateStartNow;
    private ImageButton btn_boardChange;
    private ImageButton btn_videoChange;
    private Button selectSRT = null;
    private Button selectBitrate = null;
    private LinearLayout bitrateLinearLayout = null;
    private SparseArray<Button> bitRateBtnArray = null;
    // private PolyvPlayerSRTPopupView sRTPopupView = null;
    private OnPreNextListener onPreNextListener;

    private String mTitle;
    private TextView mtitle;
    private Button backBtn;
    protected AlertDialog mDialog;
    protected AlertDialog dialog;

    public static MediaPlayerControl IjmPlayer;

    // 学过了
    // private ImageView xueguo_iv;
    private int UserPlanId;

    private String KeyValue;
    private String userID;
    // 倍速播放
    // private float videoSpeed = 1.0f;
    // private boolean is15Video = false;
    // private final String speed_05 = "0.5x", speed_1 = " 1x ",
    // speed_12 = "1.2x", speed_15 = "1.5x", speed_2 = " 2x ";
    // private String currentSpeed = speed_1;

    public static FrameLayout topbar_bg;
    //学会了按钮
    public static Button xhl_btn;
    //添加笔记
    private ImageButton imagebtn_biji;
    //添加问题
    private ImageButton imagebtn_wenti;

    // 服务状态(笔记,问题,资料是否解锁)
    public String StageProblemStatus;
    public String StageNoteStatus;
    public String StageInformationStatus;


    public VerticalMediaController(Activity context, AttributeSet attrs) {
        super(context, attrs);
        mRoot = this;
        mFromXml = true;
        initController(context);
    }

    /**
     * 当你不需要实现上一集下一集按钮时，设置isUsePreNext 为false，需要时设为true
     * 并实现setPreNextListener()方法
     *
     * @param context
     * @param isUsePreNext
     */

    public VerticalMediaController(Activity context, boolean isUsePreNext, String title, String StageProblemStatus, String StageNoteStatus, String StageInformationStatus) {
        super(context);
        if (!mFromXml && initController(context))
            initFloatingWindow();
        this.isUsePreNext = isUsePreNext;
        this.mTitle = title;
//        this.KeyValue = KeyValue;
        this.StageProblemStatus = StageProblemStatus;
        this.StageNoteStatus = StageNoteStatus;
        this.StageInformationStatus = StageInformationStatus;


    }

    private boolean initController(Activity context) {
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
        mRoot = ((LayoutInflater) mContext
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(
                R.layout.vertical_ijkmedia_controller, this);
        return mRoot;
    }

    @SuppressLint({"ShowToast", "WrongViewCast"})
    @Override
    protected void initControllerView(View v) {
        imagebtn_biji = (ImageButton) v.findViewById(R.id.imagebtn_biji);
        imagebtn_wenti = (ImageButton) v.findViewById(R.id.imagebtn_wenti);

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

        imagebtn_biji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 1;
//                int currentPage = currPage;
//                msg.obj = currentPage;
                VerticalScreenActivity.changeHandler.sendMessage(msg);
            }
        });
        imagebtn_wenti.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Message msg = new Message();
                msg.what = 2;
//                int currentPage = currPage;
//                msg.obj = currentPage;
                VerticalScreenActivity.changeHandler.sendMessage(msg);
            }
        });
        topbar_bg = (FrameLayout) v.findViewById(R.id.topbar_bg);
        xhl_btn = (Button) v.findViewById(R.id.xhl_btn);
        xhl_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("学会了 =====> ", "学会了 =====>");
                //TODO
                if (httpUtils.isNetworkConnected(mContext)) {
                    StudyEed();
                }


            }
        });
        mPauseButton = (ImageButton) v
                .findViewById(R.id.mediacontroller_play_pause);
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
//        btn_videoChange = (ImageButton) v.findViewById(R.id.landscape);
//        btn_videoChange.setTag("0");
//        btn_videoChange.setOnClickListener(mVideoListener);

        // mPreButton = (ImageButton) v.findViewById(R.id.prev);
        // mNextButton = (ImageButton) v.findViewById(R.id.next);
        // if (isUsePreNext) {
        // mPreButton.setVisibility(View.VISIBLE);
        // mNextButton.setVisibility(View.VISIBLE);
        // }
        //
        // mPreButton.setOnClickListener(mPreListener);
        // mNextButton.setOnClickListener(mNextListener);
        mProgress = (ProgressBar) v.findViewById(R.id.mediacontroller_seekbar);
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
        mCurrentTime = (TextView) v
                .findViewById(R.id.mediacontroller_time_current);

        // sRTPopupView = new PolyvPlayerSRTPopupView(mContext);
        // sRTPopupView.setCallback(new PolyvPlayerSRTPopupView.Callback() {
        //
        // @Override
        // public void onSRTSelected(String key) {
        // boolean value = ijkVideoView.changeSRT(key);
        // if (value == false) {
        // Toast.makeText(mContext, "字幕加载失败", Toast.LENGTH_SHORT);
        // }
        // }
        //
        // @Override
        // public void onPopupViewDismiss() {
        //
        // }
        // });


        // 码率选择功能涉及的控件
        selectBitrate = (Button) mRoot.findViewById(R.id.select_bitrate);
        selectBitrate.setOnClickListener(mSelectBitRate);
        bitrateLinearLayout = (LinearLayout) mRoot
                .findViewById(R.id.bitrate_linear_layout);

        bitRateBtnArray = new SparseArray<Button>();
        Button liuchangBtn = (Button) mRoot.findViewById(R.id.liuchang);
        liuchangBtn.setText(BitRateEnum.liuChang.getName());
        bitRateBtnArray.append(BitRateEnum.liuChang.getNum(), liuchangBtn);

        Button gaoqingBtn = (Button) mRoot.findViewById(R.id.gaoqing);
        gaoqingBtn.setText(BitRateEnum.gaoQing.getName());
        bitRateBtnArray.append(BitRateEnum.gaoQing.getNum(), gaoqingBtn);

        Button chaoqingBtn = (Button) mRoot.findViewById(R.id.chaoqing);
        chaoqingBtn.setText(BitRateEnum.chaoQing.getName());
        bitRateBtnArray.append(BitRateEnum.chaoQing.getNum(), chaoqingBtn);

        mtitle = (TextView) v.findViewById(R.id.title);
        if (!"".equals(mTitle) && null != mTitle) {
            mTitle = mTitle.replaceAll("&nbsp;", " ");
            mtitle.setText(mTitle);
        }

        backBtn = (Button) v.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(mfinishActivity);

        backBtn = (Button) v.findViewById(R.id.backBtn);
        backBtn.setOnClickListener(mfinishActivity);

        // 倍速
        VerticalSpeedButton speedButton = (VerticalSpeedButton) findViewById(R.id.speed);
        speedButton.init(ijkVideoView, onUpdateStartNow);


    }

    public void setOnBoardChangeListener(OnBoardChangeListener l) {
        onBoardChangeListener = l;
    }

    public void setOnVideoChangeListener(OnVideoChangeListener l) {
        onVideoChangeListener = l;
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

    public interface OnVideoChangeListener {
        public void onVideoChange(int layout);
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
        Log.i("isPro --- > ", mContext.getResources().getConfiguration().orientation + "");
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
    private View.OnClickListener mVideoListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getTag().equals("0")) {
                v.setTag("1");
                if (onVideoChangeListener != null)
                    onVideoChangeListener.onVideoChange(0);
            } else if (v.getTag().equals("1")) {
                v.setTag("2");
                if (onVideoChangeListener != null)
                    onVideoChangeListener.onVideoChange(1);
            } else if (v.getTag().equals("2")) {
                v.setTag("3");
                if (onVideoChangeListener != null)
                    onVideoChangeListener.onVideoChange(2);
            } else if (v.getTag().equals("3")) {
                v.setTag("0");
                if (onVideoChangeListener != null)
                    onVideoChangeListener.onVideoChange(3);
            }
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
                Rect anchorRect = new Rect(location[0], location[1],
                        location[0] + mAnchor.getWidth(), location[1]
                        + mAnchor.getHeight());
                mWindow.setWidth(mAnchor.getWidth());
                mWindow.setHeight(mAnchor.getHeight());
                mWindow.setAnimationStyle(mAnimStyle);
                mWindow.showAtLocation(mAnchor, Gravity.NO_GRAVITY, 0,
                        anchorRect.top);
            }
            mShowing = true;
            if (mShownListener != null)
                mShownListener.onShown();
        }
        updatePausePlay();
        mHandler.sendEmptyMessage(SHOW_PROGRESS);

        if (timeout != 0) {
            mHandler.removeMessages(FADE_OUT);
            mHandler.sendMessageDelayed(mHandler.obtainMessage(FADE_OUT),
                    timeout);
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

    // private static long setProgress() {
    // if (mPlayer == null || mDragging)
    // return 0;
    //
    // int position = mPlayer.getCurrentPosition();
    // int duration = mPlayer.getDuration();
    // if (mProgress != null) {
    // if (duration > 0) {
    // long pos = 1000L * position / duration;
    // mProgress.setProgress((int) pos);
    // }
    // int percent = mPlayer.getBufferPercentage();
    // mProgress.setSecondaryProgress(percent * 10);
    // }
    //
    // mDuration = duration;
    // if (mEndTime != null)
    // mEndTime.setText(generateTime(mDuration));
    // if (mCurrentTime != null)
    // mCurrentTime.setText(generateTime(position));
    //
    // return position;
    // }

    private static long setProgress() {
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
        // if (mDanmakuView != null)
        // correctSeekTo(position);

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
            return String.format(Locale.US, "%02d:%02d:%02d", hours, minutes,
                    seconds).toString();
        } else {
            return String.format(Locale.US, "%02d:%02d", minutes, seconds)
                    .toString();
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
        if (event.getRepeatCount() == 0
                && (keyCode == KeyEvent.KEYCODE_HEADSETHOOK
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
            mPauseButton.setBackgroundResource(R.drawable.media_pause);
        } else {
            mPauseButton.setBackgroundResource(R.drawable.media_play);
        }
    }

    private void doPauseResume() {
        if (mPlayer.isPlaying())
            mPlayer.pause();
        else
            mPlayer.start();
        updatePausePlay();
    }

    private View.OnClickListener mfinishActivity = new View.OnClickListener() {
        public void onClick(View v) {
//            Message msg = new Message();
//            msg.what = 0;
            // Z_IjkVideoActicity.finishHandler.sendMessage(msg);
            mContext.finish();

        }
    };




    private SeekBar.OnSeekBarChangeListener mSeekListener = new SeekBar.OnSeekBarChangeListener() {
        public void onStartTrackingTouch(SeekBar bar) {
            mDragging = true;
            show(3600000);
            mHandler.removeMessages(SHOW_PROGRESS);
            if (mInstantSeeking)

                mAM.setStreamMute(AudioManager.STREAM_MUSIC, true);

        }

        public void onProgressChanged(SeekBar bar, int progress,
                                      boolean fromuser) {
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
            int dfNum = PolyvSDKClient.getInstance().getVideoDBService()
                    .getDFNum(vid);
            return dfNum;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if (result == 0)
                return;

            selectBitrate.setText(BitRateEnum.getBitRateName(currBitRate));
            List<BitRateEnum> list = BitRateEnum.getBitRateList(result);
            Button bitRateBtn = null;
            for (BitRateEnum bitRateEnum : list) {
                if (bitRateEnum == BitRateEnum.ziDong)
                    continue;
                bitRateBtn = bitRateBtnArray.get(bitRateEnum.getNum());
                bitRateBtn.setVisibility(View.VISIBLE);
                bitRateBtn.setOnClickListener(new bitRateClientListener(vid,
                        currBitRate, bitRateEnum.getNum()));
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

        public bitRateClientListener(String vid, int currBitRate,
                                     int targetBitRate) {
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
            String bitRateName = BitRateEnum.getBitRate(targetBitRate)
                    .getName();
            int type = IjkUtil.validateM3U8Video(vid, targetBitRate);
            switch (type) {
                case IjkValidateM3U8VideoReturnType.M3U8_CORRECT:
                    builder = new AlertDialog.Builder(mContext);
                    builder.setTitle("提示");
                    builder.setMessage(String.format("%s码率视频已经缓存，是否切换到缓存播放",
                            bitRateName));
                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dialog.dismiss();
                                    hide();
                                    ijkVideoView.switchLevel(targetBitRate);
                                }
                            });

                    builder.setNegativeButton("取消",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dialog.dismiss();
                                }
                            });

                    builder.setCancelable(false);
                    builder.show();
                    break;
                case IjkValidateM3U8VideoReturnType.M3U8_FILE_NOT_FOUND:
                    int currType = IjkUtil.validateM3U8Video(vid, currBitRate);
                    if (currType == IjkValidateM3U8VideoReturnType.M3U8_CORRECT) {
                        builder = new AlertDialog.Builder(mContext);
                        builder.setTitle("提示");
                        builder.setMessage(String.format("%s码率视频没有缓存，是否切换到网络播放",
                                bitRateName));
                        builder.setPositiveButton("确定",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
                                        dialog.dismiss();
                                        hide();
                                        ijkVideoView.switchLevel(targetBitRate);
                                    }
                                });

                        builder.setNegativeButton("取消",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog,
                                                        int whichButton) {
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
                    builder.setMessage(String.format("%s码率视频本地缓存损坏，请重新缓存后再播放",
                            bitRateName));
                    builder.setPositiveButton("确定",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int whichButton) {
                                    dialog.dismiss();
                                    mContext.finish();
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

    public static int MedoaPlayCurrentPosition() {
        int pos = mPlayer.getCurrentPosition();
        // pos -= 5000; // milliseconds
        // mPlayer.seekTo(pos);
        // setProgress();
        return pos;

    }

    public static int MedoaPlayDuration() {
        int pos = mPlayer.getDuration();
        return pos;

    }

    public static void SpeedReduce(int pos) {
        pos -= 5000; // milliseconds
        mPlayer.seekTo(pos);
        setProgress();
    }

    public static void SpeedAdd(int pos) {
        pos += 5000; // milliseconds
        mPlayer.seekTo(pos);
        setProgress();
    }

    public static void RecordPlay(int pos) {
        mPlayer.seekTo(pos);
        setProgress();
    }


    public void StudyEed() {
        StudyEed endTask = new StudyEed();
        endTask.execute();
    }


    class StudyEed extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            SharedPreferences Preferences = mContext.getSharedPreferences("xgl_StudyKey",
                    Activity.MODE_PRIVATE);
            String StudyKey = Preferences.getString("StudyKey", "");
            SharedPreferences LoginPre = mContext.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                int SID = LoginPre.getInt("S_ID", 0);
                userID = Integer.toString(SID);
            }
            Log.i("StudyEndBean === ", "" + StudyKey);
            StudyEndBean = httpUtils.getStudyEndBean
                    (Const.URL + Const.StudyEndAPI,
                            Const.CLIENT, Const.VERSION, Const.APPNAME, userID, StudyKey);
            return null;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if ("100".equals(StudyEndBean.getCode())) {
                Message msg = new Message();
                msg.what = 0;
                VerticalScreenActivity.changeHandler.sendMessage(msg);
            }

        }

    }
}
