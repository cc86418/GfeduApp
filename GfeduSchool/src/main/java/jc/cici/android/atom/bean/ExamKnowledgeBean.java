package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 知识点做题实体类
 * Created by atom on 2018/1/3.
 */

public class ExamKnowledgeBean {

    private int TpaperId;
    private int TpaperState;
    private String KnowledgeName;
    // 知识点总数量
    private int Count;
    private ArrayList<ParentNode> List;

    public int getTpaperId() {
        return TpaperId;
    }

    public void setTpaperId(int tpaperId) {
        TpaperId = tpaperId;
    }

    public int getTpaperState() {
        return TpaperState;
    }

    public void setTpaperState(int tpaperState) {
        TpaperState = tpaperState;
    }

    public String getKnowledgeName() {
        return KnowledgeName;
    }

    public void setKnowledgeName(String knowledgeName) {
        KnowledgeName = knowledgeName;
    }

    public class ParentNode {

        // 父类层数量
        private int Count;
        // 父类节点
        private ContentNode Data;
        private ArrayList<ParentNode> List;

        public int getCount() {
            return Count;
        }

        public void setCount(int count) {
            Count = count;
        }

        public ContentNode getData() {
            return Data;
        }

        public void setData(ContentNode data) {
            Data = data;
        }

        public ArrayList<ParentNode> getList() {
            return List;
        }

        public void setList(ArrayList<ParentNode> list) {
            List = list;
        }

        public class ContentNode {
            // 父类知识点id
            private int CoursewareData_PKID;
            // 父类知识点名称
            private String CoursewareData_Name;
            // 父类中题数量
            private int QuesCount;
            // 父类已做题数
            private int DoCount;
            // 正确率
            private String RightRatio;

            public int getCoursewareData_PKID() {
                return CoursewareData_PKID;
            }

            public void setCoursewareData_PKID(int coursewareData_PKID) {
                CoursewareData_PKID = coursewareData_PKID;
            }

            public String getCoursewareData_Name() {
                return CoursewareData_Name;
            }

            public void setCoursewareData_Name(String coursewareData_Name) {
                CoursewareData_Name = coursewareData_Name;
            }

            public int getQuesCount() {
                return QuesCount;
            }

            public void setQuesCount(int quesCount) {
                QuesCount = quesCount;
            }

            public int getDoCount() {
                return DoCount;
            }

            public void setDoCount(int doCount) {
                DoCount = doCount;
            }

            public String getRightRatio() {
                return RightRatio;
            }

            public void setRightRatio(String rightRatio) {
                RightRatio = rightRatio;
            }
        }
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public ArrayList<ParentNode> getList() {
        return List;
    }

    public void setList(ArrayList<ParentNode> list) {
        List = list;
    }
}
