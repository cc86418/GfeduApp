package cn.jun.bean;


import java.util.ArrayList;

public class GetClassLiveOutBean {
    private int Code;
    private String Message;
    private Body Body;

    public class Body {
        private int ScheduleCount;
        private ArrayList<Schedules> Schedules;

        public class Schedules {
            private int CS_PKID;//课表ID
            private String CS_Date;//直播日期 yyyy-MM-dd
            private String CS_DateShort;//日期缩写 MM-dd
            private int CS_DateType;//（1：上午  2：下午   3：晚上）
            private String CS_StartTime;//直播开始时间 HH:mm
            private String CS_EndTime;//直播结束时间：HH:mm
            private int CS_LearnTime;//学时
            private String CS_Content;//上课内容
            private int CS_IsReserve;//是否可预约
            private int CS_IsPlayback;//是否支持回放
            private String Class_Feature;//课程特色
            private int Lecturer_Sex;//授课老师性别
            private String Lecturer_Named;//授课老师名称
            private String Lecturer_Img;//头像名称
            private int HasBook;//是否预约过 1：是 0：否
            private int HasBuy;//是否购买过 1：是 0：否
            private int HasBookOrBuy;//是否预约或购买 1：是 0：否
            private int IsLiveBegin;//直播是否开始
            private int IsLiveEnd;//直播是否结束
            private String Lecturer_Title;//	讲师头衔
            private String Lecturer_Intro;//	讲师简介
            private String ProjectName;//项目名称
            private int BookCount;//	预约人数
            private int HasWatch;//是否观看 1：是 0：否

            public int getCS_PKID() {
                return CS_PKID;
            }

            public void setCS_PKID(int CS_PKID) {
                this.CS_PKID = CS_PKID;
            }

            public String getCS_Date() {
                return CS_Date;
            }

            public void setCS_Date(String CS_Date) {
                this.CS_Date = CS_Date;
            }

            public String getCS_DateShort() {
                return CS_DateShort;
            }

            public void setCS_DateShort(String CS_DateShort) {
                this.CS_DateShort = CS_DateShort;
            }

            public int getCS_DateType() {
                return CS_DateType;
            }

            public void setCS_DateType(int CS_DateType) {
                this.CS_DateType = CS_DateType;
            }

            public String getCS_StartTime() {
                return CS_StartTime;
            }

            public void setCS_StartTime(String CS_StartTime) {
                this.CS_StartTime = CS_StartTime;
            }

            public String getCS_EndTime() {
                return CS_EndTime;
            }

            public void setCS_EndTime(String CS_EndTime) {
                this.CS_EndTime = CS_EndTime;
            }

            public int getCS_LearnTime() {
                return CS_LearnTime;
            }

            public void setCS_LearnTime(int CS_LearnTime) {
                this.CS_LearnTime = CS_LearnTime;
            }

            public String getCS_Content() {
                return CS_Content;
            }

            public void setCS_Content(String CS_Content) {
                this.CS_Content = CS_Content;
            }

            public int getCS_IsReserve() {
                return CS_IsReserve;
            }

            public void setCS_IsReserve(int CS_IsReserve) {
                this.CS_IsReserve = CS_IsReserve;
            }

            public int getCS_IsPlayback() {
                return CS_IsPlayback;
            }

            public void setCS_IsPlayback(int CS_IsPlayback) {
                this.CS_IsPlayback = CS_IsPlayback;
            }

            public String getClass_Feature() {
                return Class_Feature;
            }

            public void setClass_Feature(String class_Feature) {
                Class_Feature = class_Feature;
            }

            public int getLecturer_Sex() {
                return Lecturer_Sex;
            }

            public void setLecturer_Sex(int lecturer_Sex) {
                Lecturer_Sex = lecturer_Sex;
            }

            public String getLecturer_Named() {
                return Lecturer_Named;
            }

            public void setLecturer_Named(String lecturer_Named) {
                Lecturer_Named = lecturer_Named;
            }

            public String getLecturer_Img() {
                return Lecturer_Img;
            }

            public void setLecturer_Img(String lecturer_Img) {
                Lecturer_Img = lecturer_Img;
            }

            public String getProjectName() {
                return ProjectName;
            }

            public void setProjectName(String projectName) {
                ProjectName = projectName;
            }

            public int getBookCount() {
                return BookCount;
            }

            public void setBookCount(int bookCount) {
                BookCount = bookCount;
            }

            public int getHasWatch() {
                return HasWatch;
            }

            public void setHasWatch(int hasWatch) {
                HasWatch = hasWatch;
            }

            public int getHasBook() {
                return HasBook;
            }

            public void setHasBook(int hasBook) {
                HasBook = hasBook;
            }

            public int getHasBuy() {
                return HasBuy;
            }

            public void setHasBuy(int hasBuy) {
                HasBuy = hasBuy;
            }

            public int getHasBookOrBuy() {
                return HasBookOrBuy;
            }

            public void setHasBookOrBuy(int hasBookOrBuy) {
                HasBookOrBuy = hasBookOrBuy;
            }

            public int getIsLiveBegin() {
                return IsLiveBegin;
            }

            public void setIsLiveBegin(int isLiveBegin) {
                IsLiveBegin = isLiveBegin;
            }

            public int getIsLiveEnd() {
                return IsLiveEnd;
            }

            public void setIsLiveEnd(int isLiveEnd) {
                IsLiveEnd = isLiveEnd;
            }

            public String getLecturer_Title() {
                return Lecturer_Title;
            }

            public void setLecturer_Title(String lecturer_Title) {
                Lecturer_Title = lecturer_Title;
            }

            public String getLecturer_Intro() {
                return Lecturer_Intro;
            }

            public void setLecturer_Intro(String lecturer_Intro) {
                Lecturer_Intro = lecturer_Intro;
            }
        }

        public int getScheduleCount() {
            return ScheduleCount;
        }

        public void setScheduleCount(int scheduleCount) {
            ScheduleCount = scheduleCount;
        }

        public ArrayList<GetClassLiveOutBean.Body.Schedules> getSchedules() {
            return Schedules;
        }

        public void setSchedules(ArrayList<GetClassLiveOutBean.Body.Schedules> schedules) {
            Schedules = schedules;
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

    public GetClassLiveOutBean.Body getBody() {
        return Body;
    }

    public void setBody(GetClassLiveOutBean.Body body) {
        Body = body;
    }
}
