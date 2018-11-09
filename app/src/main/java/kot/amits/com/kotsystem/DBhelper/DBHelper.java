package kot.amits.com.kotsystem.DBhelper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "restaurant_kot_system.db";
    //category table
    public static final String category = "category";
    public static final String cat_id = "cat_id";
    public static final String cat_name = "cat_name";
    public static final String cat_type = "cat_type";

    //item table
    public static final String item_table  = "item_table ";
    public static final String item_id = "item_id";
    public static final String item_name = "item_name";
    public static final String cat_cat_id = "cat_cat_id";
    public static final String item_price = "item_price";
    public static final String image = "image";

    //cart_details table
    public static final String cart_details  = "cart_details ";
    public static final String cart_id = "cart_id";
    public static final String customer_mob_no = "customer_mob_no";
    public static final String date = "date";
    public static final String time = "time";
    public static final String status = "status";
    public static final String upload_status = "upload_status";

    //cart_items table
    public static final String cart_items_table  = "cart_items_table ";
    public static final String c_i_id = "c_i_id";
    public static final String cart_details_id = "cart_details_id";
    public static final String c_item_id = "c_item_id";
    public static final String c_qty = "c_qty";
    public static final String c_total = "c_total";
    public static final String c_item_order_status = "c_item_order_status";

    //purchase table
    public static final String purchase_table  = "purchase_table ";
    public static final String p_id = "p_id";
    public static final String p_date = "p_date";
    public static final String p_description = "p_description";
    public static final String p_amount = "p_amount";
    public static final String p_upload_status = "p_upload_status";


    //supplier table
    public static final String supplier_table  = "supplier_table ";
    public static final String supplier_id = "supplier_id";
    public static final String supplier_name = "supplier_name";
    public static final String supplier_address = "supplier_address";
    public static final String supplier_contact = "supplier_contact";
    public static final String supplier_upload_status = "supplier_upload_status";








    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static final String CREATE_CAT_TABLE="CREATE TABLE `category` (`cat_id`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,`cat_name` TEXT NOT NULL,`cat_type` TEXT NOT NULL);";
    public static final String CREATE_ITEM_TABLE="CREATE TABLE `item_table` (`item_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `item_name` TEXT NOT NULL, `cat_cat_id` INTEGER NOT NULL, `item_price` REAL NOT NULL ,`image` REAL NOT NULL);";
    public static final String CREATE_CART_DETAILS_TABLE="CREATE TABLE `cart_details` (`cart_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,`customer_mob_no` REAL,`date` REAL NOT NULL,`time` REAL NOT NULL,`status` TEXT NOT NULL);";
    public static final String CREATE_CART_ITEMS_TABLE="CREATE TABLE `cart_items_table` (`c_i_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,`cart_details_id` INTEGER NOT NULL,`c_item_id` INTEGER NOT NULL,`c_qty` INTEGER NOT NULL,`c_total` REAL NOT NULL,`c_item_order_status` TEXT NOT NUll );";
    public static final String CREATE_PURCHASE_TABLE="CREATE TABLE `purchase_table` (`p_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,`p_date` REAL NOT NULL,`p_description` REAL NOT NULL,`p_amount` REAL NOT NULL,`p_upload_status` TEXT NOT NULL );";
    public static final String CREATE_SUPPLIER_TABLE="CREATE TABLE `supplier_table` (`supplier_id` INTEGER NOT NULL,`supplier_name` TEXT NOT NULL,`supplier_address` TEXT NOT NULL,`supplier_contact` REAL,`supplier_upload_status` TEXT,PRIMARY KEY(`supplier_id`));";


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_CAT_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(CREATE_CART_DETAILS_TABLE);
        db.execSQL(CREATE_CART_ITEMS_TABLE);
        db.execSQL(CREATE_PURCHASE_TABLE);
        db.execSQL(CREATE_SUPPLIER_TABLE);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS category");
        db.execSQL("DROP TABLE IF EXISTS item_table");
        db.execSQL("DROP TABLE IF EXISTS cart_details");
        db.execSQL("DROP TABLE IF EXISTS cart_items_table");
        db.execSQL("DROP TABLE IF EXISTS purchase_table");
        db.execSQL("DROP TABLE IF EXISTS supplier_table");

        onCreate(db);
    }




//
//    public int numberOfRows() {
//        SQLiteDatabase db = this.getReadableDatabase();
//        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
//        return numRows;
//    }
//
//    public boolean updateContact(Integer id, String name, String phone, String email, String street, String place) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        ContentValues contentValues = new ContentValues();
//        contentValues.put("name", name);
//        contentValues.put("phone", phone);
//        contentValues.put("email", email);
//        contentValues.put("street", street);
//        contentValues.put("place", place);
//        db.update("contacts", contentValues, "id = ? ", new String[]{Integer.toString(id)});
//        return true;
//    }
//
//    public Integer deleteContact(Integer id) {
//        SQLiteDatabase db = this.getWritableDatabase();
//        return db.delete("contacts",
//                "id = ? ",
//                new String[]{Integer.toString(id)});
//    }
//
//    public ArrayList<String> getAllCotacts() {
//        ArrayList<String> array_list = new ArrayList<String>();
//
//        //hp = new HashMap();
//        SQLiteDatabase db = this.getReadableDatabase();
//        Cursor res = db.rawQuery("select * from contacts", null);
//        res.moveToFirst();
//
//        while (res.isAfterLast() == false) {
//            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));
//            res.moveToNext();
//        }
//        return array_list;
//    }
}