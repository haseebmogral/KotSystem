package kot.amits.com.kotsystem.expense_adapter;

public class view_expense_model {


    private String exp_type;
    private String exp_amount;
    private String exp_desc;
    private String slno;
    private String date;


    public view_expense_model(String slno,String exp_type,String exp_desc,String date,String exp_amount) {
        this.exp_type = exp_type;
        this.exp_amount = exp_amount;
        this.exp_desc = exp_desc;
        this.slno = slno;
        this.date=date;


    }

    public String getExp_type()
    {
        return exp_type;
    }
    public void setExp_type()
    {
        this.exp_type=exp_type;
    }


    public String getExp_amount()
    {
        return exp_amount;
    }

    public void setExp_amount()
    {
        this.exp_amount=exp_amount;
    }

    public String getExp_desc()
    {
        return exp_desc;
    }


    public void setExp_desc()
    {
        this.exp_desc=exp_desc;
    }

    public String getSlno()
    {
        return slno;
    }

    public void setSlno()
    {
        this.slno=slno;
    }

    public String getDate(){

        return date;
    }

    public void setDate()
    {
        this.date=date;
    }




}
