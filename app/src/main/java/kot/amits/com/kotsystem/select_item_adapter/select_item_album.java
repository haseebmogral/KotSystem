package kot.amits.com.kotsystem.select_item_adapter;

public class select_item_album {

    private String cat_id;
    private String item_name;
    private String price;
    private String image;
    private int item_id;
    private boolean isSelected;




    public select_item_album(int item_id, String cat_id, String item_name, String image, String price,boolean isSelected) {
        this.item_name = item_name;
        this.price=price;
        this.image=image;
        this.item_id=item_id;
        this.cat_id=cat_id;
        this.isSelected=isSelected;


        //String item_name, String price, String image

    }
    public String getCat_id() {
        return cat_id;
    }

    public void setCat_id(String cat_id) {
        this.cat_id = cat_id;
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

    public int getid()
    {
        return item_id;
    }

    public  void setid(int item_id)
    {
        this.item_id=item_id;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }


    public boolean isSelected() {
        return isSelected;
    }

}
