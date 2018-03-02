package jc.cici.android.atom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.ExamChoseBean;

/**
 * 指定知识点适配器
 * Created by atom on 2017/12/26.
 */

public class DialogKnowledgeAdapter extends BaseRecycleerAdapter<ExamChoseBean.Knowledge, DialogKnowledgeAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<ExamChoseBean.Knowledge> mItems;

    public DialogKnowledgeAdapter(Context context, List<ExamChoseBean.Knowledge> items) {
        super(context, items);
        this.mCtx = context;
        this.mItems = (ArrayList<ExamChoseBean.Knowledge>) items;
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final ExamChoseBean.Knowledge item, final int position) {
        holder.dialog_knowledgeName_txt.setText(item.getKnowledgeName());
        if (item.isSelected()) {
            holder.checkImg.setBackgroundResource(R.drawable.ic_check_checked);
        } else {
            holder.checkImg.setBackgroundResource(R.drawable.ic_check_normal);
        }

        holder.item_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.isSelected()){
                    item.setSelected(false);
                    holder.checkImg.setBackgroundResource(R.drawable.ic_check_normal);
                }else{
                    item.setSelected(true);
                    holder.checkImg.setBackgroundResource(R.drawable.ic_check_checked);
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_dialog_knowledge;
    }

    @Override
    public int getItemCount() {
        return super.getItemCount();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.item_layout)
        RelativeLayout item_layout;
        // 标题
        @BindView(R.id.dialog_knowledgeName_txt)
        TextView dialog_knowledgeName_txt;
        // 选中图片
        @BindView(R.id.checkImg)
        ImageView checkImg;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
