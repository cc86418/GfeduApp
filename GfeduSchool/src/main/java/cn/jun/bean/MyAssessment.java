package cn.jun.bean;


import java.util.ArrayList;

public class MyAssessment {
    private int TpaperCount; //试卷数量
    private int QuesCount;//试题数量
    private String RightRatio;//正确率
    private ArrayList<TpaperList> TpaperList;//试卷列表
    private int ClassCount;//推荐课程数量
    private ArrayList<ClassList> ClassList;//推荐课程
    private int CoursewareCount;//薄弱知识点数量
    private ArrayList<CoursewareList> CoursewareList;//薄弱知识点

    public class TpaperList {
        private int TpaperId;//试卷
        private String SubTime;//提交试卷
        private String RightRatio;//正确率

        public int getTpaperId() {
            return TpaperId;
        }

        public void setTpaperId(int tpaperId) {
            TpaperId = tpaperId;
        }

        public String getSubTime() {
            return SubTime;
        }

        public void setSubTime(String subTime) {
            SubTime = subTime;
        }

        public String getRightRatio() {
            return RightRatio;
        }

        public void setRightRatio(String rightRatio) {
            RightRatio = rightRatio;
        }
    }

    public class ClassList {
        private int ClassId;//课程ID
        private String ClassName;//课程名称

        public int getClassId() {
            return ClassId;
        }

        public void setClassId(int classId) {
            ClassId = classId;
        }

        public String getClassName() {
            return ClassName;
        }

        public void setClassName(String className) {
            ClassName = className;
        }
    }

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

    public int getTpaperCount() {
        return TpaperCount;
    }

    public void setTpaperCount(int tpaperCount) {
        TpaperCount = tpaperCount;
    }

    public int getQuesCount() {
        return QuesCount;
    }

    public void setQuesCount(int quesCount) {
        QuesCount = quesCount;
    }

    public String getRightRatio() {
        return RightRatio;
    }

    public void setRightRatio(String rightRatio) {
        RightRatio = rightRatio;
    }

    public ArrayList<MyAssessment.TpaperList> getTpaperList() {
        return TpaperList;
    }

    public void setTpaperList(ArrayList<MyAssessment.TpaperList> tpaperList) {
        TpaperList = tpaperList;
    }

    public int getClassCount() {
        return ClassCount;
    }

    public void setClassCount(int classCount) {
        ClassCount = classCount;
    }

    public ArrayList<MyAssessment.ClassList> getClassList() {
        return ClassList;
    }

    public void setClassList(ArrayList<MyAssessment.ClassList> classList) {
        ClassList = classList;
    }

    public int getCoursewareCount() {
        return CoursewareCount;
    }

    public void setCoursewareCount(int coursewareCount) {
        CoursewareCount = coursewareCount;
    }

    public ArrayList<MyAssessment.CoursewareList> getCoursewareList() {
        return CoursewareList;
    }

    public void setCoursewareList(ArrayList<MyAssessment.CoursewareList> coursewareList) {
        CoursewareList = coursewareList;
    }
}
