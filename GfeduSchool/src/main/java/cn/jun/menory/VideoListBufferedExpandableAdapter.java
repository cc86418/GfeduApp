package cn.jun.menory;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.jun.menory.bean.VideoClassStageBean;
import cn.jun.menory.bean.VideoItemBean;
import jc.cici.android.R;

public class VideoListBufferedExpandableAdapter extends BaseExpandableListAdapter {
    private Activity ctx;
    private ArrayList<VideoClassStageBean> mList;
    private String Type;
    private LayoutInflater mInflater;
    private boolean setEditMode = false;
    private OnAllCheckListener onAllCheckListener;

    public VideoListBufferedExpandableAdapter(Activity ctx, ArrayList<VideoClassStageBean> VideoStageList) {
        this.ctx = ctx;
        this.mList = VideoStageList;
        mInflater = LayoutInflater.from(ctx);
    }

    public void setEditMode(boolean editMode) {
        this.setEditMode = editMode;
    }



    // 设置选中所有的监听事件
    public void setOnAllCheckListener(OnAllCheckListener listener) {
        onAllCheckListener = listener;
    }

    public interface OnAllCheckListener {
        void onAllCheck(boolean allCheck);
    }

    // 监测是否选中所有
    private void detectIfCheckAll(int groupPosition) {
        if (mList.get(groupPosition).getItemBean() == null) {
            return;
        }
        boolean allCheck = true;
        for (VideoItemBean item : mList.get(groupPosition).getItemBean() ) {
            if (!item.checked) {
                allCheck = false;
                break;
            }
        }
        if (onAllCheckListener != null) {
            onAllCheckListener.onAllCheck(allCheck);
        }
    }

    class ChildViewHolder {
        private TextView LessonName;
        private TextView SubjectName;
        private ImageView StudyState;
        private ImageView ivLeft;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getItemBean().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;
//        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.expandvideo_child_items, parent,
                    false);
            holder = new ChildViewHolder();
            holder.LessonName = (TextView) convertView.findViewById(R.id.tv_lesson_name);
            holder.SubjectName = (TextView) convertView.findViewById(R.id.tv_subjectname);
            holder.StudyState = (ImageView) convertView.findViewById(R.id.item_state_study);
            holder.ivLeft = (ImageView) convertView.findViewById(R.id.iv_left);
//            convertView.setTag(holder);
//        }else{
//            holder = (ChildViewHolder) convertView.getTag();
//        }
        String Lesson_Str = mList.get(groupPosition).getItemBean().get(childPosition).lessonname;
        String Subject_Str = mList.get(groupPosition).getItemBean().get(childPosition).subjectname;

        holder.LessonName.setText(Lesson_Str);
        holder.SubjectName.setText("属于第一章: " + Subject_Str);
        Log.i("setEditMode ======== > ",""+setEditMode);

        if (setEditMode) {
//            holder.ivLeft.setVisibility(View.VISIBLE);
            holder.ivLeft.setImageResource(mList.get(groupPosition).getItemBean().get(childPosition).checked ? R.drawable.expand_btn_xuanzhong
                    : R.drawable.expand_btn_weixuanzhong);

            holder.ivLeft.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mList.get(groupPosition).getItemBean().get(childPosition).checked = !mList.get(groupPosition).getItemBean().get(childPosition).checked;
                    holder.ivLeft.setImageResource(mList.get(groupPosition).getItemBean().get(childPosition).checked ? R.drawable.expand_btn_xuanzhong
                            : R.drawable.expand_btn_weixuanzhong);
                    detectIfCheckAll(groupPosition);
                }
            });
        }

        return convertView;
    }

    @Override
    public int getChildrenCount(int postion) {
        if (mList.get(postion).getItemBean() != null) {
            return mList.get(postion).getItemBean().size();
        } else {
            return 0;
        }

    }

    @Override
    public Object getGroup(int postion) {
        return mList.get(postion);
    }

    @Override
    public int getGroupCount() {
        return mList.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        // 标题
        TextView tv_title;
        // 展开收缩Icon
        ImageView jiantou;
        if (null == convertView) {
            convertView = mInflater.inflate(
                    R.layout.expandvideo_group_items, null);
        }
        tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        String title_Str = mList.get(groupPosition).getStageName();
        tv_title.setText(title_Str);
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public boolean isChildSelectable(int arg0, int arg1) {
        return true;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }
}
