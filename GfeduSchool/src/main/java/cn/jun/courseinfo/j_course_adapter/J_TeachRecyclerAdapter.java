package cn.jun.courseinfo.j_course_adapter;


import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jun.courseinfo.bean.TeachBean;
import jc.cici.android.R;

import static cn.jun.courseinfo.course_adapter.J_RecyclerAdapter.J_msgTeachClick;


public class J_TeachRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mCtx;
    private Handler mHandler;

    private int parent_pos;
    private Map<Integer, ArrayList<TeachBean>> tecah_maps = new HashMap<>();

    //授课类型
    private int Link_BuyType;

    private ButtonInterface buttonInterface;

    /**
     * 按钮点击事件需要的方法
     */
    public void buttonSetOnclick(ButtonInterface buttonInterface) {
        this.buttonInterface = buttonInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ButtonInterface {
        public void onclick(View view, int parent_pos, int chlid_pos);

    }


    public J_TeachRecyclerAdapter(Context context, Map<Integer, ArrayList<TeachBean>> tecah_maps, Handler handler, int parent_pos, int Link_BuyType) {
        this.mCtx = context;
        this.tecah_maps = tecah_maps;
        this.mHandler = handler;
        this.parent_pos = parent_pos;
        this.Link_BuyType = Link_BuyType;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_select_child, parent, false);
        return new MyHolder(layout);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
//        Log.i("授课方式刷新 -- > ", "onBindViewHolder");
        TeachBean item = tecah_maps.get(parent_pos).get(position);
        ((MyHolder) holder).checkBox.setText(item.getTypeName());
        if (item.isChecked()) {
//            Log.i("isChecked "," --- isChecked ");
            ((MyHolder) holder).checkBox.setClickable(true);
            ((MyHolder) holder).checkBox.setBackgroundResource(R.drawable.yuan_background_all_hui);
            ((MyHolder) holder).checkBox.setTextColor(Color.parseColor("#333333"));

            if (item.isTypeSelect()) {
                ((MyHolder) holder).checkBox.setBackgroundResource(R.drawable.yuan_background_all);
                ((MyHolder) holder).checkBox.setTextColor(Color.parseColor("#ffffff"));
            } else {
                ((MyHolder) holder).checkBox.setBackgroundResource(R.drawable.yuan_background_all_hui);
                ((MyHolder) holder).checkBox.setTextColor(Color.parseColor("#333333"));
            }
        } else {
            ((MyHolder) holder).checkBox.setClickable(false);
            ((MyHolder) holder).checkBox.setBackgroundResource(R.drawable.yuan_background_all_hui);
            ((MyHolder) holder).checkBox.setTextColor(Color.parseColor("#b1b1b1"));
        }
        ((MyHolder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 接口实例化后的而对象，调用重写后的方法
                buttonInterface.onclick(buttonView, parent_pos, position);
                if (0 == Link_BuyType) {//自选才可以点击
                    if (tecah_maps.get(parent_pos).get(position).isTypeSelect()) {
                        tecah_maps.get(parent_pos).get(position).setTypeSelect(false);
                        J_msgTeachClick = false;
                        notifyItemChanged(position);
                    } else {
                        tecah_maps.get(parent_pos).get(position).setTypeSelect(true);
                        notifyItemChanged(position);
                        for (int i = 0; i < tecah_maps.get(parent_pos).size(); i++) {
                            if (i != position) {
                                tecah_maps.get(parent_pos).get(i).setTypeSelect(false);
                                notifyItemChanged(i);
                            }
                        }
                    }
                } else {

                }

                Message msg2 = new Message();
                msg2.what = -1;
                msg2.obj = parent_pos;
                mHandler.sendMessage(msg2);

                Message msg = new Message();
                msg.what = 0;
                msg.obj = tecah_maps;
                mHandler.sendMessage(msg);


            }
        });
    }

    @Override
    public int getItemCount() {
        return tecah_maps.get(parent_pos).size();
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
