package cn.jun.VerticalScreenPolyv;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import com.easefun.polyvsdk.ijk.IjkVideoView;

/**
 * 用于处理IjkVideoView的视屏外的触摸事件
 *
 */
public class VerticalVideoViewContainer extends RelativeLayout {
	private IjkVideoView videoView;
	private GestureDetector gestureDetector;

	public VerticalVideoViewContainer(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public void setVideoView(IjkVideoView videoView) {
		this.videoView = videoView;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		if (videoView != null) {
			if (gestureDetector == null)
				gestureDetector = videoView.getGestureDetector();
			if (gestureDetector != null)
				gestureDetector.onTouchEvent(event);
		}
		return true;
	}
}
