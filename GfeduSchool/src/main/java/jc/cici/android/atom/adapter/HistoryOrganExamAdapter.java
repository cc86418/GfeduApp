package jc.cici.android.atom.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.HistoryExamChoseBean;
import jc.cici.android.atom.ui.tiku.CardAnswer;
import jc.cici.android.atom.ui.tiku.CardResultActivity;
import jc.cici.android.atom.ui.tiku.newDoExam.TestActivity;
import jc.cici.android.atom.ui.tiku.newDoExam.TestContentFragment;
import jc.cici.android.atom.utils.ToolUtils;

/**
 * 组卷历史适配器
 * Created by atom on 2018/1/2.
 */

public class HistoryOrganExamAdapter extends BaseRecycleerAdapter<HistoryExamChoseBean.TestPaper, HistoryOrganExamAdapter.MyHolder> {

    private Activity mCtx;
    private ArrayList<HistoryExamChoseBean.TestPaper> mItems;

    public HistoryOrganExamAdapter(Context context, List<HistoryExamChoseBean.TestPaper> items) {
        super(context, items);
        this.mCtx = (Activity) context;
        this.mItems = (ArrayList<HistoryExamChoseBean.TestPaper>) items;
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, final HistoryExamChoseBean.TestPaper item, final int position) {

        // 添加标题
        holder.courseName_txt.setText(ToolUtils.strReplaceAll(item.getTestPaper_Name()));
        // 添加时间
        holder.timeName_txt.setText(item.getTestPaper_AddDate());
        //        // 获取试卷状态
        final int status = item.getTestPaper_Status();
        if (0 == status) { // 未提交情况
            holder.againExam_layout.setVisibility(View.GONE);
            holder.reportExam_txt.setText("继续做题");
        } else if (1 == status) { // 已提交情况
            holder.againExam_layout.setBackgroundResource(R.drawable.bg_history_organ_btn);
            holder.againExam_txt.setText("重新做题");
            holder.againExam_txt.setTextColor(Color.parseColor("#666666"));
            holder.reportExam_txt.setText("查看报告");
            holder.againExam_layout.setVisibility(View.VISIBLE);
        }

        if (1 == status) {
            /**
             * 重做按钮监听
             */
            holder.againExam_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent prIt = new Intent(mCtx, TestActivity.class);
                    Bundle prBundle = new Bundle();
                    prBundle.putInt("paperId", item.getTestPaper_PKID());
                    prIt.putExtras(prBundle);
                    mCtx.startActivity(prIt);
                    mCtx.finish();
                }
            });
        }


        /**
         * 查看报告按钮监听
         */
        holder.reportExam_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (1 == status) { // 已提价
                    Intent cIntent = new Intent(mCtx,
                            CardResultActivity.class);
                    // 传递试卷id
                    cIntent.putExtra("TestPPKID", item.getTestPaper_PKID());
                    // 班级id
                    cIntent.putExtra("classId", 0);
                    // 阶段id
                    cIntent.putExtra("stageId", 0);
                    // 课程id
                    cIntent.putExtra("lessonId", 0);
                    // 科目id
                    cIntent.putExtra("levelId", 0);
                    cIntent.putExtra("isOnline", 0);
                    cIntent.putExtra("LessonChildId", 0);
                    // 答题用时
                    cIntent.putExtra("time", "");
                    // 试卷名
                    cIntent.putExtra("name", item.getTestPaper_Name());
                    cIntent.putExtra("studyKey", "");
                    mCtx.startActivity(cIntent);
                    mCtx.finish();
                } else if (0 == status) {
                    Intent prIt = new Intent(mCtx, TestActivity.class);
                    Bundle prBundle = new Bundle();
                    prBundle.putInt("paperId", item.getTestPaper_PKID());
                    prIt.putExtras(prBundle);
                    mCtx.startActivity(prIt);
                    mCtx.finish();
                }
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_history_organ_exam;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        // 标题名
        @BindView(R.id.courseName_txt)
        TextView courseName_txt;
        // 时间
        @BindView(R.id.timeName_txt)
        TextView timeName_txt;
        // 报告布局
        @BindView(R.id.reportExam_layout)
        RelativeLayout reportExam_layout;
        // 报告文字
        @BindView(R.id.reportExam_txt)
        TextView reportExam_txt;
        //  重做布局
        @BindView(R.id.againExam_layout)
        RelativeLayout againExam_layout;
        // 重做文字
        @BindView(R.id.againExam_txt)
        TextView againExam_txt;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
