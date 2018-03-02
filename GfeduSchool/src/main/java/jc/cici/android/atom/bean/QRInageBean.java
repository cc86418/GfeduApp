package jc.cici.android.atom.bean;

/**
 * 二维码图片实体
 * Created by atom on 2018/1/30.
 */

public class QRInageBean {

    private int ProjectID;
    private String QRcodeImage;
    private String QRcodeName;

    public int getProjectID() {
        return ProjectID;
    }

    public void setProjectID(int projectID) {
        ProjectID = projectID;
    }

    public String getQRcodeImage() {
        return QRcodeImage;
    }

    public void setQRcodeImage(String QRcodeImage) {
        this.QRcodeImage = QRcodeImage;
    }

    public String getQRcodeName() {
        return QRcodeName;
    }

    public void setQRcodeName(String QRcodeName) {
        this.QRcodeName = QRcodeName;
    }
}
