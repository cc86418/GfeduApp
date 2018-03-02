package cn.jun.menory.bean;


import java.io.Serializable;
import java.util.ArrayList;

public class VideoClassStageBean implements Serializable {
    private String ClassId;
    private String ClassName;
    private String StageId;
    private String StageName;
    private int StageCount;
    public ArrayList<VideoItemBean> itemBean;


    public ArrayList<VideoItemBean> getItemBean() {
        return itemBean;
    }

    public void setItemBean(ArrayList<VideoItemBean> itemBean) {
        this.itemBean = itemBean;
    }

    public String getClassId() {
        return ClassId;
    }

    public void setClassId(String classId) {
        ClassId = classId;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getStageId() {
        return StageId;
    }

    public void setStageId(String stageId) {
        StageId = stageId;
    }

    public String getStageName() {
        return StageName;
    }

    public void setStageName(String stageName) {
        StageName = stageName;
    }

    public int getStageCount() {
        return StageCount;
    }

    public void setStageCount(int stageCount) {
        StageCount = stageCount;
    }
}
