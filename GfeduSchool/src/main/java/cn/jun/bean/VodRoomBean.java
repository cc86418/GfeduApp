package cn.jun.bean;


import java.util.ArrayList;

public class VodRoomBean {
    private String setDomain;
    private int VodCount;
    private ArrayList<String> VodNumberList;

    private String setLoginAccount;
    private String setLoginPwd;
    private String setJoinPwd;

    public String getSetDomain() {
        return setDomain;
    }

    public void setSetDomain(String setDomain) {
        this.setDomain = setDomain;
    }

    public int getVodCount() {
        return VodCount;
    }

    public void setVodCount(int vodCount) {
        VodCount = vodCount;
    }

    public ArrayList<String> getVodNumberList() {
        return VodNumberList;
    }

    public void setVodNumberList(ArrayList<String> vodNumberList) {
        VodNumberList = vodNumberList;
    }

    public String getSetLoginAccount() {
        return setLoginAccount;
    }

    public void setSetLoginAccount(String setLoginAccount) {
        this.setLoginAccount = setLoginAccount;
    }

    public String getSetLoginPwd() {
        return setLoginPwd;
    }

    public void setSetLoginPwd(String setLoginPwd) {
        this.setLoginPwd = setLoginPwd;
    }

    public String getSetJoinPwd() {
        return setJoinPwd;
    }

    public void setSetJoinPwd(String setJoinPwd) {
        this.setJoinPwd = setJoinPwd;
    }
}
