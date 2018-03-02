package cn.jun.bean;


public class CheckhAsBindBean {
    private int Code;
    private String Message;
    private Body Body;

    public class Body {
        private int BindStatus;

        public int getBindStatus() {
            return BindStatus;
        }

        public void setBindStatus(int bindStatus) {
            BindStatus = bindStatus;
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

    public Body getBody() {
        return Body;
    }

    public void setBody(Body body) {
        Body = body;
    }


}
