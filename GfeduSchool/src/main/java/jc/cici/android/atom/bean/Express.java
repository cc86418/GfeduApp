package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 快递物流实体
 * Created by atom on 2017/9/20.
 */

public class Express {

    private int ExpressCount;
    private ArrayList<ExpressBean> List;
    private int NotShippedCount;
    private ArrayList<String> NotShipped;

    public int getNotShippedCount() {
        return NotShippedCount;
    }

    public void setNotShippedCount(int notShippedCount) {
        NotShippedCount = notShippedCount;
    }

    public ArrayList<String> getNotShipped() {
        return NotShipped;
    }

    public void setNotShipped(ArrayList<String> notShipped) {
        NotShipped = notShipped;
    }

    public int getExpressCount() {
        return ExpressCount;
    }

    public void setExpressCount(int expressCount) {
        ExpressCount = expressCount;
    }

    public ArrayList<ExpressBean> getList() {
        return List;
    }

    public void setList(ArrayList<ExpressBean> list) {
        List = list;
    }
}
