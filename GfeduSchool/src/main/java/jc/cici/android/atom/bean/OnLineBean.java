package jc.cici.android.atom.bean;

import com.bignerdranch.expandablerecyclerview.model.Parent;

import java.util.ArrayList;
import java.util.List;

/**
 * 在线选择课程实体类
 * Created by atom on 2017/6/8.
 */

public class OnLineBean implements Parent<DayLesson> {

    // 月份
    private String Month;
    // 天数
    private int DayCount;
    // 月份下课表数量
    private int ScheduleCount;
    private ArrayList<Day> Days;
    private ArrayList<DayLesson> mLessData;

    public String getMonth() {
        return Month;
    }

    public void setMonth(String month) {
        Month = month;
    }

    public int getDayCount() {
        return DayCount;
    }

    public void setDayCount(int dayCount) {
        DayCount = dayCount;
    }

    public int getScheduleCount() {
        return ScheduleCount;
    }

    public void setScheduleCount(int scheduleCount) {
        ScheduleCount = scheduleCount;
    }

    public ArrayList<Day> getDays() {
        return Days;
    }

    public void setDays(ArrayList<Day> days) {
        Days = days;
    }

    public ArrayList<DayLesson> getmLessData() {
        return mLessData;
    }

    public void setmLessData(ArrayList<DayLesson> mLessData) {
        this.mLessData = mLessData;
    }

    @Override
    public List<DayLesson> getChildList() {
        return mLessData;
    }

    @Override
    public boolean isInitiallyExpanded() {
        return false;
    }
}
