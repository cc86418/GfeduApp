package cn.jun.bean;


import java.util.ArrayList;

public class MyCollectionBean {
    private int Code;
    private String Message;
    private Body Body;

    public class Body {
        private int CollectionCount;//收藏数量
        private ArrayList<CollectionList> CollectionList;//收藏列表

        public class CollectionList {
            private int Kid;//主键
            private int ProductModule;//2：班级 5：跳槽
            private int Class_PKID;//班级 或 套餐ID
            private String Class_Name;//班级或套餐名称
            private String Class_IntroName;//班级简介标题
            private String Class_Intro;//班级简介
            private String Class_MobileImage;//图片地址
            private String Class_PriceRegion;//原价范围
            private String Class_PriceSaleRegion;//售价范围
            private int Class_OutlineFreeState;//是否有免费试听课程 1：是 0：否
            private int Class_Form;//判断直播类型（系列或者单直播）

            public int getClass_Form() {
                return Class_Form;
            }

            public void setClass_Form(int class_Form) {
                Class_Form = class_Form;
            }

            public int getKid() {
                return Kid;
            }

            public void setKid(int kid) {
                Kid = kid;
            }

            public int getProductModule() {
                return ProductModule;
            }

            public void setProductModule(int productModule) {
                ProductModule = productModule;
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

            public String getClass_MobileImage() {
                return Class_MobileImage;
            }

            public void setClass_MobileImage(String class_MobileImage) {
                Class_MobileImage = class_MobileImage;
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

        public int getCollectionCount() {
            return CollectionCount;
        }

        public void setCollectionCount(int collectionCount) {
            CollectionCount = collectionCount;
        }

        public ArrayList<MyCollectionBean.Body.CollectionList> getCollectionList() {
            return CollectionList;
        }

        public void setCollectionList(ArrayList<MyCollectionBean.Body.CollectionList> collectionList) {
            CollectionList = collectionList;
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

    public MyCollectionBean.Body getBody() {
        return Body;
    }

    public void setBody(MyCollectionBean.Body body) {
        Body = body;
    }
}
