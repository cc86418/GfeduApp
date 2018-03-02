package jc.cici.android.atom.adapter;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.AddressBean;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.shopCart.EditAddressAc;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 地址管理适配器
 * Created by atom on 2017/8/23.
 */

public class AddressManagerRecAdapter extends BaseRecycleerAdapter<AddressBean, AddressManagerRecAdapter.MyHolder> {


    private Context mCtx;
    private ArrayList<AddressBean> mData;
    private Dialog dialog;
    // 选中标识
    private int selectPos = -1;
    private Handler handle = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 0) {
                notifyDataSetChanged();
            }
        }
    };

    public AddressManagerRecAdapter(Context context, ArrayList<AddressBean> items) {
        super(context, items);
        this.mCtx = context;
        this.mData = items;

    }

    /**
     * 记录默认选中pos
     *
     * @param position
     */
    public void setSelected(int position) {
        selectPos = position;
    }


    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final AddressBean item, final int position) {

        // 收货人名称
        holder.receiverName.setText(ToolUtils.strReplaceAll(item.getAddressName()));
        // 收货人联系方式
        holder.phoneName_txt.setText(ToolUtils.strReplaceAll(item.getAddressMobile()));
        // 收货人e-mail
        holder.mail_txt.setText(ToolUtils.strReplaceAll(item.getAddressEmail()));
        // 收货人详细地址
        holder.detailAddress_txt.setText(ToolUtils.strReplaceAll(item.getAddressProvince())
                + ToolUtils.strReplaceAll(item.getAddressCity())
                + ToolUtils.strReplaceAll(item.getAddressDetail()));
        // 判断是否为默认
        if (selectPos != -1) {
            if (selectPos == position) { // 默认选中
                holder.manager_checkBox.setBackgroundResource(R.drawable.ic_check_checked);
                holder.default_txt.setTextColor(Color.parseColor("#dd5555"));
            } else { // 非默认选中
                holder.manager_checkBox.setBackgroundResource(R.drawable.icon_default_select);
                holder.default_txt.setTextColor(Color.parseColor("#666666"));
            }
        }

        // 默认按钮设置监听
        holder.default_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 改变默认选中
                changeDefaultStatus(item.getAddressPKID());
            }
        });

        /**
         *  编辑按钮设置监听
         */
        holder.edit_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(mCtx, EditAddressAc.class);
                Bundle bundle = new Bundle();
                // 地址id
                bundle.putInt("addressPKID", mData.get(position).getAddressPKID());
                // 收货人名称
                bundle.putString("addressName", mData.get(position).getAddressName());
                // 收货人联系方式
                bundle.putString("addressMobile", mData.get(position).getAddressMobile());
                // 收货人所在省份
                bundle.putString("addressProvince", mData.get(position).getAddressProvince());
                // 收货人所在市份
                bundle.putString("addressCity", mData.get(position).getAddressCity());
                // 详细地址
                bundle.putString("addressDetail", mData.get(position).getAddressDetail());
                it.putExtras(bundle);
                mCtx.startActivity(it);
            }
        });

        /**
         * 删除按钮监听
         */
        holder.del_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 删除地址
                deleteAddress(item.getAddressPKID(), position);

            }
        });
    }

    /**
     * 删除地址
     *
     * @param addressPKID
     * @param position
     */
    private void deleteAddress(int addressPKID, final int position) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commonPramas(addressPKID);
        Observable<CommonBean> observable = httpPostService.deleteAddressInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(mCtx, "网络请求异常,删除失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        if (100 == commonBean.getCode()) { // 删除成功，更新ui
                            Intent it = new Intent();
                            Bundle bundle = new Bundle();
                            bundle.putInt("addressPKID", mData.get(position).getAddressPKID());
                            it.putExtras(bundle);
                            it.setAction("com.ensure.changeAddress");
                            mCtx.sendBroadcast(it);
                            mData.remove(position);
                            notifyItemRemoved(position);
                            if (position != mData.size()) {      // 这个判断的意义就是如果移除的是最后一个，就不用管
                                notifyItemRangeChanged(position, mData.size() - position);
                            }
                            // 设置删除后选中情况
                            if (selectPos > position) { // 删除的position 大于 选中情况
                                selectPos--;
                            } else if (selectPos == position) { // 删除为默认选中情况
                                if (mData.size() > 0) {
                                    // 修改第0个位置为选中状态
                                    changeDefaultStatus(mData.get(0).getAddressPKID());
                                } else { // 只有一个地址，且为默认选中情况
                                    notifyDataSetChanged();
                                }

                            }

                        } else {
                            Toast.makeText(mCtx, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 网络请求设置默认
     *
     * @param addressPKID
     */
    private void changeDefaultStatus(final int addressPKID) {

        // 加载数据
        showProcessDialog((Activity) mCtx,
                R.layout.loading_process_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commonPramas(addressPKID);
        Observable<CommonBean> observable = httpPostService.setDefaultAddressInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean>() {
                    @Override
                    public void onCompleted() {
                        if (null != dialog && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        if (null != dialog && dialog.isShowing()) {
                            dialog.dismiss();
                        }
                        Toast.makeText(mCtx, "网络异常，请重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        if (100 == commonBean.getCode()) {
                            for (int i = 0; i < mData.size(); i++) {
                                if (addressPKID == mData.get(i).getAddressPKID()) {
                                    selectPos = i;
                                    handle.sendEmptyMessage(0);
                                    break;
                                }
                            }
                        } else {
                            Toast.makeText(mCtx, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 自定义进度
     *
     * @param mContext
     * @param layout
     */
    private void showProcessDialog(Activity mContext, int layout) {
        dialog = new AlertDialog.Builder(mContext, R.style.showdialog).create();
        dialog.show();
        // 注意此处要放在show之后 否则会报异常
        dialog.setContentView(layout);
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_address_manager;
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class MyHolder extends RecyclerView.ViewHolder {

        // 收货人姓名
        @BindView(R.id.receiverName)
        TextView receiverName;
        // 收货人手机号
        @BindView(R.id.phoneName_txt)
        TextView phoneName_txt;
        // 邮箱
        @BindView(R.id.mail_txt)
        TextView mail_txt;
        // 详细地址
        @BindView(R.id.detailAddress_txt)
        TextView detailAddress_txt;
        // 默认布局
        @BindView(R.id.default_layout)
        RelativeLayout default_layout;
        // 选择框
        @BindView(R.id.manager_checkBox)
        ImageView manager_checkBox;
        // 默认文字
        @BindView(R.id.default_txt)
        TextView default_txt;
        // 编辑布局
        @BindView(R.id.edit_layout)
        RelativeLayout edit_layout;
        // 删除布局
        @BindView(R.id.del_layout)
        RelativeLayout del_layout;

        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }


    /**
     * 公共请求参数
     *
     * @return
     */
    private RequestBody commonPramas(int addressPKID) {

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(mCtx);
        try {
            // 测试数据
            obj.put("userId", commParam.getUserId());
            obj.put("addressPKID", addressPKID);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }
}
