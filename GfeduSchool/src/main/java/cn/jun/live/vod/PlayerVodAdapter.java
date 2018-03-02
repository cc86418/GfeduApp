package cn.jun.live.vod;

import android.content.Context;
import android.view.LayoutInflater;

import com.gensee.entity.QAMsg;

import java.util.List;

import cn.jun.indexmain.base.MultiTypeBaseAdapter;
import cn.jun.indexmain.viewholder.CommonViewHolder;
import jc.cici.android.R;


public class PlayerVodAdapter extends MultiTypeBaseAdapter<QAMsg> {
    private Context context;
    private LayoutInflater inflater;


    public PlayerVodAdapter(Context context, List<QAMsg> datas, boolean isOpenLoadMore) {
        super(context, datas, isOpenLoadMore);
        this.context = context;
    }


    @Override
    protected void convert(CommonViewHolder holder, final QAMsg data, int viewType) {
        if (viewType == 0) {//设置头部
        } else {
            String qa_nick = data.getQuestOwnerName();
            String qa_content = data.getQuestion();
            holder.setText(R.id.qa_nick, qa_nick);
            holder.setText(R.id.qa_content, qa_content);
        }
    }

    @Override
    protected int getItemLayoutId(int viewType) {
        if (viewType == 0) {
            return R.layout.recycleview_header;
        }
        return R.layout.vod_recycleview_item;
    }

    @Override
    protected int getViewType(int position, QAMsg data) {
        if (data == null) {
            return 0;
        } else {
            return 1;
        }


    }

}
