package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 试卷实体
 * Created by atom on 2018/1/17.
 */

public class TestBean {

    private int PaperId;
    // 试卷名称
    private String TpaperName;
    private int LastDoNo;
    private int LastDoTime;
    private int PaperQuesCount;
    private ArrayList<QuestionTest> PaperQuesCardList;
    private int TpaperTime;

    public int getTpaperTime() {
        return TpaperTime;
    }

    public void setTpaperTime(int tpaperTime) {
        TpaperTime = tpaperTime;
    }

    public String getTpaperName() {
        return TpaperName;
    }

    public void setTpaperName(String tpaperName) {
        TpaperName = tpaperName;
    }

    public int getLastDoNo() {
        return LastDoNo;
    }

    public void setLastDoNo(int lastDoNo) {
        LastDoNo = lastDoNo;
    }

    public int getLastDoTime() {
        return LastDoTime;
    }

    public void setLastDoTime(int lastDoTime) {
        LastDoTime = lastDoTime;
    }

    public int getPaperId() {
        return PaperId;
    }

    public void setPaperId(int paperId) {
        PaperId = paperId;
    }

    public int getPaperQuesCount() {
        return PaperQuesCount;
    }

    public void setPaperQuesCount(int paperQuesCount) {
        PaperQuesCount = paperQuesCount;
    }

    public ArrayList<QuestionTest> getPaperQuesCardList() {
        return PaperQuesCardList;
    }

    public void setPaperQuesCardList(ArrayList<QuestionTest> paperQuesCardList) {
        PaperQuesCardList = paperQuesCardList;
    }

    public class QuestionTest{

        private String QuesNo;
        private int QuesId;
        private String QuesStatus;
        private String QuesType;
        private int QuesOptionCount;
        private String QuesUserAnswer;
        private String QuesUrl;
        private int QuesUserAnswerImgsCount;
        private ArrayList<String> QuesUserAnswerImgs;
        // 关联答疑数量
        private int QuesProblemCount;
        // 关联笔记数量
        private int QuesNoteCount;
        // 是否收藏:1表示已收藏 0:表示未收藏
        private int HasCollect;

        public String getQuesNo() {
            return QuesNo;
        }

        public void setQuesNo(String quesNo) {
            QuesNo = quesNo;
        }

        public int getQuesId() {
            return QuesId;
        }

        public void setQuesId(int quesId) {
            QuesId = quesId;
        }

        public String getQuesStatus() {
            return QuesStatus;
        }

        public void setQuesStatus(String quesStatus) {
            QuesStatus = quesStatus;
        }

        public String getQuesType() {
            return QuesType;
        }

        public void setQuesType(String quesType) {
            QuesType = quesType;
        }

        public int getQuesOptionCount() {
            return QuesOptionCount;
        }

        public void setQuesOptionCount(int quesOptionCount) {
            QuesOptionCount = quesOptionCount;
        }

        public String getQuesUserAnswer() {
            return QuesUserAnswer;
        }

        public void setQuesUserAnswer(String quesUserAnswer) {
            QuesUserAnswer = quesUserAnswer;
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

        public int getHasCollect() {
            return HasCollect;
        }

        public void setHasCollect(int hasCollect) {
            HasCollect = hasCollect;
        }
    }
}
