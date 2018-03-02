package jc.cici.android.atom.bean;

import java.util.ArrayList;

/**
 * 筛选功能第一级
 * Created by atom on 2017/8/8.
 */

public class ParentEntity {

    private Parent LevelOne;

    public Parent getLevelOne() {
        return LevelOne;
    }

    public void setLevelOne(Parent levelOne) {
        LevelOne = levelOne;
    }
    // 二级内容列表
    private ArrayList<ChildEntity> LevelTwo;

    public ArrayList<ChildEntity> getLevelTwo() {
        return LevelTwo;
    }

    public void setLevelTwo(ArrayList<ChildEntity> levelTwo) {
        LevelTwo = levelTwo;
    }

}
