package kot.amits.com.kotsystem.manage_employee_adapter;

public class manage_employee_model {

    private String emp_name;
    private String emp_address;
    private String emp_desig;
    private String emp_contact;
    private String emp_salray;
    private String emp_doj;
    private String emp_status;
    private String emp_id;
//    private boolean isSelected = false;


    public manage_employee_model(String emp_id,String emp_name, String emp_address, String emp_desig, String emp_contact, String emp_salray, String emp_doj, String emp_status) {
        this.emp_name = emp_name;
        this.emp_address = emp_address;
        this.emp_desig = emp_desig;
        this.emp_contact = emp_contact;
        this.emp_salray = emp_salray;
        this.emp_doj = emp_doj;
        this.emp_status = emp_status;
        this.emp_id = emp_id;


    }

    public String getEmp_name() {
        return emp_name;
    }


    public void setEmp_name(String emp_name) {
        this.emp_name = emp_name;
    }


    public String getEmp_address() {
        return emp_address;
    }

    public void setEmp_address(String emp_address) {
        this.emp_address = emp_address;
    }

    public String getEmp_desig() {
        return emp_desig;
    }

    public void setEmp_desig(String emp_desig) {
        this.emp_desig = emp_desig;
    }


    public String getEmp_contact() {
        return  emp_contact;
    }

//    public void setEmp_contact(String emp_contact) {
//        this.emp_contact = emp_contact;
//    }

    public String getEmp_salray() {
        return emp_salray;
    }

    public void setEmp_salray(String emp_salray) {
        this.emp_salray = emp_salray;
    }

    public String getEmp_doj() {
        return emp_doj;
    }

    public void setEmp_doj(String emp_doj) {
        this.emp_doj = emp_doj;
    }

    public String getEmp_status() {
        return emp_status;
    }

    public void setEmp_status(String emp_status) {
        this.emp_status = emp_status;
    }


    public String getEmp_id() {
        return emp_id;
    }


}
