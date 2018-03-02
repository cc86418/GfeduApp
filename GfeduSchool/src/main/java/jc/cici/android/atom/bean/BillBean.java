package jc.cici.android.atom.bean;

/**
 * 发票实体类
 * Created by atom on 2017/9/6.
 */

public class BillBean {

    // 发票id
    private int Invoice_PKID;
    // 订单id
    private int Order_ID;
    // 发票号
    private String Invoice_Number;
    // 发票类型(0.个人   1.机构)
    private int Invoice_Type;
    // 发票种类(1.专票   2.普票)
    private int Invoice_Kind;
    // 发票抬头
    private String Invoice_Title;
    // 发票内容
    private String Invoice_Item;
    // 纳税人识别号
    private String TaxpayerID;
    // 地址
    private String Invoice_Address;
    // 手机号
    private String Invoice_Phone;
    // 开户行名称
    private String AccountBank;
    // 开户行账号
    private String BankAccount;
    // 备注信息
    private String Invoice_ApplyRemark;
    // 开票状态(1.未开票   2.已开票)
    private int Order_InvoiceState;

    public int getInvoice_PKID() {
        return Invoice_PKID;
    }

    public void setInvoice_PKID(int invoice_PKID) {
        Invoice_PKID = invoice_PKID;
    }

    public int getOrder_ID() {
        return Order_ID;
    }

    public void setOrder_ID(int order_ID) {
        Order_ID = order_ID;
    }

    public String getInvoice_Number() {
        return Invoice_Number;
    }

    public void setInvoice_Number(String invoice_Number) {
        Invoice_Number = invoice_Number;
    }

    public int getInvoice_Type() {
        return Invoice_Type;
    }

    public void setInvoice_Type(int invoice_Type) {
        Invoice_Type = invoice_Type;
    }

    public int getInvoice_Kind() {
        return Invoice_Kind;
    }

    public void setInvoice_Kind(int invoice_Kind) {
        Invoice_Kind = invoice_Kind;
    }

    public String getInvoice_Title() {
        return Invoice_Title;
    }

    public void setInvoice_Title(String invoice_Title) {
        Invoice_Title = invoice_Title;
    }

    public String getInvoice_Item() {
        return Invoice_Item;
    }

    public void setInvoice_Item(String invoice_Item) {
        Invoice_Item = invoice_Item;
    }

    public String getTaxpayerID() {
        return TaxpayerID;
    }

    public void setTaxpayerID(String taxpayerID) {
        TaxpayerID = taxpayerID;
    }

    public String getInvoice_Address() {
        return Invoice_Address;
    }

    public void setInvoice_Address(String invoice_Address) {
        Invoice_Address = invoice_Address;
    }

    public String getInvoice_Phone() {
        return Invoice_Phone;
    }

    public void setInvoice_Phone(String invoice_Phone) {
        Invoice_Phone = invoice_Phone;
    }

    public String getAccountBank() {
        return AccountBank;
    }

    public void setAccountBank(String accountBank) {
        AccountBank = accountBank;
    }

    public String getBankAccount() {
        return BankAccount;
    }

    public void setBankAccount(String bankAccount) {
        BankAccount = bankAccount;
    }

    public String getInvoice_ApplyRemark() {
        return Invoice_ApplyRemark;
    }

    public void setInvoice_ApplyRemark(String invoice_ApplyRemark) {
        Invoice_ApplyRemark = invoice_ApplyRemark;
    }

    public int getOrder_InvoiceState() {
        return Order_InvoiceState;
    }

    public void setOrder_InvoiceState(int order_InvoiceState) {
        Order_InvoiceState = order_InvoiceState;
    }
}
