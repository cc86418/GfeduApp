package cn.jun.courseinfo.bean;


public class TeachBean {
    private int id;
    private String typeName;
    private boolean typeSelect;
    private boolean isChecked = true;

    private boolean isClick = false;


    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public boolean isTypeSelect() {
        return typeSelect;
    }

    public void setTypeSelect(boolean typeSelect) {
        this.typeSelect = typeSelect;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setChecked(boolean checked) {
        isChecked = checked;
    }

    public boolean isClick() {
        return isClick;
    }

    public void setClick(boolean click) {
        isClick = click;
    }
}
