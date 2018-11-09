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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
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
import java.util.List;
import java.util.Locale;

import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.purchase_adapter.purchase_adapter;
import kot.amits.com.kotsystem.purchase_adapter.purchase_model;
import kot.amits.com.kotsystem.select_item_adapter.select_item_album;

import static kot.amits.com.kotsystem.DBhelper.DBHelper.item_price;

public class Purchase_Activity extends AppCompatActivity {

    FloatingActionButton add_purchase;
    RecyclerView Purchase_Recycler;
    ArrayAdapter adapter;

    private purchase_adapter purchase_adapter;
    private List<purchase_model> purchaseModelList;
    DBmanager mydb;
    Cursor purchase_list_Cursor;
    TextView emptyview;
    Button select_date;
    Calendar myCalendar = Calendar.getInstance();
    RadioGroup payment_mode_radio;
    EditText paid_amount;


    String[] languages = {"Android ", "java", "IOS", "SQL", "JDBC", "Web services"};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_);
        setTitle(R.string.purchase);

         adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, languages); //supplier name adapter


        Purchase_Recycler = findViewById(R.id.purchase_recycler);


        emptyview = findViewById(R.id.empty);
        emptyview.setVisibility(View.INVISIBLE);

        mydb = new DBmanager(this);

        mydb.open();
        purchase_list_Cursor = mydb.getpurchase_details();


        if (mydb.getpurchase_details().getCount() <= 0) {

            Toast.makeText(this, String.valueOf(mydb.getpurchase_details().getCount()), Toast.LENGTH_SHORT).show();
            emptyview.setVisibility(View.VISIBLE);

        } else {

            emptyview.setVisibility(View.INVISIBLE);
            Load_purchase_details();
            Toast.makeText(this, "data stored" + String.valueOf(mydb.getpurchase_details().getCount()), Toast.LENGTH_SHORT).show();


        }


        add_purchase = findViewById(R.id.add_purchase);
        add_purchase.setOnClickListener(new View.OnClickListener() {

            AutoCompleteTextView suplierName;


            @Override
            public void onClick(View view) {



                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Purchase_Activity.this);

                LayoutInflater inflater = (LayoutInflater) Purchase_Activity.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                final View dialogView = inflater.inflate(R.layout.add_purchase_layout, null);
                dialogBuilder.setView(dialogView);
                dialogBuilder.setTitle(R.string.add_purchase_details);

                suplierName = dialogView.findViewById(R.id.suplierName);
                select_date=dialogView.findViewById(R.id.purchase_date);
                payment_mode_radio=dialogView.findViewById(R.id.payment_mode_radio);
                paid_amount=dialogView.findViewById(R.id.paid_amount);

                payment_mode_radio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(RadioGroup group, int checkedId) {
                        RadioButton checkedRadioButton = (RadioButton)group.findViewById(checkedId);
                        boolean isChecked = checkedRadioButton.isChecked();

                        switch(dialogView.getId()) {
                            case R.id.cash:
                                if (isChecked)

                                    paid_amount.setVisibility(View.INVISIBLE);
                                    break;
                            case R.id.credit:
                                if (isChecked)

                                    paid_amount.setVisibility(View.VISIBLE);
                                    break;
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
                suplierName.setAdapter(adapter);


                AlertDialog alertDialog = dialogBuilder.create();
                alertDialog.show();




            }
        });


    }

    private void updateLabel() {
        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        select_date.setText(sdf.format(myCalendar.getTime()));
    }

    private void Load_purchase_details() {

        purchaseModelList = new ArrayList<>();
        purchase_adapter = new purchase_adapter(Purchase_Activity.this, purchaseModelList);


        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setReverseLayout(false);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Purchase_Activity.this, 1);
        Purchase_Recycler.setLayoutManager(mLayoutManager);
        Purchase_Recycler.setAdapter(purchase_adapter);

        purchase_model cat;


        while (purchase_list_Cursor.moveToNext()) {

            String pdate = purchase_list_Cursor.getString(purchase_list_Cursor.getColumnIndex(DBHelper.p_id));
            String p_descr = purchase_list_Cursor.getString(purchase_list_Cursor.getColumnIndex(DBHelper.p_description));
            String p_amount = purchase_list_Cursor.getString(purchase_list_Cursor.getColumnIndex(DBHelper.p_amount));

            Toast.makeText(this, "item name" + p_descr, Toast.LENGTH_SHORT).show();

            cat = new purchase_model(p_descr);
            purchaseModelList.add(cat);
        }


        purchase_adapter.notifyDataSetChanged();

    }

    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");

        finish();
    }
}
