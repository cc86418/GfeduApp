package cn.jun.bean;


import java.util.ArrayList;

public class OverClassListBean {
    private String Code;
    private String Message;
    private Body Body;

    public class Body {
        private String EndListCount;
        private ArrayList<EndList> EndList;

        public class EndList {
            private String ClassId;
            private String ClassName;
            private String ClassStartTime;
            private String ClassEndTime;
            private String ClassImg;
            private String Link_State;
            private String CloseType;

            public String getClassId() {
                return ClassId;
            }

            public void setClassId(String classId) {
                ClassId = classId;
            }

            public String getClassName() {
                return ClassName;
            }

            public void setClassName(String className) {
                ClassName = className;
            }

            public String getClassStartTime() {
                return ClassStartTime;
            }

            public void setClassStartTime(String classStartTime) {
                ClassStartTime = classStartTime;
            }

            public String getClassEndTime() {
                return ClassEndTime;
            }

            public void setClassEndTime(String classEndTime) {
                ClassEndTime = classEndTime;
            }

            public String getClassImg() {
                return ClassImg;
            }

            public void setClassImg(String classImg) {
                ClassImg = classImg;
            }

            public String getLink_State() {
                return Link_State;
            }

            public void setLink_State(String link_State) {
                Link_State = link_State;
            }

            public String getCloseType() {
                return CloseType;
            }

            public void setCloseType(String closeType) {
                CloseType = closeType;
            }
        }

        public String getEndListCount() {
            return EndListCount;
        }

        public void setEndListCount(String endListCount) {
            EndListCount = endListCount;
        }

        public ArrayList<OverClassListBean.Body.EndList> getEndList() {
            return EndList;
        }

        public void setEndList(ArrayList<OverClassListBean.Body.EndList> endList) {
            EndList = endList;
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

    public OverClassListBean.Body getBody() {
        return Body;
    }

    public void setBody(OverClassListBean.Body body) {
        Body = body;
    }
}
