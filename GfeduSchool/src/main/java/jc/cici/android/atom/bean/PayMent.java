package jc.cici.android.atom.bean;

/**
 * 支付列表内容
 * Created by atom on 2017/9/4.
 */

public class PayMent {

    // 支付流水号
    private String Payment_Code;
    // 支付时间
    private String Payment_Date;
    // 支付类型  0:线下(人工审核) 1:在线
    private int Payment_Type;
    // 支付金额
    private float Payment_Money;
    // 支付银行
    private String Payment_PayingBank;

    public String getPayment_Code() {
        return Payment_Code;
    }

    public void setPayment_Code(String payment_Code) {
        Payment_Code = payment_Code;
    }

    public String getPayment_Date() {
        return Payment_Date;
    }

    public void setPayment_Date(String payment_Date) {
        Payment_Date = payment_Date;
    }

    public int getPayment_Type() {
        return Payment_Type;
    }

    public void setPayment_Type(int payment_Type) {
        Payment_Type = payment_Type;
    }

    public float getPayment_Money() {
        return Payment_Money;
    }

    public void setPayment_Money(float payment_Money) {
        Payment_Money = payment_Money;
    }

    public String getPayment_PayingBank() {
        return Payment_PayingBank;
    }

    public void setPayment_PayingBank(String payment_PayingBank) {
        Payment_PayingBank = payment_PayingBank;
    }
}
