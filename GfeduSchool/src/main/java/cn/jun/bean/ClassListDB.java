package cn.jun.bean;


public class ClassListDB {
    private String classid;
    private String classname;
    private int classcount;


    public String getClassid() {
        return classid;
    }

    public void setClassid(String classid) {
        this.classid = classid;
    }

    public String getClassname() {
        return classname;
    }

    public void setClassname(String classname) {
        this.classname = classname;
    }

    public int getClasscount() {
        return classcount;
    }

    public void setClasscount(int classcount) {
        this.classcount = classcount;
    }

    public ClassListDB(String classid, String classname, int classcount) {
        this.classid = classid;
        this.classname = classname;
        this.classcount = classcount;

    }
    public ClassListDB() {


    }
}
