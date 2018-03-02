package jc.cici.android.atom.bean;


/**
 * 注册信息
 * Created by atom on 2017/4/19.
 */

public class Register {
    // 用户id
    private int S_ID;
    // 用户名
    private String S_Name;
    // 密码
    private String S_PassWord;
    // 用户昵称
    private String S_NickName;
    // 用户真实名
    private String S_RealName;
    // 用户已性别
    private int S_Sex;
    // 用户e-mail
    private String S_Email;
    // 用户手机号
    private String S_Telephone;

    public int getS_ID() {
        return S_ID;
    }

    public void setS_ID(int s_ID) {
        S_ID = s_ID;
    }

    public String getS_Name() {
        return S_Name;
    }

    public void setS_Name(String s_Name) {
        S_Name = s_Name;
    }

    public String getS_PassWord() {
        return S_PassWord;
    }

    public void setS_PassWord(String s_PassWord) {
        S_PassWord = s_PassWord;
    }

    public String getS_NickName() {
        return S_NickName;
    }

    public void setS_NickName(String s_NickName) {
        S_NickName = s_NickName;
    }

    public String getS_RealName() {
        return S_RealName;
    }

    public void setS_RealName(String s_RealName) {
        S_RealName = s_RealName;
    }

    public int getS_Sex() {
        return S_Sex;
    }

    public void setS_Sex(int s_Sex) {
        S_Sex = s_Sex;
    }

    public String getS_Email() {
        return S_Email;
    }

    public void setS_Email(String s_Email) {
        S_Email = s_Email;
    }

    public String getS_Telephone() {
        return S_Telephone;
    }

    public void setS_Telephone(String s_Telephone) {
        S_Telephone = s_Telephone;
    }
}
