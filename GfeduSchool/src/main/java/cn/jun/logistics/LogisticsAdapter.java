package cn.jun.logistics;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;

import jc.cici.android.R;


public class LogisticsAdapter extends FragmentPagerAdapter {

    private int[] imageResId = {R.color.white, R.drawable.ic_camera, R.color.white};
    private ArrayList<Fragment> mList;
    private ArrayList<String> mListString;
    private Context context;

    public LogisticsAdapter(FragmentManager fm, Context context, ArrayList<Fragment> mList, ArrayList<String> mListString) {
        super(fm);
        this.context = context;
        this.mList = mList;
        this.mListString = mListString;

    }

    @Override
    public Fragment getItem(int position) {
        return mList.get(position);
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
//        return mListString.get(position);
//        Drawable image = context.getResources().getDrawable(imageResId[position]);
//        image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
//        SpannableString sb = new SpannableString(" ");
//        ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
//        sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//        return sb;
        return null;
    }

    public View getTabView(int position) {
        View view = LayoutInflater.from(context).inflate(R.layout.logistics_tablayout_item, null);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(mListString.get(position));
        return view;
    }
}