package cn.jun.view;


import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.jun.bean.MyAssessment;
import cn.jun.courseinfo.activity.OnlineCourseDetailsAloneActivityTwo;
import cn.jun.utils.HttpPostServer;
import cn.jun.utils.HttpUtils;
import jc.cici.android.R;
import jc.cici.android.atom.base.BaseActivity;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.tiku.newDoExam.KnowledgeTestActivity;
import jc.cici.android.atom.utils.ToolUtils;
import lecho.lib.hellocharts.listener.LineChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.view.LineChartView;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class KLineView extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    //本地存储
    private SharedPreferences sp;
    //用户ID
    private int userId;
    private HttpUtils httpUtils = new HttpUtils();
    //数据源
    private CommonBean<MyAssessment> datas;
    private int ProjectID;
    //标题
    private TextView ac_title;
    private String title_tv;
    private TextView title1;
    //答题数量
    private TextView datishu;
    //正确率
    private TextView zhengquelv;
    //学习建议
    private TextView xxjy_content;
    private TextView ckxqBtn;
    //薄弱知识点
    private RelativeLayout click_layout;
    private RelativeLayout click_layout2;
    private TextView item_title;
    private TextView item_title2;
    //更多
    private TextView gengduoBtn;
    //K线
    private LineChartView chart;
    private LineChartData data;
    private boolean hasAxes = true;
    private boolean hasAxesNames = true;
    //图表的数据--正确率
    private int[] score;

    //    int[] score = {20};//图表的数据--正确率
    //X轴的标注
    private String[] dateTime;
    //    String[] dateTime = {"05-18", "05-19", "05-20", "05-21", "05-22"};//X轴的标注
//    String[] dateTime = {"05-18"};//X轴的标注
    private List<AxisValue> mAxisXValues = new ArrayList<AxisValue>();
    private List<PointValue> mPointValues = new ArrayList<PointValue>();
    //学习建议跳转到版型详情的ID
    private int Product_PKID;

    private LinearLayout brzsd_layout;
//    private ImageView line_br1,line_br2;

    @Override
    protected int getLayoutId() {
        return R.layout.k_view;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            ProjectID = bundle.getInt("ProjectID");
            title_tv = bundle.getString("title_tv");
            Log.i("title_tv == > ", "" + title_tv);
        }

        sp = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
        userId = sp.getInt("S_ID", 0);
        initFinishView();
        if (httpUtils.isNetworkConnected(this)) {
            initKLineDate();

        }

    }

    @Override
    protected void onResume() {
        super.onResume();


    }

    private void initKLineView() {
        //填充数据
        title1 = (TextView) findViewById(R.id.title1);
        title1.setText("根据您最近" + datas.getBody().getTpaperCount() + "次试卷答题情况生成");
        if (datas.getBody().getTpaperCount() > 0) {
            if (1 == datas.getBody().getTpaperList().size()) {
                Log.i("一条数据 == ", " === ");
                score = new int[2];
                dateTime = new String[2];
                dateTime[0] = datas.getBody().getTpaperList().get(0).getSubTime();
                dateTime[1] = "0";
                score[0] = Integer.parseInt(datas.getBody().getTpaperList().get(0).getRightRatio());
                score[1] = 0;
            } else {
                Log.i("一条数据 +++ == ", " === ");
                score = new int[datas.getBody().getTpaperCount()];
                dateTime = new String[datas.getBody().getTpaperCount()];
                for (int i = 0; i < dateTime.length; i++) {
                    score[i] = Integer.parseInt(datas.getBody().getTpaperList().get(i).getRightRatio());
                    dateTime[i] = datas.getBody().getTpaperList().get(i).getSubTime();
                }
            }

        } else {
            score = new int[0];
            dateTime = new String[0];
        }
        chart = (LineChartView) findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        getAxisXLables();
        getAxisPoints();
        generateData();
        chart.setViewportCalculationEnabled(false);
        chart.setZoomEnabled(false);//设置是否支持缩放
        resetViewport();
    }

    private void resetViewport() {
        Viewport v = new Viewport(chart.getMaximumViewport());
        v.left = 0;
        //按照X轴左边显示最多几个
        v.right = 4;
        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v);
    }

    private void generateData() {
        Line line = new Line(mPointValues).setColor(Color.parseColor("#ffffff"));
        List<Line> lines = new ArrayList<Line>();
        line.setShape(ValueShape.CIRCLE);//折线图上每个数据点的形状  这里是圆形 （有三种 ：ValueShape.SQUARE  ValueShape.CIRCLE  ValueShape.SQUARE）
        line.setCubic(false);//曲线是否平滑
        line.setStrokeWidth(2);//线条的粗细，默认是3
        line.setFilled(false);//是否填充曲线的面积
//        line.setHasLabels(false);//曲线的数据坐标是否加上备注
        line.setHasLabelsOnlyForSelected(false);//点击数据坐标提示数据（设置了这个line.setHasLabels(true);就无效）
        line.setHasLines(true);//是否用直线显示。如果为false 则没有曲线只有点显示
        line.setHasPoints(true);//是否显示圆点 如果为false 则没有原点只有点显示
        lines.add(line);

        data = new LineChartData(lines);

        if (hasAxes) {
            Axis axisX = new Axis();
            Axis axisY = new Axis();
//            axisX.setHasLines(true);
//            axisY.setHasLines(true);
            if (hasAxesNames) {
                axisX.setName("时间");
                axisX.setValues(mAxisXValues);
                axisX.setTextSize(11);
                axisY.setTextSize(11);
//                    axisX.setMaxLabelChars(3);

                axisY.setName("正确率");
            }
            data.setAxisXBottom(axisX);
            data.setAxisYLeft(axisY);
        } else {
            data.setAxisXBottom(null);
            data.setAxisYLeft(null);
        }

        data.setBaseValue(Float.NEGATIVE_INFINITY);
        chart.setLineChartData(data);

    }

    /**
     * X 轴的显示
     */
    private void getAxisXLables() {
        for (int i = 0; i < dateTime.length; i++) {
            mAxisXValues.add(new AxisValue(i).setLabel(dateTime[i]));
        }
    }

    /**
     * 图表的每个点的显示
     */
    private void getAxisPoints() {
        for (int i = 0; i < score.length; i++) {
            mPointValues.add(new PointValue(i, score[i]));
            Log.i("score -- ", "" + score[i]);
        }
    }

    private void initView() {
        brzsd_layout = (LinearLayout) findViewById(R.id.brzsd_layout);
        datishu = (TextView) findViewById(R.id.datishu);
        zhengquelv = (TextView) findViewById(R.id.zhengquelv);
        xxjy_content = (TextView) findViewById(R.id.xxjy_content);
        ckxqBtn = (TextView) findViewById(R.id.ckxqBtn);
        item_title = (TextView) findViewById(R.id.item_title);
        item_title2 = (TextView) findViewById(R.id.item_title2);
        click_layout = (RelativeLayout) findViewById(R.id.click_layout);
        click_layout2 = (RelativeLayout) findViewById(R.id.click_layout2);
        click_layout.setOnClickListener(this);
        click_layout2.setOnClickListener(this);

        //答题数量
        datishu.setText("答题量 : " + datas.getBody().getQuesCount() + "题");
        //正确率
        zhengquelv.setText("正确率 : " + datas.getBody().getRightRatio());
        //学习建议
        if (datas.getBody().getClassCount() > 0) {
            Product_PKID = datas.getBody().getClassList().get(0).getClassId();
            xxjy_content.setText(datas.getBody().getClassList().get(0).getClassName());
            ckxqBtn.setText("查看详情");
            ckxqBtn.setOnClickListener(this);
        } else {
            xxjy_content.setText("");
            ckxqBtn.setText("");
        }

        //薄弱知识点
        if (datas.getBody().getCoursewareCount() > 0) {
            if (datas.getBody().getCoursewareCount() == 1) {
                click_layout.setVisibility(View.VISIBLE);
                click_layout2.setVisibility(View.GONE);
                item_title.setText(datas.getBody().getCoursewareList().get(0).getKnowledgeName());
            } else if (datas.getBody().getCoursewareCount() == 2) {
                click_layout.setVisibility(View.VISIBLE);
                click_layout2.setVisibility(View.VISIBLE);
                item_title.setText(datas.getBody().getCoursewareList().get(0).getKnowledgeName());
                item_title2.setText(datas.getBody().getCoursewareList().get(1).getKnowledgeName());
            } else {
                click_layout.setVisibility(View.VISIBLE);
                click_layout2.setVisibility(View.VISIBLE);
                item_title.setText(datas.getBody().getCoursewareList().get(0).getKnowledgeName());
                item_title2.setText(datas.getBody().getCoursewareList().get(1).getKnowledgeName());
            }
        } else {
            brzsd_layout.setVisibility(View.GONE);
            click_layout.setVisibility(View.GONE);
            click_layout2.setVisibility(View.GONE);
        }

    }

    private void initFinishView() {
        ac_title = (TextView) findViewById(R.id.ac_title);
        ac_title.setText(title_tv);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        gengduoBtn = (TextView) findViewById(R.id.gengduoBtn);
        gengduoBtn.setOnClickListener(this);
    }

    //能力评估
    private void initKLineDate() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostServer httpPostService = retrofit.create(HttpPostServer.class);
        RequestBody body = commParam();
        Observable<CommonBean<MyAssessment>> observable = httpPostService.getMyAssessment(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<MyAssessment>>() {
                    @Override
                    public void onCompleted() {
                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(KLineView.this, "网络异常，请返回重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<MyAssessment> myAssessment) {
                        if (100 == myAssessment.getCode()) {
                            datas = myAssessment;
                            initView();
                            initKLineView();
                        } else {
                            Toast.makeText(KLineView.this, myAssessment.getMessage(), Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }
                });
    }

    //公共参数
    private RequestBody commParam() {
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {
            // 用户id
            obj.put("userId", userId);
//            obj.put("userId", 26146);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
            obj.put("ProjectID", ProjectID);
//            obj.put("ProjectID", 40);

        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.ckxqBtn://学习建议查看详情
                Intent ckxqIntent = new Intent(this, OnlineCourseDetailsAloneActivityTwo.class);
                Bundle ckxqBundle = new Bundle();
                ckxqBundle.putInt("Product_PKID", Product_PKID);
                ckxqIntent.putExtras(ckxqBundle);
                startActivity(ckxqIntent);
                break;

            case R.id.gengduoBtn://薄弱知识点更多
                Intent br_Intent = new Intent(this, BRZSDActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt("ProjectID", ProjectID);
                br_Intent.putExtras(bundle);
                startActivity(br_Intent);
                break;

            case R.id.click_layout:
                int knowledgeId = datas.getBody().getCoursewareList().get(0).getKnowledgeId();
                String title = datas.getBody().getCoursewareList().get(0).getKnowledgeName();
                Intent cK1_it = new Intent(this, KnowledgeTestActivity.class);
                Bundle bundle_ck1 = new Bundle();
                bundle_ck1.putInt("knowledgeId", knowledgeId);
                bundle_ck1.putInt("projectId", ProjectID);
                // 答案类型1:表示每日一题，2表示知识点做题
                bundle_ck1.putInt("answerType", 2);
                bundle_ck1.putString("title", title);
                cK1_it.putExtras(bundle_ck1);
                startActivity(cK1_it);
                break;

            case R.id.click_layout2:
                int knowledgeId2 = datas.getBody().getCoursewareList().get(1).getKnowledgeId();
                String title2 = datas.getBody().getCoursewareList().get(1).getKnowledgeName();
                Intent cK2_it = new Intent(this, KnowledgeTestActivity.class);
                Bundle bundle_ck2 = new Bundle();
                bundle_ck2.putInt("knowledgeId", knowledgeId2);
                bundle_ck2.putInt("projectId", ProjectID);
                // 答案类型1:表示每日一题，2表示知识点做题
                bundle_ck2.putInt("answerType", 2);
                bundle_ck2.putString("title", title2);
                cK2_it.putExtras(bundle_ck2);
                startActivity(cK2_it);
                break;
        }
    }

    private class ValueTouchListener implements
            LineChartOnValueSelectListener {

        @Override
        public void onValueSelected(int lineIndex, int pointIndex,
                                    PointValue value) {
//            Toast.makeText(KLineView.this, "选择: " + value,
//                    Toast.LENGTH_SHORT).show();
            Toast.makeText(KLineView.this, "正确率: " + value.getY() + "%",
                    Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onValueDeselected() {
            // TODO Auto-generated method stub

        }

    }
}
