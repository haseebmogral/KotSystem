package kot.amits.com.kotsystem.activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;


import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;

public class recyclerview_test_activity extends AppCompatActivity {
    RecyclerView itemrecycler;
    DBmanager dBmanager;
    Cursor cursor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recyclerview_test_activity);

        dBmanager=new DBmanager(this);
        dBmanager.open();
        cursor= dBmanager.getitems("1");
        Log.d("size",String.valueOf(cursor.getCount()));




        itemrecycler=findViewById(R.id.item_recycler);


    }


}
