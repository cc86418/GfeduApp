package jc.cici.android.atom.bean;

/**
 * 测试信息
 * Created by atom on 2017/5/19.
 */

public class TestPagerInfo {

    // 子试卷id
    private int PaperLessonChildId;
    // 试卷id
    private int PaperID;
    // 试卷名称
    private String PaperName;
    // 试卷总时长
    private String PaperTime;
    // 试题数量
    private int PaperQuesCount;
    // 试卷是否提交(0:未提交,1:已提交)
    private int PaperStatus;
    // 试卷学习状态(0:未学习,1:进行中,2已结束)
    private int PaperStudyState;
    private String PaperStudyKey;

    public int getPaperStatus() {
        return PaperStatus;
    }

    public void setPaperStatus(int paperStatus) {
        PaperStatus = paperStatus;
    }

    public String getPaperStudyKey() {
        return PaperStudyKey;
    }

    public void setPaperStudyKey(String paperStudyKey) {
        PaperStudyKey = paperStudyKey;
    }

    public int getPaperStudyState() {
        return PaperStudyState;
    }

    public void setPaperStudyState(int paperStudyState) {
        PaperStudyState = paperStudyState;
    }

    public int getPaperLessonChildId() {
        return PaperLessonChildId;
    }

    public void setPaperLessonChildId(int paperLessonChildId) {
        PaperLessonChildId = paperLessonChildId;
    }

    public int getPaperQuesCount() {
        return PaperQuesCount;
    }

    public void setPaperQuesCount(int paperQuesCount) {
        PaperQuesCount = paperQuesCount;
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

    public String getPaperTime() {
        return PaperTime;
    }

    public void setPaperTime(String paperTime) {
        PaperTime = paperTime;
    }
}
