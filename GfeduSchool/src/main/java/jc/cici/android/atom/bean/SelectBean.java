package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 筛选实体
 * Created by atom on 2017/8/8.
 */

public class SelectBean {
    // 一级内容
    private ArrayList<ParentEntity> List;

    public ArrayList<ParentEntity> getList() {
        return List;
    }

    public void setList(ArrayList<ParentEntity> list) {
        List = list;
    }
}
