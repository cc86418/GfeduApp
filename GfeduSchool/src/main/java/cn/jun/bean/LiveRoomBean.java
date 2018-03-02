package cn.jun.bean;


public class LiveRoomBean {
    private String setDomain;
    private String setNumber;
    private String setLoginAccount;
    private String setLoginPwd;
    private String setJoinPwd;
    private String UniqueName;

    public String getUniqueName() {
        return UniqueName;
    }

    public void setUniqueName(String uniqueName) {
        UniqueName = uniqueName;
    }

    public String getSetDomain() {
        return setDomain;
    }

    public void setSetDomain(String setDomain) {
        this.setDomain = setDomain;
    }

    public String getSetNumber() {
        return setNumber;
    }

    public void setSetNumber(String setNumber) {
        this.setNumber = setNumber;
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