package jc.cici.android.atom.bean;

/**
 * 单版型内容实体
 * Created by atom on 2017/9/7.
 */

public class SingleClass {
    // 班型ID
    private int ClassType_PKID;
    // 班级ID
    private int Class_PKID;
    // 班级图片
    private String Class_MobileImage;
    // 班级名称
    private String ClassType_Name;
    // 班级类型 1：面授 2：在线 4：直播
    private int ClassType_Type;
    // 类型名称
    private String ClassType_TypeName;
    // 班型原价
    private String ClassType_Price;
    // 班型售价
    private String ClassType_SalePrice;
    // 考试日期
    private String ClassType_ExamDate;
    // 考试日期格式化
    private String ClassType_ExamDateName;
    // 开课日期
    private String ClassType_BeginDate;
    // 结束日期
    private String ClassType_EndDate;
    // 课程时间
    private String ClassType_Date;
    // 考试地点
    private String ClassType_PlaceName;
    // 学时
    private int ClassType_StudyTime;
    //  在线情况下出现"1":"固定时间","2":"售后即开"
    private int ClassType_Mode;
    // 学习天数
    private int ClassType_StudyDay;

    public int getClassType_PKID() {
        return ClassType_PKID;
    }

    public void setClassType_PKID(int classType_PKID) {
        ClassType_PKID = classType_PKID;
    }

    public int getClass_PKID() {
        return Class_PKID;
    }

    public void setClass_PKID(int class_PKID) {
        Class_PKID = class_PKID;
    }

    public String getClass_MobileImage() {
        return Class_MobileImage;
    }

    public void setClass_MobileImage(String class_MobileImage) {
        Class_MobileImage = class_MobileImage;
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
