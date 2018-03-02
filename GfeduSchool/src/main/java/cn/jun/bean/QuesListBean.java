package cn.jun.bean;


import java.util.ArrayList;

public class QuesListBean {
    private String Code;
    private String Message;
    private Body Body;

    public class Body {
        private String QuesListCount;
        private ArrayList<QuesList> QuesList;

        public class QuesList {
            private String QuesId;
            private String QuesContent;
            private String QuesAddTime;
            private String QuesStatus;
            private String QuesSubjectName;
            private String ImageCount;
            private ArrayList<String> QuesImageUrl;
            private String NickName;
            private String HeadImg;
            private String MyQues;
            private String IsDelete;
            private String DeleteDetail;
            private String RoleType;


            public String getIsDelete() {
                return IsDelete;
            }

            public void setIsDelete(String isDelete) {
                IsDelete = isDelete;
            }

            public String getDeleteDetail() {
                return DeleteDetail;
            }

            public void setDeleteDetail(String deleteDetail) {
                DeleteDetail = deleteDetail;
            }

            public String getRoleType() {
                return RoleType;
            }

            public void setRoleType(String roleType) {
                RoleType = roleType;
            }

            public String getMyQues() {
                return MyQues;
            }

            public void setMyQues(String myQues) {
                MyQues = myQues;
            }

            public ArrayList<String> getQuesImageUrl() {
                return QuesImageUrl;
            }

            public void setQuesImageUrl(ArrayList<String> quesImageUrl) {
                QuesImageUrl = quesImageUrl;
            }

            public String getQuesId() {
                return QuesId;
            }

            public void setQuesId(String quesId) {
                QuesId = quesId;
            }

            public String getQuesContent() {
                return QuesContent;
            }

            public void setQuesContent(String quesContent) {
                QuesContent = quesContent;
            }

            public String getQuesAddTime() {
                return QuesAddTime;
            }

            public void setQuesAddTime(String quesAddTime) {
                QuesAddTime = quesAddTime;
            }

            public String getQuesStatus() {
                return QuesStatus;
            }

            public void setQuesStatus(String quesStatus) {
                QuesStatus = quesStatus;
            }

            public String getQuesSubjectName() {
                return QuesSubjectName;
            }

            public void setQuesSubjectName(String quesSubjectName) {
                QuesSubjectName = quesSubjectName;
            }

            public String getImageCount() {
                return ImageCount;
            }

            public void setImageCount(String imageCount) {
                ImageCount = imageCount;
            }


            public String getNickName() {
                return NickName;
            }

            public void setNickName(String nickName) {
                NickName = nickName;
            }

            public String getHeadImg() {
                return HeadImg;
            }

            public void setHeadImg(String headImg) {
                HeadImg = headImg;
            }
        }

        public String getQuesListCount() {
            return QuesListCount;
        }

        public void setQuesListCount(String quesListCount) {
            QuesListCount = quesListCount;
        }

        public ArrayList<QuesListBean.Body.QuesList> getQuesList() {
            return QuesList;
        }

        public void setQuesList(ArrayList<QuesListBean.Body.QuesList> quesList) {
            QuesList = quesList;
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

    public QuesListBean.Body getBody() {
        return Body;
    }

    public void setBody(QuesListBean.Body body) {
        Body = body;
    }
}
