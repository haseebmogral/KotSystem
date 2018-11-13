package kot.amits.com.kotsystem.activity;

import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Toast;

import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;

public class Add_supplier extends AppCompatActivity {
    Button AddSuplier;
    EditText supplier_name, supplier_address, supplier_contact;
    DBmanager mydb;
    Snackbar snackbar;
    CoordinatorLayout supplier_layout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_supplier);
        setTitle(R.string.add_suplier);
        AddSuplier = findViewById(R.id.add_supplier);
        supplier_contact = findViewById(R.id.supplier_contact);
        supplier_address = findViewById(R.id.supplier_address);
        supplier_name = findViewById(R.id.supplier_name);
        supplier_layout = findViewById(R.id.supplier_layout);

        //db initialization
        mydb = new DBmanager(this);
        mydb.open();

        AddSuplier.setOnClickListener(new View.OnClickListener() {
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
                } else {
                    long add = mydb.add_suppliers(suppliername, supplieraddress, suppliercontact);

                    if (String.valueOf(add).equals("-1")) {
                        Toast.makeText(Add_supplier.this, "Please try again", Toast.LENGTH_SHORT).show();

                    } else {

                        Toast.makeText(Add_supplier.this, "Supplier details added", Toast.LENGTH_SHORT).show();
                        finish();


                    }
                }


            }
        });


    }
}
