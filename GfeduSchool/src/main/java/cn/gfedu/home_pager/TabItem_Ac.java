package cn.gfedu.home_pager;


import android.app.Activity;

public class TabItem_Ac {

    /**
     * icon
     */
    public int imageResId;
    /**
     * 文本
     */
    public int lableResId;

    public Class<? extends Activity> tagFragmentClz;

    public TabItem_Ac(int imageResId, int lableResId) {
        this.imageResId = imageResId;
        this.lableResId = lableResId;
    }


    public TabItem_Ac(int imageResId, int lableResId, Class<? extends Activity> tagFragmentClz) {
        this.imageResId = imageResId;
        this.lableResId = lableResId;
        this.tagFragmentClz = tagFragmentClz;
    }
}
