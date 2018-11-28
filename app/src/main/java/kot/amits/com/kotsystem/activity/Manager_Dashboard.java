package kot.amits.com.kotsystem.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.LinearLayout;

import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.adapter.CustomAdapter;
import kot.amits.com.kotsystem.R;

public class Manager_Dashboard extends AppCompatActivity implements View.OnClickListener {
    DBmanager dBmanager;

    LinearLayout manage_category, manage_item, manage_supplier, manage_purchase, manage_sales, manage_salary, manage_attendace,
            manage_expense, manage_stocks, manage_feedback,manage_employee;

    public GridView gridview;
    private static String[] app_name = {"Category", "Items", "Supplier", "Purchase", "Expense", "Attendance", "Sales", "Salary",
            "Daily stock manage", "Feedback"};
    private static int[] app_icon = {R.drawable.circle_shape, R.drawable.items, R.drawable.circle_shape, R.drawable.circle_shape,
            R.drawable.circle_shape, R.drawable.circle_shape, R.drawable.circle_shape, R.drawable.circle_shape, R.drawable.circle_shape,
            R.drawable.circle_shape};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_manager__dashboard);
        dBmanager=new DBmanager(this);
        dBmanager.open();

        manage_category = findViewById(R.id.manage_category);
        manage_item = findViewById(R.id.manage_item);
        manage_supplier = findViewById(R.id.manage_supplier);
        manage_purchase = findViewById(R.id.manage_purchase);
        manage_sales = findViewById(R.id.manage_sales);
        manage_salary = findViewById(R.id.manage_salary);
        manage_attendace = findViewById(R.id.manage_attendance);
        manage_expense = findViewById(R.id.manage_expense);
        manage_stocks = findViewById(R.id.manage_stocks);
        manage_feedback = findViewById(R.id.manage_feedback);
        manage_employee = findViewById(R.id.manage_employee);


        manage_category.setOnClickListener(this);
        manage_item.setOnClickListener(this);
        manage_supplier.setOnClickListener(this);
        manage_purchase.setOnClickListener(this);
        manage_sales.setOnClickListener(this);
        manage_salary.setOnClickListener(this);
        manage_attendace.setOnClickListener(this);
        manage_expense.setOnClickListener(this);
        manage_stocks.setOnClickListener(this);
        manage_feedback.setOnClickListener(this);
        manage_employee.setOnClickListener(this);


//        gridview=(GridView)findViewById(R.id.gridView);
        // setting up Adapter tp GridView
//        gridview.setAdapter(new CustomAdapter(this,app_name,app_icon));
//
//        gridview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                switch (position)
//                {
//                    case 0:
//                        break;
//                    case 1:
//                        break;
//                    case 2:
//
//
//                        Intent add_supplier=new Intent(Manager_Dashboard.this,Add_supplier.class);
//                        startActivity(add_supplier);
//
//                        break;
//                    case 3:
//                        Intent intent=new Intent(Manager_Dashboard.this,Purchase_Activity.class);
//                        startActivity(intent);
//                        break;
//
//
//                }
//            }
//        });


    }

    @Override
    public void onClick(View v) {

        if (v == manage_category) {


        } else if (v == manage_item) {

        } else if (v == manage_supplier) {

            Intent manage_supplier=new Intent(Manager_Dashboard.this,Add_supplier.class);
                        startActivity(manage_supplier);

        } else if (v == manage_purchase) {

            Intent manage_purchase=new Intent(Manager_Dashboard.this,Purchase_Activity.class);
                        startActivity(manage_purchase);



        } else if (v == manage_sales) {

        } else if (v == manage_salary) {

        } else if (v == manage_attendace) {

        } else if (v == manage_expense) {


            Intent manage_expense=new Intent(Manager_Dashboard.this,Expense_activity.class);
            startActivity(manage_expense);


        } else if (v == manage_stocks) {

        } else if (v == manage_feedback) {

        } else if (v == manage_employee) {



            Intent manage_employee=new Intent(Manager_Dashboard.this,Manage_employee.class);
            startActivity(manage_employee);


        }

    }
}
