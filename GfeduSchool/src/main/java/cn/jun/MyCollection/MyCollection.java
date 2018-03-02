package cn.jun.MyCollection;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.ArrayList;

import cn.jun.bean.Const;
import cn.jun.bean.MyCollectionBean;
import cn.jun.bean.ProductCollection;
import cn.jun.courseinfo.activity.OnlineCourseDetailsActivity;
import cn.jun.courseinfo.activity.OnlineCourseDetailsAloneActivityTwo;
import cn.jun.live.LiveClassActivity;
import cn.jun.live.LiveClassXiLieActivity;
import cn.jun.utils.HttpUtils;
import edu.swu.pulltorefreshswipemenulistview.library.PullToRefreshSwipeMenuListView;
import edu.swu.pulltorefreshswipemenulistview.library.pulltorefresh.interfaces.IXListViewListener;
import edu.swu.pulltorefreshswipemenulistview.library.swipemenu.bean.SwipeMenu;
import edu.swu.pulltorefreshswipemenulistview.library.swipemenu.bean.SwipeMenuItem;
import edu.swu.pulltorefreshswipemenulistview.library.swipemenu.interfaces.OnMenuItemClickListener;
import edu.swu.pulltorefreshswipemenulistview.library.swipemenu.interfaces.OnSwipeListener;
import edu.swu.pulltorefreshswipemenulistview.library.swipemenu.interfaces.SwipeMenuCreator;
import jc.cici.android.R;
import jc.cici.android.atom.common.Global;


public class MyCollection extends Activity implements View.OnClickListener, IXListViewListener {
    //用户ID
    private int UserID;
    //工具类
    private HttpUtils httpUtils = new HttpUtils();
    //返回
    private ImageView iv_back;
    //侧滑ListView
    private PullToRefreshSwipeMenuListView mListView;
    //适配器
    private SwipeAdapter mAdapter;
    //数据源
    private MyCollectionBean CollectionBean = new MyCollectionBean();
    private ArrayList<MyCollectionBean> CollectionList;
    //删除收藏
    private ProductCollection productCollection = new ProductCollection();
    //判断套餐还是班级类型2：班级 5：跳槽
    private int ProductModule;
    private int Class_PKID;
    private int remove_pos;
//    private CustomBean customBean;
//    private List<CustomBean> mlist;
//    private Handler mHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_collection);

        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        if (httpUtils.isNetworkConnected(this)) {
            initData();

        }
    }

    private void initData() {
        MyCollectionTask CollectionTask = new MyCollectionTask();
        CollectionTask.execute();
    }

    class MyCollectionTask extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... arg0) {
            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                UserID = LoginPre.getInt("S_ID", 0);
            }
//            UserID = 26146;
            CollectionBean = httpUtils.getMyCollectionBeanList(Const.URL + Const.GetCollectionListAPI, UserID, 1, 0);


            return null;
        }

        @TargetApi(Build.VERSION_CODES.ICE_CREAM_SANDWICH)
        @SuppressLint("NewApi")
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == CollectionBean.getCode()) {
                if (CollectionBean.getBody().getCollectionCount() > 0) {
                    CollectionList = new ArrayList<>();
                    CollectionList.add(CollectionBean);
                    initView();
                } else {//接口无数据

                }
            } else {//接口请求失败
                Toast.makeText(MyCollection.this, CollectionBean.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void initView() {
        mListView = (PullToRefreshSwipeMenuListView) findViewById(R.id.listView);
        mAdapter = new SwipeAdapter(getApplicationContext(), CollectionList);
        mListView.setAdapter(mAdapter);
        mListView.setPullRefreshEnable(true);
        mListView.setPullLoadEnable(true);
        mListView.setXListViewListener(this);

        // step 1. create a MenuCreator
        SwipeMenuCreator creator = new SwipeMenuCreator() {
            @Override
            public void create(SwipeMenu menu) {
//                // create "open" item
//                SwipeMenuItem openItem = new SwipeMenuItem(getApplicationContext());
//                // set item background
//                openItem.setBackground(new ColorDrawable(Color.rgb(0xC9, 0xC9, 0xCE)));
//                // set item width
//                openItem.setWidth(dp2px(90));
//                // set item title
//                openItem.setTitle("Open");
//                // set item title fontsize
//                openItem.setTitleSize(18);
//                // set item title font color
//                openItem.setTitleColor(Color.WHITE);
//                // add to menu
//                menu.addMenuItem(openItem);

                // create "delete" item
                SwipeMenuItem deleteItem = new SwipeMenuItem(getApplicationContext());
                // set item background
                deleteItem.setBackground(new ColorDrawable(Color.rgb(221, 85, 85)));
//                deleteItem.setBackground(Color.parseColor("#dd5555"));
                // set item width
                deleteItem.setWidth(dp2px(90));
//                // set item title fontsize
                deleteItem.setTitle("删除");
                deleteItem.setTitleSize(18);
//                // set item title font color
                deleteItem.setTitleColor(Color.WHITE);
                // set a icon
//                deleteItem.setIcon(R.drawable.ic_delete_red);
//                deleteItem.setTitleColor(R.color.white);
                // add to menu
                menu.addMenuItem(deleteItem);
            }
        };
        // set creator
        mListView.setMenuCreator(creator);
        // step 2. listener item click event
        mListView.setOnMenuItemClickListener(new OnMenuItemClickListener() {
            @Override
            public void onMenuItemClick(int position, SwipeMenu menu, int index) {
                MyCollectionBean.Body.CollectionList item
                        = CollectionList.get(0).getBody().getCollectionList().get(position);
                Log.i("index == ", "" + index);
                switch (index) {
//                    case 0:
//                        // open
////                    open(item);
//                        break;
                    case 0:
                        Class_PKID = CollectionList.get(0).getBody().getCollectionList().get(position).getClass_PKID();
                        ProductModule = CollectionList.get(0).getBody().getCollectionList().get(position).getProductModule();
                        if (httpUtils.isNetworkConnected(MyCollection.this)) {
                            RemoveCollectionTask RemoveTask = new RemoveCollectionTask();
                            RemoveTask.execute(0);
                        }
                        remove_pos = position;

                        break;
                }
            }
        });
        // set SwipeListener
        mListView.setOnSwipeListener(new OnSwipeListener() {

            @Override
            public void onSwipeStart(int position) {
                // swipe start
            }

            @Override
            public void onSwipeEnd(int position) {
                // swipe end
            }
        });

        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Class_PKID = CollectionList.get(0).getBody().getCollectionList().get(position - 1).getClass_PKID();
                ProductModule = CollectionList.get(0).getBody().getCollectionList().get(position - 1).getProductModule();
                int Class_Form = CollectionList.get(0).getBody().getCollectionList().get(position - 1).getClass_Form();
                if (0 == Class_Form) {
                    //2：班级 5：套餐
                    if (2 == ProductModule) {
                        Intent i_class = new Intent(MyCollection.this, OnlineCourseDetailsAloneActivityTwo.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("Product_PKID", Class_PKID);
                        i_class.putExtras(bundle);
                        startActivity(i_class);
                    } else if (5 == ProductModule) {
                        Intent i_meal = new Intent(MyCollection.this, OnlineCourseDetailsActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putInt("Product_PKID", Class_PKID);
                        i_meal.putExtras(bundle);
                        startActivity(i_meal);
                    }
                } else if (1 == Class_Form) {//系列直播
                    Intent i_meal = new Intent(MyCollection.this, LiveClassXiLieActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("Class_PKID", Class_PKID);
                    i_meal.putExtras(bundle);
                    startActivity(i_meal);
                } else if (2 == Class_Form) {//单直播
                    Intent i_meal = new Intent(MyCollection.this, LiveClassActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("Class_PKID", Class_PKID);
                    i_meal.putExtras(bundle);
                    startActivity(i_meal);
                }

//                Toast.makeText(MyCollection.this, " position >>> " + position, Toast.LENGTH_SHORT).show();
            }
        });

    }


    class RemoveCollectionTask extends AsyncTask<Integer, Void, Void> {
        @Override
        protected Void doInBackground(Integer... params) {
            int type = params[0];
            Log.i("type == ", "" + type);
            SharedPreferences LoginPre = getSharedPreferences(Global.LOGIN_FLAG, Activity.MODE_PRIVATE);
            if (null != LoginPre && !"".equals(LoginPre)) {
                UserID = LoginPre.getInt("S_ID", 0);
            }
//            UserID = 26146;
            //判断是套餐还是班级
//            2：班级 5：套餐
            if (2 == ProductModule) {
                productCollection = httpUtils.RemoveProductCollection(Const.URL + Const.GetProductCollectionAPI, UserID, Class_PKID, 1);
            } else if (5 == ProductModule) {
                productCollection = httpUtils.RemoveProductCollection(Const.URL + Const.GetProductCollectionAPI, UserID, Class_PKID, 0);
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            if (100 == productCollection.getCode()) {
                Toast.makeText(MyCollection.this, "删除成功", Toast.LENGTH_SHORT).show();
                CollectionList.get(0).getBody().getCollectionList().remove(remove_pos);
                mAdapter.notifyDataSetChanged();
            } else {
                Toast.makeText(MyCollection.this, "删除失败,稍后再试", Toast.LENGTH_SHORT).show();
            }

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void onLoad() {
        mListView.setRefreshTime("刚刚");
//        mListView.setRefreshTime(RefreshTime.getRefreshTime(getApplicationContext()));
        mListView.stopRefresh();
        mListView.stopLoadMore();

    }

    @Override
    public void onRefresh() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                SimpleDateFormat df = new SimpleDateFormat("MM-dd HH:mm", Locale.getDefault());
//                RefreshTime.setRefreshTime(getApplicationContext(), df.format(new Date()));
        onLoad();
//            }
//        }, 2000);
    }

    @Override
    public void onLoadMore() {
//        mHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
        onLoad();
//            }
//        }, 2000);
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }
}
