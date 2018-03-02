package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 广告实体
 * Created by atom on 2017/8/2.
 */

public class AdsInfo {
    // 广告数量
    private int AdCount;
    private ArrayList<Ads> List;

    public int getAdCount() {
        return AdCount;
    }

    public void setAdCount(int adCount) {
        AdCount = adCount;
    }

    public ArrayList<Ads> getList() {
        return List;
    }

    public void setList(ArrayList<Ads> list) {
        List = list;
    }
}
