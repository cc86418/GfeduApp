package jc.cici.android.atom.bean;

/**
 * 预约实体
 * Created by atom on 2017/11/16.
 */

public class BookBean {

    // 是否开始直播(1:开始，0：未开始)
    private int IsLiveBegin;

    public int getIsLiveBegin() {
        return IsLiveBegin;
    }

    public void setIsLiveBegin(int isLiveBegin) {
        IsLiveBegin = isLiveBegin;
    }
}
