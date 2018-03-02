package jc.cici.android.atom.bean;

/**
 * 视频回看信息
 * Created by atom on 2017/5/19.
 */

public class ReplayInfo {

    // 子视频id
    private int VideoLessonChildId;
    // 视频id
    private int VideoID;
    // 视频播放id
    private String VideoVID;
    // 视频名称
    private String VideoName;
    // 视频时长
    private String VideoTime;
    // 视频图片
    private String VideoImg;
    // 视频播放进度
    private String VideoStudyKey;
    // 视频学习状态(0:未学习,1:学习中,2:已结束)
    private int VideoStudyState;

    public int getVideoStudyState() {
        return VideoStudyState;
    }

    public void setVideoStudyState(int videoStudyState) {
        VideoStudyState = videoStudyState;
    }

    public String getVideoStudyKey() {
        return VideoStudyKey;
    }

    public void setVideoStudyKey(String videoStudyKey) {
        VideoStudyKey = videoStudyKey;
    }

    public int getVideoLessonChildId() {
        return VideoLessonChildId;
    }

    public void setVideoLessonChildId(int videoLessonChildId) {
        VideoLessonChildId = videoLessonChildId;
    }

    public int getVideoID() {
        return VideoID;
    }

    public void setVideoID(int videoID) {
        VideoID = videoID;
    }

    public String getVideoVID() {
        return VideoVID;
    }

    public void setVideoVID(String videoVID) {
        VideoVID = videoVID;
    }

    public String getVideoName() {
        return VideoName;
    }

    public void setVideoName(String videoName) {
        VideoName = videoName;
    }

    public String getVideoTime() {
        return VideoTime;
    }

    public void setVideoTime(String videoTime) {
        VideoTime = videoTime;
    }

    public String getVideoImg() {
        return VideoImg;
    }

    public void setVideoImg(String videoImg) {
        VideoImg = videoImg;
    }
}
