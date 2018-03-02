package jc.cici.android.atom.adapter;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.AllOrderBean;
import jc.cici.android.atom.bean.ChildOrder;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.Express;
import jc.cici.android.atom.bean.ExpressBean;
import jc.cici.android.atom.bean.OrderList;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.order.OrderDetailActivity;
import jc.cici.android.atom.ui.shopCart.BillActivity;
import jc.cici.android.atom.ui.shopCart.BillContentActivity;
import jc.cici.android.atom.ui.shopCart.PayDetailActivity;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.EmptyRecyclerView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 全部订单适配器
 * Created by atom on 2017/8/25.
 */

public class AllOrderRecyclerAdapter extends BaseRecycleerAdapter<OrderList, AllOrderRecyclerAdapter.MyHolder> {

    private Context mCtx;
    private ArrayList<OrderList> mItems;
    // 适配器来源
    private int mFrom;
    // 单班型适配器
    private ChildOrderRecyclerAdapter childTypeAdapter;
    // 套餐班型适配器
    private DoubleClassRecAdapter doubleClassAdapter;
    // 进度条对话框
    private Dialog dialog;


    public AllOrderRecyclerAdapter(Context context, ArrayList<OrderList> items, int from) {
        super(context, items);
        this.mCtx = context;
        this.mItems = items;
        this.mFrom = from;
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, OrderList item, final int position) {
        final AllOrderBean allOrderBean = mItems.get(position).getOrder();
        // 订单号
        holder.orderNumber_txt.setText(ToolUtils.strReplaceAll(allOrderBean.getOrder_Code()));
        // 完成情况
        final int orderStatus = allOrderBean.getOrder_State();
        switch (orderStatus) {
            case -1:
                holder.finishCondition_txt.setText("已取消");
                holder.goBill_btn.setText("去支付");
                // 不显示物流信息
                holder.checkLog_Btn.setVisibility(View.GONE);
                // 不显示支付信息
                holder.goBill_btn.setVisibility(View.GONE);
                break;
            case 0:
                holder.finishCondition_txt.setText("未支付");
                holder.checkLog_Btn.setText("取消订单");
                holder.goBill_btn.setText("去支付");
                // 显示取消订单
                holder.checkLog_Btn.setVisibility(View.VISIBLE);
                // 显示去支付
                holder.goBill_btn.setVisibility(View.VISIBLE);
                break;
            case 1:
                holder.finishCondition_txt.setText("已欠费");
                holder.goBill_btn.setText("去支付");
                // 显示物流信息
                holder.checkLog_Btn.setVisibility(View.VISIBLE);
                // 显示支付信息
                holder.goBill_btn.setVisibility(View.VISIBLE);
                break;
            case 2:
                holder.finishCondition_txt.setText("已完成");
                holder.checkLog_Btn.setText("查看物流");
                // 发票状态
                final int billStatus = allOrderBean.getOrder_InvoiceApply();
                if (0 == billStatus) {
                    holder.goBill_btn.setText("开发票");
                } else if (1 == billStatus) {
                    holder.goBill_btn.setText("发票信息");
                }
                // 显示物流信息
                holder.checkLog_Btn.setVisibility(View.VISIBLE);
                // 显示发票信息
                holder.goBill_btn.setVisibility(View.VISIBLE);
                break;
        }

        ArrayList<ChildOrder> childOrderList = mItems.get(position).getChildOrderList();
        if (null != childOrderList && childOrderList.size() > 0) {
            for (ChildOrder childOrder : childOrderList) {
                if (1 == childOrder.getProduct_Module()) { // 班型情况
                    holder.typeRecyclerView.setVisibility(View.VISIBLE);
                    holder.typeRecyclerView.setLayoutManager(new LinearLayoutManager(mCtx) {
                        @Override
                        public boolean canScrollVertically() {
                            return false;
                        }
                    });
                    childTypeAdapter = new ChildOrderRecyclerAdapter(mCtx, childOrderList, orderStatus);
                    holder.typeRecyclerView.setAdapter(childTypeAdapter);
                    holder.typeRecyclerView.setNestedScrollingEnabled(false);
                    childTypeAdapter.setOnItemClickListener(new ChildOrderRecyclerAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int pos) {
                            Intent it = new Intent(mCtx, OrderDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("from", mFrom);
                            bundle.putInt("orderId", mItems.get(position).getOrder().getOrder_PKID());
                            it.putExtras(bundle);
                            mCtx.startActivity(it);
                        }
                    });

                } else if (3 == childOrder.getProduct_Module()) { // 商品
                    holder.typeRecyclerView.setVisibility(View.GONE);
                } else if (4 == childOrder.getProduct_Module()) { // 优惠券
                    holder.typeRecyclerView.setVisibility(View.GONE);
                } else if (5 == childOrder.getProduct_Module()) { // 套餐
                    holder.typeRecyclerView.setVisibility(View.VISIBLE);
                    holder.typeRecyclerView.setLayoutManager(new LinearLayoutManager(mCtx) {
                        @Override
                        public boolean canScrollVertically() {
                            return false;
                        }
                    });
                    int orderId = mItems.get(position).getOrder().getOrder_PKID();
                    doubleClassAdapter = new DoubleClassRecAdapter(mCtx, childOrderList, orderStatus, mFrom, orderId);
                    holder.typeRecyclerView.setAdapter(doubleClassAdapter);
                    holder.typeRecyclerView.setNestedScrollingEnabled(false);
                    // 套餐点击跳转
                    doubleClassAdapter.setOnItemClickListener(new DoubleClassRecAdapter.OnItemClickListener() {
                        @Override
                        public void onItemClick(View view, int pos) {
                            Intent it = new Intent(mCtx, OrderDetailActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("from", mFrom);
                            bundle.putInt("orderId", mItems.get(position).getOrder().getOrder_PKID());
                            it.putExtras(bundle);
                            mCtx.startActivity(it);
                        }
                    });
                }

            }
        }

        if (-1 == allOrderBean.getOrder_State()) { // 如果是已取消情况，不显示金额，支付情况
            // 隐藏总额布局
            holder.totalPrices_layout.setVisibility(View.GONE);
            // 隐藏已支付金额布局
            holder.havePrices_layout.setVisibility(View.GONE);
            // 隐藏未支付金额布局
            holder.noPrices_layout.setVisibility(View.GONE);
        } else if (0 == allOrderBean.getOrder_State()) { // 未支付情况
            // 隐藏已支付金额布局
            holder.havePrices_layout.setVisibility(View.GONE);
            // 隐藏未支付金额布局
            holder.noPrices_layout.setVisibility(View.GONE);
            // 总额价格
            holder.totalPricesName_txt.setText("￥" + String.valueOf(allOrderBean.getOrder_Money()));
        } else { // 其他情况，照常显示
            // 总额价格
            holder.totalPricesName_txt.setText("￥" + String.valueOf(allOrderBean.getOrder_Money()));
            // 判断是否含有未支付情况
            float unpaidPrice = allOrderBean.getOrder_Arrearage();
            if (unpaidPrice > 0) {
                // 已支付金额
                float havePayPrice = allOrderBean.getOrder_Money() - unpaidPrice;
                java.text.DecimalFormat myFormat = new java.text.DecimalFormat("0.00");
                holder.havePricesName_txt.setText("￥" + String.valueOf(myFormat.format(havePayPrice)));
                // 还需支付金额
                holder.noPricesName_txt.setText("￥" + String.valueOf(allOrderBean.getOrder_Arrearage()));
                holder.havePrices_layout.setVisibility(View.VISIBLE);
                holder.noPrices_layout.setVisibility(View.VISIBLE);
            } else {
                // 已支付金额布局
                holder.havePrices_layout.setVisibility(View.GONE);
                // 未支付金额布局
                holder.noPrices_layout.setVisibility(View.GONE);
            }

        }

        // 取消订单or查看物流信息监听
        holder.checkLog_Btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                switch (orderStatus) {
                    case 0: // 未支付情况下，显示取消订单，取消订单设置监听
                        doCancelPay(mItems.get(position).getOrder().getOrder_PKID());
                        break;
                    case 1: // 已欠费情况
                    case 2:// 已完成情况下，显示物流信息，物流信息设置监听
                        final Dialog dialog = new Dialog(mCtx,
                                R.style.NormalDialogStyle);
                        dialog.setContentView(R.layout.view_express_dialog);
                        dialog.setCanceledOnTouchOutside(false);
                        dialog.show();
                        // 标题
                        final TextView title_txt = (TextView) dialog.findViewById(R.id.title_txt);
                        // 发货状态
                        final TextView have_expressTxt = (TextView) dialog.findViewById(R.id.have_expressTxt);
                        // 物流信息
                        final EmptyRecyclerView expressTxt_layout = (EmptyRecyclerView) dialog.findViewById(R.id.expressTxt_layout);
                        // 无物流信息显示视图
                        final ImageView empty_img = (ImageView) dialog.findViewById(R.id.empty_img);
                        // 未发货布局
                        final RelativeLayout unHave_expressTxt_layout = (RelativeLayout) dialog.findViewById(R.id.unHave_expressTxt_layout);
                        // 未发货内容
                        final TextView unHaveExpressTxt = (TextView) dialog.findViewById(R.id.unHaveExpressTxt);

                        final ArrayList<ExpressBean> expressList = new ArrayList<ExpressBean>();
                        expressTxt_layout.setLayoutManager(new LinearLayoutManager(mCtx));
                        final ExpressRecAdapter expressAdapter = new ExpressRecAdapter(mCtx, expressList);
                        expressTxt_layout.setEmptyView(empty_img);
                        expressTxt_layout.setAdapter(expressAdapter);
                        // 标题
                        title_txt.setText("发货信息");
                        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
                        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
                        final RequestBody body = commonPramas(mItems.get(position).getOrder().getOrder_PKID());
                        Observable<CommonBean<Express>> observable = httpPostService.getOrderExpressInfo(body);
                        observable.subscribeOn(Schedulers.io())
                                .unsubscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(
                                        new Subscriber<CommonBean<Express>>() {
                                            @Override
                                            public void onCompleted() {

                                            }

                                            @Override
                                            public void onError(Throwable e) {

                                                Toast.makeText(mCtx, "网络异常", Toast.LENGTH_SHORT).show();
                                            }

                                            @Override
                                            public void onNext(CommonBean<Express> expressCommonBean) {
                                                if (100 == expressCommonBean.getCode()) {
                                                    // 获取物流信息列表
                                                    ArrayList<ExpressBean> mExpressList = expressCommonBean.getBody().getList();
                                                    if (null != mExpressList && !"".equals(mExpressList) && mExpressList.size() > 0) {
                                                        have_expressTxt.setText("已发货");
                                                        for (ExpressBean expressBean : mExpressList) {
                                                            expressList.add(expressBean);
                                                        }
                                                        expressAdapter.notifyDataSetChanged();
                                                    }
                                                    // 获取未发货内容列表
                                                    ArrayList<String> notShippedList = expressCommonBean.getBody().getNotShipped();
                                                    if (null != notShippedList
                                                            && expressCommonBean.getBody().getExpressCount() > 0
                                                            && notShippedList.size() > 0) {
                                                        unHave_expressTxt_layout.setVisibility(View.VISIBLE);
                                                        StringBuffer buffer = new StringBuffer();
                                                        for (int i = 0; i < notShippedList.size(); i++) {
                                                            buffer.append(i + 1 + "、");
                                                            buffer.append(notShippedList.get(i));
                                                            buffer.append(" ");
                                                        }
                                                        unHaveExpressTxt.setText("未发货清单：" + buffer.toString());
                                                        unHaveExpressTxt.setVisibility(View.VISIBLE);
                                                    } else {
                                                        unHaveExpressTxt.setText("");
                                                        unHave_expressTxt_layout.setVisibility(View.GONE);
                                                    }
                                                } else {
                                                    have_expressTxt.setText("");
                                                    Toast.makeText(mCtx, expressCommonBean.getMessage(), Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }
                                );
                        // 取消按钮
                        Button cancel_Btn = (Button) dialog.findViewById(R.id.cancel_Btn);
                        cancel_Btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });
                        break;
                }
            }
        });
        // 去开发票or去支付按钮监听
        holder.goBill_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (orderStatus) {
                    case -1: // 已取消情况下显示为去支付，去支付设置监听
                        Intent cancelIt = new Intent(mCtx, PayDetailActivity.class);
                        Bundle cancelBundle = new Bundle();
                        cancelBundle.putInt("orderId", allOrderBean.getOrder_PKID());
                        cancelBundle.putInt("from", mFrom);
                        cancelBundle.putInt("orderStatus", -1);
                        cancelIt.putExtras(cancelBundle);
                        mCtx.startActivity(cancelIt);
                        break;
                    case 0: // 未支付情况下显示去支付,去支付设置监听
                        Intent finishIt = new Intent(mCtx, PayDetailActivity.class);
                        Bundle finishBundle = new Bundle();
                        finishBundle.putInt("orderId", allOrderBean.getOrder_PKID());
                        finishBundle.putInt("from", mFrom);
                        finishBundle.putInt("orderStatus", 0);
                        finishIt.putExtras(finishBundle);
                        mCtx.startActivity(finishIt);
                        break;
                    case 1: // 已欠费情况下，显示去支付，去支付设置监听
                        Intent debtIt = new Intent(mCtx, PayDetailActivity.class);
                        Bundle debtBundle = new Bundle();
                        debtBundle.putInt("orderId", allOrderBean.getOrder_PKID());
                        debtBundle.putInt("from", mFrom);
                        debtBundle.putInt("orderStatus", 1);
                        debtIt.putExtras(debtBundle);
                        mCtx.startActivity(debtIt);
                        break;
                    case 2: // 已完成
                        if (0 == allOrderBean.getOrder_InvoiceApply()) { // 去开发票
                            Intent it = new Intent(mCtx, BillActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("orderId", allOrderBean.getOrder_PKID());
                            bundle.putInt("mFrom", mFrom);
                            bundle.putInt("orderStatus", 2);
                            it.putExtras(bundle);
                            mCtx.startActivity(it);
                        } else if (allOrderBean.getOrder_InvoiceApply() > 0) { // 查看发票信息
                            Intent it = new Intent(mCtx, BillContentActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("orderId", allOrderBean.getOrder_PKID());
                            it.putExtras(bundle);
                            mCtx.startActivity(it);
                        }
                        break;
                }

            }
        });

    }

    @Override
    public int getLayoutId() {
        return R.layout.item_orderlist_view;
    }

    class MyHolder extends RecyclerView.ViewHolder {

        // 订单号
        @BindView(R.id.orderNumber_txt)
        TextView orderNumber_txt;
        // 完成情况
        @BindView(R.id.finishCondition_txt)
        TextView finishCondition_txt;
        // 套餐or班级
        @BindView(R.id.typeRecyclerView)
        RecyclerView typeRecyclerView;
        // 总额布局
        @BindView(R.id.totalPrices_layout)
        RelativeLayout totalPrices_layout;
        // 总额价格
        @BindView(R.id.totalPricesName_txt)
        TextView totalPricesName_txt;
        // 已付布局
        @BindView(R.id.havePrices_layout)
        RelativeLayout havePrices_layout;
        // 已支付金额
        @BindView(R.id.havePricesName_txt)
        TextView havePricesName_txt;
        // 还需支付布局
        @BindView(R.id.noPrices_layout)
        RelativeLayout noPrices_layout;
        // 还需支付金额
        @BindView(R.id.noPricesName_txt)
        TextView noPricesName_txt;
        // 物流开票布局
        @BindView(R.id.logistics_layout)
        RelativeLayout logistics_layout;
        // 查看物流信息按钮
        @BindView(R.id.checkLog_Btn)
        Button checkLog_Btn;
        // 开发票
        @BindView(R.id.goBill_btn)
        Button goBill_btn;


        public MyHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

        }
    }

    /**
     * 取消订单操作
     */
    private void doCancelPay(int orderId) {

        showProcessDialog((Activity) mCtx, R.layout.loading_process_dialog_color);
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        final RequestBody body = commonPramas(orderId);
        Observable<CommonBean> observable = httpPostService.getCancelOrderInfo(body);
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
                        Toast.makeText(mCtx, "网络请求异常,请重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        if (100 == commonBean.getCode()) { // 请求成功
                            Intent it = new Intent();
                            switch (mFrom) {
                                case 0: // 全部来源
                                    it.setAction("com.allOrder.refresh");
                                    mCtx.sendBroadcast(it);
                                    break;
                                case 2: // 未支付来源
                                    it.setAction("com.unpaidOrder.refresh");
                                    mCtx.sendBroadcast(it);
                                    break;
                            }
                        } else { // 请求失败
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

    /**
     * 公共请求参数
     *
     * @return
     */
    private RequestBody commonPramas(int orderId) {

        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(mCtx);
        try {
            // 测试数据
            obj.put("userId", commParam.getUserId());
            // 订单id
            obj.put("orderId", orderId);
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
