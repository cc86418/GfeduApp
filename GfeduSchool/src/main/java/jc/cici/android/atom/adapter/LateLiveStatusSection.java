package jc.cici.android.atom.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jun.courseinfo.activity.OnlineCourseDetailsActivity;
import cn.jun.live.LiveClassActivity;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.adapter.base.StatusSection;
import jc.cici.android.atom.bean.LiveContent;
import jc.cici.android.atom.ui.live.LiveListActivity;

/**
 * 直播首页最近直播模块
 * Created by atom on 2017/11/13.
 */

public class LateLiveStatusSection extends StatusSection {
    private Context mContext;
    private ArrayList<LiveContent.LateContent> mLateLive;
    private Handler mHandler;
    // 当前项目id
    private int mProjectId;
    // 当前项目名称
    private String mTitle;

    public LateLiveStatusSection(Context context, ArrayList<LiveContent.LateContent> LastestLive, Handler handler, int projectId, String title) {
        super(R.layout.item_late_live, R.layout.empty_live);
        this.mContext = context;
        this.mLateLive = LastestLive;
        this.mHandler = handler;
        this.mProjectId = projectId;
        this.mTitle = title;
    }


    @Override
    public int getContentItemsTotal() {
        return 1;
    }

    @Override
    public RecyclerView.ViewHolder getItemViewHolder(View view) {
        return new EmptyViewHolder(view);
    }

    @Override
    public void onBindItemViewHolder(RecyclerView.ViewHolder holder, int position) {
    }


    @Override
    public RecyclerView.ViewHolder getHeaderViewHolder(View view) {
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderViewHolder(RecyclerView.ViewHolder holder) {
        HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
        headerHolder.lineImg.setBackgroundResource(R.drawable.icon_lesson);
        headerHolder.headerName_txt.setText("最近直播");
        headerHolder.moreLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mContext, LiveListActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("projectId", mProjectId);
                bundle.putString("title", mTitle);
                bundle.putInt("searchType", 0);
                it.putExtras(bundle);
                mContext.startActivity(it);
            }
        });
        headerHolder.lateRecyclerView.setHasFixedSize(false);
        headerHolder.lateRecyclerView.setNestedScrollingEnabled(false);
        headerHolder.lateRecyclerView.setLayoutManager(new LinearLayoutManager(mContext,
                LinearLayoutManager.VERTICAL, false));
        LateLiveAdapter adapter = new LateLiveAdapter(mContext, mLateLive, mHandler);
        headerHolder.lateRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new BaseRecycleerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                // 获取班级id
                int classPkID = mLateLive.get(position).getClass_PKID();
                // 获取系列直播or普通直播
                int classFrom = mLateLive.get(position).getClass_Form();
                switch (classFrom) {
                    case 1: // 系列直播
                        Intent intent = new Intent(mContext, OnlineCourseDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("Product_PKID", classPkID);
                        intent.putExtras(bundle);
                        mContext.startActivity(intent);
                        break;
                    case 2: // 正常直播
//                        Intent normalIt = new Intent(mContext, OnlineCourseDetailsActivity.class);
                        Intent normalIt = new Intent(mContext, LiveClassActivity.class);
                        Bundle norBundle = new Bundle();
//                        norBundle.putInt("Product_PKID", classPkID);
                        norBundle.putInt("Class_PKID", classPkID);
                        normalIt.putExtras(norBundle);
                        mContext.startActivity(normalIt);

                        break;
                }
            }
        });
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder {

        // 最近直播图标
        @BindView(R.id.lineImg)
        ImageView lineImg;
        // 最近直播文字
        @BindView(R.id.headerName_txt)
        TextView headerName_txt;
        // 更多布局
        @BindView(R.id.moreLayout)
        RelativeLayout moreLayout;
        // 列表
        @BindView(R.id.lateRecyclerView)
        RecyclerView lateRecyclerView;

        public HeaderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    private static class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
