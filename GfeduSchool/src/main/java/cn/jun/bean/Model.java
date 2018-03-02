package cn.jun.bean;


public class Model {
    public String name;
    public int iconRes;
    public int ct_id;

    public Model(String name, int iconRes,int ct_id) {
        this.name = name;
        this.iconRes = iconRes;
        this.ct_id = ct_id;
    }

    public int getCt_id() {
        return ct_id;
    }

    public void setCt_id(int ct_id) {
        this.ct_id = ct_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIconRes() {
        return iconRes;
    }

    public void setIconRes(int iconRes) {
        this.iconRes = iconRes;
    }
}
