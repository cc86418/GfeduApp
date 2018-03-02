package jc.cici.android.atom.view;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import java.util.ArrayList;

import jc.cici.android.R;
import jc.cici.android.atom.adapter.PopupAdapter;
import jc.cici.android.atom.adapter.base.BaseRecycleerAdapter;
import jc.cici.android.atom.bean.TikuHomeBean;
import jc.cici.android.atom.utils.ToolUtils;

/**
 * 顶部弹出框
 * Created by atom on 2017/12/22.
 */

public class TopMiddlePopup extends PopupWindow {

    private Context myContext;
    private RecyclerView myLv;
    private BaseRecycleerAdapter.OnItemClickListener myOnItemClickListener;
    private ArrayList<TikuHomeBean.TikuProject.Node> myItems;
    private int myWidth;
    private int myHeight;

    // 判断是否需要添加或更新列表子类项
    private boolean myIsDirty = true;

    private LayoutInflater inflater = null;
    private View myMenuView;

    private LinearLayout popupLL;

    private LinearLayoutManager linearLayoutManager;
    private PopupAdapter adapter;

    public TopMiddlePopup(Context context) {
        // TODO Auto-generated constructor stub
    }

    public TopMiddlePopup(Context context, int width, int height,
                          BaseRecycleerAdapter.OnItemClickListener onItemClickListener, ArrayList<TikuHomeBean.TikuProject.Node> items) {

        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        myMenuView = inflater.inflate(R.layout.top_popup, null);

        this.myContext = context;
        this.myItems = items;
        this.myOnItemClickListener = onItemClickListener;

        this.myWidth = width;
        this.myHeight = height;
        initWidget();
        setPopup();
    }

    /**
     * 初始化控件
     */
    private void initWidget() {
        myLv = (RecyclerView) myMenuView.findViewById(R.id.popup_lv);
        popupLL = (LinearLayout) myMenuView.findViewById(R.id.popup_layout);
        linearLayoutManager = new LinearLayoutManager(myContext);
        myLv.setLayoutManager(linearLayoutManager);

    }

    /**
     * 设置popup的样式
     */
    private void setPopup() {
        // 设置AccessoryPopup的view
        this.setContentView(myMenuView);
        // 设置AccessoryPopup弹出窗体的宽度
        this.setWidth(LinearLayout.LayoutParams.MATCH_PARENT);
        // 设置AccessoryPopup弹出窗体的高度
        this.setHeight(LinearLayout.LayoutParams.MATCH_PARENT - 40);
        // 设置AccessoryPopup弹出窗体可点击
        this.setFocusable(true);
        // 设置AccessoryPopup弹出窗体的动画效果
        this.setAnimationStyle(R.style.AnimTopMiddle);
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0x33000000);
        // 设置SelectPicPopupWindow弹出窗体的背景
        this.setBackgroundDrawable(dw);

        myMenuView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                int height = popupLL.getBottom();
                int left = popupLL.getLeft();
                int right = popupLL.getRight();
                int y = (int) event.getY();
                int x = (int) event.getX();
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    if (y > height || x < left || x > right) {
                        dismiss();
                    }
                }
                return true;
            }
        });
    }

    /**
     * 显示弹窗界面
     *
     * @param view
     */
    public void show(View view) {
        if (myIsDirty) {
            myIsDirty = false;
            adapter = new PopupAdapter(myContext, myItems);
            myLv.setAdapter(adapter);
            adapter.setOnItemClickListener((BaseRecycleerAdapter.OnItemClickListener) myOnItemClickListener);
        }

        int xoffInPixels = myWidth / 2 - view.getWidth() / 2;
        // 将pixels转为dip
        int xoffInDip = ToolUtils.px2dip(myContext,xoffInPixels);

        showAsDropDown(view, xoffInDip, 0);
        update();
    }
}
