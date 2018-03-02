package cn.jun.bean;


import java.io.Serializable;
import java.util.ArrayList;

public class ProductListBean implements Serializable {
    private String Code;
    private String Message;
    private Body Body;

    public class Body {
        private int ProductCount;
        private ArrayList<CustomBean> List;

        public int getProductCount() {
            return ProductCount;
        }

        public void setProductCount(int productCount) {
            ProductCount = productCount;
        }

        public ArrayList<CustomBean> getList() {
            return List;
        }

        public void setList(ArrayList<CustomBean> list) {
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

    public ProductListBean.Body getBody() {
        return Body;
    }

    public void setBody(ProductListBean.Body body) {
        Body = body;
    }
}
