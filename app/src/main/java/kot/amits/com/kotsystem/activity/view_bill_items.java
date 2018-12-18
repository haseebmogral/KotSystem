package kot.amits.com.kotsystem.activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;

import kot.amits.com.kotsystem.sales_items_adapter_model.sales_items_adapter;
import kot.amits.com.kotsystem.sales_items_adapter_model.sales_items_modal;

public class view_bill_items extends AppCompatActivity {
    String bill_no;

    RecyclerView sales_recycler;
    DBmanager mydb;
    Cursor sales_cursor;
    List<sales_items_modal> sales_modals;
    sales_items_adapter sales_adapter;
    TextView total;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_bill_items);
        bill_no=getIntent().getStringExtra("bill_id");

        sales_recycler=findViewById(R.id.recyclerview);
        total=findViewById(R.id.total_amount);

        mydb=new DBmanager(this);
        mydb.open();
        Log.d("f_id",bill_no);
        sales_cursor=mydb.get_sales_items(bill_no);
        Log.d("size", String.valueOf(sales_cursor.getCount()));
        load();
    }
    private void load() {
        sales_modals = new ArrayList<>();
        sales_adapter = new sales_items_adapter(view_bill_items.this, sales_modals,total);


        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setReverseLayout(false);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(view_bill_items.this, 1);
//            employee_recycler.addItemDecoration(new SimpleDividerItemDecoration(this));

        sales_recycler.setLayoutManager(mLayoutManager);
        sales_recycler.setAdapter(sales_adapter);

        sales_items_modal s;


        while (sales_cursor.moveToNext()){
            String name=sales_cursor.getString(sales_cursor.getColumnIndex(DBHelper.item_name));
            String qty=sales_cursor.getString(sales_cursor.getColumnIndex(DBHelper.c_qty));;
            String rate=sales_cursor.getString(sales_cursor.getColumnIndex(DBHelper.item_price));;
            float total= Float.parseFloat(sales_cursor.getString(sales_cursor.getColumnIndex(DBHelper.c_total)));;

            s=new sales_items_modal(name,qty,rate,total);
            sales_modals.add(s);

        }

    }

}
