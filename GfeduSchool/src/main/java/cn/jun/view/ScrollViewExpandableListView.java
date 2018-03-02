package cn.jun.view;


import android.content.Context;
import android.util.AttributeSet;
import android.widget.ExpandableListView;

/**
 * ScrollView和ExpandableList滑动冲突解决
 **/
public class ScrollViewExpandableListView extends ExpandableListView {
    public ScrollViewExpandableListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,

                MeasureSpec.AT_MOST);

        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
