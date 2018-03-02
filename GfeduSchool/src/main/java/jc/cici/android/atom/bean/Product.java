package jc.cici.android.atom.bean;

/**
 * 产品表信息
 * Created by atom on 2017/8/2.
 */

public class Product {

    // 产品id
    private int Product_PKID;
    // 产品类型 2-班级 5-套餐
    private int Type;
    // 产品名称
    private String Product_Name;
    // 产品图片
    private String Product_MobileImage;
    // 最小原价
    private float Product_MinPrice;
    // 最大原价
    private float Product_MaxPrice;
    // 最小售价
    private float Product_MinSalePrice;
    // 最大售价
    private float Product_MaxSalePrice;
    // 原价范围
    private String Product_PriceRegion;
    // 售价范围
    private String Product_PriceSaleRegion;
    // 简介名称
    private String Product_IntroName;
    // 简介
    private String Product_Intro;
    // 是否热推 1：是 0：否
    private int Product_IsHot;
    // 是否置顶
    private int Product_IsTop;
    // 是否试听
    private int Product_OutlineFreeState;
    // 收藏数量
    private int Product_CollectNum;
    // 购买数量
    private int Product_BuyNum;
    // 学习人数
    private int Product_StudyNum;

    public int getProduct_PKID() {
        return Product_PKID;
    }

    public void setProduct_PKID(int product_PKID) {
        Product_PKID = product_PKID;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public String getProduct_MobileImage() {
        return Product_MobileImage;
    }

    public void setProduct_MobileImage(String product_MobileImage) {
        Product_MobileImage = product_MobileImage;
    }

    public float getProduct_MinPrice() {
        return Product_MinPrice;
    }

    public void setProduct_MinPrice(float product_MinPrice) {
        Product_MinPrice = product_MinPrice;
    }

    public float getProduct_MaxPrice() {
        return Product_MaxPrice;
    }

    public void setProduct_MaxPrice(float product_MaxPrice) {
        Product_MaxPrice = product_MaxPrice;
    }

    public float getProduct_MinSalePrice() {
        return Product_MinSalePrice;
    }

    public void setProduct_MinSalePrice(float product_MinSalePrice) {
        Product_MinSalePrice = product_MinSalePrice;
    }

    public float getProduct_MaxSalePrice() {
        return Product_MaxSalePrice;
    }

    public void setProduct_MaxSalePrice(float product_MaxSalePrice) {
        Product_MaxSalePrice = product_MaxSalePrice;
    }

    public String getProduct_PriceRegion() {
        return Product_PriceRegion;
    }

    public void setProduct_PriceRegion(String product_PriceRegion) {
        Product_PriceRegion = product_PriceRegion;
    }

    public String getProduct_PriceSaleRegion() {
        return Product_PriceSaleRegion;
    }

    public void setProduct_PriceSaleRegion(String product_PriceSaleRegion) {
        Product_PriceSaleRegion = product_PriceSaleRegion;
    }

    public String getProduct_IntroName() {
        return Product_IntroName;
    }

    public void setProduct_IntroName(String product_IntroName) {
        Product_IntroName = product_IntroName;
    }

    public String getProduct_Intro() {
        return Product_Intro;
    }

    public void setProduct_Intro(String product_Intro) {
        Product_Intro = product_Intro;
    }

    public int getProduct_IsHot() {
        return Product_IsHot;
    }

    public void setProduct_IsHot(int product_IsHot) {
        Product_IsHot = product_IsHot;
    }

    public int getProduct_IsTop() {
        return Product_IsTop;
    }

    public void setProduct_IsTop(int product_IsTop) {
        Product_IsTop = product_IsTop;
    }

    public int getProduct_OutlineFreeState() {
        return Product_OutlineFreeState;
    }

    public void setProduct_OutlineFreeState(int product_OutlineFreeState) {
        Product_OutlineFreeState = product_OutlineFreeState;
    }

    public int getProduct_CollectNum() {
        return Product_CollectNum;
    }

    public void setProduct_CollectNum(int product_CollectNum) {
        Product_CollectNum = product_CollectNum;
    }

    public int getProduct_BuyNum() {
        return Product_BuyNum;
    }

    public void setProduct_BuyNum(int product_BuyNum) {
        Product_BuyNum = product_BuyNum;
    }

    public int getProduct_StudyNum() {
        return Product_StudyNum;
    }

    public void setProduct_StudyNum(int product_StudyNum) {
        Product_StudyNum = product_StudyNum;
    }

}
