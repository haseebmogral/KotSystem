package kot.amits.com.kotsystem.DBhelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

public class DBmanager {

    private DBHelper dbHelper;

    private Context context;

    public SQLiteDatabase database;



    public DBmanager(Context c) {
        context = c;
    }

    public DBmanager open() throws SQLException {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
        String path = database.getPath();
//        Toast.makeText(context, path, Toast.LENGTH_SHORT).show();
        return this;
    }

    public long insertcategory(String name, String type) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.cat_name, name);
        contentValues.put(dbHelper.cat_type, type);
        return database.insert(dbHelper.category, null, contentValues);

    }public long insertitems(String name, String cat_id, String price,String itemimage) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(dbHelper.item_name, name);
        contentValues.put(dbHelper.cat_cat_id, cat_id);
        contentValues.put(dbHelper.item_price, price);
        contentValues.put(dbHelper.image, itemimage);

        return database.insert(dbHelper.item_table, null, contentValues);

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


}

