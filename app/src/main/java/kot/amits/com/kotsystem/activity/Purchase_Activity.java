package kot.amits.com.kotsystem.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
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
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
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
import kot.amits.com.kotsystem.purchase_adapter.purchase_adapter;
import kot.amits.com.kotsystem.purchase_adapter.purchase_model;
import kot.amits.com.kotsystem.select_item_adapter.select_item_album;

import static kot.amits.com.kotsystem.DBhelper.DBHelper.item_price;
import static kot.amits.com.kotsystem.DBhelper.DBHelper.p_bal;
import static kot.amits.com.kotsystem.DBhelper.DBHelper.supplier_name;

public class Purchase_Activity extends AppCompatActivity {

    FloatingActionButton add_purchase;
    RecyclerView Purchase_Recycler;
    ArrayAdapter adapter;

    private purchase_adapter purchase_adapter;
    private List<purchase_model> purchaseModelList;
    DBmanager mydb;
    Cursor purchase_list_Cursor, supplier_list;
    TextView emptyview;
    Button select_date;
    Calendar myCalendar = Calendar.getInstance();
    RadioGroup payment_mode_radio;
    EditText paid_amount, p_description, amount;
    Button add_purchase_details;
    String[] supplier_id;
    String sid;
    int amnt = 0, p_amnt = 0, bal = 0;
    TextView total,paid,balance;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_);
        setTitle(R.string.purchase);

        Purchase_Recycler = findViewById(R.id.purchase_recycler);
        total=findViewById(R.id.total);
        paid=findViewById(R.id.paid);
        balance=findViewById(R.id.balance);


        emptyview = findViewById(R.id.empty);
        emptyview.setVisibility(View.INVISIBLE);

        mydb = new DBmanager(this);
        mydb.open();

        Load_purchase_details();


        add_purchase = findViewById(R.id.add_purchase);
        add_purchase.setOnClickListener(new View.OnClickListener() {


            @Override
            public void onClick(View view) {
                supplier_list = mydb.getSuppliername();
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Purchase_Activity.this);
                final TextView p_bal;
                AutoCompleteTextView suplierName;


                LayoutInflater inflater = (LayoutInflater) Purchase_Activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.add_purchase_layout, null);
                dialogBuilder.setView(dialogView);
                final AlertDialog alertDialog;
                alertDialog = dialogBuilder.create();
                dialogBuilder.setTitle(R.string.add_purchase_details);


                suplierName = dialogView.findViewById(R.id.suplierName);
                select_date = dialogView.findViewById(R.id.purchase_date);
                add_purchase_details = dialogView.findViewById(R.id.add_purchase_details);
                p_description = dialogView.findViewById(R.id.description);
                amount = dialogView.findViewById(R.id.amount);
                p_bal = dialogView.findViewById(R.id.balance);
                paid_amount = dialogView.findViewById(R.id.paid_amount);


                paid_amount.addTextChangedListener(new TextWatcher() {

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




                            if (paid_amount.getText().toString().equals("") || paid_amount.getText().toString().equals(null))
                            {



                                paid_amount.setText("0");
                                p_bal.setText("0");

                            }



                            else {



                                amnt = Integer.parseInt(amount.getText().toString());
                                p_amnt = Integer.parseInt(paid_amount.getText().toString());

                                bal = amnt - p_amnt;
                                p_bal.setText(String.valueOf(bal));

                            }

                    }
                });


                //getting supplier name to array
                final List<String> array = new ArrayList<String>();
                final LinkedHashMap<String, String> lH = new LinkedHashMap<String, String>();
                int i = 0;
                while (supplier_list.moveToNext()) {
                    String s_name = supplier_list.getString(supplier_list.getColumnIndex("supplier_name"));
                    String supplierid = supplier_list.getString(supplier_list.getColumnIndex("supplier_id"));
                    array.add(s_name);
                    lH.put(s_name, supplierid);

                    i++;
                }

                adapter = new ArrayAdapter(Purchase_Activity.this, android.R.layout.simple_list_item_1, array);
                suplierName.setAdapter(adapter);
                //end of adapter
                suplierName.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        String selection = (String) parent.getItemAtPosition(position);
                        sid = lH.get(selection);
                        Toast.makeText(Purchase_Activity.this, selection + "\n" + sid, Toast.LENGTH_SHORT).show();

                    }
                });
                add_purchase_details.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        String date = select_date.getText().toString();
                        String descr = p_description.getText().toString();
                        String amount1 = amount.getText().toString();
                        String paidamount = paid_amount.getText().toString();
                        String pbalance = p_bal.getText().toString();


                        if (date.equals("Select date of purchase"))
                        {
                            Toast.makeText(Purchase_Activity.this, "Enter Purchase date", Toast.LENGTH_SHORT).show();
                        }
                        else  if (descr.equals(""))
                        {
                            Toast.makeText(Purchase_Activity.this, "Enter description", Toast.LENGTH_SHORT).show();
                        }
                        else if (amount1.equals(""))
                        {
                            Toast.makeText(Purchase_Activity.this, "Enter amount", Toast.LENGTH_SHORT).show();
                        }
                        else if (paidamount.equals("")){
                            Toast.makeText(Purchase_Activity.this, "Enter paid amount", Toast.LENGTH_SHORT).show();
                        }
                        else {


                            long a = mydb.addPurchase(sid, date, descr, amount1,paidamount,pbalance);


                            Toast.makeText(Purchase_Activity.this, String.valueOf(a), Toast.LENGTH_SHORT).show();

                            alertDialog.dismiss();

                            Load_purchase_details();
                        }





//                        sales_adapter.notifyDataSetChanged();


                    }
                });

                paid_amount = dialogView.findViewById(R.id.paid_amount);


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

                select_date.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {


                        new DatePickerDialog(Purchase_Activity.this, date, myCalendar
                                .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                                myCalendar.get(Calendar.DAY_OF_MONTH)).show();


                    }
                });


                alertDialog.show();


            }
        });


    }

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        select_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void Load_purchase_details() {

        purchase_list_Cursor = mydb.getpurchase_details();


        if (mydb.getpurchase_details().getCount() <= 0) {

            Toast.makeText(this, String.valueOf(mydb.getpurchase_details().getCount()), Toast.LENGTH_SHORT).show();
            emptyview.setVisibility(View.VISIBLE);

        } else {

            emptyview.setVisibility(View.INVISIBLE);
//            Toast.makeText(this, "data stored" + String.valueOf(mydb.getpurchase_details().getCount()), Toast.LENGTH_SHORT).show();


            purchaseModelList = new ArrayList<>();
            purchase_adapter = new purchase_adapter(Purchase_Activity.this, purchaseModelList,total,paid,balance);


            LinearLayoutManager linearLayoutManager;
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
            linearLayoutManager.setReverseLayout(false);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Purchase_Activity.this, 1);
            Purchase_Recycler.setLayoutManager(mLayoutManager);
            Purchase_Recycler.setAdapter(purchase_adapter);


            Purchase_Recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    if (dy > 0 || dy < 0 && add_purchase.isShown())
                        add_purchase.hide();
                }

                @Override
                public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        add_purchase.show();
                    }
                    super.onScrollStateChanged(recyclerView, newState);
                }
            });

            purchase_model cat;


            while (purchase_list_Cursor.moveToNext()) {
                String s_name = purchase_list_Cursor.getString(purchase_list_Cursor.getColumnIndex(DBHelper.supplier_name));

                String pdate = purchase_list_Cursor.getString(purchase_list_Cursor.getColumnIndex(DBHelper.p_date));
                String p_descr = purchase_list_Cursor.getString(purchase_list_Cursor.getColumnIndex(DBHelper.p_description));
                String p_amount = purchase_list_Cursor.getString(purchase_list_Cursor.getColumnIndex(DBHelper.p_amount));
                String p_paid = purchase_list_Cursor.getString(purchase_list_Cursor.getColumnIndex(DBHelper.p_paid));
                String balance = purchase_list_Cursor.getString(purchase_list_Cursor.getColumnIndex(DBHelper.p_bal));

//                Toast.makeText(this, "item name" + p_descr, Toast.LENGTH_SHORT).show();

                cat = new purchase_model(s_name, pdate, p_descr, p_amount,p_paid,balance);
                purchaseModelList.add(cat);
            }


            purchase_adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");

        finish();
    }
}
