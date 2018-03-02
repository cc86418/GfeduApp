package jc.cici.android.atom.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.LiveSelectBean;

/**
 * 直播学科筛选适配
 * Created by atom on 2017/11/17.
 */

public class OneRecyclerViewAdapter extends BaseRecycleerAdapter<LiveSelectBean.SelectList.OneModel, OneRecyclerViewAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<LiveSelectBean.SelectList.OneModel> mData;
    private Handler mHandler;
    private int mSelectedPos = -1;


    public OneRecyclerViewAdapter(Context context, List items,Handler handler) {
        super(context, items);
        this.mCtx = context;
        this.mData = (ArrayList<LiveSelectBean.SelectList.OneModel>) items;
        this.mHandler = handler;
        // 初始化数组
        initArray();
    }

    private void initArray() {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isSelect()) {
                mSelectedPos = i;
            }
        }
    }


    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final LiveSelectBean.SelectList.OneModel item, final int position) {

        holder.checkBox.setText(item.getProjectName());
        if (item.isSelect()) {
            holder.checkImg.setVisibility(View.VISIBLE);
        } else {
            holder.checkImg.setVisibility(View.GONE);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (mSelectedPos != position) {
                    //先取消上个item的勾选状态
                    mData.get(mSelectedPos).setSelect(false);
                    notifyItemChanged(mSelectedPos);
                    //设置新Item的勾选状态
                    mSelectedPos = position;
                    mData.get(mSelectedPos).setSelect(true);
                    notifyItemChanged(mSelectedPos);
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = mData.get(position).getProjectId();
                    mHandler.sendMessage(msg);
                }
            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_select_child;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        // 选中框
        @BindView(R.id.checkBox)
        CheckBox checkBox;
        // 选中图片
        @BindView(R.id.checkImg)
        ImageView checkImg;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
