package cn.jun.adapter;

import android.content.Context;
import android.view.LayoutInflater;

import java.util.List;

import cn.jun.bean.WeakCourseWare;
import cn.jun.indexmain.base.MultiTypeBaseAdapter;
import cn.jun.indexmain.viewholder.CommonViewHolder;
import jc.cici.android.R;


public class KLineAdapter extends MultiTypeBaseAdapter<WeakCourseWare.CoursewareList> {
    private Context context;
    private LayoutInflater inflater;


    public KLineAdapter(Context context, List<WeakCourseWare.CoursewareList> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    protected void setHeaderView(CommonViewHolder holder, WeakCourseWare data) {
        /**标题**/
//        initTitle(holder,data);

    }

    private void initTitle(CommonViewHolder holder, WeakCourseWare data) {
    }

    @Override
    protected void convert(CommonViewHolder holder, final WeakCourseWare.CoursewareList data, int viewType) {
        if (viewType == 0) {//设置头部
//            setHeaderView(holder,data);
        } else {
            int KnowledgeId = data.getKnowledgeId();
            String KnowledgeName = data.getKnowledgeName();
            holder.setText(R.id.item_title, KnowledgeName);


        }
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        if (viewType == 0) {
            return R.layout.kline_header;
        }
        return R.layout.kline_item;
    }

    @Override
    protected int getViewType(int position, WeakCourseWare.CoursewareList data) {
        if (data == null) {
            return 0;
        } else {
            return 1;
        }


    }

}
