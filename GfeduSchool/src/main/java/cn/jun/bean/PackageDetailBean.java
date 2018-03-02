package cn.jun.bean;


import java.util.ArrayList;

public class PackageDetailBean {
    private int Code;
    private String Message;
    private Body Body;

    public class Body {
        private Package Package;
        private ArrayList<String> CutList;
        private ArrayList<ClassList> ClassList;
        private int ClassListCount = -1;//所含班级数量

        public int getClassListCount() {
            return ClassListCount;
        }

        public void setClassListCount(int classListCount) {
            ClassListCount = classListCount;
        }

        public class Package {
            private int Package_PKID;    //套餐ID
            private String Package_Project;    //所属项目
            private String Package_Code;//套餐编号
            private String Package_Name;//套餐名称
            private int Package_Type;//套餐类型 0：固定套餐 1：自由组合套餐
            private String Package_Intro;//套餐简介
            private String Package_IntroTitle;//套餐简介标题
            private String Package_Category;//套餐属性
            private int Package_EvaluateStar;//星级
            private int Package_CollectNum;//收藏数量
            private int Package_BuyNum;//购买数量
            private int Package_StudyNum;//学习数量
            private int Package_IsHot;//是否热推 1：是 0：否
            private ArrayList<String> Package_Feature;//套餐特点
            private String Package_MobileImage;//套餐图片
            private String Package_MobileIsVideo;//是否是视频 1：是 0：否
            private String Package_MobileValue;//视频或图片地址
            private String Package_MobileContent;//详细内容
            private String H5DetailLink;
            private String Package_MinPrice;//套餐原价最小价
            private String Package_MaxPrice;//套餐原价最大价
            private String Package_MinSalePrice;//套餐售价最小价
            private String Package_MaxSalePrice;//套餐售价最大价
            private String Package_PriceRegion;//原价范围
            private String Package_PriceSaleRegion;//售价范围
            private int HasCollection;//是否收藏过 1：是  0：否
            private String H5VideoLink; //H5播放地址


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

            public String getPackage_Intro() {
                return Package_Intro;
            }

            public void setPackage_Intro(String package_Intro) {
                Package_Intro = package_Intro;
            }

            public String getPackage_IntroTitle() {
                return Package_IntroTitle;
            }

            public void setPackage_IntroTitle(String package_IntroTitle) {
                Package_IntroTitle = package_IntroTitle;
            }

            public String getPackage_Category() {
                return Package_Category;
            }

            public void setPackage_Category(String package_Category) {
                Package_Category = package_Category;
            }

            public int getPackage_EvaluateStar() {
                return Package_EvaluateStar;
            }

            public void setPackage_EvaluateStar(int package_EvaluateStar) {
                Package_EvaluateStar = package_EvaluateStar;
            }

            public int getPackage_CollectNum() {
                return Package_CollectNum;
            }

            public void setPackage_CollectNum(int package_CollectNum) {
                Package_CollectNum = package_CollectNum;
            }

            public int getPackage_BuyNum() {
                return Package_BuyNum;
            }

            public void setPackage_BuyNum(int package_BuyNum) {
                Package_BuyNum = package_BuyNum;
            }

            public int getPackage_StudyNum() {
                return Package_StudyNum;
            }

            public void setPackage_StudyNum(int package_StudyNum) {
                Package_StudyNum = package_StudyNum;
            }

            public int getPackage_IsHot() {
                return Package_IsHot;
            }

            public void setPackage_IsHot(int package_IsHot) {
                Package_IsHot = package_IsHot;
            }

            public ArrayList<String> getPackage_Feature() {
                return Package_Feature;
            }

            public void setPackage_Feature(ArrayList<String> package_Feature) {
                Package_Feature = package_Feature;
            }

            public String getPackage_MobileImage() {
                return Package_MobileImage;
            }

            public void setPackage_MobileImage(String package_MobileImage) {
                Package_MobileImage = package_MobileImage;
            }

            public String getPackage_MobileIsVideo() {
                return Package_MobileIsVideo;
            }

            public void setPackage_MobileIsVideo(String package_MobileIsVideo) {
                Package_MobileIsVideo = package_MobileIsVideo;
            }

            public String getPackage_MobileValue() {
                return Package_MobileValue;
            }

            public void setPackage_MobileValue(String package_MobileValue) {
                Package_MobileValue = package_MobileValue;
            }

            public String getPackage_MobileContent() {
                return Package_MobileContent;
            }

            public void setPackage_MobileContent(String package_MobileContent) {
                Package_MobileContent = package_MobileContent;
            }

            public String getPackage_MinPrice() {
                return Package_MinPrice;
            }

            public void setPackage_MinPrice(String package_MinPrice) {
                Package_MinPrice = package_MinPrice;
            }

            public String getPackage_MaxPrice() {
                return Package_MaxPrice;
            }

            public void setPackage_MaxPrice(String package_MaxPrice) {
                Package_MaxPrice = package_MaxPrice;
            }

            public String getPackage_MinSalePrice() {
                return Package_MinSalePrice;
            }

            public void setPackage_MinSalePrice(String package_MinSalePrice) {
                Package_MinSalePrice = package_MinSalePrice;
            }

            public String getPackage_MaxSalePrice() {
                return Package_MaxSalePrice;
            }

            public void setPackage_MaxSalePrice(String package_MaxSalePrice) {
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

            public int getHasCollection() {
                return HasCollection;
            }

            public void setHasCollection(int hasCollection) {
                HasCollection = hasCollection;
            }
        }

        public class ClassList {
            private int Link_ProductID; //购买ID
            private int Class_PKID;//班级ID
            private String Class_Name;//班级名称
            private String Class_MobileImage;//图片
            private String Class_MinPrice;//最小原价
            private String Class_MaxPrice;//最大原价
            private String Class_MinSalePrice;//最小售价
            private String Class_MaxSalePrice;//最大售价
            private String Class_PriceRegion;//原价范围
            private String Class_PriceSaleRegion;//售价范围
            private int Class_OutlineFreeState;//是否有免费视频
            private String Class_StudyNum;//学习人数

            public int getLink_ProductID() {
                return Link_ProductID;
            }

            public void setLink_ProductID(int link_ProductID) {
                Link_ProductID = link_ProductID;
            }

            public String getClass_StudyNum() {
                return Class_StudyNum;
            }

            public void setClass_StudyNum(String class_StudyNum) {
                Class_StudyNum = class_StudyNum;
            }

            public int getClass_PKID() {
                return Class_PKID;
            }

            public void setClass_PKID(int class_PKID) {
                Class_PKID = class_PKID;
            }

            public String getClass_Name() {
                return Class_Name;
            }

            public void setClass_Name(String class_Name) {
                Class_Name = class_Name;
            }

            public String getClass_MobileImage() {
                return Class_MobileImage;
            }

            public void setClass_MobileImage(String class_MobileImage) {
                Class_MobileImage = class_MobileImage;
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

            public int getClass_OutlineFreeState() {
                return Class_OutlineFreeState;
            }

            public void setClass_OutlineFreeState(int class_OutlineFreeState) {
                Class_OutlineFreeState = class_OutlineFreeState;
            }
        }

        public PackageDetailBean.Body.Package getPackage() {
            return Package;
        }

        public void setPackage(PackageDetailBean.Body.Package aPackage) {
            Package = aPackage;
        }

        public ArrayList<String> getCutList() {
            return CutList;
        }

        public void setCutList(ArrayList<String> cutList) {
            CutList = cutList;
        }

        public ArrayList<PackageDetailBean.Body.ClassList> getClassList() {
            return ClassList;
        }

        public void setClassList(ArrayList<PackageDetailBean.Body.ClassList> classList) {
            ClassList = classList;
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

    public PackageDetailBean.Body getBody() {
        return Body;
    }

    public void setBody(PackageDetailBean.Body body) {
        Body = body;
    }
}
