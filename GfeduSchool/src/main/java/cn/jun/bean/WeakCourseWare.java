package cn.jun.bean;


import java.util.ArrayList;

public class WeakCourseWare {
    private int CoursewareCount;//薄弱知识点数量
    private ArrayList<CoursewareList> CoursewareList;//薄弱知识点

    public class CoursewareList {
        private int KnowledgeId;//知识点ID
        private String KnowledgeName;//知识点名称

        public int getKnowledgeId() {
            return KnowledgeId;
        }

        public void setKnowledgeId(int knowledgeId) {
            KnowledgeId = knowledgeId;
        }

        public String getKnowledgeName() {
            return KnowledgeName;
        }

        public void setKnowledgeName(String knowledgeName) {
            KnowledgeName = knowledgeName;
        }
    }

    public int getCoursewareCount() {
        return CoursewareCount;
    }

    public void setCoursewareCount(int coursewareCount) {
        CoursewareCount = coursewareCount;
    }

    public ArrayList<WeakCourseWare.CoursewareList> getCoursewareList() {
        return CoursewareList;
    }

    public void setCoursewareList(ArrayList<WeakCourseWare.CoursewareList> coursewareList) {
        CoursewareList = coursewareList;
    }
}
