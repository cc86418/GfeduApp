package cn.jun.bean;


import java.io.Serializable;
import java.util.ArrayList;

public class ProjectListBean implements Serializable {
    private String Code;
    private String Message;
    private Body Body;

    public class Body {
        private ArrayList<ProjectList> ProjectList;

        public class ProjectList implements Serializable {
            private int CT_ID;
            private String CT_Name;
            private String CT_AppLogo;
            private int CT_Belong_CT_ID;
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

        public ArrayList<ProjectListBean.Body.ProjectList> getProjectList() {
            return ProjectList;
        }

        public void setProjectList(ArrayList<ProjectListBean.Body.ProjectList> projectList) {
            ProjectList = projectList;
        }
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public ProjectListBean.Body getBody() {
        return Body;
    }

    public void setBody(ProjectListBean.Body body) {
        Body = body;
    }
}
