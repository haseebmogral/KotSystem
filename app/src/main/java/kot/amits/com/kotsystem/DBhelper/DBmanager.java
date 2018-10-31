package kot.amits.com.kotsystem.DBhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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

    public long add_to_cart_details(String date,String time) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.date, get_date());
        contentValues.put(dbHelper.time, get_time());
        contentValues.put(dbHelper.status,"0");

        return database.insert(dbHelper.cart_details, null, contentValues);

    }



    //get category

    public Cursor getData(String type) {
        Cursor res = database.rawQuery("select * from category  where "+dbHelper.cat_type+"=?", new String[]{type});


        return res;
    }

    //get items

    public Cursor getitems()
    {

        Cursor itemdetails=database.rawQuery("select * from item_table",new String[] {});
        return itemdetails;
    }

    public String get_date(){
        Date c = Calendar.getInstance().getTime();
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        String formattedDate = df.format(c);
        return formattedDate;
    }

    public String get_time(){
        Date currentTime = Calendar.getInstance().getTime();
        String time= String.valueOf(currentTime);
        return time;

    }


}

