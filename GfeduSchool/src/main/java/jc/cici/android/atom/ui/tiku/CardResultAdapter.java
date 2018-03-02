package jc.cici.android.atom.ui.tiku;


import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import jc.cici.android.R;
import jc.cici.android.atom.view.MyGridView;

public class CardResultAdapter extends BaseExpandableListAdapter {
    private Activity ctx;
    // 数据源
    private ArrayList<CardStatus> mList;
    private ResultAdaper adapter;
    // 班级id
    private int classId;
    // 阶段id
    private int stageId;
    // 课程id
    private int lessonId;
    // 在线标识
    private int isOnline;
    // 面授传递参数
    private int lessonChildId;
    // 科目id
    private int subjectId;
    private String studyKey;
    private String name;
    private int testPPKID;
    ArrayList<CardStatus.Body.PaperQuesGroupList.PaperQuesList> viewList = new ArrayList<>();

    public CardResultAdapter(Activity ctx, ArrayList<CardStatus> mList, int classId,
                             int stageId, int lessonId, int isOnline, int lessonChildId, int subjectId, String studyKey, String name, int testPPKID) {
        this.ctx = ctx;
        this.mList = mList;
        this.classId = classId;
        this.stageId = stageId;
        this.lessonId = lessonId;
        this.isOnline = isOnline;
        this.lessonChildId = lessonChildId;
        this.subjectId = subjectId;
        this.studyKey = studyKey;
        this.name = name;
        this.testPPKID = testPPKID;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return super.areAllItemsEnabled();
    }

    @Override
    public Object getGroup(int postion) {
        return mList.get(0).getBody().getPaperQuesGroupList().get(postion);
    }

    @Override
    public int getGroupCount() {
        return null != mList.get(0).getBody().getPaperQuesGroupList() ? mList.get(0).getBody().getPaperQuesGroupList().size() : 0;
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
            convertView = LayoutInflater.from(ctx).inflate(
                    R.layout.card_group_items, null);
        }
        tv_title = (TextView) convertView.findViewById(R.id.tv_title);
        jiantou = (ImageView) convertView.findViewById(R.id.jiantou);
        String title = mList.get(0).getBody().getPaperQuesGroupList().get(groupPosition).getQuesTypeName();
        tv_title.setText(title);
        if (isExpanded) {
            jiantou.setBackgroundResource(R.drawable.btn_zhankai_icon);
        } else {
            jiantou.setBackgroundResource(R.drawable.btn_shousu_icon);
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
        return mList.get(0).getBody().getPaperQuesGroupList().get(groupPosition).getPaperQuesList().get(childPosition);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    class ChildViewHolder {
        private MyGridView view_gridResult;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildViewHolder holder;
        convertView = LayoutInflater.from(ctx).inflate(R.layout.card_child_items,
                null);
        holder = new ChildViewHolder();
        holder.view_gridResult = (MyGridView) convertView
                .findViewById(R.id.gridResult_view);

        viewList = mList.get(0).getBody().getPaperQuesGroupList().get(groupPosition).getPaperQuesList();
        Gson s = new Gson();
        Log.i("数据库 --- > ", s.toJson(viewList).toString());
        adapter = new ResultAdaper(ctx, viewList, holder.view_gridResult, classId, stageId, lessonId, subjectId, isOnline, lessonChildId, studyKey, name, testPPKID);
        holder.view_gridResult.setAdapter(adapter);

        return convertView;
    }

}
