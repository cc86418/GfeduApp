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
import jc.cici.android.atom.bean.ChildEntity;

/**
 * Created by User on 2017/8/10.
 */

public class SubjectRecyclerViewAdapter extends BaseRecycleerAdapter<ChildEntity, SubjectRecyclerViewAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<ChildEntity> mData;
    private Handler mHandler;
    private ArrayList<Integer> projectId = new ArrayList<>();

    public SubjectRecyclerViewAdapter(Context context, List<ChildEntity> items, Handler handler) {
        super(context, items);
        this.mCtx = context;
        this.mData = (ArrayList<ChildEntity>) items;
        this.mHandler = handler;
        // 初始化数组
        initArray();
    }

    private void initArray() {
        for (int i = 0; i < mData.size(); i++) {
            if (mData.get(i).ismChecked()) {
                projectId.add(mData.get(i).getProjectId());
            }
        }
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(MyHolder holder, ChildEntity item, final int position) {
        holder.checkBox.setText(item.getProjectName());
        if (item.ismChecked()) {
            holder.checkImg.setVisibility(View.VISIBLE);
        } else {
            holder.checkImg.setVisibility(View.GONE);
        }

        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (0 == position) { // 全部按钮被选中情况
                    if (mData.get(position).ismChecked()) { // 选中状态情况
                        mData.get(position).setmChecked(false);
                    } else { // 未选中状态情况
                        mData.get(position).setmChecked(true);
                        for (int i = 0; i < mData.size(); i++) {
                            if (i > 0) {
                                mData.get(i).setmChecked(false);
                            }
                        }
                    }
                } else { // 其他项目
                    if (mData.get(0).ismChecked()) {
                        mData.get(0).setmChecked(false);
                    }
                    if (mData.get(position).ismChecked()) { // 其他已被选中情况
                        mData.get(position).setmChecked(false);
                    } else { // 其他未被选中情况
                        mData.get(position).setmChecked(true);
                    }
                }
                projectId.clear();
                for (int i = 0; i < mData.size(); i++) {
                    if (mData.get(i).ismChecked()) {
                        projectId.add(mData.get(i).getProjectId());
                    }
                }
                Message msg = new Message();
                msg.what = 2;
                msg.obj = projectId;
                mHandler.sendMessage(msg);
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
