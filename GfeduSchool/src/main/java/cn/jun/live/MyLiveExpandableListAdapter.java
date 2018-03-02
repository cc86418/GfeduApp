package cn.jun.live;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import cn.jun.bean.ClassOutLineBean;
import cn.jun.bean.GetMyLiveBean;
import jc.cici.android.R;


public class MyLiveExpandableListAdapter extends BaseExpandableListAdapter {
    //上下文
    private Activity activity;
    //一级List
    private ArrayList<GetMyLiveBean> mList;
    private OnChildTreeViewClickListener mTreeViewClickListener;// 点击子ExpandableListView子项的监听
    private ArrayList<ClassOutLineBean.Body.OutLineList.LevelTwo> mListLevel = new ArrayList<>();

    public MyLiveExpandableListAdapter(Activity activity, ArrayList<GetMyLiveBean> mList) {
        this.activity = activity;
        this.mList = mList;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }

    @Override
    public int getGroupCount() {
        if (!"".equals(mList) && null != mList) {
            return mList.get(0).getBody().getList().size();
        } else {
            return 0;
        }
//        return mList.get(0).getBody().getList().size();
//        return mList.get(0).getBody().getOutLineList().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(0).getBody().getList() == null ? 0 : mList.get(0).getBody().getList().get(groupPosition);
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
        Button tv_title;
        // 展开收缩Icon
        TextView jiantou;
        if (null == convertView) {
            convertView = LayoutInflater.from(activity).inflate(
                    R.layout.live_alone_group_items, null);
        }
        tv_title = (Button) convertView.findViewById(R.id.yuefen);
        jiantou = (TextView) convertView.findViewById(R.id.nianfen);
        //月
        int yue = mList.get(0).getBody().getList().get(groupPosition).getMonth();
        //年
        int nian = mList.get(0).getBody().getList().get(groupPosition).getYear();
        tv_title.setText(yue + "月");
        jiantou.setText(nian + "");
//        if (!"".equals(mList) && null != mList) {
//            String title = mList.get(0).getBody().getList().get(groupPosition).getStageName();
//            title = title.replace("&nbsp;", " ");
//            tv_title.setText(title);
//            if (isExpanded) {
//                jiantou.setBackgroundResource(R.drawable.btn_zhankai_huise_icon);
//            } else {
//                jiantou.setBackgroundResource(R.drawable.btn_shousu_huise_icon);
//            }
//        }

        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
//        return 1;
        return mList.get(0).getBody().getList().get(groupPosition).getScheduleList().size();
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
        return mList.get(0).getBody().getList().get(groupPosition).getScheduleList() == null ? 0 : mList.get(0).getBody().getList().get(groupPosition).getScheduleList().get(childPosition);
//        return mList.get(0).getBody().getOutLineList().get(groupPosition).getLevelTwo().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    class ChildViewHolder {
        private Button btn_riqi;
        private TextView content;
        private TextView time_tv;
        private ImageView zhibo_status;

    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;
        convertView = LayoutInflater.from(activity).inflate(R.layout.live_alone_child_items,
                null);
        holder = new ChildViewHolder();
        holder.btn_riqi = (Button) convertView
                .findViewById(R.id.btn_riqi);
        holder.content = (TextView) convertView
                .findViewById(R.id.content);
        holder.time_tv = (TextView) convertView
                .findViewById(R.id.time_tv);
        holder.zhibo_status = (ImageView) convertView
                .findViewById(R.id.zhibo_status);
        //标题
        String title = mList.get(0).getBody().getList().get(groupPosition).getScheduleList().get(childPosition).getCS_Name();
        //日期
        String date = mList.get(0).getBody().getList().get(groupPosition).getScheduleList().get(childPosition).getCS_DateShort();
        String[] date_s = date.split("-");

        //时间
        String CS_StartTime = mList.get(0).getBody().getList().get(groupPosition).getScheduleList().get(childPosition).getCS_StartTime();
        String CS_EndTime = mList.get(0).getBody().getList().get(groupPosition).getScheduleList().get(childPosition).getCS_EndTime();
        //直播是否开始
        int IsLiveBegin = mList.get(0).getBody().getList().get(groupPosition).getScheduleList().get(childPosition).getIsLiveBegin();
        //是否预约过 1：是 0：否
        int HasBook = mList.get(0).getBody().getList().get(groupPosition).getScheduleList().get(childPosition).getHasBook();

        holder.content.setText(title);
//        holder.btn_riqi.setText(date);
        holder.btn_riqi.setText(date_s[1]);
        holder.time_tv.setText(CS_StartTime + "-" + CS_EndTime);

        if (1 == IsLiveBegin) {
            holder.zhibo_status.setBackgroundResource(R.drawable.mylive_zhibozhong);
            holder.zhibo_status.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int scheduleId = mList.get(0).getBody().getList().get(groupPosition).getScheduleList().get(childPosition).getCS_PKID();
//                    Intent intent = new Intent(activity, LiveH5Activity.class);
                    Intent intent = new Intent(activity, LiveRoomActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("scheduleId", scheduleId);
                    bundle.putInt("classid", 0);
                    bundle.putInt("searchType", 0);
                    intent.putExtras(bundle);
                    activity.startActivity(intent);
                }
            });

        } else {
//            if (1 == HasBook) {
//                holder.zhibo_status.setBackgroundResource(R.drawable.mylive_quxiaoyuyue);
//            } else {
//                holder.zhibo_status.setBackgroundResource(R.drawable.mylive_weikaishi);
//            }
            holder.zhibo_status.setBackgroundResource(R.drawable.mylive_weikaishi);
        }

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
