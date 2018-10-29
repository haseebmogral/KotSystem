package items_adapter;

public class item_album {

    private String item_name;
    private String price;
    private String image;

    public item_album(String item_name, String price, String image) {
        this.item_name = item_name;
        this.price=price;
        this.image=image;

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

}
