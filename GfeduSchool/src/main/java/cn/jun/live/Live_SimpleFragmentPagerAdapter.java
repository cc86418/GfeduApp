//package cn.jun.live;
//
//import android.content.Context;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentPagerAdapter;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import java.util.ArrayList;
//
//import jc.cici.android.R;
//
//
//
//public class Live_SimpleFragmentPagerAdapter extends FragmentPagerAdapter {
//
//    final int PAGE_COUNT = 3;
//    //    private String tabTitles[] = new String[]{"tab1", "tab2", "tab3"};
//    private int[] imageResId = {R.color.white, R.drawable.ic_camera, R.color.white};
//    private ArrayList<Fragment> mList;
//    private ArrayList<String> mListString;
//    private Context context;
//    //是否有免费试听
////    private int Class_OutlineFreeState;
//
//    public Live_SimpleFragmentPagerAdapter(FragmentManager fm, Context context, ArrayList<Fragment> mList, ArrayList<String> mListString) {
//        super(fm);
//        this.context = context;
//        this.mList = mList;
//        this.mListString = mListString;
////        this.Class_OutlineFreeState = Class_OutlineFreeState;
//    }
//
//    @Override
//    public Fragment getItem(int position) {
//        return mList.get(position);
//    }
//
//    @Override
//    public int getCount() {
//        return mList.size();
//    }
//
//    @Override
//    public CharSequence getPageTitle(int position) {
////        return mListString.get(position);
////        Drawable image = context.getResources().getDrawable(imageResId[position]);
////        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
////        SpannableString sb = new SpannableString(" ");
////        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
////        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
////        return sb;
//        return null;
//    }
//
//    public View getTabView(int position) {
//        View view = LayoutInflater.from(context).inflate(R.layout.tablayout_item, null);
//        TextView tv = (TextView) view.findViewById(R.id.textView);
//        tv.setText(LiveDetailsActivity.list_title.get(position));
//        ImageView img = (ImageView) view.findViewById(R.id.imageView);
////        if (1 == position) {
////            img.setVisibility(View.VISIBLE);
////            img.setImageResource(imageResId[position]);
//////            if (1 == Class_OutlineFreeState) {
//////                img.setVisibility(View.VISIBLE);
//////                img.setImageResource(imageResId[position]);
//////            } else if (0 == Class_OutlineFreeState) {
//////                img.setVisibility(View.GONE);
//////            }
////        } else {
////            img.setVisibility(View.GONE);
////        }
//
//
//        return view;
//    }
//}