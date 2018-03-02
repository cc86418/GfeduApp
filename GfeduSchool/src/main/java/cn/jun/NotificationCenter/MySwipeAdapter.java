package cn.jun.NotificationCenter;


import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.jun.bean.GetNotifybyIsRead;
import jc.cici.android.R;


public class MySwipeAdapter extends BaseAdapter {
    private Activity context;
    private ArrayList<GetNotifybyIsRead> mList;

    public MySwipeAdapter(Activity context, ArrayList<GetNotifybyIsRead> mList) {
        this.context = context;
        this.mList = mList;
    }

    @Override
    public int getCount() {
        return mList.get(0).getBody().getUserInformList().size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.notifi_item, null);
            new ViewHolder(convertView);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        String Inform_Content = mList.get(0).getBody().getUserInformList().get(position).getInform_Content();
        holder.msg_tv.setText(Inform_Content);
        int Inform_Type = mList.get(0).getBody().getUserInformList().get(position).getInform_Type();
        //通知类型 1.订单通知   2.课程通知   3.系统通知   4.学习互动
        if (1==Inform_Type) {
            holder.msg_type.setText("订单通知");
            holder.iv_icon.setBackgroundResource(R.drawable.dingdantixing_icon);
        }else if(2==Inform_Type){
            holder.msg_type.setText("课程通知");
            holder.iv_icon.setBackgroundResource(R.drawable.kechengtixing_icon);
        }else if(3==Inform_Type){
            holder.msg_type.setText("系统通知");
            holder.iv_icon.setBackgroundResource(R.drawable.xitongtongzhi_icon);
        }else if(4==Inform_Type){
            holder.msg_type.setText("学习互动");
            holder.iv_icon.setBackgroundResource(R.drawable.xuexihudong_icon);
        }
        //日期
        String Inform_AddTime = mList.get(0).getBody().getUserInformList().get(position).getInform_AddTime();
        holder.msg_time.setText(Inform_AddTime);

        return convertView;
    }


    class ViewHolder {
        ImageView iv_icon;
        TextView msg_type;
        TextView msg_tv;
        TextView msg_time;

        public ViewHolder(View view) {
            iv_icon = (ImageView) view.findViewById(R.id.iv_icon);
            msg_type = (TextView) view.findViewById(R.id.msg_type);
            msg_tv = (TextView) view.findViewById(R.id.msg_tv);
            msg_time = (TextView) view.findViewById(R.id.msg_time);
            view.setTag(this);
        }
    }
}
