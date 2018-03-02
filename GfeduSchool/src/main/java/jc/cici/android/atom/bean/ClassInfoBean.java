package jc.cici.android.atom.bean;

import java.util.List;

/**
 * 班级信息
 * Created by atom on 2017/5/15.
 */

public class ClassInfoBean {

    private int Code;
    private String Message;
    private ClassInfo Body;

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

    public ClassInfo getBody() {
        return Body;
    }

    public void setBody(ClassInfo body) {
        Body = body;
    }

    public class ClassInfo {
        // 学习天数
        private int StudyDayCount;
        // 小贴士
        private String StudySlogan;
        // 是否完善资料(0:未完善,1:已完善)
        private int UserInfoIsComplete;
        // 学习信息
        private List<StudyBean> ClassList;
        // 需要评议的课表ID 等于0表示没有需要评议的课表
        private int AppraiseScheduleID;

        public int getUserInfoIsComplete() {
            return UserInfoIsComplete;
        }

        public void setUserInfoIsComplete(int userInfoIsComplete) {
            UserInfoIsComplete = userInfoIsComplete;
        }

        public int getStudyDayCount() {
            return StudyDayCount;
        }

        public void setStudyDayCount(int studyDayCount) {
            StudyDayCount = studyDayCount;
        }

        public String getStudySlogan() {
            return StudySlogan;
        }

        public void setStudySlogan(String studySlogan) {
            StudySlogan = studySlogan;
        }

        public List<StudyBean> getClassList() {
            return ClassList;
        }

        public void setClassList(List<StudyBean> classList) {
            ClassList = classList;
        }

        public int getAppraiseScheduleID() {
            return AppraiseScheduleID;
        }

        public void setAppraiseScheduleID(int appraiseScheduleID) {
            AppraiseScheduleID = appraiseScheduleID;
        }
    }

}
