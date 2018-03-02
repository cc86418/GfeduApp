package cn.jun.bean;


import java.util.ArrayList;

public class AdsListBean {
    private String Code;
    private String Message;
    private Body Body;

    public class Body {
        private int AdCount;
        private ArrayList<List> List;

        public class List {
            private int ProductType;//产品类型： 2-班级 5-套餐
            private int ProductId;//班级或套餐ID
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

        public int getAdCount() {
            return AdCount;
        }

        public void setAdCount(int adCount) {
            AdCount = adCount;
        }

        public ArrayList<AdsListBean.Body.List> getList() {
            return List;
        }

        public void setList(ArrayList<AdsListBean.Body.List> list) {
            List = list;
        }
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public AdsListBean.Body getBody() {
        return Body;
    }

    public void setBody(AdsListBean.Body body) {
        Body = body;
    }
}
