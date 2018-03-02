package cn.jun.bean;


public class UpgradeAppBean {
    private String Code;
    private String Message;
    private Body Body;

    public class Body {
        private String IsUpgrade;
        private String IsForceUpdate;
        private String H5Link;
        private String DownloadUrl;

        public String getIsUpgrade() {
            return IsUpgrade;
        }

        public void setIsUpgrade(String isUpgrade) {
            IsUpgrade = isUpgrade;
        }

        public String getIsForceUpdate() {
            return IsForceUpdate;
        }

        public void setIsForceUpdate(String isForceUpdate) {
            IsForceUpdate = isForceUpdate;
        }

        public String getH5Link() {
            return H5Link;
        }

        public void setH5Link(String h5Link) {
            H5Link = h5Link;
        }

        public String getDownloadUrl() {
            return DownloadUrl;
        }

        public void setDownloadUrl(String downloadUrl) {
            DownloadUrl = downloadUrl;
        }
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public UpgradeAppBean.Body getBody() {
        return Body;
    }

    public void setBody(UpgradeAppBean.Body body) {
        Body = body;
    }
}
