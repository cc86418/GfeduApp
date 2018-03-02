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
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jun.courseinfo.bean.ExameDateBean;
import jc.cici.android.R;

import static cn.jun.courseinfo.course_adapter.J_RecyclerAdapter.J_msgExameClick;
import static cn.jun.courseinfo.course_adapter.J_RecyclerAdapter.Time_msgExameClick;


public class J_ExameDateRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mCtx;
    //    private ArrayList<ExameDateBean> mData;
    private Handler mHandler;
    private int mSelectedPos = -1;
    private int parent_pos;
    int Link_BuyType;
    private Map<Integer, ArrayList<ExameDateBean>> exame_maps = new HashMap<>();

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

    public J_ExameDateRecyclerAdapter(Context context, Map<Integer, ArrayList<ExameDateBean>> exame_maps, Handler handler, int parent_pos, int Link_BuyType) {
        this.mCtx = context;
        this.exame_maps = exame_maps;
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
        ExameDateBean item = exame_maps.get(parent_pos).get(position);
        Gson s = new Gson();
//        Log.i("考试时间刷新 -- > ", "" + s.toJson(exame_maps).toString());
//        Log.i("Link_BuyType -- > ", "" + Link_BuyType);
        ((MyHolder) holder).checkBox.setText(item.getTypeName());
        if (item.isChecked()) {
//            Log.i("isChecked ", " --- isChecked ");
            ((MyHolder) holder).checkBox.setClickable(true);
            ((MyHolder) holder).checkBox.setBackgroundResource(R.drawable.yuan_background_all_hui);
            ((MyHolder) holder).checkBox.setTextColor(Color.parseColor("#333333"));

            if (item.isTypeSelect()) {
//                Log.i("isTypeSelect ", " --- isTypeSelect ");
                ((MyHolder) holder).checkBox.setBackgroundResource(R.drawable.yuan_background_all);
                ((MyHolder) holder).checkBox.setTextColor(Color.parseColor("#ffffff"));
            } else {
//                Log.i("isTypeSelect ", " --- on isTypeSelect ");
                ((MyHolder) holder).checkBox.setBackgroundResource(R.drawable.yuan_background_all_hui);
                ((MyHolder) holder).checkBox.setTextColor(Color.parseColor("#333333"));
            }
        } else {
//            Log.i("isChecked ", " --- no isChecked ");
//            Log.i("Link_BuyType ", " ---  " + Link_BuyType);
            if (2 == Link_BuyType) {
                ((MyHolder) holder).checkBox.setClickable(false);
                ((MyHolder) holder).checkBox.setBackgroundResource(R.drawable.yuan_background_all_hui);
                ((MyHolder) holder).checkBox.setTextColor(Color.parseColor("#b1b1b1"));
            }else{
                ((MyHolder) holder).checkBox.setClickable(false);
                ((MyHolder) holder).checkBox.setBackgroundResource(R.drawable.yuan_background_all_hui);
                ((MyHolder) holder).checkBox.setTextColor(Color.parseColor("#b1b1b1"));
            }

        }

        ((MyHolder) holder).checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // 接口实例化后的而对象，调用重写后的方法
                buttonInterface.onclick(buttonView, parent_pos, position);
                if (exame_maps.get(parent_pos).get(position).isTypeSelect()) {
                    exame_maps.get(parent_pos).get(position).setTypeSelect(false);
                    J_msgExameClick = false;
                    Time_msgExameClick = false;
                    notifyItemChanged(position);

//                    Message return_msg = new Message();
//                    return_msg.what = 5;
//                    return_msg.obj = parent_pos;
//                    mHandler.sendMessage(return_msg);

                } else {
//                    Message return_msg = new Message();
//                    return_msg.what = 5;
//                    return_msg.obj = parent_pos;
//                    mHandler.sendMessage(return_msg);

                    Time_msgExameClick = true;
                    exame_maps.get(parent_pos).get(position).setTypeSelect(true);
                    notifyItemChanged(position);
                    for (int i = 0; i < exame_maps.get(parent_pos).size(); i++) {
                        if (i != position) {
                            exame_maps.get(parent_pos).get(i).setTypeSelect(false);
                            notifyItemChanged(i);
                        }
                    }
                }
                Message msg2 = new Message();
                msg2.what = -1;
                msg2.obj = parent_pos;
                mHandler.sendMessage(msg2);


                Message msg = new Message();
                msg.what = 1;
                msg.obj = exame_maps;
                mHandler.sendMessage(msg);


            }
        });
    }


    @Override
    public int getItemCount() {
        return exame_maps.get(parent_pos).size();
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
