package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 直播列表筛选实体
 * Created by atom on 2017/11/17.
 */

public class LiveSelectBean {

    private ArrayList<SelectList> List;

    public ArrayList<SelectList> getList() {
        return List;
    }

    public void setList(ArrayList<SelectList> list) {
        List = list;
    }

    public static class SelectList {

        private OneModel LevelOne;
        private ArrayList<TwoModel> LevelTwo;

        public OneModel getLevelOne() {
            return LevelOne;
        }

        public void setLevelOne(OneModel levelOne) {
            LevelOne = levelOne;
        }

        public ArrayList<TwoModel> getLevelTwo() {
            return LevelTwo;
        }

        public void setLevelTwo(ArrayList<TwoModel> levelTwo) {
            LevelTwo = levelTwo;
        }

        public static class OneModel {

            private int ProjectId;
            private String ProjectName;
            private int Sort;
            private boolean isSelect;

            public OneModel(int projectId, String projectName, int sort, boolean isSelect) {
                ProjectId = projectId;
                ProjectName = projectName;
                Sort = sort;
                this.isSelect = isSelect;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
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

        public static class TwoModel {

            private int ProjectId;
            private String ProjectName;
            private int Sort;
            private boolean isSelect;

            public TwoModel(int projectId, String projectName, int sort, boolean isSelect) {
                ProjectId = projectId;
                ProjectName = projectName;
                Sort = sort;
                this.isSelect = isSelect;
            }

            public boolean isSelect() {
                return isSelect;
            }

            public void setSelect(boolean select) {
                isSelect = select;
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
    }
}
