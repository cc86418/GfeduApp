package jc.cici.android.atom.adapter;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.ChildEntity;
import jc.cici.android.atom.bean.TypeBean;

/**
 * Created by User on 2017/8/10.
 */

public class TypeRecyclerViewAdapter extends BaseRecycleerAdapter<TypeBean, TypeRecyclerViewAdapter.MyHolder> {


    private Context mCtx;
    private ArrayList<TypeBean> mData;
    private Handler mHandler;
    private ArrayList<Integer> arrTypeId = new ArrayList<>();


    public TypeRecyclerViewAdapter(Context context, List<TypeBean> items, Handler handler) {
        super(context, items);
        this.mCtx = context;
        this.mData = (ArrayList<TypeBean>) items;
        this.mHandler = handler;
        // 初始化数组
        initArray();
    }

    private void initArray() {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).isChecked()) {
                arrTypeId.add(mData.get(i).getTypeID());
            }
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, TypeBean item, final int position) {

        holder.checkBox.setText(item.getTypeName());

        if (item.isChecked()) {
            holder.checkImg.setVisibility(View.VISIBLE);
        } else {
            holder.checkImg.setVisibility(View.GONE);
        }
        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                if (0 == position) { // 全部按钮被选中情况
                    if (mData.get(position).isChecked()) { // 选中状态情况
                        mData.get(position).setChecked(false);
                    } else { // 未选中状态情况
                        mData.get(position).setChecked(true);
                        for (int i = 0; i < mData.size(); i++) {
                            if (i > 0) {
                                mData.get(i).setChecked(false);
                            }
                        }
                    }
                } else { // 其他项目
                    if (mData.get(0).isChecked()) {
                        mData.get(0).setChecked(false);
                    }
                    if (mData.get(position).isChecked()) { // 其他已被选中情况
                        mData.get(position).setChecked(false);
                    } else { // 其他未被选中情况
                        mData.get(position).setChecked(true);
                    }
                }

                notifyDataSetChanged();
                arrTypeId.clear();
                for (int i = 0; i < mData.size(); i++) {
                    if (mData.get(i).isChecked()) {
                        arrTypeId.add(mData.get(i).getTypeID());
                    }
                }
                Message msg = new Message();
                msg.what = 1;
                msg.obj = arrTypeId;
                mHandler.sendMessage(msg);
            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_select_child;
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
