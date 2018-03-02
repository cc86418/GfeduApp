package cn.jun.bean;


import java.util.ArrayList;

public class StageLessonListBean {
    private int Code;
    private String Message;
    private Body Body;

    public class Body {
        private String StageName;
        private String StudySchedule;
        private ArrayList<ParentLevelList> ParentLevelList;
        private StudyInfo StudyInfo;

        public class ParentLevelList {
            private String Level_PKID;
            private String Level_ShowName;
            private String Totalcount;
            private String Studycount;
            private ArrayList<ChildList> ChildList;


            public class ChildList {
                private String Level_PKID;
                private String Level_ShowName;
                private String KeyType;
                private String VPKID;
                private String VID;
                private String TestPaper_PKID;
                private String State;
                private String Date;
                private String BeginTime;
                private String EndTime;
                private String LiveStatus;
                private String LiveUrl;//直播地址
                private String SelectPlayUrl;//回看地址
                private String StudyKey;
                private int CS_IsPlayback;
                private int ClassTypeID;
                private int KeyID;


                public int getKeyID() {
                    return KeyID;
                }

                public void setKeyID(int keyID) {
                    KeyID = keyID;
                }

                public int getClassTypeID() {
                    return ClassTypeID;
                }

                public void setClassTypeID(int classTypeID) {
                    ClassTypeID = classTypeID;
                }

                public int getCS_IsPlayback() {
                    return CS_IsPlayback;
                }

                public void setCS_IsPlayback(int CS_IsPlayback) {
                    this.CS_IsPlayback = CS_IsPlayback;
                }

                public String getStudyKey() {
                    return StudyKey;
                }

                public void setStudyKey(String studyKey) {
                    StudyKey = studyKey;
                }

                public String getLiveUrl() {
                    return LiveUrl;
                }

                public void setLiveUrl(String liveUrl) {
                    LiveUrl = liveUrl;
                }

                public String getSelectPlayUrl() {
                    return SelectPlayUrl;
                }

                public void setSelectPlayUrl(String selectPlayUrl) {
                    SelectPlayUrl = selectPlayUrl;
                }

                public String getLiveStatus() {
                    return LiveStatus;
                }

                public void setLiveStatus(String liveStatus) {
                    LiveStatus = liveStatus;
                }

                public String getVID() {
                    return VID;
                }

                public void setVID(String VID) {
                    this.VID = VID;
                }

                public String getLevel_PKID() {
                    return Level_PKID;
                }

                public void setLevel_PKID(String level_PKID) {
                    Level_PKID = level_PKID;
                }

                public String getLevel_ShowName() {
                    return Level_ShowName;
                }

                public void setLevel_ShowName(String level_ShowName) {
                    Level_ShowName = level_ShowName;
                }

                public String getKeyType() {
                    return KeyType;
                }

                public void setKeyType(String keyType) {
                    KeyType = keyType;
                }

                public String getVPKID() {
                    return VPKID;
                }

                public void setVPKID(String VPKID) {
                    this.VPKID = VPKID;
                }

                public String getTestPaper_PKID() {
                    return TestPaper_PKID;
                }

                public void setTestPaper_PKID(String testPaper_PKID) {
                    TestPaper_PKID = testPaper_PKID;
                }

                public String getState() {
                    return State;
                }

                public void setState(String state) {
                    State = state;
                }

                public String getDate() {
                    return Date;
                }

                public void setDate(String date) {
                    Date = date;
                }

                public String getBeginTime() {
                    return BeginTime;
                }

                public void setBeginTime(String beginTime) {
                    BeginTime = beginTime;
                }

                public String getEndTime() {
                    return EndTime;
                }

                public void setEndTime(String endTime) {
                    EndTime = endTime;
                }
            }

            public String getLevel_PKID() {
                return Level_PKID;
            }

            public void setLevel_PKID(String level_PKID) {
                Level_PKID = level_PKID;
            }

            public String getLevel_ShowName() {
                return Level_ShowName;
            }

            public void setLevel_ShowName(String level_ShowName) {
                Level_ShowName = level_ShowName;
            }

            public String getTotalcount() {
                return Totalcount;
            }

            public void setTotalcount(String totalcount) {
                Totalcount = totalcount;
            }

            public String getStudycount() {
                return Studycount;
            }

            public void setStudycount(String studycount) {
                Studycount = studycount;
            }

            public ArrayList<StageLessonListBean.Body.ParentLevelList.ChildList> getChildList() {
                return ChildList;
            }

            public void setChildList(ArrayList<StageLessonListBean.Body.ParentLevelList.ChildList> childList) {
                ChildList = childList;
            }
        }

        public class StudyInfo {
            private String Level_PKID;
            private String Level_ShowName;
            private String KeyType;
            private String VPKID;
            private String TestPaper_PKID;
            private String VID;
            private String Level_Parent;

            public String getLevel_Parent() {
                return Level_Parent;
            }

            public void setLevel_Parent(String level_Parent) {
                Level_Parent = level_Parent;
            }

            public String getVID() {
                return VID;
            }

            public void setVID(String VID) {
                this.VID = VID;
            }

            public String getLevel_PKID() {
                return Level_PKID;
            }

            public void setLevel_PKID(String level_PKID) {
                Level_PKID = level_PKID;
            }

            public String getLevel_ShowName() {
                return Level_ShowName;
            }

            public void setLevel_ShowName(String level_ShowName) {
                Level_ShowName = level_ShowName;
            }

            public String getKeyType() {
                return KeyType;
            }

            public void setKeyType(String keyType) {
                KeyType = keyType;
            }

            public String getVPKID() {
                return VPKID;
            }

            public void setVPKID(String VPKID) {
                this.VPKID = VPKID;
            }

            public String getTestPaper_PKID() {
                return TestPaper_PKID;
            }

            public void setTestPaper_PKID(String testPaper_PKID) {
                TestPaper_PKID = testPaper_PKID;
            }
        }

        public String getStageName() {
            return StageName;
        }

        public void setStageName(String stageName) {
            StageName = stageName;
        }

        public ArrayList<StageLessonListBean.Body.ParentLevelList> getParentLevelList() {
            return ParentLevelList;
        }

        public void setParentLevelList(ArrayList<StageLessonListBean.Body.ParentLevelList> parentLevelList) {
            ParentLevelList = parentLevelList;
        }

        public StageLessonListBean.Body.StudyInfo getStudyInfo() {
            return StudyInfo;
        }

        public void setStudyInfo(StageLessonListBean.Body.StudyInfo studyInfo) {
            StudyInfo = studyInfo;
        }

        public String getStudySchedule() {
            return StudySchedule;
        }

        public void setStudySchedule(String studySchedule) {
            StudySchedule = studySchedule;
        }
    }

    public int getCode() {
        return Code;
    }

    public void setCode(int code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public StageLessonListBean.Body getBody() {
        return Body;
    }

    public void setBody(StageLessonListBean.Body body) {
        Body = body;
    }
}
