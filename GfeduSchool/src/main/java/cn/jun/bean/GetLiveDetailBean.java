package cn.jun.bean;


import java.util.ArrayList;


public class GetLiveDetailBean {
    private int Code;
    private String Message;
    private Body Body;

    public class Body {
        public ClassInfo ClassInfo;
        public Schedule Schedule;

        public class ClassInfo {
            private int Class_PKID;//班级ID
            private int Class_Project;//班级所属项目
            private String Class_Name;//班级名称
            private String Class_Code;//班级编号
            private String Class_IntroName;//班级简介名称
            private String Class_Intro;//班级简介
            private int Class_EvaluateStar;//星级
            private int Class_CollectNum;//收藏数量
            private int Class_BuyNum;//购买数量
            private int Class_StudyNum;//学习人数
            private int Class_IsHot;//是否热门 1：是 0：否
            private String Class_MobileImage;//图片地址
            private String Class_MobileIsVideo;//是否为视频 1：是 0：否
            private String Class_MobileValue;//视频或图片值
            private String Class_MobileContent;//详情页内容
            private int FeatureCount;//特色数量
            private ArrayList<String> Class_Feature;//	特色值
            private int Class_OutlineID;//免费班型ID
            private int Class_OutlineFreeState;//是否有免费视频 1：是 0：否
            private String Class_MinPrice;//班级原价最小价格
            private String Class_MaxPrice;//班级原价最大价格
            private String Class_MinSalePrice;//班级售价最小价格
            private String Class_MaxSalePrice;//班级售价最大价格
            private String Class_PriceRegion;//班级原价区间
            private String Class_PriceSaleRegion;//班级售价区间
            private int HasCollection;//是否收藏过 1：是  0：否
            private String H5VideoLink;//H5视频地址
            private int ClassTypeCount;//所含班型数量
            private String H5DetailLink;//课程详情H5

            public String getH5DetailLink() {
                return H5DetailLink;
            }

            public void setH5DetailLink(String h5DetailLink) {
                H5DetailLink = h5DetailLink;
            }

            public int getClass_PKID() {
                return Class_PKID;
            }

            public void setClass_PKID(int class_PKID) {
                Class_PKID = class_PKID;
            }

            public int getClass_Project() {
                return Class_Project;
            }

            public void setClass_Project(int class_Project) {
                Class_Project = class_Project;
            }

            public String getClass_Name() {
                return Class_Name;
            }

            public void setClass_Name(String class_Name) {
                Class_Name = class_Name;
            }

            public String getClass_Code() {
                return Class_Code;
            }

            public void setClass_Code(String class_Code) {
                Class_Code = class_Code;
            }

            public String getClass_IntroName() {
                return Class_IntroName;
            }

            public void setClass_IntroName(String class_IntroName) {
                Class_IntroName = class_IntroName;
            }

            public String getClass_Intro() {
                return Class_Intro;
            }

            public void setClass_Intro(String class_Intro) {
                Class_Intro = class_Intro;
            }

            public int getClass_EvaluateStar() {
                return Class_EvaluateStar;
            }

            public void setClass_EvaluateStar(int class_EvaluateStar) {
                Class_EvaluateStar = class_EvaluateStar;
            }

            public int getClass_CollectNum() {
                return Class_CollectNum;
            }

            public void setClass_CollectNum(int class_CollectNum) {
                Class_CollectNum = class_CollectNum;
            }

            public int getClass_BuyNum() {
                return Class_BuyNum;
            }

            public void setClass_BuyNum(int class_BuyNum) {
                Class_BuyNum = class_BuyNum;
            }

            public int getClass_StudyNum() {
                return Class_StudyNum;
            }

            public void setClass_StudyNum(int class_StudyNum) {
                Class_StudyNum = class_StudyNum;
            }

            public int getClass_IsHot() {
                return Class_IsHot;
            }

            public void setClass_IsHot(int class_IsHot) {
                Class_IsHot = class_IsHot;
            }

            public String getClass_MobileImage() {
                return Class_MobileImage;
            }

            public void setClass_MobileImage(String class_MobileImage) {
                Class_MobileImage = class_MobileImage;
            }

            public String getClass_MobileIsVideo() {
                return Class_MobileIsVideo;
            }

            public void setClass_MobileIsVideo(String class_MobileIsVideo) {
                Class_MobileIsVideo = class_MobileIsVideo;
            }

            public String getClass_MobileValue() {
                return Class_MobileValue;
            }

            public void setClass_MobileValue(String class_MobileValue) {
                Class_MobileValue = class_MobileValue;
            }

            public String getClass_MobileContent() {
                return Class_MobileContent;
            }

            public void setClass_MobileContent(String class_MobileContent) {
                Class_MobileContent = class_MobileContent;
            }

            public int getFeatureCount() {
                return FeatureCount;
            }

            public void setFeatureCount(int featureCount) {
                FeatureCount = featureCount;
            }

            public ArrayList<String> getClass_Feature() {
                return Class_Feature;
            }

            public void setClass_Feature(ArrayList<String> class_Feature) {
                Class_Feature = class_Feature;
            }

            public int getClass_OutlineID() {
                return Class_OutlineID;
            }

            public void setClass_OutlineID(int class_OutlineID) {
                Class_OutlineID = class_OutlineID;
            }

            public int getClass_OutlineFreeState() {
                return Class_OutlineFreeState;
            }

            public void setClass_OutlineFreeState(int class_OutlineFreeState) {
                Class_OutlineFreeState = class_OutlineFreeState;
            }

            public String getClass_MinPrice() {
                return Class_MinPrice;
            }

            public void setClass_MinPrice(String class_MinPrice) {
                Class_MinPrice = class_MinPrice;
            }

            public String getClass_MaxPrice() {
                return Class_MaxPrice;
            }

            public void setClass_MaxPrice(String class_MaxPrice) {
                Class_MaxPrice = class_MaxPrice;
            }

            public String getClass_MinSalePrice() {
                return Class_MinSalePrice;
            }

            public void setClass_MinSalePrice(String class_MinSalePrice) {
                Class_MinSalePrice = class_MinSalePrice;
            }

            public String getClass_MaxSalePrice() {
                return Class_MaxSalePrice;
            }

            public void setClass_MaxSalePrice(String class_MaxSalePrice) {
                Class_MaxSalePrice = class_MaxSalePrice;
            }

            public String getClass_PriceRegion() {
                return Class_PriceRegion;
            }

            public void setClass_PriceRegion(String class_PriceRegion) {
                Class_PriceRegion = class_PriceRegion;
            }

            public String getClass_PriceSaleRegion() {
                return Class_PriceSaleRegion;
            }

            public void setClass_PriceSaleRegion(String class_PriceSaleRegion) {
                Class_PriceSaleRegion = class_PriceSaleRegion;
            }

            public int getHasCollection() {
                return HasCollection;
            }

            public void setHasCollection(int hasCollection) {
                HasCollection = hasCollection;
            }

            public String getH5VideoLink() {
                return H5VideoLink;
            }

            public void setH5VideoLink(String h5VideoLink) {
                H5VideoLink = h5VideoLink;
            }

            public int getClassTypeCount() {
                return ClassTypeCount;
            }

            public void setClassTypeCount(int classTypeCount) {
                ClassTypeCount = classTypeCount;
            }
        }

        public class Schedule {
            private int CS_PKID;//课表ID
            private String CS_Date;//直播日期 yyyy-MM-dd
            private String CS_DateShort;//日期缩写 MM-dd
            private int CS_DateType;//（1：上午  2：下午   3：晚上）
            private String CS_StartTime;//直播开始时间 HH:mm
            private String CS_EndTime;//直播结束时间：HH:mm
            private String StartDate;
            private String EndDate;
            private int CS_LearnTime;//学时
            private String CS_Content;//上课内容
            private int CS_IsReserve;//是否可预约
            private int CS_IsPlayback;//是否支持回放
            private String Class_Feature;//课程特色
            private int Lecturer_Sex;//授课老师性别
            private String Lecturer_Named;//授课老师名称
            private String Lecturer_Img;//头像名称
            private String ProjectName;//项目名称
            private int HasBook;//是否预约过 1：是 0：否
            private int HasBuy;//是否购买过 1：是 0：否
            private int HasBookOrBuy;//是否预约或购买 1：是 0：否
            private int IsLiveBegin;//直播是否开始
            private int IsLiveEnd;//直播是否结束
            private String Lecturer_Title;//讲师头衔
            private String Lecturer_Intro;//讲师简介
            private int BookCount;//预约人数
            private int HasWatch;//是否观看 1：是 0：否

            public String getStartDate() {
                return StartDate;
            }

            public void setStartDate(String startDate) {
                StartDate = startDate;
            }

            public String getEndDate() {
                return EndDate;
            }

            public void setEndDate(String endDate) {
                EndDate = endDate;
            }

            public int getCS_PKID() {
                return CS_PKID;
            }

            public void setCS_PKID(int CS_PKID) {
                this.CS_PKID = CS_PKID;
            }

            public String getCS_Date() {
                return CS_Date;
            }

            public void setCS_Date(String CS_Date) {
                this.CS_Date = CS_Date;
            }

            public String getCS_DateShort() {
                return CS_DateShort;
            }

            public void setCS_DateShort(String CS_DateShort) {
                this.CS_DateShort = CS_DateShort;
            }

            public int getCS_DateType() {
                return CS_DateType;
            }

            public void setCS_DateType(int CS_DateType) {
                this.CS_DateType = CS_DateType;
            }

            public String getCS_StartTime() {
                return CS_StartTime;
            }

            public void setCS_StartTime(String CS_StartTime) {
                this.CS_StartTime = CS_StartTime;
            }

            public String getCS_EndTime() {
                return CS_EndTime;
            }

            public void setCS_EndTime(String CS_EndTime) {
                this.CS_EndTime = CS_EndTime;
            }

            public int getCS_LearnTime() {
                return CS_LearnTime;
            }

            public void setCS_LearnTime(int CS_LearnTime) {
                this.CS_LearnTime = CS_LearnTime;
            }

            public String getCS_Content() {
                return CS_Content;
            }

            public void setCS_Content(String CS_Content) {
                this.CS_Content = CS_Content;
            }

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

            public String getClass_Feature() {
                return Class_Feature;
            }

            public void setClass_Feature(String class_Feature) {
                Class_Feature = class_Feature;
            }

            public int getLecturer_Sex() {
                return Lecturer_Sex;
            }

            public void setLecturer_Sex(int lecturer_Sex) {
                Lecturer_Sex = lecturer_Sex;
            }

            public String getLecturer_Named() {
                return Lecturer_Named;
            }

            public void setLecturer_Named(String lecturer_Named) {
                Lecturer_Named = lecturer_Named;
            }

            public String getLecturer_Img() {
                return Lecturer_Img;
            }

            public void setLecturer_Img(String lecturer_Img) {
                Lecturer_Img = lecturer_Img;
            }

            public String getProjectName() {
                return ProjectName;
            }

            public void setProjectName(String projectName) {
                ProjectName = projectName;
            }

            public int getHasBook() {
                return HasBook;
            }

            public void setHasBook(int hasBook) {
                HasBook = hasBook;
            }

            public int getHasBuy() {
                return HasBuy;
            }

            public void setHasBuy(int hasBuy) {
                HasBuy = hasBuy;
            }

            public int getHasBookOrBuy() {
                return HasBookOrBuy;
            }

            public void setHasBookOrBuy(int hasBookOrBuy) {
                HasBookOrBuy = hasBookOrBuy;
            }

            public int getIsLiveBegin() {
                return IsLiveBegin;
            }

            public void setIsLiveBegin(int isLiveBegin) {
                IsLiveBegin = isLiveBegin;
            }

            public int getIsLiveEnd() {
                return IsLiveEnd;
            }

            public void setIsLiveEnd(int isLiveEnd) {
                IsLiveEnd = isLiveEnd;
            }

            public String getLecturer_Title() {
                return Lecturer_Title;
            }

            public void setLecturer_Title(String lecturer_Title) {
                Lecturer_Title = lecturer_Title;
            }

            public String getLecturer_Intro() {
                return Lecturer_Intro;
            }

            public void setLecturer_Intro(String lecturer_Intro) {
                Lecturer_Intro = lecturer_Intro;
            }

            public int getBookCount() {
                return BookCount;
            }

            public void setBookCount(int bookCount) {
                BookCount = bookCount;
            }

            public int getHasWatch() {
                return HasWatch;
            }

            public void setHasWatch(int hasWatch) {
                HasWatch = hasWatch;
            }
        }

        public GetLiveDetailBean.Body.ClassInfo getClassInfo() {
            return ClassInfo;
        }

        public void setClassInfo(GetLiveDetailBean.Body.ClassInfo classInfo) {
            ClassInfo = classInfo;
        }

        public GetLiveDetailBean.Body.Schedule getSchedule() {
            return Schedule;
        }

        public void setSchedule(GetLiveDetailBean.Body.Schedule schedule) {
            Schedule = schedule;
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

    public GetLiveDetailBean.Body getBody() {
        return Body;
    }

    public void setBody(GetLiveDetailBean.Body body) {
        Body = body;
    }
}
