package cn.jun.bean;


import java.util.ArrayList;

public class UserPhotoBean {
    private String ResultState;
    private String ResultStr;
    private ArrayList<String> ResultData;

    public String getResultState() {
        return ResultState;
    }

    public void setResultState(String resultState) {
        ResultState = resultState;
    }

    public String getResultStr() {
        return ResultStr;
    }

    public void setResultStr(String resultStr) {
        ResultStr = resultStr;
    }

    public ArrayList<String> getResultData() {
        return ResultData;
    }

    public void setResultData(ArrayList<String> resultData) {
        ResultData = resultData;
    }
}
