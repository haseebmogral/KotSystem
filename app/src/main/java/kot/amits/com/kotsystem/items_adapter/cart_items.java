package kot.amits.com.kotsystem.items_adapter;

public class cart_items {

    private int cart_id;
    private String name;
    private int item_id;
    private long price;
    private int qty;
    private long total;

    public cart_items(int cart_id,String name, int item_id, long price, int qty,long total) {
        this.cart_id = cart_id;
        this.item_id=item_id;
        this.price=price;
        this.qty=qty;
        this.total=total;
        this.name=name;

    }


    public int getItem_id()
    {
        return item_id;
    }
    public int cart_id(){
        return cart_id;
    }
    public long get_price(){
        return price;
    }
    public int get_qty(){
        return qty;
    }
    public long get_total(){
        return total;
    }
    public String get_name(){
        return name;
    }

    public void set_qty(int qty){
        this.qty=qty;
    }


}
