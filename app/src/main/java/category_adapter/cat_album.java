package category_adapter;

public class cat_album {

    private String title;

    public cat_album(String title) {
        this.title = title;

    }


    public String getName()
    {
        return title;
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
