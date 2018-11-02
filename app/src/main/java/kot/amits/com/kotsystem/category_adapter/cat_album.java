package kot.amits.com.kotsystem.category_adapter;

public class cat_album {

    private String title;
    private int  cat_id;

    public cat_album(String title,int cat_id) {
        this.title = title;
        this.cat_id = cat_id;

    }


    public String getName()
    {
        return title;
    }
    public int getCat_id()
    {
        return cat_id;
    }
//    public String getId() {
//        return id;
//    }
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
