package jc.cici.android.atom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.zhy.autolayout.utils.AutoUtils;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.TestPagerInfo;
import jc.cici.android.atom.utils.ToolUtils;

/**
 * 课程详情做题适配
 * Created by atom on 2017/7/14.
 */

public class TestRecyclerAdapter extends BaseRecycleerAdapter<TestPagerInfo, TestRecyclerAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<TestPagerInfo> mItems;

    public TestRecyclerAdapter(Context context, ArrayList<TestPagerInfo> items) {
        super(context, items);
        this.mCtx = context;
        this.mItems = items;
    }


    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, TestPagerInfo item, int position) {
        holder.faceTestTxt.setText(ToolUtils.strReplaceAll(item.getPaperName()));
        int status = item.getPaperStudyState();
        switch (status) {
            case 0: // 未学习
                holder.studyStatus_txt.setText("未答题");
                break;
            case 1: // 进行中
                holder.studyStatus_txt.setText("答题中");
                break;
            case 2: // 已答题
                holder.studyStatus_txt.setText("已答题");
                break;
            default:
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_course_testpaper;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        // 测试题名称
        @BindView(R.id.faceTestTxt)
        TextView faceTestTxt;
        // 学习状态
        @BindView(R.id.studyStatus_txt)
        TextView studyStatus_txt;

        public MyHolder(View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
