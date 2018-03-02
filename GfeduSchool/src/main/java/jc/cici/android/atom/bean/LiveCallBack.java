package jc.cici.android.atom.bean;

/**
 * 直播筛选列表回放实体
 * Created by atom on 2017/11/17.
 */

public class LiveCallBack {

    // 是否有回放，60:有回放，50:无回放
    private int isCallBack;
    // 回放名称
    private String callName;
    // 是否选中
    private boolean isSelect;

    public LiveCallBack(int isCallBack, String callName, boolean isSelect) {
        this.isCallBack = isCallBack;
        this.callName = callName;
        this.isSelect = isSelect;
    }

    public int getIsCallBack() {
        return isCallBack;
    }

    public void setIsCallBack(int isCallBack) {
        this.isCallBack = isCallBack;
    }

    public String getCallName() {
        return callName;
    }

    public void setCallName(String callName) {
        this.callName = callName;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }
}
