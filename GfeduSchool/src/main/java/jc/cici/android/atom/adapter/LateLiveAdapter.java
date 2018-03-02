package jc.cici.android.atom.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import org.json.JSONObject;

import java.util.ArrayList;

import cn.jun.live.LiveClassActivity;
import cn.jun.live.LiveClassXiLieActivity;
import cn.jun.live.LiveH5Activity;
import cn.jun.live.LiveRoomActivity;
import jc.cici.android.R;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.BookBean;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.bean.LiveContent;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.ToolUtils;
import jc.cici.android.atom.view.GlideCircleTransform;
import jc.cici.android.atom.view.ShowTimeTextView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * 首页最近直播适配器
 * Created by atom on 2017/11/15.
 */

public class LateLiveAdapter extends BaseRecycleerAdapter<LiveContent.LateContent, LateLiveAdapter.MyHolder> {

    private Context context;
    private ArrayList<LiveContent.LateContent> mLateList;
    private SharedPreferences sp;
    // 获取用户id
    private int userId;
    private Handler handle;
    private int isBeginLive;

    public LateLiveAdapter(Context context, ArrayList<LiveContent.LateContent> items, Handler handle) {
        super(context, items);
        this.context = context;
        this.mLateList = items;
        this.handle = handle;
        sp = context.getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);
    }


    @Override
    public MyHolder onCreateViewHolder(View view, int viewType) {
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(final MyHolder holder, final LiveContent.LateContent item, final int position) {

        // 老师图像
        Glide.with(context).load(item.getLecturer_Img())
                .placeholder(R.drawable.icon_avatar) //加载中显示的图片
                .error(R.drawable.icon_avatar) //加载失败时显示的图片
                .crossFade(1000) //淡入显示的时间,注意:如果设置了这个,则必须要去掉asBitmap
                .override(280, 186) // 设置最终显示图片大小
                .centerCrop() // 中心剪裁
                .skipMemoryCache(true) // 跳过缓存
                .diskCacheStrategy(DiskCacheStrategy.RESULT) // 磁盘缓存最终设置图片
                .transform(new GlideCircleTransform(context)) // 设置圆角
                .into(holder.teacherImg);
        // 直播日期时间
        if (null != item.getCS_Date() && !"".equals(item.getCS_Date())) {
            String year = item.getCS_Date().substring(0, 4);
            holder.liveTime_txt.setText(year + "年" + item.getCS_DateShort()
                    + " " + item.getCS_StartTime() + "-" + item.getCS_EndTime());
        }
        // 直播课程名
        holder.liveCourseName_txt.setText(ToolUtils.strReplaceAll(item.getClass_Name()));
        // 判断是否可预约 0 不可预约，1:可预约
        final int isBook = item.getCS_IsReserve();
        // 是否预约过 1：是 0：否
        final int hasBook = item.getHasBook();
        // 是否购买过 1：是 0：否
        final int hasBuy = item.getHasBuy();
        // 是否已经开始直播
        final int beginLive = item.getIsLiveBegin();
        // 是否已经结束直播
        final int endLive = item.getIsLiveEnd();
        // 判断用户是否登录
        if (0 != userId) {  // 用户已登录
            if (1 == isBook) { // 如果可预约
                if (1 == hasBook) { // 已经预约
                    if (1 == beginLive) { // 直播已经开始
                        holder.book_btn.setText("进入直播");
                        holder.book_btn.setBackgroundResource(R.drawable.bg_coming_live);
                    } else {
                        holder.book_btn.setText("已预约");
                        holder.book_btn.setBackgroundResource(R.drawable.booked);
                    }
                } else { // 未预约
                    holder.book_btn.setText("立即预约");
                    holder.book_btn.setBackgroundResource(R.drawable.bg_book);
                }
                // 预约人数
                holder.count_booked.setText(item.getAboutNum() + "人预约");
            } else { // 如果不可预约(购买课程)
                if (1 == hasBuy) { // 已经购买
                    if (1 == beginLive) { // 直播已经开始
                        holder.book_btn.setText("进入直播");
                        holder.book_btn.setBackgroundResource(R.drawable.bg_coming_live);
                    } else {
                        holder.book_btn.setText("已购买");
                        holder.book_btn.setBackgroundResource(R.drawable.booked);
                    }
                } else { // 未购买
                    holder.book_btn.setText("立即购买");
                    holder.book_btn.setBackgroundResource(R.drawable.bg_book);
                }
                // 预约人数
                holder.count_booked.setText(item.getAboutNum() + "人购买");
            }
        } else { // 用户未登录
            switch (isBook) {
                case 0: // 不可以预约(收费课程)
                    holder.book_btn.setText("立即购买");
                    holder.book_btn.setBackgroundResource(R.drawable.bg_book);
                    // 购买人数
                    holder.count_booked.setText(item.getAboutNum() + "人购买");
                    break;
                case 1: // 可预约
                    holder.book_btn.setText("立即预约");
                    holder.book_btn.setBackgroundResource(R.drawable.bg_book);
                    // 预约人数
                    holder.count_booked.setText(item.getAboutNum() + "人预约");
                    break;
            }
        }
        // 预订按钮监听
        holder.book_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (0 != userId) { // 用户已登录
                    if (1 == isBook) { // 如果可预约
                        if (1 == hasBook) { // 已经预约
                            if (1 == beginLive) { // 直播已经开始
                                int csID = item.getCS_PKID();
                                Intent it = new Intent(context, LiveRoomActivity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("classId", item.getClass_PKID());
                                bundle.putInt("scheduleId", csID);
                                bundle.putInt("searchType", isBeginLive);
                                it.putExtras(bundle);
                                context.startActivity(it);
                            } else {
                                Toast.makeText(context, "已预约", Toast.LENGTH_SHORT).show();
                            }
                        } else { // 未预约
                            // 获取课表id
                            int classId = item.getClass_PKID();
                            // 网络请求预约
                            addLiveBook(classId, holder, position);
                        }
                    } else { // 如果不可预约(购买课程)
                        if (1 == hasBuy) { // 已经购买
                            if (1 == beginLive) { // 直播已经开始
                                int csID = item.getCS_PKID();
                                Intent it = new Intent(context, LiveH5Activity.class);
                                Bundle bundle = new Bundle();
                                bundle.putInt("scheduleId", csID);
                                bundle.putInt("searchType", isBeginLive);
                                it.putExtras(bundle);
                                context.startActivity(it);
                            } else {
                                Toast.makeText(context, "已购买", Toast.LENGTH_SHORT).show();
                            }
                        } else { // 未购买 跳转详情去购买
                            // 获取班级id
                            int classPkID = mLateList.get(position).getClass_PKID();
                            // 获取系列直播or普通直播
                            int classFrom = mLateList.get(position).getClass_Form();
                            switch (classFrom) {
                                case 1: // 系列直播
//                                    Intent intent = new Intent(context, OnlineCourseDetailsActivity.class);
                                    Intent intent = new Intent(context, LiveClassXiLieActivity.class);
                                    Bundle bundle = new Bundle();
//                                    bundle.putInt("Product_PKID", classPkID);
                                    bundle.putInt("Class_PKID", classPkID);
                                    intent.putExtras(bundle);
                                    context.startActivity(intent);
                                    break;
                                case 2: // 正常直播
//                                    Intent normalIt = new Intent(context, OnlineCourseDetailsActivity.class);
                        Intent normalIt = new Intent(context, LiveClassActivity.class);
                                    Bundle norBundle = new Bundle();
//                                    norBundle.putInt("Product_PKID", classPkID);
                        norBundle.putInt("Class_PKID", classPkID);
                                    normalIt.putExtras(norBundle);
                                    context.startActivity(normalIt);
                                    break;
                            }
                        }
                    }
                } else { // 用户未登录
                    Toast.makeText(context, "您还未登录金程网校，请先登录", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    /**
     * 添加预约
     *
     * @param classId
     * @param holder
     */
    private void addLiveBook(int classId, final MyHolder holder, final int position) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        RequestBody body = commParams(classId);
        Observable<CommonBean<BookBean>> observable = httpPostService.addLiveBookInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<BookBean>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(context, "预订失败,网络连接不给力，请稍后重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<BookBean> commonBean) {
                        if (100 == commonBean.getCode()) {
                            isBeginLive = commonBean.getBody().getIsLiveBegin();
                            if (0 == isBeginLive) { // 已预约但未开始直播
                                holder.book_btn.setText("已预约");
                                holder.book_btn.setBackgroundResource(R.drawable.booked);
                            } else if (1 == isBeginLive) { // 预约成功，直播已开始
                                holder.book_btn.setText("进入直播");
                                holder.book_btn.setBackgroundResource(R.drawable.bg_coming_live);
                            }
                            final Dialog mDialog = new Dialog(context,
                                    R.style.NormalDialogStyle);
                            mDialog.setContentView(R.layout.dialog_book);
                            mDialog.setCanceledOnTouchOutside(false);
                            mDialog.show();
                            ShowTimeTextView timeCutDown_txt = (ShowTimeTextView) mDialog.findViewById(R.id.timeCutDown_txt);
                            timeCutDown_txt.setTime(5, mDialog, handle);
                            timeCutDown_txt.beginRun();
                        } else { // 预订失败
                            Toast.makeText(context, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    /**
     * 预约公共参数
     *
     * @return
     */
    private RequestBody commParams(int classId) {
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(context);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("classid", classId);
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

    @Override
    public int getLayoutId() {
        return R.layout.item_singleline_content_live;
    }

    class MyHolder extends RecyclerView.ViewHolder {
        // 老师图片
        ImageView teacherImg;
        // 直播时间
        TextView liveTime_txt;
        // 直播课程名
        TextView liveCourseName_txt;
        // 预订按钮
        Button book_btn;
        // 预订人数
        TextView count_booked;

        public MyHolder(View itemView) {
            super(itemView);
            teacherImg = (ImageView) itemView.findViewById(R.id.teacherImg);
            liveTime_txt = (TextView) itemView.findViewById(R.id.liveTime_txt);
            liveCourseName_txt = (TextView) itemView.findViewById(R.id.liveCourseName_txt);
            book_btn = (Button) itemView.findViewById(R.id.book_btn);
            count_booked = (TextView) itemView.findViewById(R.id.count_booked);
        }

    }

    @Override
    public int getItemCount() {
        return null == mLateList ? 0 : mLateList.size();
    }
}
