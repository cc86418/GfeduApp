package jc.cici.android.atom.bean;

/**
 * 套餐班型下邮寄信息
 * Created by atom on 2017/8/31.
 */

public class PackageExpress {

    private String Express_Datel;
    private String Express_Company;
    private String Express_Code;
    private int Express_State;
    private String Express_StateName;

    public String getExpress_Datel() {
        return Express_Datel;
    }

    public void setExpress_Datel(String express_Datel) {
        Express_Datel = express_Datel;
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
