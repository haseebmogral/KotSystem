package kot.amits.com.kotsystem.sales_items_adapter_model;

public class sales_items_modal {
    String item_name;
    String qty;
    String rate;
    float total;
;




    public sales_items_modal(String item_name,String qty,String rate,float total) {

        this.item_name = item_name;
        this.qty = qty;
        this.rate = rate;
        this.total = total;


    }

    public String getItem_name() {
        return item_name;
    }

    public void setItem_name(String item_name) {
        this.item_name = item_name;
    }

    public String getQty() {
        return qty;
    }

    public void setQty(String qty) {
        this.qty = qty;
    }

    public String getRate() {
        return rate;
    }

    public void setRate(String rate) {
        this.rate = rate;
    }

    public float getTotal() {
        return total;
    }

    public void setTotal(float total) {
        this.total = total;
    }
}
