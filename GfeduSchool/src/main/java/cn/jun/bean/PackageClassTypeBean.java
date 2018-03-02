package cn.jun.bean;


import java.util.ArrayList;

public class PackageClassTypeBean {
    private ArrayList<ProductList> ProductList; //套餐产品列表

    public class ProductList {
        private int Link_PKID;//唯一ID
        private int Link_ProductID;//班级或商品ID
        private String Link_ProductType;//2：班级 3：商品
        private int Link_BuyType;//0：自选 1：现购 2：预购
        private ClassInfo ClassInfo;//班级信息
        private ArrayList<ClassTypeList> ClassTypeList; //班型列表
        private ArrayList<String> ExameDateList;//考试日期
        private ArrayList<String> ClassTypeTypeList;//班型类型
        private ArrayList<String> ShoolPlaceList;//考试地点
        private Goods Goods; //商品信息

        public ArrayList<String> getExameDateList() {
            return ExameDateList;
        }

        public void setExameDateList(ArrayList<String> exameDateList) {
            ExameDateList = exameDateList;
        }

        public ArrayList<String> getClassTypeTypeList() {
            return ClassTypeTypeList;
        }

        public void setClassTypeTypeList(ArrayList<String> classTypeTypeList) {
            ClassTypeTypeList = classTypeTypeList;
        }

        public ArrayList<String> getShoolPlaceList() {
            return ShoolPlaceList;
        }

        public void setShoolPlaceList(ArrayList<String> shoolPlaceList) {
            ShoolPlaceList = shoolPlaceList;
        }

        public class ClassInfo {
            private int Class_PKID;//班级ID
            private String Class_Name;//班级名称
            private String Class_MobileImage;//图片
            private String Class_MinPrice;//最小原价
            private String Class_MaxPrice;//最大原价
            private String Class_MinSalePrice;//最小售价
            private String Class_MaxSalePrice;//最大售价
            private String Class_PriceRegion;//原价范围
            private String Class_PriceSaleRegion;//售价范围

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
        }

        public class ClassTypeList {
            private int ClassType_PKID;//班型ID
            private String ClassType_Name;//班级名称
            private int ClassType_Type;//1：面授 2：在线 4：直播
            private String ClassType_TypeName;//类型名称
            private String ClassType_Price;//班型原价
            private String ClassType_SalePrice;//班型售价
            private String ClassType_ExamDate;//考试日期
            private String ClassType_ExamDateName;//考试日期格式化
            private String ClassType_BeginDate;//开课日期
            private String ClassType_EndDate;//结束日期
            private String ClassType_Date;//课程时间
            private String ClassType_PlaceName;//考试地点
            private int ClassType_StudyTime;//学时
            private int ClassType_Mode;//["1":"固定时间","2":"售后即开"]
            private int ClassType_StudyDay;//学习天数
            private int ClassType_IsPresale;//1：预购 0：现购
            private String ExpireDate; //时间

            //是否选择的项目
            private boolean isChoose = false;

            public boolean isChoose() {
                return isChoose;
            }

            public void setChoose(boolean choose) {
                isChoose = choose;
            }

            public String getExpireDate() {
                return ExpireDate;
            }

            public void setExpireDate(String expireDate) {
                ExpireDate = expireDate;
            }

            public int getClassType_PKID() {
                return ClassType_PKID;
            }

            public void setClassType_PKID(int classType_PKID) {
                ClassType_PKID = classType_PKID;
            }

            public String getClassType_Name() {
                return ClassType_Name;
            }

            public void setClassType_Name(String classType_Name) {
                ClassType_Name = classType_Name;
            }

            public int getClassType_Type() {
                return ClassType_Type;
            }

            public void setClassType_Type(int classType_Type) {
                ClassType_Type = classType_Type;
            }

            public String getClassType_TypeName() {
                return ClassType_TypeName;
            }

            public void setClassType_TypeName(String classType_TypeName) {
                ClassType_TypeName = classType_TypeName;
            }

            public String getClassType_Price() {
                return ClassType_Price;
            }

            public void setClassType_Price(String classType_Price) {
                ClassType_Price = classType_Price;
            }

            public String getClassType_SalePrice() {
                return ClassType_SalePrice;
            }

            public void setClassType_SalePrice(String classType_SalePrice) {
                ClassType_SalePrice = classType_SalePrice;
            }

            public String getClassType_ExamDate() {
                return ClassType_ExamDate;
            }

            public void setClassType_ExamDate(String classType_ExamDate) {
                ClassType_ExamDate = classType_ExamDate;
            }

            public String getClassType_ExamDateName() {
                return ClassType_ExamDateName;
            }

            public void setClassType_ExamDateName(String classType_ExamDateName) {
                ClassType_ExamDateName = classType_ExamDateName;
            }

            public String getClassType_BeginDate() {
                return ClassType_BeginDate;
            }

            public void setClassType_BeginDate(String classType_BeginDate) {
                ClassType_BeginDate = classType_BeginDate;
            }

            public String getClassType_EndDate() {
                return ClassType_EndDate;
            }

            public void setClassType_EndDate(String classType_EndDate) {
                ClassType_EndDate = classType_EndDate;
            }

            public String getClassType_Date() {
                return ClassType_Date;
            }

            public void setClassType_Date(String classType_Date) {
                ClassType_Date = classType_Date;
            }

            public String getClassType_PlaceName() {
                return ClassType_PlaceName;
            }

            public void setClassType_PlaceName(String classType_PlaceName) {
                ClassType_PlaceName = classType_PlaceName;
            }

            public int getClassType_StudyTime() {
                return ClassType_StudyTime;
            }

            public void setClassType_StudyTime(int classType_StudyTime) {
                ClassType_StudyTime = classType_StudyTime;
            }

            public int getClassType_Mode() {
                return ClassType_Mode;
            }

            public void setClassType_Mode(int classType_Mode) {
                ClassType_Mode = classType_Mode;
            }

            public int getClassType_StudyDay() {
                return ClassType_StudyDay;
            }

            public void setClassType_StudyDay(int classType_StudyDay) {
                ClassType_StudyDay = classType_StudyDay;
            }

            public int getClassType_IsPresale() {
                return ClassType_IsPresale;
            }

            public void setClassType_IsPresale(int classType_IsPresale) {
                ClassType_IsPresale = classType_IsPresale;
            }


        }

        public class Goods {
            private int Goods_PKID;//商品ID
            private String Goods_Name;//商品名称
            private String Goods_Code;//商品编号
            private String Goods_Price;//原价
            private String Goods_SalePrice;//售价
            private String Goods_Image;//商品图片

            public int getGoods_PKID() {
                return Goods_PKID;
            }

            public void setGoods_PKID(int goods_PKID) {
                Goods_PKID = goods_PKID;
            }

            public String getGoods_Name() {
                return Goods_Name;
            }

            public void setGoods_Name(String goods_Name) {
                Goods_Name = goods_Name;
            }

            public String getGoods_Code() {
                return Goods_Code;
            }

            public void setGoods_Code(String goods_Code) {
                Goods_Code = goods_Code;
            }

            public String getGoods_Price() {
                return Goods_Price;
            }

            public void setGoods_Price(String goods_Price) {
                Goods_Price = goods_Price;
            }

            public String getGoods_SalePrice() {
                return Goods_SalePrice;
            }

            public void setGoods_SalePrice(String goods_SalePrice) {
                Goods_SalePrice = goods_SalePrice;
            }

            public String getGoods_Image() {
                return Goods_Image;
            }

            public void setGoods_Image(String goods_Image) {
                Goods_Image = goods_Image;
            }
        }

        public int getLink_PKID() {
            return Link_PKID;
        }

        public void setLink_PKID(int link_PKID) {
            Link_PKID = link_PKID;
        }

        public int getLink_ProductID() {
            return Link_ProductID;
        }

        public void setLink_ProductID(int link_ProductID) {
            Link_ProductID = link_ProductID;
        }

        public String getLink_ProductType() {
            return Link_ProductType;
        }

        public void setLink_ProductType(String link_ProductType) {
            Link_ProductType = link_ProductType;
        }

        public int getLink_BuyType() {
            return Link_BuyType;
        }

        public void setLink_BuyType(int link_BuyType) {
            Link_BuyType = link_BuyType;
        }

        public PackageClassTypeBean.ProductList.ClassInfo getClassInfo() {
            return ClassInfo;
        }

        public void setClassInfo(PackageClassTypeBean.ProductList.ClassInfo classInfo) {
            ClassInfo = classInfo;
        }

        public ArrayList<PackageClassTypeBean.ProductList.ClassTypeList> getClassTypeList() {
            return ClassTypeList;
        }

        public void setClassTypeList(ArrayList<PackageClassTypeBean.ProductList.ClassTypeList> classTypeList) {
            ClassTypeList = classTypeList;
        }

        public PackageClassTypeBean.ProductList.Goods getGoods() {
            return Goods;
        }

        public void setGoods(PackageClassTypeBean.ProductList.Goods goods) {
            Goods = goods;
        }
    }

    public ArrayList<PackageClassTypeBean.ProductList> getProductList() {
        return ProductList;
    }

    public void setProductList(ArrayList<PackageClassTypeBean.ProductList> productList) {
        ProductList = productList;
    }

    //    private int Code;
//    private String Message;
//    private Body Body;
//
//    public class Body {
//        private ArrayList<ProductList> ProductList; //套餐产品列表
//
//        public class ProductList {
//            private int Link_PKID;//唯一ID
//            private int Link_ProductID;//班级或商品ID
//            private String Link_ProductType;//2：班级 3：商品
//            private int Link_BuyType;//0：自选 1：现购 2：预购
//            private ClassInfo ClassInfo;//班级信息
//            private ArrayList<ClassTypeList> ClassTypeList; //班型列表
//            private ArrayList<String> ExameDateList;//考试日期
//            private ArrayList<String> ClassTypeTypeList;//班型类型
//            private ArrayList<String> ShoolPlaceList;//考试地点
//            private Goods Goods; //商品信息
//
//            public ArrayList<String> getExameDateList() {
//                return ExameDateList;
//            }
//
//            public void setExameDateList(ArrayList<String> exameDateList) {
//                ExameDateList = exameDateList;
//            }
//
//            public ArrayList<String> getClassTypeTypeList() {
//                return ClassTypeTypeList;
//            }
//
//            public void setClassTypeTypeList(ArrayList<String> classTypeTypeList) {
//                ClassTypeTypeList = classTypeTypeList;
//            }
//
//            public ArrayList<String> getShoolPlaceList() {
//                return ShoolPlaceList;
//            }
//
//            public void setShoolPlaceList(ArrayList<String> shoolPlaceList) {
//                ShoolPlaceList = shoolPlaceList;
//            }
//
//            public class ClassInfo {
//                private int Class_PKID;//班级ID
//                private String Class_Name;//班级名称
//                private String Class_MobileImage;//图片
//                private String Class_MinPrice;//最小原价
//                private String Class_MaxPrice;//最大原价
//                private String Class_MinSalePrice;//最小售价
//                private String Class_MaxSalePrice;//最大售价
//                private String Class_PriceRegion;//原价范围
//                private String Class_PriceSaleRegion;//售价范围
//
//                public int getClass_PKID() {
//                    return Class_PKID;
//                }
//
//                public void setClass_PKID(int class_PKID) {
//                    Class_PKID = class_PKID;
//                }
//
//                public String getClass_Name() {
//                    return Class_Name;
//                }
//
//                public void setClass_Name(String class_Name) {
//                    Class_Name = class_Name;
//                }
//
//                public String getClass_MobileImage() {
//                    return Class_MobileImage;
//                }
//
//                public void setClass_MobileImage(String class_MobileImage) {
//                    Class_MobileImage = class_MobileImage;
//                }
//
//                public String getClass_MinPrice() {
//                    return Class_MinPrice;
//                }
//
//                public void setClass_MinPrice(String class_MinPrice) {
//                    Class_MinPrice = class_MinPrice;
//                }
//
//                public String getClass_MaxPrice() {
//                    return Class_MaxPrice;
//                }
//
//                public void setClass_MaxPrice(String class_MaxPrice) {
//                    Class_MaxPrice = class_MaxPrice;
//                }
//
//                public String getClass_MinSalePrice() {
//                    return Class_MinSalePrice;
//                }
//
//                public void setClass_MinSalePrice(String class_MinSalePrice) {
//                    Class_MinSalePrice = class_MinSalePrice;
//                }
//
//                public String getClass_MaxSalePrice() {
//                    return Class_MaxSalePrice;
//                }
//
//                public void setClass_MaxSalePrice(String class_MaxSalePrice) {
//                    Class_MaxSalePrice = class_MaxSalePrice;
//                }
//
//                public String getClass_PriceRegion() {
//                    return Class_PriceRegion;
//                }
//
//                public void setClass_PriceRegion(String class_PriceRegion) {
//                    Class_PriceRegion = class_PriceRegion;
//                }
//
//                public String getClass_PriceSaleRegion() {
//                    return Class_PriceSaleRegion;
//                }
//
//                public void setClass_PriceSaleRegion(String class_PriceSaleRegion) {
//                    Class_PriceSaleRegion = class_PriceSaleRegion;
//                }
//            }
//
//            public class ClassTypeList {
//                private int ClassType_PKID;//班型ID
//                private String ClassType_Name;//班级名称
//                private int ClassType_Type;//1：面授 2：在线 4：直播
//                private String ClassType_TypeName;//类型名称
//                private String ClassType_Price;//班型原价
//                private String ClassType_SalePrice;//班型售价
//                private String ClassType_ExamDate;//考试日期
//                private String ClassType_ExamDateName;//考试日期格式化
//                private String ClassType_BeginDate;//开课日期
//                private String ClassType_EndDate;//结束日期
//                private String ClassType_Date;//课程时间
//                private String ClassType_PlaceName;//考试地点
//                private int ClassType_StudyTime;//学时
//                private int ClassType_Mode;//["1":"固定时间","2":"售后即开"]
//                private int ClassType_StudyDay;//学习天数
//                private int ClassType_IsPresale;//1：预购 0：现购
//                private String ExpireDate; //时间
//
//                public String getExpireDate() {
//                    return ExpireDate;
//                }
//
//                public void setExpireDate(String expireDate) {
//                    ExpireDate = expireDate;
//                }
//
//                public int getClassType_PKID() {
//                    return ClassType_PKID;
//                }
//
//                public void setClassType_PKID(int classType_PKID) {
//                    ClassType_PKID = classType_PKID;
//                }
//
//                public String getClassType_Name() {
//                    return ClassType_Name;
//                }
//
//                public void setClassType_Name(String classType_Name) {
//                    ClassType_Name = classType_Name;
//                }
//
//                public int getClassType_Type() {
//                    return ClassType_Type;
//                }
//
//                public void setClassType_Type(int classType_Type) {
//                    ClassType_Type = classType_Type;
//                }
//
//                public String getClassType_TypeName() {
//                    return ClassType_TypeName;
//                }
//
//                public void setClassType_TypeName(String classType_TypeName) {
//                    ClassType_TypeName = classType_TypeName;
//                }
//
//                public String getClassType_Price() {
//                    return ClassType_Price;
//                }
//
//                public void setClassType_Price(String classType_Price) {
//                    ClassType_Price = classType_Price;
//                }
//
//                public String getClassType_SalePrice() {
//                    return ClassType_SalePrice;
//                }
//
//                public void setClassType_SalePrice(String classType_SalePrice) {
//                    ClassType_SalePrice = classType_SalePrice;
//                }
//
//                public String getClassType_ExamDate() {
//                    return ClassType_ExamDate;
//                }
//
//                public void setClassType_ExamDate(String classType_ExamDate) {
//                    ClassType_ExamDate = classType_ExamDate;
//                }
//
//                public String getClassType_ExamDateName() {
//                    return ClassType_ExamDateName;
//                }
//
//                public void setClassType_ExamDateName(String classType_ExamDateName) {
//                    ClassType_ExamDateName = classType_ExamDateName;
//                }
//
//                public String getClassType_BeginDate() {
//                    return ClassType_BeginDate;
//                }
//
//                public void setClassType_BeginDate(String classType_BeginDate) {
//                    ClassType_BeginDate = classType_BeginDate;
//                }
//
//                public String getClassType_EndDate() {
//                    return ClassType_EndDate;
//                }
//
//                public void setClassType_EndDate(String classType_EndDate) {
//                    ClassType_EndDate = classType_EndDate;
//                }
//
//                public String getClassType_Date() {
//                    return ClassType_Date;
//                }
//
//                public void setClassType_Date(String classType_Date) {
//                    ClassType_Date = classType_Date;
//                }
//
//                public String getClassType_PlaceName() {
//                    return ClassType_PlaceName;
//                }
//
//                public void setClassType_PlaceName(String classType_PlaceName) {
//                    ClassType_PlaceName = classType_PlaceName;
//                }
//
//                public int getClassType_StudyTime() {
//                    return ClassType_StudyTime;
//                }
//
//                public void setClassType_StudyTime(int classType_StudyTime) {
//                    ClassType_StudyTime = classType_StudyTime;
//                }
//
//                public int getClassType_Mode() {
//                    return ClassType_Mode;
//                }
//
//                public void setClassType_Mode(int classType_Mode) {
//                    ClassType_Mode = classType_Mode;
//                }
//
//                public int getClassType_StudyDay() {
//                    return ClassType_StudyDay;
//                }
//
//                public void setClassType_StudyDay(int classType_StudyDay) {
//                    ClassType_StudyDay = classType_StudyDay;
//                }
//
//                public int getClassType_IsPresale() {
//                    return ClassType_IsPresale;
//                }
//
//                public void setClassType_IsPresale(int classType_IsPresale) {
//                    ClassType_IsPresale = classType_IsPresale;
//                }
//
//
//            }
//
//            public class Goods {
//                private int Goods_PKID;//商品ID
//                private String Goods_Name;//商品名称
//                private String Goods_Code;//商品编号
//                private String Goods_Price;//原价
//                private String Goods_SalePrice;//售价
//                private String Goods_Image;//商品图片
//
//                public int getGoods_PKID() {
//                    return Goods_PKID;
//                }
//
//                public void setGoods_PKID(int goods_PKID) {
//                    Goods_PKID = goods_PKID;
//                }
//
//                public String getGoods_Name() {
//                    return Goods_Name;
//                }
//
//                public void setGoods_Name(String goods_Name) {
//                    Goods_Name = goods_Name;
//                }
//
//                public String getGoods_Code() {
//                    return Goods_Code;
//                }
//
//                public void setGoods_Code(String goods_Code) {
//                    Goods_Code = goods_Code;
//                }
//
//                public String getGoods_Price() {
//                    return Goods_Price;
//                }
//
//                public void setGoods_Price(String goods_Price) {
//                    Goods_Price = goods_Price;
//                }
//
//                public String getGoods_SalePrice() {
//                    return Goods_SalePrice;
//                }
//
//                public void setGoods_SalePrice(String goods_SalePrice) {
//                    Goods_SalePrice = goods_SalePrice;
//                }
//
//                public String getGoods_Image() {
//                    return Goods_Image;
//                }
//
//                public void setGoods_Image(String goods_Image) {
//                    Goods_Image = goods_Image;
//                }
//            }
//
//            public int getLink_PKID() {
//                return Link_PKID;
//            }
//
//            public void setLink_PKID(int link_PKID) {
//                Link_PKID = link_PKID;
//            }
//
//            public int getLink_ProductID() {
//                return Link_ProductID;
//            }
//
//            public void setLink_ProductID(int link_ProductID) {
//                Link_ProductID = link_ProductID;
//            }
//
//            public String getLink_ProductType() {
//                return Link_ProductType;
//            }
//
//            public void setLink_ProductType(String link_ProductType) {
//                Link_ProductType = link_ProductType;
//            }
//
//            public int getLink_BuyType() {
//                return Link_BuyType;
//            }
//
//            public void setLink_BuyType(int link_BuyType) {
//                Link_BuyType = link_BuyType;
//            }
//
//            public PackageClassTypeBean.Body.ProductList.ClassInfo getClassInfo() {
//                return ClassInfo;
//            }
//
//            public void setClassInfo(PackageClassTypeBean.Body.ProductList.ClassInfo classInfo) {
//                ClassInfo = classInfo;
//            }
//
//            public ArrayList<PackageClassTypeBean.Body.ProductList.ClassTypeList> getClassTypeList() {
//                return ClassTypeList;
//            }
//
//            public void setClassTypeList(ArrayList<PackageClassTypeBean.Body.ProductList.ClassTypeList> classTypeList) {
//                ClassTypeList = classTypeList;
//            }
//
//            public PackageClassTypeBean.Body.ProductList.Goods getGoods() {
//                return Goods;
//            }
//
//            public void setGoods(PackageClassTypeBean.Body.ProductList.Goods goods) {
//                Goods = goods;
//            }
//        }
//
//        public ArrayList<PackageClassTypeBean.Body.ProductList> getProductList() {
//            return ProductList;
//        }
//
//        public void setProductList(ArrayList<PackageClassTypeBean.Body.ProductList> productList) {
//            ProductList = productList;
//        }
//    }
//
//    public int getCode() {
//        return Code;
//    }
//
//    public void setCode(int code) {
//        Code = code;
//    }
//
//    public String getMessage() {
//        return Message;
//    }
//
//    public void setMessage(String message) {
//        Message = message;
//    }
//
//    public PackageClassTypeBean.Body getBody() {
//        return Body;
//    }
//
//    public void setBody(PackageClassTypeBean.Body body) {
//        Body = body;
//    }
}
