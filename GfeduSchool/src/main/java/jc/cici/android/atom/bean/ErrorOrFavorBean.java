package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 错题or收藏实体
 * Created by atom on 2018/1/5.
 */

public class ErrorOrFavorBean {

    // 数量
    private int Count;
    // 列表
    private ArrayList<ErrorOrFavor> List;

    public class ErrorOrFavor{
        // 问题id
        private int Ques_PKID;
        // 问题名称
        private String Ques_Content;

        public int getQues_PKID() {
            return Ques_PKID;
        }

        public void setQues_PKID(int ques_PKID) {
            Ques_PKID = ques_PKID;
        }

        public String getQues_Content() {
            return Ques_Content;
        }

        public void setQues_Content(String ques_Content) {
            Ques_Content = ques_Content;
        }
    }

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public ArrayList<ErrorOrFavor> getList() {
        return List;
    }

    public void setList(ArrayList<ErrorOrFavor> list) {
        List = list;
    }
}
