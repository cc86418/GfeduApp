package jc.cici.android.atom.ui.tiku.bean;

import jc.cici.android.R;
import jc.cici.android.atom.ui.tiku.treeView.LayoutItemType;

/**
 * 父类节点
 * Created by atom on 2018/1/3.
 */

public class ParentNode  implements LayoutItemType {

    // 父类知识点id
    public int CoursewareData_PKID;
    // 父类知识点名称
    public String CoursewareData_Name;
    // 父类中题数量
    public int QuesCount;
    // 父类已做题数
    public int DoCount;
    // 正确率
    public String RightRatio;

    public ParentNode(int coursewareData_PKID, String coursewareData_Name, int quesCount, int doCount, String rightRatio) {
        CoursewareData_PKID = coursewareData_PKID;
        CoursewareData_Name = coursewareData_Name;
        QuesCount = quesCount;
        DoCount = doCount;
        RightRatio = rightRatio;
    }

    @Override
    public int getLayoutId() {
        return R.layout.item_parent_node;
    }

}
