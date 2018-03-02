package jc.cici.android.atom.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;

import java.util.ArrayList;

import jc.cici.android.R;
import jc.cici.android.atom.bean.ExamChoseBean;

/**
 * 题库阶段适配器
 * Created by atom on 2017/12/26.
 */

public class StageAdapter extends BaseAdapter {

    private Context mCtx;
    private ArrayList<ExamChoseBean.Stage> mData;

    public StageAdapter(Context context, ArrayList<ExamChoseBean.Stage> data) {
        this.mCtx = context;
        this.mData = data;
    }

    @Override
    public int getCount() {
        return mData.size();
    }

    @Override
    public Object getItem(int position) {
        return mData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    class ViewHolder {
        private Button item_know_btn;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            convertView = LayoutInflater.from(mCtx).inflate(R.layout.item_exam_stage, parent, false);
            holder = new ViewHolder();
            // 获取按钮id
            holder.item_know_btn = (Button) convertView.findViewById(R.id.item_know_btn);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        holder.item_know_btn.setText(mData.get(position).getStageName());
        if (mData.get(position).isSelected()) {
            holder.item_know_btn.setBackgroundResource(R.drawable.login_button_bg);
            holder.item_know_btn.setTextColor(Color.parseColor("#ffffff"));
        } else {
            holder.item_know_btn.setBackgroundResource(R.drawable.button_register_bg);
            holder.item_know_btn.setTextColor(Color.parseColor("#333333"));
        }
        return convertView;
    }
//    public void setOnItemClikListener(GridView gridView) {
//        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    mData.get(position).setSelected(true);
//                }
//        });
//    }
}
