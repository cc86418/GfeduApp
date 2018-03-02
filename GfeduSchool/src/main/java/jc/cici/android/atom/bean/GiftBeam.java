package jc.cici.android.atom.bean;

/**
 * 套餐赠送内容实体
 * Created by atom on 2017/8/30.
 */

public class GiftBeam {

    private int OrderProduct_PKID;
    private int OrderProduct_State;
    private int Product_Module;
    private int Product_Money;
    private float Product_SaleMoney;
    private float Product_OriginalPrice;
    private int Product_ID;
    private String Product_Name;
    private ExpressBean Express;

    public int getOrderProduct_PKID() {
        return OrderProduct_PKID;
    }

    public void setOrderProduct_PKID(int orderProduct_PKID) {
        OrderProduct_PKID = orderProduct_PKID;
    }

    public int getOrderProduct_State() {
        return OrderProduct_State;
    }

    public void setOrderProduct_State(int orderProduct_State) {
        OrderProduct_State = orderProduct_State;
    }

    public int getProduct_Module() {
        return Product_Module;
    }

    public void setProduct_Module(int product_Module) {
        Product_Module = product_Module;
    }

    public int getProduct_Money() {
        return Product_Money;
    }

    public void setProduct_Money(int product_Money) {
        Product_Money = product_Money;
    }

    public float getProduct_SaleMoney() {
        return Product_SaleMoney;
    }

    public void setProduct_SaleMoney(float product_SaleMoney) {
        Product_SaleMoney = product_SaleMoney;
    }

    public float getProduct_OriginalPrice() {
        return Product_OriginalPrice;
    }

    public void setProduct_OriginalPrice(float product_OriginalPrice) {
        Product_OriginalPrice = product_OriginalPrice;
    }

    public int getProduct_ID() {
        return Product_ID;
    }

    public void setProduct_ID(int product_ID) {
        Product_ID = product_ID;
    }

    public String getProduct_Name() {
        return Product_Name;
    }

    public void setProduct_Name(String product_Name) {
        Product_Name = product_Name;
    }

    public ExpressBean getExpress() {
        return Express;
    }

    public void setExpress(ExpressBean express) {
        Express = express;
    }
}
