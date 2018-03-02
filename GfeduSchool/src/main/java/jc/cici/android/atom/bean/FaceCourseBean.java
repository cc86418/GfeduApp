package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 面授列表实体
 * Created by atom on 2017/10/26.
 */

public class FaceCourseBean {

    // 列表总数量
    private int ListCount;
    // 面授列表
    private ArrayList<OnLineBean> List;

    public int getListCount() {
        return ListCount;
    }

    public void setListCount(int listCount) {
        ListCount = listCount;
    }

    public ArrayList<OnLineBean> getList() {
        return List;
    }

    public void setList(ArrayList<OnLineBean> list) {
        List = list;
    }
}
