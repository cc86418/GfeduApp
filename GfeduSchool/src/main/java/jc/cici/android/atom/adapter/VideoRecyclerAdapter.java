package jc.cici.android.atom.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jun.menory.service.VideoDownloadManager;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.ReplayInfo;
import jc.cici.android.atom.utils.ToolUtils;


/**
 * 课程详情中下载列表
 * Created by atom on 2017/7/11.
 */

public class VideoRecyclerAdapter extends BaseRecycleerAdapter<ReplayInfo, VideoRecyclerAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<ReplayInfo> mItems;
    private Handler mHandler;
    // 班级id
    private int mClassId;
    // 班级名称
    private String mClassName;
    // 阶段id
    private int mStageId;
    private String strClassId;
    private String strStageId;
    private int mStageNote;
    private int mStageProblem;
    private int mStageInfo;
    private String strStageNote;
    private String strStageProblem;
    private String strStageInfo;

    public VideoRecyclerAdapter(Context context, ArrayList<ReplayInfo> items, Handler handler, int classId, String className, int stageId, int stageNote, int stageProblem, int stageInformation) {
        super(context, items);
        this.mCtx = context;
        this.mItems = items;
        this.mHandler = handler;
        this.mClassId = classId;
        this.mClassName = className;
        this.mStageId = stageId;
        this.mStageNote = stageNote;
        this.mStageProblem = stageProblem;
        this.mStageInfo = stageInformation;
        // 便于下载管理，特转化为字符串传递
        resetString();
    }

    /**
     * 字符串转化
     */
    private void resetString() {
        strClassId = String.valueOf(mClassId);
        strStageId = String.valueOf(mStageId);
        strStageNote = String.valueOf(mStageNote);
        strStageProblem = String.valueOf(mStageProblem);
        strStageInfo = String.valueOf(mStageInfo);
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final ReplayInfo item, final int position) {
        // 视频内容
        String localVideoID = VideoDownloadManager.getInstance().SelectBufferedVideosItems(mItems.get(position).getVideoVID());
        String localVID = VideoDownloadManager.getInstance().SelectBufferedVideosItemsStatus(mItems.get(position).getVideoVID());

        if (null != localVideoID && mItems.get(position).getVideoVID().equals(localVideoID)) {
            if ("0".equals(localVID)) {
                holder.downloadImg.setVisibility(View.GONE);
                holder.downloadTxt.setText("下载中");
                holder.downloadTxt.setVisibility(View.VISIBLE);
                // 如果已缓存，则设置下载监听无效
                holder.downBtnLayout.setEnabled(false);
                holder.downBtnLayout.setClickable(false);
            } else if ("1".equals(localVID)) {
                holder.downloadImg.setVisibility(View.GONE);
                holder.downloadTxt.setText("下载失败");
                holder.downloadTxt.setVisibility(View.VISIBLE);
                // 如果已缓存，则设置下载监听无效
                holder.downBtnLayout.setEnabled(false);
                holder.downBtnLayout.setClickable(false);
            } else if ("2".equals(localVID)) {
                holder.downloadImg.setVisibility(View.GONE);
                holder.downloadTxt.setText("已缓存");
                holder.downloadTxt.setVisibility(View.VISIBLE);
                // 如果已缓存，则设置下载监听无效
                holder.downBtnLayout.setEnabled(false);
                holder.downBtnLayout.setClickable(false);
            }
        } else {
            holder.downloadImg.setVisibility(View.VISIBLE);
            holder.downloadTxt.setText("");
            holder.downloadTxt.setVisibility(View.GONE);
            // 设置可以下载监听
            holder.downBtnLayout.setEnabled(true);
            holder.downBtnLayout.setClickable(true);
        }
        holder.videoTxt.setText(ToolUtils.replaceAllChar(item.getVideoName()));
        holder.downBtnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 加入下载列表
                String lessId = String.valueOf(mItems.get(position).getVideoLessonChildId());
                download(mItems.get(position).getVideoVID(), lessId, mItems.get(position).getVideoName(), position);
                holder.downloadImg.setVisibility(View.GONE);
                holder.downloadTxt.setText("正在下载");
                holder.downloadTxt.setVisibility(View.VISIBLE);
                Message msg = new Message();
                msg.what = 0;
                msg.obj = 1;
                mHandler.sendMessage(msg);
            }
        });
    }


    private void download(String videoId, String lessId, String lessName, int position) {

        System.out.println("班级id >>>:" + strClassId + ",班级名称 >>>:"
                + mClassName + "，阶段id >>>" + strClassId + ",课程id >>>:" + lessId
                + ",课程名称 >>>:" + lessName);
        //判断班级数据库
        if (VideoDownloadManager.getInstance().addClassIdTassk(strClassId, mClassName)) {
            //判断阶段数据库
            if (VideoDownloadManager.getInstance().addStageIdTassk(strClassId, strStageId, mClassName)) {
                //判断下载列表数据
                String VPKID = String.valueOf(mItems.get(position).getVideoID());
                String studyKey = mItems.get(position).getVideoStudyKey();
                if (!VideoDownloadManager.getInstance().addVideoTask(videoId, strClassId, lessId, lessName, lessId, lessName, strStageId, VPKID, "isStudy", studyKey, strStageProblem, strStageNote, strStageInfo)) {
                    Toast.makeText(mCtx,
                            "已经到达同时最大下载数量5个，请稍后下载", Toast.LENGTH_LONG)
                            .show();
                }
            }
        }

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_download;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        // 视频名称
        @BindView(R.id.videoTxt)
        TextView videoTxt;
        // 下载按钮布局
        @BindView(R.id.downBtnLayout)
        RelativeLayout downBtnLayout;
        // 下载图片
        @BindView(R.id.downloadImg)
        ImageView downloadImg;
        // 下载文字
        @BindView(R.id.downloadTxt)
        TextView downloadTxt;


        public MyHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
