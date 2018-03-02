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
import jc.cici.android.atom.bean.LiveCallBack;

/**
 * 有无回放适配器
 * Created by atom on 2017/11/17.
 */

public class RePlayAdapter extends BaseRecycleerAdapter<LiveCallBack,RePlayAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<LiveCallBack> mData;
    private Handler mHandler;
    private int mSelectedPos = -1;

    public RePlayAdapter(Context context, List<LiveCallBack> items,Handler handler) {
        super(context, items);
        this.mCtx = context;
        this.mData = (ArrayList<LiveCallBack>) items;
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
    public void onBindViewHolder(MyHolder holder, final LiveCallBack item, final int position) {

        holder.checkBox.setText(item.getCallName());
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
                    msg.what =2;
                    msg.obj = mData.get(position).getIsCallBack();
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
