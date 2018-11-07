package kot.amits.com.kotsystem.DBhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import kot.amits.com.kotsystem.items_adapter.cart_items;

public class DBmanager {

    private DBHelper dbHelper;

    private Context context;

    public SQLiteDatabase database;

    public static String CART_ID="";



    public DBmanager(Context c) {
        context = c;
    }

    public DBmanager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        String path = database.getPath();
        Toast.makeText(context, path, Toast.LENGTH_SHORT).show();
        return this;
    }

    public long insertcategory(String name, String type) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.cat_name, name);
        contentValues.put(dbHelper.cat_type, type);
        return database.insert(dbHelper.category, null, contentValues);
    }
    public long insertitems(String name, String cat_id, String price,String itemimage) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.item_name, name);
        contentValues.put(dbHelper.cat_cat_id, cat_id);
        contentValues.put(dbHelper.item_price, price);
        contentValues.put(dbHelper.image, itemimage);

        return database.insert(dbHelper.item_table, null, contentValues);

    }

    public long add_to_cart_details() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.date, get_date());
        contentValues.put(dbHelper.time, get_time());
        contentValues.put(dbHelper.status,"1");

        return database.insert(dbHelper.cart_details, null, contentValues);

    }



    //get category

    public Cursor getData(String type) {
        Cursor res = database.rawQuery("select * from category  where "+dbHelper.cat_type+"=?", new String[]{type});


        return res;
    }

    //get items

    public Cursor getitems(String category_id)
    {

        Cursor itemdetails=database.rawQuery("select * from item_table where "+dbHelper.cat_cat_id+" =?",new String[]{category_id});
        return itemdetails;
    }
    public Cursor get_ongoing_orders(){
        Cursor cursor=database.rawQuery("select * from cart_details where "+dbHelper.status+" =?",new String[]{"0"});
        return cursor;
    }
    public Cursor get_sent_to_kitchen_order(){
        Cursor cursor=database.rawQuery("select * from cart_details where "+dbHelper.status+" =?",new String[]{"1"});
        return cursor;
    }
    public Cursor get_finished_orders(){
        Cursor cursor=database.rawQuery("select * from cart_details where "+dbHelper.status+" =?",new String[]{"1"});
        return cursor;
    }
    public Cursor get_cancelled_orders(){
        Cursor cursor=database.rawQuery("select * from cart_details where "+dbHelper.status+" =?",new String[]{"1"});
        return cursor;
    }
    public void place_order(List<cart_items> cart){
        ContentValues contentValues = new ContentValues();
        for (int i=0;i<cart.size();i++){
            contentValues.put(dbHelper.cart_details_id, CART_ID);
            contentValues.put(dbHelper.c_item_id,cart.get(i).getItem_id());
            contentValues.put(dbHelper.c_qty,cart.get(i).get_qty());
            contentValues.put(dbHelper.c_total,cart.get(i).get_total());
            contentValues.put(dbHelper.c_item_order_status,"cart");

           long a= database.insert(dbHelper.cart_items_table, null, contentValues);
            Toast.makeText(context, String.valueOf(a), Toast.LENGTH_SHORT).show();
//            Toast.makeText(context, cart.get(i).getItem_id()+"\n"+
//                    cart.get(i).get_name()+"\n"+
//                    cart.get(i).get_qty()+"\n"+
//                    cart.get(i).get_price()+"\n"+
//                    cart.get(i).get_total()+"\n", Toast.LENGTH_SHORT).show();
        }

    }

    public String get_date(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        String formattedDate = df.format(c);
        return formattedDate;
    }



    public String get_time(){
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm");
        String strDate = mdformat.format(calendar.getTime());
        return strDate;

    }

    public Cursor getitemlist()
    {
        Cursor getitemlist=database.rawQuery("select * from item_table",new String[]{});
        return getitemlist;
    }

    public String add_space(int length,String str){
        if (str.length()<length){
            int count=length-str.length();
            for (int c=0;c<count;c++){
                str=str+" " ;
            }
        }
        return str;

    }
//mamas pokkle

}

