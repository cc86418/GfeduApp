package cn.jun.bean;


public class GetLiveUrlBean {
    private int Code;
    private String Message;
    private Body Body;

    public class Body {
        private String LinkURL;

        public String getLinkURL() {
            return LinkURL;
        }

        public void setLinkURL(String linkURL) {
            LinkURL = linkURL;
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

    public GetLiveUrlBean.Body getBody() {
        return Body;
    }

    public void setBody(GetLiveUrlBean.Body body) {
        Body = body;
    }
}
