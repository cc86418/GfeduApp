package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 每日一题解析实体
 * Created by atom on 2018/1/16.
 */

public class QuesAnalysisBean {

    private int QuesNo;
    private int QuesId;
    private int QuesType;
    private int QuesOptionCount;
    private String QuesContent;
    private ArrayList<String> QuesOption;
    private String QuesAnswer;
    private String QuesUserAnswer;
    private String UserAnswerStatus;
    private String QuesRightPercent;
    private ArrayList<QuesAnalysis> QuesAnalysisList;
    private int QuesStatus;
    private String QuesUrl;
    private int QuesUserAnswerImgsCount;
    private ArrayList<String> QuesUserAnswerImgs;
    private int HasCollect;
    private int QuesProblemCount;
    private int QuesNoteCount;

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

    public class QuesAnalysis{

        private int AnalysisID;
        private int AnalysisType;
        private String AnalysisContent;

        public int getAnalysisID() {
            return AnalysisID;
        }

        public void setAnalysisID(int analysisID) {
            AnalysisID = analysisID;
        }

        public int getAnalysisType() {
            return AnalysisType;
        }

        public void setAnalysisType(int analysisType) {
            AnalysisType = analysisType;
        }

        public String getAnalysisContent() {
            return AnalysisContent;
        }

        public void setAnalysisContent(String analysisContent) {
            AnalysisContent = analysisContent;
        }
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

    public int getQuesStatus() {
        return QuesStatus;
    }

    public void setQuesStatus(int quesStatus) {
        QuesStatus = quesStatus;
    }

    public String getQuesUrl() {
        return QuesUrl;
    }

    public void setQuesUrl(String quesUrl) {
        QuesUrl = quesUrl;
    }

    public int getQuesUserAnswerImgsCount() {
        return QuesUserAnswerImgsCount;
    }

    public void setQuesUserAnswerImgsCount(int quesUserAnswerImgsCount) {
        QuesUserAnswerImgsCount = quesUserAnswerImgsCount;
    }

    public ArrayList<String> getQuesUserAnswerImgs() {
        return QuesUserAnswerImgs;
    }

    public void setQuesUserAnswerImgs(ArrayList<String> quesUserAnswerImgs) {
        QuesUserAnswerImgs = quesUserAnswerImgs;
    }

    public int getHasCollect() {
        return HasCollect;
    }

    public void setHasCollect(int hasCollect) {
        HasCollect = hasCollect;
    }

    public String getQuesAnswer() {
        return QuesAnswer;
    }

    public void setQuesAnswer(String quesAnswer) {
        QuesAnswer = quesAnswer;
    }

    public String getQuesUserAnswer() {
        return QuesUserAnswer;
    }

    public void setQuesUserAnswer(String quesUserAnswer) {
        QuesUserAnswer = quesUserAnswer;
    }

    public String getUserAnswerStatus() {
        return UserAnswerStatus;
    }

    public void setUserAnswerStatus(String userAnswerStatus) {
        UserAnswerStatus = userAnswerStatus;
    }

    public String getQuesRightPercent() {
        return QuesRightPercent;
    }

    public void setQuesRightPercent(String quesRightPercent) {
        QuesRightPercent = quesRightPercent;
    }

    public ArrayList<QuesAnalysis> getQuesAnalysisList() {
        return QuesAnalysisList;
    }

    public void setQuesAnalysisList(ArrayList<QuesAnalysis> quesAnalysisList) {
        QuesAnalysisList = quesAnalysisList;
    }
}
