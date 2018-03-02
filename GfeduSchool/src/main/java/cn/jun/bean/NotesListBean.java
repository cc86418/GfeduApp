package cn.jun.bean;


import java.util.ArrayList;

public class NotesListBean {
    private String Code;
    private String Message;
    private Body Body;

    public class Body {
        private String NotesListCount;
        private ArrayList<NotesList> NotesList;

        public class NotesList {
            private String NTBPkid;
            private String NTBContent;
            private String NTBAddTime;
            private String NTBScreenShots;
            private String Zcount;
            private String IsZan;
            private String S_NickName;
            private String SubJectSName;
            private String SN_Head;
            private String NTBTempVal;
            private String CheckStatus;
            private String IsUser;

            public String getIsUser() {
                return IsUser;
            }

            public void setIsUser(String isUser) {
                IsUser = isUser;
            }

            public String getNTBPkid() {
                return NTBPkid;
            }

            public void setNTBPkid(String NTBPkid) {
                this.NTBPkid = NTBPkid;
            }

            public String getNTBContent() {
                return NTBContent;
            }

            public void setNTBContent(String NTBContent) {
                this.NTBContent = NTBContent;
            }

            public String getNTBAddTime() {
                return NTBAddTime;
            }

            public void setNTBAddTime(String NTBAddTime) {
                this.NTBAddTime = NTBAddTime;
            }

            public String getNTBScreenShots() {
                return NTBScreenShots;
            }

            public void setNTBScreenShots(String NTBScreenShots) {
                this.NTBScreenShots = NTBScreenShots;
            }

            public String getZcount() {
                return Zcount;
            }

            public void setZcount(String zcount) {
                Zcount = zcount;
            }

            public String getIsZan() {
                return IsZan;
            }

            public void setIsZan(String isZan) {
                IsZan = isZan;
            }

            public String getS_NickName() {
                return S_NickName;
            }

            public void setS_NickName(String s_NickName) {
                S_NickName = s_NickName;
            }

            public String getSubJectSName() {
                return SubJectSName;
            }

            public void setSubJectSName(String subJectSName) {
                SubJectSName = subJectSName;
            }

            public String getSN_Head() {
                return SN_Head;
            }

            public void setSN_Head(String SN_Head) {
                this.SN_Head = SN_Head;
            }

            public String getNTBTempVal() {
                return NTBTempVal;
            }

            public void setNTBTempVal(String NTBTempVal) {
                this.NTBTempVal = NTBTempVal;
            }

            public String getCheckStatus() {
                return CheckStatus;
            }

            public void setCheckStatus(String checkStatus) {
                CheckStatus = checkStatus;
            }
        }

        public String getNotesListCount() {
            return NotesListCount;
        }

        public void setNotesListCount(String notesListCount) {
            NotesListCount = notesListCount;
        }

        public ArrayList<NotesListBean.Body.NotesList> getNotesList() {
            return NotesList;
        }

        public void setNotesList(ArrayList<NotesListBean.Body.NotesList> notesList) {
            NotesList = notesList;
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

    public NotesListBean.Body getBody() {
        return Body;
    }

    public void setBody(NotesListBean.Body body) {
        Body = body;
    }
}
