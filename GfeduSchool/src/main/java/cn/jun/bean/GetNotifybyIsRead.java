package cn.jun.bean;


import java.util.ArrayList;

public class GetNotifybyIsRead {
    private int Code;
    private String Message;
    private Body Body;

    public class Body {
        private int ListCount;
        private int TotalPageIndex;
        private int TotalCount;
        private ArrayList<UserInformList> UserInformList;

        public class UserInformList {
            private int Inform_PKID;//通知ID
            private String Inform_Content;//通知内容
            private int Inform_Type;//通知类型 1.订单通知   2.课程通知   3.系统通知   4.学习互动
            private int Inform_IsRead;//1.已读   0.未读
            private String Inform_AddTime;//通知事件
            private int OrderID;//订单id
            private int QuesID;//问题id
            private int PageIndex;//页数
            private int ChildClassTypeId;//子班型ID
            private int ClassId;//班级ID
            private int LessonID;//课程ID
            private int QuesState;//是否开启问题   1：开启   0.关闭
            private int NoteState;//是否开启笔记   1：开启   0.关闭
            private int InformationState;//是否开启资料   1：开启   0.关闭


            public int getOrderID() {
                return OrderID;
            }

            public void setOrderID(int orderID) {
                OrderID = orderID;
            }

            public int getQuesID() {
                return QuesID;
            }

            public void setQuesID(int quesID) {
                QuesID = quesID;
            }

            public int getPageIndex() {
                return PageIndex;
            }

            public void setPageIndex(int pageIndex) {
                PageIndex = pageIndex;
            }

            public int getChildClassTypeId() {
                return ChildClassTypeId;
            }

            public void setChildClassTypeId(int childClassTypeId) {
                ChildClassTypeId = childClassTypeId;
            }

            public int getClassId() {
                return ClassId;
            }

            public void setClassId(int classId) {
                ClassId = classId;
            }

            public int getLessonID() {
                return LessonID;
            }

            public void setLessonID(int lessonID) {
                LessonID = lessonID;
            }

            public int getQuesState() {
                return QuesState;
            }

            public void setQuesState(int quesState) {
                QuesState = quesState;
            }

            public int getNoteState() {
                return NoteState;
            }

            public void setNoteState(int noteState) {
                NoteState = noteState;
            }

            public int getInformationState() {
                return InformationState;
            }

            public void setInformationState(int informationState) {
                InformationState = informationState;
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

            public int getInform_Type() {
                return Inform_Type;
            }

            public void setInform_Type(int inform_Type) {
                Inform_Type = inform_Type;
            }

            public int getInform_IsRead() {
                return Inform_IsRead;
            }

            public void setInform_IsRead(int inform_IsRead) {
                Inform_IsRead = inform_IsRead;
            }

            public String getInform_AddTime() {
                return Inform_AddTime;
            }

            public void setInform_AddTime(String inform_AddTime) {
                Inform_AddTime = inform_AddTime;
            }
        }

        public int getListCount() {
            return ListCount;
        }

        public void setListCount(int listCount) {
            ListCount = listCount;
        }

        public int getTotalPageIndex() {
            return TotalPageIndex;
        }

        public void setTotalPageIndex(int totalPageIndex) {
            TotalPageIndex = totalPageIndex;
        }

        public ArrayList<GetNotifybyIsRead.Body.UserInformList> getUserInformList() {
            return UserInformList;
        }

        public void setUserInformList(ArrayList<GetNotifybyIsRead.Body.UserInformList> userInformList) {
            UserInformList = userInformList;
        }

        public int getTotalCount() {
            return TotalCount;
        }

        public void setTotalCount(int totalCount) {
            TotalCount = totalCount;
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

    public GetNotifybyIsRead.Body getBody() {
        return Body;
    }

    public void setBody(GetNotifybyIsRead.Body body) {
        Body = body;
    }
}
