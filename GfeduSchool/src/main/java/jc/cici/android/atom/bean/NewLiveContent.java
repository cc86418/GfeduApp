package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 整合后直播内容实体类
 * Created by atom on 2017/11/10.
 */

public class NewLiveContent {

    private int count;
    private ArrayList<NewContent> contentList;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public ArrayList<NewContent> getContentList() {
        return contentList;
    }

    public void setContentList(ArrayList<NewContent> contentList) {
        this.contentList = contentList;
    }

    public static class NewContent{

        // 1:系列直播 2：正常直播
        private int Class_Form;
        // 直播id
        private int Class_PKID;

        private int Class_Project;
        // 项目名称
        private String Class_Name;
        private int Class_CollectNum;
        // 回放人数/预约人数/购买人数
        private int AboutNum;
        // 图片
        private String Class_MobileImage;
        // 原价范围
        private String Class_PriceRegion;
        // 售价范围
        private String Class_PriceSaleRegion;
        // 课表id
        private int CS_PKID;
        // 直播日期
        private String CS_Date;
        // 直播时间
        private String CS_DateShort;
        // 时间类型 （1：上午  2：下午   3：晚上）
        private int CS_DateType;
        // 直播开始时间
        private String CS_StartTime;
        // 直播结束时间
        private String CS_EndTime;
        // 学时
        private int CS_LearnTime;
        // 直播内容
        private String CS_Content;
        // 是否可预约
        private int CS_IsReserve;
        // 是否支持回放
        private int CS_IsPlayback;
        // 简介
        private String Class_Intro;
        // 课程特色
        private String Class_Feature;
        // 授课老师性别
        private int Lecturer_Sex;
        // 授课老师姓名
        private String Lecturer_Named;
        // 授课老师头像
        private String Lecturer_Img;
        // 项目名称
        private String ProjectName;
        // 简介标题
        private String Class_IntroName;
        // 是否预约过 1：是 0：否
        private int HasBook;
        // 是否购买过 1：是 0：否
        private int HasBookOrBuy;
        // 是否预约或购买 1：是 0：否
        private int CanWatchPlayBack;
        // 直播是否开始
        private int IsLiveBegin;
        // 直播是否结束
        private int IsLiveEnd;
        private int HasBuy;

        public int getClass_Form() {
            return Class_Form;
        }

        public void setClass_Form(int class_Form) {
            Class_Form = class_Form;
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

        public int getClass_CollectNum() {
            return Class_CollectNum;
        }

        public void setClass_CollectNum(int class_CollectNum) {
            Class_CollectNum = class_CollectNum;
        }

        public int getAboutNum() {
            return AboutNum;
        }

        public void setAboutNum(int aboutNum) {
            AboutNum = aboutNum;
        }

        public String getClass_MobileImage() {
            return Class_MobileImage;
        }

        public void setClass_MobileImage(String class_MobileImage) {
            Class_MobileImage = class_MobileImage;
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

        public String getClass_Intro() {
            return Class_Intro;
        }

        public void setClass_Intro(String class_Intro) {
            Class_Intro = class_Intro;
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

        public String getClass_IntroName() {
            return Class_IntroName;
        }

        public void setClass_IntroName(String class_IntroName) {
            Class_IntroName = class_IntroName;
        }

        public int getHasBook() {
            return HasBook;
        }

        public void setHasBook(int hasBook) {
            HasBook = hasBook;
        }

        public int getHasBookOrBuy() {
            return HasBookOrBuy;
        }

        public void setHasBookOrBuy(int hasBookOrBuy) {
            HasBookOrBuy = hasBookOrBuy;
        }

        public int getCanWatchPlayBack() {
            return CanWatchPlayBack;
        }

        public void setCanWatchPlayBack(int canWatchPlayBack) {
            CanWatchPlayBack = canWatchPlayBack;
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

        public int getHasBuy() {
            return HasBuy;
        }

        public void setHasBuy(int hasBuy) {
            HasBuy = hasBuy;
        }
    }
}
