package cn.jun.menory;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.jun.menory.bean.VideoItemBean;
import cn.jun.menory.service.ViewHolder;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;

public class VideoListBufferedAdapter extends BaseAdapter {
    private List<VideoItemBean> list;
    private boolean setEditMode = false;
    private Context context;
    private LayoutInflater layoutInflater;
    //    private OnAllCheckListener onAllCheckListener;
    private HttpUtils httpUtils = new HttpUtils();



    public VideoListBufferedAdapter(Context ctx) {
        this.context = ctx;
        layoutInflater = LayoutInflater.from(ctx);
    }

    public void setEditMode(boolean editMode) {
        this.setEditMode = editMode;


    }

    public List<VideoItemBean> getData() {
        return list;
    }

    public void setData(List<VideoItemBean> list) {
        this.list = list;
        Log.i("bad-boy", "list: " + getCount());
    }

    @Override
    public int getCount() {
        return list == null ? 0 : list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder vh;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.item_buffer_two, parent,
                    false);
            vh = new ViewHolder(context, convertView, parent, position);
            convertView.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }
        final VideoItemBean item = list.get(position);

        TextView tvName = vh.getView(R.id.tv_name);
        tvName.setText(item.lessonname.replaceAll("&nbsp;", " "));

//        TextView tvDesc = vh.getView(R.id.tv_desc);
//        tvDesc.setText(item.desc.replaceAll("&nbsp;", " "));

        final ImageView ivLeft = vh.getView(R.id.iv_left);
        ImageView ivRight = vh.getView(R.id.iv_right);
        if (setEditMode) {
            ivLeft.setImageResource(item.checked ? R.drawable.about_icon
                    : R.drawable.ic_camera);
            ivLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    item.checked = !item.checked;
                    ivLeft.setImageResource(item.checked ? R.drawable.about_icon
                            : R.drawable.ic_camera);

//                    detectIfCheckAll();
                }
            });

            ivRight.setVisibility(View.GONE);
        } else {
            ivLeft.setImageResource(R.mipmap.ic_launcher);
            ivLeft.setOnClickListener(null);
            ivRight.setVisibility(View.VISIBLE);
            ivRight.setImageResource(R.drawable.ic_play);
            ivRight.setTag(item.vid);
            ivRight.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    String vid = (String) v.getTag();
                    String lessinid = item.lessonid;
//                    String taskType = item.tasktype;
                    System.out.println("vid - " + vid);
                    if (vid != null) {
                        if (httpUtils.isNetworkConnected(context)) {// 有网络的情况跳三步走
                            System.out.println("有网络");
//                            System.out.println("taskType -- " + taskType);

                        } else {// 否则跳转全屏本地
                            System.out.println("无网络");

                        }

                    }
                }
            });
        }

        return convertView;
    }

    public void addItem(VideoItemBean video) {
        if (list == null) {
            list = new ArrayList<VideoItemBean>();
        }
        if (video != null) {
            list.add(video);
        }
    }
}
