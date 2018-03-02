package cn.jun.bean;


import java.util.ArrayList;

public class ClassOutLineBean {
    private int Code;
    private String Message;
    private Body Body;

    public class Body {
        private int OutLineCount;//大纲数量
        private ArrayList<OutLineList> OutLineList;//大纲列表
        private int ClassId;
        private int ClassTypeId;


        public class OutLineList {
            private String StageName;//阶段名称
            private int LevelTwoCount;//第二次数量
            private int HasFreeVideo;//是否有免费视频 1：是 0：否
            private ArrayList<LevelTwo> LevelTwo;//第二层列表
            private int ChildClassTypeId;
            private int ChildClassTypeType;

            public class LevelTwo {
                private String LevelName;//层级名称
                private int HasFreeVideo;//是否有免费视频 1：是 0：否
                private int LevelCount;//第三层数量
                private ArrayList<List> List;//第三层列表

                public class List {
                    private String Level_PKID;//层级ID
                    private int Level_CSID;//子班型ID
                    private String Level_ShowName;//层级名称
                    private int KeyType;//文件类型：1:视频 2:试卷 3:直播 4:资料
                    private int KeyID;//文件ID
                    private String VID;//视频ID
                    private int IsFree;//是否免费 1：是 0：否
                    //新增直播
                    private int CS_IsReserve;//否有预约
                    private int CS_IsPlayback;//是否有回放
                    private int IsLiveStart;//直播是否开始
                    private int IsLiveEnd;//直播是否结束
                    private int HasBook;//是否预约过


                    public int getCS_IsReserve() {
                        return CS_IsReserve;
                    }

                    public void setCS_IsReserve(int CS_IsReserve) {
                        this.CS_IsReserve = CS_IsReserve;
                    }

                    public int getCS_IsPlayback() {
                        return CS_IsPlayback;
                    }

                    public void setCS_IsPlayback(int CS_IsPlayback) {
                        this.CS_IsPlayback = CS_IsPlayback;
                    }

                    public int getIsLiveStart() {
                        return IsLiveStart;
                    }

                    public void setIsLiveStart(int isLiveStart) {
                        IsLiveStart = isLiveStart;
                    }

                    public int getIsLiveEnd() {
                        return IsLiveEnd;
                    }

                    public void setIsLiveEnd(int isLiveEnd) {
                        IsLiveEnd = isLiveEnd;
                    }

                    public int getHasBook() {
                        return HasBook;
                    }

                    public void setHasBook(int hasBook) {
                        HasBook = hasBook;
                    }

                    public int getIsFree() {
                        return IsFree;
                    }

                    public void setIsFree(int isFree) {
                        IsFree = isFree;
                    }

                    public String getLevel_PKID() {
                        return Level_PKID;
                    }

                    public void setLevel_PKID(String level_PKID) {
                        Level_PKID = level_PKID;
                    }

                    public int getLevel_CSID() {
                        return Level_CSID;
                    }

                    public void setLevel_CSID(int level_CSID) {
                        Level_CSID = level_CSID;
                    }

                    public String getLevel_ShowName() {
                        return Level_ShowName;
                    }

                    public void setLevel_ShowName(String level_ShowName) {
                        Level_ShowName = level_ShowName;
                    }

                    public int getKeyType() {
                        return KeyType;
                    }

                    public void setKeyType(int keyType) {
                        KeyType = keyType;
                    }

                    public int getKeyID() {
                        return KeyID;
                    }

                    public void setKeyID(int keyID) {
                        KeyID = keyID;
                    }

                    public String getVID() {
                        return VID;
                    }

                    public void setVID(String VID) {
                        this.VID = VID;
                    }
                }

                public String getLevelName() {
                    return LevelName;
                }

                public void setLevelName(String levelName) {
                    LevelName = levelName;
                }

                public int getHasFreeVideo() {
                    return HasFreeVideo;
                }

                public void setHasFreeVideo(int hasFreeVideo) {
                    HasFreeVideo = hasFreeVideo;
                }

                public int getLevelCount() {
                    return LevelCount;
                }

                public void setLevelCount(int levelCount) {
                    LevelCount = levelCount;
                }

                public ArrayList<ClassOutLineBean.Body.OutLineList.LevelTwo.List> getList() {
                    return List;
                }

                public void setList(ArrayList<ClassOutLineBean.Body.OutLineList.LevelTwo.List> list) {
                    List = list;
                }
            }

            public String getStageName() {
                return StageName;
            }

            public void setStageName(String stageName) {
                StageName = stageName;
            }

            public int getLevelTwoCount() {
                return LevelTwoCount;
            }

            public void setLevelTwoCount(int levelTwoCount) {
                LevelTwoCount = levelTwoCount;
            }

            public ArrayList<ClassOutLineBean.Body.OutLineList.LevelTwo> getLevelTwo() {
                return LevelTwo;
            }

            public void setLevelTwo(ArrayList<ClassOutLineBean.Body.OutLineList.LevelTwo> levelTwo) {
                LevelTwo = levelTwo;
            }

            public int getHasFreeVideo() {
                return HasFreeVideo;
            }

            public void setHasFreeVideo(int hasFreeVideo) {
                HasFreeVideo = hasFreeVideo;
            }

            public int getChildClassTypeId() {
                return ChildClassTypeId;
            }

            public void setChildClassTypeId(int childClassTypeId) {
                ChildClassTypeId = childClassTypeId;
            }

            public int getChildClassTypeType() {
                return ChildClassTypeType;
            }

            public void setChildClassTypeType(int childClassTypeType) {
                ChildClassTypeType = childClassTypeType;
            }
        }

        public int getOutLineCount() {
            return OutLineCount;
        }

        public void setOutLineCount(int outLineCount) {
            OutLineCount = outLineCount;
        }

        public ArrayList<ClassOutLineBean.Body.OutLineList> getOutLineList() {
            return OutLineList;
        }

        public void setOutLineList(ArrayList<ClassOutLineBean.Body.OutLineList> outLineList) {
            OutLineList = outLineList;
        }

        public int getClassId() {
            return ClassId;
        }

        public void setClassId(int classId) {
            ClassId = classId;
        }

        public int getClassTypeId() {
            return ClassTypeId;
        }

        public void setClassTypeId(int classTypeId) {
            ClassTypeId = classTypeId;
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

    public ClassOutLineBean.Body getBody() {
        return Body;
    }

    public void setBody(ClassOutLineBean.Body body) {
        Body = body;
    }
}
