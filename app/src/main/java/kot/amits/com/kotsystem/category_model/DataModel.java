package kot.amits.com.kotsystem.category_model;

public class DataModel {

    public String name;
    public boolean checked;
    public int cat_id;
    public String cat_type;




    public DataModel(int cat_id, String name, boolean checked, String cat_type) {
        this.name = name;
        this.cat_id=cat_id;
        this.checked = checked;
        this.cat_type = cat_type;



    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean checked) {
        this.checked = checked;
    }

    public int getCat_id() {
        return cat_id;
    }

    public void setCat_id(int cat_id) {
        this.cat_id = cat_id;
    }

    public String getCat_type() {
        return cat_type;
    }

    public void setCat_type(String cat_type) {
        this.cat_type = cat_type;
    }
}