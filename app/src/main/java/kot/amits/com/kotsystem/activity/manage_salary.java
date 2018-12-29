package kot.amits.com.kotsystem.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
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
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;

import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;

import kot.amits.com.kotsystem.items_adapter.cart_items;
import kot.amits.com.kotsystem.salary_adapter.salary_adapter;
import kot.amits.com.kotsystem.salary_adapter.salary_model;

public class manage_salary extends AppCompatActivity {

    private salary_adapter salary_adapter;
    private List<salary_model> salary_model;

    RecyclerView salary_recycler;
    ArrayAdapter adapter;
    RadioGroup radiogroup;
    RelativeLayout balance_layout;

    Button pay_salary_btn,cancel;

    DBmanager mydb;

    Cursor get_salary_details, employee_list, get_basic_pay;
    TextView emptyview, basic_pay,amount_to_pay_text,total;
    String employee_id, pay,emp_name,emp_id,mont_str,year_str;
    int amnt;
    int p_amnt;
    int bal;

    String ra;
    String[] seperated;

    RadioButton full_pay,partial_pay;
    ArrayAdapter<String> arrayAdapter;



    private static Spinner employee_name_spinner;
    TextView salary_balance, salary_date;

    EditText salary_paid;

    Calendar myCalendar = Calendar.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_salary);
        salary_recycler = findViewById(R.id.salary_recycler);



        emptyview = findViewById(R.id.empty);
        emptyview.setVisibility(View.INVISIBLE);
        total=findViewById(R.id.total);


        mydb = new DBmanager(this);
        mydb.open();

         Load_salary_details();


    }

    private void Load_salary_details() {

        get_salary_details = mydb.get_salary_details();
        if (mydb.get_salary_details().getCount() <= 0) {

            Toast.makeText(this, String.valueOf(mydb.get_salary_details().getCount()), Toast.LENGTH_SHORT).show();
            emptyview.setVisibility(View.VISIBLE);

        } else {

            Toast.makeText(this, String.valueOf(mydb.get_salary_details().getCount()), Toast.LENGTH_SHORT).show();


            emptyview.setVisibility(View.INVISIBLE);


            salary_model = new ArrayList<>();
            salary_adapter = new salary_adapter(manage_salary.this, salary_model,total);


            LinearLayoutManager linearLayoutManager;
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
            linearLayoutManager.setReverseLayout(false);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(manage_salary.this, 1);
//            employee_recycler.addItemDecoration(new SimpleDividerItemDecoration(this));

            salary_recycler.setLayoutManager(mLayoutManager);
            salary_recycler.setAdapter(salary_adapter);


            salary_model cat;

//            int i = 1;
//
//            float total = 0;


            while (get_salary_details.moveToNext()) {
//
//                String name = get_salary_details.getString(get_salary_details.getColumnIndex(DBHelper.sid));
                String name = get_salary_details.getString(get_salary_details.getColumnIndex(DBHelper.emp_name));
                String paid_date = get_salary_details.getString(get_salary_details.getColumnIndex(DBHelper.giving_date));
                String salary_date = get_salary_details.getString(get_salary_details.getColumnIndex(DBHelper.salary_date));
                String amount = String.valueOf(get_salary_details.getLong(get_salary_details.getColumnIndex(DBHelper.s_paid)));

                Log.d("datas",name+"/n"+salary_date+paid_date+Float.parseFloat(amount));


                cat = new salary_model(name,salary_date, paid_date,Float.parseFloat(amount));
                salary_model.add(cat);
                }


            //  String final_amount= NumberFormat.getNumberInstance(Locale.US).format(total);

//
//           DecimalFormat formatter = new DecimalFormat("####,##,#");
//            String yourFormattedString = formatter.format(total);
            //amount.setText(String.valueOf(final_amount));

            salary_adapter.notifyDataSetChanged();


        }

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
                String[] colors = {"Date", "Employee"};

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
                                    salary_adapter.getFilter().filter(sdf.format(myCalendar.getTime())+"/date");
                                }
                            };


                            new DatePickerDialog(manage_salary.this, date, myCalendar
                                    .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                    myCalendar.get(Calendar.DAY_OF_MONTH)).show();




                        }
                        else if (which==1){
                            AlertDialog.Builder builderSingle = new AlertDialog.Builder(manage_salary.this);
                            builderSingle.setIcon(R.drawable.category_back);
                            builderSingle.setTitle("Select Bill");
                            final ArrayList<String> items =new ArrayList<String>();
                            arrayAdapter = new ArrayAdapter<String>(manage_salary.this, android.R.layout.simple_list_item_1);
                            salary_model a;

                            Cursor cursor=mydb.get_employee();
                            while (cursor.moveToNext()){
                                String name=cursor.getString(cursor.getColumnIndex(DBHelper.emp_name));
                                arrayAdapter.add(name);

                            }

                            for (int i=0;i<salary_model.size();i++){
                                a=salary_model.get(i);



                            }

                            builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });


                            builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    String name = arrayAdapter.getItem(which);
                                    Log.d("name",name);

                                    salary_adapter.getFilter().filter(name+"/employee");


                                }
                            });
                            builderSingle.show();
                        }
                    }
                });
                builder.show();
                return true;

            case R.id.add_expense:
                add_salary_dialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void add_salary_dialog() {


        employee_list = mydb.get_all_employees_name();

        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(manage_salary.this);

        LayoutInflater inflater = (LayoutInflater) manage_salary.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.add_salary, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog;
        alertDialog = dialogBuilder.create();
        dialogBuilder.setTitle(R.string.add_purchase_details);

        employee_name_spinner = dialogView.findViewById(R.id.employee_name_spinner);
        balance_layout = dialogView.findViewById(R.id.balance_layout);
        balance_layout.setVisibility(View.GONE);
        basic_pay = dialogView.findViewById(R.id.basic_pay);
        amount_to_pay_text = dialogView.findViewById(R.id.amount_to_pay_text);
        salary_balance=dialogView.findViewById(R.id.salary_balance);
        full_pay=dialogView.findViewById(R.id.full_payment_radio);
        partial_pay=dialogView.findViewById(R.id.partial_payment_radio);
        pay_salary_btn=dialogView.findViewById(R.id.pay_salary_btn);
        cancel=dialogView. findViewById(R.id.cancel_btn);


        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                alertDialog.dismiss();
            }
        });

        //adding salary to db

        pay_salary_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String sa_date = salary_date.getText().toString();
                String salary_amount_paid = salary_paid.getText().toString();
                String sal_balance = salary_balance.getText().toString();



                if (sa_date.equals("Select salary date"))
                {
                    Toast.makeText(manage_salary.this, "select salary date", Toast.LENGTH_SHORT).show();
                }
                else  if (salary_amount_paid.equals(""))
                {
                    Toast.makeText(manage_salary.this, "Enter paying salary", Toast.LENGTH_SHORT).show();
                }

                else {

                    long a = mydb.add_salary_details(employee_id, sa_date, mont_str,year_str, salary_amount_paid, sal_balance);


//                    Toast.makeText(manage_salary.this, String.valueOf(a), Toast.LENGTH_SHORT).show();

                    alertDialog.dismiss();

                    Load_salary_details();
                }
            }
        });


        final List<String> array = new ArrayList<String>();
        final LinkedHashMap<String, String> lH = new LinkedHashMap<String, String>();
        int i = 0;
        while (employee_list.moveToNext()) {
             emp_name = employee_list.getString(employee_list.getColumnIndex("emp_name"));
             emp_id = employee_list.getString(employee_list.getColumnIndex("emp_id"));
            array.add(emp_name);
            lH.put(emp_name, emp_id);

            i++;
        }


        adapter = new ArrayAdapter(manage_salary.this, android.R.layout.simple_list_item_1, array);
        employee_name_spinner.setAdapter(adapter);

        employee_name_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selection = (String) parent.getItemAtPosition(position);
                employee_id = lH.get(selection);
                Toast.makeText(manage_salary.this, selection + "\n" + employee_id, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        salary_date = dialogView.findViewById(R.id.salary_date);
        salary_paid = dialogView.findViewById(R.id.salary_paid);
        salary_balance = dialogView.findViewById(R.id.salary_balance);
        radiogroup = dialogView.findViewById(R.id.radiogroup);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {


                RadioButton radioButton = group.findViewById(checkedId);


                   ra=radioButton.getText().toString();



               if (ra.equals("Full pay")) {

                    salary_paid.setEnabled(false);
                    salary_paid.setText(seperated[0]);
                    balance_layout.setVisibility(View.GONE);


                } else {

                    salary_paid.setEnabled(true);
                    salary_balance.setText(seperated[0]);
                    balance_layout.setVisibility(View.VISIBLE);

                }


            }
        });



        salary_paid.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0)
                {
                    amnt = Integer.parseInt(amount_to_pay_text.getText().toString());
                    p_amnt = Integer.parseInt(salary_paid.getText().toString());

                    bal = amnt - p_amnt;
                    salary_balance.setText(String.valueOf(bal));


                }
                else {
                    salary_balance.setText(basic_pay.getText().toString());
                    bal= Integer.parseInt(basic_pay.getText().toString());
                    p_amnt=0;
                }



            }

        });


        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabel(year,monthOfYear);
            }

        };


        salary_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(manage_salary.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        alertDialog.show();

    }


    private void updateLabel(int year,int month) {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        salary_date.setText(sdf.format(myCalendar.getTime()));



        mont_str= String.valueOf(month+1);
        year_str= String.valueOf(year);


        if (mont_str.length()>1)
        {
            //do nothing
        }
        else {

            mont_str="0"+mont_str;
        }

       String check_salary= mydb.check_salary(employee_id,mont_str,year_str);
        seperated=check_salary.split("/");

        basic_pay.setText(seperated[1]);
        amount_to_pay_text.setText(seperated[0]);
    }



}
