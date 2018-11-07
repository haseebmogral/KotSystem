package kot.amits.com.kotsystem.select_item_adapter;

public class select_item_album {

    private String item_name;
    private String price;
    private String image;
    private String item_id;

    public select_item_album(String item_name) {
        this.item_name = item_name;
        this.price=price;
        this.image=image;
        this.item_id=item_id;


        //String item_name, String price, String image

    }


    public String getItem_name()
    {
        return item_name;
    }


    public void setName(String item_name) {
        this.item_name = item_name;
    }


    public String getPrice()
    {
        return price;
    }

    public void setPrice(String price)
    {
        this.price=price;
    }



    public String getImage()
    {
        return image;
    }

    public  void setImage(String image)
    {
        this.image=image;
    }

    public String getid()
    {
        return item_id;
    }

    public  void setid(String item_id)
    {
        this.item_id=item_id;
    }

}
