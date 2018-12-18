package kot.amits.com.kotsystem.activity;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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


        mydb=new DBmanager(this);
        mydb.open();
        sales_cursor=mydb.get_all_sales();
        Log.d("size", String.valueOf(sales_cursor.getCount()));

        load();
    }

    private void load() {
        sales_modals = new ArrayList<>();
        sales_adapter = new sales_adapter(manage_sales.this, sales_modals,total);


        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setReverseLayout(false);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(manage_sales.this, 1);
//            employee_recycler.addItemDecoration(new SimpleDividerItemDecoration(this));

        sales_recycler.setLayoutManager(mLayoutManager);
        sales_recycler.setAdapter(sales_adapter);

        sales_modal s;

        int sl=1;
        while (sales_cursor.moveToNext()){
            String bill_no=sales_cursor.getString(sales_cursor.getColumnIndex(DBHelper.cart_id));
            String date=sales_cursor.getString(sales_cursor.getColumnIndex(DBHelper.date));;
            String time=sales_cursor.getString(sales_cursor.getColumnIndex(DBHelper.time));;
            String customer=sales_cursor.getString(sales_cursor.getColumnIndex(DBHelper.cart_customer_id));;
            String type=sales_cursor.getString(sales_cursor.getColumnIndex(DBHelper.cart_type));;
            float amount= Float.parseFloat(sales_cursor.getString(sales_cursor.getColumnIndex(DBHelper.total)));;
            s=new sales_modal(sl, bill_no,  date,  time,  customer,  type,  amount);
            sales_modals.add(s);
            sl++;
        }
        sales_adapter.getFilter().filter("");
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
                String[] colors = {"Date", "Type"};

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("filter by options");
                builder.setItems(colors, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                       if (which==0){
                           final Calendar myCalendar = Calendar.getInstance();

                           DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

                               @Override
                               public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                     int dayOfMonth) {
                                   // TODO Auto-generated method stub
                                   myCalendar.set(Calendar.YEAR, year);
                                   myCalendar.set(Calendar.MONTH, monthOfYear);
                                   myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                                   updateLabel();

                               }
                               private void updateLabel() {
                                   String myFormat = "yyyy-MM-dd"; //In which you need put here
                                   SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
                                   setTitle(sdf.format(myCalendar.getTime()));
                                   sales_adapter.getFilter().filter(sdf.format(myCalendar.getTime()));
                               }
                           };


                           new DatePickerDialog(manage_sales.this, date, myCalendar
                                   .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                   myCalendar.get(Calendar.DAY_OF_MONTH)).show();




                       }
                       else if (which==1){
                           final String[] type = new String[1];
                           LayoutInflater li = LayoutInflater.from(manage_sales.this);
                           View promptsView = li.inflate(R.layout.filter_view_radiobuttons, null);

                           AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                                   manage_sales.this);

                           // set prompts.xml to alertdialog builder
                           alertDialogBuilder.setView(promptsView);

                           final RadioGroup userInput = (RadioGroup) promptsView
                                   .findViewById(R.id.radiogroup);

                           userInput.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                               @Override
                               public void onCheckedChanged(RadioGroup group, int checkedId) {
                                   if (checkedId==R.id.dine_in){
                                       type[0] ="Dine in";
                                   }
                                   else if (checkedId==R.id.take_away){
                                       type[0] ="Take away";

                                   }
                               }
                           });

                           // set dialog message
                           alertDialogBuilder
                                   .setCancelable(false)
                                   .setPositiveButton("OK",
                                           new DialogInterface.OnClickListener() {
                                               public void onClick(DialogInterface dialog,int id) {
                                                   // get user input and set it to result
                                                   // edit text
                                                   sales_adapter.getFilter().filter(type[0]);

                                               }
                                           })
                                   .setNegativeButton("Cancel",
                                           new DialogInterface.OnClickListener() {
                                               public void onClick(DialogInterface dialog,int id) {
                                                   dialog.cancel();
                                               }
                                           });

                           // create alert dialog
                           AlertDialog alertDialog = alertDialogBuilder.create();

                           // show it
                           alertDialog.show();
                       }
                    }
                });
                builder.show();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }


}
