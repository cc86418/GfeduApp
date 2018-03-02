package jc.cici.android.atom.bean;

/**
 * 课表课件内容实体
 * Created by atom on 2017/7/20.
 */

public class LessonChildInfo {

    // 课件id
    private int ServerId;
    // 课件类型(1:视频，2:试卷)
    private int ServerType;
    // 试卷id
    private int PaperID;
    // 试卷名称
    private String PaperName;
    // 视频id
    private int VideoID;
    // 视频播放id
    private String VideoVID;
    // 视频名称
    private String VideoName;

    public int getServerId() {
        return ServerId;
    }

    public void setServerId(int serverId) {
        ServerId = serverId;
    }

    public int getServerType() {
        return ServerType;
    }

    public void setServerType(int serverType) {
        ServerType = serverType;
    }

    public int getPaperID() {
        return PaperID;
    }

    public void setPaperID(int paperID) {
        PaperID = paperID;
    }

    public String getPaperName() {
        return PaperName;
    }

    public void setPaperName(String paperName) {
        PaperName = paperName;
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
}
