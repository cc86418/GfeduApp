package cn.jun.mysetting;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jun.bean.ClassScheduleBean;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;


public class SZPY_RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mCtx;
//    private ArrayList<ClassScheduleBean> mlist;
private ArrayList<cn.jun.bean.ClassScheduleBean.ClassScheduleList> mlist;
    private ButtonInterface buttonInterface;
    private HttpUtils httpUtils = new HttpUtils();

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


    public SZPY_RecyclerAdapter(Context context, ArrayList<cn.jun.bean.ClassScheduleBean.ClassScheduleList> mlist) {
        this.mCtx = context;
        this.mlist = mlist;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.szpy_child, parent, false);

        return new MyHolder(layout);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        final ClassScheduleBean.ClassScheduleList item= mlist.get(position);
//        final ClassScheduleBean.Body.ClassScheduleList item = mlist.get(0).getBody().getClassScheduleList().get(position);
        //头像
        Glide.with(mCtx)
                .load(item.getLecturerHead())
                .placeholder(R.drawable.pic_kong)
                .into(((MyHolder) holder).touxiang);
        //出勤
//        ((MyHolder) holder).chuqing
        //授课老师
        ((MyHolder) holder).tx_1.setText("授课老师 : " + item.getLecturerName());
        //班级名称
        ((MyHolder) holder).tx_2.setText(item.getChildClassTypeName());
        //阶段
        ((MyHolder) holder).tx_3.setText("");
        //内容
        ((MyHolder) holder).tx_4.setText("上课内容: "+item.getCoursesContent());
        //时间
        ((MyHolder) holder).tx_5.setText(item.getCoursesTime());
        //评一下
        ((MyHolder) holder).btn_pyx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(httpUtils.isNetworkConnected(mCtx)){
                    Intent intent = new Intent(mCtx, ShiZiPingYi_Edit.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("ClassScheduleID",item.getClassScheduleID());
                    bundle.putString("teach", item.getLecturerName());
                    bundle.putString("head", item.getLecturerHead());
                    intent.putExtras(bundle);
                    mCtx.startActivity(intent);
                }else{
                    Toast.makeText(mCtx,"网络中断",Toast.LENGTH_LONG);
                }

            }
        });


    }


    @Override
    public int getItemCount() {
//        return mlist.get(0).getBody().getClassScheduleList().size();
        return mlist.size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        //头像
        @BindView(R.id.touxiang)
        ImageView touxiang;
        //出勤
        @BindView(R.id.chuqing)
        ImageView chuqing;
        //授课老师
        @BindView(R.id.tx_1)
        TextView tx_1;
        //班级名称
        @BindView(R.id.tx_2)
        TextView tx_2;
        //阶段
        @BindView(R.id.tx_3)
        TextView tx_3;
        //内容
        @BindView(R.id.tx_4)
        TextView tx_4;
        //时间
        @BindView(R.id.tx_5)
        TextView tx_5;
        //评一下
        @BindView(R.id.btn_pyx)
        Button btn_pyx;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
