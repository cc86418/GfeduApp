package cn.jun.bean;


public class StageListDB {
    private String stageid;
    private String stagename;
    private String classid;
    private int stagecount;

    public String getStageid() {
        return stageid;
    }

    public void setStageid(String stageid) {
        this.stageid = stageid;
    }

    public String getStagename() {
        return stagename;
    }

    public void setStagename(String stagename) {
        this.stagename = stagename;
    }

    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public int getStagecount() {
        return stagecount;
    }

    public void setStagecount(int stagecount) {
        this.stagecount = stagecount;
    }


    public StageListDB(String stageid, String stagename, String classid, int stagecount) {
        this.stageid = stageid;
        this.stagename = stagename;
        this.classid = classid;
        this.stagecount = stagecount;

    }

    public StageListDB() {


    }
}
