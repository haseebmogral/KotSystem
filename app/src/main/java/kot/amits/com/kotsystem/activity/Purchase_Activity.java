package kot.amits.com.kotsystem.activity;

import android.content.Intent;
import android.database.Cursor;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.purchase_adapter.purchase_adapter;
import kot.amits.com.kotsystem.purchase_adapter.purchase_model;
import kot.amits.com.kotsystem.select_item_adapter.select_item_album;

import static kot.amits.com.kotsystem.DBhelper.DBHelper.item_price;

public class Purchase_Activity extends AppCompatActivity {

    FloatingActionButton Fab;
    RecyclerView Purchase_Recycler;

    private purchase_adapter purchase_adapter;
    private List<purchase_model> purchaseModelList;
    DBmanager mydb;
    Cursor purchase_list_Cursor;
    TextView emptyview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_);
        //delete this ydtrerestrdyfuysretdyfui
        setTitle(R.string.purchase);

        Purchase_Recycler = findViewById(R.id.purchase_recycler);


        emptyview = findViewById(R.id.empty);
        emptyview.setVisibility(View.INVISIBLE);

        mydb = new DBmanager(this);

        mydb.open();
        purchase_list_Cursor=mydb.getpurchase_details();



        if (mydb.getpurchase_details().getCount()<=0) {

            Toast.makeText(this,String.valueOf(mydb.getpurchase_details().getCount()) , Toast.LENGTH_SHORT).show();
            emptyview.setVisibility(View.VISIBLE);

        } else {

            emptyview.setVisibility(View.INVISIBLE);
            Load_purchase_details();
            Toast.makeText(this,"data stored"+String.valueOf(mydb.getpurchase_details().getCount()) , Toast.LENGTH_SHORT).show();




        }


        Fab = findViewById(R.id.fab);
        Fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


            }
        });



    }

    private void Load_purchase_details() {

        purchaseModelList = new ArrayList<>();
        purchase_adapter = new purchase_adapter(Purchase_Activity.this, purchaseModelList);


        LinearLayoutManager linearLayoutManager;
        linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true);
        linearLayoutManager.setReverseLayout(false);
        Purchase_Recycler.setLayoutManager(new LinearLayoutManager(this));

//        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(Purchase_Activity.this, 5);
//        Purchase_Recycler.setLayoutManager(mLayoutManager);
        Purchase_Recycler.setAdapter(purchase_adapter);

        purchase_model cat;


        while (purchase_list_Cursor.moveToNext()) {

            String pdate = purchase_list_Cursor.getString(purchase_list_Cursor.getColumnIndex(DBHelper.p_id));
            String p_descr = purchase_list_Cursor.getString(purchase_list_Cursor.getColumnIndex(DBHelper.p_description));
            String p_amount = purchase_list_Cursor.getString(purchase_list_Cursor.getColumnIndex(DBHelper.p_amount));

            Toast.makeText(this,"item name"+ p_descr, Toast.LENGTH_SHORT).show();

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
