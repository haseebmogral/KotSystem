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
    public static final String item_table = "item_table ";
    public static final String item_id = "item_id";
    public static final String item_name = "item_name";
    public static final String cat_cat_id = "cat_cat_id";
    public static final String item_price = "item_price";
    public static final String image = "image";

    //cart_details table
    public static final String cart_details = "cart_details ";
    public static final String cart_id = "cart_id";
    public static final String cart_customer_id = "cart_customer_id";
    public static final String date = "date";
    public static final String time = "time";
    public static final String cart_status = "cart_status";
    public static final String total = "total";
    public static final String cart_type = "cart_type";
    public static final String upload_status = "upload_status";

    //cart_items table
    public static final String cart_items_table = "cart_items_table ";
    public static final String c_i_id = "c_i_id";
    public static final String cart_details_id = "cart_details_id";
    public static final String c_item_id = "c_item_id";
    public static final String c_qty = "c_qty";
    public static final String c_total = "c_total";
    public static final String c_item_order_status = "c_item_order_status";

    //purchase table
    public static final String purchase_table = "purchase_table ";
    public static final String p_id = "p_id";
    public static final String p_supplier_id = "p_supplier_id";
    public static final String p_date = "p_date";
    public static final String p_description = "p_description";
    public static final String p_amount = "p_amount";
    public static final String p_upload_status = "p_upload_status";
    public static final String p_paid = "p_paid";
    public static final String p_bal = "p_bal";


    //supplier table
    public static final String supplier_table = "supplier_table ";
    public static final String supplier_id = "supplier_id";
    public static final String supplier_name = "supplier_name";
    public static final String supplier_address = "supplier_address";
    public static final String supplier_contact = "supplier_contact";
    public static final String supplier_upload_status = "supplier_upload_status";

    //customer table
    public static String customer_table="customer_table";
    public static String customer_id="customer_id";
    public static String customer_name="customer_name";
    public static String customer_contact="customer_contact";
    public static String customer_upload_status="customer_upload_status";

    //feedback_table
    public static String feedback_table="feedback_table";
    public static String feedback_id="feedback_id";
    public static String feedback_order_id="feedback_order_id";
    public static String ambience_rating="ambience_rating";
    public static String staff_rating="staff_rating";
    public static String feedback_review="feedback_review";
    public static String feedback_upload_status="feedback_upload_status";
    //expense table
    public static final String expense_table = "expense_table";
    public static final String e_id = "e_id";
    public static final String e_type = "e_type";
    public static final String e_amount = "e_amount";
    public static final String e_desc = "e_desc";
    public static final String e_date = "e_date";
    public static final String e_upload_status = "e_upload_status";

    //feedback_items_table
    public static String feedback_items_table="feedback_items_table";
    public static String feedback_id_id="feedback_id_id";//id of feedback
    public static String feedback_items_id="feedback_items_id";//id of feedbacking item id
    public static String rating="rating";
    public static String rating_upload_status="rating_upload_status";

    //employee table
    public static final String employee_table = "employee_table";
    public static final String emp_id = "emp_id";
    public static final String emp_name = "emp_name";
    public static final String emp_address = "emp_address";
    public static final String emp_contact = "emp_contact";
    public static final String emp_designation = "emp_designation";
    public static final String emp_status = "emp_status";
    public static final String emp_salary = "emp_salary";
    public static final String emp_doj = "emp_doj";
    public static final String emp_upload_status = "emp_upload_status";

    public static final String ORDER_CANCEL="0";
    public static final String ORDER_CART="1";
    public static final String ORDER_SENT="2";
    public static final String ORDER_FINISHED="3";

    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    public static final String CREATE_CAT_TABLE="CREATE TABLE `category` (`cat_id`INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,`cat_name` TEXT NOT NULL,`cat_type` TEXT NOT NULL);";
    public static final String CREATE_ITEM_TABLE="CREATE TABLE `item_table` (`item_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, `item_name` TEXT NOT NULL, `cat_cat_id` INTEGER NOT NULL, `item_price` REAL NOT NULL ,`image` REAL NOT NULL );";
    public static final String CREATE_CART_DETAILS_TABLE="CREATE TABLE `cart_details` (`cart_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,`cart_customer_id` REAL,`date` REAL NOT NULL,`time` REAL NOT NULL,`total` REAL NOT NULL , `cart_status` TEXT NOT NULL,`cart_type` TEXT);";
    public static final String CREATE_CART_ITEMS_TABLE="CREATE TABLE `cart_items_table` (`c_i_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,`cart_details_id` INTEGER NOT NULL,`c_item_id` INTEGER NOT NULL,`c_qty` INTEGER NOT NULL,`c_total` REAL NOT NULL,`c_item_order_status` TEXT NOT NUll );";
    public static final String CREATE_PURCHASE_TABLE="CREATE TABLE `purchase_table` (`p_id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, p_supplier_id REAL NOT NULL,`p_date` REAL NOT NULL,`p_description` REAL NOT NULL,`p_amount` REAL NOT NULL,`p_upload_status` TEXT NOT NULL );";
    public static final String CREATE_SUPPLIER_TABLE="CREATE TABLE `supplier_table` (`supplier_id` INTEGER NOT NULL,`supplier_name` TEXT NOT NULL,`supplier_address` TEXT NOT NULL,`supplier_contact` REAL,`supplier_upload_status` TEXT,PRIMARY KEY(`supplier_id`));";
    public static final String CREATE_CUSTOMER_TABLE="CREATE TABLE `customer_table` (\n" + "\t`customer_id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" + "\t`customer_name`\tREAL,\n" + "\t`customer_contact`\tREAL,\n" + "\t`customer_upload_status`\tREAL\n" + ");";
    public static final String CREATE_FEEDBACK_TABLE="CREATE TABLE `feedback_table` (\n" + "\t`feedback_id`\tINTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,\n" + "\t`feedback_order_id`\tINTEGER,\n" + "\t`ambience_rating`\tINTEGER,\n" + "\t`staff_rating`\tINTEGER,\n" + "\t`feedback_review`\tTEXT,\n" +"\t`feedback_upload_status`\tINTEGER\n" + ");";
    public static final String CREATE_ITEMS_TABLE="CREATE TABLE `feedback_items_table` (\n" + "\t`feedback_id_id`\tINTEGER,\n" + "\t`feedback_items_id`\tINTEGER,\n" + "\t`rating`\tINTEGER,\n" + "\t`rating_upload_status`\tINTEGER\n" + ");";
    public static final String CREATE_EXPENSE_TABLE = "CREATE TABLE `expense_table` (`e_id` INTEGER NOT NULL,`e_type` TEXT NOT NULL,`e_amount` TEXT NOT NULL,`e_desc` TEXT,e_date REAL,`e_upload_status` TEXT,PRIMARY KEY(`e_id`));";
    public static final String CREATE_EMPLOYEE_TABLE = "CREATE TABLE `employee_table` (`emp_id` INTEGER NOT NULL,`emp_name` TEXT NOT NULL,`emp_address`TEXT NOT NULL,`emp_contact` REAL,`emp_designation` TEXT NOT NULL,`emp_status` TEXT NOT NULL,`emp_salary` REAL,`emp_doj` TEXT NOT NULL,`emp_upload_status` TEXT NOT NULL,PRIMARY KEY(`emp_id`));";




    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(CREATE_CAT_TABLE);
        db.execSQL(CREATE_ITEM_TABLE);
        db.execSQL(CREATE_CART_DETAILS_TABLE);
        db.execSQL(CREATE_CART_ITEMS_TABLE);
        db.execSQL(CREATE_PURCHASE_TABLE);
        db.execSQL(CREATE_SUPPLIER_TABLE);
        db.execSQL(CREATE_CUSTOMER_TABLE);
        db.execSQL(CREATE_FEEDBACK_TABLE);
        db.execSQL(CREATE_ITEMS_TABLE);
        db.execSQL(CREATE_EXPENSE_TABLE);
        db.execSQL(CREATE_EMPLOYEE_TABLE);


    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS "+category);
        db.execSQL("DROP TABLE IF EXISTS "+item_table);
        db.execSQL("DROP TABLE IF EXISTS "+cart_details);
        db.execSQL("DROP TABLE IF EXISTS "+cart_items_table);
        db.execSQL("DROP TABLE IF EXISTS "+purchase_table);
        db.execSQL("DROP TABLE IF EXISTS "+supplier_table);
        db.execSQL("DROP TABLE IF EXISTS "+customer_table);
        db.execSQL("DROP TABLE IF EXISTS "+feedback_table);
        db.execSQL("DROP TABLE IF EXISTS "+feedback_items_table);
        db.execSQL("DROP TABLE IF EXISTS employee_table");
        db.execSQL("DROP TABLE IF EXISTS expense_table");
        db.execSQL("DROP TABLE IF EXISTS employee_table");

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