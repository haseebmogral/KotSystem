package kot.amits.com.kotsystem.purchase_adapter;

public class purchase_model {

    private String p_date;
    private String p_description;
    private String p_amount;
    private String supplier_name;
    private  String paid_amount;
    private String balance;

    public purchase_model(String supplier_name,String p_date,String p_description,String p_amount,String paid_amount,String balance) {

        this.p_date = p_date;
        this.p_description = p_description;
        this.p_amount = p_amount;
        this.supplier_name=supplier_name;
        this.paid_amount=paid_amount;
        this.balance=balance;

    }

    public String getP_description() {
        return p_description;
    }

    public void setP_description(String p_description) {
        this.p_description = p_description;
    }


    public String getSupplier_name()
    {
        return  supplier_name;
    }
    public void setSupplier_name(String supplier_name)
    {
        this.supplier_name=supplier_name;
    }


    public String getP_date()
    {
        return p_date;
    }

    public void setP_date(String p_date)
    {
        this.p_date=p_date;
    }

    public  String getP_amount()
    {
        return p_amount;
    }

    public  void setP_amount(String amount){
        this.p_amount=p_amount;
    }

    public String getPaid_amount()
    {
        return paid_amount;
    }
    public void setPaid_amount(String paid_amount)
    {
        this.paid_amount=paid_amount;
    }

    public  String getBalance()
    {
        return balance;
    }

    public void setBalance(String balance){
        this.balance=balance;
    }


}
