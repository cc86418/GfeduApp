package jc.cici.android.atom.bean;

/**
 * 下一个任务
 * Created by atom on 2017/7/18.
 */

public class NextTaskBean {
    // 节课程id
    private int Level_PKID;
    // 节课程名称
    private String Level_ShowName;
    // 节类型(1:视频 2:试卷 3:资料 4:直播)
    private int KeyType;
    // 视频id
    private int VPKID;
    // 试卷id
    private int TestPaper_PKID;
    // 学习状态
    private String State;
    //  直播开始日期
    private String Date;
    // 直播开始时间
    private String BeginTime;
    //  直播结束时间
    private String EndTime;
    // 视频Vid
    private String VID;
    // 直播状态(0 未开始 1 直播中 2 已结束)
    private int LiveStatus;
    // 父类名称
    private String ParentLevel_ShowName;
    // 学习记录
    private String StudyKey;
    // 班型id
    private int ClassType;
    // 直播连接
    private String LiveUrl;
    // 点播连接
    private String SelectPlayUrl;
    // 父类id
    private String ParentLevelID;
    // 当前位置
    private int IsPosition;
    // 总数
    private int IsCount;

    public int getLevel_PKID() {
        return Level_PKID;
    }

    public void setLevel_PKID(int level_PKID) {
        Level_PKID = level_PKID;
    }

    public String getLevel_ShowName() {
        return Level_ShowName;
    }

    public void setLevel_ShowName(String level_ShowName) {
        Level_ShowName = level_ShowName;
    }

    public int getKeyType() {
        return KeyType;
    }

    public void setKeyType(int keyType) {
        KeyType = keyType;
    }

    public int getVPKID() {
        return VPKID;
    }

    public void setVPKID(int VPKID) {
        this.VPKID = VPKID;
    }

    public int getTestPaper_PKID() {
        return TestPaper_PKID;
    }

    public void setTestPaper_PKID(int testPaper_PKID) {
        TestPaper_PKID = testPaper_PKID;
    }

    public String getState() {
        return State;
    }

    public void setState(String state) {
        State = state;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getBeginTime() {
        return BeginTime;
    }

    public void setBeginTime(String beginTime) {
        BeginTime = beginTime;
    }

    public String getEndTime() {
        return EndTime;
    }

    public void setEndTime(String endTime) {
        EndTime = endTime;
    }

    public String getVID() {
        return VID;
    }

    public void setVID(String VID) {
        this.VID = VID;
    }

    public int getLiveStatus() {
        return LiveStatus;
    }

    public void setLiveStatus(int liveStatus) {
        LiveStatus = liveStatus;
    }

    public String getParentLevel_ShowName() {
        return ParentLevel_ShowName;
    }

    public void setParentLevel_ShowName(String parentLevel_ShowName) {
        ParentLevel_ShowName = parentLevel_ShowName;
    }

    public String getStudyKey() {
        return StudyKey;
    }

    public void setStudyKey(String studyKey) {
        StudyKey = studyKey;
    }

    public int getClassType() {
        return ClassType;
    }

    public void setClassType(int classType) {
        ClassType = classType;
    }

    public String getLiveUrl() {
        return LiveUrl;
    }

    public void setLiveUrl(String liveUrl) {
        LiveUrl = liveUrl;
    }

    public String getSelectPlayUrl() {
        return SelectPlayUrl;
    }

    public void setSelectPlayUrl(String selectPlayUrl) {
        SelectPlayUrl = selectPlayUrl;
    }

    public String getParentLevelID() {
        return ParentLevelID;
    }

    public void setParentLevelID(String parentLevelID) {
        ParentLevelID = parentLevelID;
    }

    public int getIsPosition() {
        return IsPosition;
    }

    public void setIsPosition(int isPosition) {
        IsPosition = isPosition;
    }

    public int getIsCount() {
        return IsCount;
    }

    public void setIsCount(int isCount) {
        IsCount = isCount;
    }
}
