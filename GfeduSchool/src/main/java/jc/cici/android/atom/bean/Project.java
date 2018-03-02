package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 项目实体
 * Created by atom on 2017/8/4.
 */

public class Project {
    // 项目ID
    private int CT_ID;
    // 项目名称
    private String CT_Name;
    // Logo地址
    private String CT_AppLogo;
    // 父级ID
    private int CT_Belong_CT_ID;
    // 领域
    private String CT_Classify;

    public int getCT_ID() {
        return CT_ID;
    }

    public void setCT_ID(int CT_ID) {
        this.CT_ID = CT_ID;
    }

    public String getCT_Name() {
        return CT_Name;
    }

    public void setCT_Name(String CT_Name) {
        this.CT_Name = CT_Name;
    }

    public String getCT_AppLogo() {
        return CT_AppLogo;
    }

    public void setCT_AppLogo(String CT_AppLogo) {
        this.CT_AppLogo = CT_AppLogo;
    }

    public int getCT_Belong_CT_ID() {
        return CT_Belong_CT_ID;
    }

    public void setCT_Belong_CT_ID(int CT_Belong_CT_ID) {
        this.CT_Belong_CT_ID = CT_Belong_CT_ID;
    }

    public String getCT_Classify() {
        return CT_Classify;
    }

    public void setCT_Classify(String CT_Classify) {
        this.CT_Classify = CT_Classify;
    }

}
