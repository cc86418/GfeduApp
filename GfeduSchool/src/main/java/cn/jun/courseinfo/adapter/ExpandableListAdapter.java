package cn.jun.courseinfo.adapter;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.jun.bean.ClassOutLineBean;
import cn.jun.courseinfo.ui.MyScrollView;
import cn.jun.view.ScrollViewExpandableListView;
import jc.cici.android.R;

import static jc.cici.android.R.id.myList_Level;

public class ExpandableListAdapter extends BaseExpandableListAdapter {
    //上下文
    private Activity activity;
    //一级List
    private ArrayList<ClassOutLineBean> mList;
    private OnChildTreeViewClickListener mTreeViewClickListener;// 点击子ExpandableListView子项的监听
    private ArrayList<ClassOutLineBean.Body.OutLineList.LevelTwo> mListLevel = new ArrayList<>();
    private ExpandableListView eListView;
    //    private ScrollViewExpandableListViewLevel
    private ExpandableListLeveLAdapter adapterLevel;

    public ExpandableListAdapter(Activity activity, ArrayList<ClassOutLineBean> mList) {
        this.activity = activity;
        this.mList = mList;
        Gson s = new Gson();
        Log.i("mList == ", "" + s.toJson(mList).toString());
    }

    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }

    @Override
    public int getGroupCount() {
        if (!"".equals(mList) && null != mList) {
            return mList.get(0).getBody().getOutLineList().size();
        } else {
//            Log.i("return", " 0000000 ");
            return 0;
        }

//        return mList.get(0).getBody().getOutLineList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(0).getBody().getOutLineList() == null ? 0 : mList.get(0).getBody().getOutLineList().get(groupPosition);
//        return mList.get(0).getBody().getOutLineList().get(groupPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
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

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        // 父类标题
        TextView tv_title;
        // 展开收缩Icon
        ImageView jiantou;
        if (null == convertView) {
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.alone_group_items, null);
        }
        tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        jiantou = (ImageView) convertView.findViewById(R.id.jiantou);
        if (!"".equals(mList) && null != mList) {
            String title = mList.get(0).getBody().getOutLineList().get(groupPosition).getStageName();
            title = title.replace("&nbsp;", " ");
            tv_title.setText(title);
            if (isExpanded) {
                jiantou.setBackgroundResource(R.drawable.btn_zhankai_huise_icon);
            } else {
                jiantou.setBackgroundResource(R.drawable.btn_shousu_huise_icon);
            }
        }

        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return 1;
    }

    @Override
    public long getCombinedChildId(long arg0, long arg1) {
        return 0;
    }

    @Override
    public long getCombinedGroupId(long arg0) {
        return 0;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(0).getBody().getOutLineList().get(groupPosition).getLevelTwo() == null ? 0 : mList.get(0).getBody().getOutLineList().get(groupPosition).getLevelTwo().get(childPosition);
//        return mList.get(0).getBody().getOutLineList().get(groupPosition).getLevelTwo().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    class ChildViewHolder {
        //        private ScrollViewExpandableListViewLevel myList_Level;
        private ScrollViewExpandableListView myList_Level;
        private MyScrollView childScrollview;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;
        convertView = LayoutInflater.from(activity).inflate(R.layout.alone_child_items,
                null);
        holder = new ChildViewHolder();
        holder.myList_Level = (ScrollViewExpandableListView) convertView
                .findViewById(myList_Level);
        holder.myList_Level.setDivider(null);
        holder.myList_Level.setGroupIndicator(null);
        if (!"".equals(mList) && null != mList) {
            mListLevel = mList.get(0).getBody().getOutLineList().get(groupPosition).getLevelTwo();
            if (!"".equals(mListLevel) && null != mListLevel) {
                int ChildClassTypeType = mList.get(0).getBody().getOutLineList().get(groupPosition).getChildClassTypeType();
                int classId =  mList.get(0).getBody().getClassId();
                int ChildClassTypeId = mList.get(0).getBody().getOutLineList().get(groupPosition).getChildClassTypeId();
                int ClassTypeId = mList.get(0).getBody().getClassTypeId();
                adapterLevel = new ExpandableListLeveLAdapter(activity, mListLevel, ChildClassTypeType,classId,ClassTypeId,ChildClassTypeId);
                holder.myList_Level.setAdapter(adapterLevel);
                holder.myList_Level.expandGroup(0);
                //默认展开点击不能收缩
//                for (int i = 0; i < mListLevel.size(); i++) {
//                    holder.myList_Level.expandGroup(i);
//                }
//                holder.myList_Level.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
//                    @Override
//                    public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
//                        return true;
//                    }
//                });
                //默认展开点击不能收缩

            }
        }


        //点击子ExpandableListView子项时，调用回调接口
        holder.myList_Level.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView arg0, View arg1,
                                        int groupIndex, int childIndex, long arg4) {
                if (mTreeViewClickListener != null) {
                    mTreeViewClickListener.onClickPosition(groupPosition,
                            childPosition, childIndex);
                }
                return false;
            }
        });


        return convertView;
    }


//    @Override
//    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
//        mListLevel = mList.get(0).getBody().getOutLineList().get(groupPosition).getLevelTwo();
//        eListView = getExpandableListView();
//        final ChildAdapter childAdapter = new ChildAdapter(activity,
//                mListLevel);
//        eListView.setAdapter(childAdapter);
//        //点击子ExpandableListView子项时，调用回调接口
//        eListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//            @Override
//            public boolean onChildClick(ExpandableListView arg0, View arg1,
//                                        int groupIndex, int childIndex, long arg4) {
//                if (mTreeViewClickListener != null) {
//                    mTreeViewClickListener.onClickPosition(groupPosition,
//                            childPosition, childIndex);
//                }
//                return false;
//            }
//        });
//        //子ExpandableListView展开时，因为group只有一项，所以子ExpandableListView的总高度=
//        //（子ExpandableListView的child数量 + 1 ）* 每一项的高度
//        eListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
//            @Override
//            public void onGroupExpand(int groupPosition) {
//                Log.i("子类数量 -- ", "" + mListLevel.get(groupPosition).getList().size());
//                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT,
//                        (mListLevel.get(groupPosition).getList().size() + 1)
//                                * (int) activity.getResources().getDimension(
//                                R.dimen.parent_expandable_list_height));
//                eListView.setLayoutParams(lp);
//
//
//            }
//        });
//        //子ExpandableListView关闭时，此时只剩下group这一项，
//        // 所以子ExpandableListView的总高度即为一项的高度
//        eListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {
//            @Override
//            public void onGroupCollapse(int groupPosition) {
//                AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
//                        ViewGroup.LayoutParams.MATCH_PARENT, (int) activity
//                        .getResources().getDimension(
//                                R.dimen.parent_expandable_list_height));
//                eListView.setLayoutParams(lp);
//            }
//        });
//        return eListView;
//
//
//    }

    public ExpandableListView getExpandableListView() {
        ExpandableListView mExpandableListView = new ExpandableListView(
                activity);
        AbsListView.LayoutParams lp;
        if (!"".equals(mListLevel) && null != mListLevel) {
            lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, (int) activity
                    .getResources().getDimension(
                            R.dimen.parent_expandable_list_height));
        } else {
            lp = new AbsListView.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, (int) activity
                    .getResources().getDimension(
                            R.dimen.null_parent_expandable_list_height));
        }
        mExpandableListView.setLayoutParams(lp);
        mExpandableListView.setDividerHeight(0);// 取消group项的分割线
        mExpandableListView.setChildDivider(null);// 取消child项的分割线
        mExpandableListView.setGroupIndicator(null);// 取消展开折叠的指示图标
        return mExpandableListView;
    }

    //设置点击子ExpandableListView子项的监听
    public void setOnChildTreeViewClickListener(
            OnChildTreeViewClickListener treeViewClickListener) {
        this.mTreeViewClickListener = treeViewClickListener;
    }

    //点击子ExpandableListView子项的回调接口
    public interface OnChildTreeViewClickListener {
        void onClickPosition(int parentPosition, int groupPosition,
                             int childPosition);
    }

}
