package cn.jun.courseinfo.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.ArrayList;

import cn.jun.bean.PackageDetailBean;
import cn.jun.courseinfo.activity.OnlineCourseDetailsAloneActivityTwo;
import cn.jun.courseinfo.ui.MyScrollView;
import cn.jun.courseinfo.ui.PublicStaticClass;
import cn.jun.view.ListViewForScrollView;
import jc.cici.android.R;

import static cn.jun.courseinfo.activity.OnlineCourseDetailsActivity.SyllabusDetailsFragmentContent;


public class SyllabusFragment extends Fragment {
    private View mView;
    private ListViewForScrollView listview;
    private ScrollViewAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_two, container, false);
        Log.i("", "");
        initView();
        initDate();
        return mView;
    }

    private void initView() {
        MyScrollView twoScrollView = (MyScrollView) mView.findViewById(R.id.twoScrollview);
        twoScrollView.setScrollListener(new MyScrollView.ScrollListener() {
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
        Gson s = new Gson();
        Log.i("传递的数据 === ", "" + s.toJson(SyllabusDetailsFragmentContent).toString());
        if (!"".equals(SyllabusDetailsFragmentContent) && null != SyllabusDetailsFragmentContent) {
            adapter = new ScrollViewAdapter(getActivity(), SyllabusDetailsFragmentContent);
            listview.setAdapter(adapter);
            listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    int ProductId = SyllabusDetailsFragmentContent.get(position).getClass_PKID();
                    Intent intent = new Intent(getActivity(), OnlineCourseDetailsAloneActivityTwo.class);
                    Bundle bundle = new Bundle();
                    bundle.putInt("Product_PKID", ProductId);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
        }

    }

    private class ScrollViewAdapter extends BaseAdapter {
        private Activity activity;
        private ArrayList<PackageDetailBean.Body.ClassList> mList;

        private ScrollViewAdapter(Activity activity, ArrayList<PackageDetailBean.Body.ClassList> SyllabusDetailsFragmentContent) {
            this.activity = activity;
            this.mList = SyllabusDetailsFragmentContent;
        }

        @Override
        public int getCount() {
//            return mList.size() > 0 ? mList.size() : 0;
            return mList == null ? 0 : mList.size();
//            return mList.size();
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
                        R.layout.syllabus_recycleview_item, parent, false);
                viewHolder.title = (TextView) convertView
                        .findViewById(R.id.title);
                viewHolder.holder_image = (ImageView) convertView
                        .findViewById(R.id.holder_image);
                viewHolder.price_saleregion = (TextView) convertView
                        .findViewById(R.id.price_saleregion);
                viewHolder.mfst_tv = (Button) convertView.findViewById(R.id.mfst_tv);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }
            //免费试听
            int Free = mList.get(position).getClass_OutlineFreeState();
            if (1 == Free) {
                viewHolder.mfst_tv.setVisibility(View.VISIBLE);
                viewHolder.mfst_tv.setText("免费试听");
                viewHolder.mfst_tv.setTextColor(Color.parseColor("#ffffff"));
                viewHolder.mfst_tv.setBackgroundColor(Color.parseColor("#6cb8a7"));
            } else if (0 == Free) {
                String StudyNum = mList.get(position).getClass_StudyNum();
                viewHolder.mfst_tv.setText("学习人数 : " + StudyNum);
                viewHolder.mfst_tv.setTextColor(Color.parseColor("#666666"));
                viewHolder.mfst_tv.setBackgroundResource(0);
            }
            String Title = mList.get(position).getClass_Name();
            String Class_MinSalePrice = mList.get(position).getClass_MinSalePrice();
            String Class_MaxSalePrice = mList.get(position).getClass_MaxSalePrice();
            String Image = mList.get(position).getClass_MobileImage();
            viewHolder.title.setText(Title);
            if (Class_MinSalePrice.equals(Class_MaxSalePrice)) {
                viewHolder.price_saleregion.setText("¥" + Class_MinSalePrice);
            } else {
                viewHolder.price_saleregion.setText("¥" + Class_MinSalePrice + " - " + Class_MaxSalePrice);
            }

            Glide.with(activity)
                    .load(Image)
                    .placeholder(R.drawable.pic_kong)
                    .into(viewHolder.holder_image);

            return convertView;
        }
    }

    class ViewHolder {
        TextView title;
        ImageView holder_image;
        TextView price_saleregion;

        Button mfst_tv;//免费试听
    }

}
