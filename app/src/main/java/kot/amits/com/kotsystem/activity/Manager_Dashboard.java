package kot.amits.com.kotsystem.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;

public class Manager_Dashboard extends AppCompatActivity implements View.OnClickListener {
    DBmanager dBmanager;

    LinearLayout manage_category, manage_item, manage_supplier, manage_purchase, manage_sales, manage_salary, manage_attendace,
            manage_expense, manage_stocks, manage_feedback,manage_employee,settings,customers;
    TextView sales,purchase,expense,cash_in_hand,cafe_feedback;

    CardView top_items,feedback;
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
        settings = findViewById(R.id.settings);
        customers = findViewById(R.id.customers);

        sales=findViewById(R.id.sales);
        purchase=findViewById(R.id.purchase);
        expense=findViewById(R.id.expense);
        cash_in_hand=findViewById(R.id.cash_in_hand);
        cafe_feedback=findViewById(R.id.customer_feedback);
        top_items=findViewById(R.id.top_item_card);
        feedback=findViewById(R.id.feedback_card);



        top_items.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("click","click");

                    // Prepare grid view
                    GridView gridView = new GridView(Manager_Dashboard.this);

                    List<String> mList = new ArrayList<String>();


                                Cursor item_cursor=dBmanager.get_top_items();
                                int i=1;
                while (item_cursor.moveToNext()) {
//                    String item_id = String.valueOf(item_cursor.getInt(item_cursor.getColumnIndex(DBHelper.item_id)));
                    String itemname = item_cursor.getString(item_cursor.getColumnIndex(DBHelper.item_name));
//                    String iteprice = item_cursor.getString(item_cursor.getColumnIndex(DBHelper.item_price));
//                    String image = item_cursor.getString(item_cursor.getColumnIndex(DBHelper.image));
                    mList.add(String.valueOf(i)+"."+itemname);
                    i++;
                }
//

                    gridView.setAdapter(new ArrayAdapter(Manager_Dashboard.this, android.R.layout.simple_list_item_1, mList));
                    gridView.setNumColumns(3);
                    gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            // do something here
                        }
                    });

                    // Set grid view to alertDialog
                    AlertDialog.Builder builder = new AlertDialog.Builder(Manager_Dashboard.this);
                    builder.setView(gridView);
                    builder.setTitle("Top sold items");
                    builder.show();
                }



        });

        feedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manage_feedback=new Intent(Manager_Dashboard.this,manage_feedback.class);
                startActivity(manage_feedback);
            }
        });


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
        settings.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        if (v == manage_category) {
            Intent intent=new Intent(Manager_Dashboard.this,category_selection.class);
            intent.putExtra("mode","settings");
            startActivity(intent);

        } else if (v == manage_item) {

        } else if (v == manage_supplier) {

            Intent manage_supplier=new Intent(Manager_Dashboard.this,Add_supplier.class);
                        startActivity(manage_supplier);

        } else if (v == manage_purchase) {

            Intent manage_purchase=new Intent(Manager_Dashboard.this,Purchase_Activity.class);
                        startActivity(manage_purchase);



        } else if (v == manage_sales) {
            Intent intent=new Intent(Manager_Dashboard.this,manage_sales.class);
            startActivity(intent);

        } else if (v == manage_salary) {
            Intent intent=new Intent(Manager_Dashboard.this,manage_salary.class);
            startActivity(intent);

        } else if (v == manage_attendace) {

        } else if (v == manage_expense) {


            Intent manage_expense=new Intent(Manager_Dashboard.this,Expense_activity.class);
            startActivity(manage_expense);


        } else if (v == manage_stocks) {

        } else if (v == manage_feedback) {
            Intent manage_feedback=new Intent(Manager_Dashboard.this,manage_feedback.class);
            startActivity(manage_feedback);
        } else if (v == manage_employee) {
            Intent manage_employee=new Intent(Manager_Dashboard.this,Manage_employee.class);
            startActivity(manage_employee);
        }
        else if (v == settings) {
            Intent manage_employee=new Intent(Manager_Dashboard.this,settings_activity.class);
            startActivity(manage_employee);
        } else if (v == customers) {
            Intent manage_employee=new Intent(Manager_Dashboard.this,settings_activity.class);
            startActivity(manage_employee);
        }

    }

    @Override
    protected void onPostResume() {
        Log.d("resume","resume");
        super.onPostResume();
        load();
    }
    public void load(){
        sales.setText("₹ "+dBmanager.get_todays_total_sales());
        purchase.setText("₹ "+dBmanager.get_todays_total_purchase());
        expense.setText("₹ "+dBmanager.get_todays_total_expense());
        cash_in_hand.setText("₹ "+dBmanager.get_todays_cash_in_hand());
        Cursor feedback_cursor=dBmanager.get_branch_feedbacks();
        feedback_cursor.moveToFirst();
        String f=feedback_cursor.getString(feedback_cursor.getColumnIndex("staff"));
        if (f==null){
            f="";
        }
        cafe_feedback.setText(f+"/5");
    }
}
