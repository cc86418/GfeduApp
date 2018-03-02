package jc.cici.android.atom.bean;

/**
 * 试题二维码扫描结果类
 * Created by atom on 2018/1/19.
 */

public class QrBean {

    private String QRCodeType;
    private int ID;
    private int Type;

    public String getQRCodeType() {
        return QRCodeType;
    }

    public void setQRCodeType(String QRCodeType) {
        this.QRCodeType = QRCodeType;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}
