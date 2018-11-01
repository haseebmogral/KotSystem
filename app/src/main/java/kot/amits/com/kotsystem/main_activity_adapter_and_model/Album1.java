package kot.amits.com.kotsystem.main_activity_adapter_and_model;

/**
 * Created by Lincoln on 18/05/ha.
 */
public class Album1 {
    private int id;
    private String title;
    private int qty;
    private long price;
    private long total;

    public Album1(int id, String title,int qty,long price,long total) {
        this.id = id;
        this.title = title;
        this.qty = qty;
        this.price = price;
        this.total = total;

    }


    public String getName() {
        return title;
    }
    public int getId() {
        return id;
    }
    public int getQty() {
        return qty;
    }
    public long getprice() {
        return price;
    }
    public long getTotal() {
        return total;
    }


//    public String getImage() {
//        return image;
//    }

    public void setName(String name) {
        this.title = name;
    }
//    public void setImage(String image) {
//        this.image = image;
//    }
//    public void setId(String id) {
//        this.id = id;
//    }




}
