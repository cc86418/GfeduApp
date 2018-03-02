package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 智能组卷实体
 * Created by atom on 2017/12/26.
 */

public class ExamChoseBean {

    // 试卷id
    private int TpaperId;
    // 试卷名称
    private String TpaperName;
    // 知识点列表
    private ArrayList<Knowledge> Knowledges;
    // 阶段列表
    private ArrayList<Stage> Stages;

    public int getTpaperId() {
        return TpaperId;
    }

    public void setTpaperId(int tpaperId) {
        TpaperId = tpaperId;
    }

    public String getTpaperName() {
        return TpaperName;
    }

    public void setTpaperName(String tpaperName) {
        TpaperName = tpaperName;
    }

    public ArrayList<Knowledge> getKnowledges() {
        return Knowledges;
    }

    public void setKnowledges(ArrayList<Knowledge> knowledges) {
        Knowledges = knowledges;
    }

    public ArrayList<Stage> getStages() {
        return Stages;
    }

    public void setStages(ArrayList<Stage> stages) {
        Stages = stages;
    }

    public class Knowledge{

        // 知识点id
       private int KnowledgeId;
        // 知识点名称
        private String KnowledgeName;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public int getKnowledgeId() {
            return KnowledgeId;
        }

        public void setKnowledgeId(int knowledgeId) {
            KnowledgeId = knowledgeId;
        }

        public String getKnowledgeName() {
            return KnowledgeName;
        }

        public void setKnowledgeName(String knowledgeName) {
            KnowledgeName = knowledgeName;
        }
    }

    public class Stage{

        // 阶段id
        private int StageID;
        // 阶段名称
        private String StageName;
        private boolean isSelected;

        public int getStageID() {
            return StageID;
        }

        public void setStageID(int stageID) {
            StageID = stageID;
        }

        public String getStageName() {
            return StageName;
        }

        public void setStageName(String stageName) {
            StageName = stageName;
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }
    }
}
