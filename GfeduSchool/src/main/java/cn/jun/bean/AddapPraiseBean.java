package cn.jun.bean;


public class AddapPraiseBean {
    private int code;
    private String Message;
    private Body Body;

    public class Body {
        public int Appraise_PKID;

        public int getAppraise_PKID() {
            return Appraise_PKID;
        }

        public void setAppraise_PKID(int appraise_PKID) {
            Appraise_PKID = appraise_PKID;
        }
    }

    public AddapPraiseBean.Body getBody() {
        return Body;
    }

    public int getCode() {
        return code;
    }

    public void setBody(AddapPraiseBean.Body body) {
        Body = body;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }


}
