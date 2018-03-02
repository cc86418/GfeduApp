package cn.jun.courseinfo.adapter;


import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.jun.bean.ClassOutLineBean;
import jc.cici.android.R;

public class ChildAdapter extends BaseExpandableListAdapter {

    //上下文
    private Activity activity;
    //二级List
    private ArrayList<ClassOutLineBean.Body.OutLineList.LevelTwo> mListLevel;

    public ChildAdapter(Activity context, ArrayList<ClassOutLineBean.Body.OutLineList.LevelTwo> childs) {
        this.activity = context;
        this.mListLevel = childs;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return mListLevel.get(groupPosition).getList() == null ? 0 : mListLevel.get(groupPosition).getList().size();
    }

    @Override
    public String getChild(int groupPosition, int childPosition) {
        if (mListLevel.get(groupPosition).getList() != null && mListLevel.get(groupPosition).getList().size() > 0) {
            return mListLevel.get(groupPosition).getList().get(childPosition).getLevel_ShowName();
        } else {
            return null;
        }
//        if (mChilds.get(groupPosition).getChildNames() != null
//                && mChilds.get(groupPosition).getChildNames().size() > 0)
//            return mChilds.get(groupPosition).getChildNames()
//                    .get(childPosition).toString();
//        return null;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition,
                             boolean isExpanded, View convertView, ViewGroup parent) {
        ChildHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.alone_level_child_items, null);
            holder = new ChildHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (ChildHolder) convertView.getTag();
        }
        holder.update(getChild(groupPosition, childPosition));
        return convertView;
    }

    class ChildHolder {
        private TextView Level_ShowName;

        public ChildHolder(View v) {
            Level_ShowName = (TextView) v.findViewById(R.id.Level_ShowName);
        }

        public void update(String str) {
            Level_ShowName.setText(str);
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListLevel.get(groupPosition);
//        if (mChilds != null && mChilds.size() > 0)
//            return mChilds.get(groupPosition);
//        return null;
    }

    @Override
    public int getGroupCount() {
//        return mListLevel.size();
        return mListLevel != null ? mListLevel.size() : 0;
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        GroupHolder holder = null;
        if (convertView == null) {
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.alone_level_group_items, null);
            holder = new GroupHolder(convertView);
            convertView.setTag(holder);
        } else {
            holder = (GroupHolder) convertView.getTag();
        }
        holder.update(mListLevel.get(groupPosition),isExpanded);
//        holder.update(mChilds.get(groupPosition));
        return convertView;
    }


    class GroupHolder {
        private TextView tv_title;
        // 展开收缩Icon
        private ImageView jiantou;
        public GroupHolder(View v) {
            jiantou = (ImageView) v.findViewById(R.id.jiantou);
            tv_title = (TextView) v.findViewById(R.id.tv_title);
        }

        public void update(ClassOutLineBean.Body.OutLineList.LevelTwo model,boolean isExpanded) {
            tv_title.setText(model.getLevelName());
            if (isExpanded) {
                jiantou.setBackgroundResource(R.drawable.expandable_level_zhankai);
            } else {
                jiantou.setBackgroundResource(R.drawable.expandable_level_sousuo);
            }

        }
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {

        return true;
    }

}
