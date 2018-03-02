package jc.cici.android.atom.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.bean.LiveProduct;
import jc.cici.android.atom.utils.ToolUtils;


/**
 * 直播列表适配器
 * Created by atom on 2017/11/10.
 */

public class LiveListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // 正常布局
    private static final int TYPE_NORMAL = 0x001;
    // 加载更多布局
    private static final int TYPE_FOOT = 0x002;

    private OnItemClickListener clickListener;
    private Context mCtx;
    //正在加载更多
    static final int LOADING_MORE = 1;
    //没有更多
    static final int NO_MORE = 2;
    // 没有数据
    static final int NO_DATA = 3;
    //脚布局当前的状态,默认为没有更多
    int footer_state = 1;
    private View itemView;
    private ArrayList<LiveProduct.Live> mData;

    public LiveListAdapter(Context context, ArrayList<LiveProduct.Live> data) {
        this.mCtx = context;
        this.mData = data;
    }

    public void setClickListener(OnItemClickListener clickListener) {
        this.clickListener = clickListener;
    }

    public interface OnItemClickListener {
        void onClick(View view, int position);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_NORMAL:
                itemView = LayoutInflater.from(mCtx).inflate(R.layout.item_livelist, parent, false);
                return new TypeNormalHolder(itemView);
            case TYPE_FOOT: // 底部加载更多布局
                itemView = LayoutInflater.from(mCtx).inflate(R.layout.item_loading_more, parent, false);
                return new TypeFootHolder(itemView);
            default:
                return null;
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof TypeNormalHolder) {
            bindTyPeNormalHolder((TypeNormalHolder) holder, position);
        } else if (holder instanceof CourseSearchRecyclerAdapter.TypeFootHolder) {
            TypeFootHolder footHolder = (TypeFootHolder) holder;
            if (position == 0) {//如果第一个就是脚布局,,那就让他隐藏
                footHolder.progressBar.setVisibility(View.GONE);
                footHolder.loadingMore_txt.setText("");
            }
            switch (footer_state) {//根据状态来让脚布局发生改变
                case LOADING_MORE:
                    footHolder.progressBar.setVisibility(View.VISIBLE);
                    footHolder.loadingMore_txt.setText("加载更多");
                    footHolder.loadingMore_txt.setVisibility(View.VISIBLE);
                    break;
                case NO_MORE:
                    footHolder.progressBar.setVisibility(View.GONE);
                    footHolder.loadingMore_txt.setText("已经到底部");
                    footHolder.loadingMore_txt.setVisibility(View.VISIBLE);
                    break;
                case NO_DATA:
                    footHolder.progressBar.setVisibility(View.GONE);
                    footHolder.loadingMore_txt.setText("");
                    break;

            }
        }
    }

    private void bindTyPeNormalHolder(TypeNormalHolder holder, int position) {

        Glide.with(mCtx).load(mData.get(position).getClass_MobileImage())
                .placeholder(R.drawable.item_studyhome_img) //加载中显示的图片
                .error(R.drawable.item_studyhome_img) //加载失败时显示的图片
                .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                .override(280, 186) // 设置最终显示图片大小
                .centerCrop() // 中心剪裁
                .skipMemoryCache(true) // 跳过缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                .into(holder.courseImg);
        // 班级名称
        holder.courseName_txt.setText(ToolUtils.strReplaceAll(mData.get(position).getClass_Name()));
        if (1 != mData.get(position).getClass_Form()) { // 正常课程情况(系列课程为1)
            // 直播日期
            if (null != mData.get(position).getCS_Date() && !"".equals(mData.get(position).getCS_Date())) {
                String year = mData.get(position).getCS_Date().substring(0, 4);
                holder.courseLiveDate_txt.setText(year + "年" + mData.get(position).getCS_DateShort()
//                    + " " + mData.get(position).getCS_StartTime() + "-" + mData.get(position).getCS_EndTime()
                );
            }
            // 直播时间
            holder.courseLiveTime_txt.setText(mData.get(position).getCS_StartTime() + "-" + mData.get(position).getCS_EndTime());
        } else {
            holder.courseLiveDate_txt.setText("");
            holder.courseLiveTime_txt.setText("");
        }
        // 价格
        if (!"￥0".equals(mData.get(position).getClass_PriceSaleRegion())) {
            holder.coursePrice_txt.setText(ToolUtils.strReplaceAll(mData.get(position).getClass_PriceSaleRegion()));
            // 购买人数
            holder.studyCount_txt.setText("购买人数：" + mData.get(position).getAboutNum());
        } else {
            holder.coursePrice_txt.setText("免费");
            // 预约人数
            holder.studyCount_txt.setText("预约人数：" + mData.get(position).getAboutNum());
        }
//        // 回放人数
//        if (1 == mData.get(position).getCS_IsPlayback()) {
//            // 回放人数
//            holder.studyCount_txt.setText("回放人数：" + mData.get(position).getAboutNum());
//        }

    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return TYPE_FOOT;
        } else {
            return TYPE_NORMAL;
        }
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size() + 1;
    }

    class TypeNormalHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        // 课程图片
        @BindView(R.id.courseImg)
        ImageView courseImg;
        // 课程名
        @BindView(R.id.courseName_txt)
        TextView courseName_txt;
        // 直播日期
        @BindView(R.id.courseLiveDate_txt)
        TextView courseLiveDate_txt;
        // 直播时间
        @BindView(R.id.courseLiveTime_txt)
        TextView courseLiveTime_txt;
        // 学习人数
        @BindView(R.id.studyCount_txt)
        TextView studyCount_txt;
        // 价格
        @BindView(R.id.coursePrice_txt)
        TextView coursePrice_txt;

        public TypeNormalHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (null != clickListener) {
                clickListener.onClick(itemView, getAdapterPosition());
            }
        }
    }

    /**
     * 底部加载更多布局
     */
    class TypeFootHolder extends RecyclerView.ViewHolder {

        // 进度条
        @BindView(R.id.progressBar)
        ProgressBar progressBar;
        // 提示文字
        @BindView(R.id.loadingMore_txt)
        TextView loadingMore_txt;

        public TypeFootHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

    }

    /**
     * 改变脚布局的状态的方法,在activity根据请求数据的状态来改变这个状态
     *
     * @param state
     */
    public void changeState(int state) {
        this.footer_state = state;
        notifyDataSetChanged();
    }
}
