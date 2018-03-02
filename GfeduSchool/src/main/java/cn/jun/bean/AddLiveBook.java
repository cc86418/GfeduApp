package cn.jun.bean;



public class AddLiveBook {
    private int Code;
    private String Message;
    private Body Body;

    public class Body {
        private int IsLiveBegin;

        public int getIsLiveBegin() {
            return IsLiveBegin;
        }

        public void setIsLiveBegin(int isLiveBegin) {
            IsLiveBegin = isLiveBegin;
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

    public AddLiveBook.Body getBody() {
        return Body;
    }

    public void setBody(AddLiveBook.Body body) {
        Body = body;
    }
}
