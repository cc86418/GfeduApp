package jc.cici.android.atom.bean;

/**
 * 直播binner
 * Created by atom on 2017/11/10.
 */

public class LiveAds {

    // 产品类型： 2-班级 5-套餐
    private int ProductType;
    // 班级或套餐ID
    private int ProductId;
    // 2：单个直播 1：系列直播 其它：普通课程
    private int Class_Form;
    // 图片地址
    private String ImgUrl;
    // 日期
    private String Date;
    // 开始时间
    private String BeginTime;
    // 结束时间
    private String EndTime;
    // 标题
    private String Title;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

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

    public int getClass_Form() {
        return Class_Form;
    }

    public void setClass_Form(int class_Form) {
        Class_Form = class_Form;
    }

    public String getImgUrl() {
        return ImgUrl;
    }

    public void setImgUrl(String imgUrl) {
        ImgUrl = imgUrl;
    }
}
