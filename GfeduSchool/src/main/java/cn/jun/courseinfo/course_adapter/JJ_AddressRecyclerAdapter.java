//package cn.jun.courseinfo.course_adapter;
//
//import android.content.Context;
//import android.graphics.Color;
//import android.os.Handler;
//import android.os.Message;
//import android.support.v7.widget.RecyclerView;
//import android.view.View;
//import android.widget.CheckBox;
//import android.widget.CompoundButton;
//import android.widget.ImageView;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import cn.jun.adapter.base.BaseRecycleerAdapter;
//import cn.jun.courseinfo.bean.AddressBean;
//import jc.cici.android.R;
//
//import static cn.jun.courseinfo.course_adapter.J_RecyclerAdapter.J_msgAddressClick;
//
//
///**
// * Created by administrato on 2017/9/18.
// */
//
//public class JJ_AddressRecyclerAdapter extends BaseRecycleerAdapter<AddressBean, JJ_AddressRecyclerAdapter.MyHolder> {
//
//    private Context mCtx;
//    private Handler mHandler;
//
//        private Map<Integer, ArrayList<AddressBean>> address_maps = new HashMap<>();
//    private List<AddressBean> items;
//    private int parent_pos;
//    private int chlid_pos;
//
//
//    public JJ_AddressRecyclerAdapter(Context context, Map<Integer, ArrayList<AddressBean>> mMaps, Handler handler, int position) {
//        super(context, mMaps, position);
//        this.mCtx = context;
//        this.items = mMaps.get(position);
//        this.address_maps = mMaps;
//        this.mHandler = handler;
//        this.parent_pos = position;
//    }
//
//    @Override
//    public MyHolder onCreateViewHolder(View view, int viewType) {
//        return new MyHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(final MyHolder holder, AddressBean item, final int position) {
////        AddressBean items = address_maps.get(parent_pos).get(position);
//        chlid_pos = position;
//        holder.checkBox.setText(item.getTypeName());
//        if (item.isChecked()) {
//            holder.checkBox.setClickable(true);
//            holder.checkBox.setBackgroundResource(R.drawable.yuan_background_all_hui);
//            holder.checkBox.setTextColor(Color.parseColor("#333333"));
//
//            if (item.isTypeSelect()) {
//                holder.checkBox.setBackgroundResource(R.drawable.yuan_background_all);
//                holder.checkBox.setTextColor(Color.parseColor("#ffffff"));
//            } else {
//                holder.checkBox.setBackgroundResource(R.drawable.yuan_background_all_hui);
//                holder.checkBox.setTextColor(Color.parseColor("#333333"));
//            }
//        } else {
//            holder.checkBox.setClickable(false);
//            holder.checkBox.setBackgroundResource(R.drawable.yuan_background_all_hui);
//            holder.checkBox.setTextColor(Color.parseColor("#b1b1b1"));
//        }
//
//        holder.checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
//                if (items.get(position).isTypeSelect()) {
//                    items.get(position).setTypeSelect(false);
//                    J_msgAddressClick = false;
//                    notifyItemChanged(position);
//                } else {
//                    items.get(position).setTypeSelect(true);
//                    notifyItemChanged(position);
//                    for (int i = 0; i < items.size(); i++) {
//                        if (i != position) {
//                            items.get(i).setTypeSelect(false);
//                            notifyItemChanged(i);
//                        }
//                    }
//                }
//
//
//                Message msg = new Message();
//                msg.what = 3;
//                msg.obj = address_maps;
//                mHandler.sendMessage(msg);
//
//                Message msg2 = new Message();
//                msg2.what = -1;
//                msg2.obj = parent_pos;
//                mHandler.sendMessage(msg2);
//
//                Message msg3 = new Message();
//                msg3.what = -2;
//                msg3.obj = chlid_pos;
//                mHandler.sendMessage(msg3);
//            }
//        });
//    }
//
//
//    @Override
//    public int getLayoutId() {
//        return R.layout.course_select_child;
//    }
//
//    @Override
//    public int getItemCount() {
//        return items.size();
//    }
//
//    public class MyHolder extends RecyclerView.ViewHolder {
//        // 选中框
//        @BindView(R.id.checkBox)
//        CheckBox checkBox;
//        // 选中图片
//        @BindView(R.id.checkImg)
//        ImageView checkImg;
//
//        public MyHolder(View itemView) {
//            super(itemView);
//            ButterKnife.bind(this, itemView);
//        }
//
//    }
//}
