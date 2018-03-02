package jc.cici.android.atom.bean;


import java.util.ArrayList;

/**
 * 每日一题or每月一测实体
 * Created by atom on 2018/1/16.
 */

public class EveryDayAndMothBean {

    private int QuesNo;
    private int QuesId;
    private int QuesType;
    private int QuesStatus;
    private int QuesOptionCount;
    private String QuesContent;
    private ArrayList<String> QuesOption;
    private String QuesParentContent;
    private int QuesProblemCount;
    private int QuesNoteCount;
    private String QuesUrl;
    private String QuesUserAnswer;
    private String QuesUserAnswerImgsCount;
    private String QuesUserAnswerImgs;
    private int HasCollect;
    private int EveryDayQuesId;

    public int getEveryDayQuesId() {
        return EveryDayQuesId;
    }

    public void setEveryDayQuesId(int everyDayQuesId) {
        EveryDayQuesId = everyDayQuesId;
    }

    public String getQuesParentContent() {
        return QuesParentContent;
    }

    public void setQuesParentContent(String quesParentContent) {
        QuesParentContent = quesParentContent;
    }

    public int getQuesProblemCount() {
        return QuesProblemCount;
    }

    public void setQuesProblemCount(int quesProblemCount) {
        QuesProblemCount = quesProblemCount;
    }

    public int getQuesNoteCount() {
        return QuesNoteCount;
    }

    public void setQuesNoteCount(int quesNoteCount) {
        QuesNoteCount = quesNoteCount;
    }

    public String getQuesUrl() {
        return QuesUrl;
    }

    public void setQuesUrl(String quesUrl) {
        QuesUrl = quesUrl;
    }

    public String getQuesUserAnswer() {
        return QuesUserAnswer;
    }

    public void setQuesUserAnswer(String quesUserAnswer) {
        QuesUserAnswer = quesUserAnswer;
    }

    public String getQuesUserAnswerImgsCount() {
        return QuesUserAnswerImgsCount;
    }

    public void setQuesUserAnswerImgsCount(String quesUserAnswerImgsCount) {
        QuesUserAnswerImgsCount = quesUserAnswerImgsCount;
    }

    public String getQuesUserAnswerImgs() {
        return QuesUserAnswerImgs;
    }

    public void setQuesUserAnswerImgs(String quesUserAnswerImgs) {
        QuesUserAnswerImgs = quesUserAnswerImgs;
    }

    public int getHasCollect() {
        return HasCollect;
    }

    public void setHasCollect(int hasCollect) {
        HasCollect = hasCollect;
    }

    public int getQuesNo() {
        return QuesNo;
    }

    public void setQuesNo(int quesNo) {
        QuesNo = quesNo;
    }

    public int getQuesId() {
        return QuesId;
    }

    public void setQuesId(int quesId) {
        QuesId = quesId;
    }

    public int getQuesType() {
        return QuesType;
    }

    public void setQuesType(int quesType) {
        QuesType = quesType;
    }

    public int getQuesStatus() {
        return QuesStatus;
    }

    public void setQuesStatus(int quesStatus) {
        QuesStatus = quesStatus;
    }

    public int getQuesOptionCount() {
        return QuesOptionCount;
    }

    public void setQuesOptionCount(int quesOptionCount) {
        QuesOptionCount = quesOptionCount;
    }

    public String getQuesContent() {
        return QuesContent;
    }

    public void setQuesContent(String quesContent) {
        QuesContent = quesContent;
    }

    public ArrayList<String> getQuesOption() {
        return QuesOption;
    }

    public void setQuesOption(ArrayList<String> quesOption) {
        QuesOption = quesOption;
    }
}
