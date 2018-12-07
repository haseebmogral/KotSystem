package kot.amits.com.kotsystem.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
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

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.manage_employee_adapter.manage_employee_adapter;
import kot.amits.com.kotsystem.manage_employee_adapter.manage_employee_model;

public class Manage_employee extends AppCompatActivity {

    private manage_employee_adapter manage_employee_adapter;
    private List<manage_employee_model> manage_employee_model;

    RecyclerView employee_recycler;
    DBmanager mydb;

    Cursor get_employee_details_cursor;
    TextView emptyview;

    EditText emp_name,emp_address,emp_salary,emp_contact;
    Spinner emp_desig;
    TextView emp_doj;
    Button add_emp;

    String desig_name;

    Calendar myCalendar = Calendar.getInstance();




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Manage Employee");
        setContentView(R.layout.activity_manage_employee);
        employee_recycler = findViewById(R.id.employees_recycler);

        emptyview = findViewById(R.id.empty);
        emptyview.setVisibility(View.INVISIBLE);


        mydb = new DBmanager(this);
        mydb.open();

        Load_employee_details();



    }

    private void Load_employee_details() {
        get_employee_details_cursor = mydb.get_employee_details();


        if (mydb.get_employee_details().getCount() <= 0) {

            Toast.makeText(this, String.valueOf(mydb.get_employee_details().getCount()), Toast.LENGTH_SHORT).show();
            emptyview.setVisibility(View.VISIBLE);

        } else {

            Toast.makeText(this, String.valueOf(mydb.get_employee_details().getCount()), Toast.LENGTH_SHORT).show();


            emptyview.setVisibility(View.INVISIBLE);


            manage_employee_model = new ArrayList<>();
            manage_employee_adapter = new manage_employee_adapter(Manage_employee.this, manage_employee_model);


            LinearLayoutManager linearLayoutManager;
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
            linearLayoutManager.setReverseLayout(false);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Manage_employee.this, 1);
//            employee_recycler.addItemDecoration(new SimpleDividerItemDecoration(this));

            employee_recycler.setLayoutManager(mLayoutManager);
            employee_recycler.setAdapter(manage_employee_adapter);


            manage_employee_model cat;

//            int i = 1;
//
//            float total = 0;


            while (get_employee_details_cursor.moveToNext()) {

                String emp_id = get_employee_details_cursor.getString(get_employee_details_cursor.getColumnIndex(DBHelper.emp_id));
                String emp_name = get_employee_details_cursor.getString(get_employee_details_cursor.getColumnIndex(DBHelper.emp_name));
                String emp_address = get_employee_details_cursor.getString(get_employee_details_cursor.getColumnIndex(DBHelper.emp_address));
                String emp_desig = get_employee_details_cursor.getString(get_employee_details_cursor.getColumnIndex(DBHelper.emp_designation));
                String emp_contact = String.valueOf(get_employee_details_cursor.getLong(get_employee_details_cursor.getColumnIndex(DBHelper.emp_contact)));
                String emp_salary = get_employee_details_cursor.getString(get_employee_details_cursor.getColumnIndex(DBHelper.emp_salary));
                String emp_doj = get_employee_details_cursor.getString(get_employee_details_cursor.getColumnIndex(DBHelper.emp_doj));
                String emp_status = String.valueOf(get_employee_details_cursor.getLong(get_employee_details_cursor.getColumnIndex(DBHelper.emp_status)));

                Log.i("status",emp_status);
                Log.i("contact",emp_contact);

                cat = new manage_employee_model(emp_id,emp_name, emp_address, emp_desig,emp_contact,emp_salary,emp_doj,emp_status);
                manage_employee_model.add(cat);

//                total = total + Float.parseFloat(amount);
//
//
//                i++;
            }


          //  String final_amount= NumberFormat.getNumberInstance(Locale.US).format(total);

//
//            DecimalFormat formatter = new DecimalFormat("####,##,#");
//            String yourFormattedString = formatter.format(total);



//            amount.setText(String.valueOf(final_amount));

            manage_employee_adapter.notifyDataSetChanged();


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
                //addfav (heart icon) was clicked, Insert your after click code here.
                return true;

            case R.id.add_expense:

                add_employee();

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void add_employee() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Manage_employee.this);

        LayoutInflater inflater = (LayoutInflater) Manage_employee.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.add_employee_layout, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog;
        alertDialog = dialogBuilder.create();
        dialogBuilder.setTitle(R.string.add_purchase_details);

        emp_name = dialogView.findViewById(R.id.emp_name);
        emp_address = dialogView.findViewById(R.id.emp_address);
        emp_desig = dialogView.findViewById(R.id.emp_desig);
        emp_salary = dialogView.findViewById(R.id.emp_salary);
        emp_doj = dialogView.findViewById(R.id.emp_doj);
        emp_contact = dialogView.findViewById(R.id.emp_contact);
        add_emp = dialogView.findViewById(R.id.add_emp);



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


        emp_doj.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new DatePickerDialog(Manage_employee.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });

        List<String> expense_type = new ArrayList<String>();

        expense_type.add("select designation");
        expense_type.add("Baker");
        expense_type.add("Broiler cook");
        expense_type.add("Executive chef");
        expense_type.add("Food and  beverage director");
        expense_type.add("General manager");
        expense_type.add("Kitchen manager");
        expense_type.add("Server");
        expense_type.add("Others");
        // Creating adapter for spinner
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, expense_type);

        // Drop down layout style - list view with radio button
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        // attaching data adapter to spinner
        emp_desig.setAdapter(dataAdapter);

        emp_desig.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                desig_name = parent.getItemAtPosition(position).toString();

                Toast.makeText(Manage_employee.this, desig_name, Toast.LENGTH_SHORT).show();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {




            }
        });


        add_emp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String e_name = emp_name.getText().toString();
                String e_address = emp_address.getText().toString();
                String e_desig = desig_name;
                String e_salary = emp_salary.getText().toString();
                String e_doj = emp_doj.getText().toString();
                String e_contact = emp_contact.getText().toString();



                if (e_name.equals("")) {

                    Snackbar.make(v, "Please enter employee name", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else if (e_address.equals("")) {
                    Snackbar.make(v, "please enter employee address", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (e_desig.equals("select designation")) {
                    Snackbar.make(v, "Please enter designation", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (e_salary.equals("")) {
                    Toast.makeText(Manage_employee.this, "Please enter salary", Toast.LENGTH_SHORT).show();
                } else if (e_doj.equals("Select date of join")) {
                    Toast.makeText(Manage_employee.this, "Please select date of join", Toast.LENGTH_SHORT).show();
                } else if (e_contact.equals("")) {
                    Toast.makeText(Manage_employee.this, "Please enter contact number", Toast.LENGTH_SHORT).show();
                } else {


                    long add = mydb.add_employee(e_name, e_address, e_desig, e_salary,e_doj,e_contact);

                    if (String.valueOf(add).equals("-1")) {
                        Toast.makeText(Manage_employee.this, "Please try again", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(Manage_employee.this, "Expense details added", Toast.LENGTH_SHORT).show();

                        alertDialog.dismiss();
                        Load_employee_details();


                    }
                }

            }
        });

        alertDialog.show();

    }

    private void updateLabel() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        emp_doj.setText(sdf.format(myCalendar.getTime()));
    }

}
