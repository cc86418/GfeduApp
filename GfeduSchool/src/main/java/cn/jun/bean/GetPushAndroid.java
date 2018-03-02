package cn.jun.bean;


public class GetPushAndroid {
    private int Code;
    private String Message;
    private Body Body;

    public class Body {
        private int Inform_PKID;
        private String Inform_Title;
        private String Inform_Content;
        private String Inform_Url;
        private int Inform_Type;
        private int Inform_Platform;
        private boolean Inform_IsRead;
        private String Inform_CreateUser;
        private String Inform_AddTime;
        private String Inform_Item;
        private String Inform_TypeID;
        private String Inform_State;
        private String Inform_IsDelete;
        private String Inform_IsPushAndroid;


        public String getInform_Title() {
            return Inform_Title;
        }

        public void setInform_Title(String inform_Title) {
            Inform_Title = inform_Title;
        }

        public String getInform_Url() {
            return Inform_Url;
        }

        public void setInform_Url(String inform_Url) {
            Inform_Url = inform_Url;
        }

        public int getInform_Type() {
            return Inform_Type;
        }

        public void setInform_Type(int inform_Type) {
            Inform_Type = inform_Type;
        }

        public int getInform_Platform() {
            return Inform_Platform;
        }

        public void setInform_Platform(int inform_Platform) {
            Inform_Platform = inform_Platform;
        }

        public boolean isInform_IsRead() {
            return Inform_IsRead;
        }

        public void setInform_IsRead(boolean inform_IsRead) {
            Inform_IsRead = inform_IsRead;
        }

        public String getInform_CreateUser() {
            return Inform_CreateUser;
        }

        public void setInform_CreateUser(String inform_CreateUser) {
            Inform_CreateUser = inform_CreateUser;
        }

        public String getInform_AddTime() {
            return Inform_AddTime;
        }

        public void setInform_AddTime(String inform_AddTime) {
            Inform_AddTime = inform_AddTime;
        }

        public String getInform_Item() {
            return Inform_Item;
        }

        public void setInform_Item(String inform_Item) {
            Inform_Item = inform_Item;
        }

        public String getInform_TypeID() {
            return Inform_TypeID;
        }

        public void setInform_TypeID(String inform_TypeID) {
            Inform_TypeID = inform_TypeID;
        }

        public String getInform_State() {
            return Inform_State;
        }

        public void setInform_State(String inform_State) {
            Inform_State = inform_State;
        }

        public String getInform_IsDelete() {
            return Inform_IsDelete;
        }

        public void setInform_IsDelete(String inform_IsDelete) {
            Inform_IsDelete = inform_IsDelete;
        }

        public String getInform_IsPushAndroid() {
            return Inform_IsPushAndroid;
        }

        public void setInform_IsPushAndroid(String inform_IsPushAndroid) {
            Inform_IsPushAndroid = inform_IsPushAndroid;
        }

        public int getInform_PKID() {
            return Inform_PKID;
        }

        public void setInform_PKID(int inform_PKID) {
            Inform_PKID = inform_PKID;
        }

        public String getInform_Content() {
            return Inform_Content;
        }

        public void setInform_Content(String inform_Content) {
            Inform_Content = inform_Content;
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

    public GetPushAndroid.Body getBody() {
        return Body;
    }

    public void setBody(GetPushAndroid.Body body) {
        Body = body;
    }
}
