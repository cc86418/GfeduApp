package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 产品信息实体
 * Created by atom on 2017/8/2.
 */

public class ProductInfo {
    // 产品数量
    private int ProductCount;
    private ArrayList<Product> List;

    public int getProductCount() {
        return ProductCount;
    }

    public void setProductCount(int productCount) {
        ProductCount = productCount;
    }

    public ArrayList<Product> getList() {
        return List;
    }

    public void setList(ArrayList<Product> list) {
        List = list;
    }
}
