package jc.cici.android.atom.bean;

/**
 * 子班型下邮寄信息
 * Created by atom on 2017/8/31.
 */

public class ChildExpress {

    private String Express_Date;
    private String Express_Company;
    private String Express_Code;
    private int Express_State;
    private String Express_StateName;

    public String getExpress_Date() {
        return Express_Date;
    }

    public void setExpress_Date(String express_Date) {
        Express_Date = express_Date;
    }

    public String getExpress_Company() {
        return Express_Company;
    }

    public void setExpress_Company(String express_Company) {
        Express_Company = express_Company;
    }

    public String getExpress_Code() {
        return Express_Code;
    }

    public void setExpress_Code(String express_Code) {
        Express_Code = express_Code;
    }

    public int getExpress_State() {
        return Express_State;
    }

    public void setExpress_State(int express_State) {
        Express_State = express_State;
    }

    public String getExpress_StateName() {
        return Express_StateName;
    }

    public void setExpress_StateName(String express_StateName) {
        Express_StateName = express_StateName;
    }
}
