package jc.cici.android.atom.bean;

/**
 * 套餐班父类班型
 * Created by atom on 2017/9/7.
 */

public class PackageBean {
    // 套餐ID
    private int Package_PKID;
    // 所属项目
    private String Package_Project;
    // 套餐编号
    private String Package_Code;
    // 套餐名称
    private String Package_Name;
    // 套餐类型 0：固定套餐 1：自由组合套餐
    private int Package_Type;
    // 套餐图片
    private String Package_MobileImage;
    // 套餐原价最小价
    private float Package_MinPrice;
    // 套餐原价最大价
    private float Package_MaxPrice;
    // 套餐售价最小价
    private float Package_MinSalePrice;
    // 套餐售价最大价
    private float Package_MaxSalePrice;
    // 原价范围
    private String Package_PriceRegion;
    // 售价范围
    private String Package_PriceSaleRegion;


    public int getPackage_PKID() {
        return Package_PKID;
    }

    public void setPackage_PKID(int package_PKID) {
        Package_PKID = package_PKID;
    }

    public String getPackage_Project() {
        return Package_Project;
    }

    public void setPackage_Project(String package_Project) {
        Package_Project = package_Project;
    }

    public String getPackage_Code() {
        return Package_Code;
    }

    public void setPackage_Code(String package_Code) {
        Package_Code = package_Code;
    }

    public String getPackage_Name() {
        return Package_Name;
    }

    public void setPackage_Name(String package_Name) {
        Package_Name = package_Name;
    }

    public int getPackage_Type() {
        return Package_Type;
    }

    public void setPackage_Type(int package_Type) {
        Package_Type = package_Type;
    }

    public String getPackage_MobileImage() {
        return Package_MobileImage;
    }

    public void setPackage_MobileImage(String package_MobileImage) {
        Package_MobileImage = package_MobileImage;
    }

    public float getPackage_MinPrice() {
        return Package_MinPrice;
    }

    public void setPackage_MinPrice(float package_MinPrice) {
        Package_MinPrice = package_MinPrice;
    }

    public float getPackage_MaxPrice() {
        return Package_MaxPrice;
    }

    public void setPackage_MaxPrice(float package_MaxPrice) {
        Package_MaxPrice = package_MaxPrice;
    }

    public float getPackage_MinSalePrice() {
        return Package_MinSalePrice;
    }

    public void setPackage_MinSalePrice(float package_MinSalePrice) {
        Package_MinSalePrice = package_MinSalePrice;
    }

    public float getPackage_MaxSalePrice() {
        return Package_MaxSalePrice;
    }

    public void setPackage_MaxSalePrice(float package_MaxSalePrice) {
        Package_MaxSalePrice = package_MaxSalePrice;
    }

    public String getPackage_PriceRegion() {
        return Package_PriceRegion;
    }

    public void setPackage_PriceRegion(String package_PriceRegion) {
        Package_PriceRegion = package_PriceRegion;
    }

    public String getPackage_PriceSaleRegion() {
        return Package_PriceSaleRegion;
    }

    public void setPackage_PriceSaleRegion(String package_PriceSaleRegion) {
        Package_PriceSaleRegion = package_PriceSaleRegion;
    }

}
