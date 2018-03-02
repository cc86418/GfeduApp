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

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jun.courseinfo.bean.AddressBean;
import jc.cici.android.R;

import static cn.jun.courseinfo.course_adapter.J_RecyclerAdapter.J_msgAddressClick;
import static cn.jun.courseinfo.course_adapter.J_RecyclerAdapter.address_maps;


public class J_AddressRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mCtx;
    //    private ArrayList<AddressBean> mData;
    private Handler mHandler;

//    private Map<Integer, ArrayList<AddressBean>> address_maps = new HashMap<>();
    private int parent_pos;
    private int chlid_pos;
    private RecyclerView.ViewHolder mHolder;
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


    public J_AddressRecyclerAdapter(Context context, Map<Integer, ArrayList<AddressBean>> address_maps, Handler handler, int parent_pos) {
        this.mCtx = context;
//        this.address_maps = address_maps;
        this.mHandler = handler;
        this.parent_pos = parent_pos;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.course_select_child, parent, false);

        return new MyHolder(layout);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        AddressBean item = address_maps.get(parent_pos).get(position);
        chlid_pos = position;
        mHolder = holder;
        Gson s = new Gson();
//        Log.i("上课地点刷新 -- > ", "" + s.toJson(address_maps).toString());
        ((MyHolder) holder).checkBox.setText(item.getTypeName());
        if (item.isChecked()) {
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
//                if (buttonInterface != null) {
//                  接口实例化后的而对象，调用重写后的方法
                buttonInterface.onclick(buttonView, parent_pos, position);
//                }
                if (address_maps.get(parent_pos).get(position).isTypeSelect()) {
                    address_maps.get(parent_pos).get(position).setTypeSelect(false);
                    J_msgAddressClick = false;
                    notifyItemChanged(position);
                } else {
                    address_maps.get(parent_pos).get(position).setTypeSelect(true);
                    notifyItemChanged(position);
                    for (int i = 0; i < address_maps.get(parent_pos).size(); i++) {
                        if (i != position) {
                            address_maps.get(parent_pos).get(i).setTypeSelect(false);
                            notifyItemChanged(i);
                        }
                    }
                }

                Message msg2 = new Message();
                msg2.what = -1;
                msg2.obj = parent_pos;
                mHandler.sendMessage(msg2);

                Message msg3 = new Message();
                msg3.what = -2;
                msg3.obj = chlid_pos;
                mHandler.sendMessage(msg3);

                Message msg = new Message();
                msg.what = 3;
                msg.obj = address_maps;
                mHandler.sendMessage(msg);




            }
        });

    }


    @Override
    public int getItemCount() {
        return address_maps.get(parent_pos).size();
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
