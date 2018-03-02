package cn.jun.bean;


public class ProductBean {
    private int pkid;
    private String name;
    public boolean ischeck = false;

    public ProductBean(String name, int pkid) {
        this.name = name;
        this.pkid = pkid;
    }

    public int getPkid() {
        return pkid;
    }

    public void setPkid(int pkid) {
        this.pkid = pkid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


}
