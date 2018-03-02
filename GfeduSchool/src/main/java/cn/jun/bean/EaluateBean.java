package cn.jun.bean;


import java.util.ArrayList;

public class EaluateBean {
    private int Code;
    private String Message;
    private Body Body;

    public class Body {
        private int ECount;
        private ArrayList<List> List;

        public class List {
            private int Kid;//主键ID
            private String ContentDetail;//问题
            private String ReplyContent;//回复
            private String AddTime;//回复时间

            public int getKid() {
                return Kid;
            }

            public void setKid(int kid) {
                Kid = kid;
            }

            public String getContentDetail() {
                return ContentDetail;
            }

            public void setContentDetail(String contentDetail) {
                ContentDetail = contentDetail;
            }

            public String getReplyContent() {
                return ReplyContent;
            }

            public void setReplyContent(String replyContent) {
                ReplyContent = replyContent;
            }

            public String getAddTime() {
                return AddTime;
            }

            public void setAddTime(String addTime) {
                AddTime = addTime;
            }
        }

        public int getECount() {
            return ECount;
        }

        public void setECount(int ECount) {
            this.ECount = ECount;
        }

        public ArrayList<EaluateBean.Body.List> getList() {
            return List;
        }

        public void setList(ArrayList<EaluateBean.Body.List> list) {
            List = list;
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

    public EaluateBean.Body getBody() {
        return Body;
    }

    public void setBody(EaluateBean.Body body) {
        Body = body;
    }
}
