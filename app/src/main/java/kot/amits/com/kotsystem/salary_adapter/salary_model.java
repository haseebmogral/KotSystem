package kot.amits.com.kotsystem.salary_adapter;

public class salary_model {

    private String name;
    private String salary_date;
    private String paid_date;
    private float amount;

    public salary_model(String name, String salary_date, String paid_date, float amount) {
        this.name = name;
        this.salary_date = salary_date;
        this.paid_date = paid_date;
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSalary_date() {
        return salary_date;
    }

    public void setSalary_date(String salary_date) {
        this.salary_date = salary_date;
    }

    public String getPaid_date() {
        return paid_date;
    }

    public void setPaid_date(String paid_date) {
        this.paid_date = paid_date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }
}
