package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 订单实体
 * Created by atom on 2017/8/30.
 */

public class OrderBean {

    // 订单数量
    private int OrderCount;
    // 订单列表
    private ArrayList<OrderList> List;

    public int getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(int orderCount) {
        OrderCount = orderCount;
    }

    public ArrayList<OrderList> getList() {
        return List;
    }

    public void setList(ArrayList<OrderList> list) {
        List = list;
    }
}
