package jc.cici.android.atom.bean;

/**
 * 题库首页内容实体
 * Created by atom on 2017/12/25.
 */

public class ContentExamHomeBean {

    // 正确率
    private String RightRatio;
    // 正确个数
    private int RightCount;
    // 错误个数
    private int WrongCount;
    // 总个数
    private int TotalCount;
    // 组卷个数
    private int ZuJuanCount;
    // 章节练习正确率
    private String ZhangRatio;
    // 章节练习比
    private String ZhangPercent;
    // 每月一测最高分
    private int MonthlyScore;
    // 每月一测试卷id
    private int MonthlyTpaperId;
    // 每日一题问题id
    private int EveryDayQuesId;
    // 坚持做题时间
    private int EveryDayCount;
    // 每月一测做题状态
    private int MonthlyStatus;

    public int getMonthlyStatus() {
        return MonthlyStatus;
    }

    public void setMonthlyStatus(int monthlyStatus) {
        MonthlyStatus = monthlyStatus;
    }

    public String getRightRatio() {
        return RightRatio;
    }

    public void setRightRatio(String rightRatio) {
        RightRatio = rightRatio;
    }

    public int getRightCount() {
        return RightCount;
    }

    public void setRightCount(int rightCount) {
        RightCount = rightCount;
    }

    public int getWrongCount() {
        return WrongCount;
    }

    public void setWrongCount(int wrongCount) {
        WrongCount = wrongCount;
    }

    public int getTotalCount() {
        return TotalCount;
    }

    public void setTotalCount(int totalCount) {
        TotalCount = totalCount;
    }

    public int getZuJuanCount() {
        return ZuJuanCount;
    }

    public void setZuJuanCount(int zuJuanCount) {
        ZuJuanCount = zuJuanCount;
    }

    public String getZhangRatio() {
        return ZhangRatio;
    }

    public void setZhangRatio(String zhangRatio) {
        ZhangRatio = zhangRatio;
    }

    public String getZhangPercent() {
        return ZhangPercent;
    }

    public void setZhangPercent(String zhangPercent) {
        ZhangPercent = zhangPercent;
    }

    public int getMonthlyScore() {
        return MonthlyScore;
    }

    public void setMonthlyScore(int monthlyScore) {
        MonthlyScore = monthlyScore;
    }

    public int getMonthlyTpaperId() {
        return MonthlyTpaperId;
    }

    public void setMonthlyTpaperId(int monthlyTpaperId) {
        MonthlyTpaperId = monthlyTpaperId;
    }

    public int getEveryDayQuesId() {
        return EveryDayQuesId;
    }

    public void setEveryDayQuesId(int everyDayQuesId) {
        EveryDayQuesId = everyDayQuesId;
    }

    public int getEveryDayCount() {
        return EveryDayCount;
    }

    public void setEveryDayCount(int everyDayCount) {
        EveryDayCount = everyDayCount;
    }
}
