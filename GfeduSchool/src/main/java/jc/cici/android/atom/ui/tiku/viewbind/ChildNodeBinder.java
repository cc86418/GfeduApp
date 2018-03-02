package jc.cici.android.atom.ui.tiku.viewbind;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import jc.cici.android.R;
import jc.cici.android.atom.ui.tiku.bean.ChildNode;
import jc.cici.android.atom.ui.tiku.newDoExam.KnowledgeTestActivity;
import jc.cici.android.atom.ui.tiku.treeView.TreeNode;
import jc.cici.android.atom.ui.tiku.treeView.TreeViewBinder;

/**
 * Created by atom on 2018/1/3.
 */

public class ChildNodeBinder extends TreeViewBinder<ChildNodeBinder.ViewHolder> {

    private Context mCtx;
    private int mProjectId;

    public ChildNodeBinder(Context context, int projectId) {
        this.mCtx = context;
        this.mProjectId = projectId;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_child_node;
    }

    @Override
    public ViewHolder provideViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void bindView(ViewHolder holder, int position, TreeNode node) {


        // 默认关闭状态
        holder.child_node_close_img.setBackgroundResource(R.drawable.icon_node_open);
        final ChildNode childNode = (ChildNode) node.getContent();
        holder.child_nameCourse_txt.setText(childNode.CoursewareData_Name);
        // 设置进度条
        float i = ((float) childNode.DoCount / (float) childNode.QuesCount);
        int progress = ((int) (i * 100));
        holder.child_progressBar.setProgress(progress);
        //设置百分数
        holder.child_percentPro_txt.setText(childNode.DoCount + "/" + childNode.QuesCount);
        // 设置正确率
        holder.child_rightRate_txt.setText(childNode.RightRatio);
        if (node.isLeaf()) { // 叶子节点
            holder.child_node_close_img.setBackgroundResource(R.drawable.icon_node_open);
        } else {
            holder.child_node_close_img.setBackgroundResource(R.drawable.icon_node_close);
        }

        holder.child_doExam_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mCtx, KnowledgeTestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("knowledgeId", childNode.CoursewareData_PKID);
                bundle.putInt("projectId", mProjectId);
                // 答案类型1:表示每日一题，2表示知识点做题
                bundle.putInt("answerType", 2);
                bundle.putString("title", childNode.CoursewareData_Name);
                it.putExtras(bundle);
                mCtx.startActivity(it);
            }
        });

    }

    public static class ViewHolder extends TreeViewBinder.ViewHolder {

        // 伸缩图片
        public ImageView child_node_close_img;
        // 课程文字
        private TextView child_nameCourse_txt;
        // 去做题
        private ImageButton child_doExam_txt;
        // 进度条
        private ProgressBar child_progressBar;
        // 进度百分比
        private TextView child_percentPro_txt;
        // 正确率
        private TextView child_rightRate_txt;

        public ViewHolder(View itemView) {
            super(itemView);
            this.child_node_close_img = (ImageView) itemView.findViewById(R.id.child_node_close_img);
            this.child_nameCourse_txt = (TextView) itemView.findViewById(R.id.child_nameCourse_txt);
            this.child_doExam_txt = (ImageButton) itemView.findViewById(R.id.child_doExam_txt);
            this.child_progressBar = (ProgressBar) itemView.findViewById(R.id.child_progressBar);
            this.child_percentPro_txt = (TextView) itemView.findViewById(R.id.child_percentPro_txt);
            this.child_rightRate_txt = (TextView) itemView.findViewById(R.id.child_rightRate_txt);
        }
    }
}
