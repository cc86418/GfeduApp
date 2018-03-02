package jc.cici.android.atom.bean;

/**
 * 套餐班下班级类型实体
 * Created by atom on 2017/8/31.
 */

public class PackageClassType {


    private int ClassType_PKID;
    private String Name;
    private int StudyTime;
    private String StudyPlace;
    private String ExamDate;
    private String ExpireDate;
    private String BeginDate;
    private String EndDate;
    private int ClassType_Type;
    private String ClassType_TypeName;
    private int ClassType_StudyDay;
    private int ClassType_Mode;

    public int getClassType_PKID() {
        return ClassType_PKID;
    }

    public void setClassType_PKID(int classType_PKID) {
        ClassType_PKID = classType_PKID;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getStudyTime() {
        return StudyTime;
    }

    public void setStudyTime(int studyTime) {
        StudyTime = studyTime;
    }

    public String getStudyPlace() {
        return StudyPlace;
    }

    public void setStudyPlace(String studyPlace) {
        StudyPlace = studyPlace;
    }

    public String getExamDate() {
        return ExamDate;
    }

    public void setExamDate(String examDate) {
        ExamDate = examDate;
    }

    public String getExpireDate() {
        return ExpireDate;
    }

    public void setExpireDate(String expireDate) {
        ExpireDate = expireDate;
    }

    public String getBeginDate() {
        return BeginDate;
    }

    public void setBeginDate(String beginDate) {
        BeginDate = beginDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
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

    public int getClassType_StudyDay() {
        return ClassType_StudyDay;
    }

    public void setClassType_StudyDay(int classType_StudyDay) {
        ClassType_StudyDay = classType_StudyDay;
    }

    public int getClassType_Mode() {
        return ClassType_Mode;
    }

    public void setClassType_Mode(int classType_Mode) {
        ClassType_Mode = classType_Mode;
    }
}
