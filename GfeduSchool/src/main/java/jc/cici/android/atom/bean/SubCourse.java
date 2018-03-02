package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 分项目内容实体
 * Created by atom on 2017/7/31.
 */

public class SubCourse {
    private int position;
    private ArrayList<Product> projectList;

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public ArrayList<Product> getProjectList() {
        return projectList;
    }

    public void setProjectList(ArrayList<Product> projectList) {
        this.projectList = projectList;
    }
}
