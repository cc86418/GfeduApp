package jc.cici.android.atom.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bignerdranch.expandablerecyclerview.ChildViewHolder;
import com.bignerdranch.expandablerecyclerview.ExpandableRecyclerAdapter;
import com.bignerdranch.expandablerecyclerview.ParentViewHolder;
import com.zhy.autolayout.utils.AutoUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import jc.cici.android.R;
import jc.cici.android.atom.bean.Day;
import jc.cici.android.atom.bean.DayLesson;
import jc.cici.android.atom.bean.OnLineBean;
import jc.cici.android.atom.ui.study.ChapterActivity;

/**
 * 在线筛选适配
 * Created by atom on 2017/6/8.
 */

public class OnLineRecycleAdapter extends ExpandableRecyclerAdapter<OnLineBean, DayLesson, OnLineRecycleAdapter.RecipeViewHolder, OnLineRecycleAdapter.ChildHolder> {

    private LayoutInflater mInflater;
    private Context mCtx;
    private List<OnLineBean> mList;
    // 班级名称
    private String className;
    // 标题内容
    private String titleName;
    // 班型id
    private int classId;
    // 阶段id
    private int stageId;
    // 笔记解锁
    private int stageNote;
    // 问题解锁
    private int stageProblem;
    // 资料解锁
    private int stageInformation;

    public OnLineRecycleAdapter(Context context, @NonNull List<OnLineBean> recipeList
            , int classId, String className, String titleName, int stageId, int stageNote, int stageProblem, int stageInformation) {
        super(recipeList);
        mCtx = context;
        mInflater = LayoutInflater.from(context);
        this.mList = recipeList;
        this.classId = classId;
        this.className = className;
        this.titleName = titleName;
        this.stageId = stageId;
        this.stageNote = stageNote;
        this.stageProblem = stageProblem;
        this.stageInformation = stageInformation;
    }

    @NonNull
    @Override
    public RecipeViewHolder onCreateParentViewHolder(@NonNull ViewGroup parentViewGroup, int viewType) {
        View recipeView = mInflater.inflate(R.layout.item_recipe_view, parentViewGroup, false);
        return new RecipeViewHolder(recipeView);
    }

    @NonNull
    @Override
    public ChildHolder onCreateChildViewHolder(@NonNull ViewGroup childViewGroup, int viewType) {
        View childView = mInflater.inflate(R.layout.item_expand_child_view, childViewGroup, false);
        return new ChildHolder(childView);
    }

    @Override
    public void onBindParentViewHolder(@NonNull RecipeViewHolder parentViewHolder, int parentPosition, @NonNull OnLineBean parent) {
        parentViewHolder.bind(parent);
    }

    @Override
    public void onBindChildViewHolder(@NonNull ChildHolder childViewHolder, int parentPosition, int childPosition, @NonNull DayLesson child) {
        childViewHolder.bind(child, childPosition);
    }

    /**
     * 第一层ViewHolder
     */
    class RecipeViewHolder extends ParentViewHolder {

        @BindView(R.id.item_parent_layout)
        RelativeLayout item_parent_layout;
        // 月份
        @BindView(R.id.month_txt)
        TextView month_txt;
        // 设置item文字
        @BindView(R.id.item_parent_txt)
        TextView item_parent_txt;
        // 设置打开关闭图片
        @BindView(R.id.item_parentFlag_img)
        ImageView item_parentFlag_img;

        /**
         * Default constructor.
         *
         * @param itemView The {@link View} being hosted in this ViewHolder
         */
        public RecipeViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            item_parent_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.item_parent_layout) { // 表示选中
                        if (isExpanded()) { // 当前扩展
                            collapseView();// 收缩
                            item_parentFlag_img.setBackgroundResource(R.drawable.icon_expandable_close);
                        } else { // 当前收缩
//                            collapseAllParents(); // 如果有其他打开的先关闭其他
                            expandView();
                            item_parentFlag_img.setBackgroundResource(R.drawable.icon_expandable_open);
                        }

                    }
                }
            });
        }

        public void bind(OnLineBean onLineBean) {
            month_txt.setText(onLineBean.getMonth() + "月");
            item_parent_txt.setText("共有课表" + onLineBean.getScheduleCount()+ "节");
        }


        @Override
        public boolean shouldItemViewClickToggleExpansion() {
            return false;
        }


    }

    /**
     * 子分类viewHolder
     * Created by atom on 2017/6/8.
     */

    class ChildHolder extends ChildViewHolder {

        // item 布局
        @BindView(R.id.item_child_layout)
        RelativeLayout item_child_layout;
        // 当前天数
        @BindView(R.id.day_txt)
        TextView day_txt;
        // 设置item文字
        @BindView(R.id.item_child_txt)
        TextView item_child_txt;
        // 时间段
        @BindView(R.id.item_childTime_txt)
        TextView item_childTime_txt;

        private int mPos;

        /**
         * Default constructor.
         *
         * @param itemView The {@link View} being hosted in this ViewHolder
         */
        public ChildHolder(@NonNull View itemView) {
            super(itemView);
            AutoUtils.autoSize(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void bind(final DayLesson childItem, final int position) {
            day_txt.setText(childItem.getDate());
            item_child_layout.setTag(position);
            item_child_txt.setText(childItem.getLessonName());
            switch (childItem.getLessonDateType()) { // 获取上课时间段
                case 1: // 上午
                    item_childTime_txt.setText("上午 " + childItem.getLessonStartTime() + "-" + childItem.getLessonEndTime());
                    break;
                case 2: // 下午
                    item_childTime_txt.setText("下午 " + childItem.getLessonStartTime() + "-" + childItem.getLessonEndTime());
                    break;
                case 3: // 晚上
                    item_childTime_txt.setText("晚上 " + childItem.getLessonStartTime() + "-" + childItem.getLessonEndTime());
                    break;
            }
            mPos = position;
            if(1 == childItem.getIsValid()){ // 开放7天课程
                day_txt.setTextColor(Color.parseColor("#333333"));
                item_child_txt.setTextColor(Color.parseColor("#333333"));
                item_childTime_txt.setTextColor(Color.parseColor("#999999"));
                item_child_layout.setClickable(true);
                item_child_layout.setEnabled(true);
            }else{ // 关闭7天后课程
                day_txt.setTextColor(Color.parseColor("#cccccc"));
                item_child_txt.setTextColor(Color.parseColor("#cccccc"));
                item_childTime_txt.setTextColor(Color.parseColor("#cccccc"));
                item_child_layout.setClickable(false);
                item_child_layout.setEnabled(false);
            }

            item_child_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getId() == R.id.item_child_layout) { // 表示选中
                        if (mPos == (int) item_child_layout.getTag()) {
                            Intent it = new Intent(mCtx, ChapterActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putString("className", className);
                            bundle.putString("titleName", titleName);
                            bundle.putInt("classId", classId);
                            bundle.putInt("stageId", stageId);
                            bundle.putInt("lessonId", childItem.getLessonId());
                            bundle.putInt("stageNote", stageNote);
                            bundle.putInt("stageProblem", stageProblem);
                            bundle.putInt("stageInformation", stageInformation);
                            it.putExtras(bundle);
                            mCtx.startActivity(it);
                        }
                    }
                }
            });
        }

    }


}
