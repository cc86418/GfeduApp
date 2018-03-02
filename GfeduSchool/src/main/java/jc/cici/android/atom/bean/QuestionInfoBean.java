package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 试题信息实体类
 * Created by atom on 2018/1/9.
 */

public class QuestionInfoBean {

    private int QuesNo;
    private int QuesId;
    private int QuesType;
    private int QuesStatus;
    private int QuesOptionCount;
    private String QuesContent;
    private ArrayList<String> QuesOption;

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
