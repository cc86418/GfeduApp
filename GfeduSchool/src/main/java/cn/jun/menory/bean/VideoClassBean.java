package cn.jun.menory.bean;



public class VideoClassBean {
    public boolean checked = false; // 是否被选中
    public String classid;
    public String classname;
    public String classcount;

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

    public String getClasscount() {
        return classcount;
    }

    public void setClasscount(String classcount) {
        this.classcount = classcount;
    }
}
