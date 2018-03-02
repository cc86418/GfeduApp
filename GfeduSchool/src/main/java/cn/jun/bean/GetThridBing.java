package cn.jun.bean;


public class GetThridBing {
    private int Code;
    private String Message;
    private Body Body;

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

    public class Body {
        private String Mobile;
        private String Email;
        private int BindQQ;
        private int BindWECHAT;
        private int BindSINA;
        private int BindEail;


        public String getMobile() {
            return Mobile;
        }

        public int getBindEail() {
            return BindEail;
        }

        public void setBindEail(int bindEail) {
            BindEail = bindEail;
        }

        public void setMobile(String mobile) {
            Mobile = mobile;
        }

        public String getEmail() {
            return Email;
        }

        public void setEmail(String email) {
            Email = email;
        }

        public int getBindQQ() {
            return BindQQ;
        }

        public void setBindQQ(int bindQQ) {
            BindQQ = bindQQ;
        }

        public int getBindWECHAT() {
            return BindWECHAT;
        }

        public void setBindWECHAT(int bindWECHAT) {
            BindWECHAT = bindWECHAT;
        }

        public int getBindSINA() {
            return BindSINA;
        }

        public void setBindSINA(int bindSINA) {
            BindSINA = bindSINA;
        }


    }
}
