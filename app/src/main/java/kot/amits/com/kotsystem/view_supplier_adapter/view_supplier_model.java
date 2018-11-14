package kot.amits.com.kotsystem.view_supplier_adapter;

public class view_supplier_model {

    private String supplier_name;
    private String supplier_address;
    private String supplier_contact;
    private String item_id;
    private boolean isSelected = false;


    public view_supplier_model(String supplier_name,String supplier_address,String supplier_contact) {
        this.supplier_name = supplier_name;
        this.supplier_address=supplier_address;
        this.supplier_contact=supplier_contact;
        this.item_id=item_id;




    }

    public String getSupplier_name()
    {
        return supplier_name;
    }


    public void setSupplier_name(String item_name) {
        this.supplier_name = item_name;
    }


    public String getSupplier_address()
    {
        return supplier_address;
    }

    public void setSupplier_address(String price)
    {
        this.supplier_address=price;
    }



    public String getSupplier_contact()
    {
        return supplier_contact;
    }




}
