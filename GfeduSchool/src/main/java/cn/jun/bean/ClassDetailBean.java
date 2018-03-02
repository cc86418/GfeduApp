package cn.jun.bean;


import java.util.ArrayList;

public class ClassDetailBean {
    private int Code;
    private String Message;
    private Body Body;

    public class Body {
        private int Class_PKID;//班级ID
        private int Class_Project;//班级所属项目
        private String Class_Name;//班级名称
        private String Class_Code;//班级编号
        private String Class_IntroName;//班级简介名称
        private String Class_Intro;//班级简介
        private int Class_EvaluateStar;//星级
        private int Class_CollectNum;//收藏数量
        private int Class_BuyNum;//购买数量
        private int Class_StudyNum;//学习人数
        private int Class_IsHot;//是否热门1：是 0：否
        private String Class_MobileImage;//图片地址
        private String Class_MobileIsVideo;//是否为视频 1：是 0：否
        private String Class_MobileValue;//视频或图片值
        private String Class_MobileContent;//详情页内容
        private String H5DetailLink;
        private int FeatureCount;//特色数量
        private ArrayList<String> Class_Feature;//特色值
        private int Class_OutlineID;//免费班型ID
        private int Class_OutlineFreeState;//是否有免费视频 1：是 0：否
        private String Class_MinPrice;//班级原价最小价格
        private String Class_MaxPrice;//班级原价最大价格
        private String Class_MinSalePrice;//班级售价最小价格
        private String Class_MaxSalePrice;//班级售价最大价格
        private String Class_PriceRegion;//班级原价区间
        private String Class_PriceSaleRegion;//班级售价区间
        private int HasCollection;//是否收藏过 1：是  0：否
        private String H5VideoLink;//H5视频地址
        private int ClassTypeCount;//所含班级数量

        public int getClassTypeCount() {
            return ClassTypeCount;
        }

        public void setClassTypeCount(int classTypeCount) {
            ClassTypeCount = classTypeCount;
        }

        public String getH5DetailLink() {
            return H5DetailLink;
        }

        public void setH5DetailLink(String h5DetailLink) {
            H5DetailLink = h5DetailLink;
        }

        public String getH5VideoLink() {
            return H5VideoLink;
        }

        public void setH5VideoLink(String h5VideoLink) {
            H5VideoLink = h5VideoLink;
        }

        public int getClass_PKID() {
            return Class_PKID;
        }

        public void setClass_PKID(int class_PKID) {
            Class_PKID = class_PKID;
        }

        public int getClass_Project() {
            return Class_Project;
        }

        public void setClass_Project(int class_Project) {
            Class_Project = class_Project;
        }

        public String getClass_Name() {
            return Class_Name;
        }

        public void setClass_Name(String class_Name) {
            Class_Name = class_Name;
        }

        public String getClass_Code() {
            return Class_Code;
        }

        public void setClass_Code(String class_Code) {
            Class_Code = class_Code;
        }

        public String getClass_IntroName() {
            return Class_IntroName;
        }

        public void setClass_IntroName(String class_IntroName) {
            Class_IntroName = class_IntroName;
        }

        public String getClass_Intro() {
            return Class_Intro;
        }

        public void setClass_Intro(String class_Intro) {
            Class_Intro = class_Intro;
        }

        public int getClass_EvaluateStar() {
            return Class_EvaluateStar;
        }

        public void setClass_EvaluateStar(int class_EvaluateStar) {
            Class_EvaluateStar = class_EvaluateStar;
        }

        public int getClass_CollectNum() {
            return Class_CollectNum;
        }

        public void setClass_CollectNum(int class_CollectNum) {
            Class_CollectNum = class_CollectNum;
        }

        public int getClass_BuyNum() {
            return Class_BuyNum;
        }

        public void setClass_BuyNum(int class_BuyNum) {
            Class_BuyNum = class_BuyNum;
        }

        public int getClass_StudyNum() {
            return Class_StudyNum;
        }

        public void setClass_StudyNum(int class_StudyNum) {
            Class_StudyNum = class_StudyNum;
        }

        public int getClass_IsHot() {
            return Class_IsHot;
        }

        public void setClass_IsHot(int class_IsHot) {
            Class_IsHot = class_IsHot;
        }

        public String getClass_MobileImage() {
            return Class_MobileImage;
        }

        public void setClass_MobileImage(String class_MobileImage) {
            Class_MobileImage = class_MobileImage;
        }

        public String getClass_MobileIsVideo() {
            return Class_MobileIsVideo;
        }

        public void setClass_MobileIsVideo(String class_MobileIsVideo) {
            Class_MobileIsVideo = class_MobileIsVideo;
        }

        public String getClass_MobileValue() {
            return Class_MobileValue;
        }

        public void setClass_MobileValue(String class_MobileValue) {
            Class_MobileValue = class_MobileValue;
        }

        public String getClass_MobileContent() {
            return Class_MobileContent;
        }

        public void setClass_MobileContent(String class_MobileContent) {
            Class_MobileContent = class_MobileContent;
        }

        public int getFeatureCount() {
            return FeatureCount;
        }

        public void setFeatureCount(int featureCount) {
            FeatureCount = featureCount;
        }

        public ArrayList<String> getClass_Feature() {
            return Class_Feature;
        }

        public void setClass_Feature(ArrayList<String> class_Feature) {
            Class_Feature = class_Feature;
        }

        public int getClass_OutlineID() {
            return Class_OutlineID;
        }

        public void setClass_OutlineID(int class_OutlineID) {
            Class_OutlineID = class_OutlineID;
        }

        public int getClass_OutlineFreeState() {
            return Class_OutlineFreeState;
        }

        public void setClass_OutlineFreeState(int class_OutlineFreeState) {
            Class_OutlineFreeState = class_OutlineFreeState;
        }

        public String getClass_MinPrice() {
            return Class_MinPrice;
        }

        public void setClass_MinPrice(String class_MinPrice) {
            Class_MinPrice = class_MinPrice;
        }

        public String getClass_MaxPrice() {
            return Class_MaxPrice;
        }

        public void setClass_MaxPrice(String class_MaxPrice) {
            Class_MaxPrice = class_MaxPrice;
        }

        public String getClass_MinSalePrice() {
            return Class_MinSalePrice;
        }

        public void setClass_MinSalePrice(String class_MinSalePrice) {
            Class_MinSalePrice = class_MinSalePrice;
        }

        public String getClass_MaxSalePrice() {
            return Class_MaxSalePrice;
        }

        public void setClass_MaxSalePrice(String class_MaxSalePrice) {
            Class_MaxSalePrice = class_MaxSalePrice;
        }

        public String getClass_PriceRegion() {
            return Class_PriceRegion;
        }

        public void setClass_PriceRegion(String class_PriceRegion) {
            Class_PriceRegion = class_PriceRegion;
        }

        public String getClass_PriceSaleRegion() {
            return Class_PriceSaleRegion;
        }

        public void setClass_PriceSaleRegion(String class_PriceSaleRegion) {
            Class_PriceSaleRegion = class_PriceSaleRegion;
        }

        public int getHasCollection() {
            return HasCollection;
        }

        public void setHasCollection(int hasCollection) {
            HasCollection = hasCollection;
        }
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ClassDetailBean.Body getBody() {
        return Body;
    }

    public void setBody(ClassDetailBean.Body body) {
        Body = body;
    }
}
