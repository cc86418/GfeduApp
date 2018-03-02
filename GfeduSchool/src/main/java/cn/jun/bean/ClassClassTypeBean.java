package cn.jun.bean;


import java.util.ArrayList;

public class ClassClassTypeBean {
    private int Code;
    private String Message;
    private Body Body;

    public class Body {
        private ArrayList<ClassTypeList> ClassTypeList;//班级包含班型
        private ArrayList<String> ExameDateList;//考试日期
        private ArrayList<String> ClassTypeTypeList;//班型类型
        private ArrayList<String> ShoolPlaceList;//考试地点


        public class ClassTypeList {
            private int ClassType_PKID;//班型ID
            private String ClassType_Name;//班级名称
            private int ClassType_Type;//1：面授 2：在线 4：直播
            private String ClassType_TypeName;//类型名称
            private String ClassType_Price;//班型原价
            private String ClassType_SalePrice;//班型售价
            private String ClassType_ExamDate;//考试日期
            private String ClassType_ExamDateName;//考试日期格式化
            private String ClassType_BeginDate;//开课日期
            private String ClassType_EndDate;//结束日期
            private String ClassType_Date;//课程时间
            private String ClassType_PlaceName;//考试地点
            private int ClassType_StudyTime;//学时
            private int ClassType_Mode;//["1":"固定时间","2":"售后即开"]
            private int ClassType_StudyDay;//学习天数
            private String ExpireDate;

            public String getExpireDate() {
                return ExpireDate;
            }

            public void setExpireDate(String expireDate) {
                ExpireDate = expireDate;
            }

            public int getClassType_PKID() {
                return ClassType_PKID;
            }

            public void setClassType_PKID(int classType_PKID) {
                ClassType_PKID = classType_PKID;
            }

            public String getClassType_Name() {
                return ClassType_Name;
            }

            public void setClassType_Name(String classType_Name) {
                ClassType_Name = classType_Name;
            }

            public int getClassType_Type() {
                return ClassType_Type;
            }

            public void setClassType_Type(int classType_Type) {
                ClassType_Type = classType_Type;
            }

            public String getClassType_TypeName() {
                return ClassType_TypeName;
            }

            public void setClassType_TypeName(String classType_TypeName) {
                ClassType_TypeName = classType_TypeName;
            }

            public String getClassType_Price() {
                return ClassType_Price;
            }

            public void setClassType_Price(String classType_Price) {
                ClassType_Price = classType_Price;
            }

            public String getClassType_SalePrice() {
                return ClassType_SalePrice;
            }

            public void setClassType_SalePrice(String classType_SalePrice) {
                ClassType_SalePrice = classType_SalePrice;
            }

            public String getClassType_ExamDate() {
                return ClassType_ExamDate;
            }

            public void setClassType_ExamDate(String classType_ExamDate) {
                ClassType_ExamDate = classType_ExamDate;
            }

            public String getClassType_ExamDateName() {
                return ClassType_ExamDateName;
            }

            public void setClassType_ExamDateName(String classType_ExamDateName) {
                ClassType_ExamDateName = classType_ExamDateName;
            }

            public String getClassType_BeginDate() {
                return ClassType_BeginDate;
            }

            public void setClassType_BeginDate(String classType_BeginDate) {
                ClassType_BeginDate = classType_BeginDate;
            }

            public String getClassType_EndDate() {
                return ClassType_EndDate;
            }

            public void setClassType_EndDate(String classType_EndDate) {
                ClassType_EndDate = classType_EndDate;
            }

            public String getClassType_Date() {
                return ClassType_Date;
            }

            public void setClassType_Date(String classType_Date) {
                ClassType_Date = classType_Date;
            }

            public String getClassType_PlaceName() {
                return ClassType_PlaceName;
            }

            public void setClassType_PlaceName(String classType_PlaceName) {
                ClassType_PlaceName = classType_PlaceName;
            }

            public int getClassType_StudyTime() {
                return ClassType_StudyTime;
            }

            public void setClassType_StudyTime(int classType_StudyTime) {
                ClassType_StudyTime = classType_StudyTime;
            }

            public int getClassType_Mode() {
                return ClassType_Mode;
            }

            public void setClassType_Mode(int classType_Mode) {
                ClassType_Mode = classType_Mode;
            }

            public int getClassType_StudyDay() {
                return ClassType_StudyDay;
            }

            public void setClassType_StudyDay(int classType_StudyDay) {
                ClassType_StudyDay = classType_StudyDay;
            }
        }

        public ArrayList<ClassClassTypeBean.Body.ClassTypeList> getClassTypeList() {
            return ClassTypeList;
        }

        public void setClassTypeList(ArrayList<ClassClassTypeBean.Body.ClassTypeList> classTypeList) {
            ClassTypeList = classTypeList;
        }

        public ArrayList<String> getExameDateList() {
            return ExameDateList;
        }

        public void setExameDateList(ArrayList<String> exameDateList) {
            ExameDateList = exameDateList;
        }

        public ArrayList<String> getClassTypeTypeList() {
            return ClassTypeTypeList;
        }

        public void setClassTypeTypeList(ArrayList<String> classTypeTypeList) {
            ClassTypeTypeList = classTypeTypeList;
        }

        public ArrayList<String> getShoolPlaceList() {
            return ShoolPlaceList;
        }

        public void setShoolPlaceList(ArrayList<String> shoolPlaceList) {
            ShoolPlaceList = shoolPlaceList;
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

    public ClassClassTypeBean.Body getBody() {
        return Body;
    }

    public void setBody(ClassClassTypeBean.Body body) {
        Body = body;
    }
}
