package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 错题或收藏类型实体
 * Created by atom on 2018/1/8.
 */

public class ErrorOrFavorTypeBean {

    private int Count;
    private ArrayList<ErrorOrFavorType> List;

    public int getCount() {
        return Count;
    }

    public void setCount(int count) {
        Count = count;
    }

    public ArrayList<ErrorOrFavorType> getList() {
        return List;
    }

    public void setList(ArrayList<ErrorOrFavorType> list) {
        List = list;
    }

    public class ErrorOrFavorType {
        // 问题类型
        private int Ques_Type;
        // 类型名称
        private String TypeName;
        // 数量
        private int Count;
        private boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }

        public int getQues_Type() {
            return Ques_Type;
        }

        public void setQues_Type(int ques_Type) {
            Ques_Type = ques_Type;
        }

        public String getTypeName() {
            return TypeName;
        }

        public void setTypeName(String typeName) {
            TypeName = typeName;
        }

        public int getCount() {
            return Count;
        }

        public void setCount(int count) {
            Count = count;
        }
    }
}
