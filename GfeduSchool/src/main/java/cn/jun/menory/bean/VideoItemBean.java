package cn.jun.menory.bean;


import com.easefun.polyvsdk.Video;

import java.io.Serializable;

public class VideoItemBean implements Serializable {
    /***
     * 当前使用字段
     */
    public int learnStatus; // 学习状态：0 未学习，1 学习中，2 已学习
    public boolean checked = false; // 是否被选中


    public final Video.HlsSpeedType hlsSpeedType/* = Video.HlsSpeedType.SPEED_1X */;

    public VideoItemBean() {
        hlsSpeedType = Video.HlsSpeedType.values()[0];
        speed = hlsSpeedType.getName();
    }

    public static final int STATUS_DOWNLOADING = 0; // 下载中
    public static final int STATUS_FAILED = 1; // 下载失败
    public static final int STATUS_SUCCESS = 2; // 下载成功
    public static final int STATUS_PAUSED = 3; // 暂停

    public String vid;
    //    public String name; // 课程名
//    public String desc; // 课程描述
//    public String tasktype;// 课程类型
//    public int planid; // 任务ID
    public long current; // 当前下载字节
    public long total; // 总字节
    public final int bitrate = 1; // 码率
    //    public final String speed;
    public int status;

    //改版
    public String lessonid; //课程id
    public String lessonname; //课程名字
    public String subjectid; //科目id
    public String subjectname; //科目名字
    public String classid;
    public String stageid;  //阶段id
    //    public int  lessoncount; //课程数量
    public final String speed;     //下载倍速
//    public String duration;
//    public long filesize; // 文件大小
//    public final int bitrate = 1; // 码率
//    public long current; // 当前下载字节
//    public long total; // 总字节
//    public int status;

    /**
     * 新增字段
     **/
    public String videoId;
    public String isStudy;
    public String keyValue;//学习进度
    // 服务状态(笔记,问题,资料是否解锁)
    public String StageProblemStatus;
    public String StageNoteStatus;
    public String StageInformationStatus;

}
