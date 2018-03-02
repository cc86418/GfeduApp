package cn.jun.bean;


public class AddClassCartPackageBean {
    private int Code;
    private String Message;
    private Body Body;

    public class Body {
        private int CartId;

        public int getCartId() {
            return CartId;
        }

        public void setCartId(int cartId) {
            CartId = cartId;
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

    public AddClassCartPackageBean.Body getBody() {
        return Body;
    }

    public void setBody(AddClassCartPackageBean.Body body) {
        Body = body;
    }
}
