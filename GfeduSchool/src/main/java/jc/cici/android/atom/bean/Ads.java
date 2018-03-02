package jc.cici.android.atom.bean;

/**
 * 广告位实体
 * Created by atom on 2017/8/2.
 */

public class Ads {

    // 产品类型
    private int  ProductType;
    // 班级或套餐id
    private int ProductId;
    // 图片地址
    private String ImgUrl;

    public int getProductType() {
        return ProductType;
    }

    public void setProductType(int productType) {
        ProductType = productType;
    }

    public int getProductId() {
        return ProductId;
    }

    public void setProductId(int productId) {
        ProductId = productId;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }
}
