package cn.jun.courseinfo.fragment;


import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;

import java.util.ArrayList;

import cn.jun.bean.EaluateBean;
import cn.jun.courseinfo.ui.MyScrollView;
import cn.jun.courseinfo.ui.PublicStaticClass;
import cn.jun.view.ListViewForScrollView;
import jc.cici.android.R;

import static cn.jun.courseinfo.activity.OnlineCourseDetailsActivity.ealuateList;

public class SyllabusConsultationFragment extends Fragment {
    private View mView;
    private ListViewForScrollView listview;
    private ScrollViewAdapter adapter;
    private RelativeLayout no_date_realtive;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_three, container, false);
        initView();
        initDate();
        return mView;
    }

    private void initView() {
        MyScrollView oneScrollView = (MyScrollView) mView.findViewById(R.id.threeScrollview);
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


    private void initDate() {
        listview = (ListViewForScrollView) mView.findViewById(R.id.myList);
        no_date_realtive = (RelativeLayout) mView.findViewById(R.id.no_date_realtive);
        Gson s =  new Gson();
        Log.i("ealuateList ==> ",""+ s.toJson(ealuateList).toString());
        if (!"".equals(ealuateList) && null != ealuateList) {
            listview.setVisibility(View.VISIBLE);
            no_date_realtive.setVisibility(View.GONE);
            adapter = new ScrollViewAdapter(getActivity(), ealuateList);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });
        }else{
            listview.setVisibility(View.GONE);
            no_date_realtive.setVisibility(View.VISIBLE);
        }

    }


    private class ScrollViewAdapter extends BaseAdapter {
        private Activity activity;
        private ArrayList<EaluateBean> mList = new ArrayList<>();

        private ScrollViewAdapter(Activity activity, ArrayList<EaluateBean> ealuateList) {
            this.activity = activity;
            this.mList = ealuateList;
        }

        @Override
        public int getCount() {
            return mList == null ? 0 : mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder viewHolder;
            if (convertView == null) {
                viewHolder = new ViewHolder();
                convertView = LayoutInflater.from(activity).inflate(
                        R.layout.changjianwenti_item, parent, false);
                viewHolder.wen_tv = (TextView) convertView
                        .findViewById(R.id.wen_tv);
                viewHolder.da_tv = (TextView) convertView
                        .findViewById(R.id.da_tv);
                viewHolder.date_tv = (TextView) convertView
                        .findViewById(R.id.date_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            int Kid = mList.get(0).getBody().getList().get(position).getKid();
            String wen = mList.get(0).getBody().getList().get(position).getContentDetail();
            wen = wen.replaceAll("&nbsp", " ");
            String da = mList.get(0).getBody().getList().get(position).getReplyContent();
            da = da.replaceAll("&nbsp", " ");
            String date = mList.get(0).getBody().getList().get(position).getAddTime();
            date = date.replaceAll("&nbsp", " ");
            viewHolder.wen_tv.setText(wen);
            viewHolder.da_tv.setText(da);
            viewHolder.date_tv.setText(date);


            return convertView;
        }
    }

    class ViewHolder {
        TextView wen_tv;
        TextView da_tv;
        TextView date_tv;

    }
}
