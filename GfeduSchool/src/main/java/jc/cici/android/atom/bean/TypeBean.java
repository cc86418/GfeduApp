package jc.cici.android.atom.bean;

/**
 * Created by User on 2017/8/10.
 */

public class TypeBean {
    // 1:面授 2:在线  4:直播多个逗号隔开
    private int typeID;
    private String typeName;
    private boolean isChecked;

    public TypeBean(int typeID, String typeName, boolean isChecked) {
        this.typeID = typeID;
        this.typeName = typeName;
        this.isChecked = isChecked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public int getTypeID() {
        return typeID;
    }

    public void setTypeID(int typeID) {
        this.typeID = typeID;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }
}
