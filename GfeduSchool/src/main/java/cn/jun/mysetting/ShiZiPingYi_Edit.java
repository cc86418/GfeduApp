package cn.jun.mysetting;


import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import cn.jun.bean.AddapPraiseBean;
import cn.jun.bean.Const;
import cn.jun.bean.RaiseQuestionBean;
import cn.jun.utils.HttpPostServer;
import cn.jun.utils.HttpUtils;
import cn.jun.view.ListViewForScrollView;
import jc.cici.android.R;
import jc.cici.android.atom.bean.CommonBean;
import jc.cici.android.atom.common.CommParam;
import jc.cici.android.atom.common.Global;
import jc.cici.android.atom.http.RetrofitOKManager;
import jc.cici.android.atom.utils.ToolUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class ShiZiPingYi_Edit extends Activity implements View.OnClickListener {
    //用户ID
    private int UserID;
    //工具
    private HttpUtils httpUtils = new HttpUtils();
    //返回
    private ImageView iv_back;
    //listView
//    private RecyclerView sz_list;
    private ListViewForScrollView sz_list2;
    //    private SZPY_Edit_RecyclerAdapter sz_adapter;
    private SZPY_Edit_ScrollAdapter sz_adapter;
    //数据源
    private RaiseQuestionBean RaiseQuestion = new RaiseQuestionBean();
    private ArrayList<RaiseQuestionBean> mlist;


    private ArrayList<String> Appraise_Content = new ArrayList<>();
    private String Appraise_String = "";
    private String Appraise_Opinion;

    private Button submit;//提交
    private EditText edittext; //编辑框
    private String editString = "";

    private String teach;//授课老师
    private TextView tx_1;
    private ImageView touxiang; //头像
    private String head;

    private int ClassScheduleID;//课程ID
    private int TemplateId;//模板ID

    //返回数据
    private AddapPraiseBean addapPraise = new AddapPraiseBean();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shizipingyi_activity_edit);

        Bundle bundle = getIntent().getExtras();
        if (null != bundle) {
            ClassScheduleID = bundle.getInt("ClassScheduleID");
            teach = bundle.getString("teach");
            head = bundle.getString("head");

            Log.i("ClassScheduleID ", "" + ClassScheduleID);
            Log.i("teach ", "" + teach);
            Log.i("head ", "" + head);

            HeadView();

        }

        if (httpUtils.isNetworkConnected(this)) {
            initData();
        } else {
            Toast.makeText(ShiZiPingYi_Edit.this, "请检查你的网络，返回重试", Toast.LENGTH_SHORT).show();
        }
    }

    public void HeadView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        //授课老师
        tx_1 = (TextView) findViewById(R.id.tx_1);
        tx_1.setText("授课老师: " + teach);
        //头像
        touxiang = (ImageView) findViewById(R.id.touxiang);
        Glide.with(this)
                .load(head)
                .placeholder(R.drawable.pic_kong)
                .into(touxiang);
    }

    private void initData() {
        Log.i("initData ", "initData ");
        ShiZiPingYiTask shiZiPingYiTask = new ShiZiPingYiTask();
        shiZiPingYiTask.execute();
    }

    class ShiZiPingYiTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                UserID = LoginPre.getInt("S_ID", 0);
            }
//            UserID = 123898;
            RaiseQuestion = httpUtils.getRaiseQuestion(Const.URL + Const.GetRaiseQuestionAPI, UserID, ClassScheduleID);

            return null;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == RaiseQuestion.getCode()) {
                if (RaiseQuestion.getBody().getListCount() > 0) {
                    mlist = new ArrayList<>();
                    mlist.add(RaiseQuestion);
                    initView();
                } else {//无数据

                }


            } else {//接口请求失败
                Toast.makeText(ShiZiPingYi_Edit.this, RaiseQuestion.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void initView() {

//        sz_list = (RecyclerView) findViewById(sz_list);
        sz_list2 = (ListViewForScrollView) findViewById(R.id.sz_list2);
        sz_adapter = new SZPY_Edit_ScrollAdapter(this, mlist);
        sz_list2.setAdapter(sz_adapter);

        //提交
        submit = (Button) findViewById(R.id.submit);
        submit.setOnClickListener(this);

        //编辑框
        edittext = (EditText) findViewById(R.id.edittext);

    }


    private class SZPY_Edit_ScrollAdapter extends BaseAdapter {
        private Activity activity;
        private ArrayList<RaiseQuestionBean> mlist;


        private SZPY_Edit_ScrollAdapter(Activity activity, ArrayList<RaiseQuestionBean> mlist) {
            this.activity = activity;
            this.mlist = mlist;
        }

        @Override
        public int getCount() {
            return mlist.get(0).getBody().getQuestionList() == null ? 0 : mlist.get(0).getBody().getQuestionList().size();
        }

        @Override
        public Object getItem(int position) {
            return mlist.get(0).getBody().getQuestionList().size();
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(activity).inflate(
                        R.layout.szpy_edit_child, parent, false);
                viewHolder.szpy_text = (TextView) convertView
                        .findViewById(R.id.szpy_text);
                viewHolder.radioGroupID = (RadioGroup) convertView
                        .findViewById(R.id.radioGroupID);
                viewHolder.rb_you = (RadioButton) convertView
                        .findViewById(R.id.rb_you);
                viewHolder.rb_liang = (RadioButton) convertView
                        .findViewById(R.id.rb_liang);
                viewHolder.rb_yiban = (RadioButton) convertView
                        .findViewById(R.id.rb_yiban);
                viewHolder.rb_cha = (RadioButton) convertView
                        .findViewById(R.id.rb_cha);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final RaiseQuestionBean.Body.QuestionList item = mlist.get(0).getBody().getQuestionList().get(position);
            //授课老师
            viewHolder.szpy_text.setText(item.getQuestionContent());
            final RadioGroup radioGroup = viewHolder.radioGroupID;
            viewHolder.rb_you.setText(item.getAnswers().get(0).getAnswerContent());
            viewHolder.rb_liang.setText(item.getAnswers().get(1).getAnswerContent());
            viewHolder.rb_yiban.setText(item.getAnswers().get(2).getAnswerContent());
            viewHolder.rb_cha.setText(item.getAnswers().get(3).getAnswerContent());

            viewHolder.radioGroupID.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    int ClickButtonId = group.indexOfChild(group.findViewById(checkedId));
                    Log.i("点击的按钮下标 111 -- ", "" + ClickButtonId);
                    Log.i("答案ID == > ", "" + item.getAnswers().get(ClickButtonId).getAnswerID());
                    // 1.优秀   2.好  3.一般   4.差
                    if (viewHolder.rb_you.getId() == checkedId) {
                        Log.i("选中的--- 1", "下标=> " + position + " ," + viewHolder.rb_you.getText().toString());
                        if (!item.isChoose()) {
//                            Appraise_String = item.getQuestionPKID() + "-" + "1";
                            Appraise_String = item.getQuestionPKID() + "-" + item.getAnswers().get(ClickButtonId).getAnswerID();
                            Log.i("传递的ID --- ", "" + Appraise_String);
                            Appraise_Content.add(Appraise_String);
                            item.setChoose(true);
                        }else {
                            Appraise_String = item.getQuestionPKID() + "-" + item.getAnswers().get(ClickButtonId).getAnswerID();
                            String left;
                            for (int i = 0; i < Appraise_Content.size(); i++) {
//                                String[] content = Appraise_Content.get(i).split("-");
//                                left = content[0];
//                                Log.i("left -- ",""+left );
//                                if(Appraise_Content.get(i).indexOf(left) != -1){
//
//                                }
                                if (Appraise_Content.get(i).contains(item.getQuestionPKID()+"")) {
                                    Appraise_Content.remove(i);
                                    Log.i("包含 -- ",""+i);
                                }
                            }
                            Appraise_Content.add(Appraise_String);
                            Gson s =  new Gson();
                            Log.i("删除后 === > ",""+ s.toJson(Appraise_Content).toString());
                        }

                    }
                    if (viewHolder.rb_liang.getId() == checkedId) {
                        Log.i("选中的--- 2", "下标=> " + position + " ," + viewHolder.rb_liang.getText().toString());
                        if (!item.isChoose()) {
//                            Appraise_String = item.getQuestionPKID() + "-" + "2";
                            Appraise_String = item.getQuestionPKID() + "-" + item.getAnswers().get(ClickButtonId).getAnswerID();
                            Appraise_Content.add(Appraise_String);
                            item.setChoose(true);
                            Log.i("传递的ID --- ", "" + Appraise_String);
                        }else {
                            Appraise_String = item.getQuestionPKID() + "-" + item.getAnswers().get(ClickButtonId).getAnswerID();
                            String left;
                            for (int i = 0; i < Appraise_Content.size(); i++) {
//                                String[] content = Appraise_Content.get(i).split("-");
//                                left = content[0];
//                                Log.i("left -- ",""+left );
//                                if(Appraise_Content.get(i).indexOf(left) != -1){
//
//                                }
                                if (Appraise_Content.get(i).contains(item.getQuestionPKID()+"")) {
                                    Appraise_Content.remove(i);
                                    Log.i("包含 -- ",""+i);
                                }
                            }
                            Appraise_Content.add(Appraise_String);
                            Gson s =  new Gson();
                            Log.i("删除后 === > ",""+ s.toJson(Appraise_Content).toString());
                        }

                    }
                    if (viewHolder.rb_yiban.getId() == checkedId) {
                        Log.i("选中的--- 3", "下标=> " + position + " ," + viewHolder.rb_yiban.getText().toString());
                        if (!item.isChoose()) {
//                            Appraise_String = item.getQuestionPKID() + "-" + "3";
                            Appraise_String = item.getQuestionPKID() + "-" + item.getAnswers().get(ClickButtonId).getAnswerID();
                            Appraise_Content.add(Appraise_String);
                            item.setChoose(true);
                            Log.i("传递的ID --- ", "" + Appraise_String);
                        } else {
                            Appraise_String = item.getQuestionPKID() + "-" + item.getAnswers().get(ClickButtonId).getAnswerID();
                            String left;
                            for (int i = 0; i < Appraise_Content.size(); i++) {
//                                String[] content = Appraise_Content.get(i).split("-");
//                                left = content[0];
//                                Log.i("left -- ",""+left );
//                                if(Appraise_Content.get(i).indexOf(left) != -1){
//
//                                }
                                if (Appraise_Content.get(i).contains(item.getQuestionPKID()+"")) {
                                    Appraise_Content.remove(i);
                                    Log.i("包含 -- ",""+i);
                                }
                            }
                            Appraise_Content.add(Appraise_String);
                            Gson s =  new Gson();
                            Log.i("删除后 === > ",""+ s.toJson(Appraise_Content).toString());
                        }

                    }
                    if (viewHolder.rb_cha.getId() == checkedId) {
                        Log.i("选中的--- 4", "下标=> " + position + " ," + viewHolder.rb_cha.getText().toString());
                        if (!item.isChoose()) {
//                            Appraise_String = item.getQuestionPKID() + "-" + "4";
                            Appraise_String = item.getQuestionPKID() + "-" + item.getAnswers().get(ClickButtonId).getAnswerID();
                            Appraise_Content.add(Appraise_String);
                            item.setChoose(true);
                            Log.i("传递的ID --- ", "" + Appraise_String);
                        }else {
                            Appraise_String = item.getQuestionPKID() + "-" + item.getAnswers().get(ClickButtonId).getAnswerID();
                            String left;
                            for (int i = 0; i < Appraise_Content.size(); i++) {
//                                String[] content = Appraise_Content.get(i).split("-");
//                                left = content[0];
//                                Log.i("left -- ",""+left );
//                                if(Appraise_Content.get(i).indexOf(left) != -1){
//
//                                }
                                if (Appraise_Content.get(i).contains(item.getQuestionPKID()+"")) {
                                    Appraise_Content.remove(i);
                                    Log.i("包含 -- ",""+i);
                                }
                            }
                            Appraise_Content.add(Appraise_String);
                            Gson s =  new Gson();
                            Log.i("删除后 === > ",""+ s.toJson(Appraise_Content).toString());
                        }

                    }
                }
            });

            return convertView;
        }
    }

    class ViewHolder {
        TextView szpy_text;
        RadioGroup radioGroupID;
        RadioButton rb_you;
        RadioButton rb_liang;
        RadioButton rb_yiban;
        RadioButton rb_cha;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;

            case R.id.submit:
                editString = edittext.getText().toString().trim();
                int TemplateId = mlist.get(0).getBody().getTemplateId();
                Appraise_Opinion = editString;
                if (!"".equals(Appraise_Content) && null != Appraise_Content) {
                    Gson s = new Gson();
                    Log.i("提交的数据 -- ", "" + s.toJson(Appraise_Content));
                    Log.i("提交的数据 edit -- ", "" + editString);
                    if (httpUtils.isNetworkConnected(this)) {
                        SubmitPingYiTask submitPingYiTask = new SubmitPingYiTask();
                        submitPingYiTask.execute(TemplateId);
//                        SubmitDate(TemplateId);
                    }

                } else {
                    Toast.makeText(this, "请对此课堂的老师做出评价", Toast.LENGTH_SHORT).show();
                }
                break;

        }
    }

    class SubmitPingYiTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... arg0) {
            int id = arg0[0];
            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                UserID = LoginPre.getInt("S_ID", 0);
            }
            addapPraise = httpUtils.getAddapPraiseBean(Const.URL + Const.GetAddAppraiseAPI, UserID, ClassScheduleID, Appraise_Content, editString, id);


            return null;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == addapPraise.getCode()) {
                Intent intent = new Intent(ShiZiPingYi_Edit.this, ShiZiOver.class);
                Bundle bundle = new Bundle();
                bundle.putInt("Appraise_PKID", addapPraise.getBody().getAppraise_PKID());
                intent.putExtras(bundle);
                startActivity(intent);
                finish();
            } else {//接口请求失败
                Toast.makeText(ShiZiPingYi_Edit.this, addapPraise.getMessage(), Toast.LENGTH_SHORT).show();
                Appraise_Content.clear();
                Appraise_Content = null;
            }
        }

    }


    private void SubmitDate(int TemplateId) {
        Retrofit retrofit = RetrofitOKManager.getinstance().doBaseRetrofit(Global.BASE_URL);
        HttpPostServer httpPostService = retrofit.create(HttpPostServer.class);
        RequestBody body = commParam(TemplateId);
        Observable<CommonBean<AddapPraiseBean>> observable = httpPostService.getAddapPraiseBean(body);
        observable.subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<CommonBean<AddapPraiseBean>>() {
                    @Override
                    public void onCompleted() {
//                        swipeRefreshLayout.setRefreshing(false);
                    }

                    @Override
                    public void onError(Throwable e) {
//                        swipeRefreshLayout.setRefreshing(false);
                        Toast.makeText(ShiZiPingYi_Edit.this, "网络异常，请返回重试", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNext(CommonBean<AddapPraiseBean> AddapPraiseBean) {
                        if (100 == AddapPraiseBean.getCode()) {
                            Log.i("提交评价 --- ", " 100 ====  ");
                            Intent intent = new Intent(ShiZiPingYi_Edit.this, ShiZiOver.class);
                            Bundle bundle = new Bundle();
                            bundle.putInt("Appraise_PKID", AddapPraiseBean.getBody().getBody().getAppraise_PKID());
                            intent.putExtras(bundle);
                            startActivity(intent);
                            finish();
                        } else {
                            Toast.makeText(ShiZiPingYi_Edit.this, AddapPraiseBean.getBody().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    //公共参数
    private RequestBody commParam(int TemplateId) {
        JSONObject obj = new JSONObject();
        CommParam commParam = new CommParam(this);
        try {
            // 用户id
            SharedPreferences sp = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            int userId = sp.getInt("S_ID", 0);
            obj.put("userId", userId);
            obj.put("appName", commParam.getAppname());
            obj.put("client", commParam.getClient());
            obj.put("timeStamp", commParam.getTimeStamp());
            obj.put("oauth", ToolUtils.getMD5Str(commParam.getUserId() + commParam.getTimeStamp() + "android!%@%$@#$"));

            obj.put("ClassSchedule_ID", ClassScheduleID);
            obj.put("TemplateId", TemplateId);
            JSONArray arr = new JSONArray();
            for (int i = 0; i < Appraise_Content.size(); i++) {
                arr.put(Appraise_Content.get(i));
            }
            Log.i("arr - ", "" + arr.toString());
            Log.i("Appraise_Content - ", "" + Appraise_Content);

            obj.put("Appraise_Content", Appraise_Content);
            obj.put("Appraise_Opinion", Appraise_Opinion);
        } catch (Exception e) {
            e.printStackTrace();
        }
        RequestBody body = RequestBody.create(MediaType.parse("application/json"), obj.toString());
        return body;
    }
}
