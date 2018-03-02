package cn.jun.courseinfo.adapter;


import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.jun.bean.AddLiveBook;
import cn.jun.bean.ClassOutLineBean;
import cn.jun.bean.Const;
import cn.jun.courseinfo.polyvplayer.C_IjkVideoActicity;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;

import static jc.cici.android.R.id.shiting_textview;

public class ExpandableListLeveLAdapter extends BaseExpandableListAdapter {
    //上下文
    private Activity activity;
    //二级List
    private ArrayList<ClassOutLineBean.Body.OutLineList.LevelTwo> mListLevel;
    //类型
    private int ChildClassTypeType;
    private HttpUtils httpUtils = new HttpUtils();
    private AddLiveBook addLiveBook = new AddLiveBook();
    private int classId;
    private int ChildClassTypeId;
    private int ClassTypeId;
    private int ScheduleId;
    private int LevelId;

    public ExpandableListLeveLAdapter(Activity activity, ArrayList<ClassOutLineBean.Body.OutLineList.LevelTwo> mListLevel, int ChildClassTypeType, int classId, int ClassTypeId, int ChildClassTypeId) {
        this.activity = activity;
        this.mListLevel = mListLevel;
        this.ChildClassTypeType = ChildClassTypeType;
        this.classId = classId;
        this.ClassTypeId = ClassTypeId;
        this.ChildClassTypeId = ChildClassTypeId;


    }

    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }

    @Override
    public int getGroupCount() {
        return mListLevel.size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mListLevel.get(groupPosition);
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
                    R.layout.alone_level_group_items, null);
        }
        tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        jiantou = (ImageView) convertView.findViewById(R.id.jiantou);
        String title = mListLevel.get(groupPosition).getLevelName();
        title = title.replace("&nbsp;", " ");
        tv_title.setText(title);
        int HasFreeVideo = mListLevel.get(groupPosition).getHasFreeVideo();
        Log.i("HasFreeVideo ", "" + HasFreeVideo);
        TextView shiting_textview = (TextView) convertView.findViewById(R.id.shiting_textview);
        if (1 == HasFreeVideo) {
            shiting_textview.setVisibility(View.VISIBLE);
        } else {
            shiting_textview.setVisibility(View.GONE);
        }

        if (isExpanded) {
            jiantou.setBackgroundResource(R.drawable.expandable_level_zhankai);
        } else {
            jiantou.setBackgroundResource(R.drawable.expandable_level_sousuo);
        }
        return convertView;
    }


    @Override
    public int getChildrenCount(int groupPosition) {
        return mListLevel.get(groupPosition).getList() == null ? 0 : mListLevel.get(groupPosition).getList().size();

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
        return mListLevel.get(groupPosition).getList().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    class ChildViewHolder {
        private TextView Level_ShowName;
        private TextView shiting_textview;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;
        convertView = LayoutInflater.from(activity).inflate(R.layout.alone_level_child_items,
                null);
        holder = new ChildViewHolder();
        holder.Level_ShowName = (TextView) convertView.findViewById(R.id.Level_ShowName);
        String title = mListLevel.get(groupPosition).getList().get(childPosition).getLevel_ShowName();
        title = title.replace("&nbsp;", " ");
        title = title.replace("&amp;", "");
        holder.Level_ShowName.setText(title);
        holder.shiting_textview = (TextView) convertView.findViewById(shiting_textview);
        if (4 == ChildClassTypeType) {
            holder.shiting_textview.setText("");
            //直播是否开始
            int IsLiveStart = mListLevel.get(groupPosition).getList().get(childPosition).getIsLiveStart();
            //直播是否结束
            int IsLiveEnd = mListLevel.get(groupPosition).getList().get(childPosition).getIsLiveEnd();
            //是否有回放
            int CS_IsPlayback = mListLevel.get(groupPosition).getList().get(childPosition).getCS_IsPlayback();
            //是否有预约
            int CS_IsReserve = mListLevel.get(groupPosition).getList().get(childPosition).getCS_IsReserve();
            //是否预约过
            int HasBook = mListLevel.get(groupPosition).getList().get(childPosition).getHasBook();

            if (1 == CS_IsReserve) {
                if (1 == HasBook) {
                    holder.shiting_textview.setText("已预约");
                    holder.shiting_textview.setTextColor(Color.parseColor("#dd5555"));
                } else {
                    holder.shiting_textview.setText("立即预约");
                    holder.shiting_textview.setTextColor(Color.parseColor("#dd5555"));
                    holder.shiting_textview.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ScheduleId = mListLevel.get(groupPosition).getList().get(childPosition).getKeyID();
                            String Level_Id = mListLevel.get(groupPosition).getList().get(childPosition).getLevel_PKID();
                            LevelId = Integer.parseInt(Level_Id);
                            if (httpUtils.isNetworkConnected(activity)) {
                                LJYYTask collectionTask = new LJYYTask();
                                collectionTask.execute();
                                holder.shiting_textview.setText("已预约");
                            }
                        }
                    });


                }
            }
        } else {
            int IsFree = mListLevel.get(groupPosition).getList().get(childPosition).getIsFree();
            if (1 == IsFree) {
                holder.shiting_textview.setText("试听");
                holder.shiting_textview.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //名称
                        String childName = mListLevel.get(groupPosition).getList().get(childPosition).getLevel_ShowName();
                        //视频VID
                        String childVID = mListLevel.get(groupPosition).getList().get(childPosition).getVID();
                        //是否免费试听
                        int isFree= mListLevel.get(groupPosition).getList().get(childPosition).getIsFree();
                        if (!"".equals(childVID) && null != childVID) {
                            if (1 == isFree) {
                                C_IjkVideoActicity.intentTo(activity, C_IjkVideoActicity.PlayMode.landScape, C_IjkVideoActicity.PlayType.vid, childVID,
                                        true, childName);
                            } else {
                                Toast.makeText(activity, "该节课程无试听视频", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(activity, "视频暂时无法播放", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                holder.shiting_textview.setText("");
            }
        }
//        //文件类型：1:视频 2:试卷 3:直播 4:资料
//        int KeyType = mListLevel.get(groupPosition).getList().get(childPosition).getKeyType();
//        if (1 == KeyType) {//视频
//            int IsFree = mListLevel.get(groupPosition).getList().get(childPosition).getIsFree();
//            if (1 == IsFree) {
//                holder.shiting_textview.setText("试听");
//            } else {
//                holder.shiting_textview.setText("");
//            }
//        } else if (2 == KeyType) {//试卷
//
//        } else if (3 == KeyType) {
//            holder.shiting_textview.setText("");
//            //直播是否开始
//            int IsLiveStart = mListLevel.get(groupPosition).getList().get(childPosition).getIsLiveStart();
//            //直播是否结束
//            int IsLiveEnd = mListLevel.get(groupPosition).getList().get(childPosition).getIsLiveEnd();
//            //是否有回放
//            int CS_IsPlayback = mListLevel.get(groupPosition).getList().get(childPosition).getCS_IsPlayback();
//            //是否有预约
//            int CS_IsReserve = mListLevel.get(groupPosition).getList().get(childPosition).getCS_IsReserve();
//            //是否预约过
//            int HasBook = mListLevel.get(groupPosition).getList().get(childPosition).getHasBook();
//
//            if (1 == IsLiveEnd) {//已结束
//                if (1 == CS_IsPlayback) {///有回放
//                    holder.shiting_textview.setText("已结束,有回放");
//                    holder.shiting_textview.setTextColor(Color.parseColor("#dd5555"));
//                } else {
//                    holder.shiting_textview.setText("已结束,无回放");
//                    holder.shiting_textview.setTextColor(Color.parseColor("#d8d8d8"));
//                }
//            } else {//未结束
//                if (1 == IsLiveStart) {//开始了
//                    if (1 == HasBook) {
//                        holder.shiting_textview.setText("进入直播");
//                        holder.shiting_textview.setTextColor(Color.parseColor("#dd5555"));
//                    } else {
//                        holder.shiting_textview.setText("立即预约");
//                        holder.shiting_textview.setTextColor(Color.parseColor("#dd5555"));
//                    }
//                } else {//未开始
//                    if (1 == CS_IsReserve) {
//                        holder.shiting_textview.setText("立即预约");
//                        holder.shiting_textview.setTextColor(Color.parseColor("#dd5555"));
//                    }
//                }
//            }
//
//        } else if (4 == KeyType) {//资料
//
//        }


        return convertView;
    }


    class LJYYTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... params) {
            try {
                addLiveBook = httpUtils.addlivebookXL(Const.URL + Const.ClassLiveBookAPI, getUserID(), classId, ClassTypeId, ChildClassTypeId, ScheduleId, LevelId);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == addLiveBook.getCode()) {
//                    expandableListView.expandGroup(0);
                Toast.makeText(activity, addLiveBook.getMessage(), Toast.LENGTH_SHORT).show();
//                adapter.notifyDataSetChanged();
//                    go_buy_layout.setBackgroundColor(Color.parseColor("#DBDBDB"));
//                    ImageView tel_im = (ImageView) findViewById(R.id.tel_im);
//                    tel_im.setVisibility(View.GONE);
//                    TextView buy_immediately_bt = (TextView) findViewById(R.id.buy_immediately_bt);
//                    buy_immediately_bt.setText("已预约");
//                    buy_immediately_bt.setTextColor(Color.parseColor("#999999"));
//                    goBuyStatus = YiYuYue;
            } else {
                Toast.makeText(activity, addLiveBook.getMessage(), Toast.LENGTH_SHORT).show();
            }

        }
    }


    private int getUserID() {
        SharedPreferences LoginPre = activity.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        if (null != LoginPre && !"".equals(LoginPre)) {
            int UserID = LoginPre.getInt("S_ID", 0);
            return UserID;
        } else {
            return 0;
        }

    }
}

