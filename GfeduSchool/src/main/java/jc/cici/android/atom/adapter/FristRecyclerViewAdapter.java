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
import jc.cici.android.atom.bean.Parent;

/**
 * Created by User on 2017/8/10.
 */

public class FristRecyclerViewAdapter extends BaseRecycleerAdapter<Parent, FristRecyclerViewAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<Parent> mData;
    private Handler mHandler;
    private int mSelectedPos = -1;

    public FristRecyclerViewAdapter(Context context, List items, Handler handler) {
        super(context, items);
        this.mCtx = context;
        this.mData = (ArrayList<Parent>) items;
        this.mHandler = handler;
        initSelect();
    }

    /**
     * 创建初始map值
     */
    private void initSelect() {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isSelected()) {
                mSelectedPos = i;
            }
        }
    }


    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, Parent item, final int position) {

        holder.checkBox.setText(item.getProjectName());
        if (item.isSelected()) {
            holder.checkImg.setVisibility(View.VISIBLE);
        } else {
            holder.checkImg.setVisibility(View.GONE);
        }
//        if (map.get(position)) {
//            holder.checkImg.setVisibility(View.VISIBLE);
//        } else {
//            holder.checkImg.setVisibility(View.GONE);
//        }

        /**
         *  筛选框选中监听
         */
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mSelectedPos != position) {
                    //先取消上个item的勾选状态
                    mData.get(mSelectedPos).setSelected(false);
                    notifyItemChanged(mSelectedPos);
                    //设置新Item的勾选状态
                    mSelectedPos = position;
                    mData.get(mSelectedPos).setSelected(true);
                    notifyItemChanged(mSelectedPos);
                    Message msg = new Message();
                    msg.what = 0;
                    msg.obj = mData.get(position).getProjectId();
                    mHandler.sendMessage(msg);
                }
//                if (map.get(position)) {
//                    map.put(position, false);
//                    setMap(map);
//                    holder.checkImg.setVisibility(View.VISIBLE);
//                } else {
//                    map.put(position, true);
//                    setMap(map);
//                    holder.checkImg.setVisibility(View.GONE);
//                }
//                notifyItemChanged(position);
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

    public class MyHolder extends RecyclerView.ViewHolder {

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

//    public Map<Integer, Boolean> getMap() {
//        return map;
//    }
//
//    public void setMap(Map<Integer, Boolean> map) {
//        this.map = map;
//    }

}
