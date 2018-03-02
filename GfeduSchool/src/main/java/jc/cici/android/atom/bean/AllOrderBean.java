package jc.cici.android.atom.bean;


/**
 * 全部订单内容实体
 * Created by atom on 2017/8/25.
 */

public class AllOrderBean {

    // 订单id
    private int Order_PKID;
    // 订单编号
    private String Order_Code;
    //
    private int Order_User;
    // 订单总价
    private float Order_Money;
    // 欠费金额
    private float Order_Arrearage;
    // 订单状态(-1:已取消 0:未支付 1:欠费 2:已完成)
    private int Order_State;
    // 订单状态名称
    private String Order_StateName;
    // 发票申请状态(0:不需要 1:需要 2:已发送)
    private int Order_InvoiceApply;
    //
    private int Order_InvoiceState;
    //
    private String Order_InvoiceTitle;
    //
    private String Order_InvoiceItem;
    //
    private String Order_InvoiceNumber;
    //
    private String Order_Addressee;
    //
    private String Order_Phone;
    //
    private String Order_Email;
    //
    private String Order_Province;
    //
    private String Order_City;
    //
    private String Order_Address;
    //
    private String Order_CreateTime;
    //
    private int OrderAddressPKID;
    //
    private String Order_MailCode;
    //
    private boolean Order_IsCoupon;
    //
    private int Order_OriginalPrice;
    //
    private int Order_FromID;
    //
    private int Order_FromType;

    public int getOrder_PKID() {
        return Order_PKID;
    }

    public void setOrder_PKID(int order_PKID) {
        Order_PKID = order_PKID;
    }

    public String getOrder_Code() {
        return Order_Code;
    }

    public void setOrder_Code(String order_Code) {
        Order_Code = order_Code;
    }

    public int getOrder_User() {
        return Order_User;
    }

    public void setOrder_User(int order_User) {
        Order_User = order_User;
    }

    public float getOrder_Money() {
        return Order_Money;
    }

    public void setOrder_Money(float order_Money) {
        Order_Money = order_Money;
    }

    public float getOrder_Arrearage() {
        return Order_Arrearage;
    }

    public void setOrder_Arrearage(float order_Arrearage) {
        Order_Arrearage = order_Arrearage;
    }

    public int getOrder_State() {
        return Order_State;
    }

    public void setOrder_State(int order_State) {
        Order_State = order_State;
    }

    public String getOrder_StateName() {
        return Order_StateName;
    }

    public void setOrder_StateName(String order_StateName) {
        Order_StateName = order_StateName;
    }

    public int getOrder_InvoiceApply() {
        return Order_InvoiceApply;
    }

    public void setOrder_InvoiceApply(int order_InvoiceApply) {
        Order_InvoiceApply = order_InvoiceApply;
    }

    public int getOrder_InvoiceState() {
        return Order_InvoiceState;
    }

    public void setOrder_InvoiceState(int order_InvoiceState) {
        Order_InvoiceState = order_InvoiceState;
    }

    public String getOrder_InvoiceTitle() {
        return Order_InvoiceTitle;
    }

    public void setOrder_InvoiceTitle(String order_InvoiceTitle) {
        Order_InvoiceTitle = order_InvoiceTitle;
    }

    public String getOrder_InvoiceItem() {
        return Order_InvoiceItem;
    }

    public void setOrder_InvoiceItem(String order_InvoiceItem) {
        Order_InvoiceItem = order_InvoiceItem;
    }

    public String getOrder_InvoiceNumber() {
        return Order_InvoiceNumber;
    }

    public void setOrder_InvoiceNumber(String order_InvoiceNumber) {
        Order_InvoiceNumber = order_InvoiceNumber;
    }

    public String getOrder_Addressee() {
        return Order_Addressee;
    }

    public void setOrder_Addressee(String order_Addressee) {
        Order_Addressee = order_Addressee;
    }

    public String getOrder_Phone() {
        return Order_Phone;
    }

    public void setOrder_Phone(String order_Phone) {
        Order_Phone = order_Phone;
    }

    public String getOrder_Email() {
        return Order_Email;
    }

    public void setOrder_Email(String order_Email) {
        Order_Email = order_Email;
    }

    public String getOrder_Province() {
        return Order_Province;
    }

    public void setOrder_Province(String order_Province) {
        Order_Province = order_Province;
    }

    public String getOrder_City() {
        return Order_City;
    }

    public void setOrder_City(String order_City) {
        Order_City = order_City;
    }

    public String getOrder_Address() {
        return Order_Address;
    }

    public void setOrder_Address(String order_Address) {
        Order_Address = order_Address;
    }

    public String getOrder_CreateTime() {
        return Order_CreateTime;
    }

    public void setOrder_CreateTime(String order_CreateTime) {
        Order_CreateTime = order_CreateTime;
    }

    public int getOrderAddressPKID() {
        return OrderAddressPKID;
    }

    public void setOrderAddressPKID(int orderAddressPKID) {
        OrderAddressPKID = orderAddressPKID;
    }

    public String getOrder_MailCode() {
        return Order_MailCode;
    }

    public void setOrder_MailCode(String order_MailCode) {
        Order_MailCode = order_MailCode;
    }

    public boolean isOrder_IsCoupon() {
        return Order_IsCoupon;
    }

    public void setOrder_IsCoupon(boolean order_IsCoupon) {
        Order_IsCoupon = order_IsCoupon;
    }

    public int getOrder_OriginalPrice() {
        return Order_OriginalPrice;
    }

    public void setOrder_OriginalPrice(int order_OriginalPrice) {
        Order_OriginalPrice = order_OriginalPrice;
    }

    public int getOrder_FromID() {
        return Order_FromID;
    }

    public void setOrder_FromID(int order_FromID) {
        Order_FromID = order_FromID;
    }

    public int getOrder_FromType() {
        return Order_FromType;
    }

    public void setOrder_FromType(int order_FromType) {
        Order_FromType = order_FromType;
    }

}
