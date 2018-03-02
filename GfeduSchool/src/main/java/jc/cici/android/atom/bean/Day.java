package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 月份下对应天面授内容
 * Created by atom on 2017/10/26.
 */

public class Day {

    private String Date;
    private int LessonCount;
    private ArrayList<DayLesson> LessonData;

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public int getLessonCount() {
        return LessonCount;
    }

    public void setLessonCount(int lessonCount) {
        LessonCount = lessonCount;
    }

    public ArrayList<DayLesson> getLessonData() {
        return LessonData;
    }

    public void setLessonData(ArrayList<DayLesson> lessonData) {
        LessonData = lessonData;
    }

//    private int LessonId;
//    // 课程名称
//    private String LessonName;
//    // 课程类型(1:面授 2:直播 3:测试)
//    private int LessonType;
//    // 课程时段(1:上午 2:下午 3:晚上)
//    private int LessonDateType;
//    // 课程开始时间
//    private String LessonStartTime;
//    // 课程结束时间
//    private String LessonEndTime;
//
//    public int getLessonId() {
//        return LessonId;
//    }
//
//    public void setLessonId(int lessonId) {
//        LessonId = lessonId;
//    }
//
//    public String getLessonName() {
//        return LessonName;
//    }
//
//    public void setLessonName(String lessonName) {
//        LessonName = lessonName;
//    }
//
//    public int getLessonType() {
//        return LessonType;
//    }
//
//    public void setLessonType(int lessonType) {
//        LessonType = lessonType;
//    }
//
//    public int getLessonDateType() {
//        return LessonDateType;
//    }
//
//    public void setLessonDateType(int lessonDateType) {
//        LessonDateType = lessonDateType;
//    }
//
//    public String getLessonStartTime() {
//        return LessonStartTime;
//    }
//
//    public void setLessonStartTime(String lessonStartTime) {
//        LessonStartTime = lessonStartTime;
//    }
//
//    public String getLessonEndTime() {
//        return LessonEndTime;
//    }
//
//    public void setLessonEndTime(String lessonEndTime) {
//        LessonEndTime = lessonEndTime;
//    }
}
