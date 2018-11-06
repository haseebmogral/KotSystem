package kot.amits.com.kotsystem.category_model;

public class DataModel {

    public String name;
    public boolean checked;
    public int cat_id;

    public DataModel(int cat_id,String name, boolean checked) {
        this.name = name;
        this.cat_id=cat_id;
        this.checked = checked;


    }
}