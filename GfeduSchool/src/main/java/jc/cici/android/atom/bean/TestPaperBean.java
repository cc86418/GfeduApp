package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 做题记录实体类
 * Created by atom on 2018/1/12.
 */

public class TestPaperBean {

    // 试卷数量
    private int TpaperCount;
    // 试卷列表
    private ArrayList<TestPaperInfo>Tpapers;

    public int getTpaperCount() {
        return TpaperCount;
    }

    public void setTpaperCount(int tpaperCount) {
        TpaperCount = tpaperCount;
    }

    public ArrayList<TestPaperInfo> getTpapers() {
        return Tpapers;
    }

    public void setTpapers(ArrayList<TestPaperInfo> tpapers) {
        Tpapers = tpapers;
    }

    public class TestPaperInfo{

        // 知识点ID
        private int TestPaper_PKID;
        // 知识点名次
        private String TestPaper_Name;
        // 试卷添加日期
        private String TestPaper_AddDate;
        // 试卷状态 0：未提交 1：已提交
        private int TestPaper_Status;

        public int getTestPaper_PKID() {
            return TestPaper_PKID;
        }

        public void setTestPaper_PKID(int testPaper_PKID) {
            TestPaper_PKID = testPaper_PKID;
        }

        public String getTestPaper_Name() {
            return TestPaper_Name;
        }

        public void setTestPaper_Name(String testPaper_Name) {
            TestPaper_Name = testPaper_Name;
        }

        public String getTestPaper_AddDate() {
            return TestPaper_AddDate;
        }

        public void setTestPaper_AddDate(String testPaper_AddDate) {
            TestPaper_AddDate = testPaper_AddDate;
        }

        public int getTestPaper_Status() {
            return TestPaper_Status;
        }

        public void setTestPaper_Status(int testPaper_Status) {
            TestPaper_Status = testPaper_Status;
        }
    }
}
