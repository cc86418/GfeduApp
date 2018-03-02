package cn.jun.bean;


public class OnLineInfoBean {
    private String Code;
    private String Message;
    private Body Body;

    public class Body {
        private String OnLineInfoCount;
        //当前课程信息
        private info info;
        //上一个课程信息
        private Topinfo Topinfo;
        //下一个课程信息
        private Downinfo Downinfo;

        public class Topinfo {
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
            private String ParentLevel_ShowName;
            private String StudyKey;
            private String ClassTypeID;
            private String VTime;

            public String getVTime() {
                return VTime;
            }

            public void setVTime(String VTime) {
                this.VTime = VTime;
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

            public String getVID() {
                return VID;
            }

            public void setVID(String VID) {
                this.VID = VID;
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

            public String getLiveStatus() {
                return LiveStatus;
            }

            public void setLiveStatus(String liveStatus) {
                LiveStatus = liveStatus;
            }

            public String getParentLevel_ShowName() {
                return ParentLevel_ShowName;
            }

            public void setParentLevel_ShowName(String parentLevel_ShowName) {
                ParentLevel_ShowName = parentLevel_ShowName;
            }

            public String getStudyKey() {
                return StudyKey;
            }

            public void setStudyKey(String studyKey) {
                StudyKey = studyKey;
            }

            public String getClassTypeID() {
                return ClassTypeID;
            }

            public void setClassTypeID(String classTypeID) {
                ClassTypeID = classTypeID;
            }
        }

        public class Downinfo {
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
            private String ParentLevel_ShowName;
            private String ParentLevelID;
            private String StudyKey;
            private String ClassTypeID;
            private String VTime;

            private String LiveUrl;
            private String SelectPlayUrl;
            private String PaperStatus;

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

            public String getPaperStatus() {
                return PaperStatus;
            }

            public void setPaperStatus(String paperStatus) {
                PaperStatus = paperStatus;
            }

            public String getParentLevelID() {
                return ParentLevelID;
            }

            public void setParentLevelID(String parentLevelID) {
                ParentLevelID = parentLevelID;
            }

            public String getVTime() {
                return VTime;
            }

            public void setVTime(String VTime) {
                this.VTime = VTime;
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

            public String getVID() {
                return VID;
            }

            public void setVID(String VID) {
                this.VID = VID;
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

            public String getLiveStatus() {
                return LiveStatus;
            }

            public void setLiveStatus(String liveStatus) {
                LiveStatus = liveStatus;
            }

            public String getParentLevel_ShowName() {
                return ParentLevel_ShowName;
            }

            public void setParentLevel_ShowName(String parentLevel_ShowName) {
                ParentLevel_ShowName = parentLevel_ShowName;
            }

            public String getStudyKey() {
                return StudyKey;
            }

            public void setStudyKey(String studyKey) {
                StudyKey = studyKey;
            }

            public String getClassTypeID() {
                return ClassTypeID;
            }

            public void setClassTypeID(String classTypeID) {
                ClassTypeID = classTypeID;
            }
        }

        public class info {
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
            private String ParentLevel_ShowName;
            private String StudyKey;
            private String ClassTypeID;
            private String VTime;

            public String getVTime() {
                return VTime;
            }

            public void setVTime(String VTime) {
                this.VTime = VTime;
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

            public String getVID() {
                return VID;
            }

            public void setVID(String VID) {
                this.VID = VID;
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

            public String getLiveStatus() {
                return LiveStatus;
            }

            public void setLiveStatus(String liveStatus) {
                LiveStatus = liveStatus;
            }

            public String getParentLevel_ShowName() {
                return ParentLevel_ShowName;
            }

            public void setParentLevel_ShowName(String parentLevel_ShowName) {
                ParentLevel_ShowName = parentLevel_ShowName;
            }

            public String getStudyKey() {
                return StudyKey;
            }

            public void setStudyKey(String studyKey) {
                StudyKey = studyKey;
            }

            public String getClassTypeID() {
                return ClassTypeID;
            }

            public void setClassTypeID(String classTypeID) {
                ClassTypeID = classTypeID;
            }
        }

        public String getOnLineInfoCount() {
            return OnLineInfoCount;
        }

        public void setOnLineInfoCount(String onLineInfoCount) {
            OnLineInfoCount = onLineInfoCount;
        }


        public OnLineInfoBean.Body.info getInfo() {
            return info;
        }

        public void setInfo(OnLineInfoBean.Body.info info) {
            this.info = info;
        }

        public OnLineInfoBean.Body.Topinfo getTopinfo() {
            return Topinfo;
        }

        public void setTopinfo(OnLineInfoBean.Body.Topinfo topinfo) {
            Topinfo = topinfo;
        }

        public OnLineInfoBean.Body.Downinfo getDowninfo() {
            return Downinfo;
        }

        public void setDowninfo(OnLineInfoBean.Body.Downinfo downinfo) {
            Downinfo = downinfo;
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

    public OnLineInfoBean.Body getBody() {
        return Body;
    }

    public void setBody(OnLineInfoBean.Body body) {
        Body = body;
    }
}
