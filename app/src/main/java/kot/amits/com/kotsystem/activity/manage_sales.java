package kot.amits.com.kotsystem.activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.manage_employee_adapter.manage_employee_adapter;
import kot.amits.com.kotsystem.sales_adapter_model.sales_adapter;
import kot.amits.com.kotsystem.sales_adapter_model.sales_modal;

public class manage_sales extends AppCompatActivity {

    RecyclerView sales_recycler;
    DBmanager mydb;
    Cursor sales_cursor;
     List<sales_modal> sales_modals;
     sales_adapter sales_adapter;
     TextView total;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_sales);

        sales_recycler=findViewById(R.id.recyclerview);
        total=findViewById(R.id.total_amount);
        total.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sales_adapter.getFilter().filter("2018");

            }
        });

        mydb=new DBmanager(this);
        mydb.open();
        sales_cursor=mydb.get_all_sales_report();
        Log.d("size", String.valueOf(sales_cursor.getCount()));

        load();
    }

    private void load() {
        sales_modals = new ArrayList<>();
        sales_adapter = new sales_adapter(manage_sales.this, sales_modals);


        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setReverseLayout(false);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(manage_sales.this, 1);
//            employee_recycler.addItemDecoration(new SimpleDividerItemDecoration(this));

        sales_recycler.setLayoutManager(mLayoutManager);
        sales_recycler.setAdapter(sales_adapter);

        sales_modal s;
        s=new sales_modal( "11",  "11/08/2017",  "11:05 AM",  "Haseeb",  "Take away",  1000);
        sales_modals.add(s);

        while (sales_cursor.moveToNext()){
            String bill_no=sales_cursor.getString(sales_cursor.getColumnIndex(DBHelper.cart_id));
            String date=sales_cursor.getString(sales_cursor.getColumnIndex(DBHelper.date));;
            String time=sales_cursor.getString(sales_cursor.getColumnIndex(DBHelper.time));;
            String customer=sales_cursor.getString(sales_cursor.getColumnIndex(DBHelper.cart_customer_id));;
            String type=sales_cursor.getString(sales_cursor.getColumnIndex(DBHelper.cart_type));;
            float amount= Float.parseFloat(sales_cursor.getString(sales_cursor.getColumnIndex(DBHelper.total)));;
            s=new sales_modal( bill_no,  date,  time,  customer,  type,  amount);
            sales_modals.add(s);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.filter_menu, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            //Back button

            case R.id.filter:
                //addfav (heart icon) was clicked, Insert your after click code here.
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
