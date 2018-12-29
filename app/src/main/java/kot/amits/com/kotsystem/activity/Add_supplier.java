package kot.amits.com.kotsystem.activity;

import android.app.DatePickerDialog;
import android.content.Context;
import android.database.Cursor;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.purchase_adapter.purchase_adapter;
import kot.amits.com.kotsystem.purchase_adapter.purchase_model;
import kot.amits.com.kotsystem.view_supplier_adapter.view_supplier_adapter;
import kot.amits.com.kotsystem.view_supplier_adapter.view_supplier_model;

public class Add_supplier extends AppCompatActivity implements View.OnClickListener {
    EditText supplier_name, supplier_address, supplier_contact;
    DBmanager mydb;
    Snackbar snackbar;
    CoordinatorLayout supplier_layout;
    Cursor get_supplier_details;
    RecyclerView supplier_recycler;

    private view_supplier_adapter view_supplier_adapter;
    private List<view_supplier_model> view_supplier_model;
    FloatingActionButton add_supplier;
    Button add_supplier_details;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_add_supplier);
        setTitle(R.string.add_suplier);

        supplier_layout = findViewById(R.id.supplier_layout);
        supplier_recycler = findViewById(R.id.supplier_recycler);
        add_supplier = findViewById(R.id.add_supplier);

        add_supplier.setOnClickListener(this);

        supplier_recycler.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (dy > 0 || dy < 0 && add_supplier.isShown())
                    add_supplier.hide();
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

                if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                    add_supplier.show();
                }
                super.onScrollStateChanged(recyclerView, newState);
            }
        });


        mydb = new DBmanager(this);
        mydb.open();

        Load_supplier_details();


//        AddSuplier.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String suppliername = supplier_name.getText().toString();
//                String supplieraddress = supplier_address.getText().toString();
//                String suppliercontact = supplier_contact.getText().toString();
//
//
//                if (suppliername.equals("")) {
//
//                    Snackbar.make(v, "Enter Supplier Name", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//
//                } else if (supplieraddress.equals("")) {
//                    Snackbar.make(v, "Enter Supplier address", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                } else if (suppliercontact.equals("")) {
//                    Snackbar.make(v, "Enter Supplier contact", Snackbar.LENGTH_LONG)
//                            .setAction("Action", null).show();
//                } else {
//                    long add = mydb.add_suppliers(suppliername, supplieraddress, suppliercontact);
//
//                    if (String.valueOf(add).equals("-1")) {
//                        Toast.makeText(Add_supplier.this, "Please try again", Toast.LENGTH_SHORT).show();
//
//                    } else {
//
//                        Toast.makeText(Add_supplier.this, "Supplier details added", Toast.LENGTH_SHORT).show();
//                        finish();
//
//
//                    }
//                }
//
//
//            }
//        });


    }

    private void Load_supplier_details() {


        get_supplier_details = mydb.get_supplier_details();


        if (mydb.get_supplier_details().getCount() <= 0) {

            Toast.makeText(this, String.valueOf(mydb.get_supplier_details().getCount()), Toast.LENGTH_SHORT).show();
            //emptyview.setVisibility(View.VISIBLE);

        } else {

            view_supplier_model = new ArrayList<>();
            view_supplier_adapter = new view_supplier_adapter(Add_supplier.this, view_supplier_model);


            LinearLayoutManager linearLayoutManager;
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
            linearLayoutManager.setReverseLayout(false);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Add_supplier.this, 1);
            supplier_recycler.setLayoutManager(mLayoutManager);
            supplier_recycler.setAdapter(view_supplier_adapter);

            view_supplier_model cat;


            while (get_supplier_details.moveToNext()) {

                String supplier_name = get_supplier_details.getString(get_supplier_details.getColumnIndex(DBHelper.supplier_name));
                String supplier_address = get_supplier_details.getString(get_supplier_details.getColumnIndex(DBHelper.supplier_address));
                String supplier_contact = String.valueOf(get_supplier_details.getLong(get_supplier_details.getColumnIndex(DBHelper.supplier_contact)));
                Toast.makeText(this, "taking value is "+
                        supplier_contact, Toast.LENGTH_SHORT).show();
//                Toast.makeText(this, "item name" + p_descr, Toast.LENGTH_SHORT).show();

                cat = new view_supplier_model(supplier_name, supplier_address, supplier_contact);
                view_supplier_model.add(cat);
            }


            view_supplier_adapter.notifyDataSetChanged();
        }

    }

    @Override
    public void onClick(View v) {

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Add_supplier.this);

        LayoutInflater inflater = (LayoutInflater) Add_supplier.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        final View dialogView = inflater.inflate(R.layout.add_supplier_details, null);
        dialogBuilder.setView(dialogView);
        final AlertDialog alertDialog;
        alertDialog = dialogBuilder.create();
        dialogBuilder.setTitle(R.string.add_purchase_details);

        supplier_contact = dialogView.findViewById(R.id.supplier_contact);

        supplier_address = dialogView.findViewById(R.id.supplier_address);
        supplier_name = dialogView.findViewById(R.id.supplier_name);
        add_supplier_details = dialogView.findViewById(R.id.add_supplier_details);

        add_supplier_details.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                String suppliername = supplier_name.getText().toString();
                String supplieraddress = supplier_address.getText().toString();
                String suppliercontact = supplier_contact.getText().toString();


                if (suppliername.equals("")) {

                    Snackbar.make(v, "Enter Supplier Name", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                } else if (supplieraddress.equals("")) {
                    Snackbar.make(v, "Enter Supplier address", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                } else if (suppliercontact.equals("")) {
                    Snackbar.make(v, "Enter Supplier contact", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
                else if (supplier_contact.getText().toString().length()<10) {
                    Snackbar.make(v, "Enter valid contact number", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }else {
                    long add = mydb.add_suppliers(suppliername, supplieraddress, suppliercontact);

                    if (String.valueOf(add).equals("-1")) {
                        Toast.makeText(Add_supplier.this, "Please try again", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(Add_supplier.this, "Supplier details added", Toast.LENGTH_SHORT).show();

                        alertDialog.dismiss();
                        Load_supplier_details();


                    }
                }

            }
        });

        alertDialog.show();

    }

}





//    @Override
//    public void onClick(View v) {
//
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Add_supplier.this);
//
//        LayoutInflater inflater = (LayoutInflater) Add_supplier.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View dialogView = inflater.inflate(R.layout.add_supplier_details, null);
//        final AlertDialog alertDialog;
//        alertDialog = dialogBuilder.create();
//        dialogBuilder.setTitle(R.string.add_supplier);
//
//
//        supplier_contact = dialogView.findViewById(R.id.supplier_contact);
//        supplier_address = dialogView.findViewById(R.id.supplier_address);
//        supplier_name = dialogView.findViewById(R.id.supplier_name);
//        add_supplier_details=dialogView.findViewById(R.id.add_supplier_details);
//
//
//
//
//



