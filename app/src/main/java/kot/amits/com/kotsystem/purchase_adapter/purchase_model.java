package kot.amits.com.kotsystem.purchase_adapter;

public class purchase_model {

    private String p_date;
    private String p_description;
    private String p_amount;

    public purchase_model(String p_description) {

        this.p_date = p_date;
        this.p_description = p_description;
        this.p_amount = p_amount;

    }

    public String getP_description() {
        return p_description;
    }

    public void setP_description(String p_description) {
        this.p_description = p_description;
    }


}
