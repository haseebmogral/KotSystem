package kot.amits.com.kotsystem.category_adapter;

public class cat_album {

    private String title;
    private int  cat_id;
    private boolean  selected;

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isSelected() {

        return selected;
    }

    public cat_album(String title, int cat_id, boolean selected) {
        this.title = title;
        this.cat_id = cat_id;
        this.selected = selected;

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
