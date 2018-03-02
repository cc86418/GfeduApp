package cn.jun.courseinfo.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.RelativeLayout;

import cn.jun.courseinfo.ui.MyScrollView;
import cn.jun.courseinfo.ui.PublicStaticClass;
import jc.cici.android.R;

import static cn.jun.courseinfo.activity.OnlineCourseDetailsActivity.CourseDetailsFragmentContent;


public class CourseDetailsFragment extends Fragment {
    private View mView;
    private WebView wb, wb2, wb3, wb4;
    private RelativeLayout no_date_realtive;
    private String data;
    private String mDate;

    public static CourseDetailsFragment newInstance() {
        CourseDetailsFragment newFragment = new CourseDetailsFragment();

        return newFragment;

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (mView == null) {
            mView = inflater.inflate(R.layout.fragment_one, container, false);
            initView();
            initData();
        } else {
            ViewGroup parent = (ViewGroup) mView.getParent();
            if (parent != null) {
                parent.removeView(mView);
            }
        }
        return mView;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    private void initView() {
        MyScrollView oneScrollView = (MyScrollView) mView.findViewById(R.id.oneScrollview);
        oneScrollView.setScrollListener(new MyScrollView.ScrollListener() {
            @Override
            public void onScrollToBottom() {

            }

            @Override
            public void onScrollToTop() {

            }

            @Override
            public void onScroll(int scrollY) {
                if (scrollY == 0) {
                    PublicStaticClass.IsTop = true;
                } else {
                    PublicStaticClass.IsTop = false;
                }
            }

            @Override
            public void notBottom() {

            }

        });
    }

    private void initData() {
        no_date_realtive = (RelativeLayout) mView.findViewById(R.id.no_date_realtive);
        wb = (WebView) mView.findViewById(R.id.CourseDetailsWb);
        Log.i("tContent  ", "" + CourseDetailsFragmentContent);
        if ("".equals(CourseDetailsFragmentContent) && null == CourseDetailsFragmentContent) {
            wb.setVisibility(View.GONE);
            no_date_realtive.setVisibility(View.VISIBLE);
        } else {
            wb.setVisibility(View.VISIBLE);
            no_date_realtive.setVisibility(View.GONE);
            wb.loadUrl(CourseDetailsFragmentContent);
//            wb.loadDataWithBaseURL(null, CourseDetailsFragmentContent, "text/html", "utf-8", null);
            wb.getSettings().setJavaScriptEnabled(true);
        }
    }


}