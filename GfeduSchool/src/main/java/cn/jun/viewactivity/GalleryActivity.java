package cn.jun.viewactivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PointF;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.Window;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import cn.jun.utils.Bimp;
import cn.jun.utils.PublicWay;
import jc.cici.android.R;


/**
 * 这个是用于进行图片浏览时的界面
 *
 */
public class GalleryActivity extends Activity {
	private Intent intent;

	// 共显示图片数
	private TextView send_Txt;
	// 顶部显示预览图片位置的textview
	private TextView positionTextView;
	// 获取前一个activity传过来的position
	private int position;
	// 当前的位置
	private int location = 0;

	private ArrayList<View> listViews = null;
	private ViewPagerFixed pager;
	private MyPageAdapter adapter;

	public List<Bitmap> bmp = new ArrayList<Bitmap>();
	public List<String> drr = new ArrayList<String>();
	public List<String> del = new ArrayList<String>();

	private Context mContext;

	RelativeLayout photo_relativeLayout;
	// 触摸图片关闭标示
	private boolean isClose;
	// 触屏的第一点坐标
	private PointF startPoint = new PointF();

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// 去标题
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.plugin_camera_gallery);// 切屏到主界面
		PublicWay.activityList.add(this);
		mContext = this;

		send_Txt = (TextView) findViewById(R.id.TextView);
		intent = getIntent();
		Bundle bundle = intent.getExtras();
		position = Integer.parseInt(bundle.getString("position"));
		isShowOkBt();
		// 为发送按钮设置文字
		pager = (ViewPagerFixed) findViewById(R.id.gallery01);
		pager.setOnPageChangeListener(pageChangeListener);
		for (int i = 0; i < Bimp.tempSelectBitmap.size(); i++) {
//			initListViews(Bimp.tempSelectBitmap.get(i).getBitmap());
		}

		adapter = new MyPageAdapter(listViews);
		pager.setAdapter(adapter);
		pager.setPageMargin((int) getResources().getDimensionPixelOffset(
				R.dimen.ui_10_dip));
		int id = intent.getIntExtra("ID", 0);
		pager.setCurrentItem(id);
	}

	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			location = arg0;
			send_Txt.setText((location + 1) + "/"
					+ Bimp.tempSelectBitmap.size());
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};

//	private void initListViews(Bitmap bm) {
//		if (listViews == null)
//			listViews = new ArrayList<View>();
//		PhotoView img = new PhotoView(this);
//		img.setBackgroundColor(0xff000000);
//		img.setImageBitmap(bm);
//		img.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
//				LayoutParams.MATCH_PARENT));
//		listViews.add(img);
//		img.setOnTouchListener(new OnTouchListener() {
//
//			@Override
//			public boolean onTouch(View v, MotionEvent event) {
//
//				switch (event.getAction()) {
//				case MotionEvent.ACTION_DOWN:
//
//					isClose = true;
//					startPoint.set(event.getX(),event.getY());
//					break;
//				case MotionEvent.ACTION_MOVE:
//
//					if(Math.abs(event.getX() - startPoint.x) > DensityUtil.dip2px(mContext, 5) ||
//							Math.abs(event.getY() -startPoint.y) > DensityUtil.dip2px(mContext, 5)){
//						isClose = false;
//					}
//					break;
//				case MotionEvent.ACTION_UP:
//
//					if(isClose){
//						finish();
//
//					}
//					break;
//				default:
//					break;
//				}
//				return true;
//			}
//		});
//	}

	public void isShowOkBt() {
		if (Bimp.tempSelectBitmap.size() > 0) {
			send_Txt.setText(position + "/"
					+ Bimp.tempSelectBitmap.size());
		} else {
			send_Txt.setTextColor(Color.parseColor("#E1E0DE"));
		}
	}

	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;

		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}

}
