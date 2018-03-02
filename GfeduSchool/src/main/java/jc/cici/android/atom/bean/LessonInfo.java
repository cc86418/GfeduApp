package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 课程信息
 * Created by atom on 2017/5/19.
 */

public class LessonInfo {

    // 章节课程名称
    private String LessonName;
    // 课程类型
    private int LessonType;
    // 课程日期
    private String LessonDate;
    // 课程时段(上午,中午,下午)
    private int LessonDateType;
    // 课程开始时间
    private String LessonStartTime;
    // 课程结束时间
    private String LessonEndTime;
    // 授课老师姓名
    private String TeacherName;
    // 授课老师图片
    private String TeacherImg;
    // 授课地点
    private String LessonPlace;
    // 出勤率
    private int AttendanceStatus;
    // 是否支持回放
    private int CS_IsPlayback;
    // 直播状态
    private int LiveStatus;
    // 直播url
    private String LiveUrl;
    // 回放url
    private String LiveReplayUrl;
    // 试卷id
    private int TestPaperId;
    // 回看视频数量
    private int ReplayVideoListCount;
    // 回看列表
    private ArrayList<ReplayInfo> ReplayVideoList;
    // 测试数量
    private int TestPaperListCount;
    // 测试列表
    private ArrayList<TestPagerInfo> TestPaperList;
    // 下载数量
    private int DownloadListCount;
    // 下载列表
    private ArrayList<DownloadInfo> DownloadList;

    public int getCS_IsPlayback() {
        return CS_IsPlayback;
    }

    public void setCS_IsPlayback(int CS_IsPlayback) {
        this.CS_IsPlayback = CS_IsPlayback;
    }

    public int getLiveStatus() {
        return LiveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        LiveStatus = liveStatus;
    }

    public String getLiveUrl() {
        return LiveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        LiveUrl = liveUrl;
    }

    public String getLiveReplayUrl() {
        return LiveReplayUrl;
    }

    public void setLiveReplayUrl(String liveReplayUrl) {
        LiveReplayUrl = liveReplayUrl;
    }

    public int getTestPaperId() {
        return TestPaperId;
    }

    public void setTestPaperId(int testPaperId) {
        TestPaperId = testPaperId;
    }

    public int getReplayVideoListCount() {
        return ReplayVideoListCount;
    }

    public void setReplayVideoListCount(int replayVideoListCount) {
        ReplayVideoListCount = replayVideoListCount;
    }

    public int getTestPaperListCount() {
        return TestPaperListCount;
    }

    public void setTestPaperListCount(int testPaperListCount) {
        TestPaperListCount = testPaperListCount;
    }

    public int getDownloadListCount() {
        return DownloadListCount;
    }

    public void setDownloadListCount(int downloadListCount) {
        DownloadListCount = downloadListCount;
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

    public String getLessonDate() {
        return LessonDate;
    }

    public void setLessonDate(String lessonDate) {
        LessonDate = lessonDate;
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

    public int getAttendanceStatus() {
        return AttendanceStatus;
    }

    public void setAttendanceStatus(int attendanceStatus) {
        AttendanceStatus = attendanceStatus;
    }

    public ArrayList<ReplayInfo> getReplayVideoList() {
        return ReplayVideoList;
    }

    public void setReplayVideoList(ArrayList<ReplayInfo> replayVideoList) {
        ReplayVideoList = replayVideoList;
    }

    public ArrayList<TestPagerInfo> getTestPaperList() {
        return TestPaperList;
    }

    public void setTestPaperList(ArrayList<TestPagerInfo> testPaperList) {
        TestPaperList = testPaperList;
    }

    public ArrayList<DownloadInfo> getDownloadList() {
        return DownloadList;
    }

    public void setDownloadList(ArrayList<DownloadInfo> downloadList) {
        DownloadList = downloadList;
    }
}
