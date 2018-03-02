package cn.jun.live.vod;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import com.gensee.doc.OnDocViewEventListener;
import com.gensee.entity.ChatMsg;
import com.gensee.entity.DocInfo;
import com.gensee.entity.PageInfo;
import com.gensee.media.PlaySpeed;
import com.gensee.media.VODPlayer;
import com.gensee.pdu.IGSDocView;
import com.gensee.utils.GenseeLog;
import com.gensee.utils.StringUtil;
import com.gensee.view.GSDocViewGx;
import com.gensee.view.GSVideoView;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jun.bean.AddVodRecordBean;
import cn.jun.indexmain.interfaces.OnLoadMoreListener;
import cn.jun.utils.HttpPostServer;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static cn.jun.live.vod.VodRoomActivity.QAList;

//点播界面
public class PlayActivity extends Activity implements OnDocViewEventListener,
        VODPlayer.OnVodPlayListener, View.OnClickListener, SeekBar.OnSeekBarChangeListener {

    private static final String TAG = "PlayActivity";
    private VODPlayer mVodPlayer;
    private GSVideoView mGSVideoView;
    //private GSDocView mDocView;
    private GSDocViewGx mGlDocView;
    private SeekBar mSeekBarPlayViedo;
    private Button btnDocList;
    private ListView lvChapterList;
    private ChapterListAdapter chapterListAdapter;

    private Button stopVeidoPlay;
    private Button replyVedioPlay;
    private TextView mNowTimeTextview;
    private TextView mAllTimeTextView;

    private ImageButton mPauseScreenplay;
    private boolean isTouch = false;

    //    private static final int DURITME = 2000;
    private static final String DURATION = "DURATION";

    private int VIEDOPAUSEPALY = 0;
    private int speedItem = 0;

    private List<ChapterInfo> chapterList;

    private int lastPostion = 0;
    //问答列表
    private PlayerVodAdapter mAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    //本地存储
    private SharedPreferences sp;
    //用户ID
    private int userId;
    //传递参数
    private int scheduleId;
    //判断是否含有ppt
    private int IsHavePPT = 0;

    //切换显示
    private LinearLayout palylist;
    private LinearLayout wordList;
    private ImageButton landscape, landscape2;

    interface MSG {
        int MSG_ON_INIT = 1;
        int MSG_ON_STOP = 2;
        int MSG_ON_POSITION = 3;
        int MSG_ON_VIDEOSIZE = 4;
        int MSG_ON_PAGE = 5;
        int MSG_ON_SEEK = 6;
        int MSG_ON_AUDIOLEVEL = 7;
        int MSG_ON_ERROR = 8;
        int MSG_ON_PAUSE = 9;
        int MSG_ON_RESUME = 10;
    }

    protected Handler myHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG.MSG_ON_INIT:
                    int max = msg.getData().getInt(DURATION);
                    mSeekBarPlayViedo.setMax(max);
                    max = max / 1000;
                    GenseeLog.i(TAG, "MSG_ON_INIT duration = " + max);
                    mAllTimeTextView.setText(getTime(max));
                    mVodPlayer.seekTo(lastPostion);
                    mPauseScreenplay.setImageResource(R.drawable.icon_pause);

                    if (null != chapterListAdapter) {
                        chapterList.clear();
                        if (null != msg.obj) {
                            List<DocInfo> docInfoList = (List<DocInfo>) msg.obj;
                            for (DocInfo docInfo : docInfoList) {
                                List<PageInfo> pageInfoList = docInfo.getPages();
                                if (null != pageInfoList && pageInfoList.size() > 0) {
                                    for (PageInfo pageInfo : pageInfoList) {
                                        ChapterInfo chapterInfo = new ChapterInfo();
                                        chapterInfo.setDocId(docInfo.getDocId());
                                        chapterInfo
                                                .setDocName(docInfo.getDocName());
                                        chapterInfo.setDocPageNum(docInfo
                                                .getPageNum());
                                        chapterInfo.setDocType(docInfo.getType());

                                        chapterInfo.setPageTimeStamp(pageInfo
                                                .getTimeStamp());
                                        chapterInfo.setPageTitle(pageInfo
                                                .getTitle());
                                        chapterList.add(chapterInfo);
                                    }
                                }
                            }

                        }
                        chapterListAdapter.notifyData(chapterList);
                    }
                    break;
                case MSG.MSG_ON_STOP:

                    break;
                case MSG.MSG_ON_VIDEOSIZE:

                    break;
                case MSG.MSG_ON_PAGE:
                    int position = (Integer) msg.obj;
                    int nSize = chapterList.size();
                    for (int i = 0; i < nSize; i++) {
                        ChapterInfo chapterInfo = chapterList.get(i);
                        if (chapterInfo.getPageTimeStamp() == position) {
                            if (null != chapterListAdapter) {
                                chapterListAdapter.setSelectedPosition(i);
                            }
                            break;
                        }
                    }
                    break;
                case MSG.MSG_ON_PAUSE:
                    VIEDOPAUSEPALY = 1;
                    mPauseScreenplay.setImageResource(R.drawable.icon_play);
                    break;
                case MSG.MSG_ON_RESUME:
                    VIEDOPAUSEPALY = 0;
                    mPauseScreenplay.setImageResource(R.drawable.icon_pause);
                    break;
                case MSG.MSG_ON_POSITION:
                    if (isTouch) {
                        return;
                    }
                case MSG.MSG_ON_SEEK:
                    isTouch = false;
                    int anyPosition = (Integer) msg.obj;
                    mSeekBarPlayViedo.setProgress(anyPosition);
                    anyPosition = anyPosition / 1000;
                    mNowTimeTextview.setText(getTime(anyPosition));
                    break;
                case MSG.MSG_ON_AUDIOLEVEL:

                    break;
                case MSG.MSG_ON_ERROR:
                    int errorCode = (Integer) msg.obj;
                    switch (errorCode) {
                        case ERR_PAUSE:
                            Toast.makeText(getApplicationContext(), "暂停失败", Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        case ERR_PLAY:
                            Toast.makeText(getApplicationContext(), "播放失败", Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        case ERR_RESUME:
                            Toast.makeText(getApplicationContext(), "恢复失败", Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        case ERR_SEEK:
                            Toast.makeText(getApplicationContext(), "进度变化失败", Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        case ERR_STOP:
                            Toast.makeText(getApplicationContext(), "停止失败", Toast.LENGTH_SHORT)
                                    .show();
                            break;
                        default:
                            break;
                    }
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }

    };

    private String getTime(int time) {
        return String.format("%02d", time / 3600) + ":"
                + String.format("%02d", time % 3600 / 60) + ":"
                + String.format("%02d", time % 3600 % 60);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON,
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.vod_player);
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            scheduleId = bundle.getInt("scheduleId");
        }
        lastPostion = getPreferences(MODE_PRIVATE).getInt("lastPos", 0);

        palylist = (LinearLayout) findViewById(R.id.palylist);
        wordList = (LinearLayout) findViewById(R.id.wordList);
        landscape = (ImageButton) findViewById(R.id.landscape);
        landscape2 = (ImageButton) findViewById(R.id.landscape2);
        landscape.setOnClickListener(this);
        landscape2.setOnClickListener(this);
        mGSVideoView = (GSVideoView) findViewById(R.id.gsvideoview);
        mGlDocView = (GSDocViewGx) findViewById(R.id.playGlDocView);
        mSeekBarPlayViedo = (SeekBar) findViewById(R.id.seekbarpalyviedo);
        stopVeidoPlay = (Button) findViewById(R.id.stopveidoplay);
        mPauseScreenplay = (ImageButton) findViewById(R.id.pauseresumeplay);
        replyVedioPlay = (Button) findViewById(R.id.replayvedioplay);
        mNowTimeTextview = (TextView) findViewById(R.id.palynowtime);
        mAllTimeTextView = (TextView) findViewById(R.id.palyalltime);
        btnDocList = (Button) findViewById(R.id.doc_list_btn);
        btnDocList.setOnClickListener(this);
        chapterListAdapter = new ChapterListAdapter();
        chapterList = new ArrayList<ChapterInfo>();
        lvChapterList = (ListView) findViewById(R.id.doc_lv);
        lvChapterList.setAdapter(chapterListAdapter);
        lvChapterList.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                ChapterInfo chapterInfo = chapterList.get(position);
                if (null != mVodPlayer) {
                    mVodPlayer.seekTo(chapterInfo.getPageTimeStamp());
                }
            }
        });

        mSeekBarPlayViedo.setOnSeekBarChangeListener(this);
        //mDocView.setOnDocViewClickedListener(this);
        // mDocView.showAdaptViewWidth();

        stopVeidoPlay.setOnClickListener(this);
        replyVedioPlay.setOnClickListener(this);

        mPauseScreenplay.setOnClickListener(this);
        findViewById(R.id.speed).setOnClickListener(this);

        initPlayer();

        initQAList();

        initAddVodRecord();
    }


    private void initAddVodRecord() {
        HttpUtils httpUtils = new HttpUtils();
        if (httpUtils.isNetworkConnected(this)) {

        }

    }

    private void initQAList() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerview);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefreshLayout.setColorSchemeColors(Color.RED, Color.RED);
        //初始化adapter
        mAdapter = new PlayerVodAdapter(PlayActivity.this, null, true);
        mAdapter.setAddHaed(false);//设置无头部

        //刷新数据监听
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();

            }
        });
        //加载更多事件
        mAdapter.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(boolean isReload) {
//                loadMore();
            }
        });
        LinearLayoutManager layoutManager = new LinearLayoutManager(PlayActivity.this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        loadData();//初始化数据
    }

    private void loadData() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!"".equals(QAList) && null != QAList) {
                    mAdapter.setInitData(QAList);
                    mSwipeRefreshLayout.setRefreshing(false);
                    mAdapter.setLoadingView(R.layout.load_loading);
                } else {//无数据处理

                }

            }
        }, 1000);
    }


    /**
     * 加载更多问答数据
     */
//    private void loadMore() {
//        PageIndex = PageIndex + 1;
//        ProductListInfo(0, PageIndex);
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
////                if (mAdapter.getItemCount() > 30 && isFailed) {
//                if (1 == isEndList && isFailed) {
//                    isFailed = false;
//                    //加载失败
//                    mAdapter.setLoadFailedView(R.layout.load_failed);
//                } else if (0 == isEndList) {
//                    //加载完成
//                    mAdapter.setLoadEndView(R.layout.load_end);
//                } else {
//                    mAdapter.setLoadMoreData(CustomList);
//                }
//            }
//        }, 1000);
//    }
    private void initPlayer() {
        String vodIdOrLocalPath = getVodIdOrLocalPath();
        if (vodIdOrLocalPath == null) {
            Toast.makeText(this, "路径不对", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mVodPlayer == null) {
            mVodPlayer = new VODPlayer();
            mVodPlayer.setGSVideoView(mGSVideoView);
            mVodPlayer.setGSDocViewGx(mGlDocView);
            mVodPlayer.play(vodIdOrLocalPath, this, "", false);
//            mVodPlayer.


            Log.i("lp width === ", "" + mGSVideoView.getWidth());
            Log.i("lp height === ", "" + mGSVideoView.getWidth());
        }
    }

    private String getVodIdOrLocalPath() {
        String vodId = getIntent().getStringExtra("play_param");
        String localpath = getIntent().getStringExtra("play_path");
        GenseeLog.d(TAG, "path = " + localpath + " vodId = " + vodId);
        String vodIdOrLocalPath = null;
        if (!StringUtil.isEmpty(localpath)) {
            vodIdOrLocalPath = localpath;
        } else if (!StringUtil.isEmpty(vodId)) {
            vodIdOrLocalPath = vodId;
        }
        return vodIdOrLocalPath;
    }

    @Override
    public void onInit(int result, boolean haveVideo, int duration,
                       List<DocInfo> docInfos) {
        Gson S = new Gson();
        Log.i("onInit -- ", "" + S.toJson(docInfos).toString());
//        onDocInfo
        Log.i("haveVideo -- ", "" + haveVideo);
        if (null != docInfos && !"".equals(docInfos)) {
            for (int i = 0; i < docInfos.size(); i++) {
                IsHavePPT = docInfos.get(i).getType();
            }
        }

        if (lastPostion >= duration - 1000) {
            lastPostion = 0;
        }
        Message message = new Message();
        message.what = MSG.MSG_ON_INIT;
        message.obj = docInfos;
        Bundle bundle = new Bundle();
        bundle.putInt(DURATION, duration);
        message.setData(bundle);
        myHandler.sendMessage(message);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    public void onPlayStop() {
        myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_STOP, 0));
    }

    @Override
    public void onPosition(int position) {
        GenseeLog.d(TAG, "onPosition pos = " + position);
        lastPostion = position;
        myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_POSITION,
                position));
    }

    @Override
    public void onVideoSize(int position, int videoWidth, int videoHeight) {
        myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_VIDEOSIZE, 0));
    }


    @Override
    public void onSeek(int position) {
        myHandler.sendMessage(myHandler
                .obtainMessage(MSG.MSG_ON_SEEK, position));
    }

    @Override
    public void onAudioLevel(int level) {
        myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_AUDIOLEVEL,
                level));
    }

    @Override
    public void onError(int errCode) {
        myHandler.sendMessage(myHandler
                .obtainMessage(MSG.MSG_ON_ERROR, errCode));
    }

    @Override
    public void onPlayPause() {
        myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_PAUSE, 0));
    }

    @Override
    public void onPlayResume() {
        myHandler.sendMessage(myHandler.obtainMessage(MSG.MSG_ON_RESUME, 0));
    }


    @Override
    public void onPageSize(int position, int w, int h) {
        //文档翻页切换，开始显示
        myHandler.sendMessage(myHandler
                .obtainMessage(MSG.MSG_ON_PAGE, position));

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
                                  boolean fromUser) {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        isTouch = true;

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (null != mVodPlayer) {
            int pos = seekBar.getProgress();
            GenseeLog.d(TAG, "onStopTrackingTouch pos = " + pos);
            mVodPlayer.seekTo(pos);

        }

    }

    @Override
    public void onClick(View currenView) {
        if (currenView.getId() == R.id.stopveidoplay) {
            boolean ret = mVodPlayer.stop();
            mSeekBarPlayViedo.setMax(0);
            Toast.makeText(this, ret ? "操作成功" : "操作失败", Toast.LENGTH_SHORT).show();
        } else if (currenView.getId() == R.id.replayvedioplay) {
            isTouch = false;
            String vodIdOrLocalPath = getVodIdOrLocalPath();
            if (vodIdOrLocalPath == null) {
                Toast.makeText(this, "路径不对", Toast.LENGTH_SHORT).show();
                return;
            }
            //重新播放时  将速度恢复为正常速度，如果要保持上一次速度播放，设置为上一次速度
            speedItem = 0;
            mVodPlayer.setSpeed(PlaySpeed.SPEED_NORMAL, null);
            mVodPlayer.setGSVideoView(mGSVideoView);
            mVodPlayer.setGSDocViewGx(mGlDocView);
            mVodPlayer.play(vodIdOrLocalPath, this, "", false);

        } else if (currenView.getId() == R.id.pauseresumeplay) {
            if (VIEDOPAUSEPALY == 0) {
                mVodPlayer.pause();
            } else if (VIEDOPAUSEPALY == 1) {
                mVodPlayer.resume();
            }
        } else if (currenView.getId() == R.id.doc_list_btn) {
//            章节列表按钮显示和隐藏
//            if (lvChapterList.getVisibility() == View.VISIBLE) {
//                lvChapterList.setVisibility(View.GONE);
//            } else {
//                lvChapterList.setVisibility(View.VISIBLE);
//            }
        } else if (currenView.getId() == R.id.speed) {
            switchSpeed();
        } else if (currenView.getId() == R.id.landscape) {
            Log.i("视频全屏=== ", " ===== ");
            if (mGSVideoView.getWidth() < 1000) {
                palylist.setVisibility(View.VISIBLE);
                wordList.setVisibility(View.GONE);
                ViewGroup.LayoutParams lp = mGSVideoView.getLayoutParams();
                lp.height = LinearLayout.LayoutParams.MATCH_PARENT;
                lp.width = LinearLayout.LayoutParams.MATCH_PARENT;
                mGSVideoView.setLayoutParams(lp);
            } else {
                palylist.setVisibility(View.VISIBLE);
                wordList.setVisibility(View.VISIBLE);
            }


        } else if (currenView.getId() == R.id.landscape2) {
            if(palylist.getVisibility() == View.VISIBLE){
                palylist.setVisibility(View.GONE);
                mGSVideoView.setVisibility(View.GONE);
                wordList.setVisibility(View.VISIBLE);
            }else{
                palylist.setVisibility(View.VISIBLE);
                mGSVideoView.setVisibility(View.VISIBLE);
                wordList.setVisibility(View.VISIBLE);
            }
//            if (mGlDocView.getWidth() < 1000) {
//                palylist.setVisibility(View.GONE);
//                wordList.setVisibility(View.VISIBLE);
//                ViewGroup.LayoutParams lp = mGlDocView.getLayoutParams();
//                lp.height = RelativeLayout.LayoutParams.MATCH_PARENT;
//                lp.width = RelativeLayout.LayoutParams.MATCH_PARENT;
//                mGlDocView.setLayoutParams(lp);
//            } else {
//                Log.i("文档半屏=== ", " ===== ");
//                palylist.setVisibility(View.VISIBLE);
//                wordList.setVisibility(View.VISIBLE);
//            }

        }
    }

    /**
     * 变速播放
     */
    private void switchSpeed() {
        new AlertDialog.Builder(this)
                .setSingleChoiceItems(R.array.speeds, speedItem,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                speedItem = which;
                                PlaySpeed ps = PlaySpeed.SPEED_NORMAL;
                                switch (which) {
                                    case 0:
                                        ps = PlaySpeed.SPEED_NORMAL;
                                        break;
                                    case 1:
                                        ps = PlaySpeed.SPEED_125;
                                        break;
                                    case 2:
                                        ps = PlaySpeed.SPEED_150;
                                        break;
                                    case 3:
                                        ps = PlaySpeed.SPEED_175;
                                        break;
                                    case 4:
                                        ps = PlaySpeed.SPEED_200;
                                        break;
                                    case 6:
                                        ps = PlaySpeed.SPEED_250;
                                        break;
                                    case 7:
                                        ps = PlaySpeed.SPEED_300;
                                        break;
                                    case 8:
                                        ps = PlaySpeed.SPEED_350;
                                        break;
                                    case 9:
                                        ps = PlaySpeed.SPEED_400;
                                        break;

                                    default:
                                        break;
                                }
                                mVodPlayer.setSpeed(ps, null);
                                dialog.dismiss();
                            }
                        }).create().show();
    }

    private void stopPlay() {
        if (mVodPlayer != null) {
            mVodPlayer.stop();
        }
    }

    private void release() {
        stopPlay();
        if (mVodPlayer != null) {
            mVodPlayer.release();
        }
    }

    @Override
    public void onBackPressed() {
//		getPreferences(MODE_PRIVATE).edit().putInt("lastPos", lastPostion).commit();
        release();
        super.onBackPressed();
    }

    @Override
    public boolean onDoubleClicked(IGSDocView arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    /**
     * type 边界类型
     * eventType  MotionEvent.ACTION_MOVE or MotionEvent.ACTION_UP
     */
    public boolean onEndHDirection(IGSDocView arg0, int arg1, int eventType) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean onSingleClicked(IGSDocView arg0) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void onCaching(boolean isCatching) {
        // TODO Auto-generated method stub

    }

    private class ChapterListAdapter extends BaseAdapter {
        private List<ChapterInfo> pageList;
        private int selectedPosition = 0;

        public void setSelectedPosition(int position) {
            selectedPosition = position;
            notifyDataSetChanged();
            lvChapterList.setSelection(position);
        }

        public ChapterListAdapter() {
            pageList = new ArrayList<ChapterInfo>();
        }

        public void notifyData(List<ChapterInfo> pageList) {
            this.pageList.clear();
            this.pageList.addAll(pageList);
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return pageList.size();
        }

        @Override
        public Object getItem(int position) {
            return pageList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder = null;
            if (null == convertView) {
                convertView = LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.doc_list_item_ly, null);
                viewHolder = new ViewHolder(convertView);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            viewHolder.init((ChapterInfo) getItem(position), position);
            return convertView;
        }

        private class ViewHolder {
            private TextView tvChapter;
            private TextView tvTitle;
            private TextView tvTime;
            private LinearLayout lyChapter;

            private String getChapterTime(long time) {
                return String.format("%02d", time / (3600 * 1000))
                        + ":"
                        + String.format("%02d", time % (3600 * 1000)
                        / (60 * 1000))
                        + ":"
                        + String.format("%02d", time % (3600 * 1000)
                        % (60 * 1000) / 1000);
            }

            public ViewHolder(View view) {
                tvChapter = (TextView) view.findViewById(R.id.chapter_title);
                tvTitle = (TextView) view.findViewById(R.id.doc_title);
                tvTime = (TextView) view.findViewById(R.id.chapter_time);
                lyChapter = (LinearLayout) view.findViewById(R.id.chapter_ly);
            }

            public void init(ChapterInfo chapterInfo, int position) {
                tvChapter.setText(chapterInfo.getPageTitle());
                tvTime.setText(getChapterTime(chapterInfo.getPageTimeStamp()));
                tvTitle.setText(chapterInfo.getDocName());

                if (selectedPosition == position) {
                    lyChapter.setBackgroundResource(R.color.red);
                } else {
                    lyChapter.setBackgroundResource(R.color.white);
                }
            }
        }

    }

    @Override
    public void onVideoStart() {

    }

    @Override
    public void onChat(List<ChatMsg> arg0) {
        //ChatMsg msg = chatMsgs.get(0);
        // msg.getRichText()//富文本
        // msg.getSender()//发送者名称
        // msg.getSenderId()//发送者id
        // msg.getContent()//纯文本
        // msg.getTimeStamp()//相对与播放开始的时间  单位毫秒
    }

    @Override
    public void onDocInfo(List<DocInfo> docInfos) {
        // TODO Auto-generated method stub
        Gson ss = new Gson();
        Log.i("onDocInfo --- ", "" + ss.toJson(docInfos).toString());
        if (null != docInfos && !"".equals(docInfos)) {
            for (int i = 0; i < docInfos.size(); i++) {
                IsHavePPT = docInfos.get(i).getType();
            }
        }

    }

    /**
     * msg 的时候，id代表要删除的聊天消息id，user的时候，代表用户id，强转为long型后进行用户id匹配删除该id说有的聊天消息
     *
     * @param type msg(CHATCENSOR_MSG) / user(CHATCENSOR_USER)
     * @param id   msgId/userId
     */
    @Override
    public void onChatCensor(String type, String id) {

    }


    //公共参数
    private RequestBody commParam() {
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {
            // 用户id
            sp = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            userId = sp.getInt("S_ID", 0);
            obj.put("userId", userId);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
            obj.put("scheduleId", scheduleId);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }


    private void initRecordDate() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostServer httpPostService = retrofit.create(HttpPostServer.class);
        RequestBody body = commParam();
        Observable<CommonBean<AddVodRecordBean>> observable = httpPostService.getAddVodRecord(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<AddVodRecordBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                    }

                    @Override
                    public void onNext(CommonBean<AddVodRecordBean> AddVodRecordBean) {

                    }
                });
    }
}
