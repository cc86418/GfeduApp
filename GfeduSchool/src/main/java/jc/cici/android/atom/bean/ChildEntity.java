package jc.cici.android.atom.bean;

/**
 * 筛选功能第二级内容
 * Created by atom on 2017/8/8.
 */

public class ChildEntity {

    // 项目id
    private int ProjectId;
    //  项目名称
    private String ProjectName;
    // 排序值
    private int Sort;
    private boolean mChecked;

    public ChildEntity(int projectId, String projectName, int sort,boolean isChecked) {
        ProjectId = projectId;
        ProjectName = projectName;
        Sort = sort;
        mChecked = isChecked;
    }

    public boolean ismChecked() {
        return mChecked;
    }

    public void setmChecked(boolean mChecked) {
        this.mChecked = mChecked;
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
