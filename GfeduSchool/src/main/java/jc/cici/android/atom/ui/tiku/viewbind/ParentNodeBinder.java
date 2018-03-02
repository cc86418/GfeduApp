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
import jc.cici.android.atom.ui.tiku.bean.ParentNode;
import jc.cici.android.atom.ui.tiku.newDoExam.KnowledgeTestActivity;
import jc.cici.android.atom.ui.tiku.treeView.TreeNode;
import jc.cici.android.atom.ui.tiku.treeView.TreeViewBinder;

/**
 * 父类视图绑定
 * Created by atom on 2018/1/3.
 */

public class ParentNodeBinder extends TreeViewBinder<ParentNodeBinder.ViewHolder> {

    private Context mCtx;
    private int mProjectId;

    public ParentNodeBinder(Context context,int projectId) {
        this.mCtx = context;
        this.mProjectId = projectId;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_parent_node;
    }

    @Override
    public ViewHolder provideViewHolder(View itemView) {
        return new ViewHolder(itemView);
    }

    @Override
    public void bindView(ViewHolder holder, int position, final TreeNode node) {

        // 默认关闭状态
        holder.node_close_img.setBackgroundResource(R.drawable.icon_node_close);
        final ParentNode parentNode = (ParentNode) node.getContent();
        holder.nameCourse_txt.setText(parentNode.CoursewareData_Name);
        // 设置进度条
        float i = ((float)parentNode.DoCount /(float) parentNode.QuesCount);
        int progress = ((int)(i*100));
        holder.progressBar.setProgress(progress);
        //设置百分数
        holder.percentPro_txt.setText(parentNode.DoCount + "/" + parentNode.QuesCount);
        // 设置正确率
        holder.rightRate_txt.setText(parentNode.RightRatio);
        if (node.isLeaf()) { // 叶子节点
            holder.node_close_img.setBackgroundResource(R.drawable.icon_node_open);
        } else {
            holder.node_close_img.setBackgroundResource(R.drawable.icon_node_close);
        }

        holder.doExam_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mCtx, KnowledgeTestActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("knowledgeId", parentNode.CoursewareData_PKID);
                bundle.putInt("projectId",mProjectId);
                // 答案类型1:表示每日一题，2表示知识点做题
                bundle.putInt("answerType", 2);
                bundle.putString("title", parentNode.CoursewareData_Name);
                it.putExtras(bundle);
                mCtx.startActivity(it);
            }
        });
    }

    public static class ViewHolder extends TreeViewBinder.ViewHolder {

        // 伸缩图片
        public ImageView node_close_img;
        // 课程文字
        private TextView nameCourse_txt;
        // 去做题
        private ImageButton doExam_txt;
        // 进度条
        private ProgressBar progressBar;
        // 进度百分比
        private TextView percentPro_txt;
        // 正确率
        private TextView rightRate_txt;

        public ViewHolder(View itemView) {
            super(itemView);
            this.node_close_img = (ImageView) itemView.findViewById(R.id.node_close_img);
            this.nameCourse_txt = (TextView) itemView.findViewById(R.id.nameCourse_txt);
            this.doExam_txt = (ImageButton) itemView.findViewById(R.id.doExam_txt);
            this.progressBar = (ProgressBar) itemView.findViewById(R.id.progressBar);
            this.percentPro_txt = (TextView) itemView.findViewById(R.id.percentPro_txt);
            this.rightRate_txt = (TextView) itemView.findViewById(R.id.rightRate_txt);
        }
    }
}
