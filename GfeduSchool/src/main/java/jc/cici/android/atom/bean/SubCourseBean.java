package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 分类课程实体
 * Created by atom on 2017/7/31.
 */

public class SubCourseBean {

    // 广告位
    private ArrayList<Ads> AdsList;
    // 推荐班级
    private ArrayList<Product> RecommentList;
    // 列表
    private ArrayList<Product> HotList;
    // 项目列表
    private ArrayList<Project> ProjectList;

    public ArrayList<Ads> getAdsList() {
        return AdsList;
    }

    public void setAdsList(ArrayList<Ads> adsList) {
        AdsList = adsList;
    }

    public ArrayList<Product> getRecommentList() {
        return RecommentList;
    }

    public void setRecommentList(ArrayList<Product> recommentList) {
        RecommentList = recommentList;
    }

    public ArrayList<Product> getHotList() {
        return HotList;
    }

    public void setHotList(ArrayList<Product> hotList) {
        HotList = hotList;
    }

    public ArrayList<Project> getProjectList() {
        return ProjectList;
    }

    public void setProjectList(ArrayList<Project> projectList) {
        ProjectList = projectList;
    }
}
