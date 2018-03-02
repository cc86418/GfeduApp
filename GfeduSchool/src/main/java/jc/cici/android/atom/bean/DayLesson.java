package jc.cici.android.atom.bean;

/**
 * 班级下对应课程
 * Created by atom on 2017/10/26.
 */

public class DayLesson {

    // 是否属于当前7天内可点击操作
    private int IsValid;
    // 定义标题日期
    private String Date;
    // 课程id
    private int LessonId;
    // 日期
    private String LessonDate;
    // 课程名称
    private String LessonName;
    // 课程类型(1:面授 2:直播 3:测试)
    private int LessonType;
    // 课程时段(1:上午 2:下午 3:晚上)
    private int LessonDateType;
    // 课程开始时间
    private String LessonStartTime;
    // 课程结束时间
    private String LessonEndTime;
    // 讲师姓名
    private String TeacherName;
    // 讲师图像
    private String TeacherImg;
    // 上课地址
    private String LessonPlace;

    public int getIsValid() {
        return IsValid;
    }

    public void setIsValid(int isValid) {
        IsValid = isValid;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getLessonId() {
        return LessonId;
    }

    public void setLessonId(int lessonId) {
        LessonId = lessonId;
    }

    public String getLessonDate() {
        return LessonDate;
    }

    public void setLessonDate(String lessonDate) {
        LessonDate = lessonDate;
    }

    public String getLessonName() {
        return LessonName;
    }

    public void setLessonName(String lessonName) {
        LessonName = lessonName;
    }

    public int getLessonType() {
        return LessonType;
    }

    public void setLessonType(int lessonType) {
        LessonType = lessonType;
    }

    public int getLessonDateType() {
        return LessonDateType;
    }

    public void setLessonDateType(int lessonDateType) {
        LessonDateType = lessonDateType;
    }

    public String getLessonStartTime() {
        return LessonStartTime;
    }

    public void setLessonStartTime(String lessonStartTime) {
        LessonStartTime = lessonStartTime;
    }

    public String getLessonEndTime() {
        return LessonEndTime;
    }

    public void setLessonEndTime(String lessonEndTime) {
        LessonEndTime = lessonEndTime;
    }

    public String getTeacherName() {
        return TeacherName;
    }

    public void setTeacherName(String teacherName) {
        TeacherName = teacherName;
    }

    public String getTeacherImg() {
        return TeacherImg;
    }

    public void setTeacherImg(String teacherImg) {
        TeacherImg = teacherImg;
    }

    public String getLessonPlace() {
        return LessonPlace;
    }

    public void setLessonPlace(String lessonPlace) {
        LessonPlace = lessonPlace;
    }
}
