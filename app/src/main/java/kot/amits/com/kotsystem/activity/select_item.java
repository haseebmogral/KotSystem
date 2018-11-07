package kot.amits.com.kotsystem.activity;

import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.animation.AnimationUtils;
import android.view.animation.LayoutAnimationController;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import kot.amits.com.kotsystem.DBhelper.DBHelper;
import kot.amits.com.kotsystem.DBhelper.DBmanager;
import kot.amits.com.kotsystem.R;
import kot.amits.com.kotsystem.category_adapter.cat_adapter;
import kot.amits.com.kotsystem.category_adapter.cat_album;
import kot.amits.com.kotsystem.items_adapter.cart_items;
import kot.amits.com.kotsystem.items_adapter.item_adapter;
import kot.amits.com.kotsystem.items_adapter.item_album;
import kot.amits.com.kotsystem.select_item_adapter.select_item_adapter;
import kot.amits.com.kotsystem.select_item_adapter.select_item_album;

import static kot.amits.com.kotsystem.DBhelper.DBHelper.cat_id;
import static kot.amits.com.kotsystem.DBhelper.DBHelper.category;
import static kot.amits.com.kotsystem.DBhelper.DBHelper.image;
import static kot.amits.com.kotsystem.DBhelper.DBHelper.item_price;

public class select_item extends AppCompatActivity {

    private select_item_adapter select_item_adapter;
    private List<select_item_album> itemlist;
    Cursor item_list_cursour;
    RecyclerView items_recycler;
    DBmanager mydb;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_item);
        setTitle("Select Items");

        mydb=new DBmanager(this);
        mydb.open();

        item_list_cursour = mydb.getitemlist();
        Toast.makeText(this, "size"+String.valueOf(item_list_cursour.getCount()), Toast.LENGTH_SHORT).show();




        items_recycler=(RecyclerView)findViewById(R.id.loaditems_recycler);

        load_item_list();



    }

    private void load_item_list() {


            itemlist = new ArrayList<>();
            select_item_adapter = new select_item_adapter(select_item.this, itemlist);


            LinearLayoutManager linearLayoutManager;
            linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, true);
            linearLayoutManager.setReverseLayout(false);
            RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(select_item.this, 5);
            items_recycler.setLayoutManager(mLayoutManager);
        items_recycler.setAdapter(select_item_adapter);

            select_item_album cat;


            while (item_list_cursour.moveToNext()) {

                String itemname = item_list_cursour.getString(item_list_cursour.getColumnIndex(DBHelper.item_name));
                String image = item_list_cursour.getString(item_list_cursour.getColumnIndex(DBHelper.image));
                String price = item_list_cursour.getString(item_list_cursour.getColumnIndex(DBHelper.item_price));

                Toast.makeText(this,"item name"+ itemname, Toast.LENGTH_SHORT).show();

                cat = new select_item_album(itemname,image,item_price);
                itemlist.add(cat);
            }


            select_item_adapter.notifyDataSetChanged();



    }

}
