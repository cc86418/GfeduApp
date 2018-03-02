package cn.jun.bean;


import java.util.ArrayList;

public class ClassScheduleBean {
    private int ListCount;
    private ArrayList<ClassScheduleList> ClassScheduleList;

    public class ClassScheduleList {
        private int ClassScheduleID;//课表ID
        private String ChildClassTypeName;//子班型名
        private String CoursesContent;//上课内容
        private String CoursesTime;//上课时间
        private String LecturerName;//讲师名
        private String LecturerHead;//讲师头像

        public int getClassScheduleID() {
            return ClassScheduleID;
        }

        public void setClassScheduleID(int classScheduleID) {
            ClassScheduleID = classScheduleID;
        }

        public String getChildClassTypeName() {
            return ChildClassTypeName;
        }

        public void setChildClassTypeName(String childClassTypeName) {
            ChildClassTypeName = childClassTypeName;
        }

        public String getCoursesContent() {
            return CoursesContent;
        }

        public void setCoursesContent(String coursesContent) {
            CoursesContent = coursesContent;
        }

        public String getCoursesTime() {
            return CoursesTime;
        }

        public void setCoursesTime(String coursesTime) {
            CoursesTime = coursesTime;
        }

        public String getLecturerName() {
            return LecturerName;
        }

        public void setLecturerName(String lecturerName) {
            LecturerName = lecturerName;
        }

        public String getLecturerHead() {
            return LecturerHead;
        }

        public void setLecturerHead(String lecturerHead) {
            LecturerHead = lecturerHead;
        }
    }

    public int getListCount() {
        return ListCount;
    }

    public void setListCount(int listCount) {
        ListCount = listCount;
    }

    public ArrayList<ClassScheduleBean.ClassScheduleList> getClassScheduleList() {
        return ClassScheduleList;
    }

    public void setClassScheduleList(ArrayList<ClassScheduleBean.ClassScheduleList> classScheduleList) {
        ClassScheduleList = classScheduleList;
    }


//    private int Code;
//    private String Message;
//    private Body Body;
//
//    public class Body {
//        private int ListCount;
//        private ArrayList<ClassScheduleList> ClassScheduleList;
//
//        public class ClassScheduleList {
//            private int ClassScheduleID;//课表ID
//            private String ChildClassTypeName;//子班型名
//            private String CoursesContent;//上课内容
//            private String CoursesTime;//上课时间
//            private String LecturerName;//讲师名
//            private String LecturerHead;//讲师头像
//
//            public int getClassScheduleID() {
//                return ClassScheduleID;
//            }
//
//            public void setClassScheduleID(int classScheduleID) {
//                ClassScheduleID = classScheduleID;
//            }
//
//            public String getChildClassTypeName() {
//                return ChildClassTypeName;
//            }
//
//            public void setChildClassTypeName(String childClassTypeName) {
//                ChildClassTypeName = childClassTypeName;
//            }
//
//            public String getCoursesContent() {
//                return CoursesContent;
//            }
//
//            public void setCoursesContent(String coursesContent) {
//                CoursesContent = coursesContent;
//            }
//
//            public String getCoursesTime() {
//                return CoursesTime;
//            }
//
//            public void setCoursesTime(String coursesTime) {
//                CoursesTime = coursesTime;
//            }
//
//            public String getLecturerName() {
//                return LecturerName;
//            }
//
//            public void setLecturerName(String lecturerName) {
//                LecturerName = lecturerName;
//            }
//
//            public String getLecturerHead() {
//                return LecturerHead;
//            }
//
//            public void setLecturerHead(String lecturerHead) {
//                LecturerHead = lecturerHead;
//            }
//        }
//
//        public int getListCount() {
//            return ListCount;
//        }
//
//        public void setListCount(int listCount) {
//            ListCount = listCount;
//        }
//
//        public ArrayList<ClassScheduleBean.Body.ClassScheduleList> getClassScheduleList() {
//            return ClassScheduleList;
//        }
//
//        public void setClassScheduleList(ArrayList<ClassScheduleBean.Body.ClassScheduleList> classScheduleList) {
//            ClassScheduleList = classScheduleList;
//        }
//    }
//
//    public int getCode() {
//        return Code;
//    }
//
//    public void setCode(int code) {
//        Code = code;
//    }
//
//    public String getMessage() {
//        return Message;
//    }
//
//    public void setMessage(String message) {
//        Message = message;
//    }
//
//    public ClassScheduleBean.Body getBody() {
//        return Body;
//    }
//
//    public void setBody(ClassScheduleBean.Body body) {
//        Body = body;
//    }

}
