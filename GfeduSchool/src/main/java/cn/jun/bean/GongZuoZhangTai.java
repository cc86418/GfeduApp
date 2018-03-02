package cn.jun.bean;


import com.bigkoo.pickerview.model.IPickerViewData;

public class GongZuoZhangTai implements IPickerViewData {
    int id;
    String tpye;

    public GongZuoZhangTai(int id, String tpye) {
        this.id = id;
        this.tpye = tpye;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTpye() {
        return tpye;
    }

    public void setTpye(String tpye) {
        this.tpye = tpye;
    }

    @Override
    public String getPickerViewText() {
        return tpye;
    }
}
