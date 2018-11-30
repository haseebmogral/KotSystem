package kot.amits.com.kotsystem.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.os.Build;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.expense_adapter.view_expense_adapter;
import kot.amits.com.kotsystem.expense_adapter.view_expense_model;
import kot.amits.com.kotsystem.purchase_adapter.purchase_adapter;
import kot.amits.com.kotsystem.purchase_adapter.purchase_model;
import kot.amits.com.kotsystem.view_supplier_adapter.view_supplier_adapter;
import kot.amits.com.kotsystem.view_supplier_adapter.view_supplier_model;

public class Expense_activity extends AppCompatActivity {


    private view_expense_adapter view_expense_adapter;
    private List<view_expense_model> view_expense_model;

    Calendar myCalendar = Calendar.getInstance();


    Spinner exp_type;
    TextView exp_date;
    String expense_type_name;

    EditText exp_desc, exp_amount;
    DBmanager mydb;
    Snackbar snackbar;
    CoordinatorLayout supplier_layout;
    Cursor get_expense_details_cursor;
    RecyclerView expense_recycler;
    TextView amount;


    Button add_expense;
    Button add_supplier_details;
    TextView emptyview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setTitle("Expense");

        setContentView(R.layout.activity_expense_activity);
        amount = findViewById(R.id.amount);
        expense_recycler = findViewById(R.id.expense_recycler);
        emptyview = findViewById(R.id.empty);
        emptyview.setVisibility(View.INVISIBLE);

        mydb = new DBmanager(this);
        mydb.open();

        Load_expense_details();


    }

    private void Load_expense_details() {

        get_expense_details_cursor = mydb.get_expense_details();


        if (mydb.get_expense_details().getCount() <= 0) {

            Toast.makeText(this, String.valueOf(mydb.get_expense_details().getCount()), Toast.LENGTH_SHORT).show();
            emptyview.setVisibility(View.VISIBLE);

        } else {

            emptyview.setVisibility(View.INVISIBLE);


            view_expense_model = new ArrayList<>();
            view_expense_adapter = new view_expense_adapter(Expense_activity.this, view_expense_model);


            LinearLayoutManager linearLayoutManager;
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
            linearLayoutManager.setReverseLayout(false);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Expense_activity.this, 1);
            expense_recycler.addItemDecoration(new SimpleDividerItemDecoration(this));

            expense_recycler.setLayoutManager(mLayoutManager);
            expense_recycler.setAdapter(view_expense_adapter);


            view_expense_model cat;

            int i = 1;

            float total = 0;


            while (get_expense_details_cursor.moveToNext()) {

                String e_type = get_expense_details_cursor.getString(get_expense_details_cursor.getColumnIndex(DBHelper.e_type));
                String slno = String.valueOf(i);
                String desc = get_expense_details_cursor.getString(get_expense_details_cursor.getColumnIndex(DBHelper.e_desc));
                String date = get_expense_details_cursor.getString(get_expense_details_cursor.getColumnIndex(DBHelper.e_date));
                String amount = get_expense_details_cursor.getString(get_expense_details_cursor.getColumnIndex(DBHelper.e_amount));



                cat = new view_expense_model(slno, e_type, desc, date, amount);
                view_expense_model.add(cat);

                total = total + Float.parseFloat(amount);


                i++;
            }


          String final_amount= NumberFormat.getNumberInstance(Locale.US).format(total);

//
//            DecimalFormat formatter = new DecimalFormat("####,##,#");
//            String yourFormattedString = formatter.format(total);



            amount.setText(String.valueOf(final_amount));

            view_expense_adapter.notifyDataSetChanged();


        }

    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");

        finish();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.custom_menu, menu);
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

            case R.id.add_expense:

                add_expense_dialog();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void add_expense_dialog() {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Expense_activity.this);

        LayoutInflater inflater = (LayoutInflater) Expense_activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.add_expense_layout, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog;
        alertDialog = dialogBuilder.create();
        dialogBuilder.setTitle(R.string.add_purchase_details);

        exp_desc = dialogView.findViewById(R.id.exp_description);
        exp_amount = dialogView.findViewById(R.id.exp_amount);
        add_expense = dialogView.findViewById(R.id.add_expense);
        exp_type = dialogView.findViewById(R.id.exp_type);
        exp_date = dialogView.findViewById(R.id.exp_date);



        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel();
            }

        };


        exp_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Expense_activity.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        List<String> expense_type = new ArrayList<String>();

        expense_type.add("Select expense type");
        expense_type.add("Rent Payment");
        expense_type.add("Telephone bill");
        expense_type.add("Electric bill");
        expense_type.add("Travel");
        expense_type.add("Advertising");
        expense_type.add("Office expense");
        expense_type.add("Repairs & maintenance");
        expense_type.add("Others");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, expense_type);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        exp_type.setAdapter(dataAdapter);

        exp_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                expense_type_name = parent.getItemAtPosition(position).toString();

                Toast.makeText(Expense_activity.this, expense_type_name, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        add_expense.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String ex_type = expense_type_name;
                String ex_date = exp_date.getText().toString();
                String ex_desc = exp_desc.getText().toString();
                String ex_amount = exp_amount.getText().toString();



                if (ex_type.equals("Select expense type")) {

                    Snackbar.make(v, "Please select expense type", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else if (ex_date.equals("Select date")) {
                    Snackbar.make(v, "please select date", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (ex_desc.equals("")) {
                    Snackbar.make(v, "Please enter description", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (ex_amount.equals("")) {
                    Toast.makeText(Expense_activity.this, "Please enter amount", Toast.LENGTH_SHORT).show();
                } else {
                    long add = mydb.add_expense(ex_type, ex_date, ex_desc, ex_amount);

                    if (String.valueOf(add).equals("-1")) {
                        Toast.makeText(Expense_activity.this, "Please try again", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(Expense_activity.this, "Expense details added", Toast.LENGTH_SHORT).show();

                        alertDialog.dismiss();
                        Load_expense_details();


                    }
                }

            }
        });

        alertDialog.show();

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        exp_date.setText(sdf.format(myCalendar.getTime()));
    }

}


