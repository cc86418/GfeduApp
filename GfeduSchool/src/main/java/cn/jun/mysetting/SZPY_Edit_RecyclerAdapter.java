package cn.jun.mysetting;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jun.bean.RaiseQuestionBean;
import jc.cici.android.R;


public class SZPY_Edit_RecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context mCtx;
    private ArrayList<RaiseQuestionBean> mlist;
    private ButtonInterface buttonInterface;

    /**
     * 按钮点击事件需要的方法
     */
    public void buttonSetOnclick(ButtonInterface buttonInterface) {
        this.buttonInterface = buttonInterface;
    }

    /**
     * 按钮点击事件对应的接口
     */
    public interface ButtonInterface {
        public void onclick(View view, int parent_pos, int chlid_pos);

    }


    public SZPY_Edit_RecyclerAdapter(Context context, ArrayList<RaiseQuestionBean> mlist) {
        this.mCtx = context;
        this.mlist = mlist;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.szpy_edit_child, parent, false);
        return new MyHolder(layout);
    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        RaiseQuestionBean.Body.QuestionList item = mlist.get(0).getBody().getQuestionList().get(position);
        //授课老师
        ((MyHolder) holder).szpy_text.setText(item.getQuestionContent());
        //选项
//        ((MyHolder) holder).rb_you


    }


    @Override
    public int getItemCount() {
        return mlist.get(0).getBody().getQuestionList().size();
    }


    public class MyHolder extends RecyclerView.ViewHolder {
        //问题
        @BindView(R.id.szpy_text)
        TextView szpy_text;
        //选项
        @BindView(R.id.rb_you)
        RadioButton rb_you;
        @BindView(R.id.rb_liang)
        RadioButton rb_liang;
        @BindView(R.id.rb_yiban)
        RadioButton rb_yiban;
        @BindView(R.id.rb_cha)
        RadioButton rb_cha;


        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
