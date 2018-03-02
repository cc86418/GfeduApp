package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 题库首页实体
 * Created by atom on 2017/12/20.
 */

public class TikuHomeBean {

    // 默认项目id
    private int DefProjectId;
    // 默认二级项目id
    private int DefChildProjectId;
    private ArrayList<TikuProject> List;

    public int getDefProjectId() {
        return DefProjectId;
    }

    public void setDefProjectId(int defProjectId) {
        DefProjectId = defProjectId;
    }

    public int getDefChildProjectId() {
        return DefChildProjectId;
    }

    public void setDefChildProjectId(int defChildProjectId) {
        DefChildProjectId = defChildProjectId;
    }

    public ArrayList<TikuProject> getList() {
        return List;
    }

    public void setList(ArrayList<TikuProject> list) {
        List = list;
    }

    public class TikuProject {
        // 项目id
        private int ProjectId;
        // 项目名称
        private String ProjectName;
        private ArrayList<Node> LevelTwo;

        public ArrayList<Node> getLevelTwo() {
            return LevelTwo;
        }

        public void setLevelTwo(ArrayList<Node> levelTwo) {
            LevelTwo = levelTwo;
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

        public class Node {

            // 项目id
            private int ProjectId;
            // 项目名称
            private String ProjectName;

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

        }

    }

}
