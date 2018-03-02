package jc.cici.android.atom.ui.tiku;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import jc.cici.android.R;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.HttpPostService;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.ui.tiku.newDoExam.KnowledgeTestActivity;
import jc.cici.android.atom.ui.tiku.newDoExam.TestContentFragment;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CardAnswer extends jc.cici.android.atom.base.BaseActivity implements OnClickListener {

    private GridView cardGrid;
    private RelativeLayout backBtn_layout;
    private Button sumbitBtn;
    private ArrayList<Card> mCardList;
    private AnswerAdapter adapter;
    @SuppressLint("UseSparseArrays")
    private HashMap<Integer, View> viewMap = new HashMap<Integer, View>();
    private int count = 0;

    private int userId;// 用户id
    private int testPaperID; // 试卷id
    // 班级id
    private int classid;
    // 阶段id
    private int stageid;
    // 课程id
    private int lessonid;
    // 是否在线(1:在线，其他面授)
    private int isOnline;
    // 面授情况传递id
    private int lessChildId;
    // 在线情况传递 科目id
    private int subjectid;
    private String studyKey;
    private String testTime; // 测试时间
    private HttpUtils httpUtils = new HttpUtils();
    private String name; // 当前学习标题

    // 传递过来的类型
    private String CardAnwserType;
    private Dialog mDialog;
    private SubmitQuesAnswer submitQuesAnswer;
    private CommParam commParam;

    @Override
    protected int getLayoutId() {
        return R.layout.card_view;
    }

    @Override
    protected int getFragmentContentId() {
        return 0;
    }

    @SuppressWarnings("unchecked")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutId());
        mCardList = (ArrayList<Card>) getIntent().getSerializableExtra(
                "pageStatus");
        userId = getIntent().getIntExtra("userId", 0);
        testPaperID = getIntent().getIntExtra("TestPaperID", 0);
        classid = getIntent().getIntExtra("classId", 0);
        stageid = getIntent().getIntExtra("stageId", 0);
        lessonid = getIntent().getIntExtra("lessonId", 0);
        isOnline = getIntent().getIntExtra("isOnline", 0);
        lessChildId = getIntent().getIntExtra("LessonChildId", 0);
        subjectid = getIntent().getIntExtra("levelId", 0);
        studyKey = getIntent().getStringExtra("studyKey");
        testTime = getIntent().getStringExtra("time");
        CardAnwserType = getIntent().getStringExtra("CardAnwserType");

        System.out.println("isOnline : " + isOnline);
        System.out.println("lessChildId : " + lessChildId);
        System.out.println("subjectid : " + subjectid);
        System.out.println("答题卡获取的 : " + CardAnwserType);
        // 获取传递当前标题
        name = getIntent().getStringExtra("name");
        cardGrid = (GridView) findViewById(R.id.gridBtn_view);
        backBtn_layout = (RelativeLayout) findViewById(R.id.backBtn_layout);
        sumbitBtn = (Button) findViewById(R.id.submit_card);
        commParam = new CommParam(this);
        backBtn_layout.setOnClickListener(this);
        sumbitBtn.setOnClickListener(this);
        // 加载数据
        showProcessDialog(CardAnswer.this,
                R.layout.loading_process_dialog_color);
    }

    /**
     * 自定义进度条
     */
    private void showProcessDialog(Context ctx, int layout) {
        mDialog = new AlertDialog.Builder(ctx, R.style.showdialog).create();
        mDialog.show();
        // 注意此处要放在show之后 否则会报异常
        mDialog.setContentView(layout);
        adapter = new AnswerAdapter();
        cardGrid.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {
            case R.id.backBtn_layout: // 返回按钮监听
                this.finish();

                break;
            case R.id.submit_card: // 提交按钮监听
                final Dialog dialog = new Dialog(CardAnswer.this,
                        R.style.NormalDialogStyle);
                dialog.setContentView(R.layout.answerz_submit_dialog);
                TextView txt = (TextView) dialog.findViewById(R.id.txt_dialog);
                Button go_Btn = (Button) dialog.findViewById(R.id.go_on);
                Button ensure_Btn = (Button) dialog.findViewById(R.id.sumbit_btn);

                for (Card c : mCardList) {
                    if (c.isStatus() == true) {
                        count++;
                    }
                }

                if (count < mCardList.size()) {
                    txt.setText("总共" + mCardList.size() + "道题,未答" + (mCardList.size() - count) + "道题");
                } else {
                    txt.setText("总共" + mCardList.size() + "道题,未答" + (mCardList.size() - count) + "道题");
                }

                dialog.show();
                go_Btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();

                    }
                });
                ensure_Btn.setOnClickListener(new OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        if (httpUtils.isNetworkConnected(CardAnswer.this)) {
                            if (!"".equals(studyKey)) {
                                SubmitTask task = new SubmitTask();
                                task.execute();
                            } else {
                                // 普通试卷提交答案
                                submitTest();
                            }

                        } else {
                            Toast.makeText(CardAnswer.this, "提交失败，请检查网络设置！",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });

                break;

            default:
                break;
        }
    }

    class SubmitTask extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... param) {
            JSONObject obj = new JSONObject();
            String[] str = testTime.split(":");
            int doTime = Integer.parseInt(str[0]) * 60 + Integer.parseInt(str[1]);
            try {
                obj.put("userId", commParam.getUserId());
                obj.put("appName", commParam.getAppname());
                obj.put("oauth", commParam.getOauth());
                obj.put("client", commParam.getClient());
                obj.put("timeStamp", commParam.getTimeStamp());
                obj.put("version", commParam.getVersion());
                obj.put("paperId", testPaperID);
                obj.put("classId", classid);
                obj.put("stageId", stageid);
                obj.put("lessonId", lessonid);
                obj.put("paperDoneTime", doTime);
                if (1 == isOnline) { // 在线情况
                    // 目录id
                    obj.put("levelId", subjectid);
                } else { // 面授情况
                    obj.put("lessonChildId", lessChildId);
                }
                System.out.println("studyKey >>>:" + studyKey);
                obj.put("studyKey", studyKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
            submitQuesAnswer = HttpUtils.getInstance().getSubmitQuesAnswer(
                    Const.URL + Const.SubmitQuesanswerAPI, obj);
            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            if ("100".equals(submitQuesAnswer.getCode())) {
                // 答案提交成功
                Toast.makeText(CardAnswer.this, "答案提交成功", Toast.LENGTH_SHORT)
                        .show();
                Intent cIntent = new Intent(CardAnswer.this,
                        CardResultActivity.class);
                // 传递试卷id
                cIntent.putExtra("TestPPKID", testPaperID);
                // 班级id
                cIntent.putExtra("classId", classid);
                // 阶段id
                cIntent.putExtra("stageId", stageid);
                // 课程id
                cIntent.putExtra("lessonId", lessonid);
                cIntent.putExtra("isOnline", isOnline);
                cIntent.putExtra("LessonChildId", lessChildId);
                // 科目id
                cIntent.putExtra("levelId", subjectid);
                // 答题用时
                cIntent.putExtra("time", testTime);
                // 试卷名
                cIntent.putExtra("name", name);
                cIntent.putExtra("studyKey", studyKey);
                TiKuContentFragment.handler.sendEmptyMessage(0);
                CardAnswer.this.startActivity(cIntent);
                finish();
            } else {
                // 答案提交失败
                Toast.makeText(CardAnswer.this, "答案提交失败", Toast.LENGTH_SHORT)
                        .show();
            }
            super.onPostExecute(result);
        }

    }

    /**
     * 提交试卷
     */
    private void submitTest() {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostService httpPostService = retrofit.create(HttpPostService.class);
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {
            // 用户id
            obj.put("userId", userId);
            obj.put("paperId", testPaperID);
            if (null!= testTime &&testTime.contains(":")) {
                String[] str = testTime.split(":");
                int doTime = Integer.parseInt(str[0]) * 60 + Integer.parseInt(str[1]);
                obj.put("paperDoneTime", doTime);
            } else {
                obj.put("paperDoneTime", testTime);
            }
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            // 测试加密数据
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        Observable<CommonBean> observable = httpPostService.submitPaperInfo(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        Toast.makeText(CardAnswer.this, "网络异常，试卷提交失败", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean commonBean) {
                        if (100 == commonBean.getCode()) {
                            Intent cIntent = new Intent(CardAnswer.this,
                                    CardResultActivity.class);
                            // 传递试卷id
                            cIntent.putExtra("TestPPKID", testPaperID);
                            // 班级id
                            cIntent.putExtra("classId", 0);
                            // 阶段id
                            cIntent.putExtra("stageId", 0);
                            // 课程id
                            cIntent.putExtra("lessonId", 0);
                            // 科目id
                            cIntent.putExtra("levelId", 0);
                            cIntent.putExtra("isOnline", 0);
                            cIntent.putExtra("LessonChildId", 0);
                            // 答题用时
                            cIntent.putExtra("time", testTime);
                            // 试卷名
                            cIntent.putExtra("name", name);
                            cIntent.putExtra("studyKey", "");
                            TestContentFragment.handler.sendEmptyMessage(0);
                            CardAnswer.this.startActivity(cIntent);
                            finish();
                        } else {
                            Toast.makeText(CardAnswer.this, commonBean.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    class AnswerAdapter extends BaseAdapter {

        @Override
        public int getCount() {

            return mCardList.size();
        }

        @Override
        public Object getItem(int postion) {

            return postion;
        }

        @Override
        public long getItemId(int position) {

            return position;
        }

        class ViewHolder {
            private ImageView flagImg;
            private Button sButton;
        }

        @Override
        public View getView(final int position, View convertView,
                            ViewGroup parent) {
            ViewHolder viewHolder;
            if (!viewMap.containsKey(position) || viewMap.get(position) == null) {
                convertView = LayoutInflater.from(CardAnswer.this).inflate(
                        R.layout.card_btn, null);
                viewHolder = new ViewHolder();
                viewHolder.flagImg = (ImageView) convertView
                        .findViewById(R.id.flag_img);
                viewHolder.sButton = (Button) convertView
                        .findViewById(R.id.asbtn_card);
                convertView.setTag(viewHolder);
                viewMap.put(position, convertView);
            } else {
                convertView = viewMap.get(position);
                viewHolder = (ViewHolder) convertView.getTag();
            }
            if (viewMap.size() > 20) {
                synchronized (convertView) {
                    for (int i = 1; i < cardGrid.getFirstVisiblePosition() - 3; i++) {
                        viewMap.remove(i);
                    }
                    for (int i = cardGrid.getLastVisiblePosition() + 3; i < getCount(); i++) {
                        viewMap.remove(i);
                    }
                }
            }
            // 获取按钮文字
            String txt = position + 1 + "";
            // 设置文字
            viewHolder.sButton.setText(txt);
            // 设置文字大小
            viewHolder.sButton.setTextSize(21);
            // 获取按钮点击状态
            if (position == mCardList.get(position).getPosition()) {// 当前positon与点击position相同
                // 获取选项是否选择
                boolean status = mCardList.get(position).isStatus();
                // 获取是否设置标记
                final boolean flag = mCardList.get(position).isFlag();
                if (status) { // 选择(已作答)
                    viewHolder.sButton
                            .setBackgroundResource(R.drawable.btn_yizuo_weibijiao);
                    viewHolder.sButton
                            .setTextColor(Color.parseColor("#ffffff"));
                }
                if (flag & status) { // 已标记
                    viewHolder.sButton
                            .setBackgroundResource(R.drawable.btn_yizuo_bijiao);
                    viewHolder.sButton
                            .setTextColor(Color.parseColor("#ffffff"));
                } else if (flag) {
                    viewHolder.sButton
                            .setBackgroundResource(R.drawable.btn_weizuo_yibijiao);
                    viewHolder.sButton
                            .setTextColor(Color.parseColor("#dd5555"));
                }
                viewHolder.sButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if ("My_Fragment".equals(CardAnwserType)) {
                            Intent cIntent = new Intent(CardAnswer.this,
                                    MyQuestionActivity.class);
                            cIntent.putExtra("pageID", position);
                            cIntent.putExtra("flag", flag);
                            setResult(1, cIntent);
                            finish();
                        } else if ("KnowledgeTestActivity".equals(CardAnwserType)) {
                            Intent cIntent = new Intent(CardAnswer.this,
                                    KnowledgeTestActivity.class);
                            cIntent.putExtra("pageID", position);
                            cIntent.putExtra("flag", flag);
                            setResult(1, cIntent);
                            finish();
                        }
                    }
                });
            }
            mDialog.dismiss();
            return convertView;
        }

    }
}
