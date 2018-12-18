package kot.amits.com.kotsystem.feedback_adapter;

public class view_feedback_model {


    private String f_id;
    private String billno;
    private String date;
    private String customer_name;
    private double ambience_rating;
    private double staff_rating;
    private String review;




    public view_feedback_model(String f_id, String billno, String date, String customer_name, double ambience_rating, double staff_rating, String review) {
        this.f_id = f_id;
        this.billno = billno;
        this.date = date;
        this.customer_name = customer_name;

        this.ambience_rating = ambience_rating;
        this.staff_rating=staff_rating;
        this.review=review;
    }
    public String getF_id() {
        return f_id;
    }

    public void setF_id(String f_id) {
        this.f_id = f_id;
    }

    public String getReview() {
        return review;
    }

    public void setStaff_rating(double staff_rating) {
        this.staff_rating = staff_rating;
    }

    public void setAmbience_rating(double ambience_rating) {

        this.ambience_rating = ambience_rating;
    }

    public void setReview(String review) {

        this.review = review;
    }

    public String getBillno() {
        return billno;
    }

    public String getDate() {
        return date;
    }

    public String getCustomer_name() {
        return customer_name;
    }

    public double getAmbience_rating() {
        return ambience_rating;
    }

    public double getStaff_rating() {
        return staff_rating;
    }

    public void setBillno(String billno) {
        this.billno = billno;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setCustomer_name(String customer_name) {
        this.customer_name = customer_name;
    }

    public void setAmbience_rating(float ambience_rating) {
        this.ambience_rating = ambience_rating;
    }

    public void setStaff_rating(float staff_rating) {
        this.staff_rating = staff_rating;
    }
}
