package jc.cici.android.atom.bean;

/**
 * 筛选功能第一级内容
 * Created by atom on 2017/8/9.
 */

public class Parent {

    // 项目id
    private int ProjectId;
    //  项目名称
    private String ProjectName;
    // 排序值
    private int Sort;
    // 是否选中
    private boolean isSelected;

    public Parent(int projectId, String projectName, int sort, boolean isSelected) {
        ProjectId = projectId;
        ProjectName = projectName;
        Sort = sort;
        this.isSelected = isSelected;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public int getProjectId() {
        return ProjectId;
    }

    public void setProjectId(int projectId) {
        ProjectId = projectId;
    }

    public String getProjectName() {
        return ProjectName;
    }

    public void setProjectName(String projectName) {
        ProjectName = projectName;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }
}
