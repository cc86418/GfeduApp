package cn.jun.courseinfo.course_adapter;


import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jun.courseinfo.bean.TypeBean;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;

import static cn.jun.courseinfo.course_adapter.ClassCourseRecyclerAdapter.msgTypeClick;

public class TypeRecyclerAdapter extends BaseRecycleerAdapter<TypeBean, TypeRecyclerAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<TypeBean> mData;
    private Handler mHandler;
    private int mSelectedPos = -1;

    private int parent_pos;

    public TypeRecyclerAdapter(Context context, List items, Handler handler,int parent_pos) {
        super(context, items);
        this.mCtx = context;
        this.mData = (ArrayList<TypeBean>) items;
        this.mHandler = handler;
        this.parent_pos = parent_pos;

//        Log.i("parent_pos = ","" + parent_pos);
        initSelect();
//        clickClassBen = new ClickClassBen();
    }

    /**
     * 创建初始map值
     */
    private void initSelect() {
        Gson s = new Gson();
        Log.i("initSelect", "== > " + s.toJson(mData).toString());
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, TypeBean item, final int position) {
        Log.i("子类","onBindViewHolder");
        holder.checkBox.setText(item.getTypeName());
        if (item.isChecked()) {
            holder.checkBox.setClickable(true);
            holder.checkBox.setBackgroundResource(R.drawable.yuan_background_all_hui);
            holder.checkBox.setTextColor(Color.parseColor("#333333"));

            if (item.isTypeSelect()) {
                holder.checkBox.setBackgroundResource(R.drawable.yuan_background_all);
                holder.checkBox.setTextColor(Color.parseColor("#ffffff"));
            } else {
                holder.checkBox.setBackgroundResource(R.drawable.yuan_background_all_hui);
                holder.checkBox.setTextColor(Color.parseColor("#333333"));
            }
        } else {
            holder.checkBox.setClickable(false);
            holder.checkBox.setBackgroundResource(R.drawable.yuan_background_all_hui);
            holder.checkBox.setTextColor(Color.parseColor("#b1b1b1"));
        }


        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (mData.get(position).isTypeSelect()) {
                    mData.get(position).setTypeSelect(false);
                    msgTypeClick = false;
                    notifyItemChanged(position);
                } else {
                    mData.get(position).setTypeSelect(true);
                    notifyItemChanged(position);
                    for (int i = 0; i < mData.size(); i++) {
                        if (i != position) {
                            mData.get(i).setTypeSelect(false);
                            notifyItemChanged(i);
                        }
                    }

                }

//                for (int i = 0; i < mData.size(); i++) {
//                    if (mData.get(i).isTypeSelect()) {
//                        clickClassBen.setTypeClick(true);
//                    } else {
//                        clickClassBen.setTypeClick(false);
//                    }
//                }

                Message msg = new Message();
                msg.what = 2;
                msg.obj = mData;
                mHandler.sendMessage(msg);

                Message msg2 = new Message();
                msg2.what = -1;
                msg2.obj = parent_pos;
                mHandler.sendMessage(msg2);

                Gson s = new Gson();
                Log.i(" /点击之后。。。。。/", "" + s.toJson(mData).toString());

            }
        });
    }

    @Override
    public int getLayoutId() {
        return R.layout.course_select_child;
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
}
